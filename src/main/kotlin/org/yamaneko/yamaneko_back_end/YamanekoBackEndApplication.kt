package org.yamaneko.yamaneko_back_end

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class YamanekoBackEndApplication: SpringBootServletInitializer() {}

fun main(args: Array<String>) {
    runApplication<YamanekoBackEndApplication>(*args)
}

@Override
fun configure( application: SpringApplicationBuilder ): SpringApplicationBuilder {
    return application.sources( YamanekoBackEndApplication::class.java )
}