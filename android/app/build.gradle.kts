plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.scrapnox.app"
    compileSdk = 36

    defaultConfig {
        applicationId = "app.scrapnox.in"
        minSdk = 24
        targetSdk = 36
        versionCode = 2
        versionName = "1.0.0"
    }

    signingConfigs {
        create("release") {
            // Keystore path — release keystore bana kar yahan daalo (Play Store ke liye)
            val ksFile = file("../keystore/scrapnox-release.jks")
            if (ksFile.exists()) {
                storeFile = ksFile
                storePassword = System.getenv("SCRAPNOX_KEYSTORE_PASSWORD") ?: "scrapnox-release"
                keyAlias = "scrapnox"
                keyPassword = System.getenv("SCRAPNOX_KEY_PASSWORD") ?: "scrapnox-release"
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
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
        viewBinding = false
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.activity:activity-ktx:1.9.3")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.webkit:webkit:1.12.1")
}
