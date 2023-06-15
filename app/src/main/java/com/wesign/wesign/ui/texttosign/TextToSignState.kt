package com.wesign.wesign.ui.texttosign

import com.wesign.wesign.data.entity.TextToSignResponse

data class TextToSignState(
    val listWord: TextToSignResponse? = null,
    val selectedWord: List<TextToSignResponse.SignWord> = emptyList(),
    val isLoading: Boolean = false,
    val isTryAgain: Boolean = false,
)