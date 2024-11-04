package org.yamaneko.yamaneko_back_end.utils

import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@Component
class DateFormatter {

    fun dateToString( date: Date ): String {
        val localDateTime = LocalDateTime.ofInstant( date.toInstant(), ZoneId.systemDefault() )
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")

        return localDateTime.format( formatter )
    }
}