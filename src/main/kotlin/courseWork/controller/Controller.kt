package courseWork.controller

import courseWork.model.Model
import courseWork.model.State.*
import courseWork.model.GAME_FINISHED
import courseWork.view.*

class Controller(private val model: Model) {

        //model.chooseDifficultyLevel()

        //model.initializeBoard()

        //model.placeMines()

        //model.placeMines("mines.txt")

        //model.printDataBoard()

        /*while(model.state !in GAME_FINISHED) {
            println("Enter your move:")
            val (a, b) = readLine()!!.split(' ')

            if (a.toInt() == 100) {
                model.SwitchMode()
            } else {
                try {

                    println("Move: " + a + " " + b)
                    model.doMove(a.toInt(), b.toInt())
                    model.printDataBoard()
                    println()
                    model.printGameBoard()
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }
        println("Game finished :)))))")
    }*/
}