package com.example.buncisapp.views.calculator

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.buncisapp.R
import com.example.buncisapp.data.ShipPreference
import com.example.buncisapp.data.model.Biodata
import com.example.buncisapp.data.model.SoundingItems
import com.example.buncisapp.data.response.RobResponse
import com.example.buncisapp.databinding.ActivityCalculatorBinding
import com.example.buncisapp.utils.CalculatorCallback
import com.example.buncisapp.views.ViewModelFactory
import com.example.buncisapp.views.auth.LoginActivity
import com.example.buncisapp.views.record.RecordActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.math.RoundingMode
import java.text.DecimalFormat

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CalculatorActivity : AppCompatActivity(), CalculatorCallback {

    private lateinit var calculatorViewModel: CalculatorViewModel
    private var listOfTank = mutableListOf<SoundingItems>()
    private var listNoTanki = mutableListOf<String>()
    private var calculatorAdapter = CalculatorAdapter(listOfTank)
    private lateinit var rvSounding : RecyclerView
    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var biodata: Biodata
    private var average = 0.0
    private var volume = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()

        var dataBunker: RobResponse
        biodata = intent.getParcelableExtra("data")!!

        calculatorViewModel.getShip().observe(this) { ship ->
            calculatorViewModel.noTanki(ship.token)
        }

        rvSounding = binding.rvListTangki
        rvSounding.setHasFixedSize(true)
        setRecycleView()

        binding.btnSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.tvMode.text = resources.getString(R.string.volume)
                binding.btnSwitch.text = resources.getString(R.string.volume)
                binding.layoutSounding.visibility = View.GONE
                binding.tilVolume.visibility = View.VISIBLE
            } else {
                binding.tvMode.text = resources.getString(R.string.sounding)
                binding.btnSwitch.text = resources.getString(R.string.sounding)
                binding.layoutSounding.visibility = View.VISIBLE
                binding.tilVolume.visibility = View.GONE
            }
        }

        val adapter = ArrayAdapter(this, R.layout.dropdown_items, getNoTangki())
        binding.edNomorTangki.setAdapter(adapter)

        binding.edTrim.setText(biodata.draft.toString())
        binding.edTrim.isEnabled = false

        binding.btnCalculate.setOnClickListener {
            calculateAndDisplayResult(biodata)
        }

        binding.btnTambah.setOnClickListener {
            binding.edSounding4.isEnabled = false
            binding.edSounding5.isEnabled = false
            addNewItem()
        }

        binding.lvToolbar.btnAccount.setOnClickListener {

            binding.lvToolbar.btnAccount.isEnabled = false

            MaterialAlertDialogBuilder(this@CalculatorActivity)
                .setTitle("Peringatan!")
                .setMessage("Apakah anda yakin untuk keluar?")
                .setPositiveButton("Ya") { _, _ ->
                    calculatorViewModel.logout()
                    val intent = Intent(this@CalculatorActivity, LoginActivity::class.java)
                    startActivity(intent)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                    binding.lvToolbar.btnAccount.isEnabled = true
                }
                .show()
        }

        binding.btnNext.setOnClickListener {
            calculatorViewModel.getShip().observe(this) { user ->
                calculatorViewModel.postResult(
                    user.token,
                    biodata.nama,
                    biodata.tanggal,
                    biodata.waktu,
                    biodata.bahanBakar,
                    biodata.depan,
                    biodata.tengah,
                    biodata.kondisiKapal,
                    biodata.belakang,
                    0.0,
                    biodata.draft,
                    listOfTank,
                    this)
            }

            val intent = Intent(this@CalculatorActivity, RecordActivity::class.java)
            calculatorViewModel.data.observe(this) { data ->
                dataBunker = data
                intent.putExtra("bunkerData", dataBunker)
                startActivity(intent)
            }
        }
        calculatorViewModel.loading.observe(this){ isLoading ->
            showLoading(isLoading)
        }
    }

    private fun setRecycleView() {
        rvSounding.layoutManager = LinearLayoutManager(this)
        binding.rvListTangki.adapter = calculatorAdapter
    }

    private fun setupViewModel() {
        calculatorViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShipPreference.getInstance(dataStore), this)
        )[CalculatorViewModel::class.java]
    }

    private fun isValidate(): Boolean{
        return binding.edNomorTangki.text.toString().isNotEmpty()
    }
    private fun calculateAndDisplayResult(data: Biodata) {
        val decimalPlaces = DecimalFormat("#.##")
        decimalPlaces.roundingMode = RoundingMode.DOWN

        if(!isValidate()){
            Toast.makeText(this@CalculatorActivity, resources.getString(R.string.warning_empty_data,"nomor tangki tidak boleh kosong!"), Toast.LENGTH_SHORT).show()
            return
        }

        val sounding1 = binding.edSounding1.text.toString().toDoubleOrNull() ?: 0.0
        val sounding2 = binding.edSounding2.text.toString().toDoubleOrNull() ?: 0.0
        val sounding3 = binding.edSounding3.text.toString().toDoubleOrNull() ?: 0.0

        if (delta(sounding1, sounding2) > 3 || delta(sounding2, sounding3) > 3 || delta(sounding1, sounding3)>3) {
            binding.edSounding4.isEnabled = true
            binding.edSounding5.isEnabled = true
            val sounding4 = binding.edSounding4.text.toString().toDoubleOrNull() ?: 0.0
            val sounding5 = binding.edSounding5.text.toString().toDoubleOrNull() ?: 0.0

            if (sounding4 == 0.0 || sounding5 == 0.0) {
                Toast.makeText(this, "Isi sounding 4 dan 5!", Toast.LENGTH_SHORT).show()
                return
            }

            val sum5Sounding = sounding1 + sounding2 + sounding3 + sounding4 + sounding5
            Log.e("test", (sum5Sounding/5).toString())
            average = decimalPlaces.format(sum5Sounding / 5).toDouble()

            calculatorViewModel.getShip().observe(this) { user ->
                calculatorViewModel.calculation(
                    user.token,
                    data.draft,
                    binding.edNomorTangki.text.toString(),
                    average.toInt(),
                    this
                )
                calculatorViewModel.calculation.observe(this) { result ->
                    binding.tvResult.text = result.data?.volume.toString()
                }
            }
        }
        else {
            val sum = sounding1 + sounding2 + sounding3
            average = decimalPlaces.format(sum / 3).toDouble()

            calculatorViewModel.getShip().observe(this) { user ->
                calculatorViewModel.calculation(
                    user.token,
                    data.draft,
                    binding.edNomorTangki.text.toString(),
                    average.toInt(),
                    this
                )
                calculatorViewModel.calculation.observe(this) { result ->
                    binding.tvResult.text = result.data?.volume.toString()
                }
            }
        }
    }

    private fun addNewItem() {
        volume = if (binding.btnSwitch.isChecked){
            average = 0.0
            binding.edVolume.text.toString().toDouble()
        }else{
            binding.tvResult.text.toString().toDouble()
        }
        val newItem = SoundingItems(
            levelSounding = average,
            nomorTanki = binding.edNomorTangki.text.toString(),
            volume = volume
        )
        listOfTank.add(newItem)
        calculatorAdapter.notifyItemInserted(listOfTank.size)

        // back to initial state
        binding.rvListTangki.visibility = View.VISIBLE
        binding.tvEmptyData.visibility = View.GONE
        binding.edSounding1.text?.clear()
        binding.edSounding2.text?.clear()
        binding.edSounding3.text?.clear()
        binding.edSounding4.text?.clear()
        binding.edSounding5.text?.clear()
        binding.edVolume.text?.clear()

        Toast.makeText(this@CalculatorActivity, "Data Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show()
    }

    private fun getNoTangki(): List<String> {
        calculatorViewModel.noTanki.observe(this) { items ->
            for (i in items) {
                if (i != null) {
                    listNoTanki.add(i)
                }
            }
        }
        return listNoTanki
    }

    private fun showLoading(isLoading: Boolean){
        if(isLoading){
            binding.pbLoading.visibility  = View.VISIBLE
            binding.pbLoading.progress = 0
            binding.pbLoading.max = 100
        }else{
            binding.pbLoading.visibility = View.GONE
        }
    }

    private fun delta(param1: Double, param2: Double): Double {
        return if (param2 > param1) {
            param2 - param1
        } else {
            param1 - param2
        }
    }

    override fun onErrorCalculator(message: String?) {
        Toast.makeText(this@CalculatorActivity, message.toString(), Toast.LENGTH_SHORT).show()
    }
}