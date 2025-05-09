plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "com.algopuzzle"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    // Weitere Bibliotheken nach Bedarf
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("MainKt") // Passe dies an deine Hauptklasse an
}

tasks.test {
    useJUnitPlatform()
}