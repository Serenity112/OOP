package courseWork.view

import courseWork.model.*
import java.awt.*
import javax.imageio.ImageIO
import javax.swing.*
import courseWork.model.FieldData.*
import javax.swing.BoxLayout.*
import java.awt.Image.SCALE_SMOOTH
import kotlin.math.abs

private const val GAP = 10

private const val GAME_WIDTH = 700
private const val GAME_HEIGHT = 1000

enum class Colors(val hex: Int) {
    GREY(0x878a9a)
}

class JSMinesweeperView : JFrame("Mineswepper."), ModelChangeListener {
    class Textures(
        val MineTex: Image,
        val FieldTex: Image,
        val MarkTex: Image,
        val EmptyTex: Image,
        val logoTex: Image,
        val arrowLeft: Image,
        val arrowRight: Image,
        val bottomLeft: Image,
        val bottomRight: Image,
        val goButton: Image,
        val settingsButton: Image,
    )

    private var gameModel = Model()

    private val statusLabel = JLabel("Status", JLabel.CENTER)
    //private val statusLabel = JLabel("Status", JLabel.CENTER)
    // private val statusLabel = JLabel("Status", JLabel.CENTER)


    private val buttons = mutableListOf<MutableList<JButton>>()
    private val textures = loadTextures()

    data class Difficulty(val difficulty: String, val rows: Int, val cols: Int, val mines: Int)

    private val difficultyPreset = arrayOf(
        Difficulty("Easy", 9, 9, 10),
        Difficulty("Medium", 16, 16, 40),
        Difficulty("Hard", 24, 24, 99)
    )

    private var difficulty: Int = 0
    private var rows: Int = 0
    private var cols: Int = 0
    private var mines: Int = 0

    private fun loadTextures(): Textures {
        return Textures(

            ImageIO.read(javaClass.getResource("/images/mine.png")).getScaledInstance(100, 100, SCALE_SMOOTH),

            ImageIO.read(javaClass.getResource("/images/field.png")).getScaledInstance(200, 200, SCALE_SMOOTH),

            ImageIO.read(javaClass.getResource("/images/flag.png")).getScaledInstance(100, 100, SCALE_SMOOTH),
            ImageIO.read(javaClass.getResource("/images/empty.png")).getScaledInstance(100, 100, SCALE_SMOOTH),
            ImageIO.read(javaClass.getResource("/images/logo.png")).getScaledInstance(700, 100, SCALE_SMOOTH),

            ImageIO.read(javaClass.getResource("/images/arrowleft.png")).getScaledInstance(50, 50, SCALE_SMOOTH),
            ImageIO.read(javaClass.getResource("/images/arrowright.png")).getScaledInstance(50, 50, SCALE_SMOOTH),
            ImageIO.read(javaClass.getResource("/images/bottomleft.png")).getScaledInstance(200, 300, SCALE_SMOOTH),
            ImageIO.read(javaClass.getResource("/images/bottomright.png")).getScaledInstance(200, 300, SCALE_SMOOTH),

            ImageIO.read(javaClass.getResource("/images/gobutton.png")).getScaledInstance(200, 200, SCALE_SMOOTH),
            ImageIO.read(javaClass.getResource("/images/button.png")).getScaledInstance(250, 50, SCALE_SMOOTH),
        )
    }

    init {
        setSize(GAME_WIDTH, GAME_HEIGHT)
        defaultCloseOperation = EXIT_ON_CLOSE
        rootPane.contentPane = createStartingMenu()

    }

