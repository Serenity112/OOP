package courseWork.gamemenu.model

import courseWork.gamemenu.model.FieldData.*
import courseWork.gamemenu.model.ModelState.*
import courseWork.gamemenu.model.ClickMode.*

enum class ClickMode {
    REVEALING,
    MARKING,
}

enum class FieldData {
    MINE,
    FIELD,
    MARK,
    WATER,
}

open class Field(var field: FieldData)

class WaterField(var depth: Int) : Field(WATER)

enum class ModelState {
    LOSS,
    WIN,
    INGAME,
}

val GAME_FINISHED = setOf(WIN, LOSS)

interface ModelChangeListener {
    fun onModelChanged()
}

class Model(private val rows: Int, private val cols: Int, private val mines: Int) {
    val size = rows * cols

    var dataBoard: MutableList<MutableList<Field>> = MutableList(rows) { MutableList(cols) { WaterField(0) } }
        private set

    var gameBoard: MutableList<MutableList<Field>> = MutableList(rows) { MutableList(cols) { Field(FIELD) } }
        private set

    var minesPositions: MutableList<Pair<Int, Int>> = mutableListOf()
        private set

    var movesLeft = rows * cols
        private set

    var minesLeft = mines
        private set

    var clickMode = REVEALING
        private set

    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    var state = LOSS
        private set

    init {
        require(rows in 1..99) { "Wrong rows number" }
        require(cols in 1..99) { "Wrong cols number" }
        require(mines in 1..rows * cols) { "Wrong mines number" }

        state = INGAME

        // placeMines("mines.txt")
        placeMines()
    }

    fun addModelChangeListener(listener: ModelChangeListener) {
        listeners.add(listener)
    }

    fun removeModelChangeListener(listener: ModelChangeListener) {
        listeners.remove(listener)
    }

    private fun notifyListeners() {
        listeners.forEach { it.onModelChanged() }
    }


    private fun isValid(row: Int, col: Int): Boolean {
        return ((row in 0 until rows) && (col in 0 until cols))
    }

    private fun printGameBoard() {
        var res = ""
        gameBoard.forEach { it ->
            it.forEach {
                when (it.field) {
                    MARK -> res += "M"
                    MINE -> res += "*"
                    WATER -> res += (it as WaterField).depth
                    FIELD -> res += "-"
                }
            }
            res += '\n'
        }

        println(res)
    }

    private fun placeMines() {
        var i = 0

        while (i < mines) {
            val y = (0 until rows).random()
            val x = (0 until cols).random()

            if (dataBoard[y][x].field != MINE) {
                dataBoard[y][x].field = MINE
                minesPositions.add(Pair(y, x))
                i++
            }
        }
    }


    fun switchClickMode() {
        clickMode = if (clickMode == MARKING) REVEALING else MARKING
    }

    fun doMove(row: Int, col: Int) {
        require(isValid(row, col)) { "Move was out of board" }

        require(state !in GAME_FINISHED) { "Game ended!" }

        val gameField = gameBoard[row][col].field

        if (gameField == WATER && (gameBoard[row][col] as WaterField).depth != 0) {
            autoRevealAdjacentFields(row, col)
        } else {
            when (clickMode) {
                REVEALING -> {
                    if ((movesLeft == rows * cols) && (dataBoard[row][col].field == MINE)) {
                        replaceMine(row, col)
                        revealField(row, col)
                    } else if (gameField == FIELD) {
                        revealField(row, col)
                    }
                }
                MARKING -> {
                    markField(row, col)
                }
            }
        }


        if (movesLeft == 0) {
            state = WIN
        }

        notifyListeners()
    }

    private fun replaceMine(row: Int, col: Int) {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (dataBoard[i][j].field == WATER) {
                    dataBoard[i][j].field = MINE
                    dataBoard[row][col].field = WATER

                    minesPositions.remove(Pair(row, col))
                    minesPositions.add(Pair(i, j))

                    return
                }
            }
        }


    }

    private fun countAdjacentObjects(
        board: MutableList<MutableList<Field>>,
        row: Int,
        col: Int,
        field: FieldData,
    ): Int {
        var count = 0

        for (i in -1..1) {
            for (j in -1..1) {
                if (isValid(row + i, col + j)) {
                    if (board[row + i][col + j].field == field) {
                        count++
                    }
                }
            }
        }

        return count
    }

    private fun autoRevealAdjacentFields(row: Int, col: Int) {
        //println("Calling auto reveal")
        val marks = countAdjacentObjects(gameBoard, row, col, MARK)

        //println("Marks = " + marks)
        if (marks >= (gameBoard[row][col] as WaterField).depth) {
            //println("Reveal by marks")
            revealAdjacentFields(row, col)
        }
    }


    private fun revealAdjacentFields(row: Int, col: Int) {
        //              N.W   N   N.E
        //				   \  |  /
        //					\ | /
        //				W----Cell----E
        //					/ | \
        //				   /  |  \
        //				S.W   S   S.E
        for (i in -1..1) {
            for (j in -1..1) {
                // Check !((i==0) && (j==0)) because we don't want to reveal current field, only adjacent
                if (isValid(row + i, col + j) && !((i == 0) && (j == 0))) {
                    if (gameBoard[row + i][col + j].field != MARK)
                        revealField(row + i, col + j)
                }
            }
        }
    }

    private fun markField(row: Int, col: Int) {
        val field = gameBoard[row][col].field

        if (field == MARK) {
            gameBoard[row][col].field = FIELD
            minesLeft++

            if (dataBoard[row][col].field == MINE) {
                movesLeft++
            }

        } else if (field == FIELD) {
            gameBoard[row][col].field = MARK
            minesLeft--

            if (dataBoard[row][col].field == MINE) {
                movesLeft--
            }
        }
    }

    private fun revealField(row: Int, col: Int) {
        if (gameBoard[row][col].field == WATER)
            return

        if (dataBoard[row][col].field == MINE) {
            state = LOSS

            gameBoard[row][col].field = MINE
            minesPositions.remove(Pair(row, col))

            return
        }

        movesLeft--
        val mines = countAdjacentObjects(dataBoard, row, col, MINE)
        val newField = WaterField(mines)
        gameBoard[row][col] = newField

        printGameBoard()

        if (mines == 0) {
            revealAdjacentFields(row, col)
        }
    }


//        Продукты
//        Яйца - 2 шт.
//        Сахар - 130 г
//                Мед - 2 ст.л.
//        Сода - 0,5 ч.л.
//        Корица - 1 ст.л. (по вкусу)
//        Мука - 1,5 стакана
}