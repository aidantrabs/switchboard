plugins {
    `java-library`
    `maven-publish`
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    testImplementation("org.junit.jupiter:junit-jupiter:5.11.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = "com.switchboard"
            artifactId = "switchboard-sdk-java"
        }
    }
}
