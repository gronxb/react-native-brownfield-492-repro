import groovy.json.JsonSlurper

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.callstack.react.brownfield")
    `maven-publish`
    id("com.facebook.react")
}

fun resolveRootProjectInt(name: String): Int {
    val extraProperties = rootProject.extensions.extraProperties
    val value =
        when {
            extraProperties.has(name) -> extraProperties.get(name)
            rootProject.findProperty(name) != null -> rootProject.findProperty(name)
            rootProject.findProperty("android.$name") != null -> rootProject.findProperty("android.$name")
            else -> error("Unable to resolve root project property '$name' for Brownfield Android packaging.")
        }

    return when (value) {
        is Int -> value
        is Number -> value.toInt()
        is String -> value.toInt()
        else -> value.toString().toInt()
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenAar") {
            groupId = "com.rnapp"
            artifactId = "brownfieldlib"
            version = "0.0.1-SNAPSHOT"
            afterEvaluate {
                from(components.getByName("default"))
            }
        }
    }

    repositories {
        mavenLocal() // publishes to the local Maven repository (~/.m2/repository by default)
    }
}

reactBrownfield {
    missingDimensionStrategies = listOf()
}

react {
    autolinkLibrariesWithApp()
}

android {
    namespace = "com.rnapp"
    compileSdk = resolveRootProjectInt("compileSdkVersion")

    defaultConfig {
        minSdk = 24

        buildConfigField(
            "boolean",
            "IS_EDGE_TO_EDGE_ENABLED",
            properties["edgeToEdgeEnabled"].toString()
        )
        buildConfigField(
            "boolean",
            "IS_NEW_ARCHITECTURE_ENABLED",
            properties["newArchEnabled"].toString()
        )
        buildConfigField("boolean", "IS_HERMES_ENABLED", properties["hermesEnabled"].toString())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")


    }

    buildFeatures {
        buildConfig = true
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

    publishing {
        multipleVariants {
            allVariants()
        }
    }
}

dependencies {
    api("com.facebook.react:react-android:0.85.2")
    api("com.facebook.react:hermes-android:0.85.2")

    api("io.coil-kt.coil3:coil-compose:3.2.0")
    api("io.coil-kt.coil3:coil-network-okhttp:3.2.0")
}
