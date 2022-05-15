package courseWork

import courseWork.gamemenu.view.MineSweeperBoard
import courseWork.mainmenu.view.MainMenuPanel
import javax.swing.*

fun main() {

    val game = MineSweeperBoard(1, 1, 1)
    val mainMenu = MainMenuPanel(game)

    mainMenu.isVisible = true
    mainMenu.initialize()

}
