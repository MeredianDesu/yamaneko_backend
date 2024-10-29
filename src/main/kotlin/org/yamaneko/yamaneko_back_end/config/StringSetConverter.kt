package org.yamaneko.yamaneko_back_end.config

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class StringSetConverter: AttributeConverter< Set< String >, String> {
    override fun convertToDatabaseColumn( attribute: Set<String>? ): String {
        return attribute?.joinToString(",") ?: ""
    }

    override fun convertToEntityAttribute( dbData: String? ): Set<String> {
        return dbData?.split(",")?.toSet() ?: emptySet()
    }
}