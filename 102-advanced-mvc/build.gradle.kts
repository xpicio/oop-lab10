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
    compileOnly("com.github.spotbugs:spotbugs-annotations:4.8.6")    
}

application {
    mainClass.set("it.unibo.mvc.DrawNumberApp")
}
