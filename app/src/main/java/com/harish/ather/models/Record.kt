package com.harish.ather.models

import java.io.Serializable

class Record : Serializable {
    var tiles: ArrayList<Int>
    var score: Int
    var numEmpty: Int
    var gameOver: Boolean
    var maxTile: Int

    constructor(tiles: ArrayList<Int>, score: Int, numEmpty: Int, gameOver: Boolean, maxTile: Int) {
        this.tiles = tiles
        this.score = score
        this.numEmpty = numEmpty
        this.gameOver = gameOver
        this.maxTile = maxTile
    }
}