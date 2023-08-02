package com.github.diegoberaldin.raccoonforlemmy.feature.profile.content.logged

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.DateTime
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.UserModel
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@Composable
internal fun ProfileLoggedCounters(
    user: UserModel,
) {
    Row(
        modifier = Modifier.padding(horizontal = Spacing.m).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.s),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = (user.score?.postScore ?: 0).toString(),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(MR.strings.profile_post_score),
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Box(
            modifier = Modifier.width(1.dp).height(30.dp)
                .background(color = MaterialTheme.colorScheme.onSurface)
                .padding(vertical = Spacing.s),
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = (user.score?.commentScore ?: 0).toString(),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(MR.strings.profile_comment_score),
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Box(
            modifier = Modifier.width(1.dp).height(30.dp)
                .background(color = MaterialTheme.colorScheme.onSurface)
                .padding(vertical = Spacing.s),
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Spacing.xs),
        ) {
            Text(
                text = DateTime.getPrettyDate(
                    iso8601Timestamp = user.accountAge + "Z",
                    yearLabel = stringResource(MR.strings.profile_year_short),
                    monthLabel = stringResource(MR.strings.profile_month_short),
                    dayLabel = stringResource(MR.strings.profile_day_short),
                ),
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = stringResource(MR.strings.profile_account_age),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}

@Composable
internal fun ProfileLoggedHeader(
    user: UserModel,
) {
    val avatar = user.avatar.orEmpty()
    if (avatar.isNotEmpty()) {
        val painterResource = asyncPainterResource(data = avatar)
        val profileImageSize = 100.dp
        KamelImage(
            modifier = Modifier.size(profileImageSize)
                .clip(RoundedCornerShape(profileImageSize / 2)),
            resource = painterResource,
            contentDescription = null,
            onLoading = {
                CircularProgressIndicator()
            },
        )
    }
    Text(
        text = user.name,
        style = MaterialTheme.typography.headlineSmall,
    )
    Text(
        text = user.host,
        style = MaterialTheme.typography.titleMedium,
    )
}
