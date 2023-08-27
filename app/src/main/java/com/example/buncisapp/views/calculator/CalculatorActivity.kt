package com.example.buncisapp.views.calculator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.buncisapp.R
import com.example.buncisapp.databinding.ActivityCalculatorBinding
import com.example.buncisapp.views.history.HistoryActivity
import com.example.buncisapp.views.record.RecordActivity

class CalculatorActivity : AppCompatActivity() {

    // Mock array
    private var listOfTank = mutableSetOf<String>()
    private var selectedItem = ""

    private lateinit var binding : ActivityCalculatorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lvToolbar.btnHistory.setOnClickListener {
            val intent = Intent(this@CalculatorActivity, HistoryActivity::class.java)
            startActivity(intent)
        }

        // This is still data mock
        val items = listOf("DB(S)", "DB(P)", "AFT")

        val adapter = ArrayAdapter(this, R.layout.dropdown_items, items)
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
            if(listOfTank.size == items.size){
                val intent = Intent(this@CalculatorActivity, RecordActivity::class.java)
                startActivity(intent)
            }else{
                // mock method
                val temp = ArrayList<String>()
                for (i in items){
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

        val builder = AlertDialog.Builder(this@CalculatorActivity)
        builder
            .setTitle("Peringatan!")
            .setMessage("List Tangki yang belum terisi: \n $stringFormat ")
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