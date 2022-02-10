package miguel.project.data.datasource.remote

import miguel.project.data.datasource.local.PostTable

data class PostResponse(
    val userId : Long?,
    val id: Long?,
    val title: String?,
    val body: String?
) {
    fun mapToTable() = PostTable(userId, id, title, body)
}