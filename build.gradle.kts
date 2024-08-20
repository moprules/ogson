plugins {
    kotlin("jvm") version "2.0.0"
    `java-library`
    `maven-publish`
}

group = "ru.moprules"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("com.google.code.gson:gson:[2.11.0, 3.0)")
    implementation("com.squareup.retrofit2:retrofit:[2.11.0, 3.0)")
    implementation("com.squareup.okhttp3:okhttp:[5.0.0-alpha.14, 6.0.0)")
    implementation(kotlin("reflect"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

publishing {
    repositories {
//        // Публикация в OSSRH (Maven Central)
//        maven {
//            name = "OSSRH"
//            url = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
//            credentials {
//                username = System.getenv("MAVEN_USERNAME")
//                password = System.getenv("MAVEN_PASSWORD")
//            }
//        }

        // Публикация в GitHub Packages
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/moprules/ogson")  // Обновите URL на свой
            credentials {
                username = System.getenv("GITHUB_ACTOR")  // Имя пользователя GitHub (обычно GITHUB_ACTOR в Actions)
                password = System.getenv("GITHUB_TOKEN")  // Токен доступа (обычно GITHUB_TOKEN в Actions)
            }
        }
    }
}