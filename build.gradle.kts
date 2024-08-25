import cn.lalaki.pub.BaseCentralPortalPlusExtension
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Properties


plugins {
    kotlin("jvm") version "2.0.20"
    `java-library`
    `maven-publish`
    id("cn.lalaki.central") version "1.2.5"
    signing
}

fun getLastVersion(defaultVersion: String = "1.0.0-SNAPSHOT"): String {
    val stdout = ByteArrayOutputStream()
    return try {
        exec {
            commandLine = "git describe --tags --abbrev=0".split(" ")
            standardOutput = stdout
        }
        val tag = stdout.toString().trim()
        if (tag.isEmpty()) {
            defaultVersion
        } else {
            // Удалить первую букву 'v', если она есть
            tag.removePrefix("v")
        }
    } catch (e: Exception) {
        defaultVersion
    }
}

group = "ru.moprules"
version = getLastVersion("0.0.1-SNAPSHOT")

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

val localMavenRepo = uri(layout.buildDirectory.dir("local_maven"))
centralPortalPlus {
    url = localMavenRepo
    username = localProperties["maven.username"] as String? ?: System.getenv("MAVEN_USER")
    password = localProperties["maven.password"] as String? ?: System.getenv("MAVEN_PASS")
    publishingType = BaseCentralPortalPlusExtension.PublishingType.AUTOMATIC // or PublishingType.AUTOMATIC
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
        }
    }
    repositories {

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
            name = "localMavenRepo"
            url = localMavenRepo
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

tasks.register("lastVer") {
    doLast {
        val ver = getLastVersion()
        println("project verson: $ver")
    }
}