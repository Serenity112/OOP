package laba4

import laba4.controller.MazeController
import laba4.model.MazeModel
import laba4.view.MazeView

fun main() {
    val newMaze = MazeModel("maze.txt")

    MazeView(newMaze)
    MazeController(newMaze)
}