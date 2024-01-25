plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.github.compose.barcodereader"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    val cameraVersion = "1.3.1"
    api("androidx.camera:camera-core:$cameraVersion")
    api("androidx.camera:camera-camera2:$cameraVersion")
    api("androidx.camera:camera-lifecycle:$cameraVersion")
    api("androidx.camera:camera-view:$cameraVersion")
    api("com.airbnb.android:lottie-compose:6.3.0")
    api("com.github.markusfisch:zxing-cpp:v2.2.0.2")
    api("com.google.mlkit:barcode-scanning:17.2.0")
    api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")
    api("androidx.core:core-ktx:1.12.0")
    api(platform("androidx.compose:compose-bom:2023.10.01"))
    api("androidx.activity:activity-compose:1.8.2")
    api("androidx.compose.ui:ui")
    api("androidx.compose.ui:ui-graphics")
    api("androidx.compose.ui:ui-tooling-preview")
    api("androidx.compose.material3:material3")
    api("androidx.compose.material:material-icons-extended")
    api("com.google.accompanist:accompanist-permissions:0.32.0")
    api("androidx.compose.runtime:runtime-livedata")
}