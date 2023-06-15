package com.wesign.wesign.core

sealed class SignLanguageModels(val filename: String, val name: String) {
    object BISINDO_NEW : SignLanguageModels("metadata-bisindo-new.tflite","BISINDO")
    object BISINDO : SignLanguageModels("metadata-bisindo.tflite","BISINDO")
    object SIBI : SignLanguageModels("metadata-sibi-all.tflite", "SIBI")
}
