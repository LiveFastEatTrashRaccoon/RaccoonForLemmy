package com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.di

import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.PostsRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.SiteRepository
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.repository.UserRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val postsRepositoryModule = module {
    singleOf(::PostsRepository)
    singleOf(::CommunityRepository)
    singleOf(::UserRepository)
    singleOf(::SiteRepository)
}
