package com.mkd.memories

import android.app.Application
import com.mkd.memories.sync.initializers.Sync
import dagger.hilt.android.HiltAndroidApp

/**
 * [Application] class for Memories
 */
@HiltAndroidApp
class MemoriesApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Sync; the system responsible for keeping data in the app up to date.
        Sync.initialize(context = this)
    }
}
