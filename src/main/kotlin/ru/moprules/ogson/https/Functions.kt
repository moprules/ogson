package ru.moprules.ogson.https


fun toHttps(str: String): String {
    var indexStart = str.indexOf("://")

    if (indexStart != -1) {
        indexStart += 3
    } else {
        indexStart = 0
    }

    return "https://${str.subSequence(indexStart, str.lastIndex)}"
}

fun isContainsHttpsAnnotation(annotations: Array<Annotation>): Boolean {
    return annotations.filterIsInstance<Https>().isNotEmpty()
}