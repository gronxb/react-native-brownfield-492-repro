plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.host"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.host"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        release {
            // Release so the release AAR (with the JS bundle inside) is used; no Metro needed.
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }
}

dependencies {
    implementation("com.rnapp:brownfieldlib:0.0.1-SNAPSHOT")
    implementation("androidx.appcompat:appcompat:1.7.0")
}

// The generated brownfieldlib POM depends on com.facebook.react:hermes-android:0.85.2,
// which is not published. Inside the RN project the RN Gradle plugin substitutes it;
// a plain host app has to do the same (unrelated to #492).
configurations.all {
    resolutionStrategy.dependencySubstitution {
        substitute(module("com.facebook.react:hermes-android"))
            .using(module("com.facebook.hermes:hermes-android:250829098.0.10"))
    }
}
