package laba4.model

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

data class MazeInitializer(
    val board: List<List<Cell>>,
    val playerPos: Pair<Int, Int>,
    val rows: Int,
    val cols: Int,
)


class MazeModel(_maze: MazeInitializer) {
    private var rows = _maze.rows
    private var cols = _maze.cols
    private var playerPos = _maze.playerPos
    private val board = MutableList(rows) { i -> MutableList(cols) { j -> _maze.board[i][j] } }


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