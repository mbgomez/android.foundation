package miguel.project.data.testextensions


import miguel.project.data.extensions.AUTH_ERROR_CODE
import miguel.project.data.extensions.INTERNAL_ERROR_CODE
import miguel.project.data.extensions.UNHANDLE_ERROR_CODE
import miguel.project.data.extensions.parse
import miguel.project.common.model.Resource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Test
import retrofit2.Response
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class RetrofitExtensionsTests {

    private val fakeErrorResponseBody: ResponseBody =
        ResponseBody.create(MediaType.parse(""), "")

    data class TestResponse(
        val data1:String,
        val data2:Int
    )

    @Test
    fun test_parse_success()  {
        val data1 = "data1"
        val data2 = 1

        val postResponse = TestResponse(data1, data2)
        val response = Response.success(postResponse)

        val expectedResource = Resource.success(postResponse)

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        val parseResponse = response.parse()

        Assert.assertEquals(parseResponse, expectedResource)

    }

    @Test
    fun test_parse_HTTP_INTERNAL_ERROR()  {

        val response = Response.error<TestResponse>(
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            fakeErrorResponseBody
        )

        val expectedResource = Resource.error<TestResponse>(INTERNAL_ERROR_CODE)

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        val parseResponse = response.parse()

        Assert.assertEquals(parseResponse, expectedResource)

    }

    @Test
    fun test_parse_HTTP_GATEWAY_TIMEOUT()  {

        val response = Response.error<TestResponse>(
            HttpURLConnection.HTTP_GATEWAY_TIMEOUT,
            fakeErrorResponseBody
        )

        val expectedResource = Resource.error<TestResponse>(INTERNAL_ERROR_CODE)

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        val parseResponse = response.parse()

        Assert.assertEquals(parseResponse, expectedResource)

    }

    @Test
    fun test_parse_HTTP_UNAUTHORIZED()  {

        val response = Response.error<TestResponse>(
            HttpURLConnection.HTTP_UNAUTHORIZED,
            fakeErrorResponseBody
        )

        val expectedResource = Resource.error<TestResponse>(AUTH_ERROR_CODE)

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        val parseResponse = response.parse()

        Assert.assertEquals(parseResponse, expectedResource)

    }

    @Test
    fun test_parse_HTTP_FORBIDDEN()  {

        val response = Response.error<TestResponse>(
            HttpURLConnection.HTTP_FORBIDDEN,
            fakeErrorResponseBody
        )

        val expectedResource = Resource.error<TestResponse>(AUTH_ERROR_CODE)

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        val parseResponse = response.parse()

        Assert.assertEquals(parseResponse, expectedResource)

    }

    @Test
    fun test_parse_any_other_error()  {

        val response = Response.error<TestResponse>(
            HttpURLConnection.HTTP_GONE,
            fakeErrorResponseBody
        )

        val expectedResource = Resource.error<TestResponse>(UNHANDLE_ERROR_CODE)

        mockkStatic("miguel.project.data.extensions.RetrofitExtensions")

        val parseResponse = response.parse()

        Assert.assertEquals(parseResponse, expectedResource)

    }

}