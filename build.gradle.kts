plugins {
    kotlin("jvm") version "1.4.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"
val sniffyVersion = "3.1.10-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
}

dependencies {

    implementation(kotlin("stdlib"))

    // CTG
    implementation("com.ibm:ctg:9.2")

    // Sniffy
    implementation("io.sniffy:sniffy-core:$sniffyVersion")
    implementation("io.sniffy:sniffy-module-nio:$sniffyVersion")
    implementation("io.sniffy:sniffy-module-nio-compat:$sniffyVersion")

    // test.junit
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.0")

    // test.assertj
    testImplementation("org.assertj:assertj-core:3.18.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}