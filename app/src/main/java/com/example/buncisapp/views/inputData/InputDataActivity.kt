package com.example.buncisapp.views.inputData

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.buncisapp.R
import com.example.buncisapp.databinding.ActivityInputDataBinding
import com.example.buncisapp.views.calculator.CalculatorActivity
import com.example.buncisapp.views.history.HistoryActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Locale

class InputDataActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInputDataBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // mock items
        val items = listOf("MFO 180", "MFO 380", "Biosolar (HSD)", "LSFO")
        val adapter = ArrayAdapter(this, R.layout.dropdown_items, items)
        binding.edBahanBakar.setAdapter(adapter)

        binding.mvTimer.setOnClickListener {
            showTimePickerDialog()
        }

        binding.edTanggal.setOnFocusChangeListener{
            view, focus ->
            if(focus){
                view.clearFocus()
                showDatePickerDialog()
            }
        }

        binding.lvToolbar.btnHistory.setOnClickListener {
            val intent = Intent(this@InputDataActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnNext.setOnClickListener{
            MaterialAlertDialogBuilder(this@InputDataActivity)
                .setTitle("Yakin untuk melanjutkan?")
                .setMessage("Anda yakin ingin melanjutkan ke CalculatorActivity?")
                .setPositiveButton("Ya") { _, _ ->
                    val intent = Intent(this@InputDataActivity, CalculatorActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Batal") { _, _ ->
                    // Do nothing if the user cancels
                }
                .show()
        }
    }

    private fun showTimePickerDialog(){
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            updateTimer()
        }
        TimePickerDialog(
            this,
            timeSetListener,
            12,
            10,
            true
        ).show()
    }

    private fun showDatePickerDialog(){
        val dateSetListener = DatePickerDialog.OnDateSetListener{
            _, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateEditText()
        }
        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun updateTimer(){
        val format = "HH : mm"
        val dateFormat = SimpleDateFormat(format, Locale.getDefault())
        binding.tvTime.text = dateFormat.format(calendar.time)
    }

    private fun updateEditText(){
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        binding.edTanggal.setText(sdf.format(calendar.time))
    }

}