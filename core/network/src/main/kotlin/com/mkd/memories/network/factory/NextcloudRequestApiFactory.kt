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

package com.mkd.memories.network.factory

import com.google.gson.Gson
import com.mkd.memories.network.NetworkConstants.BASE_API_PATH
import com.nextcloud.android.sso.aidl.NextcloudRequest
import javax.inject.Inject

/**
 * Factory for creating NextcloudRequest instances.
 *
 * @param gson The Gson instance for JSON serialization.
 */
class NextcloudRequestFactory @Inject constructor(
    private val gson: Gson,
) {
    /**
     * Creates a NextcloudRequest with the specified method, request body, and endpoint.
     *
     * @param method The HTTP method (e.g., "POST", "GET").
     * @param requestBody The request body object (will be serialized to JSON).
     * @param endpoint The endpoint path (e.g., "/image/multipreview").
     * @return A NextcloudRequest instance.
     */
    fun create(method: String, requestBody: Any, endpoint: String): NextcloudRequest =
        NextcloudRequest.Builder()
            .setMethod(method)
            .setRequestBody(gson.toJson(requestBody))
            .setUrl("$BASE_API_PATH$endpoint")
            .build()
}
