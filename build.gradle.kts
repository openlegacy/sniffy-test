plugins {
    kotlin("jvm") version "1.4.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"
val sniffyVersion = "3.1.12-SNAPSHOT"
val kotlinVersion = "1.4.21"
val kotlinSerializationVersion = "1.1.0"
val vertxVersion = "4.0.3"
val javaVersion: String by project

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {

    implementation(kotlin("stdlib"))

    // kotlin
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:$kotlinSerializationVersion")
    api("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")


    implementation("io.vertx:vertx-web-client:${vertxVersion}")
    implementation("io.vertx:vertx-web:${vertxVersion}")
    implementation("io.vertx:vertx-lang-kotlin-coroutines:${vertxVersion}")

    // CTG
    implementation("com.ibm:ctg:9.2")

    // Sniffy
    implementation("io.sniffy:sniffy-core:$sniffyVersion")
    implementation("io.sniffy:sniffy-module-nio:$sniffyVersion")
    implementation("io.sniffy:sniffy-module-nio-compat:$sniffyVersion")
    implementation("io.sniffy:sniffy-module-tls:$sniffyVersion")

    // test.junit
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    // test.assertj
    testImplementation("org.assertj:assertj-core:3.18.1")
}

tasks {
    withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = javaVersion
        }
    }
    withType<JavaCompile> {
        sourceCompatibility = javaVersion
        targetCompatibility = javaVersion
        options.encoding = "UTF-8"
    }
    named<Test>("test") {
        useJUnitPlatform()
    }
}