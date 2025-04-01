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

import com.mkd.memories.network.NetworkConstants.BASE_API_PATH
import com.nextcloud.android.sso.api.NextcloudAPI
import retrofit2.NextcloudRetrofitApiBuilder
import javax.inject.Inject

/**
 * Generic factory for creating Retrofit API instances.
 *
 * @param nextcloudApi The NextcloudAPI instance.
 */
class RetrofitApiFactory @Inject constructor(
    private val nextcloudApi: NextcloudAPI,
) {
    /**
     * Creates a Retrofit API instance of the specified type.
     *
     * @param apiClass The class of the Retrofit API interface.
     * @return An instance of the specified Retrofit API.
     */
    fun <T> create(apiClass: Class<T>): T {
        return NextcloudRetrofitApiBuilder(nextcloudApi, BASE_API_PATH)
            .create(apiClass)
    }
}
