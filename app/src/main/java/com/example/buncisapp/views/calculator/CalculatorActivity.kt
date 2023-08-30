package com.example.buncisapp.views.calculator

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.buncisapp.R
import com.example.buncisapp.data.DataDummy
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.Biodata
import com.example.buncisapp.data.response.SoundingItem
import com.example.buncisapp.databinding.ActivityCalculatorBinding
import com.example.buncisapp.views.ViewModelFactory
import com.example.buncisapp.views.history.HistoryActivity
import com.example.buncisapp.views.record.RecordActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.function.DoubleUnaryOperator

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CalculatorActivity : AppCompatActivity() {

    // Mock array
    private lateinit var calculatorViewModel: CalculatorViewModel

    private var listOfTank = mutableSetOf<SoundingItem>()
    private var listNoTanki = mutableListOf<String>()
    private var selectedItem = ""

    private lateinit var binding : ActivityCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()

        val data = intent.getParcelableExtra<Biodata>("data")
        binding.lvToolbar.btnHistory.setOnClickListener {
            val intent = Intent(this@CalculatorActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        val adapter = ArrayAdapter(this, R.layout.dropdown_items, getNoTanki())
        binding.edNomorTangki.setAdapter(adapter)

        binding.edNomorTangki.setOnItemClickListener { adapterView, _, i, _ ->
            selectedItem = adapterView.getItemAtPosition(i) as String
        }

        if (data != null) {
            if(data.draft.toInt() > 2){
                binding.edSounding4.isEnabled
                binding.edSounding5.isEnabled
            }
        }

        binding.btnCalculate.setOnClickListener {
            val sounding1 = binding.edSounding1.text.toString().toDouble()
            val sounding2 = binding.edSounding1.text.toString().toDouble()
            val sounding3 = binding.edSounding1.text.toString().toDouble()
            val sounding4 = binding.edSounding1.text.toString().toDouble()
            val sounding5 = binding.edSounding1.text.toString().toDouble()
            val average : Double

            if (data != null) {
                if(data.draft.toInt() < 2){
                    val sum = sounding1 + sounding2 + sounding3
                    average = sum/3
                    if (selectedItem.isNotEmpty()){
                        val soundingLevel = SoundingItem(average.toInt(),selectedItem)
                        listOfTank.add(soundingLevel)
                    }
                }else{
                    val sum = sounding1 + sounding2 + sounding3 + sounding4 + sounding4 + sounding5
                    average = sum/5
                    if (selectedItem.isNotEmpty()){
                        val soundingLevel = SoundingItem(average.toInt(),selectedItem)
                        listOfTank.add(soundingLevel)
                    }
                }
            }
            Toast.makeText(this@CalculatorActivity, listOfTank.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.btnNext.setOnClickListener {
            if (data != null) {
                calculatorViewModel.postResult(data.nama, data.kondisiKapal, data.tanggal, data.bahanBakar,data.waktu,data.draft)
            }
        }
    }

    private fun setupViewModel(){
        calculatorViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShipPreference.getInstance(dataStore),this)
        )[CalculatorViewModel::class.java]
    }

    private fun getNoTanki(): List<String>{
        calculatorViewModel.noTanki.observe(this){ items ->
            for(i in items){
                if(i != null){
                    listNoTanki.add(i)
                }
            }
        }
        return listNoTanki
    }

    private fun showDialog(data: ArrayList<String>){

        // Mock method
        var stringFormat = ""
        for (i in data){
            stringFormat += "$i \n"
        }

        val builder = MaterialAlertDialogBuilder(this@CalculatorActivity)
        builder
            .setTitle("Peringatan!")
            .setMessage("List Tangki yang belum terisi: \n$stringFormat ")
            .setPositiveButton("Lanjut"){
                    _,_->
                val intent = Intent(
                    this@CalculatorActivity,
                    RecordActivity::class.java
                )
                startActivity(intent)
            }
            .setNegativeButton("Kembali"){
                dialog, _ ->
                dialog.dismiss()
            }
            .create()
        builder.show()
    }

    private fun checkPermission(permission: String):Boolean{
        return ContextCompat.checkSelfPermission(
            this, permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestRuntimePermission(){
        if (checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) && checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(this@CalculatorActivity, "Already permitted",Toast.LENGTH_SHORT).show()
        }else{

        }
    }
}