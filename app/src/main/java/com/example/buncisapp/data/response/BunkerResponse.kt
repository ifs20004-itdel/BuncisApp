package com.example.buncisapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue
import retrofit2.http.FormUrlEncoded

@Parcelize
data class BunkerResponse(

	@field:SerializedName("data")
	val data: @RawValue Data,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("status")
	val status: Int
): Parcelable

data class Data(

	@field:SerializedName("sounding_levels")
	val soundingLevels: List<SoundingLevelsItem>,

	@field:SerializedName("middle_draft")
	val middleDraft: Any,

	@field:SerializedName("back_draft")
	val backDraft: Any,

	@field:SerializedName("trim")
	val trim: Any,

	@field:SerializedName("port")
	val port: String,

	@field:SerializedName("vessel")
	val vessel: Vessel,

	@field:SerializedName("ship_condition")
	val shipCondition: String,

	@field:SerializedName("sounding_datetime")
	val soundingDatetime: String,

	@field:SerializedName("fuel_type")
	val fuelType: String,

	@field:SerializedName("front_draft")
	val frontDraft: Any,

	@field:SerializedName("heel_correction")
	val heelCorrection: Any
)

data class SoundingLevelsItem(

	@field:SerializedName("volume")
	val volume: Any,

	@field:SerializedName("level")
	val level: Int,

	@field:SerializedName("fuel_tank")
	val fuelTank: String
)


data class Vessel(

	@field:SerializedName("code")
	val code: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("chief_engineer")
	val chiefEngineer: String,

	@field:SerializedName("first_engineer")
	val firstEngineer: String,

	@field:SerializedName("master")
	val master: String
)

//import com.google.gson.annotations.SerializedName
//
//
//data class BunkerResponse(
//
//    @field:SerializedName("pelabuhan")
//	val pelabuhan: String? = null,
//
//    @field:SerializedName("waktu_sounding")
//	val waktuSounding: String? = null,
//
//    @field:SerializedName("trim")
//	val trim: Any? = null,
//
//    @field:SerializedName("draft_belakang")
//	val draftBelakang: Int? = null,
//
//    @field:SerializedName("tanggal_sounding")
//	val tanggalSounding: String? = null,
//
//    @field:SerializedName("jenis_BBM")
//	val jenisBBM: String? = null,
//
//    @field:SerializedName("sounding")
//	val sounding: List<SoundingItem?>? = null,
//
//    @field:SerializedName("draft_depan")
//	val draftDepan: Int? = null,
//
//    @field:SerializedName("draft_tengah")
//	val draftTengah: Int? = null,
//
//    @field:SerializedName("heel_correction")
//	val heelCorrection: Int? = null
//)
