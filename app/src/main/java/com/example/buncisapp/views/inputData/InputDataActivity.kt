package com.example.buncisapp.views.inputData

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.buncisapp.R
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.Biodata
import com.example.buncisapp.databinding.ActivityInputDataBinding
import com.example.buncisapp.views.ViewModelFactory
import com.example.buncisapp.views.auth.LoginActivity
import com.example.buncisapp.views.calculator.CalculatorActivity
import com.example.buncisapp.views.history.HistoryActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class InputDataActivity : AppCompatActivity() {

    private lateinit var inputDataViewModel: InputDataViewModel
    private lateinit var binding : ActivityInputDataBinding
    private val calendar = Calendar.getInstance()
    private var fuelTypeItems = mutableListOf<String>()
    private var shipConditionItems = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewModel()

        inputDataViewModel.getShip().observe(this) { ship ->
            binding.lvToolbar.btnAccount.text = intent.getStringExtra("username")
            inputDataViewModel.fuelType(ship.token)
            inputDataViewModel.shipCondition(ship.token)
        }

        val fuelTypeAdapter = ArrayAdapter(this, R.layout.dropdown_items, getFuelTypes())
        binding.edBahanBakar.setAdapter(fuelTypeAdapter)

        val shipConditionAdapter = ArrayAdapter(this, R.layout.dropdown_items, getShipCondition())
        binding.edKondisiKapal.setAdapter(shipConditionAdapter)

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

        binding.lvToolbar.btnAccount.setOnClickListener {
            MaterialAlertDialogBuilder(this@InputDataActivity)
                .setTitle("Peringatan!")
                .setMessage("Apakah anda yakin untuk keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    inputDataViewModel.logout()
                    val intent = Intent(this@InputDataActivity, LoginActivity::class.java)
                    startActivity(intent)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }

        binding.btnNext.setOnClickListener{
            var trim = 0.0
            if(binding.edDraftDepan.text != null && binding.edDraftBelakang.text != null ){
                trim =  binding.edDraftBelakang.text.toString().toDouble() - binding.edDraftDepan.text.toString().toDouble()
            }else if(binding.edDraftDepan.text ==null){
                trim =  binding.edDraftBelakang.text.toString().toDouble() - binding.edDraftTengah.text.toString().toDouble()
            }else if(binding.edDraftBelakang.text ==null){
                trim =  binding.edDraftTengah.text.toString().toDouble() - binding.edDraftDepan.text.toString().toDouble()
            }
            val data = Biodata(binding.edNamaPelabuhan.text.toString(), binding.edKondisiKapal.text.toString(), binding.edTanggal.text.toString(), binding.edBahanBakar.text.toString(), binding.tvTime.text.toString(), trim.toString())
            MaterialAlertDialogBuilder(this@InputDataActivity)
                .setTitle("Yakin untuk melanjutkan?")
                .setMessage("Anda yakin ingin melanjutkan ke CalculatorActivity?")
                .setPositiveButton("Ya") { _, _ ->
                    val intent = Intent(this@InputDataActivity, CalculatorActivity::class.java)
                    intent.putExtra("data", data )
                    startActivity(intent)
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun setupViewModel(){
        inputDataViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShipPreference.getInstance(dataStore),this)
        )[InputDataViewModel::class.java]
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

    private fun getFuelTypes(): List<String> {
        inputDataViewModel.fuelType.observe(this){items ->
            for(i in items){
                if(i != null){
                    fuelTypeItems.add(i)
                }
            }
        }
        return fuelTypeItems
    }

    private fun getShipCondition(): List<String>{
        inputDataViewModel.shipCondition.observe(this){ items ->
            for(i in items){
                if(i != null){
                    shipConditionItems.add(i)
                }
            }
        }
        return shipConditionItems
    }
}