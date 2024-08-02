package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.di

import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.AcknowledgementsMviModel
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.AcknowledgementsViewModel
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.datasource.AcknowledgementsRemoteDataSource
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.datasource.DefaultAcknowledgementsRemoteDataSource
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.repository.AcknowledgementsRepository
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.repository.DefaultAcknowledgementsRepository
import org.koin.dsl.module

val acknowledgementsModule =
    module {
        single<AcknowledgementsRemoteDataSource> {
            DefaultAcknowledgementsRemoteDataSource()
        }
        single<AcknowledgementsRepository> {
            DefaultAcknowledgementsRepository(
                dataSource = get(),
            )
        }
        factory<AcknowledgementsMviModel> {
            AcknowledgementsViewModel(
                acknowledgementsRepository = get(),
            )
        }
    }
