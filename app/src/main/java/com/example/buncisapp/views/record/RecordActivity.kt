package com.example.buncisapp.views.record

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buncisapp.R
import com.example.buncisapp.databinding.ActivityRecordBinding

class RecordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}