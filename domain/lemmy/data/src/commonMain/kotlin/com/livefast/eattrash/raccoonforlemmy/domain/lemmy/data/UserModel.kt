package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.data

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import kotlin.jvm.Transient

data class UserModel(
    val id: Long = 0,
    val instanceId: Long = 0,
    val name: String = "",
    val displayName: String = "",
    val avatar: String? = null,
    val bio: String? = null,
    val matrixUserId: String? = null,
    val banner: String? = null,
    val host: String = "",
    val score: UserScoreModel? = null,
    val accountAge: String = "",
    val banned: Boolean = false,
    val updateDate: String? = null,
    val bot: Boolean = false,
    @Transient val tags: List<UserTagModel> = emptyList(),
)

fun List<UserModel>.containsId(value: Long?): Boolean = any { it.id == value }

fun UserModel.readableName(preferNickname: Boolean): String = if (preferNickname) {
    displayName.takeIf { it.isNotEmpty() } ?: readableHandle
} else {
    readableHandle
}

val UserModel.readableHandle: String
    get() =
        buildString {
            append(name)
            if (host.isNotEmpty()) {
                append("@$host")
            }
        }

@Composable
fun UserModel.populateSpecialTags(
    isAdmin: Boolean,
    isBot: Boolean,
    isMe: Boolean,
    isMod: Boolean,
    isOp: Boolean,
    adminColor: Int? = null,
    botColor: Int? = null,
    meColor: Int? = null,
    opColor: Int? = null,
    modColor: Int? = null,
): UserModel = this.let { user ->
    user.copy(
        tags =
        buildList {
            // first add all special tags
            if (isAdmin) {
                add(
                    UserTagModel(
                        type = UserTagType.Admin,
                        name = LocalStrings.current.defaultTagAdmin,
                        color = adminColor,
                    ),
                )
            }
            if (isBot) {
                add(
                    UserTagModel(
                        type = UserTagType.Bot,
                        name = LocalStrings.current.defaultTagBot,
                        color = botColor,
                    ),
                )
            }
            if (isMe) {
                add(
                    UserTagModel(
                        type = UserTagType.Me,
                        name = LocalStrings.current.defaultTagCurrentUser,
                        color = meColor,
                    ),
                )
            }
            if (isMod) {
                add(
                    UserTagModel(
                        type = UserTagType.Moderator,
                        name = LocalStrings.current.defaultTagModerator,
                        color = modColor,
                    ),
                )
            }
            if (isOp) {
                add(
                    UserTagModel(
                        type = UserTagType.OriginalPoster,
                        name = LocalStrings.current.defaultTagOriginalPoster,
                        color = opColor,
                    ),
                )
            }

            // then add all the regular tags
            addAll(tags)
        },
    )
}
