package com.example.buncisapp.views.record

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.buncisapp.R
import com.example.buncisapp.databinding.ActivityRecordBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class RecordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordBinding
    private val REQUEST_CODE_PERMISSIONS = 1001
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnUpload.setOnClickListener {
            if (arePermissionsGranted()) {
                printPDF()
            } else {
                requestPermissions()
            }
        }
        printPDF()
    }

    private fun arePermissionsGranted(): Boolean {
        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (arePermissionsGranted()) {
                printPDF()
            } else {
                Toast.makeText(this, "Permissions are required to save the PDF.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun printPDF() {
        val view = findViewById<View>(R.id.surat)
        view.measure(View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(1920, View.MeasureSpec.EXACTLY))
        view.layout(0, 0, view.measuredWidth, view.measuredHeight)

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(1080, 1920, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        view.draw(canvas)
        document.finishPage(page)

        val downloadDir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val numberOfFilesToCreate = 1000

        for (i in 1..numberOfFilesToCreate) {
            val fileName = "doc_$i.pdf"
            val file = File(downloadDir, fileName)

            try {
                val fos = FileOutputStream(file)
                document.writeTo(fos)
                document.close()
                fos.close()
                Log.e("iniii","$downloadDir")
                Toast.makeText(this, "Conversion successful. PDF saved in $downloadDir", Toast.LENGTH_SHORT).show()
            } catch (e: IOException) {
                throw RuntimeException(e)
            }
        }


    }
}
