plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.docverifypro"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.docverifypro"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.vision.common)
    implementation(libs.play.services.mlkit.barcode.scanning)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("androidx.activity:activity-ktx:1.8.2")


//    implementation("com.google.android.material:material:1.9.0")
//    implementation ("androidx.navigation:navigation-fragment-ktx:2.5.3")
//    implementation ("androidx.navigation:navigation-ui-ktx:2.5.3")
//    implementation("androidx.compose.material3:material3:1.3.1")
//    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")
//    implementation("androidx.compose.material3:material3-adaptive-navigation-suite:1.4.0-alpha05")
//    QR code caner or generater
    implementation("com.itextpdf:itextg:5.5.9")
    implementation("org.apache.poi:poi-ooxml:5.2.3")

    implementation ("com.google.code.gson:gson:2.10")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")

    implementation ("com.google.zxing:core:3.5.0")

    implementation ("androidx.camera:camera-core:1.4.1")
    implementation ("androidx.camera:camera-camera2:1.4.1")
    implementation ("androidx.camera:camera-lifecycle:1.4.1")
    implementation ("androidx.camera:camera-view:1.4.1")
    implementation ("androidx.camera:camera-mlkit-vision:1.4.1")
    implementation ("com.google.mlkit:barcode-scanning:17.3.0")
}