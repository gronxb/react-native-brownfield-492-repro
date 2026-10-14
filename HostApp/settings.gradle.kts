pluginManagement {
    repositories { google(); mavenCentral(); gradlePluginPortal() }
}
dependencyResolutionManagement {
    repositories {
        mavenLocal() // brownfieldlib AAR from `npx brownfield publish:android`
        google()
        mavenCentral()
    }
}
rootProject.name = "HostApp"
include(":app")
