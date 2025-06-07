package com.livefast.eattrash.raccoonforlemmy.domain.inbox.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.livefaast.eattrash.raccoonforlemmy.domain.inbox.R
import com.livefast.eattrash.raccoonforlemmy.core.di.RootDI
import com.livefast.eattrash.raccoonforlemmy.core.l10n.L10nManager
import com.livefast.eattrash.raccoonforlemmy.core.l10n.di.getStrings
import com.livefast.eattrash.raccoonforlemmy.domain.inbox.coordinator.InboxCoordinator
import org.kodein.di.instance
import java.util.Collections.max

internal class CheckNotificationWorker(private val context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {
    private val inboxCoordinator by RootDI.di.instance<InboxCoordinator>()
    private val l10nManager by RootDI.di.instance<L10nManager>()

    override suspend fun doWork(): Result {
        inboxCoordinator.updateUnreadCount()
        val unread = inboxCoordinator.totalUnread.value
        if (unread > 0) {
            sendNotification(unread)
        }
        return Result.success()
    }

    private suspend fun sendNotification(count: Int) {
        val lang = l10nManager.lang.value
        val messages = getStrings(lang)
        val title = messages.inboxNotificationTitle()
        val content = messages.inboxNotificationContent(count)
        val notification =
            Notification
                .Builder(context, NotificationConstants.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_notification)
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
