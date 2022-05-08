package courseWork.model

import courseWork.model.FieldData.*
import courseWork.model.State.*
import courseWork.model.Difficulty.*
import courseWork.model.ClickMode.*

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
    WATER,
    MARK,
    EMPTY,
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


class Model {
    private var _dataBoard: MutableList<MutableList<Field>> = MutableList(0) { MutableList(0) { WaterField(0) } }
    val dataBoard: List<List<Field>>
        get() = _dataBoard

    private var _gameBoard: MutableList<MutableList<Field>> = MutableList(0) { MutableList(0) { Field(EMPTY) } }
    val gameBoard: List<List<Field>>
        get() = _gameBoard

    private var minesPositions: MutableList<Pair<Int, Int>> = MutableList(0) { Pair(0, 0) }

    private var minesCount = 0

    var sizeRows = 0
        private set
    var sizeCols = 0
        private set

    private var movesLeft = 0
    private var clickMode = REVEALING

    private val listeners: MutableSet<ModelChangeListener> = mutableSetOf()

    var state = LOSS
        private set

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
        println("Check valid for:")
        println(row)
        println(col)
        return ((row in 0 until sizeRows) && (col in 0 until sizeCols))
    }

    fun printGameBoard() {
        var res: String = ""
        _gameBoard.forEach { it ->
            it.forEach {
                when (it.field) {
                    MARK -> res += "M"
                    MINE -> res += "*"
                    WATER -> res += (it as WaterField).depth
                    EMPTY -> res += "-"
                }
            }
            res += '\n'
        }

        println(res)
    }

    fun printDataBoard() {
        var res: String = ""

        for (i in 0 until sizeRows) {
            for (j in 0 until sizeCols) {
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
        val file = File(file).forEachLine { it ->
            val line: MutableList<Field> = mutableListOf()

            it.forEach {

                val currentField = when (it) {
                    '*' -> Field(MINE)
                    '-' -> WaterField(0)
                    else -> WaterField(0)
                }

                line.add(currentField)
            }
            _dataBoard.add(line)
        }
        sizeRows = 9
        sizeCols = 9
        movesLeft = sizeRows * sizeCols
        state = INGAME

        _gameBoard = MutableList(sizeRows) { MutableList(sizeCols) { Field(EMPTY) } }
    }

    fun placeMines() {
        var i: Int = 0

        while (i < minesCount) {
            val x = (0 until sizeRows).random()
            val y = (0 until sizeCols).random()

            if (_dataBoard[x][y].field != MINE) {
                _dataBoard[x][y].field = MINE
                minesPositions.add(Pair(x, y))
                i++
            }
        }
    }

    fun chooseDifficultyLevel() {
        println("Choose difficulty:")

        var result = readln().toInt()

        when (result) {
            0 -> {
                initializeSizes(EASY)
            }
            1 -> {
                initializeSizes(MEDIUM)
            }
            2 -> {
                initializeSizes(HARD)
            }
            else -> {
                initializeSizes(EASY)
            }
        }
    }

    private fun initializeSizes(difficulty: Difficulty) {
        when (difficulty) {
            EASY -> {
                sizeRows = 9
                sizeCols = 9
                minesCount = 10
            }
            MEDIUM -> {
                sizeRows = 16
                sizeCols = 16
                minesCount = 40
            }
            HARD -> {
                sizeRows = 24
                sizeCols = 24
                minesCount = 99
            }
        }

        movesLeft = sizeRows * sizeCols
    }

    fun initializeBoard() {
        _dataBoard = MutableList(sizeRows) { MutableList(sizeCols) { Field(WATER) } }
        _gameBoard = MutableList(sizeRows) { MutableList(sizeCols) { Field(EMPTY) } }
        state = INGAME
    }

    fun SwitchMode() {
        println("Mode switched")
        clickMode = if (clickMode == MARKING) REVEALING else MARKING
    }

    fun doMove(row: Int, col: Int) {
        require(isValid(row, col)) { "Move was out of board" }
        require(state == INGAME) { "Game ended!" }

        if (movesLeft == sizeCols * sizeRows) {
            if (_dataBoard[row][col].field == MINE) {
                replaceMine(row, col)
            }
        }


        //Если поле уже было открыто, игра попробует провести авто-раскрытие полей вокруг
        if (_gameBoard[row][col].field == WATER) {
            autoRevealAdjacentFields(row, col)
        } else {
            revealField(row, col)
        }

        notifyListeners()
    }

    private fun replaceMine(row: Int, col: Int) {
        for (i in 0 until sizeRows) {
            for (j in 0 until sizeCols) {
                if (_dataBoard[i][j].field == WATER) {
                    _dataBoard[i][j].field = MINE
                    _dataBoard[row][col].field = WATER
                    return
                }
            }
        }
    }

    private fun countAdjacentObjects(board : MutableList<MutableList<Field>>, row: Int, col: Int, field: FieldData): Int {
        var count: Int = 0

        for(i in -1 .. 1) {
            for(j in -1 .. 1) {
                if (isValid(row+i, col+j)) {
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
        // Так все положения соседних клеток:
        //              N.W   N   N.E
        //				   \  |  /
        //					\ | /
        //				W----Cell----E
        //					/ | \
        //				   /  |  \
        //				S.W   S   S.E

        for(i in -1 .. 1) {
            for(j in -1 .. 1) {
                if (isValid(row+i, col+j)) {
                    revealField(row + i, col + j)
                }
            }
        }
    }
    // Количество меток совпадает с количеством мин.

    private fun revealField(row: Int, col: Int) {
        // Мы нажали на число (вне зависимости от типа ввода), а значит хотим запустить автовсрытие ближайших влеток на основе меток


        when (clickMode) {
            REVEALING -> {
                println("Revealing " + row + " " + col + " " + _dataBoard[row][col].field)
                // Мы открыли мину
                if (_gameBoard[row][col].field == WATER)
                    return

                if (_dataBoard[row][col].field == MINE) {
                    println("Mine")
                    state = LOSS
                    _gameBoard[row][col].field = MINE
                    minesPositions.forEach {
                        _gameBoard[it.first][it.second].field = MINE
                    }
                    printGameBoard()
                    return
                }

                // Мы гарантированно попали не в мину, поэтому считаем мины вокруг
                movesLeft--
                val minesCount = countAdjacentObjects(_dataBoard, row, col, MINE)
                val newField = WaterField(minesCount)
                _gameBoard[row][col] = newField

                printGameBoard()
                // Если вокруг клетки нет мин, означает, что это нулевая клетка. А значит мы вскрываем все 8 клеток вокруг
                if (minesCount == 0) {
                    println("if (minesCount == 0)")


                    revealAdjacentFields(row, col)
                }
            }
            MARKING -> {
                // Убираем метку, если стояла
                if (_gameBoard[row][col].field == MARK) {
                    movesLeft--
                    _gameBoard[row][col].field = EMPTY
                } // Ставим метку на поле
                else {
                    movesLeft++
                    _gameBoard[row][col].field = MARK
                }
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
}