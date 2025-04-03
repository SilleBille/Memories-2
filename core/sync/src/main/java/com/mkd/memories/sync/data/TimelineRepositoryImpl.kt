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

package com.mkd.memories.sync.data

import com.mkd.memories.database.dao.DayContentDao
import com.mkd.memories.database.dao.DaysDao
import com.mkd.memories.network.NextcloudNetworkDataSource
import com.mkd.memories.network.RetrofitNetworkDataSource
import com.mkd.memories.network.models.NetworkDayContents
import com.mkd.memories.network.models.NetworkDays
import com.mkd.memories.sync.data.parsers.Preview
import com.mkd.memories.sync.data.parsers.asEntity
import com.mkd.memories.sync.data.parsers.parsePreview
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TimelineRepositoryImpl @Inject constructor(
    private val retrofitDatasource: RetrofitNetworkDataSource,
    private val nextcloudNetworkDataSource: NextcloudNetworkDataSource,
    private val daysDao: DaysDao,
    private val dayContentDao: DayContentDao,
) : TimelineRepository {

    override suspend fun sync(): Boolean = withContext(Dispatchers.IO) {
        // Get the list of days of the Timeline
        val days = retrofitDatasource.getDays().map(NetworkDays::asEntity)
        daysDao.upsertDays(days)

        // TODO: See if this can be moved to Paging
        // Get the fileIds for each day
        val dayContents = retrofitDatasource
            .getDayContents(days.map { it.dayId })
            .map(NetworkDayContents::asEntity)

        true

    }

    override suspend fun getMultiPreview(fileIds: List<Long>): List<Preview> =
        nextcloudNetworkDataSource.getMultiPreview(fileIds).parsePreview()

}
