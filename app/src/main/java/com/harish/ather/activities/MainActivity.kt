package com.harish.ather.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harish.ather.R
import com.harish.ather.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val cells = IntArray(16)

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


    }
}