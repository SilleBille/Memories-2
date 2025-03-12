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

package com.mkd.memories.network.data

import android.content.Context
import com.google.gson.Gson
import com.mkd.memories.core.auth.util.User
import com.mkd.memories.network.RetrofitNetworkDataSource
import com.nextcloud.android.sso.api.NextcloudAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.NextcloudRetrofitApiBuilder
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Base URL for the Nextcloud Memories API
 */
internal const val BASE_PATH = "/apps/memories/api"

/**
 * Retrofit API declaration for Memories Network API. Note that, these APIs cannot be marked as
 * suspend due to the lack of Nextcloud SSO support.
 *
 * @see <a href="https://github.com/nextcloud/Android-SingleSignOn/issues/177">Nextcloud SSO issue #177</a>
 */
private interface RetrofitMemoriesNetworkApi {
    @GET(value = "/days")
    fun getDays(): List<NetworkDays>

    @GET(value = "/days/{ids}")
    fun getDayContents(
        @Path(value = "ids") ids: String
    ): List<NetworkDayContents>
}

@Singleton
internal class RetrofitNetworkDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val gson: Gson,
    user: User,
) : RetrofitNetworkDataSource {

    private val ssoAccount = user.ncSsoAccount
    private val nextcloudApi = ssoAccount?.let { NextcloudAPI(appContext, ssoAccount, gson) }
    private val networkApi = nextcloudApi?.let {
        NextcloudRetrofitApiBuilder(it, BASE_PATH)
            .create(RetrofitMemoriesNetworkApi::class.java)
    }

    override suspend fun getDays() = withContext(Dispatchers.IO) {
        networkApi?.getDays() ?: emptyList()
    }

    override suspend fun getDayContents(dayIds: List<Long>) = withContext(Dispatchers.IO) {
        networkApi?.getDayContents(dayIds.joinToString(",")) ?: emptyList()
    }
}
