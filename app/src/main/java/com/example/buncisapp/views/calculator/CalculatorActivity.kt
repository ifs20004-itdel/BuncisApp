package com.example.buncisapp.views.calculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.buncisapp.R
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.Biodata
import com.example.buncisapp.data.model.SoundingItem
import com.example.buncisapp.data.model.SoundingItems
import com.example.buncisapp.data.response.BunkerResponse
import com.example.buncisapp.databinding.ActivityCalculatorBinding
import com.example.buncisapp.views.ViewModelFactory
import com.example.buncisapp.views.history.HistoryActivity
import com.example.buncisapp.views.record.RecordActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CalculatorActivity : AppCompatActivity() {

    // Mock array
    private lateinit var calculatorViewModel: CalculatorViewModel

    private var listOfTank = mutableSetOf<SoundingItems>()
    private var listNoTanki = mutableListOf<String>()
    private var bunkerResponse = mutableListOf<BunkerResponse>()
//    private var selectedItem = ""

    private lateinit var binding : ActivityCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()

        calculatorViewModel.getShip().observe(this) { ship ->
            calculatorViewModel.noTanki(ship.token)
        }

        val data = intent.getParcelableExtra<Biodata>("data")

        binding.lvToolbar.btnHistory.setOnClickListener {
            val intent = Intent(this@CalculatorActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        val adapter = ArrayAdapter(this, R.layout.dropdown_items, getNoTanki())
        binding.edNomorTangki.setAdapter(adapter)

        if (data != null) {
            binding.edTrim.setText(data.draft)
        }

//        binding.edNomorTangki.setOnItemClickListener { adapterView, _, i, _ ->
//            selectedItem = adapterView.getItemAtPosition(i) as String
//        }

        binding.btnCalculate.setOnClickListener {
            var n = 3
            var sum = 0.0

            val sounding1 = binding.edSounding1.text.toString().toDouble()
            val sounding2 = binding.edSounding2.text.toString().toDouble()
            val sounding3 = binding.edSounding3.text.toString().toDouble()
            val sounding4 = binding.edSounding4
            val sounding5 = binding.edSounding5

//            if(delta(sounding1, sounding2)>3 || delta(sounding1, sounding3)>3 || delta(sounding2, sounding3)>3){
//                sounding4.isEnabled
//                sounding5.isEnabled
//                n += 2
//                Toast.makeText(this@CalculatorActivity, "Silahkan tambahkan data sounding!", Toast.LENGTH_SHORT).show()
//            }

//            sum = if(n ==3){
//                sounding1 + sounding2 + sounding3
//            }else{
//                sounding1 + sounding2 + sounding3 + sounding4.text.toString().toDouble() + sounding5.text.toString().toDouble()
//            }
//            sum/n
            sum = sounding1 + sounding2 + sounding3
            val average = sum / 3

            binding.tvResult.text = average.toString()

            val newItem = SoundingItems(
                levelSounding = average,
                nomorTanki = binding.edNomorTangki.text.toString()
            )
            listOfTank.add(newItem)

        }

        binding.btnNext.setOnClickListener {
            calculatorViewModel.getShip().observe(this){
                    user ->
                if (data != null) {
                    calculatorViewModel.postResult(
                        user.token, data.nama, data.kondisiKapal, data.waktu,data.bahanBakar,data.depan,data.tengah,data.kondisiKapal,data.belakang,0,data.draft.toDouble(),listOfTank
                    )
                }
            }

            if (data != null) {
                calculatorViewModel.data.observe(this){bunker ->
                    bunkerResponse.add(bunker)
                    val data = Biodata("Ketapang", "REDELIVERY", "2023-08-13", "Pertamax Racing", "15:01:02", "-2.5", 3.0, 3.0, 3.0)
                    val intent = Intent(this@CalculatorActivity, RecordActivity::class.java)
                    intent.putExtra("data", data )
                    startActivity(intent)
                }
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

    private fun delta(param1: Double, param2: Double): Double {
        return if(param2>param1){
            param2-param1
        }else{
            param1-param2
        }
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

}