package com.mkd.memories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mkd.memories.ui.MemoriesApp
import com.mkd.mkd.designsystem.theme.Memories2Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Memories2Theme {
                MemoriesApp()
            }
        }
    }
}
