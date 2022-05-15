package courseWork.mainmenu.view


import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.*
import kotlin.math.abs

import courseWork.GameTextures
import courseWork.gamemenu.view.MineSweeperBoard
import courseWork.mainmenu.model.*
import java.awt.Image

private const val GAME_WIDTH = 700
private const val GAME_HEIGHT = 1000

private const val MAX_ROWS = 99
private const val MIN_ROWS = 1

private const val MAX_COLS = 99
private const val MIN_COLS = 1

private const val MAX_MINES = 99
private const val MIN_MINES = 1

class MainMenuPanel(var game: MineSweeperBoard) : JFrame("Minesweeper.") {
    data class Difficulty(val diffname: String, val rows: Int, val cols: Int, val mines: Int)

    private val difficultyPreset = arrayOf(
        Difficulty("Easy", 9, 9, 10),
        Difficulty("Medium", 16, 16, 40),
        Difficulty("Hard", 24, 24, 99)
    )

    enum class Colors(val hex: Int) {
        GREY(0x878a9a)
    }

    private var difficulty: Int = 0
    private var rows: Int = 1
    private var cols: Int = 1
    private var mines: Int = 1

    private var difficultyLabel = JLabel()
    private var rowsLabel = JLabel()
    private var colsLabel = JLabel()
    private var minesLabel = JLabel()


    private val textures = GameTextures.textures

    init {

    }

    public fun initialize() {
        setSize(GAME_WIDTH, GAME_HEIGHT)
        defaultCloseOperation = JFrame.EXIT_ON_CLOSE

        rootPane.contentPane = createStartingMenu()

        updateDifficulty(0)
        updateLabels()
        // Set easy difficulty

    }

     fun startGame() {
        game = MineSweeperBoard(rows, cols, mines)
        game.initialize()
        game.isVisible = true
    }

    private fun createStartButton(): JButton {
        val startButton = JButton().apply {
            addActionListener {
                // FIX!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                //val game = MineSweeperBoard(rows, cols, mines)
                //game.isVisible = true
                startGame()
                println("Start!")
            }
            preferredSize = Dimension(200, 200)
            maximumSize = Dimension(200, 200)
            minimumSize = Dimension(200, 200)
            alignmentX = JFrame.CENTER_ALIGNMENT
            icon = ImageIcon(textures.goButton.getScaledInstance(200, 200, Image.SCALE_SMOOTH))
        }

        return startButton
    }

    private fun createLogoPanel(): JPanel {
        val logoLabel = JLabel(ImageIcon(textures.logoTex.getScaledInstance(700, 100, Image.SCALE_SMOOTH)))
        val logo = JPanel().apply {
            background = Color(Colors.GREY.hex)
            preferredSize = Dimension(GAME_WIDTH, 100)
            add(logoLabel)
        }
        return logo
    }

