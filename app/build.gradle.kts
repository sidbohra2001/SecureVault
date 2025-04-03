import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.sid.securevault"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sid.securevault"
        minSdk = 31
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

    packaging {
        resources.pickFirsts.add("README.md")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.database) //firebase
    implementation(libs.lombok) //lombok
    implementation(libs.bcprov.jdk15on) //BCrypt

    annotationProcessor(libs.lombok) //lombok

    testImplementation(libs.lombok) //lombok
    testImplementation(libs.junit)

    testAnnotationProcessor(libs.lombok) //lombok

    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}