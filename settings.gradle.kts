pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagementVersion: String by settings

    plugins {
        id("org.springframework.boot") version springBootVersion
        id("io.spring.dependency-management") version springDependencyManagementVersion
    }
}

rootProject.name = "switchboard"

include("switchboard-server")
include("switchboard-sdk-java")
include("switchboard-spring-boot-starter")
