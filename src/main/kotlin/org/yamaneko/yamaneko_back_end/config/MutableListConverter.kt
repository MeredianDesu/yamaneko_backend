package org.yamaneko.yamaneko_back_end.config

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import org.yamaneko.yamaneko_back_end.dto.dubber.DubberDTO

//Converter for entity.Release mutableList<DubberDTO>
@Converter
class MutableListConverter : AttributeConverter<MutableList<DubberDTO>, String> {
    private val objectMapper = jacksonObjectMapper()

    override fun convertToDatabaseColumn(attribute: MutableList<DubberDTO>?): String {
        return try {
            // Convert to JSON and log the output for debugging
            val json = objectMapper.writeValueAsString(attribute)
            println("Converted dubbers to JSON: $json") // Log the JSON string
            json
        } catch (e: JsonProcessingException) {
            // Handle exception and log the error
            println("Error converting dubbers to JSON: ${e.message}")
            ""
        }
    }

    override fun convertToEntityAttribute(dbData: String?): MutableList<DubberDTO> {
        println("DB Data for dubbers: $dbData") // Log the data retrieved from the database
        return if (dbData.isNullOrEmpty()) {
            mutableListOf()
        } else {
            try {
                objectMapper.readValue(
                    dbData,
                    objectMapper.typeFactory.constructCollectionType(MutableList::class.java, DubberDTO::class.java)
                )
            } catch (e: JsonProcessingException) {
                e.printStackTrace() // Log the error
                mutableListOf()
            }
        }
    }
}
