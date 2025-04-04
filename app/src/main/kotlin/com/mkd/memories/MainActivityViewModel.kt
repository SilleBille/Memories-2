/*
 * Copyright 2025 SilleBille
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

package com.mkd.memories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkd.memories.core.auth.util.User
import com.mkd.memories.sync.SyncManagerScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    user: User,
    private val syncManagerScheduler: SyncManagerScheduler,
) : ViewModel() {

    val isUserSignedIn: StateFlow<Boolean> = user.isUserSignedIn.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = false
    )

    private var hasSyncTriggered = false // Tracks whether WorkManager has been triggered

    fun triggerSync() {
        if (!hasSyncTriggered) {
            hasSyncTriggered = true
            syncManagerScheduler.scheduleSync()
        }
    }

    fun resetSync() {
        hasSyncTriggered = false
    }
}
