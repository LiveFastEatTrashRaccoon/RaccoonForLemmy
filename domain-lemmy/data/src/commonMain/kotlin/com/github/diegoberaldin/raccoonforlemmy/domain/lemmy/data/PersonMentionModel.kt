package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data

data class PersonMentionModel(
    val post: PostModel,
    val comment: CommentModel,
)
