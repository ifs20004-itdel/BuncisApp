package com.example.buncisapp.utils

object ExtractMessage {
    fun extractMessage(responseBody: String): String{
        val inputText = """
            $responseBody
        """.trimIndent()
        val messageIndex = inputText.indexOf("\"message\"")
        if (messageIndex != -1) {
            val messageText = inputText.substring(messageIndex + "\"message\":".length)
            return messageText.trim().removePrefix(":").trim()
        }
        return ""
    }
}