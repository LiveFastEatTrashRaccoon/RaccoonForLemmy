package com.livefast.eattrash.raccoonforlemmy.core.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import chaintech.videoplayer.model.VideoPlayerConfig

interface CoreResources {
    // region Platform logos
    val github: Painter @Composable get
    val lemmy: Painter @Composable get
    // endregion

    // region App icons
    val appIconDefault: Painter @Composable get
    val appIconClassical: Painter @Composable get
    // endregion

    // region Fonts
    val notoSans: FontFamily @Composable get
    val poppins: FontFamily @Composable get
    val atkinsonHyperlegible: FontFamily @Composable get
    // endregion

    // region Media player config
    val videoPlayerConfig: VideoPlayerConfig
    // endregion

    // region Material Symbols
    val abc: ImageVector @Composable get
    val accountCircleFill: ImageVector @Composable get
    val add: ImageVector @Composable get
    val addCircle: ImageVector @Composable get
    val adminPanelSettings: ImageVector @Composable get
    val allInclusive: ImageVector @Composable get
    val alternateEmail: ImageVector @Composable get
    val api: ImageVector @Composable get
    val arrowBack: ImageVector @Composable get
    val arrowCircleDown: ImageVector @Composable get
    val arrowCircleUp: ImageVector @Composable get
    val arrowDropDown: ImageVector @Composable get
    val article: ImageVector @Composable get
    val aspectRatio: ImageVector @Composable get
    val awardStar: ImageVector @Composable get
    val book: ImageVector @Composable get
    val bookmark: ImageVector @Composable get
    val bookmarksFill: ImageVector @Composable get
    val bookmarkFill: ImageVector @Composable get
    val bugReport: ImageVector @Composable get
    val cake: ImageVector @Composable get
    val calendarMonthFill: ImageVector @Composable get
    val calendarViewDayFill: ImageVector @Composable get
    val camera: ImageVector @Composable get
    val cancel: ImageVector @Composable get
    val changeCircle: ImageVector @Composable get
    val chatFill: ImageVector @Composable get
    val check: ImageVector @Composable get
    val checkCircle: ImageVector @Composable get
    val chevronForward: ImageVector @Composable get
    val circle: ImageVector @Composable get
    val code: ImageVector @Composable get
    val clearAll: ImageVector @Composable get
    val close: ImageVector @Composable get
    val comment: ImageVector @Composable get
    val computer: ImageVector @Composable get
    val cottage: ImageVector @Composable get
    val darkMode: ImageVector @Composable get
    val darkModeFill: ImageVector @Composable get
    val displaySettings: ImageVector @Composable get
    val doneAll: ImageVector @Composable get
    val doNotDisturbOn: ImageVector @Composable get
    val download: ImageVector @Composable get
    val edit: ImageVector @Composable get
    val elderlyWoman: ImageVector @Composable get
    val explicitFill: ImageVector @Composable get
    val exploreFill: ImageVector @Composable get
    val favorite: ImageVector @Composable get
    val fileOpen: ImageVector @Composable get
    val formatBold: ImageVector @Composable get
    val formatItalic: ImageVector @Composable get
    val formatListBulleted: ImageVector @Composable get
    val formatListNumbered: ImageVector @Composable get
    val formatQuote: ImageVector @Composable get
    val gavel: ImageVector @Composable get
    val group: ImageVector @Composable get
    val groups: ImageVector @Composable get
    val homeFill: ImageVector @Composable get
    val image: ImageVector @Composable get
    val inboxFill: ImageVector @Composable get
    val info: ImageVector @Composable get
    val keyboardArrowDown: ImageVector @Composable get
    val keyboardArrowLeft: ImageVector @Composable get
    val keyboardArrowRight: ImageVector @Composable get
    val keyboardArrowUp: ImageVector @Composable get
    val keyboardDoubleArrowLeft: ImageVector @Composable get
    val keyboardDoubleArrowRight: ImageVector @Composable get
    val lightMode: ImageVector @Composable get
    val link: ImageVector @Composable get
    val listAlt: ImageVector @Composable get
    val localFireDepartment: ImageVector @Composable get
    val localPolice: ImageVector @Composable get
    val lock: ImageVector @Composable get
    val logout: ImageVector @Composable get
    val markChatRead: ImageVector @Composable get
    val markChatUnread: ImageVector @Composable get
    val menu: ImageVector @Composable get
    val menuOpen: ImageVector @Composable get
    val moreHoriz: ImageVector @Composable get
    val moreVert: ImageVector @Composable get
    val notifications: ImageVector @Composable get
    val openInBrowser: ImageVector @Composable get
    val palette: ImageVector @Composable get
    val pending: ImageVector @Composable get
    val percent: ImageVector @Composable get
    val person: ImageVector @Composable get
    val playCircle: ImageVector @Composable get
    val preview: ImageVector @Composable get
    val public: ImageVector @Composable get
    val reply: ImageVector @Composable get
    val report: ImageVector @Composable get
    val reportOff: ImageVector @Composable get
    val rocketLaunch: ImageVector @Composable get
    val save: ImageVector @Composable get
    val scale: ImageVector @Composable get
    val science: ImageVector @Composable get
    val schedule: ImageVector @Composable get
    val share: ImageVector @Composable get
    val search: ImageVector @Composable get
    val send: ImageVector @Composable get
    val settingsApplications: ImageVector @Composable get
    val settingsFill: ImageVector @Composable get
    val shield: ImageVector @Composable get
    val star: ImageVector @Composable get
    val starFill: ImageVector @Composable get
    val strikethroughS: ImageVector @Composable get
    val styleFill: ImageVector @Composable get
    val stylusFountainPenFill: ImageVector @Composable get
    val sync: ImageVector @Composable get
    val syncDisabled: ImageVector @Composable get
    val support: ImageVector @Composable get
    val tag: ImageVector @Composable get
    val thumbsUpDownFill: ImageVector @Composable get
    val thunderstorm: ImageVector @Composable get
    val trendingUp: ImageVector @Composable get
    val unfoldLess: ImageVector @Composable get
    val unfoldMore: ImageVector @Composable get
    val update: ImageVector @Composable get
    val viewWeekFill: ImageVector @Composable get
    val verified: ImageVector @Composable get
    val visibility: ImageVector @Composable get
    val visibilityOff: ImageVector @Composable get
    val volunteerActivism: ImageVector @Composable get
    val workspacePremium: ImageVector @Composable get
    // endregion
}
