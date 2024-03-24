plugins {
    alias(libs.plugins.memories.android.library)
    alias(libs.plugins.memories.android.library.compose)
    alias(libs.plugins.memories.android.hilt)
}

android {
    namespace = "com.mkd.memories.testing"
}

dependencies {
    api(kotlin("test"))
    api(libs.androidx.compose.ui.test)

    implementation(libs.hilt.android.testing)
    implementation(project(":core:designsystem"))
}
