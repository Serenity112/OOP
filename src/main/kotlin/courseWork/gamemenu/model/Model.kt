package courseWork.gamemenu.model

import courseWork.gamemenu.model.FieldData.*
import courseWork.gamemenu.model.State.*
import courseWork.gamemenu.model.ClickMode.*

import java.io.File

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
}


enum class ClickMode {
    REVEALING,
    MARKING,
}

enum class FieldData {
    MINE,
    FIELD, // Only undecided fields
    MARK,
    WATER, // Empty water or field with depth
}


open class Field(var field: FieldData)

class WaterField(var depth: Int) : Field(WATER)


enum class State(val textValue: String) {
    LOSS("Game finished, you lost..."),
    WIN("GGame finished, you won!"),
    INGAME("Game in progress, keep trying!"),
}

val GAME_FINISHED = setOf(State.WIN, State.LOSS)

interface ModelChangeListener {
    fun onModelChanged()
}


class Model(private val rows: Int, private val cols: Int, private val mines: Int) {
    private var _dataBoard: MutableList<MutableList<Field>> = MutableList(0) { MutableList(0) { WaterField(0) } }
    val dataBoard: List<List<Field>>
        get() = _dataBoard

    private var _gameBoard: MutableList<MutableList<Field>>
    val gameBoard: List<List<Field>>
        get() = _gameBoard

    private var _minesPositions: MutableList<Pair<Int, Int>> = MutableList(0) { Pair(0, 0) }
    val minesPositions: List<Pair<Int, Int>>
        get() = _minesPositions


    private var movesLeft = 0

    var clickMode = REVEALING
        private set

    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    var state = LOSS
        private set

    init {
        require(rows in 1..99) { "Wrong rows number" }
        require(cols in 1..99) { "Wrong cols number" }
        require(mines in 1..rows * cols) { "Wrong mines number" }

        movesLeft = rows * cols
        state = INGAME

        _gameBoard = MutableList(rows) { MutableList(cols) { Field(FIELD) } }

        // Init when mines placed
        //_dataBoard = MutableList(rows) { MutableList(cols) { Field(WATER) } }
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

    fun printGameBoard() {
        var res: String = ""
        _gameBoard.forEach { it ->
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

    fun printDataBoard() {
        var res: String = ""

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (_dataBoard[i][j].field == MINE) {
                    print("*")
                } else {
                    print("-")
                }
            }
            println()
        }
    }

    fun placeMines(file: String) {
        val i = 0
        val j = 0

        val file = File(file).forEachLine { it ->
            val line: MutableList<Field> = mutableListOf()

            it.forEach {

                when (it) {
                    '*' -> {
                        _minesPositions.add(Pair(_dataBoard.size, line.size))

                        println(_dataBoard.size.toString() + " " +  line.size.toString())

                        line.add(Field(MINE))

                    }
                    '-' -> line.add( WaterField(0))
                    else -> line.add( WaterField(0))
                }


            }
            // Искуссвенная инициализация))))
            _dataBoard.add(line)
        }


    }

    fun placeMines() {
        var i: Int = 0

        while (i < mines) {
            val y = (0 until rows).random()
            val x = (0 until cols).random()

            if (_dataBoard[y][x].field != MINE) {
                _dataBoard[y][x].field = MINE
                _minesPositions.add(Pair(y, x))
                i++
            }
        }
    }


    fun switchClickMode() {
        println("Mode switched")
        clickMode = if (clickMode == MARKING) REVEALING else MARKING
    }

    fun doMove(row: Int, col: Int) {
        require(isValid(row, col)) { "Move was out of board" }


        require(state == INGAME) { "Game ended!" }


        val gameField = _gameBoard[row][col].field

        if (gameField == WATER && (_gameBoard[row][col] as WaterField).depth != 0) {
            autoRevealAdjacentFields(row, col)
        } else {
            when (clickMode) {
                REVEALING -> {
                    if ((movesLeft == rows * cols) && (_dataBoard[row][col].field == MINE)) {
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


        notifyListeners()
    }

    private fun replaceMine(row: Int, col: Int) {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (_dataBoard[i][j].field == WATER) {
                    _dataBoard[i][j].field = MINE
                    _dataBoard[row][col].field = WATER
                    return
                }
            }
        }
    }

    private fun countAdjacentObjects(
        board: MutableList<MutableList<Field>>,
        row: Int,
        col: Int,
        field: FieldData
    ): Int {
        var count: Int = 0

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
        println("Calling auto reveal")
        val marks = countAdjacentObjects(_gameBoard, row, col, MARK)

        println("Marks = " + marks)
        if (marks == (_gameBoard[row][col] as WaterField).depth) {
            println("Reveal by marks")
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
                if (isValid(row + i, col + j) && !((i==0) && (j==0))) {
                    if(_gameBoard[row + i][col + j].field != MARK)
                    revealField(row + i, col + j)
                }
            }
        }
    }
// Количество меток совпадает с количеством мин.


    private fun markField(row: Int, col: Int) {
        val field = _gameBoard[row][col].field

        if (field == MARK) {
            _gameBoard[row][col].field = FIELD
            movesLeft++
        } else if (field == FIELD) {
            movesLeft--
            _gameBoard[row][col].field = MARK
        }
    }

    private fun revealField(row: Int, col: Int) {
        // Мы нажали на число (вне зависимости от типа ввода), а значит хотим запустить автовсрытие ближайших влеток на основе меток
        println("Revealing " + row + " " + col + " " + _dataBoard[row][col].field)
        // Мы открыли мину
        if (_gameBoard[row][col].field == WATER)
            return

        if (_dataBoard[row][col].field == MINE) {
            println("Mine")
            state = LOSS

            _gameBoard[row][col].field = MINE
            _minesPositions.remove(Pair(row, col))


            return
        }

        // Мы гарантированно попали не в мину, поэтому считаем мины вокруг
        movesLeft--
        val mines = countAdjacentObjects(_dataBoard, row, col, MINE)
        val newField = WaterField(mines)
        _gameBoard[row][col] = newField

        printGameBoard()
        // Если вокруг клетки нет мин, означает, что это нулевая клетка. А значит мы вскрываем все 8 клеток вокруг
        if (mines == 0) {
            println("if (mines == 0)")


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