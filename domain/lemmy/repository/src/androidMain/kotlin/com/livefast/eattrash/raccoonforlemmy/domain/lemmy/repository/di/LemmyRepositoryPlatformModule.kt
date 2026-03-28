package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.di

import com.arm.aichat.AiChat
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.LlamaCppCategorizer
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.DefaultPostRepository
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.MediaPipePostCategorizer
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.ModelDownloader
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostCategorizer
import com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository.PostRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

actual val lemmyRepositoryPlatformModule: DI.Module = DI.Module("LemmyRepositoryPlatformModule") {
    bind<HttpClient>(tag = "modelDownloader") {
        singleton {
            HttpClient(CIO) {
                engine {
                    requestTimeout = 0
                }
            }
        }
    }

    bind<ModelDownloader> {
        singleton { ModelDownloader(instance(), instance(tag = "modelDownloader")) }
    }

    bind<PostCategorizer> {
        singleton {
//            LlamaCppCategorizer(
//                modelDownloader = instance(),
//                engine = AiChat.getInferenceEngine(instance())
//            )
            MediaPipePostCategorizer(instance())
        }
    }

    bind<PostRepository> {
        singleton {
            DefaultPostRepository(
                services = instance(tag = "default"),
                customServices = instance(tag = "custom"),
                postCategorizer = instance(),
            )
        }
    }
}
