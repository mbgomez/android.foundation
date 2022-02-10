package miguel.project.data.mapper

import miguel.project.data.datasource.local.PostTable
import miguel.project.domain.model.Post
import org.junit.Assert
import org.junit.Test

class DataMapperTest {


    @Test
    fun check_PostMapper(){

        val userId = 1L
        val id = 2L
        val title = "title"
        val body = "body"

        val postTable = PostTable(userId, id, title, body)
        val expectResult = Post(title, body)

        val postMapper = PostMapper()

        val result = postMapper.fromDataToDomain(postTable)

        Assert.assertEquals(expectResult, result)

    }

}