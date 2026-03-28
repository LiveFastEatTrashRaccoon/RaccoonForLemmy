package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import android.util.Log

abstract class BasePostCategorizer : PostCategorizer {
    override suspend fun categorize(postHeadline: String): String? {
        val startTime = System.currentTimeMillis()
        val result = performCategorization(postHeadline)
        val duration = System.currentTimeMillis() - startTime
        Log.i("PostCategorizer", "Categorization took ${duration}ms for: $postHeadline")
        return result
    }

    protected abstract suspend fun performCategorization(postHeadline: String): String?
}
