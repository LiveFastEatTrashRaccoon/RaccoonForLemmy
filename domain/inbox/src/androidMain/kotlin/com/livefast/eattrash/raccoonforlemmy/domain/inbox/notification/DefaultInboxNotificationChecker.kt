package com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import org.koin.core.annotation.Single
import java.util.concurrent.TimeUnit

@Single
internal actual class DefaultInboxNotificationChecker(
    private val context: Context,
) : InboxNotificationChecker {
    actual override val isBackgroundCheckSupported = true
    private var intervalMinutes = 15L

    actual override fun setPeriod(minutes: Long) {
        intervalMinutes = minutes
    }

    actual override fun start() {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG)

        createNotificationChannel()

        val constraints =
            Constraints
                .Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

        // check immediately with an expedited one-time request
        OneTimeWorkRequestBuilder<CheckNotificationWorker>()
            .addTag(TAG)
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .setConstraints(constraints)
            .build()
            .also { req ->
                WorkManager.getInstance(context).enqueue(req)
            }

        PeriodicWorkRequestBuilder<CheckNotificationWorker>(
            repeatInterval = intervalMinutes,
            repeatIntervalTimeUnit = TimeUnit.MINUTES,
        ).addTag(TAG)
            .setConstraints(constraints)
            .setInitialDelay(
                5,
                TimeUnit.MINUTES,
            ).build()
            .also { req ->
                WorkManager.getInstance(context).enqueue(req)
            }
    }

    actual override fun stop() {
        WorkManager.getInstance(context).cancelAllWorkByTag(TAG)
    }

    private fun createNotificationChannel() {
        val descriptionText = ""
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel =
            NotificationChannel(
                NotificationConstants.CHANNEL_ID,
                NotificationConstants.CHANNEL_NAME,
                importance,
            ).apply {
                description = descriptionText
            }
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        private const val TAG = "InboxNotificationChecker"
    }
}
