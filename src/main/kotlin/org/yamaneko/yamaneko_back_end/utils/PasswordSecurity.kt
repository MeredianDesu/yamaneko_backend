package org.yamaneko.yamaneko_back_end.utils

import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCrypt.gensalt
import org.springframework.stereotype.Service

@Service
class PasswordSecurity {

    fun hashPassword( userPassword: String ): String{
        val hashPassword = BCrypt.hashpw( userPassword, gensalt( 7 ) )

        return hashPassword
    }

    fun verifyPassword( userPassword: String, hashPassword: String ): Boolean{

        return try {
            BCrypt.checkpw( userPassword, hashPassword )
        } catch( e: IllegalArgumentException ){
            println( "Verify password exception: $e")
            false
        }
    }
}