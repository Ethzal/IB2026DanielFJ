import org.gradle.process.ExecOperations
import java.io.ByteArrayOutputStream
import javax.inject.Inject

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

android {
    namespace = "com.iberdrola.practicas2026.DanielFJ"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.iberdrola.practicas2026.DanielFJ"
        minSdk = 29
        targetSdk = 36
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

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
        }
    }
}

dependencies {

    implementation(project(":presentation"))
    implementation(project(":data"))

    // Base
    implementation(libs.androidx.appcompat)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation.fragment)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.config)
}

abstract class AdbReversePhysicalTask @Inject constructor(
    private val execOperations: ExecOperations
) : DefaultTask() {

    @TaskAction
    fun run() {
        val sdkPath = project.extensions.getByType(com.android.build.gradle.BaseExtension::class.java).sdkDirectory.absolutePath
        val adb = "$sdkPath/platform-tools/adb"
        val isWindows = org.gradle.internal.os.OperatingSystem.current().isWindows
        val executable = if (isWindows) "$adb.exe" else adb

        val stdout = ByteArrayOutputStream()

        try {
            execOperations.exec {
                commandLine(executable, "devices")
                standardOutput = stdout
            }

            val output = stdout.toString()

            val devices = output.lines()
                .drop(1)
                .filter { it.isNotBlank() && it.endsWith("device") }
                .map { it.split("\\s+".toRegex())[0] }

            var foundPhysical = false

            devices.forEach { serial ->
                if (!serial.startsWith("emulator-")) {
                    foundPhysical = true
                    execOperations.exec {
                        commandLine(executable, "-s", serial, "reverse", "tcp:3000", "tcp:3000")
                    }
                    logger.lifecycle("ADB Reverse ejecutado en dispositivo físico: $serial")
                }
            }

            if (!foundPhysical) {
                logger.lifecycle("No hay dispositivos físicos conectados. Se omite ADB Reverse.")
            }

        } catch (e: Exception) {
            logger.error("Error ejecutando ADB Reverse: ${e.message}")
        }
    }
}

val adbReversePhysicalTask = tasks.register<AdbReversePhysicalTask>("adbReversePhysical")

tasks.matching { it.name == "assembleDebug" }.configureEach {
    dependsOn(adbReversePhysicalTask)
}