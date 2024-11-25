plugins {
    application
    java
    id("org.danilopianini.gradle-java-qa") version "1.75.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.yaml:snakeyaml:2.3")
}

application {
    mainClass.set("it.unibo.mvc.DrawNumberApp")
}
