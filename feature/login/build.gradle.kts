plugins {
    alias(libs.plugins.memories.android.feature)
    alias(libs.plugins.memories.android.library.compose)
    alias(libs.plugins.memories.android.hilt)
}

android {
    namespace = "com.mkd.memories.login"
}

dependencies {
    implementation(project(":core:auth"))
}
