//package com.example.buncisapp.data.response
//
//import android.os.Parcelable
//import kotlinx.parcelize.Parcelize
//import kotlinx.parcelize.RawValue
//
//@Parcelize
//data class Rob2Response(
//	val data: @RawValue DataRob,
//	val message: String? = null,
//	val status: Int? = null
//): Parcelable
//
//@Parcelize
//data class DataRob(
//	val thirdSignerName: String? = null,
//	val backDraft: @RawValue Any? = null,
//	val fourthSignerName: Boolean? = null,
//	val firstSignerName: String? = null,
//	val secondSignerName: String? = null,
//	val shipCondition: String? = null,
//	val soundingDatetime: String? = null,
//	val frontDraft: @RawValue Any? = null,
//	val soundingLevels: List<SoundingLevelsItems?>? = null,
//	val fourthSignerPosition: Boolean? = null,
//	val middleDraft: @RawValue Any? = null,
//	val trim: @RawValue Any? = null,
//	val port: String? = null,
//	val vessel: Vessels,
//	val thirdSignerPosition: String? = null,
//	val secondSignerPosition: String? = null,
//	val fuelType: String? = null,
//	val heelCorrection: @RawValue Any? = null,
//	val firstSignerPosition: String? = null
//): Parcelable
//
//@Parcelize
//data class SoundingLevelsItems(
//	val volume: @RawValue Any? = null,
//	val level: Int? = null,
//	val fuelTank: String? = null
//): Parcelable
//
//@Parcelize
//data class Vessels(
//	val code: String? = null,
//	val name: String? = null,
//	val chiefEngineer: String? = null,
//	val firstEngineer: String? = null,
//	val master: String? = null
//): Parcelable
//
