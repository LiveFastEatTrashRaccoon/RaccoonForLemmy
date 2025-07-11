package com.livefast.eattrash.raccoonforlemmy.core.api.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListPostReportsResponse(@SerialName("post_reports") val postReports: List<PostReportView>)
