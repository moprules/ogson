package ru.moprules.ogson

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.Writer
import java.nio.charset.StandardCharsets

class OGsonRequestBodyConverter<T>(private val gson: com.google.gson.Gson, private val adapter: com.google.gson.TypeAdapter<T>) :
    retrofit2.Converter<T, okhttp3.RequestBody> {
    private val MEDIA_TYPE: okhttp3.MediaType = "application/json; charset=UTF-8".toMediaType()

    @Throws(IOException::class)
    override fun convert(value: T): okhttp3.RequestBody {
        val buffer = okio.Buffer()
        val writer: Writer = OutputStreamWriter(buffer.outputStream(), StandardCharsets.UTF_8)
        val jsonWriter: com.google.gson.stream.JsonWriter = gson.newJsonWriter(writer)
        adapter.write(jsonWriter, value)
        jsonWriter.close()
        return buffer.readByteString().toRequestBody(MEDIA_TYPE)
    }
}