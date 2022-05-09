package courseWork.gamemenu.controller

import courseWork.gamemenu.model.Model
import courseWork.gamemenu.model.State.*
import courseWork.gamemenu.model.GAME_FINISHED

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