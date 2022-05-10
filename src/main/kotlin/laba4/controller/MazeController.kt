package laba4.controller
import laba4.model.MazeModel
import laba4.model.State.FINISHED

class MazeController(private val model: MazeModel) {

    init {
        startGame()
    }

    private fun startGame() {
        while(model.state != FINISHED) {
            val input = readln()

            try {
                model.doMove(input)
            } catch (e: Exception) {
                println(e.message)
            }
        }

        model.unInitializeMaze()

        println("Game finished :)))))")
    }
}