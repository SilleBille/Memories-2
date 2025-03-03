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

package com.mkd.memories.core.auth.util

import android.content.Context
import android.util.Log
import androidx.lifecycle.asFlow
import com.nextcloud.android.sso.exceptions.NoCurrentAccountSelectedException
import com.nextcloud.android.sso.helper.SingleAccountHelper
import com.nextcloud.android.sso.model.SingleSignOnAccount
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

data class UserAccount(
    val token: String,
    val userId: String,
    val name: String,
    val url: String,
)

class User @Inject constructor(
    @ApplicationContext private val context: Context
) {

    /**
     * Flow of [SingleSignOnAccount], the raw account type provided by the Nextcloud SSO library
     */
    val nextCloudAccount: Flow<SingleSignOnAccount?> = try {
        SingleAccountHelper.`getCurrentSingleSignOnAccount$`(context)
            .asFlow()
    } catch (_: NoCurrentAccountSelectedException) {
        Log.e(TAG, "No current Account Selected!!!")
        flowOf(null)
    }

    /**
     * Flow of mapped [UserAccount] that extracts the required data from the [SingleSignOnAccount]
     */
    val userAccount: Flow<UserAccount?> = nextCloudAccount
        .filterNotNull()
        .map { UserAccount(token = it.token, userId = it.userId, name = it.name, url = it.url) }

    val isUserSignedIn: Flow<Boolean> =
        nextCloudAccount.distinctUntilChanged().mapLatest { it != null }

    companion object {
        private val TAG = User::class.java.simpleName
    }
}
