plugins {
    alias(libs.plugins.memories.android.feature)
    alias(libs.plugins.memories.android.library.compose)
    alias(libs.plugins.memories.android.hilt)
}

android {
    namespace = "com.mkd.memories.login"
}

dependencies {
    implementation(libs.nextcloud.android.library) {
        exclude(group = "org.ogce", module = "xpp3") // unused in Android and brings wrong Junit version
    }
}
