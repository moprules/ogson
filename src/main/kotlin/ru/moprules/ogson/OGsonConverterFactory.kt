package ru.moprules.ogson

import java.lang.reflect.Type

class OGsonConverterFactory private constructor(private val gson: com.google.gson.Gson) : retrofit2.Converter.Factory() {
    override fun responseBodyConverter(
        type: Type, annotations: Array<Annotation>, retrofit: retrofit2.Retrofit
    ): retrofit2.Converter<okhttp3.ResponseBody, *> {
        val adapter = gson.getAdapter(com.google.gson.reflect.TypeToken.get(type))
        return OGsonResponseBodyConverter(gson, adapter, annotations)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: retrofit2.Retrofit
    ): retrofit2.Converter<*, okhttp3.RequestBody> {
        val adapter = gson.getAdapter(com.google.gson.reflect.TypeToken.get(type))
        return OGsonRequestBodyConverter(gson, adapter)
    }

    companion object {
        @JvmOverloads  // Guarding public API nullability.
        fun create(gson: com.google.gson.Gson? = com.google.gson.Gson()): OGsonConverterFactory {
            if (gson == null) throw NullPointerException("gson == null")
            return OGsonConverterFactory(gson)
        }
    }
}