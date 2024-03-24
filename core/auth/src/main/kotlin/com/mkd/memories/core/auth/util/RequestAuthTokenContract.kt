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

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.nextcloud.android.sso.AccountImporter
import com.nextcloud.android.sso.AccountImporter.REQUEST_AUTH_TOKEN_SSO
import com.nextcloud.android.sso.Constants.NEXTCLOUD_FILES_ACCOUNT
import com.nextcloud.android.sso.exceptions.NextcloudFilesAppAccountNotFoundException
import com.nextcloud.android.sso.exceptions.NextcloudFilesAppAccountPermissionNotGrantedException
import com.nextcloud.android.sso.exceptions.NoCurrentAccountSelectedException
import com.nextcloud.android.sso.helper.SingleAccountHelper
import com.nextcloud.android.sso.model.FilesAppType
import com.nextcloud.android.sso.model.SingleSignOnAccount
import com.nextcloud.android.sso.ui.UiExceptionManager
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

/**
 * A Custom contract that fetches the request Auth token that can used with NextCloudAPI. This
 * contract requires the account name to be passed as input and returns an instance of
 * [SingleSignOnAccount]
 */
class RequestAuthTokenContract @Inject constructor(
    @ActivityContext private val context: Context
) : ActivityResultContract<String?, SingleSignOnAccount?>() {

    @Throws(NextcloudFilesAppAccountPermissionNotGrantedException::class)
    override fun createIntent(context: Context, input: String?): Intent {
        val account = AccountImporter.getAccountForName(context, input)
            ?: throw NextcloudFilesAppAccountPermissionNotGrantedException(context)
        val componentName = FilesAppType.findByAccountType(account.type).packageId
        val authIntent = Intent()
        authIntent.setComponent(
            ComponentName(
                componentName,
                "com.owncloud.android.ui.activity.SsoGrantPermissionActivity"
            )
        )
        authIntent.putExtra(NEXTCLOUD_FILES_ACCOUNT, account)
        return authIntent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): SingleSignOnAccount? {
        var ssoAccount: SingleSignOnAccount? = null
        if (resultCode == Activity.RESULT_OK) {
            AccountImporter.onActivityResult(
                REQUEST_AUTH_TOKEN_SSO,
                resultCode,
                intent,
                context as Activity // We are getting @ActivityContext. So, this should be fine
            ) { account ->
                // As this library supports multiple accounts we created some helper methods if you
                // only want to use one. The following line stores the selected account as the
                // "default" account which can be queried by using the
                // SingleAccountHelper.getCurrentSingleSignOnAccount(context) method
                SingleAccountHelper.commitCurrentAccount(context, account.name)

                try {
                    ssoAccount = SingleAccountHelper.getCurrentSingleSignOnAccount(context)
                } catch (e: NextcloudFilesAppAccountNotFoundException) {
                    UiExceptionManager.showDialogForException(context, e)
                } catch (e: NoCurrentAccountSelectedException) {
                    UiExceptionManager.showDialogForException(context, e)
                }
            }
        }
        return ssoAccount
    }
}
