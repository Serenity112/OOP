package courseWork.gamemanager

import courseWork.gamemenu.view.MineSweeperBoard
import courseWork.mainmenu.view.MainMenuPanel

import javax.swing.*

enum class GameState() {
    MAINMENU,
    GAME,
}

data class Difficulty(val difficultyName: String, var rows: Int, var cols: Int, var mines: Int)

private const val GAME_WIDTH = 1200
private const val GAME_HEIGHT = 1000

class GameManager() : JFrame("Minesweeper.") {


    var gameDifficulty = Difficulty("Easy", 9, 9, 10)
        private set

    val difficultyPreset = arrayOf(
        Difficulty("Easy", 9, 9, 10),
        Difficulty("Medium", 16, 16, 40),
        Difficulty("Hard", 24, 24, 99)
    )

    private var state = GameState.MAINMENU

    init {
        this.isVisible = true
        setSize(GAME_WIDTH, GAME_HEIGHT)
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    fun changeConfiguration(difficultyName: String, rows: Int, cols: Int, mines: Int) {
        gameDifficulty = Difficulty(difficultyName, rows, cols, mines)
    }

    fun changeState(state: GameState) {
        this.state = state
        onStateChange()
    }

    private fun onStateChange() {
        rootPane.contentPane.removeAll()

        when (state) {
            GameState.MAINMENU -> {
                val menu = MainMenuPanel(this)
                rootPane.contentPane = menu.createStartingMenu()
                rootPane.contentPane.revalidate()
            }

            GameState.GAME -> {
                val game = MineSweeperBoard(this)
                rootPane.contentPane = game.createGamePanel()
                rootPane.contentPane.revalidate()
            }
        }
    }

    fun startGame() {
        changeState(GameState.MAINMENU)
    }
}