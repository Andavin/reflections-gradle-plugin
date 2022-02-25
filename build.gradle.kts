plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    id("com.gradle.plugin-publish") version "0.18.0"
}

group = "io.github.andavin"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.dom4j:dom4j:2.1.3")
}

pluginBundle {
    website = "https://github.com/Andavin/reflections-gradle-plugin"
    vcsUrl = "https://github.com/Andavin/reflections-gradle-plugin"
    description = "Pre-scan and embed a Reflections metadata index file into your artifact."
    tags = listOf("reflections")
}

gradlePlugin {
    plugins {
        create("greetingsPlugin") {
            id = "io.github.andavin.reflections.gradle.plugin"
            displayName = "Reflections Gradle Plugin"
            description = "Pre-scan and embed a Reflections metadata index file into your artifact."
            implementationClass = "io.github.andavin.reflections.gradle.plugin.ReflectionsPlugin"
        }
    }
}