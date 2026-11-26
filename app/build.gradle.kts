plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.tradewithsanket.powermasterpro"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.tradewithsanket.powermasterpro"
        minSdk = 26
        targetSdk = 34
        versionCode = 4
        versionName = "4.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}
