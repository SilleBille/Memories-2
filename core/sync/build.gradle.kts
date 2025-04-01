plugins {
    alias(libs.plugins.memories.android.library)
    alias(libs.plugins.memories.android.library.jacoco)
    alias(libs.plugins.memories.hilt)
}

android {
    namespace = "com.mkd.memories.sync"
}

dependencies {
    implementation(projects.core.network)

}
