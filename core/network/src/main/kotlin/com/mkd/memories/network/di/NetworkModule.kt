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

package com.mkd.memories.network.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.Strictness
import com.mkd.memories.core.auth.util.User
import com.mkd.memories.network.NextcloudNetworkDataSource
import com.mkd.memories.network.RetrofitNetworkDataSource
import com.mkd.memories.network.data.NextcloudNetworkDataSourceImpl
import com.mkd.memories.network.data.RetrofitNetworkDataSourceImpl
import com.nextcloud.android.sso.api.NextcloudAPI
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkModule {

    @Binds
    fun bindsRetrofitNetworkDataSource(impl: RetrofitNetworkDataSourceImpl): RetrofitNetworkDataSource

    @Binds
    fun bindsNextcloudNetworkDataSource(impl: NextcloudNetworkDataSourceImpl): NextcloudNetworkDataSource

    companion object {
        @Provides
        @Singleton
        fun providesNetworkGson(): Gson = GsonBuilder().setStrictness(Strictness.LENIENT).create()

        @Provides
        @Singleton
        fun providesNextcloudApi(
            user: User,
            @ApplicationContext appContext: Context,
            gson: Gson,
        ): NextcloudAPI {
            // Check if the user is signed in and has a valid Nextcloud SSO account.
            val ncSsoAccount = user.ncSsoAccount
                ?: throw IllegalAccessException("User is not signed in or does not have a Nextcloud SSO account.")

            // Attempt to create a NextcloudAPI instance.
            return NextcloudAPI(appContext, ncSsoAccount, gson)
        }
    }
}
