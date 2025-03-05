package org.yamaneko.yamaneko_back_end

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import java.security.MessageDigest

@SpringBootApplication
class YamanekoBackEndApplication: SpringBootServletInitializer() {}

@OptIn(ExperimentalStdlibApi::class)
fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest( this.toByteArray() )

    return digest.toHexString()
}

fun main(args: Array<String>) {
    runApplication<YamanekoBackEndApplication>(*args)
}

//@Override
//fun configure( application: SpringApplicationBuilder ): SpringApplicationBuilder {
//    return application.sources( YamanekoBackEndApplication::class.java )
//}