package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository

import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val postsRepositoryModule = module {
    singleOf(::ApiConfigurationRepository)
    singleOf(::PostsRepository)
    singleOf(::CommunityRepository)
}