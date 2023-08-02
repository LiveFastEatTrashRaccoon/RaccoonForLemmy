package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.di

import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostsRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val postsRepositoryModule = module {
    singleOf(::PostsRepository)
    singleOf(::CommunityRepository)
    singleOf(::UserRepository)
    singleOf(::SiteRepository)
    singleOf(::CommentRepository)
}
