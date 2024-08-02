package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.repository

import com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.models.AcknowledgementModel

interface AcknowledgementsRepository {
    suspend fun getAcknowledgements(): List<AcknowledgementModel>?
}
