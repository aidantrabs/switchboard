plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    api(project(":switchboard-sdk-java"))

    implementation("dev.openfeature:sdk:1.12.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.switchboard"
            artifactId = "switchboard-openfeature-provider"
        }
    }
}
