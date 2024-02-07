/*
 * This file was generated by the Gradle 'init' task.
 */

plugins {
    java
    `maven-publish`
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("io.trino:trino-spi:426")
    implementation("io.airlift:log:240")
    implementation("org.komamitsu:fluency-core:2.7.0")
    implementation("org.komamitsu:fluency-fluentd:2.7.0")
    implementation("com.google.guava:guava:33.0.0-jre")
}

group = "fluentd"
version = "0.0.7"
description = "presto-fluentd"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}
