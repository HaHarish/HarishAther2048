package com.harish.ather.activities

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.harish.ather.R
import com.harish.ather.databinding.ActivityMainBinding
import com.harish.ather.enums.GameMoves
import com.harish.ather.enums.TileMoveType
import com.harish.ather.gameLogics.StartGame
import com.harish.ather.interfaces.LogicMethods
import com.harish.ather.models.Transition

class MainActivity : AppCompatActivity(), LogicMethods {

    private lateinit var binding: ActivityMainBinding

    private var x1 = 0f
    private var y1 = 0f
    private val cells = IntArray(16)
    private lateinit var game: StartGame

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        cells[0] = R.id.index0
        cells[1] = R.id.index1
        cells[2] = R.id.index2
        cells[3] = R.id.index3
        cells[4] = R.id.index4
        cells[5] = R.id.index5
        cells[6] = R.id.index6
        cells[7] = R.id.index7
        cells[8] = R.id.index8
        cells[9] = R.id.index9
        cells[10] = R.id.index10
        cells[11] = R.id.index11
        cells[12] = R.id.index12
        cells[13] = R.id.index13
        cells[14] = R.id.index14
        cells[15] = R.id.index15

        if (savedInstanceState != null) {
            val tmpGame : StartGame? = savedInstanceState.getSerializable(GAME_KEY) as StartGame
            if (tmpGame != null) {
                game = tmpGame
                game.gameBoard()
            }
        } else {
            game = StartGame(this)
            this.setupNewGame()
        }
    }

    @Suppress("UNUSED_PARAMETER")
    fun onClick(view: View) {
        this.setupNewGame()
    }

    private fun setupNewGame() {
        game.newGame(0)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                val x2 = event.x
                val y2 = event.y
                val minDistance = 200

                if (x1 < x2 && x2 - x1 > minDistance) { game.actionMove(GameMoves.Right) }
                else if (x1 > x2 && x1 - x2 > minDistance) { game.actionMove(GameMoves.Left) }
                else if (y1 < y2 && y2 - y1 > minDistance) { game.actionMove(GameMoves.Down) }
                else if (y1 > y2 && y1 - y2 > minDistance) { game.actionMove(GameMoves.Up) }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun paintTransition(move: Transition) {
        val tv = findViewById<TextView>(cells[move.location])
        if (move.action == TileMoveType.Slide || move.action == TileMoveType.Merge) {
            paintCell(tv, move.value)
            this.paintTransition(Transition(TileMoveType.Clear, 0, move.oldLocation))
        } else {
            paintCell(tv, move.value)
        }
    }

    private fun paintCell(obj: Any, value: Int) {

        val tv = obj as TextView
        tv.text = if (value <= 0) "" else "$value"

        var txCol = resources.getColor(R.color.t_dark_text, null)
        var bgCol = resources.getColor(R.color.t0_bg, null)

        when (value) {
            2 -> bgCol = resources.getColor(R.color.t2_bg, null)
            4 -> bgCol = resources.getColor(R.color.t4_bg, null)
            8 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t8_bg, null)
            }
            16 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t16_bg, null)
            }
            32 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t32_bg, null)
            }
            64 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t64_bg, null)
            }
            128 -> {
                txCol = resources.getColor(R.color.t_light_text, null)
                bgCol = resources.getColor(R.color.t128_bg, null)
            }
            256 -> bgCol = resources.getColor(R.color.t256_bg, null)
            512 -> bgCol = resources.getColor(R.color.t512_bg, null)
            1024 -> bgCol = resources.getColor(R.color.t1024_bg, null)
            2048 -> bgCol = resources.getColor(R.color.t2048_bg, null)
            else -> { }
        }
        tv.setBackgroundColor(bgCol)
        tv.setTextColor(txCol)
    }

    override fun userWin() {
        Toast.makeText(this, resources.getString(R.string.winner_toast_message) +
                game.maxTile, Toast.LENGTH_LONG).show()
    }

    override fun userFail() {
        Toast.makeText(this, resources.getString(R.string.lose_toast_message),
            Toast.LENGTH_LONG).show()
    }

    override fun updateTileValue(move: Transition) {
        paintTransition(move)
    }

    companion object {
        private const val GAME_KEY = "2048_GAME_KEY"
    }
}