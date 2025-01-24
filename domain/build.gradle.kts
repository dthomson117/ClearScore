plugins {
    alias(libs.plugins.kotlin.jvm)
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(libs.koin)
    implementation(libs.napier)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.junit)
}