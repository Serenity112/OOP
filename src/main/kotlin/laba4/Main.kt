package laba4

import laba4.controller.MazeController
import laba4.model.MazeModel
import laba4.model.readMazeFromFile
import laba4.view.MazeView

fun main() {
    try {
        val newMaze = readMazeFromFile("maze.txt")
        val newModel = MazeModel(newMaze)

        MazeView(newModel)
        MazeController(newModel)
    } catch (e: Exception) {
        println(e.message)
    }
}