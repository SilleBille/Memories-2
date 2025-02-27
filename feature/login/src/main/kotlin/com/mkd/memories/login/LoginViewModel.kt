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

package com.mkd.memories.login

import android.accounts.AccountManager
import androidx.lifecycle.ViewModel
import com.nextcloud.android.sso.model.FilesAppType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class LoginUIState(
    val showAccountPicker: Boolean = false,
    val isUserLoggedIn: Boolean = false,
)

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUIState())
    val uiState = _uiState.asStateFlow()

    fun onSsoClick() {
        _uiState.update { it.copy(showAccountPicker = true) }
    }

    fun onAccountPickerDismiss(isUserLoggedIn: Boolean) =
        _uiState.update {
            it.copy(
                isUserLoggedIn = isUserLoggedIn,
                showAccountPicker = false
            )
        }

    fun getSsoIntent() = AccountManager.newChooseAccountIntent(
        null, null, ACCOUNT_TYPES,
        null, "SSO", null, null
    )

    companion object {
        private val ACCOUNT_TYPES = FilesAppType.entries.map { it.accountType }.toTypedArray()
    }
}
