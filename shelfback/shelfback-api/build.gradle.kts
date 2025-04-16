plugins {
    kotlin("jvm") version "2.1.20"
}

dependencies {
    api("org.junit.jupiter:junit-jupiter-api:5.13.0-M2")
    api(project(":paper-api"))
    api(project(":paper-server"))
}
