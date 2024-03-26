plugins {
    alias(libs.plugins.memories.android.application)
    alias(libs.plugins.memories.android.application.compose)
    alias(libs.plugins.memories.android.application.jacoco)
    alias(libs.plugins.memories.android.hilt)
}

android {
    namespace = "com.mkd.memories"

    defaultConfig {
        applicationId = "com.mkd.memories"
        versionCode = 1
        versionName = "0.1" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(project(":core:designsystem"))

    implementation(project(":feature:login"))

    ksp(libs.hilt.compiler)

    kspTest(libs.hilt.compiler)

    testImplementation(project(":core:testing"))
    testImplementation(libs.hilt.android.testing)

    androidTestImplementation(project(":core:testing"))
    androidTestImplementation(libs.hilt.android.testing)
}
