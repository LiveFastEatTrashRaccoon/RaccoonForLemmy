package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.repository

import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.datasource.Acknowledgement
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.datasource.AcknowledgementsRemoteDataSource
import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.models.AcknowledgementModel

internal class DefaultAcknowledgementsRepository(
    private val dataSource: AcknowledgementsRemoteDataSource,
) : AcknowledgementsRepository {
    override suspend fun getAcknowledgements(): List<AcknowledgementModel>? =
        dataSource.getAcknowledgements()?.map { it.toModel() }
}

private fun Acknowledgement.toModel() =
    AcknowledgementModel(
        title = title,
        url = url,
        avatar = avatar,
        subtitle = subtitle,
    )
