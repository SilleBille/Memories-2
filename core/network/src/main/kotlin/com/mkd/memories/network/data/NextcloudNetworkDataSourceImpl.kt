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

import com.mkd.memories.network.NextcloudNetworkDataSource
import com.mkd.memories.network.factory.NextcloudRequestApiFactory
import com.mkd.memories.network.models.MultiPreviewProcessor
import com.mkd.memories.network.models.NetworkMultiPreviewFileRequest
import com.mkd.memories.network.models.NetworkMultiPreviewRequest
import com.mkd.memories.network.models.NetworkPreviewResponse
import com.nextcloud.android.sso.api.NextcloudAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NextcloudNetworkDataSourceImpl @Inject constructor(
    private val nextcloudApi: NextcloudAPI,
    private val nextcloudRequestApiFactory: NextcloudRequestApiFactory,
    private val multiPreviewProcessor: MultiPreviewProcessor,
) : NextcloudNetworkDataSource {


    private fun getCustomNetworkApi(requestBody: NetworkMultiPreviewRequest): List<NetworkPreviewResponse> {

        val nextcloudRequest = nextcloudRequestApiFactory.create(
            method = "POST",
            requestBody = requestBody,
            endpoint = "/image/multipreview"
        )
        val response = nextcloudApi.performNetworkRequestV2(nextcloudRequest)

        return response?.body?.let { multiPreviewProcessor.parse((it)) } ?: emptyList()
    }

    override suspend fun getMultiPreview(fileIds: List<Long>): List<NetworkPreviewResponse> =
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
