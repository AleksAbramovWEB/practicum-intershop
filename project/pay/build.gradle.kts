
dependencies {

}

tasks.bootJar {
    archiveFileName = "app-pay.jar"
    mainClass.set("ru.abramov.practicum.pay.PayApplication")
}

tasks.bootRun {
    mainClass.set("ru.abramov.practicum.pay.PayApplication")
}