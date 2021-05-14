package com.harish.ather.interfaces

import com.harish.ather.models.Transition

interface LogicMethods {
    fun updateTileValue(move: Transition)
    fun userWin()
    fun userFail()
}