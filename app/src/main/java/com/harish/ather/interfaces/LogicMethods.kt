package com.harish.ather.interfaces

import com.harish.ather.models.Transition

interface LogicMethods {
    fun updateTileValue(move: Transition)
    fun userPB(score: Int)
    fun userWin()
    fun userFail()
    fun userScoreChanged(score: Int)
}