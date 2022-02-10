package miguel.project.data.extensions

inline fun <R> R?.otherwise(block: () -> R): R {
    return this ?: block()
}