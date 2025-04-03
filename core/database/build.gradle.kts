plugins {
    alias(libs.plugins.memories.android.library)
    alias(libs.plugins.memories.android.library.jacoco)
    alias(libs.plugins.memories.android.room)
    alias(libs.plugins.memories.hilt)
}

android {
    namespace = "com.mkd.memories.core.database"
}

dependencies {

}
