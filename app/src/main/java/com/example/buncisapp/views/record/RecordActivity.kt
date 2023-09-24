package com.example.buncisapp.views.record

import android.Manifest
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.buncisapp.R
import com.example.buncisapp.data.response.RobResponse
import com.example.buncisapp.databinding.ActivityRecordBinding
import com.example.buncisapp.views.inputData.InputDataActivity
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RecordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecordBinding
    private val REQUEST_CODE_PERMISSIONS = 1232

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        askPermission()

        val data = intent.getParcelableExtra<RobResponse>("bunkerData")
        if (data != null) {
            binding.shipCondition.text = data.data?.shipCondition
            binding.rsNameOfVessel.text = data.data?.vessel?.name

            val date = data.data?.soundingDatetime?.substring(0, 10)
            val time = data.data?.soundingDatetime?.substring(11, 19)

            binding.rsDate.text = date
            binding.rsTime.text = time
            binding.rsPort.text = data.data?.port
            binding.rsGradeOfBunker.text = data.data?.fuelType
            binding.rsFore.text = data.data?.frontDraft.toString()
            binding.rsMiddle.text = data.data?.middleDraft.toString()
            binding.rsAft.text = data.data?.backDraft.toString()
            binding.rsTrim.text = data.data?.trim.toString()
            binding.representativeName.text = data.data?.firstSignerName
            binding.masterName.text = data.data?.secondSignerName
            binding.ceName.text = data.data?.thirdSignerName
            val fuelTankStringBuilder = StringBuilder()
            val levelStringBuilder = StringBuilder()
            val volumeStringBuilder = StringBuilder()

            for (soundingLevel in data.data?.soundingLevels!!) {
                if (soundingLevel != null && !soundingLevel.fuelTank.isNullOrBlank()) {
                    // Tambahkan fuelTank ke string dengan tanda koma sebagai pemisah
                    fuelTankStringBuilder.append(soundingLevel.fuelTank)
                    fuelTankStringBuilder.append("\n\n")
                    levelStringBuilder.append(soundingLevel.level)
                    levelStringBuilder.append("\n")
                    volumeStringBuilder.append(soundingLevel.volume)
                    volumeStringBuilder.append("\n")
                }
                // Hapus tanda koma ekstra di akhir string jika ada
                if (fuelTankStringBuilder.isNotEmpty()) {
                    fuelTankStringBuilder.deleteCharAt(fuelTankStringBuilder.length - 1)
                } else if (levelStringBuilder.isNotEmpty()) {
                    levelStringBuilder.deleteCharAt(levelStringBuilder.length - 2)
                } else if (volumeStringBuilder.isNotEmpty()) {
                    volumeStringBuilder.deleteCharAt(volumeStringBuilder.length - 2)
                }

                // Set nilai fuelTankName dengan string yang berisi fuelTank yang dipisahkan oleh koma
                binding.fuelTankName.text = fuelTankStringBuilder.toString()
                binding.sounding.text = levelStringBuilder.toString()
                binding.volume.text = volumeStringBuilder.toString()
            }
            binding.btnUpload.setOnClickListener {
                convertXMLtoPDF()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, InputDataActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun askPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            REQUEST_CODE_PERMISSIONS
        )
    }

    private fun createPDF() {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(1080, 1920, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()
        paint.color = Color.RED
        paint.textSize = 42f

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "example.pdf"
        val file = File(downloadDir, fileName)

        try {
            val fos = FileOutputStream(file)
            document.writeTo(fos)
            document.close()
            fos.close()
            Toast.makeText(
                this,
                "Conversion successful. PDF saved in $downloadDir",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun convertXMLtoPDF() {
        val view = findViewById<View>(R.id.surat)
        val displayMetrics = DisplayMetrics()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            display?.getRealMetrics(displayMetrics)
        } else {
            windowManager.defaultDisplay.getMetrics(displayMetrics)
        }

        view.measure(
            View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY)
        )

        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels)
        val document = PdfDocument()


        val pageInfo = PdfDocument.PageInfo.Builder(1080, 1920, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        view.draw(canvas)

        document.finishPage(page)

        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val currentDateTime = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(Date())
        val fileName = "ROB_$currentDateTime.pdf"
        val file = File(downloadDir, fileName)

        try {
            val fos = FileOutputStream(file)
            document.writeTo(fos)
            document.close()
            fos.close()
            Toast.makeText(
                this,
                "Conversion successful. PDF saved in $downloadDir",
                Toast.LENGTH_SHORT
            ).show()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}