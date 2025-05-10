
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.5")
}

tasks.jar {
    enabled = false
}

tasks.bootJar {
    archiveFileName = "app.jar"
    mainClass.set("ru.abramov.practicum.pay.PayApplication")
}

tasks.bootRun {
    mainClass.set("ru.abramov.practicum.pay.PayApplication")
}

openApiGenerate {
    generatorName.set("spring")
    inputSpec.set("$rootDir/project/pay/src/main/resources/openapi.yaml")
    outputDir.set("$rootDir/project/pay")
    apiPackage.set("ru.abramov.practicum.pay.api")
    modelPackage.set("ru.abramov.practicum.pay.model")
    configOptions.set(
        mapOf(
            "library" to "spring-boot",
            "reactive" to "true",
        )
    )
}