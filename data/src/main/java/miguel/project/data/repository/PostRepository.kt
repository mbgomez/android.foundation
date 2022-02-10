package miguel.project.data.repository

import miguel.project.common.model.Resource
import miguel.project.common.model.Status
import miguel.project.data.datasource.local.PostDao
import miguel.project.data.datasource.local.PostTable
import miguel.project.data.datasource.remote.InternetConnectionChecker
import miguel.project.data.datasource.remote.PostService
import miguel.project.data.extensions.otherwise
import miguel.project.data.extensions.parse
import miguel.project.data.mapper.PostMapper
import miguel.project.domain.model.Post
import miguel.project.domain.repository.IPostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class PostRepository(
    private val postService: PostService,
    private val internetConnectionChecker: InternetConnectionChecker,
    private val postDao: PostDao,
    private val postMapper: PostMapper
) : IPostRepository {

    fun getPostsInternet(): Flow<Resource<List<PostTable>>> =
        flow {
            val response = postService.getPosts()
            val responseParsed = response.parse()
            val resource = if (responseParsed.status == Status.SUCCESS) {
                val postTable = responseParsed.data?.map { it.mapToTable() }
                postTable?.let { table ->
                    postDao.insertPosts(table)
                    Resource.success(table)
                }.otherwise {
                    Resource.success(emptyList())
                }

            } else {
                Resource.error(responseParsed.errorCode)
            }
            emit(resource)
        }

    fun getPostsDb(): Flow<Resource<List<PostTable>>> =
        flow {
            emit(Resource.success(postDao.getPosts()))
        }

    override fun getPosts(): Flow<Resource<List<Post>>> =
        if (internetConnectionChecker.isOnline()) {
            getPostsInternet()
        } else {
            getPostsDb()
        }.map {
            mapPosts(it)
        }

    fun mapPosts(resource: Resource<List<PostTable>>) =
        if (resource.status == Status.SUCCESS) {
            val t = resource.data?.map { postData ->
                postMapper.fromDataToDomain(postData)
            } ?: emptyList()
            Resource.success(t)
        } else {
            Resource.error(resource.errorCode)
        }


}