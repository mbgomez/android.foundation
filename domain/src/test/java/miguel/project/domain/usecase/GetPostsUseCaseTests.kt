package miguel.project.domain.usecase

import miguel.project.domain.model.Post
import miguel.project.common.model.Resource
import miguel.project.common_test.extension.CoroutinesTestRule
import miguel.project.common_test.extension.test
import miguel.project.domain.repository.IPostRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class GetPostsUseCaseTests {
    private lateinit var postRepository: IPostRepository
    private lateinit var getPostsUseCase: GetPostsUseCase

    @ExperimentalCoroutinesApi
    @get:Rule
    var coroutinesRule = CoroutinesTestRule()

    @Before
    fun setup() {
        postRepository = mockk(relaxed = true)
        getPostsUseCase = GetPostsUseCase(postRepository)
    }

    @Test
    fun run_success() = runBlockingTest {

        val resource = mockk<Resource<List<Post>>>()
        val flow = flowOf(resource)

        coEvery { postRepository.getPosts() } returns flow

        getPostsUseCase.run().test(this)

        coVerify(exactly = 1) { postRepository.getPosts() }

    }



}