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
import com.example.buncisapp.data.response.CalculationResponse
import com.example.buncisapp.databinding.ActivityCalculatorBinding
import com.example.buncisapp.views.ViewModelFactory
import com.example.buncisapp.views.auth.LoginActivity
import com.example.buncisapp.views.record.RecordActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class CalculatorActivity : AppCompatActivity() {

    private lateinit var calculatorViewModel: CalculatorViewModel
    private var listOfTank = mutableListOf<SoundingItems>()
    private var listNoTanki = mutableListOf<String>()
    private lateinit var rvSounding : RecyclerView
    private lateinit var binding: ActivityCalculatorBinding
    private lateinit var biodata: Biodata

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalculatorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewModel()

        calculatorViewModel.getShip().observe(this) { ship ->
            calculatorViewModel.noTanki(ship.token)
        }

        rvSounding = binding.rvListTangki
        rvSounding.setHasFixedSize(true)
        setRecycleView()

        val switchButton = binding.switch1

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.layoutSounding.visibility = View.GONE
                binding.edVolume.visibility = View.VISIBLE
            } else {
                binding.layoutSounding.visibility = View.VISIBLE
                binding.edVolume.visibility = View.GONE
            }
        }

        biodata = intent.getParcelableExtra("data")!!

        val adapter = ArrayAdapter(this, R.layout.dropdown_items, getNoTanki())
        binding.edNomorTangki.setAdapter(adapter)

        if (biodata != null) {
            binding.edTrim.setText(biodata?.draft.toString())
        }

        binding.btnCalculate.setOnClickListener {
            calculateAndDisplayResult()
        }

        binding.btnTambah.setOnClickListener {
            addNewItem()
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
                    listOfTank)
            }

            val intent = Intent(this@CalculatorActivity, RecordActivity::class.java)
            calculatorViewModel.data.observe(this){
                intent.putExtra("bunkerData",it)
            }
            startActivity(intent)



        }

        binding.lvToolbar.btnAccount.setOnClickListener {
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
                }
                .show()
        }
    }

    private fun setRecycleView() {
        rvSounding.layoutManager = LinearLayoutManager(this)
        val soundingItemAdapter  = CalculatorAdapter(listOfTank)
        soundingItemAdapter.notifyDataSetChanged()
        binding.rvListTangki.adapter = soundingItemAdapter
    }

    private fun calculateAndDisplayResult() {
        val switchButton = binding.switch1

        val sounding1: Double
        val sounding2: Double
        val sounding3: Double

        if (switchButton.isChecked) {
            // Jika switch dinyalakan, set nilai sounding ke 0
            sounding1 = 0.0
            sounding2 = 0.0
            sounding3 = 0.0
            // Sembunyikan tampilan untuk pengisian sounding
            binding.layoutSounding.visibility = View.GONE
        } else {
            // Jika switch dimatikan, ambil nilai dari inputan
            sounding1 = binding.edSounding1.text.toString().toDoubleOrNull() ?: 0.0
            sounding2 = binding.edSounding2.text.toString().toDoubleOrNull() ?: 0.0
            sounding3 = binding.edSounding3.text.toString().toDoubleOrNull() ?: 0.0
            // Tampilkan tampilan untuk pengisian sounding
            binding.layoutSounding.visibility = View.VISIBLE
        }

        // Lanjutkan dengan menghitung hasil sesuai dengan nilai sounding yang sudah diatur
        val volume: Double = if (switchButton.isChecked) {
            binding.edVolume.text.toString().toDoubleOrNull() ?: 0.0
        } else {
            0.0
        }

        val sum = sounding1 + sounding2 + sounding3
        val average = sum / 3

        if (delta(sounding1, sounding2) > 3 || delta(sounding2, sounding3) > 3 || delta(sounding1, sounding3)>3) {
            // Selisih lebih dari 3, memerlukan pengisian sounding4 dan sounding5
            val sounding4 = binding.edSounding4.text.toString().toDoubleOrNull() ?: 0.0
            val sounding5 = binding.edSounding5.text.toString().toDoubleOrNull() ?: 0.0

            // Validasi jika sounding4 atau sounding5 tidak diisi
            if (sounding4 == 0.0 || sounding5 == 0.0) {
                Toast.makeText(this, "Sounding 4 dan Sounding 5 harus diisi", Toast.LENGTH_SHORT).show()
//                binding.edSounding4.background = ResourcesCompat.getDrawable()
                return
            }

            val sumWith5Soundings = sum + sounding4 + sounding5
            val averageWith5Soundings = sumWith5Soundings / 5
            calculatorViewModel.getShip().observe(this) { user ->
                val data = intent.getParcelableExtra<Biodata>("data")
                if (data != null) {
                    calculatorViewModel.calculation(
                        user.token,
                        data.draft,
                        binding.edNomorTangki.text.toString(),
                        averageWith5Soundings.toInt(), // Menggunakan rata-rata dengan 5 sounding
                        volume
                    )
                    calculatorViewModel.calculation.observe(this) { result ->
                        setHasil(result)
                    }
                }
            }
        } else {
            calculatorViewModel.getShip().observe(this) { user ->
                val data = intent.getParcelableExtra<Biodata>("data")
                if (data != null) {
                    calculatorViewModel.calculation(
                        user.token,
                        data.draft,
                        binding.edNomorTangki.text.toString(),
                        average.toInt(),
                        volume
                    )
                    calculatorViewModel.calculation.observe(this) { result ->
                        setHasil(result) // Gunakan average
                    }
                }
            }
        }
    }


    private fun addNewItem() {
        val switchButton = binding.switch1

        val sounding1: Double
        val sounding2: Double
        val sounding3: Double

        if (switchButton.isChecked) {
            // Jika switch dinyalakan, set nilai sounding ke 0
            sounding1 = 0.0
            sounding2 = 0.0
            sounding3 = 0.0
            // Sembunyikan tampilan untuk pengisian sounding
            binding.layoutSounding.visibility = View.GONE
        } else {
            // Jika switch dimatikan, ambil nilai dari inputan
            sounding1 = binding.edSounding1.text.toString().toDoubleOrNull() ?: 0.0
            sounding2 = binding.edSounding2.text.toString().toDoubleOrNull() ?: 0.0
            sounding3 = binding.edSounding3.text.toString().toDoubleOrNull() ?: 0.0
            // Tampilkan tampilan untuk pengisian sounding
            binding.layoutSounding.visibility = View.VISIBLE
        }

        // Lanjutkan dengan menghitung hasil sesuai dengan nilai sounding yang sudah diatur
        val volume: Double = if (switchButton.isChecked) {
            binding.edVolume.text.toString().toDoubleOrNull() ?: 0.0
        } else {
            0.0
        }

        val sum = sounding1 + sounding2 + sounding3
        val average = sum / 3

        // Validasi tambahan untuk memeriksa selisih sounding
        val soundingDifference = delta(sounding1, sounding2) + delta(sounding2, sounding3) + delta(sounding1, sounding3)
        if (soundingDifference > 3) {
            // Selisih lebih dari 3, memerlukan pengisian sounding4 dan sounding5
            val sounding4 = binding.edSounding4.text.toString().toDoubleOrNull() ?: 0.0
            val sounding5 = binding.edSounding5.text.toString().toDoubleOrNull() ?: 0.0

            // Validasi jika sounding4 atau sounding5 tidak diisi
            if (sounding4 == 0.0 || sounding5 == 0.0) {
                Toast.makeText(this, "Sounding 4 dan Sounding 5 harus diisi", Toast.LENGTH_SHORT).show()
                return // Keluar dari fungsi jika validasi tidak terpenuhi
            }

            // Menghitung rata-rata dengan 5 nilai sounding
            val sumWith5Soundings = sum + sounding4 + sounding5
            val averageWith5Soundings = sumWith5Soundings / 5

            val newItem = SoundingItems(
                levelSounding = averageWith5Soundings, // Gunakan rata-rata dengan 5 sounding
                nomorTanki = binding.edNomorTangki.text.toString(),
                volume = volume
            )
            listOfTank.add(newItem)
        } else {
            // Menghitung rata-rata dengan 3 nilai sounding
            val newItem = SoundingItems(
                levelSounding = average, // Gunakan rata-rata dengan 3 sounding
                nomorTanki = binding.edNomorTangki.text.toString(),
                volume = volume
            )
            listOfTank.add(newItem)
        }

        // Membersihkan inputan setelah item baru ditambahkan
        binding.edSounding1.text?.clear()
        binding.edSounding2.text?.clear()
        binding.edSounding3.text?.clear()
        binding.edSounding4.text?.clear()
        binding.edSounding5.text?.clear()
        binding.edVolume.text?.clear()
        Log.e("test", listOfTank.toString())
        Toast.makeText(this@CalculatorActivity, "Data Berhasil Ditambahkan!", Toast.LENGTH_SHORT).show()
    }


    private fun setHasil(result: CalculationResponse) {
        binding.tvResult.text = result.data?.volume.toString()
    }

    private fun setupViewModel() {
        calculatorViewModel = ViewModelProvider(
            this,
            ViewModelFactory(ShipPreference.getInstance(dataStore), this)
        )[CalculatorViewModel::class.java]
    }

    private fun getNoTanki(): List<String> {
        calculatorViewModel.noTanki.observe(this) { items ->
            for (i in items) {
                if (i != null) {
                    listNoTanki.add(i)
                }
            }
        }
        return listNoTanki
    }

    private fun delta(param1: Double, param2: Double): Double {
        return if (param2 > param1) {
            param2 - param1
        } else {
            param1 - param2
        }
    }

    private fun showDialog(data: ArrayList<String>) {
        // Mock method
        var stringFormat = ""
        for (i in data) {
            stringFormat += "$i\n"
        }

        val builder = MaterialAlertDialogBuilder(this@CalculatorActivity)
        builder
            .setTitle("Peringatan!")
            .setMessage("List Tangki yang belum terisi: \n$stringFormat ")
            .setPositiveButton("Lanjut") { _, _ ->
                val intent = Intent(
                    this@CalculatorActivity,
                    RecordActivity::class.java
                )
                startActivity(intent)
            }
            .setNegativeButton("Kembali") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        builder.show()
    }
}
