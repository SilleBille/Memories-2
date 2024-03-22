package com.mkd.memories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.mkd.memories.ui.MemoriesApp
import com.mkd.memories.ui.rememberMemoriesAppState
import com.mkd.mkd.designsystem.theme.MemoriesTheme
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val appState = rememberMemoriesAppState(
                windowSizeClass = calculateWindowSizeClass(this)
            )

            MemoriesTheme {
                MemoriesApp(appState)
            }
        }
    }
}
