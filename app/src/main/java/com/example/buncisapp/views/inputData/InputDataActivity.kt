package com.example.buncisapp.views.inputData

import android.app.DatePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.example.buncisapp.R
import com.example.buncisapp.databinding.ActivityInputDataBinding
import java.text.SimpleDateFormat
import java.util.Locale

class InputDataActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInputDataBinding
    private val calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        val items = listOf("Kapal A, Kapal B, Kapal C, Kapal D")
//        val adapter = ArrayAdapter(this, R.layout.activity_input_data, items)
//        (binding.edBahanBakar as? AutoCompleteTextView)?.setAdapter(adapter)

        binding.edTanggal.setOnFocusChangeListener{
            view, focus ->
            if(focus){
                view.clearFocus()
                showDatePickerDialog()
            }
        }
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

    private fun updateEditText(){
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale.US)
        binding.edTanggal.setText(sdf.format(calendar.time))
    }
}