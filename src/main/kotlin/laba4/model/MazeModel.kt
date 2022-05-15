package laba4.model

import java.io.File
import laba4.model.State.*
import laba4.model.Cell.*

// We use (i, j) move logic. Means we have YoX coordinates
enum class Move(val y: Int, val x: Int) {
    UP(-1, 0),
    RIGHT(0, 1),
    DOWN(1, 0),
    LEFT(0, -1),
    STAY(0, 0),
}

enum class Cell(val textValue: String) {
    PLAYER("P"),
    WALL("#"),
    TRACE("-"),
    EXIT("E"),
}

enum class State {
    FINISHED,
    PROGRESS,
}

interface ModelChangeListener {
    fun onModelChanged()
}

class Maze() {
    var board: MutableList<MutableList<Cell>> = MutableList(0) { MutableList(0) { WALL } }
    var playerPos: Pair<Int, Int> = Pair(-1, -1)
    var rows = 0
    var cols = 0
}

fun readMazeFromFile(fileName: String): Maze {
    val maze = Maze()

    maze.apply {
        File(fileName).forEachLine { it ->
            val mazeLine: MutableList<Cell> = mutableListOf()

            it.forEach {
                val currentCell = when (it) {
                    '#' -> WALL
                    '-' -> TRACE
                    'P' -> PLAYER
                    'E' -> EXIT
                    else -> WALL
                }

                if (currentCell == PLAYER)
                    playerPos = Pair(board.size, mazeLine.size)

                mazeLine.add(mazeLine.size, currentCell)
            }
            board.add(board.size, mazeLine)
        }
    }

    // Board size validation
    val tempCols = maze.board[0].size
    require(maze.board.all { it.size == tempCols }) { "Different rows size! Maze needs to be a rectangle." }

    maze.rows = maze.board.size
    maze.cols = maze.board[0].size

    // Player validation
    require(maze.playerPos != Pair(-1, -1)) { "No player found!" }

    return maze
}

class MazeModel(_maze: Maze) {
    var board: MutableList<MutableList<Cell>> = _maze.board
    private var rows = _maze.rows
    private var cols = _maze.cols
    private var playerPos = _maze.playerPos

    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    var state: State = PROGRESS
        private set

    fun addModelChangeListener(listener: ModelChangeListener) {
        listeners.add(listener)
    }

    fun removeModelChangeListener(listener: ModelChangeListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.onModelChanged() }
    }

    fun doMove(move: Move) {
        require(state != FINISHED) { "Maze was completed! Play new game please :(" }

        val newPosI = playerPos.first + move.y
        val newPosJ = playerPos.second + move.x

        require(newPosI in 0..rows && newPosJ in 0..cols) { "Move was out of maze!" }

        require(board[newPosI][newPosJ] != WALL) { "Move in wall!" }

        if (board[newPosI][newPosJ] == EXIT)
            state = FINISHED

        board[playerPos.first][playerPos.second] = TRACE
        board[newPosI][newPosJ] = PLAYER

        playerPos = Pair(newPosI, newPosJ)

        notifyListeners()
    }

    override fun toString(): String {
        return if (board.size != 0) buildString {
            board.forEach { it ->
                it.forEach {
                    append(it.textValue)
                }
                append('\n')
            }
        } else "Empty maze!"
    }
}