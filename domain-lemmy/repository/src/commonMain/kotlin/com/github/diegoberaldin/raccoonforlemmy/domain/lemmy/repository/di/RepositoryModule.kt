package com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.di

import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommentRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.CommunityRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PostRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.PrivateMessageRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.SiteRepository
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.repository.UserRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {
    single {
        PostRepository(
            services = get(named("default")),
            customServices = get(named("custom")),
        )
    }
    single {
        CommunityRepository(
            services = get(named("default")),
            customServices = get(named("custom")),
        )
    }
    single {
        UserRepository(
            services = get(named("default")),
            customServices = get(named("custom")),
        )
    }
    single {
        SiteRepository(
            services = get(named("default")),
        )
    }
    single {
        CommentRepository(
            services = get(named("default")),
            customServices = get(named("custom")),
        )
    }
    single {
        PrivateMessageRepository(
            services = get(named("default")),
        )
    }
}
