plugins {
    kotlin("jvm")
    java
    id ("com.bmuschko.docker-java-application") version "9.3.2"
}

dependencies {
    val rabbitVersion: String by project
    val jacksonVersion: String by project
    val logbackVersion: String by project
    val coroutinesVersion: String by project
    val testContainersVersion: String by project

    implementation(kotlin("stdlib"))
    implementation("com.rabbitmq:amqp-client:$rabbitVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")

    // common
    implementation(project(":common"))

    // api
    implementation(project(":jackson"))
    implementation(project(":mappers"))

    implementation(project(":biz"))

    testImplementation("org.testcontainers:rabbitmq:$testContainersVersion")
    testImplementation(kotlin("test"))
    testImplementation(project(":stubs"))
}

docker {
    javaApplication {

        baseImage.set("openjdk:17")
        ports.set(listOf(8080,5672))
        images.set(setOf("${project.name}:latest"))
        jvmArgs.set(listOf("-XX:+UseContainerSupport"))
    }

}