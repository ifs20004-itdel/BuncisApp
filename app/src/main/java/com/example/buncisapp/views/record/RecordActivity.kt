package com.example.buncisapp.views.record

import android.Manifest
import android.content.Intent
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
import java.util.TimeZone

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

            val timeIn= time?.let { convertUTCToLocalTime(it,"Asia/Jakarta") }
            binding.rsDate.text = date
            binding.rsTime.text = timeIn
            binding.rsPort.text = data.data?.port
            binding.rsGradeOfBunker.text = data.data?.fuelType
            binding.rsFore.text = data.data?.frontDraft.toString()
            binding.rsMiddle.text = data.data?.middleDraft.toString()
            binding.tvTotal.text = String.format("%.3f", data.data?.total)
            binding.rsAft.text = data.data?.backDraft.toString()
            binding.rsTrim.text = data.data?.trim.toString()
            binding.representativeName.text = data.data?.firstSignerName
            binding.masterName.text = data.data?.secondSignerName
            binding.ceName.text = data.data?.thirdSignerName
            binding.rsHeelInfo.text = "(No Heel)"
            val fuelTankStringBuilder = StringBuilder()
            val levelStringBuilder = StringBuilder()
            val volumeStringBuilder = StringBuilder()
            val trimInfo= data.data?.trim

            if (trimInfo is Int || trimInfo is Double || trimInfo is Float) {
                val trimValue = when (trimInfo) {
                    is Int -> trimInfo.toDouble()
                    is Double -> trimInfo
                    is Float -> trimInfo.toDouble()
                    else -> 0.0 // Handle other numerical types
                }

                if (trimValue < 0) {
                    binding.rsTrimInfo.text = "(Trim by Head)"
                } else if (trimValue > 0) {
                    binding.rsTrimInfo.text = "(Trim by Stern)"
                } else if (trimValue == 0.0) {
                    binding.rsTrimInfo.text = "(Even Keel)"
                }
            } else {
                // Handle the case where trimInfo is not a valid numerical type
                binding.rsTrimInfo.text = "(Invalid TrimInfo)"
            }

            for (soundingLevel in data.data?.soundingLevels!!) {
                if (soundingLevel != null && !soundingLevel.fuelTank.isNullOrBlank()) {
                    // Format sounding and volume with two decimal places
                    val formattedSounding = String.format("%.2f", soundingLevel.level)
                    val formattedVolume = String.format("%.3f", soundingLevel.volume)

                    fuelTankStringBuilder.append(soundingLevel.fuelTank)
                    fuelTankStringBuilder.append("\n\n")
                    levelStringBuilder.append(formattedSounding)
                    levelStringBuilder.append("\n")
                    volumeStringBuilder.append(formattedVolume)
                    volumeStringBuilder.append("\n")
                }
                if (fuelTankStringBuilder.isNotEmpty()) {
                    fuelTankStringBuilder.deleteCharAt(fuelTankStringBuilder.length - 1)
                } else if (levelStringBuilder.isNotEmpty()) {
                    levelStringBuilder.deleteCharAt(levelStringBuilder.length - 2)
                } else if (volumeStringBuilder.isNotEmpty()) {
                    volumeStringBuilder.deleteCharAt(volumeStringBuilder.length - 2)
                }

                binding.fuelTankName.text = fuelTankStringBuilder.toString()
                binding.sounding.text = levelStringBuilder.toString()
                binding.volume.text = volumeStringBuilder.toString()
            }

            binding.btnUpload.setOnClickListener {
                convertXMLtoPDF()
            }
        }
    }

    private fun convertUTCToLocalTime(utcTime: String, localTimeZoneId: String): String {
        val utcFormat = SimpleDateFormat("HH:mm:ss")
        utcFormat.timeZone = TimeZone.getTimeZone("UTC")

        val localFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        localFormat.timeZone = TimeZone.getTimeZone(localTimeZoneId)

        val utcDateTime = utcFormat.parse(utcTime)
        return localFormat.format(utcDateTime)
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

        val data = intent.getParcelableExtra<RobResponse>("bunkerData")
        val nameVessel= data?.data?.vessel?.name
        val dateSounding = data?.data?.soundingDatetime?.substring(0, 10)
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val fileName = "ROB-$nameVessel-$dateSounding.pdf"
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


