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
import com.mkd.memories.network.NextcloudNetworkDataSource
import com.mkd.memories.network.models.MultiPreviewProcessor
import com.nextcloud.android.sso.aidl.NextcloudRequest
import com.nextcloud.android.sso.api.NextcloudAPI
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class NextcloudNetworkDataSourceImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    user: User,
    private val gson: Gson,
    private val multiPreviewProcessor: MultiPreviewProcessor,
) : NextcloudNetworkDataSource {

    private val ssoAccount = user.ncSsoAccount
    private val nextcloudApi = ssoAccount?.let { NextcloudAPI(appContext, it, gson) }


    private fun getCustomNetworkApi(requestBody: NetworkMultiPreviewRequest): List<Preview> {

        val nextCloudRequest = NextcloudRequest.Builder()
            .setMethod("POST")
            .setRequestBody(gson.toJson(requestBody))
            .setUrl("$BASE_PATH/image/multipreview")
            .build()
        val response = nextcloudApi?.performNetworkRequestV2(nextCloudRequest)

        return response?.body?.let { multiPreviewProcessor.parse((it)) } ?: emptyList()
    }

    override suspend fun getMultiPreview(fileIds: List<Long>): List<Preview> =
        withContext(Dispatchers.IO) {
            val request = NetworkMultiPreviewRequest(
                fileIds.map {
                    NetworkMultiPreviewFileRequest(
                        fileId = it,
                        reqId = System.currentTimeMillis()
                    )
                }
            )
            getCustomNetworkApi(request)
        }
}
