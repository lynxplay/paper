plugins {
    kotlin("jvm") version "2.1.20"
}

dependencies {
    testImplementation(project(":shelfback:shelfback-runner"))
}

tasks.test {
    useJUnitPlatform {
        includeEngines("papermc:shelfback")
    }

    outputs.upToDateWhen { false }
}
