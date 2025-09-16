import java.util.Date

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    dynamicFeatures += setOf(":dynamicfeature")
}

tasks.register("writeVersionSuffix") {
    doLast {
        var versionSuffix = getUnifiedVersionSuffix(1234)
        println("writeVersionSuffix versionSuffix: ${versionSuffix}")
        var versionSuffixFile = file("${buildDir}/outputs/versionSuffix.txt")
        versionSuffixFile.writeText(versionSuffix)
    }
}


tasks.register("cleanOriginArtifacts") {
    doLast {

    }
}

fun getUnifiedVersionSuffix(number: Any?): String {
    val versionName = android.defaultConfig.versionName
    val versionParts = versionName?.split(".")?: listOf("1", "0")
    val majorMinorVersion = if (versionParts.size < 2) "${versionParts[0]}.0" else "${versionParts[0]}.${versionParts[1]}"
    val validNumber = number ?: "000"
    val suffix = "${majorMinorVersion}.${validNumber}"
    return suffix
}

tasks.register("standardizeArtifactsNames") {
    doLast {
        copy {
            val outDir = "${buildDir}/outputs"
            val versionSuffix = getUnifiedVersionSuffix(1234)
            into(outDir)
            println("standardizeArtifactsNames outputDir: $outDir, versionSuffix: $versionSuffix")

            into("apk_from_bundle/release") {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
                from("${outDir}/apk_from_bundle/release")
                rename("app-release-universal.apk", "app_release_universal_${versionSuffix}.apk")
            }

            into("bundle/release") {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
                from("${outDir}/bundle/release")
                rename("app-release.aab", "app_release_${versionSuffix}.aab")
            }

            into("mapping/release") {
                duplicatesStrategy = DuplicatesStrategy.INCLUDE
                from("${outDir}/mapping/release")
                rename("mapping.txt", "mapping_${versionSuffix}.txt")
            }
        }
    }
}

tasks.register("publishRelease", GradleBuild::class) {
    doFirst {
        println("=============== core build publishRelease start ===============")
    }
    dependsOn("clean")
    setTasks(listOf("bundleRelease", "packageReleaseUniversalApk", "standardizeArtifactsNames", "writeVersionSuffix", "cleanOriginArtifacts"))
    doLast {
        println("=============== core build publishRelease finish ===============")
    }
}

dependencies {
    implementation(project(":mylibrary"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    //implementation(libs.androidx.navigation.fragment.ktx)
    //implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}