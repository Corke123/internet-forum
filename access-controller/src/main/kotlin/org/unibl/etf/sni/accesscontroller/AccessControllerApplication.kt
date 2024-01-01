package org.unibl.etf.sni.accesscontroller

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AccessControllerApplication

fun main(args: Array<String>) {
    runApplication<AccessControllerApplication>(*args)
}
