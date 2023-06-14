package demo

import kotlinx.coroutines.delay
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@RestController
class DemoApplication {

    @GetMapping("/")
    suspend fun index() = run {
        delay(10)
        "hello, world"
    }
}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
