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

@file:OptIn(ExperimentalMaterial3Api::class)

package com.mkd.memories.timeline.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mkd.memories.designsystem.theme.scrollbar.Scrollbar

@Composable
internal fun TimelineRoute(
    viewModel: TimelineViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    //val dayContents = viewModel.dayContentsPagingFlow.collectAsLazyPagingItems()
    //viewModel.fetchPreview(dayContents.itemSnapshotList.items)
    TimelineScreen(
        timelineData = uiState.days,
        isRefreshing = uiState.isRefreshing,
        onRefresh = viewModel::refresh,
        modifier = Modifier.fillMaxSize(),
    )

}

@Composable
private fun TimelineScreen(
    timelineData: Map<String, Int>,
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
) {

    PullToRefreshBox(
        modifier = modifier,
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
    ) {
        if (timelineData.isEmpty()) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(32.dp)
            )
        } else {
            val gridType by remember { mutableStateOf(GridCells.Adaptive(120.dp)) }
            val lazyGridState = rememberLazyGridState()
            LazyVerticalGrid(
                modifier = Modifier.padding(vertical = 24.dp),
                columns = gridType,
                state = lazyGridState,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                // TODO: Add Photo Scroll for Stories

                timelineData.forEach {
                    DayView(header = it.key, items = it.value)
                }
            }

            Scrollbar(state = lazyGridState)
        }
    }
}

private fun LazyGridScope.DayView(
    header: String,
    items: Int,
) {
    // Place the header first
    item(span = { GridItemSpan(maxLineSpan) }) {
        Column {
            Spacer(modifier = Modifier.padding(top = 24.dp))
            Text(
                modifier = Modifier.padding(
                    vertical = 16.dp,
                    horizontal = 8.dp
                ),
                text = header,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
    items(items) {
        Box(
            modifier = Modifier
                .clipToBounds()
                .aspectRatio(1f)
                .size(120.dp)
                .background(color = Color.Gray),
        )
    }
}
