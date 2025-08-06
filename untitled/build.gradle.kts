plugins {
    id("java")
}

group = "com.ll.wisesaying"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {

    /**
     * Util 의존성
     * - Object Mapper
     */
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.0")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}