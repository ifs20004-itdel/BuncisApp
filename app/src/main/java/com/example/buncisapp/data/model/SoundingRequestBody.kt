package com.example.buncisapp.data.model
import com.squareup.moshi.Json

data class SoundingRequestBody(
    @Json(name = "pelabuhan") val pelabuhan: String,
    @Json(name = "tanggal_sounding") val tanggal: String,
    @Json(name = "waktu_sounding") val waktu: String,
    @Json(name = "jenis_BBM") val bbm: String,
    @Json(name = "draft_depan") val depan: Double,
    @Json(name = "draft_tengah") val tengah: Double,
    @Json(name = "ship_condition") val kondisi: String,
    @Json(name = "draft_belakang") val belakang: Double,
    @Json(name = "heel_correction") val heel: Int,
    @Json(name = "trim") val trim: Double,
    @Json(name = "sounding") val sounding: List<SoundingItem>
)