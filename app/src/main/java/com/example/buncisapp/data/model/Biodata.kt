package com.example.buncisapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Biodata (
    val nama: String,
    val kondisiKapal : String,
    val tanggal : String,
    val bahanBakar: String,
    val waktu: String,
    val draft: String,
    val depan: String,
    val tengah: String,
    val belakang:String
    ): Parcelable