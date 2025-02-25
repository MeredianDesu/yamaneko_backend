package org.yamaneko.yamaneko_back_end.utils

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class DateFormatter {

    // Если снова не заработает: поменять формат
    private final val pattern = "dd/MM/yyyy HH:mm:ss"
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern( pattern )

    fun dateToString( date: Date ): String {
        val localDateTime = LocalDateTime.ofInstant( date.toInstant(), ZoneId.systemDefault() )

        return localDateTime.format( formatter )
    }
}