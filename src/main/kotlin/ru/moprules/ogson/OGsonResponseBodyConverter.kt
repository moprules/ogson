package ru.moprules.ogson

import ru.moprules.ogson.extract.getExtracts
import ru.moprules.ogson.extract.getSubJson
import java.io.IOException

class OGsonResponseBodyConverter<T>(
    private val gson: com.google.gson.Gson,
    private val adapter: com.google.gson.TypeAdapter<T>,
    private val annotations: Array<Annotation>,
) : retrofit2.Converter<okhttp3.ResponseBody, T> {
    @Throws(IOException::class)
    override fun convert(value: okhttp3.ResponseBody): T? {
        val jsonReader: com.google.gson.stream.JsonReader = gson.newJsonReader(value.charStream())


        // Парсим вcё в json объект
        var json: com.google.gson.JsonElement? = value.use {
            jsonReader.use {reader ->
                com.google.gson.JsonParser.parseReader(reader)
            }
        }

        getExtracts(annotations).forEach { extractAnnotation ->
            json = getSubJson(json, extractAnnotation.field)
        }

        if (json == null) {
            json = com.google.gson.JsonNull.INSTANCE
        }

//        return null
        return adapter.fromJsonTree(json)
    }
}