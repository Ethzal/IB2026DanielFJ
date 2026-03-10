plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

android {
    namespace = "com.iberdrola.practicas2026.core"
    compileSdk = 36

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    implementation(project(":data"))

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Retrofit (Lo necesita el NetworkModule)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}