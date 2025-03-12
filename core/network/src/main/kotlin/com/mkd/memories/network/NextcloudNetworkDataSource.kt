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

import com.mkd.memories.network.data.Preview

/**
 * Interface representing network calls to the Nextcloud backend using [com.nextcloud.android.sso.aidl.NextcloudRequest.Builder]
 */
interface NextcloudNetworkDataSource {

    /**
     * Get previews for a list of provided fileIds
     *
     * @param fileIds list of fileIds to fetch previews for
     *
     * @return list of [Preview]s
     */
    suspend fun getMultiPreview(fileIds: List<Long>): List<Preview>
}
