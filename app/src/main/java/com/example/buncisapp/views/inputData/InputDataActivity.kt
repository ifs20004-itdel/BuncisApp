package com.example.buncisapp.views.inputData

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import com.example.buncisapp.R
import com.example.buncisapp.data.DataDummy
import com.example.buncisapp.databinding.ActivityInputDataBinding
import com.example.buncisapp.network.ApiConfig
import com.example.buncisapp.network.ApiService
import com.example.buncisapp.response.DataFuelType
import com.example.buncisapp.response.FuelTypeResponse
import com.example.buncisapp.response.ShipConditionResponse
import com.example.buncisapp.views.calculator.CalculatorActivity
import com.example.buncisapp.views.history.HistoryActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale
import javax.security.auth.callback.Callback

class InputDataActivity : AppCompatActivity() {

    private lateinit var inputDataViewModel: InputDataViewModel
    private lateinit var binding : ActivityInputDataBinding
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inputDataViewModel = ViewModelProvider(this).get(InputDataViewModel::class.java)
        fetchFuelTypes()
        fetchShipCondition()
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

    private fun fetchShipCondition() {
        inputDataViewModel.getShip().observe(this) { type ->
            val client = ApiConfig.getApiService().getShipCondition("Bearer ${type.token}")
            client.enqueue(object : Callback<ShipConditionResponse> {
                override fun onResponse(call: Call<ShipConditionResponse?>, response: Response<ShipConditionResponse?>) {
                    if (response.isSuccessful) {
                        val dataShipCondition = response.body()?.data
                        val shipCondition = dataShipCondition?.shipCondition

                        if (shipCondition != null) {
                            val adapter = ArrayAdapter(this@InputDataActivity, R.layout.dropdown_items, shipCondition)
                            binding.edKondisiKapal.setAdapter(adapter)
                        }
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<ShipConditionResponse?>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
    private fun fetchFuelTypes() {
        inputDataViewModel.getShip().observe(this) { type ->
            val client = ApiConfig.getApiService().getFuelType("Bearer ${type.token}")

            client.enqueue(object : Callback<FuelTypeResponse> {
                override fun onResponse(call: Call<FuelTypeResponse?>, response: Response<FuelTypeResponse?>) {
                    if (response.isSuccessful) {
                        val dataFuelType = response.body()?.data
                        val fuelTypes = dataFuelType?.fuelType

                        if (fuelTypes != null) {
                            val adapter = ArrayAdapter(this@InputDataActivity, R.layout.dropdown_items, fuelTypes)
                            binding.edBahanBakar.setAdapter(adapter)
                        }
                    } else {
                        Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    }
                }

                override fun onFailure(call: Call<FuelTypeResponse?>, t: Throwable) {
                    Log.e(ContentValues.TAG, "onFailure: ${t.message}")
                }
            })
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