package ru.moprules.ogson.extract

// Аннотация Для извлечения поля из ответа и подстановка поля вместо ответа
@JvmRepeatable(Extracts::class)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class Extract(val field: String)

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.FIELD)
annotation class Extracts(val value: Array<Extract>)
