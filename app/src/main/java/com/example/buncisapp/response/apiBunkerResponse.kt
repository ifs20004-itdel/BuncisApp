package com.example.buncisapp.response

import com.google.gson.annotations.SerializedName

data class SoundingItem(

	@field:SerializedName("level_sounding")
	val levelSounding: Int? = null,

	@field:SerializedName("nomor_tanki")
	val nomorTanki: String? = null
)

data class ApiBunkerResponse(

	@field:SerializedName("pelabuhan")
	val pelabuhan: String? = null,

	@field:SerializedName("waktu_sounding")
	val waktuSounding: String? = null,

	@field:SerializedName("trim")
	val trim: Any? = null,

	@field:SerializedName("draft_belakang")
	val draftBelakang: Int? = null,

	@field:SerializedName("tanggal_sounding")
	val tanggalSounding: String? = null,

	@field:SerializedName("jenis_BBM")
	val jenisBBM: String? = null,

	@field:SerializedName("sounding")
	val sounding: List<SoundingItem?>? = null,

	@field:SerializedName("draft_depan")
	val draftDepan: Int? = null,

	@field:SerializedName("draft_tengah")
	val draftTengah: Int? = null,

	@field:SerializedName("heel_correction")
	val heelCorrection: Int? = null
)
