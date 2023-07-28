package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.di

import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.PostsRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val postsRepositoryModule = module {
    singleOf(::PostsRepository)
    singleOf(::CommunityRepository)
}