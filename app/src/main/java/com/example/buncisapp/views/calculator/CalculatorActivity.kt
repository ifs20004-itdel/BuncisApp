package com.example.buncisapp.views.calculator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.buncisapp.R
import com.example.buncisapp.data.DataDummy
import com.example.buncisapp.data.model.Biodata
import com.example.buncisapp.databinding.ActivityCalculatorBinding
import com.example.buncisapp.views.history.HistoryActivity
import com.example.buncisapp.views.record.RecordActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CalculatorActivity : AppCompatActivity() {

    // Mock array
    private var listOfTank = mutableSetOf<String>()
    private var selectedItem = ""

    private lateinit var binding : ActivityCalculatorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val data = intent.getParcelableExtra<Biodata>("data")
        binding.lvToolbar.btnHistory.setOnClickListener {
            val intent = Intent(this@CalculatorActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        val adapter = ArrayAdapter(this, R.layout.dropdown_items, DataDummy.tankiMinyak)
        binding.edNomorTangki.setAdapter(adapter)

        binding.edNomorTangki.setOnItemClickListener { adapterView, _, i, _ ->
            selectedItem = adapterView.getItemAtPosition(i) as String
        }


        binding.btnCalculate.setOnClickListener {
            // save to db
            // record the tank that has been filled
            if (selectedItem.isNotEmpty()){
                listOfTank.add(selectedItem)
            }
            Toast.makeText(this@CalculatorActivity, listOfTank.toString(), Toast.LENGTH_SHORT).show()
        }

        binding.btnNext.setOnClickListener {
            if(listOfTank.size == DataDummy.tankiMinyak.size){
                requestRuntimePermission()
                val intent = Intent(this@CalculatorActivity, RecordActivity::class.java)
                startActivity(intent)
            }else{
                // mock method
                val temp = ArrayList<String>()
                for (i in DataDummy.tankiMinyak){
                    if(i !in listOfTank ){
                        temp.add(i)
                    }
                }
                showDialog(temp)
            }
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