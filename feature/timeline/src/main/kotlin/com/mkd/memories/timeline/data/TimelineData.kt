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

package com.mkd.memories.timeline.data

import com.mkd.memories.network.data.NetworkDayContents
import com.mkd.memories.network.data.NetworkDays

/**
 * Class summarizing Days and number of photos taken on that day.
 */
data class Days(
    val dayId: Long,
    val count: Long,
)

fun List<NetworkDays>.parseDays() = map { Days(dayId = it.dayId, count = it.count) }

data class DayContents(
    val fileId: Long,
    val dayId: Long,
    val fileName: String,
    val epoch: Long,
    val mimetype: String,
)

fun List<NetworkDayContents>.parseDayContents() = map {
    DayContents(
        fileId = it.fileid,
        dayId = it.dayid,
        fileName = it.basename,
        epoch = it.epoch,
        mimetype = it.mimetype
    )
}
