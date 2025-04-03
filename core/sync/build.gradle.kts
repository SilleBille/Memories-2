plugins {
    alias(libs.plugins.memories.android.library)
    alias(libs.plugins.memories.android.library.jacoco)
    alias(libs.plugins.memories.hilt)
}

android {
    namespace = "com.mkd.memories.core.sync"
}

dependencies {
    api(projects.core.model)

    ksp(libs.hilt.ext.compiler)

    implementation(libs.androidx.work.ktx)
    implementation(libs.hilt.ext.work)

    implementation(projects.core.network)
    implementation(projects.core.database)

}
