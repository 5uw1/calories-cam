import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
  alias(libs.plugins.kotlin.multiplatform)
  alias(libs.plugins.android.application)
  alias(libs.plugins.compose.multiplatform)
  alias(libs.plugins.compose.compiler)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.room)
}

kotlin {
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
    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material3)
      implementation(compose.materialIconsExtended)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)

      implementation(libs.androidx.lifecycle.viewmodel)
      implementation(libs.androidx.lifecycle.runtime.compose)

      implementation(libs.kotlinx.coroutines.core)
      implementation(libs.kotlinx.serialization.json)
      implementation(libs.kotlinx.datetime)

      implementation(libs.ktor.client.core)
      implementation(libs.ktor.client.content.negotiation)
      implementation(libs.ktor.serialization.json)

      implementation(libs.androidx.room.runtime)
      implementation(libs.androidx.sqlite.bundled)

      implementation(libs.coil.compose)
      implementation(libs.coil.network.ktor)
    }

    androidMain.dependencies {
      implementation(compose.preview)
      implementation(libs.androidx.activity.compose)
      implementation(libs.androidx.core.ktx)
      implementation(libs.kotlinx.coroutines.android)
      implementation(libs.ktor.client.okhttp)

      implementation(libs.androidx.camera.camera2)
      implementation(libs.androidx.camera.core)
      implementation(libs.androidx.camera.lifecycle)
      implementation(libs.androidx.camera.view)
      implementation(libs.accompanist.permissions)
    }

    iosMain.dependencies {
      implementation(libs.ktor.client.darwin)
    }

    commonTest.dependencies {
      implementation(kotlin("test"))
    }
  }
}

// Room schema location + KSP for both targets
room {
  schemaDirectory("$projectDir/schemas")
}

dependencies {
  add("kspAndroid", libs.androidx.room.compiler)
  add("kspIosX64", libs.androidx.room.compiler)
  add("kspIosArm64", libs.androidx.room.compiler)
  add("kspIosSimulatorArm64", libs.androidx.room.compiler)
}

// Read Gemini API key from local.properties or env for Android BuildConfig
val geminiApiKey: String = run {
  val props = Properties()
  val f = rootProject.file("local.properties")
  if (f.exists()) f.inputStream().use { props.load(it) }
  props.getProperty("geminiApiKey") ?: System.getenv("GEMINI_API_KEY") ?: ""
}

android {
  namespace = "com.example"
  compileSdk = libs.versions.compileSdk.get().toInt()

  defaultConfig {
    applicationId = "com.suw1labs.caloriecam"
    minSdk = libs.versions.minSdk.get().toInt()
    targetSdk = libs.versions.targetSdk.get().toInt()
    versionCode = 1
    versionName = "1.0"
    buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
      excludes += "/META-INF/*.version"
      excludes += "/META-INF/versions/**"
      excludes += "/META-INF/*.kotlin_module"
      excludes += "/META-INF/DEPENDENCIES"
      excludes += "/META-INF/INDEX.LIST"
      excludes += "META-INF/io.netty.versions.properties"
      pickFirsts += "**/*.so"
    }
  }

  buildTypes {
    release {
      isMinifyEnabled = false
    }
  }
}
