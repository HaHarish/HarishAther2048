package com.harish.ather.gameLogics

import com.harish.ather.enums.GameMoves
import com.harish.ather.enums.TileMoveType
import com.harish.ather.interfaces.LogicMethods
import com.harish.ather.models.Record
import com.harish.ather.models.Transition
import com.harish.ather.utils.Constants
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

open class GameLogic : Serializable {

    private var delegate: LogicMethods

    private val gridCount = Constants.TILE_CNT
    private val rowCnt = Constants.DIMENSION
    private val colCnt = Constants.DIMENSION
    private val blankTile = Constants.EMPTY_TILE_VAL

    var score = 0
        private set
    var previousHighScore = 0
        private set
    var maxTile = Constants.EMPTY_TILE_VAL
        private set

    private var gameOver = false
    private var numEmpty = Constants.TILE_CNT

    private var previousMoves : ArrayList<Record> = ArrayList()
    private var transitions : ArrayList<Transition> = ArrayList()
    private var tiles : ArrayList<Int> = ArrayList()

    private val rand = Random()

    constructor(delegate: LogicMethods) {
        this.delegate = delegate
    }

    open fun newGame(newHighScore: Int) {
        this.previousHighScore = newHighScore
        this.numEmpty = Constants.TILE_CNT
        this.maxTile = Constants.EMPTY_TILE_VAL
        this.gameOver = false
        this.score = 0

        this.previousMoves = ArrayList()
        this.transitions = ArrayList()
        this.tiles = ArrayList()

        for (i in 0 until gridCount) {
            tiles.add(Constants.EMPTY_TILE_VAL)
            this.transitions.add(Transition(TileMoveType.Clear, this.blankTile, i))
        }

        this.addNewTile(2)
        this.addNewTile(2)
        this.previousMoves.add(0, getGameBoardRecord())
        this.applyGameMoves()
    }

    fun replotBoard() {
        transitions = ArrayList()
        for (i in 0 until gridCount) {
            this.transitions.add(Transition(TileMoveType.Reset, tiles[i], i))
        }
        this.applyGameMoves()
    }

    private fun getGameBoardRecord() : Record {
        return Record(tiles, score, numEmpty, gameOver, maxTile)
    }

    private fun addNewTile(seedValue: Int = -1) : Boolean {
        if (numEmpty == 0) { return false }

        var value = if (seedValue < 2 || seedValue > 4) {

            val sample = rand.nextInt(100)
            if (sample >= Constants.RANDOM_RATIO) {
                4
            } else {
                2
            }
        } else {
            seedValue
        }

        val pos = rand.nextInt(numEmpty)
        var blanksFound = 0

        for (i in 0 until gridCount) {
            if (tiles[i] == blankTile) {
                if (blanksFound == pos) {
                    tiles[i] = value
                    if (value > maxTile) { maxTile = value }
                    numEmpty -= 1
                    transitions.add(Transition(TileMoveType.Add, value, i))
                    return true
                }
                blanksFound += 1
            }
        }
        return false
    }

    private fun hasMovesRemaining() : Boolean {

        if (numEmpty > 0) { return true }

        val arrLimitX = gridCount-colCnt
        for (i in 0 until arrLimitX) {
            if (tiles[i] == tiles[i + colCnt]) { return true }
        }

        val arrLimitY = gridCount - 1
        for (i in 0 until arrLimitY) {
            if ((i + 1) % rowCnt > 0) {
                if (tiles[i] == tiles[i + 1]) { return true }
            }
        }
        return false
    }

    private fun slideTileRowOrColumn(vararg indexes: Int) : Boolean {

        var moved = false
        val tmpArr = intArrayOf(*indexes)


        var es = 0
        for (j in tmpArr.indices) {
            if (tiles[tmpArr[es]] != blankTile) {
                es += 1
                continue
            } else if (tiles[tmpArr[j]] == blankTile) {
                continue
            } else {
                // Otherwise we have a slide condition
                tiles[tmpArr[es]] = tiles[tmpArr[j]]
                tiles[tmpArr[j]] = blankTile
                transitions.add(Transition(TileMoveType.Slide, tiles[tmpArr[es]], tmpArr[es], tmpArr[j]))
                moved = true
                es += 1
            }
        }
        return moved
    }

    private fun compactTileRowOrColumn(vararg indexes: Int) : Boolean {

        var compacted = false
        val tmpArr = intArrayOf(*indexes)

        for (j in 0 until (tmpArr.size-1)) {
            if (tiles[tmpArr[j]] != blankTile && tiles[tmpArr[j]] == tiles[tmpArr[j+1]]) { // we found a matching pair
                val ctv = tiles[tmpArr[j]] * 2   // = compacted tile value
                tiles[tmpArr[j]] = ctv
                tiles[tmpArr[j+1]] = blankTile
                score += ctv
                if (ctv > maxTile) {
                    maxTile = ctv
                }  // is this the biggest tile # so far
                transitions.add(Transition(TileMoveType.Merge, ctv, tmpArr[j], tmpArr[j+1]))
                compacted = true
                numEmpty += 1
            }
        }
        return compacted
    }

