package com.livefast.eattrash.raccoonforlemmy.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import chaintech.videoplayer.model.PlayerConfig
import chaintech.videoplayer.model.ScreenResize
import com.livefast.eattrash.raccoonforlemmy.core.resources.CoreResources
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import raccoonforlemmy.shared.generated.resources.Res
import raccoonforlemmy.shared.generated.resources.charissil_bold
import raccoonforlemmy.shared.generated.resources.charissil_italic
import raccoonforlemmy.shared.generated.resources.charissil_regular
import raccoonforlemmy.shared.generated.resources.ic_alt_1
import raccoonforlemmy.shared.generated.resources.ic_alt_2
import raccoonforlemmy.shared.generated.resources.ic_default
import raccoonforlemmy.shared.generated.resources.ic_github
import raccoonforlemmy.shared.generated.resources.ic_lemmy
import raccoonforlemmy.shared.generated.resources.notosans_bold
import raccoonforlemmy.shared.generated.resources.notosans_italic
import raccoonforlemmy.shared.generated.resources.notosans_medium
import raccoonforlemmy.shared.generated.resources.notosans_regular
import raccoonforlemmy.shared.generated.resources.poppins_bold
import raccoonforlemmy.shared.generated.resources.poppins_italic
import raccoonforlemmy.shared.generated.resources.poppins_medium
import raccoonforlemmy.shared.generated.resources.poppins_regular

internal class SharedResources : CoreResources {
    override val github: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_github)

    override val lemmy: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_lemmy)

    override val appIconDefault: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_default)

    override val appIconAlt1: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_alt_1)

    override val appIconAlt2: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_alt_2)

    override val notoSans: FontFamily
        @Composable
        get() =
            FontFamily(
                Font(Res.font.notosans_regular, FontWeight.Normal, FontStyle.Normal),
                Font(Res.font.notosans_bold, FontWeight.Bold, FontStyle.Normal),
                Font(Res.font.notosans_medium, FontWeight.Medium, FontStyle.Normal),
                Font(Res.font.notosans_italic, FontWeight.Normal, FontStyle.Italic),
            )

    override val poppins: FontFamily
        @Composable
        get() =
            FontFamily(
                Font(Res.font.poppins_regular, FontWeight.Normal, FontStyle.Normal),
                Font(Res.font.poppins_bold, FontWeight.Bold, FontStyle.Normal),
                Font(Res.font.poppins_medium, FontWeight.Medium, FontStyle.Normal),
                Font(Res.font.poppins_italic, FontWeight.Normal, FontStyle.Italic),
            )

    override val charisSil: FontFamily
        @Composable
        get() =
            FontFamily(
                Font(Res.font.charissil_regular, FontWeight.Normal, FontStyle.Normal),
                Font(Res.font.charissil_bold, FontWeight.Bold, FontStyle.Normal),
                Font(Res.font.charissil_italic, FontWeight.Normal, FontStyle.Italic),
            )

    override fun getPlayerConfig(contentScale: ContentScale): PlayerConfig =
        PlayerConfig(
            isFullScreenEnabled = false,
            videoFitMode =
                if (contentScale == ContentScale.Fit) {
                    ScreenResize.FIT
                } else {
                    ScreenResize.FILL
                },
        )
}
