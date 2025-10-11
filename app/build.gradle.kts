plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)

}

android {
    namespace = "com.example.nihongomaster"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nihongomaster"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Core + AppCompat
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // UI cơ bản
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)                         // Material 3 (LinearProgressIndicator, MaterialToolbar, MaterialCardView, ...)

    // 🔧 Những phần bạn đang dùng trong code:
    implementation(libs.androidx.activity.ktx)            // ADD: enableEdgeToEdge(), lifecycle scope cho Activity
    implementation(libs.androidx.fragment.ktx)            // ADD: viewModels() trong Fragment, các tiện ích KTX
    implementation(libs.androidx.recyclerview)            // ADD: RecyclerView cho list/category
    implementation(libs.androidx.coordinatorlayout)       // ADD: CoordinatorLayout + AppBarLayout behavior

    // Lifecycle + Navigation
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
