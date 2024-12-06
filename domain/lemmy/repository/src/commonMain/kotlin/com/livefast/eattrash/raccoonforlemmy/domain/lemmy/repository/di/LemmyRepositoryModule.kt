package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di

import com.livefast.eattrash.raccoonforlemmy.core.utils.cache.LruCache
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommentModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.PostModel
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data.UserModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

private const val CACHE_SIZE = 5

@Module
internal class CacheModule {
    @Single
    fun providePostCache(): LruCache<PostModel> = LruCache.factory(CACHE_SIZE)

    @Single
    fun provideCommentCache(): LruCache<CommentModel> = LruCache.factory(CACHE_SIZE)

    @Single
    fun provideCommunityCache(): LruCache<CommunityModel> = LruCache.factory(CACHE_SIZE)

    @Single
    fun provideUserCache(): LruCache<UserModel> = LruCache.factory(CACHE_SIZE)
}

@Module
@ComponentScan("com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository")
internal class RepositoryModule

@Module(includes = [CacheModule::class, RepositoryModule::class])
class LemmyRepositoryModule
