package miguel.project.data.mapper

import miguel.project.data.datasource.local.PostTable
import miguel.project.domain.model.MapperData
import miguel.project.domain.model.Post

class PostMapper : MapperData<PostTable, Post> {
    override fun fromDataToDomain(table: PostTable): Post = table.run { Post(title, body) }

}