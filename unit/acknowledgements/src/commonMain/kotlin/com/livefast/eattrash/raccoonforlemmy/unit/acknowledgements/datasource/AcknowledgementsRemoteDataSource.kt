package com.livefast.eattrash.raccoonforlemmy.unit.acknowledgements.datasource

interface AcknowledgementsRemoteDataSource {
    suspend fun getAcknowledgements(): List<Acknowledgement>?
}
