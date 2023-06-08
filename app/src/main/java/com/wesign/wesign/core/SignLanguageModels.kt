package com.wesign.wesign.core

sealed class SignLanguageModels(val filename: String) {
    object BISINDO : SignLanguageModels("metadata-bisindo.tflite")
    object SIBI : SignLanguageModels("metadata-sibi-all.tflite")
}
