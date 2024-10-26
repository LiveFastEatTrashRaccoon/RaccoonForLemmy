package com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.livefast.eattrash.raccoonforlemmy.core.l10n.L10nManager
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.InboxCoordinator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.inject
import java.util.Collections.max
import com.livefast.eattrash.raccoonforlemmy.core.resources.R as resourcesR

internal class CheckNotificationWorker(
    private val context: Context,
    parameters: WorkerParameters,
) : CoroutineWorker(context, parameters) {
    private val inboxCoordinator by inject<InboxCoordinator>(InboxCoordinator::class.java)
    private val l10nManager by inject<L10nManager>(L10nManager::class.java)

    override suspend fun doWork() =
        withContext(Dispatchers.IO) {
            inboxCoordinator.updateUnreadCount()
            val unread = inboxCoordinator.totalUnread.value
            if (unread > 0) {
                sendNotification(unread)
            }
            Result.success()
        }

    @SuppressLint("StringFormatInvalid")
    private fun sendNotification(count: Int) {
        val title = l10nManager.currentValues.inboxNotificationTitle
        val content = l10nManager.currentValues.inboxNotificationContent(count)
        val notification =
            Notification
                .Builder(context, NotificationConstants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(resourcesR.drawable.ic_monochrome)
                .setContentIntent(getPendingIntent())
                .setNumber(count)
                .build()
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationId = getNextNotificationId()
        notificationManager.notify(
            NotificationConstants.NOTIFICATION_TAG,
            notificationId,
            notification,
        )
    }

    private fun getPendingIntent(): PendingIntent {
        val intent =
            Intent(
                context,
                Class.forName(MAIN_ACTIVITY_NAME),
            ).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        return pendingIntent
    }

    private fun getNextNotificationId(): Int {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val activeNotifications =
            notificationManager.activeNotifications.filter { it.tag == context.packageName }
        return if (activeNotifications.isEmpty()) {
            0
        } else {
            max(activeNotifications.map { it.id }) + 1
        }
    }

    companion object {
        private const val MAIN_ACTIVITY_NAME =
            "com.livefast.eattrash.raccoonforlemmy.android.MainActivity"
    }
}
