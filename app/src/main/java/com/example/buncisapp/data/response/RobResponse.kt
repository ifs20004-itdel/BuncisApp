package com.example.buncisapp.data.response

data class RobResponse(
	val data: Data? = null,
	val message: String? = null,
	val status: Int? = null
)

data class Data(
	val thirdSignerName: String? = null,
	val backDraft: Any? = null,
	val fourthSignerName: Boolean? = null,
	val firstSignerName: String? = null,
	val secondSignerName: String? = null,
	val shipCondition: String? = null,
	val soundingDatetime: String? = null,
	val frontDraft: Any? = null,
	val soundingLevels: List<SoundingLevelsItem?>? = null,
	val fourthSignerPosition: Boolean? = null,
	val middleDraft: Any? = null,
	val trim: Any? = null,
	val port: String? = null,
	val vessel: Vessel? = null,
	val thirdSignerPosition: String? = null,
	val secondSignerPosition: String? = null,
	val fuelType: String? = null,
	val heelCorrection: Any? = null,
	val firstSignerPosition: String? = null
)

data class SoundingLevelsItem(
	val volume: Any? = null,
	val level: Int? = null,
	val fuelTank: String? = null
)

data class Vessel(
	val code: String? = null,
	val name: String? = null,
	val chiefEngineer: String? = null,
	val firstEngineer: String? = null,
	val master: String? = null
)

