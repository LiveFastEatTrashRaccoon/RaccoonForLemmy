package com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.di

import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.PostsRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.SiteRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_post.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val postsRepositoryModule = module {
    singleOf(::PostsRepository)
    singleOf(::CommunityRepository)
    singleOf(::UserRepository)
    singleOf(::SiteRepository)
}