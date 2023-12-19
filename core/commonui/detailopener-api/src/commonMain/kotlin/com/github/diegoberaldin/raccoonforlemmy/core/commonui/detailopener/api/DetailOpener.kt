package com.github.diegoberaldin.raccoonforlemmy.core.commonui.detailopener.api

import androidx.compose.runtime.Stable
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.CommunityModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel

@Stable
interface DetailOpener {
    fun openCommunityDetail(
        community: CommunityModel,
        otherInstance: String = "",
    )

    fun openUserDetail(
        user: UserModel,
        otherInstance: String = "",
    )

    fun openPostDetail(
        post: PostModel,
        otherInstance: String = "",
        highlightCommentId: Int? = null,
        isMod: Boolean = false,
    )
}
