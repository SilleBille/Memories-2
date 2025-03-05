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

package com.mkd.memories.network.retrofit

import android.content.Context
import com.google.gson.Gson
import com.mkd.memories.core.auth.util.User
import com.mkd.memories.core.auth.util.UserAccount
import com.mkd.memories.network.MemoriesNetworkDataSource
import com.mkd.memories.network.data.NetworkDayContents
import com.mkd.memories.network.data.NetworkDays
import com.nextcloud.android.sso.api.NextcloudAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import retrofit2.NextcloudRetrofitApiBuilder
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Retrofit API declaration for Memories Network API
 */
private interface RetrofitMemoriesNetworkApi {
    @GET(value = "/days")
    fun getDays(): List<NetworkDays>

    @GET(value = "/days/{ids}")
    fun getDayContents(
        @Path(value = "ids") ids: String
    ): List<NetworkDayContents>
}

/**
 * Wrapper for data provided from the selected [UserAccount.url]
 */
@Serializable
private data class NetworkResponse<T>(
    val data: T,
)

private const val BASE_PATH = "/apps/memories/api"

@Singleton
internal class MemoriesNetworkDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val gson: Gson,
    user: User,
) : MemoriesNetworkDataSource {
    private val networkApi: Flow<RetrofitMemoriesNetworkApi> =
        user.nextCloudAccount
            .filterNotNull()
            .map { ssoAccount ->
                val nextCloudApi = NextcloudAPI(appContext, ssoAccount, gson)
                NextcloudRetrofitApiBuilder(nextCloudApi, BASE_PATH)
                    .create(RetrofitMemoriesNetworkApi::class.java)
            }


    private suspend fun getNetworkApi() = networkApi.first()

    override suspend fun getDays(): List<NetworkDays> = getNetworkApi().getDays()

    override suspend fun getDayContents(dayIds: List<Long>): List<NetworkDayContents> =
        getNetworkApi().getDayContents(dayIds.joinToString(","))
}
