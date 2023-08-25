package com.example.buncisapp.views.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buncisapp.R
import com.example.buncisapp.databinding.ActivityCalculatorBinding
import com.example.buncisapp.databinding.ActivityHistoryBinding
import com.example.buncisapp.databinding.ItemHistoryBinding
import com.example.buncisapp.views.history.HistoryActivity

class CalculatorActivity : AppCompatActivity() {

    private lateinit var binding : ActivityCalculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lvToolbar.btnHistory.setOnClickListener {
            val intent = Intent(this@CalculatorActivity, HistoryActivity::class.java)
            startActivity(intent)
        }
    }
}