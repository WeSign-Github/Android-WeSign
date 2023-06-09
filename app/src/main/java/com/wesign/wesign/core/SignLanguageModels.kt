package com.wesign.wesign.core

sealed class SignLanguageModels(val filename: String) {
    object BISINDO_NEW : SignLanguageModels("metadata-bisindo-new.tflite")
    object BISINDO : SignLanguageModels("metadata-bisindo.tflite")
    object SIBI : SignLanguageModels("metadata-sibi-all.tflite")
}
