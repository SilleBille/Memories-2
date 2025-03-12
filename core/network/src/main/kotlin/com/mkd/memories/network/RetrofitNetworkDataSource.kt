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

package com.mkd.memories.network

import com.mkd.memories.network.data.NetworkDayContents
import com.mkd.memories.network.data.NetworkDays

/**
 * Interface representing network calls to the Nextcloud backend using Retrofit
 */
interface RetrofitNetworkDataSource {
    /**
     * Fetch all available days. This fill fetch DayID and count of media in the day
     */
    suspend fun getDays(): List<NetworkDays>

    /**
     * Fetch all available media for the given list of DayIds
     */
    suspend fun getDayContents(dayIds: List<Long>): List<NetworkDayContents>

}
