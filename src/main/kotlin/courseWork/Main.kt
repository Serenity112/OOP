package courseWork

import courseWork.gamemenu.view.MineSweeperBoard
import courseWork.mainmenu.view.MainMenuPanel

fun main() {
    //val model = Model()

    //val controller = Controller(model)

   // controller.startGame()

    //val mineSweeper = MainMenuPanel()
    val mineSweeper = MineSweeperBoard(9, 9, 16)
    mineSweeper.isVisible = true
}