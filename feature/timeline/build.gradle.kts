plugins {
    alias(libs.plugins.memories.android.feature)
    alias(libs.plugins.memories.android.library.compose)
    alias(libs.plugins.memories.android.library.jacoco)
}

android {
    namespace = "com.mkd.memories.timeline"
}

dependencies {
    implementation(libs.coil.kt.compose)
    implementation(libs.nextcloud.sso)

    implementation(projects.core.auth)
    implementation(projects.core.network)
    implementation(projects.core.sync) // TODO: Remove this dependency

    testImplementation(libs.hilt.android.testing)
}
