plugins {
    `java-library`
    `maven-publish`
}

java {
    withJavadocJar()
    withSourcesJar()
}

group = "de.juliansiebert"
version = project.properties["projectVersion"].toString()

repositories {
    mavenCentral()
}