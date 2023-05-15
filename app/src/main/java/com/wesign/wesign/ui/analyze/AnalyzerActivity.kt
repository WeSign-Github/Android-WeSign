package com.wesign.wesign.ui.analyze

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.wesign.wesign.ui.theme.WeSignTheme

class AnalyzerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeSignTheme() {
                AnalyzerRoute(onNavigateUp = { finishActivity(1) })
            }
        }
    }
}