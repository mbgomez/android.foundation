package miguel.project.data.repository


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import miguel.project.data.datasource.local.PostDao
import miguel.project.data.datasource.local.PostTable
import miguel.project.data.datasource.remote.InternetConnectionChecker
import miguel.project.data.datasource.remote.PostResponse
import miguel.project.data.datasource.remote.PostService
import miguel.project.data.extensions.parse
import miguel.project.common.model.Resource
import miguel.project.common_test.extension.CoroutinesTestRule
import miguel.project.common_test.extension.test
import miguel.project.data.mapper.PostMapper
import miguel.project.domain.model.Post
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import retrofit2.Response

@ExperimentalCoroutinesApi
class PostRepositoryTests {
    private lateinit var repository: PostRepository
    private lateinit var postService: PostService
    private lateinit var internetConnectionChecker: InternetConnectionChecker
    private lateinit var postDao: PostDao
    private lateinit var mapper: PostMapper

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesTestRule()


    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        postService = mockk(relaxed = true)
        internetConnectionChecker = mockk(relaxed = true)
        postDao = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        repository =
            spyk(PostRepository(postService, internetConnectionChecker, postDao, mapper))
    }

    @Test
    fun getPhotosInternet_call_success_data() = runBlockingTest {
        val userId = 1L
        val id = 2L
        val title = "title"
        val body = "body"

        val photoResponse = PostResponse(userId, id, title, body)
        val photoTable = PostTable(userId, id, title, body)
        val response = Response.success(listOf(photoResponse))
        val resourceResponse = Resource.success(listOf(photoResponse))
        val resourceTable = Resource.success(listOf(photoTable))

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        coEvery { response.parse() } returns resourceResponse
        coEvery { postService.getPosts() } returns response

        val flowTest = repository.getPostsInternet().test(this)

        coVerify(exactly = 1) { response.parse() }
        coVerify(exactly = 1) { postService.getPosts() }
        coVerify(exactly = 1) { postDao.insertPosts(listOf(photoTable)) }

        flowTest.assertValues(resourceTable)
        flowTest.finish()

    }

    @Test
    fun getPhotosInternet_call_success_no_data() = runBlockingTest {

        val response = Response.success<List<PostResponse>>(null)
        val resourceResponse = Resource.success<List<PostResponse>>(null)
        val resourceTable = Resource.success<List<PostTable>>(emptyList())

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        coEvery { response.parse() } returns resourceResponse
        coEvery { postService.getPosts() } returns response

        val flowTest = repository.getPostsInternet().test(this)

        coVerify(exactly = 1) { response.parse() }
        coVerify(exactly = 1) { postService.getPosts() }
        coVerify(exactly = 0) { postDao.insertPosts(any()) }

        flowTest.assertValues(resourceTable)
        flowTest.finish()

    }

    @Test
    fun getPhotosInternet_call_error() = runBlockingTest {
        val userId = 1L
        val id = 2L
        val title = "title"
        val body = "body"

        val photoResponse = PostResponse(userId, id, title, body)
        val response = Response.success(listOf(photoResponse))
        val resourceResponse = Resource.error<List<PostResponse>>(1)
        val resourceTable = Resource.error<List<PostTable>>(1)

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        coEvery { response.parse() } returns resourceResponse
        coEvery { postService.getPosts() } returns response

        val flowTest = repository.getPostsInternet().test(this)

        coVerify(exactly = 1) { response.parse() }
        coVerify(exactly = 1) { postService.getPosts() }

        flowTest.assertValues(resourceTable)
        flowTest.finish()

    }

    @Test
    fun getPostsDb_call() = runBlockingTest {
        val userId = 1L
        val id = 2L
        val title = "title"
        val body = "body"

        val photoTable = PostTable(userId, id, title, body)
        val resourceTable = Resource.success(listOf(photoTable))

        coEvery { postDao.getPosts() } returns listOf(photoTable)

        val flowTest = repository.getPostsDb().test(this)

        coVerify(exactly = 1) { postDao.getPosts() }

        flowTest.assertValues(resourceTable)
        flowTest.finish()

    }

    @Test
    fun getPosts_internet() = runBlockingTest {

        val resource = Resource.success<List<PostTable>>(emptyList())

        coEvery { internetConnectionChecker.isOnline() } returns true
        coEvery { repository.getPostsInternet() } returns flow {
            emit(resource)
        }

        repository.getPosts().test(this)

        coVerify(exactly = 1) { internetConnectionChecker.isOnline() }
        coVerify(exactly = 1) { repository.getPostsInternet() }
        coVerify(exactly = 0) { repository.getPostsDb() }
        coVerify(exactly = 1) { repository.mapPosts(resource) }

    }

    @Test
    fun getPosts_db() = runBlockingTest {

        val resource = Resource.success<List<PostTable>>(emptyList())

        coEvery { internetConnectionChecker.isOnline() } returns false
        coEvery { repository.getPostsDb() } returns flow {
            emit(resource)
        }

        repository.getPosts().test(this)

        coVerify(exactly = 1) { internetConnectionChecker.isOnline() }
        coVerify(exactly = 0) { repository.getPostsInternet() }
        coVerify(exactly = 1) { repository.getPostsDb() }
        coVerify(exactly = 1) { repository.mapPosts(resource) }


    }

    @Test
    fun mapPosts_error() {
        val errorCode = 1
        val resource = Resource.error<List<PostTable>>(errorCode)
        val expectedResult = Resource.error<List<Post>>(errorCode)
        val result = repository.mapPosts(resource)

        Assert.assertEquals(expectedResult, result)
    }

    @Test
    fun mapPosts_success() {

        val mockPostTable  = mockk<PostTable>()
        val resource = Resource.success(listOf(mockPostTable))

        val mockPost  = mockk<Post>()
        val expectedResult = Resource.success(listOf(mockPost))

        every { mapper.fromDataToDomain(mockPostTable) } returns mockPost

        val result = repository.mapPosts(resource)

        Assert.assertEquals(expectedResult, result)
    }


}