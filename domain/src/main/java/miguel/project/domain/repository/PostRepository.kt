package miguel.project.domain.repository

import miguel.project.common.model.Resource
import miguel.project.domain.model.Post
import kotlinx.coroutines.flow.Flow

interface IPostRepository {
    fun getPosts(): Flow<Resource<List<Post>>>
}
