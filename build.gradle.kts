import java.util.*
import org.gradle.api.tasks.Copy

plugins {
    kotlin("jvm") version "2.0.0"
    `java-library`
    `maven-publish`
    signing
}

group = "ru.moprules"
version = "0.0.1-alpha.1"

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

// Загрузка свойств из файла
val localProperties = Properties().apply {
    val localFile = File(rootProject.projectDir, "local.properties")
    if (localFile.exists()) {
        load(localFile.inputStream())
    }
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            pom {
                name = "ogson"
                description = "Useful library for retorfit2"
                url = "https://github.com/moprules/ogson"
                licenses {
                    license {
                        name = "MIT"
                        url = "https://opensource.org/licenses/MIT"
                    }
                }
                developers {
                    developer {
                        id = "moprules"
                        name = "Mop Rules"
                        email = "rav-navini-gego-cutropal@yandex.ru"
                    }
                }
                scm {
                    connection = "scm:git:git://github.com/moprules/ogson.git.git"
                    developerConnection = "scm:git:ssh://github.com/moprules/ogson.git.git"
                    url = "https://github.com/moprules/ogson"
                }
            }

//            groupId = "ru.moprules"
//            artifactId = "ogson"
//            version = "X.X.X"
        }
    }
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
            url = uri("https://maven.pkg.github.com/moprules/ogson")
            credentials {
                // Имя пользователя GitHub (обычно GITHUB_ACTOR в Actions)
                username = localProperties["gpr.user"] as String? ?: System.getenv("GITHUB_ACTOR")
                // Токен доступа (обычно GITHUB_TOKEN в Actions)
                password = localProperties["gpr.token"] as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }

        maven {
            name = "LocalMavenRepo"
            url = uri("file:///home/mop/local-maven-repo") // Укажите локальный путь
        }
    }
}

tasks.javadoc {
    if (JavaVersion.current().isJava9Compatible) {
        (options as StandardJavadocDocletOptions).addBooleanOption("html5", true)
    }
}

signing {
    val signingSecretKey: String? =
        localProperties["signing.secretKey"] as String? ?: System.getenv("SIGNING_SECRET_KEY")
    val signingPassword: String? = localProperties["signing.password"] as String? ?: System.getenv("SIGNING_PASSWORD")

    useInMemoryPgpKeys(signingSecretKey, signingPassword)
    sign(publishing.publications["mavenJava"])
}
