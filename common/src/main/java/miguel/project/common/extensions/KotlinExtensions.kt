package miguel.project.common.extensions

inline fun <R> R?.otherwise(block: () -> R): R {
    return this ?: block()
}