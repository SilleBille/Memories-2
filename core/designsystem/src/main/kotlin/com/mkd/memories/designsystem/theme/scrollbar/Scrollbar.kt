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

package com.mkd.memories.designsystem.theme.scrollbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val ThumbShadowElevation = 8.dp
private val ThumbSize = 48.dp
private val ThumbXOffset = (ThumbSize.value * 0.40).dp // 40% of the thumb will be clipped
private val ThumbHeight = 72.dp
private const val ThumbVisibilityTimeout = 1_500L

/**
 * A [Scrollbar] that allows for fast scrolling of content by dragging its thumb.
 * Its thumb disappears when the scrolling container is dormant for 1.5 seconds.
 *
 * @param modifier a [Modifier] for the [Scrollbar]
 * @param state the driving LazyGridState for the [Scrollbar]
 */
@Composable
fun BoxScope.Scrollbar(
    state: LazyGridState,
    modifier: Modifier = Modifier
) {

    var scrollAreaHeight by remember { mutableIntStateOf(0) }
    var isScrollbarVisible by remember { mutableStateOf(false) }
    var isDragging by remember { mutableStateOf(false) }
    var thumbOffsetY by remember { mutableFloatStateOf(0f) }

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()
    val thumbHeight = with(density) { ThumbHeight.toPx() }
    val showScrollbar = state.isScrollInProgress || isDragging
    val firstVisibleItemIndex by remember { derivedStateOf { state.firstVisibleItemIndex } }
    val firstItemScrollOffset by remember { derivedStateOf { state.firstVisibleItemScrollOffset } }


    // Auto-hide scrollbar
    LaunchedEffect(showScrollbar) {
        if (showScrollbar) {
            isScrollbarVisible = true
        } else {
            delay(ThumbVisibilityTimeout)
            isScrollbarVisible = false
        }
    }

    // Calculate the thumb offset and ensure it doesn't go offscreen
    LaunchedEffect(firstVisibleItemIndex, firstItemScrollOffset, isDragging) {
        if (!isDragging) {
            val itemCount = state.layoutInfo.totalItemsCount
            if (itemCount > 0) {
                val scrollProgress = firstVisibleItemIndex.toFloat() / (itemCount - 1)
                thumbOffsetY = (scrollAreaHeight - thumbHeight) * scrollProgress
            }
        }
    }

    Box(
        modifier = modifier
            .align(Alignment.TopEnd)
            .fillMaxHeight()
            .width(48.dp)
            .onGloballyPositioned { scrollAreaHeight = it.size.height }
    ) {
        AnimatedVisibility(
            isScrollbarVisible,
            enter = slideInHorizontally { it },
            exit = slideOutHorizontally { it },
        ) {
            ScrollThumb(
                modifier = Modifier
                    .size(ThumbSize)
                    .offset(x = ThumbXOffset, y = with(density) { thumbOffsetY.toDp() })
                    .pointerInput(Unit) {
                        detectVerticalDragGestures(
                            onDragStart = {
                                isDragging = true
                            },
                            onVerticalDrag = { change, dragAmount ->
                                change.consume()
                                // Update directly from previous thumbOffsetY
                                thumbOffsetY = (thumbOffsetY + dragAmount)
                                    .coerceIn(0f, scrollAreaHeight - thumbHeight)

                                val totalItems = state.layoutInfo.totalItemsCount
                                if (totalItems > 0) {
                                    val targetIndex =
                                        ((thumbOffsetY / (scrollAreaHeight - thumbHeight)) * (totalItems - 1))
                                            .toInt()
                                            .coerceIn(0, totalItems - 1)

                                    scope.launch {
                                        state.scrollToItem(targetIndex)
                                    }
                                }
                            },
                            onDragEnd = {
                                isDragging = false
                            }
                        )
                    },
            )
        }
    }
}

@Composable
private fun ScrollThumb(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.semantics(mergeDescendants = true) { },
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = ThumbShadowElevation,
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(end = 8.dp)) {
            Icon(
                modifier = Modifier.padding(bottom = 12.dp),
                imageVector = Icons.Filled.ArrowDropUp,
                contentDescription = null,
                tint = Color.Gray,
            )
            Icon(
                modifier = Modifier.padding(top = 12.dp),
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = null,
                tint = Color.Gray,
            )
        }
    }
}
