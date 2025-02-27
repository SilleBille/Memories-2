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

package com.mkd.memories.timeline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkd.memories.core.auth.util.User
import com.nextcloud.android.sso.model.SingleSignOnAccount
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class TimelineUIState(
    val selectedAccount: SingleSignOnAccount?,
) {
    companion object {
        val Initial = TimelineUIState(
            selectedAccount = null
        )
    }
}

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val user: User,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineUIState.Initial)
    val uiState = _uiState
        .onStart { initializeTimelineScreen() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TimelineUIState.Initial)

    private fun initializeTimelineScreen() {
        _uiState.value = _uiState.value.copy(selectedAccount = user.account)
    }
}
