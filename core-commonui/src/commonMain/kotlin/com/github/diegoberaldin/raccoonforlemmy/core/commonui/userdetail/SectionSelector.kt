package com.github.diegoberaldin.raccoonforlemmy.core.commonui.userdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.diegoberaldin.racconforlemmy.core.utils.onClick
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.CornerSize
import com.github.diegoberaldin.raccoonforlemmy.core.appearance.theme.Spacing
import com.github.diegoberaldin.raccoonforlemmy.resources.MR
import dev.icerock.moko.resources.compose.stringResource

@Composable
internal fun SectionSelector(
    modifier: Modifier = Modifier,
    currentSection: UserDetailSection,
    onSectionSelected: (UserDetailSection) -> Unit,
) {
    val highlightColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
    Row(
        modifier = modifier
            .height(34.dp)
            .padding(horizontal = Spacing.m)
            .fillMaxWidth()
            .border(
                color = highlightColor,
                width = 1.dp,
                shape = RoundedCornerShape(CornerSize.m),
            ),
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(bottom = Spacing.xxs)
                .onClick {
                    onSectionSelected(UserDetailSection.POSTS)
                }
                .let {
                    if (currentSection == UserDetailSection.POSTS) {
                        it.background(
                            color = highlightColor,
                            shape = RoundedCornerShape(
                                topStart = CornerSize.m,
                                bottomStart = CornerSize.m,
                            ),
                        )
                    } else {
                        it
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(MR.strings.profile_section_posts),
                style = MaterialTheme.typography.titleSmall,
            )
        }
        Box(
            modifier = Modifier.width(1.dp).fillMaxHeight()
                .background(color = highlightColor),
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(bottom = Spacing.xxs)
                .onClick {
                    onSectionSelected(UserDetailSection.COMMENTS)
                }
                .let {
                    if (currentSection == UserDetailSection.COMMENTS) {
                        it.background(
                            color = highlightColor,
                            shape = RoundedCornerShape(
                                topEnd = CornerSize.m,
                                bottomEnd = CornerSize.m,
                            ),
                        )
                    } else {
                        it
                    }
                },
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(MR.strings.profile_section_comments),
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}