    private fun createStartingMenu(): JPanel {
        // Main menu panel
        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout()

        // Logo panel
        val logoLabel = JLabel(ImageIcon(textures.logoTex))
        val logo = JPanel().apply {
            background = Color(Colors.GREY.hex)
            preferredSize = Dimension(GAME_WIDTH, 100)
            add(logoLabel)
        }
        mainPanel.add(logo, BorderLayout.NORTH)




        // Settings panel
        val settingsPanel = JPanel().apply {
            layout = BoxLayout(this, Y_AXIS)
            background = Color.GRAY
            preferredSize = Dimension(GAME_WIDTH, 500)
        }
        mainPanel.add(settingsPanel)

        // Exact settings on settings panel

        val startButton = JButton().apply {
            addActionListener {
                startGame()
            }
            preferredSize = Dimension(200, 200)
            maximumSize = Dimension(200, 200)
            minimumSize = Dimension(200, 200)
            alignmentX = CENTER_ALIGNMENT
            icon = ImageIcon(textures.goButton)
        }

        val difficultyLabel = JLabel().apply {
            this.alignmentX = CENTER_ALIGNMENT
            this.alignmentY = CENTER_ALIGNMENT
            text = "Easy"
            updateFont(this, 40F)
            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }

        val rowLabel = JLabel().apply {
            alignmentX = CENTER_ALIGNMENT
            text = "0"
            updateFont(this, 40F)
            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }

        val colLabel = JLabel().apply {
            alignmentX = CENTER_ALIGNMENT
            text = "0"
            //updateFont(this, 40F)
            maximumSize = Dimension(50, 50)
            minimumSize = Dimension(50, 50)
            preferredSize = Dimension(50, 50)
        }

        val minesLabel = JLabel().apply {
            alignmentX = CENTER_ALIGNMENT
            text = "0"
            //updateFont(this, 40F)
            maximumSize = Dimension(50, 50)
            minimumSize = Dimension(50, 50)
            preferredSize = Dimension(50, 50)
        }



        val difficultyPanel = JPanel().apply {
            layout = BoxLayout(this, X_AXIS)
            //alignmentX = CENTER_ALIGNMENT
            background = Color(Colors.GREY.hex)


            val buttonLeft = JButton().apply {
                icon = ImageIcon(textures.arrowLeft)
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
                addActionListener {
                    difficulty--
                    difficultyLabel.text = difficultyPreset[abs(difficulty % 3)].difficulty
                }
            }
            val buttonRight = JButton().apply {
                icon = ImageIcon(textures.arrowRight)
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
                addActionListener {
                    difficulty++
                    difficultyLabel.text = difficultyPreset[abs(difficulty % 3)].difficulty
                }
            }

            add(buttonLeft)
            add(difficultyLabel)
            add(buttonRight)
        }


        val rowsPanel = JPanel().apply {
            layout = BoxLayout(this, X_AXIS)
            background = Color(Colors.GREY.hex)

            val buttonLeft = JButton().apply {
                icon = ImageIcon(textures.arrowLeft)
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
            }
            val buttonRight = JButton().apply {
                icon = ImageIcon(textures.arrowRight)
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
            }


            add(buttonLeft)
            add(rowLabel)
            add(buttonRight)
        }


        val colsPanel = JPanel().apply {
            layout = BoxLayout(this, X_AXIS)
            alignmentX = CENTER_ALIGNMENT
            val buttonLeft2 = JButton("Left")
            val buttonRight2 = JButton("Right")
            add(buttonLeft2)
            add(colLabel)
            add(buttonRight2)
        }


        val minesPanel = JPanel().apply {
            layout = BoxLayout(this, X_AXIS)
            alignmentX = CENTER_ALIGNMENT
            val buttonLeft3 = JButton("Left")
            val buttonRight3 = JButton("Right")
            add(buttonLeft3)
            add(minesLabel)
            add(buttonRight3)
        }

//        val rowsLabel = JLabel("Rows:").apply {
//            alignmentX = CENTER_ALIGNMENT
//        }
//        val colsLabel = JLabel("Cols:").apply {
//            alignmentX = CENTER_ALIGNMENT
//        }
//        val minesLabel = JLabel("Mines:").apply {
//            alignmentX = CENTER_ALIGNMENT
//        }


        // Applying settings to panel
        settingsPanel.apply {
            add(Box.createRigidArea(Dimension(0, 30)))
            add(startButton)
            add(Box.createRigidArea(Dimension(0, 30)))
            add(difficultyPanel)
            add(Box.createRigidArea(Dimension(0, 10)))
            //add(rowsLabel)
            add(rowsPanel)
            add(Box.createRigidArea(Dimension(0, 10)))
            //add(colsLabel)
            add(colsPanel)
            add(Box.createRigidArea(Dimension(0, 10)))
            //add(minesLabel)
            add(minesPanel)
        }

        // mainPanel.getComponent(0).background = Color.RED

        // Bottom panel
        val bottomPanel = JPanel().apply {
            layout = BorderLayout()
            preferredSize = Dimension(GAME_WIDTH, 300)
            background = Color(Colors.GREY.hex)
        }
        mainPanel.add(bottomPanel, BorderLayout.SOUTH)

        // Bottom corners
        bottomPanel.add(JLabel(ImageIcon(textures.bottomLeft)), BorderLayout.WEST)
        bottomPanel.add(JLabel(ImageIcon(textures.bottomRight)), BorderLayout.EAST)

        return mainPanel
    }

//    private fun createLeftButton() : JButton {
//        val button = JButton()
//    }

    private fun startGame() {
        rootPane.contentPane.removeAll()
        rootPane.contentPane.revalidate()

        gameModel.placeMines("mines.txt")
        val gamePanel = JPanel()

        println("Start2!")
        gamePanel.layout = BorderLayout(GAP, GAP)
        gamePanel.border = BorderFactory.createEmptyBorder(GAP, GAP, GAP, GAP)
        gamePanel.background = Color.GRAY

        rootPane.contentPane = gamePanel


        val gameGui = createGuiPanel()
        gameGui.add(createSwitchButton())

        gamePanel.add(gameGui, BorderLayout.NORTH)

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
                    MINE -> ImageIcon(textures.MineTex)
                    WATER -> ImageIcon(textures.FieldTex)
                    EMPTY -> ImageIcon(textures.EmptyTex)
                    MARK -> ImageIcon(textures.MarkTex)
                }

                //button.isEnabled = cell == Cell.EMPTY && state in GAME_NOT_FINISHED
                // button.foreground = when (cell) {
                //     Cell.X -> Color.BLUE
                //      Cell.O -> Color.RED
                //       Cell.EMPTY -> Color.BLACK
            }
        }
    }


    private fun createSwitchButton(): JButton {
        val switchButton = JButton("Switch")
        switchButton.addActionListener {
            gameModel.SwitchMode()
        }
        switchButton.preferredSize = Dimension(75, 75)
        return switchButton
    }

    private fun createGuiPanel(): JPanel {
        val guiPanel = JPanel()
        guiPanel.preferredSize = Dimension(700, 85)
        guiPanel.background = Color.DARK_GRAY
        guiPanel.layout = FlowLayout(FlowLayout.CENTER)
        return guiPanel
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
                updateFont(cellButton, 30.0f)
            }
            buttons.add(buttonsRow)
        }

        return boardPanel
    }


    private fun updateFont(component: JComponent, newFontSize: Float) {
        val font = component.font
        val derivedFont = font.deriveFont(newFontSize)
        component.font = derivedFont
    }
}

