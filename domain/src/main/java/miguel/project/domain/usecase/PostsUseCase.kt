package miguel.project.domain.usecase


import miguel.project.common.model.Resource
import miguel.project.domain.model.Post
import miguel.project.domain.repository.IPostRepository
import kotlinx.coroutines.flow.Flow

class GetPostsUseCase(
    private val postRepository: IPostRepository
) : FlowInteractorNoParams<Resource<List<Post>>>() {
    override fun buildUseCase(): Flow<Resource<List<Post>>> {
        return postRepository.getPosts()
    }
}