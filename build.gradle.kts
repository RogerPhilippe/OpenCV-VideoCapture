plugins {
    kotlin("jvm") version "1.8.0"
    application
}

group = "br.com.phs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    //implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(files("/usr/local/share/java/opencv4/opencv-490.jar"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

application {

    mainClass.set("br.com.phs.opencvvideocapture.MainKt")

    applicationDefaultJvmArgs += listOf(
        "-Djava.library.path=/usr/local/share/java/opencv4/"
    )

}