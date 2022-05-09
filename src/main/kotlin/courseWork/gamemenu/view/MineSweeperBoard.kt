package courseWork.gamemenu.view

import courseWork.GameTextures
import courseWork.GameTextures.textures
import courseWork.gamemenu.model.*
import courseWork.gamemenu.view.MineSweeperBoard.Colors.*
import java.awt.*
import javax.swing.*

private const val GAME_WIDTH = 700
private const val GAME_HEIGHT = 1000
private const val BOARD_PANEL_SIZE = 650


class MineSweeperBoard(private val rows: Int, private val cols: Int, private val mines: Int) : JFrame("Minesweeper."),
    ModelChangeListener {

    enum class Colors(val hex: Int) {
        GREY(0x878a9a),
        BLUE(0x3366ff),
        GREEN(0x33cc33),
        RED(0xff0000),
        DARK_BLUE(0x000066),
        DARK_RED(0x800000),
        DARK_PURPLE(0x660033),
        PURPLE(0x990099),
        BLACK(0x000000)
    }

    private var gameModel = Model(rows, cols, mines)

    private val buttons = mutableListOf<MutableList<JButton>>()
    private val textures = GameTextures.textures

    private val depthColors =
        mapOf(1 to BLUE, 2 to GREEN, 3 to RED, 4 to DARK_BLUE, 5 to DARK_RED, 6 to DARK_PURPLE, 7 to PURPLE, 8 to BLACK)

    private enum class FieldIcons(var img: ImageIcon) {
        Mine(ImageIcon()),
        Field(ImageIcon()),
        Mark(ImageIcon()),
        Water(ImageIcon()),
        Cross(ImageIcon())
    }

    var cellSize = 0
    var panelWidth = 0
    var panelHeight = 0

    init {
        setSize(GAME_WIDTH, GAME_HEIGHT)
        defaultCloseOperation = EXIT_ON_CLOSE

        if (cols > rows) {
            panelWidth = BOARD_PANEL_SIZE
            cellSize = BOARD_PANEL_SIZE/cols
            panelHeight = cellSize*rows
        } else {
            panelHeight = BOARD_PANEL_SIZE
            cellSize = BOARD_PANEL_SIZE/rows
            panelWidth = cellSize*cols
        }

        rootPane.contentPane = createMainPanel()

        FieldIcons.Mine.img = ImageIcon(GameTextures.textures.MineTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        FieldIcons.Field.img = ImageIcon(GameTextures.textures.FieldTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        FieldIcons.Mark.img = ImageIcon(GameTextures.textures.MarkTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        FieldIcons.Water.img = ImageIcon(GameTextures.textures.EmptyWaterTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        FieldIcons.Cross.img = ImageIcon(GameTextures.textures.crossTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))


        resubscribe()
    }

    override fun onModelChanged() {
        updateGameUI()
    }


    private fun createGuiPanel(): JPanel {
        val guiPanel = JPanel()
        guiPanel.preferredSize = Dimension(700, 85)
        guiPanel.background = Color(GREY.hex)
        guiPanel.layout = FlowLayout(FlowLayout.CENTER)
        return guiPanel
    }

    private fun createSwitchButton(): JButton {
        val switchButton = JButton().apply {
            icon = when (gameModel.clickMode) {
                ClickMode.REVEALING -> ImageIcon(textures.MineTex.getScaledInstance(75, 75, Image.SCALE_SMOOTH))
                ClickMode.MARKING -> ImageIcon(textures.MarkTex.getScaledInstance(75, 75, Image.SCALE_SMOOTH))
            }

            addActionListener {
                gameModel.switchClickMode()
                icon = when (gameModel.clickMode) {
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

    private fun createRestartButton(): JButton {
        val restartButton = JButton("RES").apply {
            addActionListener {
                restartGame()
            }
            preferredSize = Dimension(75, 75)
            minimumSize = Dimension(75, 75)
            maximumSize = Dimension(75, 75)
        }

        return restartButton
    }

    private fun restartGame() {

    }


    private fun createMainPanel(): JPanel {
        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout()

        val gameGui = createGuiPanel()
        gameGui.add(createSwitchButton())
        gameGui.add(createRestartButton())
        mainPanel.add(gameGui, BorderLayout.NORTH)

        mainPanel.add(createBoardPanel())

        return mainPanel
    }


    private fun resubscribe() {
        gameModel.removeModelChangeListener(this)
        gameModel = Model(rows, cols, mines)
        gameModel.placeMines("mines.txt")

        gameModel.addModelChangeListener(this)
        updateGameUI()
    }

    private fun updateGameUI() {
        for ((i, buttonRow) in buttons.withIndex()) {
            for ((j, button) in buttonRow.withIndex()) {
                val cell = gameModel.gameBoard[i][j]

                when (cell.field) {
                    FieldData.MINE -> {
                        button.apply {
                            background = Color(RED.hex)
                            icon = FieldIcons.Mine.img
                        }
                    }
                    FieldData.FIELD -> button.icon = FieldIcons.Field.img
                    FieldData.MARK -> button.icon = FieldIcons.Mark.img
                    FieldData.WATER -> {
                        val water: WaterField = cell as WaterField

                        if (water.depth == 0) {
                            button.icon = FieldIcons.Water.img
                        } else {
                            val label = (button.getComponent(0) as JLabel).apply {
                                text = water.depth.toString()
                                foreground = depthColors[water.depth]?.let { Color(it.hex) }
                            }
                            button.apply {
                                icon = FieldIcons.Field.img
                                horizontalTextPosition = JButton.CENTER
                                verticalTextPosition = JButton.CENTER
                                add(label)
                            }
                        }
                    }
                }

            }
        }

        // Final update if we lost the game!
        // Show all mines
        if (gameModel.state == State.LOSS) {
            gameModel.minesPositions.forEach {
                val cell = gameModel.gameBoard[it.first][it.second]
                if (cell.field != FieldData.MARK) {
                    val button = buttons[it.first][it.second]
                    button.apply {
                        //val label = JLabel()
                        background = Color(GREEN.hex)
                        icon = FieldIcons.Mine.img
                    }
                }
            }

            // Show wrong marks
            for ((i, buttonRow) in buttons.withIndex()) {
                for ((j, button) in buttonRow.withIndex()) {
                    val cell = gameModel.gameBoard[i][j]

                    if (cell.field == FieldData.MARK) {
                        if (gameModel.dataBoard[i][j].field != FieldData.MINE) {
                            button.apply {

                                val label = button.getComponent(0) as JLabel
                                label.apply {
                                    icon = FieldIcons.Cross.img
                                    // FIX
                                    BorderLayout.CENTER

                                }
                            }
                        }
                    }
                }
            }


        }
    }


    private fun createBoardPanel(): JPanel {
        val boardPanel = JPanel().apply {

            preferredSize = Dimension(panelWidth, panelHeight)
            maximumSize =  Dimension(panelWidth, panelHeight)
            minimumSize =  Dimension(panelWidth, panelHeight)
            background = Color(GREY.hex)
            // add constructor
            layout = GridLayout(rows, cols, 0, 0)
        }

        val img = FieldIcons.Field.img

        for (i in 0 until rows) {
            val buttonsRow = mutableListOf<JButton>()
            for (j in 0 until cols) {
                val cellButton = JButton().apply {
                    //layout = BorderLayout()
                    icon = img
                    val depthLabel = JLabel().apply {
                        alignmentX = CENTER_ALIGNMENT
                        updateFont(this, 40F)
                    }

                    //layout = FlowLayout()

                    add(depthLabel, BorderLayout.CENTER)

                    //val jLabel = JLabel()
                    addActionListener {
                        gameModel.doMove(i, j)
                    }
                    // layout = OverlayLayout(this)
                    // add(jLabel)
                    preferredSize = Dimension(cellSize, cellSize)
                    maximumSize = Dimension(cellSize, cellSize)
                    minimumSize = Dimension(cellSize, cellSize)

                }

                buttonsRow.add(cellButton)
                boardPanel.add(cellButton)
            }
            buttons.add(buttonsRow)
        }

        val staticPanel = JPanel().apply {
            layout = FlowLayout()
            background = Color(GREY.hex)
            add(boardPanel)
        }

        return staticPanel
    }

    private fun updateFont(component: JComponent, newFontSize: Float) {
        val font = component.font
        val derivedFont = font.deriveFont(newFontSize)
        component.font = derivedFont
    }
}