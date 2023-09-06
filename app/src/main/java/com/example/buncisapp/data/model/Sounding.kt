package com.example.buncisapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sounding(
    var nomorTanki: String = "",
    var levelSounding: String = ""
) : Parcelable