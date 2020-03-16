description = "Spring Boot GraphQL API"

plugins {
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
}

val springBootVersion: String by project
val reactorVersion: String by project
val graphqlVersion: String by project
val kotlinTestVersion: String by project
val kluentVersion: String by project
val mockkVersion: String by project

dependencies {
    implementation("com.expediagroup:graphql-kotlin-spring-server:$graphqlVersion")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation("io.projectreactor:reactor-test:$reactorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:$kotlinTestVersion")
    testImplementation("org.amshove.kluent:kluent:$kluentVersion")
    testImplementation("io.mockk:mockk:$mockkVersion")
}

tasks {
    jacocoTestCoverageVerification {
        violationRules {
            rule {
                limit {
                    counter = "INSTRUCTION"
                    value = "COVEREDRATIO"
                    minimum = "0.96".toBigDecimal()
                }
                limit {
                    counter = "BRANCH"
                    value = "COVEREDRATIO"
                    minimum = "0.95".toBigDecimal()
                }
            }
        }
        classDirectories.setFrom(
            sourceSets.main.get().output.asFileTree.matching {
                // exclude main()
                exclude("com/pyruby/phones/ApplicationKt.class")
            }
        )
    }
}
