package com.example.buncisapp.utils

import com.google.gson.JsonParser

object ExtractMessage {
    fun extractMessage(responseBody: String): String {
        val jsonParser = JsonParser.parseString(responseBody).asJsonObject
        return jsonParser.get("message").asString
    }
}