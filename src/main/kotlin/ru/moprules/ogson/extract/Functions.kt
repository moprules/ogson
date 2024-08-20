package ru.moprules.ogson.extract

import com.google.gson.JsonArray
import com.google.gson.JsonElement

fun getExtracts(annotations: Array<Annotation>): List<Extract> {
    val resExtracts = mutableListOf<Extract>()
    annotations.forEach {
        when (it) {
            is Extract -> resExtracts.add(it)
            is Extracts -> resExtracts.addAll(it.value)
        }
    }
    return resExtracts
}

fun getSubJson(json: JsonElement?, fieldName: String): JsonElement? {
    return if (json?.isJsonArray == true) {
        val newJson = JsonArray()
        json.asJsonArray?.forEach {
            newJson.add(it?.asJsonObject?.get(fieldName))
        }
        newJson
    } else {
        json?.asJsonObject?.get(fieldName)
    }
}
