package io.positivinh.virtuoso.security.dummy

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DummySecurityApplication

fun main(args: Array<String>) {

    runApplication<DummySecurityApplication>(*args)
}
