package com.harish.ather.utils

class Constants {
    companion object {
        // Game specific values
        const val WIN_TARGET = 2048
        const val EMPTY_TILE_VAL = 0
        const val MAX_PREVIOUS_MOVES = 5
        const val PB_MESG_THRESHOLD = 100

        // Board specifics
        const val DIMENSION = 4
        const val TILE_CNT = 16
        const val TILE_WIDTH = 65.5
        const val TILE_PADDING = 8.0
        const val BOARD_CORNER_RADIUS = 2.0
        const val TILE_CORNER_RADIUS = 5

        // Animation duration in seconds
        const val ZERO_DURATION = 0.00
        const val QUICK_DURATION = 0.09
        const val NORMAL_DURATION = 0.25
        const val SLOW_DURATION = 0.45
        const val LONG_DURATION = 0.60
        const val PAUSE_DURATION = 1.00

        // Random tile selection ratio
        const val RANDOM_RATIO = 70  // favours '2's over '4's by ~3.5:1 ~70%




    }
}