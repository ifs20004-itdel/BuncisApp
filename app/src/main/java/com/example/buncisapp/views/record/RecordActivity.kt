package com.example.buncisapp.views.record

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.Page
import android.graphics.pdf.PdfDocument.PageInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.buncisapp.R
import com.example.buncisapp.databinding.ActivityRecordBinding
import java.io.File
import java.io.FileOutputStream

class RecordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRecordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        printPDF()
    }

    private fun printPDF() {
        val myPdfDocument = PdfDocument()
        val paint = Paint()
        val myPageInfo = PageInfo.Builder(250, 350, 1).create()
        val myPage: Page = myPdfDocument.startPage(myPageInfo)
        val canvas: Canvas = myPage.canvas

        canvas.drawText("HEY HEY", 20F, 20F,paint)

        myPdfDocument.finishPage(myPage)

        val file = File(this.getExternalFilesDir(null), "contoh.pdf")
        val fileOutputStream = FileOutputStream(file)
        myPdfDocument.writeTo(fileOutputStream)
        fileOutputStream.close()

        myPdfDocument.close()
    }
}
