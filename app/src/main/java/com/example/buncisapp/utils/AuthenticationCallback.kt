package com.example.buncisapp.utils

interface AuthenticationCallback {
    fun onError(isLogin: Int?, message: String?)
}