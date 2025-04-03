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

package com.mkd.memories.timeline.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mkd.memories.sync.data.TimelineRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

data class TimelineUIState(
    val days: Map<String, Int> = mapOf(),
)

@HiltViewModel
class TimelineViewModel @Inject constructor(
    private val timelineRepository: TimelineRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TimelineUIState())
    val uiState = _uiState
        .onStart { initializeTimelineScreen() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TimelineUIState())

    private fun initializeTimelineScreen() {
        timelineRepository.getDays().onEach { days ->
            _uiState.update { it.copy(days = days.associate { getFormattedDate(it.dayId) to it.count.toInt() }) }
        }.launchIn(viewModelScope)


    }

    private fun getFormattedDate(dayId: Long): String {
        val epochTime = dayId * 86400
        val instant = Instant.ofEpochSecond(epochTime)
        val date = instant.atZone(ZoneOffset.UTC).toLocalDate()
        return DateTimeFormatter
            .ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale.getDefault())
            .format(date)
    }
}
