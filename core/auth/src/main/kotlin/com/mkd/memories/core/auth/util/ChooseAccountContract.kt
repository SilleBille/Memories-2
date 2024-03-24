/*
 * Copyright 2024 SilleBille
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

import android.accounts.AccountManager
import android.accounts.AccountManager.KEY_ACCOUNT_NAME
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.activity.result.contract.ActivityResultContract
import com.nextcloud.android.sso.model.FilesAppType
import javax.inject.Inject

/**
 * A Custom contract to launch the NC account picker dialog intent. Consumers need to pass the
 * activity. This returns the user-chosen account name back to the consumer. The consumer needs to
 * request auth token before accessing NC API. Use [RequestAuthTokenContract]
 */
class ChooseAccountContract @Inject constructor() : ActivityResultContract<Void?, String?>() {

    override fun createIntent(context: Context, input: Void?): Intent =
        AccountManager.newChooseAccountIntent(
            null,
            null,
            ACCOUNT_TYPES,
            null,
            "SSO",
            null,
            null
        )

    override fun parseResult(resultCode: Int, intent: Intent?): String? =
        if (resultCode == Activity.RESULT_OK) {
            val accountName = intent?.getStringExtra(KEY_ACCOUNT_NAME)
            Log.d(TAG, "Retrieved account name: $accountName")
            accountName
        } else {
            Log.w(TAG, "Account choosing failed! resultCode: $resultCode")
            null
        }

    companion object {
        private val ACCOUNT_TYPES = FilesAppType.entries.map { it.accountType }.toTypedArray()
        private val TAG = ChooseAccountContract::class.java.canonicalName
    }
}
