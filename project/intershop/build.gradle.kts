

plugins {
    id("org.flywaydb.flyway") version "9.9.0"
}


dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-postgresql:0.8.6.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.flywaydb:flyway-core")
    implementation("org.flywaydb:flyway-database-postgresql")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

    implementation("org.postgresql:postgresql")

    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive")
}

tasks.bootJar {
    archiveFileName = "app.jar"
    mainClass.set("ru.abramov.practicum.intershop.PracticumIntershopApplication")
}

tasks.bootRun {
    mainClass.set("ru.abramov.practicum.intershop.PracticumIntershopApplication")
}

openApiGenerate {
    generatorName.set("java")
    inputSpec.set("$rootDir/project/pay/src/main/resources/openapi.yaml")
    outputDir.set("$rootDir/project/intershop")
    apiPackage.set("ru.abramov.practicum.intershop.client.pay.api")
    invokerPackage.set("ru.abramov.practicum.intershop.client.pay.invoker")
    modelPackage.set("ru.abramov.practicum.intershop.client.pay.domain")
    configOptions.set(
        mapOf(
            "library" to "webclient",
            "reactive" to "true",
            "openApiNullable" to "false",
            "useBeanValidation" to "true",
            "performBeanValidation" to "true",
            "generateApiDocumentation" to "false",
            "generateModelDocumentation" to "false",
            "generateApiTests" to "false",
            "generateModelTests" to "false",
            "skipDefaultInterface" to "true",
            "generateSupportingFiles" to "false"
        )
    )
}


val postgresHost: String = System.getenv("POSTGRES_HOST") ?: ""
val postgresPort: String = System.getenv("POSTGRES_PORT") ?: ""
val postgresDb: String = System.getenv("POSTGRES_DB") ?: ""
val postgresUser: String = System.getenv("POSTGRES_USER") ?: ""
val postgresPassword: String = System.getenv("POSTGRES_PASSWORD") ?: ""

flyway {
    url = "jdbc:postgresql://$postgresHost:$postgresPort/$postgresDb"
    user = postgresUser
    password = postgresPassword
    locations = arrayOf("classpath:db/migration")
}