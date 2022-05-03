package laba4.model

import java.io.File
import laba4.model.State.*
import laba4.model.Cell.*

//operator fun Pair<Int, Int>.plus(other: Pair<Int, Int>) = Pair(first + other.first, second + other.second)

// We use (i, j) move logic. Means we have YoX coordinates
enum class Move(val direction: Pair<Int, Int>) {
    UP(Pair(-1, 0)),
    RIGHT(Pair(0, 1)),
    DOWN(Pair(1, 0)),
    LEFT(Pair(0, -1)),
    STAY(Pair(0, 0)),
}

enum class Cell(val textValue: String) {
    PLAYER("P"),
    WALL("#"),
    TRACE("-"),
    EXIT("E"),
}

enum class State(val textValue: String) {
    NO_MAZE("Maze is not initialized"),
    FINISHED("Game finished, you won!"),
    PROGRESS("Game in progress, keep trying!"),
}

interface ModelChangeListener {
    fun onModelChanged()
}

class MazeModel {
    private var _maze: MutableList<MutableList<Cell>> = MutableList(0) { MutableList(0) { WALL } }
    private var rows = 0
    private var cols = 0
    private var currentPos: Pair<Int, Int> = Pair(-1, -1)

    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    var state: State = NO_MAZE
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

    fun unInitializeMaze() {
        require(state != NO_MAZE) { "Maze was not initialized!" }

        state = NO_MAZE
        _maze = MutableList(0) { MutableList(0) { WALL } }
        rows = 0
        cols = 0
        currentPos = Pair(-1, -1)
    }

    fun initializeMaze(fileName: String): MutableList<MutableList<Cell>> {
        require(state == NO_MAZE) { "Maze already initialized" }
        state = PROGRESS

        val file = File(fileName).forEachLine { it ->
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
                    currentPos = Pair(_maze.size, mazeLine.size)

                mazeLine.add(mazeLine.size, currentCell)
            }
            _maze.add(_maze.size, mazeLine)
        }
        rows = _maze[0].size
        cols = _maze.size

        require(currentPos != Pair(-1, -1)) { "No player found!" }

        return _maze
    }

    fun doMove(inputMove: String) {
        require(state != NO_MAZE) { "Maze is not initialized" }
        require(state != FINISHED) { "Maze was completed! Play new game please :(" }

        val move = when (inputMove) {
            "W" -> Move.UP
            "A" -> Move.LEFT
            "S" -> Move.DOWN
            "D" -> Move.RIGHT
            else -> Move.STAY
        }

        val newPosI = currentPos.first + move.direction.first
        val newPosJ = currentPos.second + move.direction.second

        require(newPosI in 0..rows && newPosJ in 0..cols) { "Move was out of maze!" }

        require(_maze[newPosI][newPosJ] != WALL) { "Move in wall!" }

        if (_maze[newPosI][newPosJ] == EXIT)
            state = FINISHED

        _maze[currentPos.first][currentPos.second] = TRACE
        _maze[newPosI][newPosJ] = PLAYER

        currentPos = Pair(newPosI, newPosJ)

        notifyListeners()
    }

    override fun toString(): String {
        return if (_maze.size != 0) buildString {
            _maze.forEach { it ->
                it.forEach {
                    append(it.textValue)
                }
                append('\n')
            }
        } else "Empty maze!"
    }
}