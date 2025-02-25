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
import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mkd.memories.core.auth.util.RequestAuthTokenContract
import com.mkd.mkd.designsystem.theme.MemoriesTheme

@Composable
internal fun LoginRoute(
    onAccountSelected: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val authTokenLauncher = rememberLauncherForActivityResult(RequestAuthTokenContract(context)) {
        Toast.makeText(context, "Account selected: ${it?.name}", Toast.LENGTH_SHORT).show()
        viewModel.onAccountPickerDismiss(it)
    }

    val accountPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val accountName = result.data?.getStringExtra(AccountManager.KEY_ACCOUNT_NAME)

                    // Launch the second contract only if an account is selected
                    accountName?.let { authTokenLauncher.launch(it) }
                }

                Activity.RESULT_CANCELED -> {
                    viewModel.onAccountPickerDismiss(null)
                }
            }

        }

    LaunchedEffect(uiState.selectedAccount) {
        uiState.selectedAccount?.let { onAccountSelected() }
    }

    LoginScreen(
        modifier = Modifier.fillMaxSize(),
        onSsoClick = viewModel::onSsoClick,
    )

    if (uiState.showAccountPicker) {
        val intent = viewModel.getSsoIntent()
        accountPickerLauncher.launch(intent)
    }
}

@Composable
internal fun LoginScreen(
    onSsoClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Scaffold {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onSsoClick) {
                    Text(text = stringResource(id = R.string.feature_login_continue_with_sso_button_text))
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun LoginItemPreview() {
    MemoriesTheme {
        LoginScreen(onSsoClick = {})
    }
}
