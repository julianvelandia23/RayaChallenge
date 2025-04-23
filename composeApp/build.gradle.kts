import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.sqldelight)
}

val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { load(it) }
    }
}

val coinGeckoKey = localProperties.getProperty("COIN_GECKO_KEY") ?: ""
val exchangeKey = localProperties.getProperty("EXCHANGE_KEY") ?: ""

val generateSecrets by tasks.registering {
    val outputDir = file("${layout.buildDirectory.get()}/generated/kmp/org/julianvelandia/raya")
    val secretsFile = file("$outputDir/Secrets.kt")

    outputs.file(secretsFile)

    doLast {
        outputDir.mkdirs()
        secretsFile.writeText("""
            package org.julianvelandia.raya

            object Secrets {
                const val COIN_GECKO_KEY = "$coinGeckoKey"
                const val EXCHANGE_KEY = "$exchangeKey"
            }
        """.trimIndent())
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    dependsOn(generateSecrets)
}

kotlin {
    sourceSets["commonMain"].kotlin.srcDir("${layout.buildDirectory.get()}/generated/kmp")

    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.koin.compose)
            implementation(libs.ktor.clientOkhttp)
            implementation(libs.sqldelight.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.clientDarwin)
            implementation(libs.sqldelight.native)
        }

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kotlinx.datetime)
            implementation(libs.koin.core)
            implementation(libs.insert.koin.koin.compose)
            implementation(libs.jetbrains.navigation.compose)
            implementation(libs.lifecycle.viewmodel.compose)

            implementation(libs.ktor.clientCore)
            implementation(libs.ktor.clientContentNegotiation)
            implementation(libs.ktor.serializationKotlinxJson)
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.ktor.clientLogging)

            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines.extensions)
        }


        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}

android {
    namespace = "org.julianvelandia.raya"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.julianvelandia.raya"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

sqldelight {
    databases {
        create("WalletDatabase") {
            packageName.set("db")
        }
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}


