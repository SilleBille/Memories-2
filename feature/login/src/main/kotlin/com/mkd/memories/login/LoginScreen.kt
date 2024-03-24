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

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.mkd.memories.core.auth.util.ChooseAccountContract
import com.mkd.memories.core.auth.util.RequestAuthTokenContract
import com.mkd.mkd.designsystem.theme.MemoriesTheme

@Composable
internal fun LoginRoute(
    viewModel: LoginViewModel = hiltViewModel(),
) {

    // Initialize the context for creating RequestAuthTokenContract
    viewModel.createRequestAuthTokenContract(LocalContext.current)
    LoginScreen(
        chooseAccountContract = viewModel.chooseAccountContract,
        requestAuthTokenContract = viewModel.requestAuthTokenContract
    )
}

@Composable
internal fun LoginScreen(
    chooseAccountContract: ChooseAccountContract,
    requestAuthTokenContract: RequestAuthTokenContract
) {

    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // SSO Button
        val authTokenLauncherResult =
            rememberLauncherForActivityResult(requestAuthTokenContract) { _ ->

                Log.e("MKD", "Result came back to Login Screen")
            }
        val accountChooserResult = rememberLauncherForActivityResult(chooseAccountContract) { name ->
            authTokenLauncherResult.launch(name)
        }
        LoginItem(
            buttonText = stringResource(id = R.string.feature_login_continue_with_sso_button_text)
        ) {
            accountChooserResult.launch(null)
        }
    }
}

@Composable
internal fun LoginItem(buttonText: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(text = buttonText)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginItemPreview() {
    MemoriesTheme {
        LoginItem("Continue with SSO login") {}
    }
}
