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

import android.content.Context
import androidx.lifecycle.ViewModel
import com.mkd.memories.core.auth.di.RequestAuthTokenContractFactory
import com.mkd.memories.core.auth.util.ChooseAccountContract
import com.mkd.memories.core.auth.util.RequestAuthTokenContract
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val chooseAccountContract: ChooseAccountContract,
    private val requestAuthTokenContractFactory: RequestAuthTokenContractFactory
) : ViewModel() {

    lateinit var requestAuthTokenContract: RequestAuthTokenContract
    fun createRequestAuthTokenContract(context: Context) {
        requestAuthTokenContract = requestAuthTokenContractFactory.create(context)
    }

}
