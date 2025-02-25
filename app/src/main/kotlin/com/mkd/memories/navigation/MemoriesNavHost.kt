/*
 * Copyright 2024 SilleBille
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 *
 */

package com.mkd.memories.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import com.mkd.memories.login.navigation.LoginRoute
import com.mkd.memories.login.navigation.loginScreen
import com.mkd.memories.timeline.navigation.navigateToTimeline
import com.mkd.memories.timeline.navigation.timelineScreen
import com.mkd.memories.ui.MemoriesAppState

@Composable
fun MemoriesNavHost(
    appState: MemoriesAppState,
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = LoginRoute
    ) {
        loginScreen(onAccountSelected = navController::navigateToTimeline)

        timelineScreen()
    }
}
