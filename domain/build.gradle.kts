plugins {
    kotlin("jvm")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    // Testing
    testImplementation(libs.junit)
//    testImplementation(libs.mockito.core)
//    testImplementation(libs.mockito.kotlin)

    // Coroutines + Flow
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.retrofit2.kotlin.coroutines.adapter)
}
