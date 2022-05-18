package courseWork.mainmenu.view

import java.awt.*
import javax.swing.*
import kotlin.math.abs

import courseWork.GameTextures
import courseWork.gamemanager.GameManager
import courseWork.gamemanager.GameState
import courseWork.guiutils.GuiUtils.updateFont
import courseWork.guiutils.GuiUtils.Colors.*

import java.awt.Image

private const val GAME_WIDTH = 1200
private const val GAME_HEIGHT = 1000

private const val MAX_ROWS = 99
private const val MIN_ROWS = 1

private const val MAX_COLS = 99
private const val MIN_COLS = 1

private const val MAX_MINES = 1000
private const val MIN_MINES = 1

class MainMenuPanel(private val manager: GameManager) : JFrame("Minesweeper.") {
    private val difficultyPreset = manager.difficultyPreset

    private var difficultyName = manager.gameDifficulty.difficultyName
    private var rows = manager.gameDifficulty.rows
    private var cols = manager.gameDifficulty.cols
    private var mines = manager.gameDifficulty.mines

    private var difficultyCounter: Int = 0

    private var difficultyLabel = JLabel()
    private var rowsLabel = JLabel()
    private var colsLabel = JLabel()
    private var minesLabel = JLabel()

    private val textures = GameTextures.textures

    init {
        setSize(GAME_WIDTH, GAME_HEIGHT)
        defaultCloseOperation = EXIT_ON_CLOSE
    }

    fun createStartingMenu(): JPanel {
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
        bottomPanel.add(JLabel(ImageIcon(textures.bottomLeft.getScaledInstance(200, 300, Image.SCALE_SMOOTH))),
            BorderLayout.WEST)
        bottomPanel.add(JLabel(ImageIcon(textures.bottomRight.getScaledInstance(200, 300, Image.SCALE_SMOOTH))),
            BorderLayout.EAST)

        // Set text and values to buttons
        updateLabels()

        return mainPanel
    }

    private fun createStartButton(): JButton {
        val startButton = JButton().apply {
            addActionListener {
                manager.changeConfiguration(difficultyLabel.text, rows, cols, mines)
                manager.changeState(GameState.GAME)
            }
            preferredSize = Dimension(200, 200)
            maximumSize = Dimension(200, 200)
            minimumSize = Dimension(200, 200)
            alignmentX = CENTER_ALIGNMENT
            icon = ImageIcon(textures.goButton.getScaledInstance(200, 200, Image.SCALE_SMOOTH))
        }

        return startButton
    }

    private fun createLogoPanel(): JPanel {
        val logoLabel = JLabel(ImageIcon(textures.logoTex.getScaledInstance(700, 100, Image.SCALE_SMOOTH)))
        val logo = JPanel().apply {
            background = Color(GREY.hex)
            preferredSize = Dimension(GAME_WIDTH, 100)
            add(logoLabel)
        }
        return logo
    }

    private fun createSettingsPanel(): JPanel {
        val settingsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            background = Color(GREY.hex)
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
            this.alignmentX = CENTER_ALIGNMENT
            this.alignmentY = CENTER_ALIGNMENT

            updateFont(this, 40F)

            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }

        this.difficultyLabel = difficultyLabel

        val difficultyPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = CENTER_ALIGNMENT
            background = Color(WHITE.hex)

            val buttonLeft = createArrowButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    difficultyCounter--
                    updateDifficulty()
                }
            }

            val buttonRight = createArrowButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    difficultyCounter++
                    updateDifficulty()
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
            this.alignmentX = CENTER_ALIGNMENT
            this.alignmentY = CENTER_ALIGNMENT

            updateFont(this, 40F)

            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)

        }
        this.rowsLabel = rowsLabel

        val rowsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = CENTER_ALIGNMENT
            background = Color(WHITE.hex)

            val buttonLeft = createArrowButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (rows > MIN_ROWS) {
                        rows--
                        updateDifficulty("Custom")
                    }
                }
            }
            val buttonRight = createArrowButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (rows < MAX_ROWS) {
                        rows++
                        updateDifficulty("Custom")
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
            this.alignmentX = CENTER_ALIGNMENT
            this.alignmentY = CENTER_ALIGNMENT

            updateFont(this, 40F)

            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }
        this.colsLabel = colsLabel

        val colsPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = CENTER_ALIGNMENT
            background = Color(WHITE.hex)

            val buttonLeft = createArrowButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (cols > MIN_COLS) {
                        cols--
                        updateDifficulty("Custom")
                    }
                }
            }
            val buttonRight = createArrowButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                addActionListener {
                    if (cols < MAX_COLS) {
                        cols++
                        updateDifficulty("Custom")
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
            this.alignmentX = CENTER_ALIGNMENT
            this.alignmentY = CENTER_ALIGNMENT

            updateFont(this, 40F)

            maximumSize = Dimension(170, 50)
            minimumSize = Dimension(170, 50)
            preferredSize = Dimension(170, 50)
        }
        this.minesLabel = minesLabel


        val minesPanel = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.X_AXIS)
            alignmentX = CENTER_ALIGNMENT
            background = Color(WHITE.hex)

            val buttonLeft = JButton().apply {
                icon = ImageIcon(textures.arrowLeft.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
                addActionListener {
                    if (mines > MIN_MINES) {
                        mines--
                        updateDifficulty("Custom")
                    }
                }
            }
            val buttonRight = JButton().apply {
                icon = ImageIcon(textures.arrowRight.getScaledInstance(50, 50, Image.SCALE_SMOOTH))
                maximumSize = Dimension(50, 50)
                minimumSize = Dimension(50, 50)
                preferredSize = Dimension(50, 50)
                addActionListener {
                    if (mines < MAX_MINES) {
                        mines++
                        updateDifficulty("Custom")
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
            background = Color(GREY.hex)
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

    private fun updateDifficulty(customDifficulty: String) {
        difficultyPreset.forEachIndexed  { index, it ->
            if( it.rows == rows && it.cols == cols && it.mines == mines) {
                difficultyName = it.difficultyName
                this.difficultyCounter = index
                updateLabels()
                return
            }
        }

        difficultyName = customDifficulty
        this.difficultyCounter = 0
        updateLabels()
    }

    private fun updateDifficulty() {
        val roundedDifficulty = abs(difficultyCounter) % difficultyPreset.size

        this.difficultyName = difficultyPreset[roundedDifficulty].difficultyName
        this.rows = difficultyPreset[roundedDifficulty].rows
        this.cols = difficultyPreset[roundedDifficulty].cols
        this.mines = difficultyPreset[roundedDifficulty].mines

        updateLabels()
    }

    private fun updateLabels() {
        if (mines > rows * cols) {
            mines = rows * cols
        }

        difficultyLabel.text = difficultyName
        rowsLabel.text = rows.toString()
        colsLabel.text = cols.toString()
        minesLabel.text = mines.toString()
    }
}