package laba4.model
import java.io.File

object FileProcessingUtil {
    fun readMazeFromFile(fileName: String): MazeInitializer {
        var playerPos = Pair(-1, -1)
        val board =  MutableList(0) { MutableList(0) { Cell.WALL } }

        File(fileName).forEachLine { it ->
            val mazeLine: MutableList<Cell> = mutableListOf()

            it.forEach {
                val currentCell = when (it) {
                    '#' -> Cell.WALL
                    '-' -> Cell.TRACE
                    'P' -> Cell.PLAYER
                    'E' -> Cell.EXIT
                    else -> Cell.WALL
                }

                if (currentCell == Cell.PLAYER)
                    playerPos = Pair(board.size, mazeLine.size)

                mazeLine.add(mazeLine.size, currentCell)
            }
            board.add(board.size, mazeLine)
        }
        // Board size validation
        val tempCols = board[0].size
        require(board.all { it.size == tempCols }) { "Different rows size! Maze needs to be a rectangle." }

        // Player validation
        require(playerPos != Pair(-1, -1)) { "No player found!" }

        return MazeInitializer(board, playerPos, board.size, board[0].size)
    }
}