package com.example.buncisapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Hasil(
    val token: String,
    val pelabuhan: String,
    val tanggal: String,
    val waktu: String,
    val bbm: String,
    val depan: String,
    val tengah: String,
    val kondisi: String,
    val belakang: String,
    val trim: String,
    val sounding: @RawValue MutableSet<SoundingItem>
) : Parcelable