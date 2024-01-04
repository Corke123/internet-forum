package org.unibl.etf.sni.accesscontroller.config

import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.ErrorPageRegistrar
import org.springframework.boot.web.server.ErrorPageRegistry
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component

@Component
class ErrorPageConfig : ErrorPageRegistrar {
    override fun registerErrorPages(registry: ErrorPageRegistry?) {
        registry?.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "index.html"))
    }
}