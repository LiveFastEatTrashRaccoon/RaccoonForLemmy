package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di

import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultLocalItemCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LocalItemCache
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
internal class CacheModule {
    @Single
    @Named("postCache")
    fun providePostCache(): LocalItemCache<PostModel> = DefaultLocalItemCache()

    @Single
    @Named("commentCache")
    fun provideCommentCache(): LocalItemCache<CommentModel> = DefaultLocalItemCache()

    @Single
    @Named("communityCache")
    fun provideCommunityCache(): LocalItemCache<CommunityModel> = DefaultLocalItemCache()

    @Single
    @Named("userCache")
    fun provideUserCache(): LocalItemCache<UserModel> = DefaultLocalItemCache()
}

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository")
internal class RepositoryModule

@Module(includes = [CacheModule::class, RepositoryModule::class])
class LemmyRepositoryModule