    private fun createSettingsPanel(): JPanel {
        val settingsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = Color.GRAY
            preferredSize = Dimension(GAME_WIDTH, 500)
        }
        return settingsPanel
    }

    private fun createArrowButton(): JButton {
        val button = JButton().apply {
            maximumSize = Dimension(50, 50)
            minimumSize = Dimension(50, 50)
            preferredSize = Dimension(50, 50)
        }
        return button
    }

    private fun createDifficultyPanel(): JPanel {
        val difficultyLabel = JLabel().apply {
            this.alignmentX = JFrame.CENTER_ALIGNMENT
            this.alignmentY = JFrame.CENTER_ALIGNMENT
            updateFont(this, 40F)
            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }

        this.difficultyLabel = difficultyLabel

        val difficultyPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            background = Color(Colors.GREY.hex)

            val buttonLeft = createArrowButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    difficulty--
                    updateDifficulty(difficulty)
                    updateLabels()
                }
            }

            val buttonRight = createArrowButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    difficulty++
                    updateDifficulty(difficulty)
                    updateLabels()
                }
            }

            add(buttonLeft)
            add(difficultyLabel)
            add(buttonRight)
        }

        return difficultyPanel
    }

    private fun createRowsPanel(): JPanel {
        val rowsLabel = JLabel().apply {
            this.alignmentX = JFrame.CENTER_ALIGNMENT
            this.alignmentY = JFrame.CENTER_ALIGNMENT
            updateFont(this, 40F)
            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }
        this.rowsLabel = rowsLabel

        val rowsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            background = Color(Colors.GREY.hex)

            val buttonLeft = createArrowButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (rows > MIN_ROWS) {
                        updateDifficulty("Custom")
                        rows--
                        updateLabels()
                    }
                }
            }
            val buttonRight =createArrowButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (rows < MAX_ROWS) {
                        updateDifficulty("Custom")
                        rows++
                        updateLabels()
                    }
                }
            }


            add(buttonLeft)
            add(rowsLabel)
            add(buttonRight)
        }

        return rowsPanel
    }

    private fun createColsPanel(): JPanel {
        val colsLabel = JLabel().apply {
            this.alignmentX = JFrame.CENTER_ALIGNMENT
            this.alignmentY = JFrame.CENTER_ALIGNMENT
            updateFont(this, 40F)
            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }
        this.colsLabel = colsLabel

        val colsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = JFrame.CENTER_ALIGNMENT

            val buttonLeft = createArrowButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (cols > MIN_COLS) {
                        updateDifficulty("Custom")
                        cols--
                        updateLabels()
                    }
                }
            }
            val buttonRight = createArrowButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (cols < MAX_COLS) {
                        updateDifficulty("Custom")
                        cols++
                        updateLabels()
                    }
                }
            }

            add(buttonLeft)
            add(colsLabel)
            add(buttonRight)
        }

        return colsPanel
    }

    private fun createMinesPanel(): JPanel {
        val minesLabel = JLabel().apply {
            this.alignmentX = JFrame.CENTER_ALIGNMENT
            this.alignmentY = JFrame.CENTER_ALIGNMENT
            updateFont(this, 40F)
            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }
        this.minesLabel = minesLabel


        val minesPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = JFrame.CENTER_ALIGNMENT

            val buttonLeft = JButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
                addActionListener {
                    if (mines > MIN_MINES) {
                        updateDifficulty("Custom")
                        mines--
                        updateLabels()
                    }
                }
            }
            val buttonRight = JButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
                addActionListener {
                    if (mines < MAX_MINES ) {
                        updateDifficulty("Custom")
                        mines++
                        updateLabels()
                    }
                }
            }

            add(buttonLeft)
            add(minesLabel)
            add(buttonRight)
        }

        return minesPanel
    }

    private fun createButtonPanel(): JPanel {
        val bottomPanel = JPanel().apply {
            layout = BorderLayout()
            preferredSize = Dimension(GAME_WIDTH, 300)
            background = Color(Colors.GREY.hex)
        }

        return bottomPanel
    }

    private fun createInfoLabel(info: String): JLabel {
        val label = JLabel(info).apply {
            preferredSize = Dimension(250, 50)
            alignmentX = CENTER_ALIGNMENT
            alignmentY = CENTER_ALIGNMENT
            updateFont(this, 20F)
        }

        return label
    }

    private fun createStartingMenu(): JPanel {
        // Main menu panel
        val mainPanel = JPanel()
        mainPanel.layout = BorderLayout()

        // Logo panel
        mainPanel.add(createLogoPanel(), BorderLayout.NORTH)

        // Settings panel
        val settingsPanel = createSettingsPanel()
        mainPanel.add(settingsPanel)

        // Exact settings on settings panel
        val startButton = createStartButton()
        val difficultyPanel = createDifficultyPanel()
        val rowsPanel = createRowsPanel()
        val colsPanel = createColsPanel()
        val minesPanel = createMinesPanel()

        // Applying settings to panel
        settingsPanel.apply {
            add(Box.createRigidArea(Dimension(0, 10)))
            add(startButton)

            add(Box.createRigidArea(Dimension(0, 10)))
            add(createInfoLabel("Difficulty"))
            add(difficultyPanel)

            add(Box.createRigidArea(Dimension(0, 10)))
            add(createInfoLabel("Rows:"))
            add(rowsPanel)

            add(Box.createRigidArea(Dimension(0, 10)))
            add(createInfoLabel("Cols:"))
            add(colsPanel)

            add(Box.createRigidArea(Dimension(0, 10)))
            add(createInfoLabel("Mines:"))
            add(minesPanel)
        }

        // Bottom panel

        val bottomPanel = createButtonPanel()
        mainPanel.add(bottomPanel, BorderLayout.SOUTH)

        // Bottom corners
        bottomPanel.add(JLabel(ImageIcon(textures.bottomLeft.getScaledInstance(200, 300, Image.SCALE_SMOOTH))), BorderLayout.WEST)
        bottomPanel.add(JLabel(ImageIcon(textures.bottomRight.getScaledInstance(200, 300, Image.SCALE_SMOOTH))), BorderLayout.EAST)



        return mainPanel
    }

    private fun updateDifficulty(customText: String) {
        difficultyLabel.text = customText
        this.difficulty = 0
    }

    private fun updateDifficulty(diff: Int) {
        //println(difficulty)
        val moddif = abs(diff) % 3
        difficultyLabel.text = difficultyPreset[moddif].diffname
        this.rows = difficultyPreset[moddif].rows
        this.cols = difficultyPreset[moddif].cols
        this.mines = difficultyPreset[moddif].mines


    }

    private fun updateLabels() {
        if (mines > rows*cols) {
            mines = rows*cols
        }
        rowsLabel.text = rows.toString()
        colsLabel.text = cols.toString()
        minesLabel.text = mines.toString()
    }

    private fun updateFont(component: JComponent, newFontSize: Float) {
        val font = component.font
        val derivedFont = font.deriveFont(newFontSize)
        component.font = derivedFont
    }
}