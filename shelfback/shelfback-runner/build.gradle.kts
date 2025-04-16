plugins {
    kotlin("jvm") version "2.1.20"
    `java-library`
}

dependencies {
    api(project(":shelfback:shelfback-api"))
    implementation("org.junit.jupiter:junit-jupiter-engine:5.13.0-M2")
}

tasks.test {
    useJUnitPlatform()
}
