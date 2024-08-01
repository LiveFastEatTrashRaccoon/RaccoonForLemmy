package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

sealed interface SearchResult {
    data class Post(val model: PostModel) : SearchResult

    data class Comment(val model: CommentModel) : SearchResult

    data class User(val model: UserModel) : SearchResult

    data class Community(val model: CommunityModel) : SearchResult
}

val SearchResult.uniqueIdentifier: String
    get() =
        when (this) {
            is SearchResult.Post -> "post" + model.id.toString() + model.updateDate
            is SearchResult.Comment -> "comment" + model.id.toString() + model.updateDate
            is SearchResult.User -> "user" + model.id.toString()
            is SearchResult.Community -> "community" + model.id.toString()
        }
