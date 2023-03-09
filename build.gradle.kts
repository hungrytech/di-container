plugins {
    id("java")
    `maven-publish`
}

repositories {
    mavenCentral()
}


publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "org.hungrytech"
            artifactId = "di-container"
            version = "0.0.5"

            from(components["java"])
        }
    }
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
