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
import kotlinx.coroutines.flow.flowOf
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


    private val ncSsoAccountFlow: Flow<SingleSignOnAccount?> = try {
        SingleAccountHelper.`getCurrentSingleSignOnAccount$`(context)
            .asFlow()
    } catch (_: NoCurrentAccountSelectedException) {
        Log.e(TAG, "No current Account Selected!!!")
        flowOf(null)
    }


    /**
     * Flow of boolean indicating if the user is signed in to Nextcloud
     */
    val isUserSignedIn: Flow<Boolean> =
        ncSsoAccountFlow.distinctUntilChanged().mapLatest { it != null }

    /**
     * The raw account type provided by the Nextcloud SSO library
     */
    val ncSsoAccount: SingleSignOnAccount? = try {
        SingleAccountHelper.getCurrentSingleSignOnAccount(context)
    } catch (_: NoCurrentAccountSelectedException) {
        Log.e(TAG, "No current Account Selected!!!")
        null
    }

    /**
     * Flow of mapped [UserAccount] that extracts the required data from the [SingleSignOnAccount]
     */
    val userAccount: UserAccount? = ncSsoAccount?.let {
        UserAccount(
            token = it.token,
            userId = it.userId,
            name = it.name,
            url = it.url
        )
    }

    companion object {
        private val TAG = User::class.java.simpleName
    }
}
