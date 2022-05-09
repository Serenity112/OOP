package courseWork.gamemenu.view
import courseWork.GameTextures
import courseWork.gamemenu.model.FieldData
import courseWork.gamemenu.model.*
import courseWork.gamemenu.model.ModelChangeListener
import java.awt.*
import javax.swing.*

private const val GAP = 10
private const val GAME_WIDTH = 700
private const val GAME_HEIGHT = 1000

class MineSweeperBoard(rows: Int, cols: Int, mines: Int): JFrame("Mineswepper."), ModelChangeListener {

    enum class Colors(val hex: Int) {
        GREY(0x878a9a)
    }

    private var gameModel = Model()

    private val statusLabel = JLabel("Status", JLabel.CENTER)
    //private val statusLabel = JLabel("Status", JLabel.CENTER)
    // private val statusLabel = JLabel("Status", JLabel.CENTER)


    private val buttons = mutableListOf<MutableList<JButton>>()
    private val textures = GameTextures.textures

    init {
        setSize(GAME_WIDTH, GAME_HEIGHT)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE
        //rootPane.contentPane = MainMenuPanel().createStartingMenu()

        rootPane.contentPane = createMainPanel()
        startGame()

    }

    private fun createMainPanel() : JPanel {
        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout()

        val gameGui = createGuiPanel()
        gameGui.add(createSwitchButton())
        gameGui.add(createRestartButton())
        mainPanel.add(gameGui, BorderLayout.NORTH)

        return mainPanel
    }

    private fun createGuiPanel(): JPanel {
        val guiPanel = JPanel()
        guiPanel.preferredSize = Dimension(700, 85)
        guiPanel.background = Color(Colors.GREY.hex)
        guiPanel.layout = FlowLayout(FlowLayout.CENTER)
        return guiPanel
    }

    private fun createSwitchButton(): JButton {
        val switchButton = JButton().apply {
            icon = when(gameModel.clickMode) {
                ClickMode.REVEALING -> ImageIcon(textures.MineTex.getScaledInstance(75, 75, Image.SCALE_SMOOTH))
                ClickMode.MARKING -> ImageIcon(textures.MarkTex.getScaledInstance(75, 75, Image.SCALE_SMOOTH))
            }

            addActionListener {
                gameModel.switchClickMode()
                icon = when(gameModel.clickMode) {
                    ClickMode.REVEALING -> ImageIcon(textures.MineTex.getScaledInstance(75, 75, Image.SCALE_SMOOTH))
                    ClickMode.MARKING -> ImageIcon(textures.MarkTex.getScaledInstance(75, 75, Image.SCALE_SMOOTH))
                }
            }

            preferredSize = Dimension(75, 75)
            minimumSize = Dimension(75, 75)
            maximumSize = Dimension(75, 75)
        }

        return switchButton
    }

    private fun createRestartButton() : JButton {
        val restartButton = JButton("RES").apply {
            addActionListener {
                //gameModel.SwitchMode()
            }
            preferredSize = Dimension(75, 75)
            minimumSize = Dimension(75, 75)
            maximumSize = Dimension(75, 75)
        }

        return restartButton
    }

    private fun startGame() {
        gameModel.placeMines("mines.txt")
        val gamePanel = JPanel()

        println("Start2!")

        gamePanel.background = Color.GRAY









        gamePanel.add(createBoardPanel(), BorderLayout.CENTER)


        resubscribe()
    }

    override fun onModelChanged() {
        updateGameUI()
    }

    private fun resubscribe() {
        gameModel.removeModelChangeListener(this)
        gameModel = Model()
        gameModel.placeMines("mines.txt")

        gameModel.addModelChangeListener(this)
        //updateGameUI()
    }

    private fun updateGameUI() {
        val state = gameModel.state
        statusLabel.text = state.textValue

        for ((i, buttonRow) in buttons.withIndex()) {
            for ((j, button) in buttonRow.withIndex()) {
                val cell = gameModel.gameBoard[i][j]


                button.icon = when (cell.field) {
                    FieldData.MINE -> ImageIcon(textures.MineTex)
                    FieldData.WATER -> ImageIcon(textures.FieldTex)
                    FieldData.EMPTY -> ImageIcon(textures.EmptyTex)
                    FieldData.MARK -> ImageIcon(textures.MarkTex)
                }

                //button.isEnabled = cell == Cell.EMPTY && state in GAME_NOT_FINISHED
                // button.foreground = when (cell) {
                //     Cell.X -> Color.BLUE
                //      Cell.O -> Color.RED
                //       Cell.EMPTY -> Color.BLACK
            }
        }
    }





    private fun createBoardPanel(): JPanel {
        val boardPanel = JPanel()

        val img: Image = textures.EmptyTex

        boardPanel.preferredSize = Dimension(700, 300)
        boardPanel.background = Color.DARK_GRAY
        boardPanel.layout = GridLayout(gameModel.sizeRows, gameModel.sizeCols, 0, 0)
        for (i in 0 until gameModel.sizeRows) {
            val buttonsRow = mutableListOf<JButton>()
            for (j in 0 until gameModel.sizeCols) {
                val cellButton = JButton("M")
                cellButton.icon = ImageIcon(img)
                cellButton.addActionListener {
                    gameModel.doMove(i, j)
                }
                buttonsRow.add(cellButton)
                boardPanel.add(cellButton)
                //updateFont(cellButton, 30.0f)
            }
            buttons.add(buttonsRow)
        }

        return boardPanel
    }
}