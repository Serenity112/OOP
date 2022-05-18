package courseWork.gamemenu.view

import courseWork.GameTextures
import courseWork.gamemanager.GameManager
import courseWork.gamemanager.GameState
import courseWork.gamemenu.model.*
import courseWork.guiutils.GuiUtils.updateFont
import courseWork.guiutils.GuiUtils.Colors.*

import java.awt.*
import javax.swing.*


private const val GAME_WIDTH = 700
private const val GAME_HEIGHT = 1000
private const val BOARD_PANEL_SIZE = 850

class MineSweeperBoard(private val manager: GameManager) : JFrame("Minesweeper."),
    ModelChangeListener {

    private val rows = manager.gameDifficulty.rows
    private val cols = manager.gameDifficulty.cols
    private val mines = manager.gameDifficulty.mines

    private var gameModel = Model(rows, cols, mines)
    private var bufferBoard = MutableList(rows) { MutableList(cols) { Field(FieldData.WATER) } }

    private val buttons = mutableListOf<MutableList<JButton>>()
    private val textures = GameTextures.textures

    private val depthColors =
        mapOf(1 to BLUE, 2 to GREEN, 3 to RED, 4 to DARK_BLUE, 5 to DARK_RED, 6 to DARK_PURPLE, 7 to PURPLE, 8 to BLACK)

    private enum class Fields {
        Mine,
        Field,
        Mark,
        Water,
        Cross,
    }

    private var cellSize = 0
    private var panelWidth = 0
    private var panelHeight = 0

    private val fieldIcons: MutableMap<Fields, ImageIcon> = mutableMapOf()

    private var minesLabel = JLabel()

    init {

        setSize(GAME_WIDTH, GAME_HEIGHT)
        defaultCloseOperation = EXIT_ON_CLOSE

        if (cols > rows) {
            panelWidth = BOARD_PANEL_SIZE
            cellSize = BOARD_PANEL_SIZE / cols
            panelHeight = cellSize * rows
        } else {
            panelHeight = BOARD_PANEL_SIZE
            cellSize = BOARD_PANEL_SIZE / rows
            panelWidth = cellSize * cols
        }

        fieldIcons[Fields.Mine] =
            ImageIcon(GameTextures.textures.MineTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        fieldIcons[Fields.Field] =
            ImageIcon(GameTextures.textures.FieldTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        fieldIcons[Fields.Mark] =
            ImageIcon(GameTextures.textures.MarkTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        fieldIcons[Fields.Water] =
            ImageIcon(GameTextures.textures.EmptyWaterTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))
        fieldIcons[Fields.Cross] =
            ImageIcon(GameTextures.textures.crossTex.getScaledInstance(cellSize, cellSize, Image.SCALE_SMOOTH))

        gameModel.addModelChangeListener(this)
        updateGameUI()
    }

    fun createGamePanel(): JPanel {
        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout()

        val gameGui = createGuiPanel()
        gameGui.add(createSwitchButton())
        gameGui.add(createRestartButton())
        gameGui.add(createBackButton())

        minesLabel = createLeftMinesLabel()
        gameGui.add(minesLabel)

        mainPanel.add(gameGui, BorderLayout.NORTH)

        mainPanel.add(createBoardPanel())

        updateGameUI()

        return mainPanel
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

            background = Color(LIGHT_GREY.hex)

            preferredSize = Dimension(75, 75)
            minimumSize = Dimension(75, 75)
            maximumSize = Dimension(75, 75)
        }

        return switchButton
    }

    private fun createRestartButton(): JButton {
        val restartButton = JButton().apply {
            icon = ImageIcon(textures.restart.getScaledInstance(60, 60, Image.SCALE_SMOOTH))
            background = Color(LIGHT_GREY.hex)

            addActionListener {
                if (gameModel.movesLeft != gameModel.size) {
                    val dialogOption = JOptionPane.showConfirmDialog(
                        this,
                        "Game not finished, are you sure want to restart?",
                        "Restart",
                        JOptionPane.OK_CANCEL_OPTION
                    )

                    if (dialogOption == JOptionPane.OK_OPTION) {
                        resubscribe()
                    }
                } else {
                    resubscribe()
                }
            }
            preferredSize = Dimension(75, 75)
            minimumSize = Dimension(75, 75)
            maximumSize = Dimension(75, 75)
        }

        return restartButton
    }

    private fun createBackButton(): JButton {
        val restartButton = JButton().apply {
            icon = ImageIcon(textures.backButton.getScaledInstance(60, 60, Image.SCALE_SMOOTH))
            background = Color(LIGHT_GREY.hex)

            addActionListener {
                if (gameModel.movesLeft != gameModel.size) {
                    val dialogOption = JOptionPane.showConfirmDialog(
                        this,
                        "Game not finished, are you sure want to return to menu?",
                        "Return",
                        JOptionPane.OK_CANCEL_OPTION
                    )

                    if (dialogOption == JOptionPane.OK_OPTION) {
                        manager.changeState(GameState.MAINMENU)
                    }
                } else {
                    manager.changeState(GameState.MAINMENU)
                }
            }
            preferredSize = Dimension(75, 75)
            minimumSize = Dimension(75, 75)
            maximumSize = Dimension(75, 75)
        }

        return restartButton
    }

    private fun createLeftMinesLabel(): JLabel {
        val minesLabel = JLabel().apply {
            updateFont(this, 40F)

            text = mines.toString()

            isOpaque = true
            foreground = Color(BLACK.hex)
            background = Color(LIGHT_GREY.hex)

            preferredSize = Dimension(75, 75)
            minimumSize = Dimension(75, 75)
            maximumSize = Dimension(75, 75)
        }

        return minesLabel
    }

    private fun updateMinesLabel() {
        minesLabel.text = gameModel.minesLeft.toString()
    }

    private fun createFinishedPanel(modelState: ModelState) {
        val finishText: String =
            when (modelState) {
                ModelState.WIN -> "Congratulations! You win!"
                ModelState.LOSS -> "Chel kak zhe ti slab))))"
                else -> ""
            }

        val dialogOption = JOptionPane.showConfirmDialog(
            this,
            finishText,
            "Game finished!",
            JOptionPane.OK_OPTION
        )

        //fix
        manager.changeState(GameState.MAINMENU)
    }

    private fun resubscribe() {
        gameModel.removeModelChangeListener(this)
        manager.changeState(GameState.GAME)
        gameModel.addModelChangeListener(this)
        updateGameUI()
    }

    private fun updateGameUI() {
        for ((i, buttonRow) in buttons.withIndex()) {
            for ((j, button) in buttonRow.withIndex()) {
                val cell = gameModel.gameBoard[i][j]

                if (cell != bufferBoard[i][j]) {
                    when (cell.field) {
                        FieldData.MINE -> {
                            button.apply {
                                // Removing text in case of "Replace mine" proc
                                text = ""
                                background = Color(RED.hex)
                                icon = fieldIcons[Fields.Mine]
                            }
                        }

                        FieldData.FIELD -> {
                            button.apply {
                                icon = fieldIcons[Fields.Field]
                            }

                        }
                        FieldData.MARK -> {
                            button.apply {
                                icon = fieldIcons[Fields.Mark]
                            }
                        }
                        FieldData.WATER -> {
                            val water: WaterField = cell as WaterField

                            if (water.depth == 0) {
                                button.icon = fieldIcons[Fields.Water]
                            } else {
                                button.apply {
                                    icon = fieldIcons[Fields.Field]
                                    text = water.depth.toString()
                                    foreground = depthColors[water.depth]?.let { Color(it.hex) }
                                }
                            }
                        }
                    }

                    if (cell.field == FieldData.WATER) {
                        bufferBoard[i][j] = WaterField((cell as WaterField).depth)
                    } else {
                        bufferBoard[i][j] = Field(cell.field)
                    }
                }
            }
        }

        updateMinesLabel()

        if (gameModel.state == ModelState.WIN) {
            createFinishedPanel(gameModel.state)
        }

        // Final update if we lost the game!
        if (gameModel.state == ModelState.LOSS) {
            updatePostGameUI()
            createFinishedPanel(gameModel.state)
        }
    }

    private fun updatePostGameUI() {
        // Show all mines
        gameModel.minesPositions.forEach {
            val cell = gameModel.gameBoard[it.first][it.second]
            if (cell.field != FieldData.MARK) {
                val button = buttons[it.first][it.second]
                button.apply {
                    background = Color.GRAY
                    icon = fieldIcons[Fields.Mine]
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
                            icon = fieldIcons[Fields.Cross]
                        }
                    }
                }
            }
        }
    }

    private fun createBoardPanel(): JPanel {
        val boardPanel = JPanel().apply {

            preferredSize = Dimension(panelWidth, panelHeight)
            maximumSize = Dimension(panelWidth, panelHeight)
            minimumSize = Dimension(panelWidth, panelHeight)
            background = Color(GREY.hex)
            layout = GridLayout(rows, cols, 0, 0)
        }

        for (i in 0 until rows) {
            val buttonsRow = mutableListOf<JButton>()
            for (j in 0 until cols) {
                val cellButton = JButton().apply {
                    horizontalTextPosition = JButton.CENTER
                    verticalTextPosition = JButton.CENTER

                    updateFont(this, cellSize * 0.7.toFloat())
                    text = ""

                    border = BorderFactory.createEmptyBorder()

                    preferredSize = Dimension(cellSize, cellSize)
                    maximumSize = Dimension(cellSize, cellSize)
                    minimumSize = Dimension(cellSize, cellSize)


                    addActionListener {
                        gameModel.doMove(i, j)
                    }
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
}