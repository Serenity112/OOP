package laba4.controller
import laba4.model.MazeModel
import laba4.model.Move
import laba4.model.State.FINISHED

class MazeController(private val model: MazeModel) {

    init {
        startGame()
    }

    private fun startGame() {
        while(model.state != FINISHED) {

            val move = when (readln()) {
                "W" -> Move.UP
                "A" -> Move.LEFT
                "S" -> Move.DOWN
                "D" -> Move.RIGHT
                else -> Move.STAY
            }

            try {
                model.doMove(move)
            } catch (e: Exception) {
                println(e.message)
            }
        }

        println("Game finished :)))))")
    }
}