package com.example.buncisapp.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class RobResponse(

    @field:SerializedName("data")
    val data: @RawValue RobData? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null
) : Parcelable

@Parcelize
data class Vessel(

    @field:SerializedName("code")
    val code: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("chief_engineer")
    val chiefEngineer: String? = null,

    @field:SerializedName("first_engineer")
    val firstEngineer: String? = null,

    @field:SerializedName("master")
    val master: String? = null
) : Parcelable

@Parcelize
data class SoundingLevelsItem(

    @field:SerializedName("volume")
    val volume: @RawValue Any? = null,

    @field:SerializedName("level")
    val level: @RawValue Any? = null,

    @field:SerializedName("fuel_tank")
    val fuelTank: String? = null
) : Parcelable

@Parcelize
data class RobData(

    @field:SerializedName("third_signer_name")
    val thirdSignerName: String? = null,

    @field:SerializedName("back_draft")
    val backDraft: @RawValue Any? = null,

    @field:SerializedName("fourth_signer_name")
    val fourthSignerName: Boolean? = null,

    @field:SerializedName("first_signer_name")
    val firstSignerName: String? = null,

    @field:SerializedName("second_signer_name")
    val secondSignerName: String? = null,

    @field:SerializedName("ship_condition")
    val shipCondition: String? = null,

    @field:SerializedName("sounding_datetime")
    val soundingDatetime: String? = null,

    @field:SerializedName("front_draft")
    val frontDraft: @RawValue Any? = null,

    @field:SerializedName("sounding_levels")
    val soundingLevels: List<SoundingLevelsItem?>,

    @field:SerializedName("fourth_signer_position")
    val fourthSignerPosition: Boolean? = null,

    @field:SerializedName("middle_draft")
    val middleDraft: @RawValue Any? = null,

    @field:SerializedName("trim")
    val trim: @RawValue Any? = null,

    @field:SerializedName("total_volume")
    val total: @RawValue Any? = null,

    @field:SerializedName("port")
    val port: String? = null,

    @field:SerializedName("vessel")
    val vessel: Vessel? = null,

    @field:SerializedName("third_signer_position")
    val thirdSignerPosition: String? = null,

    @field:SerializedName("second_signer_position")
    val secondSignerPosition: String? = null,

    @field:SerializedName("fuel_type")
    val fuelType: String? = null,

    @field:SerializedName("heel_correction")
    val heelCorrection: @RawValue Any? = null,

    @field:SerializedName("first_signer_position")
    val firstSignerPosition: String? = null
) : Parcelable