    private fun slideLeft() : Boolean {
        val a = slideTileRowOrColumn(0, 4, 8, 12)
        val b = slideTileRowOrColumn(1, 5, 9, 13)
        val c = slideTileRowOrColumn(2, 6, 10, 14)
        val d = slideTileRowOrColumn(3, 7, 11, 15)
        return (a || b || c || d)
    }

    private fun slideRight() : Boolean {
        val a = slideTileRowOrColumn(12, 8, 4, 0)
        val b = slideTileRowOrColumn(13, 9, 5, 1)
        val c = slideTileRowOrColumn(14, 10, 6, 2)
        val d = slideTileRowOrColumn(15, 11, 7, 3)
        return (a || b || c || d)
    }

    private fun slideUp() : Boolean {
        val a = slideTileRowOrColumn(0, 1, 2, 3)
        val b = slideTileRowOrColumn(4, 5, 6, 7)
        val c = slideTileRowOrColumn(8, 9, 10, 11)
        val d = slideTileRowOrColumn(12, 13, 14, 15)
        return (a || b || c || d)
    }

    private fun slideDown() : Boolean {
        val a = slideTileRowOrColumn(3, 2, 1, 0)
        val b = slideTileRowOrColumn(7, 6, 5, 4)
        val c = slideTileRowOrColumn(11, 10, 9, 8)
        val d = slideTileRowOrColumn(15, 14, 13, 12)
        return (a || b || c || d)
    }

    private fun compactLeft() : Boolean {
        val a = compactTileRowOrColumn(0, 4, 8, 12)
        val b = compactTileRowOrColumn(1, 5, 9, 13)
        val c = compactTileRowOrColumn(2, 6, 10, 14)
        val d = compactTileRowOrColumn(3, 7, 11, 15)
        return (a || b || c || d)
    }

    private fun compactRight() : Boolean {
        val a = compactTileRowOrColumn(12, 8, 4, 0)
        val b = compactTileRowOrColumn(13, 9, 5, 1)
        val c = compactTileRowOrColumn(14, 10, 6, 2)
        val d = compactTileRowOrColumn(15, 11, 7, 3)
        return (a || b || c || d)
    }

    private fun compactUp() : Boolean {
        val a = compactTileRowOrColumn(0, 1, 2, 3)
        val b = compactTileRowOrColumn(4, 5, 6, 7)
        val c = compactTileRowOrColumn(8, 9, 10, 11)
        val d = compactTileRowOrColumn(12, 13, 14, 15)
        return (a || b || c || d)
    }

    private fun compactDown() : Boolean {
        val a = compactTileRowOrColumn(3, 2, 1, 0)
        val b = compactTileRowOrColumn(7, 6, 5, 4)
        val c = compactTileRowOrColumn(11, 10, 9, 8)
        val d = compactTileRowOrColumn(15, 14, 13, 12)
        return (a || b || c || d)
    }

    private fun actionMoveLeft() : Boolean {
        val a = slideLeft()
        val b = compactLeft()
        val c = slideLeft()
        return (a || b || c)
    }

    private fun actionMoveRight() : Boolean {
        val a = slideRight()
        val b = compactRight()
        val c = slideRight()
        return (a || b || c)
    }

    private fun actionMoveUp() : Boolean {
        val a = slideUp()
        val b = compactUp()
        val c = slideUp()
        return (a || b || c)
    }

    private fun actionMoveDown() : Boolean {
        val a = slideDown()
        val b = compactDown()
        val c = slideDown()
        return (a || b || c)
    }

    fun actionMove(move : GameMoves) : Boolean {

        var tempScore = this.score

        this.transitions = ArrayList()

        val changed = when (move) {
            GameMoves.Up -> actionMoveUp()
            GameMoves.Down -> actionMoveDown()
            GameMoves.Left -> actionMoveLeft()
            GameMoves.Right -> actionMoveRight()
        }

        if (changed) {
            addNewTile()
            this.previousMoves.add(0, getGameBoardRecord())  // index zero is current/last board result
            if (previousMoves.size > Constants.MAX_PREVIOUS_MOVES+1) {
                this.previousMoves.removeAt(previousMoves.size-1)
            }
            this.applyGameMoves()
        }

        if (!hasMovesRemaining()) {
            this.gameOver = true
            if (this.maxTile >= Constants.WIN_TARGET) {
                delegate.userWin()
            } else {
                delegate.userFail()
            }
            return false
        }

        return changed
    }

    private fun applyGameMoves() {
        for (trans in this.transitions) {
            delegate.updateTileValue(trans)
        }
    }
}