package com.mkd.memories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.mkd.memories.login.navigation.navigateToLogin
import com.mkd.memories.ui.MemoriesApp
import com.mkd.memories.ui.rememberMemoriesAppState
import com.mkd.mkd.designsystem.theme.MemoriesTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val isUserSignedIn by viewModel.isUserSignedIn.collectAsStateWithLifecycle()

            val appState = rememberMemoriesAppState(
                isUserLoggedIn = isUserSignedIn,
                windowSizeClass = calculateWindowSizeClass(this)
            )

            MemoriesTheme {
                MemoriesApp(appState)
            }

            LaunchedEffect(this@MainActivity) {
                launchLoginObserver(appState.navController)
            }
        }
    }

    private fun launchLoginObserver(navHostController: NavHostController) {
        lifecycleScope.launch {
            viewModel.isUserSignedIn.collect { isUserSignedIn ->
                if (!isUserSignedIn) {
                    navHostController.navigateToLogin(
                        navOptions {
                            popUpTo(0) // reset backstack
                        }
                    )
                }
            }
        }
    }
}
