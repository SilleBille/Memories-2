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

package com.mkd.memories.sync.data.parsers

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.mkd.memories.core.model.data.DayContent
import com.mkd.memories.core.model.data.Days
import com.mkd.memories.network.models.NetworkDayContents
import com.mkd.memories.network.models.NetworkDays
import com.mkd.memories.network.models.NetworkPreviewResponse


fun List<NetworkDays>.parseDays() = map { Days(dayId = it.dayid, count = it.count) }

fun List<NetworkDayContents>.parseDayContents() = map {
    DayContent(
        fileId = it.fileid,
        dayId = it.dayid,
        fileName = it.basename,
        epoch = it.epoch,
        mimetype = it.mimetype
    )
}

// TODO: Move this to :domain
data class Preview(
    //val fileId: Long,
    val preview: Bitmap,
    val mimeType: MimeType,
)

enum class MimeType {
    PHOTO,
    MOTION_PHOTO,
    VIDEO,
    UNKNOWN,
}

fun List<NetworkPreviewResponse>.parsePreview() = map {
    Preview(
        preview = BitmapFactory.decodeByteArray(it.imageData, 0, it.imageData.size),
        mimeType = when (it.type) {
            "image/jpeg" -> MimeType.PHOTO
            "image/mov" -> MimeType.MOTION_PHOTO
            "video/mp4" -> MimeType.VIDEO
            else -> MimeType.UNKNOWN
        }
    )
}
