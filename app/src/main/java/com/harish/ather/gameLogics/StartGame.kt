package com.harish.ather.gameLogics

import com.harish.ather.interfaces.LogicMethods
import java.util.*

class StartGame (delegated: LogicMethods) : GameLogic(delegate = delegated) {

    lateinit var startedPlaying : Date
        private set
    lateinit var startLastGame : Date
        private set

    init {
        this.startedPlaying = Date()
        this.startLastGame = this.startedPlaying
    }

    override fun newGame(newHighScore: Int) {
        super.newGame(newHighScore)
        this.startLastGame = Date()
    }
}