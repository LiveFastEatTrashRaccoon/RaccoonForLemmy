package com.livefast.eattrash.raccoonforlemmy.core.api.di

import com.livefast.eattrash.raccoonforlemmy.core.api.provider.DefaultServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.provider.ServiceProvider
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.AuthServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.CommentServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.CommunityServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultAuthServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultCommentServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultCommunityServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultModlogServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultPostServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultPrivateMessageServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultSearchServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultSiteServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.DefaultUserServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.ModlogServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.PostServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.PrivateMessageServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SearchServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.SiteServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v3.UserServiceV3
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.AccountServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.DefaultAccountServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.DefaultSiteServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.api.service.v4.SiteServiceV4
import com.livefast.eattrash.raccoonforlemmy.core.utils.network.provideHttpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.factory
import org.kodein.di.instance
import org.kodein.di.singleton

internal data class ServiceCreationArgs(val baseUrl: String, val client: HttpClient)

val apiModule =
    DI.Module("ApiModule") {
        bind<HttpClientEngine> {
            singleton {
                provideHttpClientEngine()
            }
        }
        bind<AuthServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultAuthServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<CommentServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultCommentServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<CommunityServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultCommunityServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<ModlogServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultModlogServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<PostServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultPostServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<PrivateMessageServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultPrivateMessageServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<SearchServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultSearchServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<SiteServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultSiteServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<UserServiceV3> {
            factory { arg: ServiceCreationArgs ->
                DefaultUserServiceV3(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<AccountServiceV4> {
            factory { arg: ServiceCreationArgs ->
                DefaultAccountServiceV4(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<SiteServiceV4> {
            factory { arg: ServiceCreationArgs ->
                DefaultSiteServiceV4(baseUrl = arg.baseUrl, client = arg.client)
            }
        }
        bind<ServiceProvider>(tag = "default") {
            singleton {
                DefaultServiceProvider(appInfoRepository = instance())
            }
        }
        bind<ServiceProvider>(tag = "custom") {
            singleton {
                DefaultServiceProvider(appInfoRepository = instance())
            }
        }
    }
