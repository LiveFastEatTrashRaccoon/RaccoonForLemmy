package com.livefast.eattrash.raccoonforlemmy.resources

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import chaintech.videoplayer.model.VideoPlayerConfig
import com.livefast.eattrash.raccoonforlemmy.core.resources.CoreResources
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import raccoonforlemmy.shared.generated.resources.Res
import raccoonforlemmy.shared.generated.resources.abc
import raccoonforlemmy.shared.generated.resources.account_circle_fill
import raccoonforlemmy.shared.generated.resources.add
import raccoonforlemmy.shared.generated.resources.add_circle
import raccoonforlemmy.shared.generated.resources.admin_panel_settings
import raccoonforlemmy.shared.generated.resources.all_inclusive
import raccoonforlemmy.shared.generated.resources.alternate_email
import raccoonforlemmy.shared.generated.resources.api
import raccoonforlemmy.shared.generated.resources.arrow_back
import raccoonforlemmy.shared.generated.resources.arrow_circle_down
import raccoonforlemmy.shared.generated.resources.arrow_circle_up
import raccoonforlemmy.shared.generated.resources.arrow_drop_down
import raccoonforlemmy.shared.generated.resources.article
import raccoonforlemmy.shared.generated.resources.aspect_ratio
import raccoonforlemmy.shared.generated.resources.atkinsonhyperlegible_bold
import raccoonforlemmy.shared.generated.resources.atkinsonhyperlegible_italic
import raccoonforlemmy.shared.generated.resources.atkinsonhyperlegible_regular
import raccoonforlemmy.shared.generated.resources.award_star
import raccoonforlemmy.shared.generated.resources.book
import raccoonforlemmy.shared.generated.resources.bookmark
import raccoonforlemmy.shared.generated.resources.bookmark_fill
import raccoonforlemmy.shared.generated.resources.bookmarks_fill
import raccoonforlemmy.shared.generated.resources.bug_report
import raccoonforlemmy.shared.generated.resources.cake
import raccoonforlemmy.shared.generated.resources.calendar_month_fill
import raccoonforlemmy.shared.generated.resources.calendar_view_day_fill
import raccoonforlemmy.shared.generated.resources.camera
import raccoonforlemmy.shared.generated.resources.cancel
import raccoonforlemmy.shared.generated.resources.change_circle
import raccoonforlemmy.shared.generated.resources.chatFill
import raccoonforlemmy.shared.generated.resources.check
import raccoonforlemmy.shared.generated.resources.check_circle
import raccoonforlemmy.shared.generated.resources.chevron_forward
import raccoonforlemmy.shared.generated.resources.circle
import raccoonforlemmy.shared.generated.resources.clear_all
import raccoonforlemmy.shared.generated.resources.close
import raccoonforlemmy.shared.generated.resources.code
import raccoonforlemmy.shared.generated.resources.comment
import raccoonforlemmy.shared.generated.resources.computer
import raccoonforlemmy.shared.generated.resources.cottage
import raccoonforlemmy.shared.generated.resources.dark_mode
import raccoonforlemmy.shared.generated.resources.dark_mode_fill
import raccoonforlemmy.shared.generated.resources.display_settings
import raccoonforlemmy.shared.generated.resources.do_not_disturb_on
import raccoonforlemmy.shared.generated.resources.done_all
import raccoonforlemmy.shared.generated.resources.download
import raccoonforlemmy.shared.generated.resources.edit
import raccoonforlemmy.shared.generated.resources.elderly_woman
import raccoonforlemmy.shared.generated.resources.explicit_fill
import raccoonforlemmy.shared.generated.resources.explore_fill
import raccoonforlemmy.shared.generated.resources.favorite
import raccoonforlemmy.shared.generated.resources.file_open
import raccoonforlemmy.shared.generated.resources.format_bold
import raccoonforlemmy.shared.generated.resources.format_italic
import raccoonforlemmy.shared.generated.resources.format_list_bulleted
import raccoonforlemmy.shared.generated.resources.format_list_numbered
import raccoonforlemmy.shared.generated.resources.format_quote
import raccoonforlemmy.shared.generated.resources.gavel
import raccoonforlemmy.shared.generated.resources.group
import raccoonforlemmy.shared.generated.resources.groups
import raccoonforlemmy.shared.generated.resources.home_fill
import raccoonforlemmy.shared.generated.resources.ic_classical
import raccoonforlemmy.shared.generated.resources.ic_default
import raccoonforlemmy.shared.generated.resources.ic_github
import raccoonforlemmy.shared.generated.resources.ic_lemmy
import raccoonforlemmy.shared.generated.resources.image
import raccoonforlemmy.shared.generated.resources.inbox_fill
import raccoonforlemmy.shared.generated.resources.info
import raccoonforlemmy.shared.generated.resources.keyboard_arrow_down
import raccoonforlemmy.shared.generated.resources.keyboard_arrow_left
import raccoonforlemmy.shared.generated.resources.keyboard_arrow_right
import raccoonforlemmy.shared.generated.resources.keyboard_arrow_up
import raccoonforlemmy.shared.generated.resources.keyboard_double_arrow_left
import raccoonforlemmy.shared.generated.resources.keyboard_double_arrow_right
import raccoonforlemmy.shared.generated.resources.light_mode
import raccoonforlemmy.shared.generated.resources.link
import raccoonforlemmy.shared.generated.resources.list_alt
import raccoonforlemmy.shared.generated.resources.local_fire_department
import raccoonforlemmy.shared.generated.resources.local_police
import raccoonforlemmy.shared.generated.resources.lock
import raccoonforlemmy.shared.generated.resources.logout
import raccoonforlemmy.shared.generated.resources.mark_chat_read
import raccoonforlemmy.shared.generated.resources.mark_chat_unread
import raccoonforlemmy.shared.generated.resources.menu
import raccoonforlemmy.shared.generated.resources.menu_open
import raccoonforlemmy.shared.generated.resources.more_horiz
import raccoonforlemmy.shared.generated.resources.more_vert
import raccoonforlemmy.shared.generated.resources.notifications
import raccoonforlemmy.shared.generated.resources.notosans_bold
import raccoonforlemmy.shared.generated.resources.notosans_italic
import raccoonforlemmy.shared.generated.resources.notosans_medium
import raccoonforlemmy.shared.generated.resources.notosans_regular
import raccoonforlemmy.shared.generated.resources.open_in_browser
import raccoonforlemmy.shared.generated.resources.palette
import raccoonforlemmy.shared.generated.resources.pending
import raccoonforlemmy.shared.generated.resources.percent
import raccoonforlemmy.shared.generated.resources.person
import raccoonforlemmy.shared.generated.resources.play_circle
import raccoonforlemmy.shared.generated.resources.poppins_bold
import raccoonforlemmy.shared.generated.resources.poppins_italic
import raccoonforlemmy.shared.generated.resources.poppins_medium
import raccoonforlemmy.shared.generated.resources.poppins_regular
import raccoonforlemmy.shared.generated.resources.preview
import raccoonforlemmy.shared.generated.resources.public
import raccoonforlemmy.shared.generated.resources.reply
import raccoonforlemmy.shared.generated.resources.report
import raccoonforlemmy.shared.generated.resources.report_off
import raccoonforlemmy.shared.generated.resources.rocket_launch
import raccoonforlemmy.shared.generated.resources.save
import raccoonforlemmy.shared.generated.resources.scale
import raccoonforlemmy.shared.generated.resources.schedule
import raccoonforlemmy.shared.generated.resources.science
import raccoonforlemmy.shared.generated.resources.search
import raccoonforlemmy.shared.generated.resources.send
import raccoonforlemmy.shared.generated.resources.settings_applications
import raccoonforlemmy.shared.generated.resources.settings_fill
import raccoonforlemmy.shared.generated.resources.share
import raccoonforlemmy.shared.generated.resources.shield
import raccoonforlemmy.shared.generated.resources.star
import raccoonforlemmy.shared.generated.resources.star_fill
import raccoonforlemmy.shared.generated.resources.strikethrough_s
import raccoonforlemmy.shared.generated.resources.style_fill
import raccoonforlemmy.shared.generated.resources.stylus_fountain_pen_fill
import raccoonforlemmy.shared.generated.resources.support
import raccoonforlemmy.shared.generated.resources.sync
import raccoonforlemmy.shared.generated.resources.sync_disabled
import raccoonforlemmy.shared.generated.resources.tag
import raccoonforlemmy.shared.generated.resources.thumbs_up_down_fill
import raccoonforlemmy.shared.generated.resources.thunderstorm
import raccoonforlemmy.shared.generated.resources.trending_up
import raccoonforlemmy.shared.generated.resources.unfold_less
import raccoonforlemmy.shared.generated.resources.unfold_more
import raccoonforlemmy.shared.generated.resources.update
import raccoonforlemmy.shared.generated.resources.verified
import raccoonforlemmy.shared.generated.resources.view_week_fill
import raccoonforlemmy.shared.generated.resources.visibility
import raccoonforlemmy.shared.generated.resources.visibility_off
import raccoonforlemmy.shared.generated.resources.volunteer_activism
import raccoonforlemmy.shared.generated.resources.workspace_premium

internal class SharedResources : CoreResources {
    // region Platform logos
    override val github: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_github)

    override val lemmy: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_lemmy)
    // endregion

    // region App icons
    override val appIconDefault: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_default)

    override val appIconClassical: Painter
        @Composable
        get() = painterResource(Res.drawable.ic_classical)
    // endregion

    // region Fonts
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

    override val atkinsonHyperlegible: FontFamily
        @Composable
        get() =
            FontFamily(
                Font(Res.font.atkinsonhyperlegible_regular, FontWeight.Normal, FontStyle.Normal),
                Font(Res.font.atkinsonhyperlegible_bold, FontWeight.Bold, FontStyle.Normal),
                Font(Res.font.atkinsonhyperlegible_italic, FontWeight.Normal, FontStyle.Italic),
            )
    // endregion

    // region Media player config
    override val videoPlayerConfig: VideoPlayerConfig =
        VideoPlayerConfig(isFullScreenEnabled = false)
    // endregion

    // region Material Symbols
    override val abc: ImageVector
        @Composable get() = vectorResource(Res.drawable.abc)

    override val accountCircleFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.account_circle_fill)

    override val add: ImageVector
        @Composable get() = vectorResource(Res.drawable.add)

    override val addCircle: ImageVector
        @Composable get() = vectorResource(Res.drawable.add_circle)

    override val adminPanelSettings: ImageVector
        @Composable get() = vectorResource(Res.drawable.admin_panel_settings)

    override val allInclusive: ImageVector
        @Composable get() = vectorResource(Res.drawable.all_inclusive)

    override val alternateEmail: ImageVector
        @Composable get() = vectorResource(Res.drawable.alternate_email)

    override val api: ImageVector
        @Composable get() = vectorResource(Res.drawable.api)

    override val arrowBack: ImageVector
        @Composable get() = vectorResource(Res.drawable.arrow_back)

    override val arrowCircleDown: ImageVector
        @Composable get() = vectorResource(Res.drawable.arrow_circle_down)

    override val arrowCircleUp: ImageVector
        @Composable get() = vectorResource(Res.drawable.arrow_circle_up)

    override val arrowDropDown: ImageVector
        @Composable get() = vectorResource(Res.drawable.arrow_drop_down)

    override val article: ImageVector
        @Composable get() = vectorResource(Res.drawable.article)

    override val aspectRatio: ImageVector
        @Composable get() = vectorResource(Res.drawable.aspect_ratio)

    override val awardStar: ImageVector
        @Composable get() = vectorResource(Res.drawable.award_star)

    override val book: ImageVector
        @Composable get() = vectorResource(Res.drawable.book)

    override val bookmark: ImageVector
        @Composable get() = vectorResource(Res.drawable.bookmark)

    override val bookmarksFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.bookmarks_fill)

    override val bookmarkFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.bookmark_fill)

    override val bugReport: ImageVector
        @Composable get() = vectorResource(Res.drawable.bug_report)

    override val cake: ImageVector
        @Composable get() = vectorResource(Res.drawable.cake)

    override val calendarMonthFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.calendar_month_fill)

    override val calendarViewDayFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.calendar_view_day_fill)

    override val camera: ImageVector
        @Composable get() = vectorResource(Res.drawable.camera)

    override val cancel: ImageVector
        @Composable get() = vectorResource(Res.drawable.cancel)

    override val changeCircle: ImageVector
        @Composable get() = vectorResource(Res.drawable.change_circle)

    override val chatFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.chatFill)

    override val check: ImageVector
        @Composable get() = vectorResource(Res.drawable.check)

    override val checkCircle: ImageVector
        @Composable get() = vectorResource(Res.drawable.check_circle)

    override val chevronForward: ImageVector
        @Composable get() = vectorResource(Res.drawable.chevron_forward)

    override val circle: ImageVector
        @Composable get() = vectorResource(Res.drawable.circle)

    override val code: ImageVector
        @Composable get() = vectorResource(Res.drawable.code)

    override val clearAll: ImageVector
        @Composable get() = vectorResource(Res.drawable.clear_all)

    override val close: ImageVector
        @Composable get() = vectorResource(Res.drawable.close)

    override val comment: ImageVector
        @Composable get() = vectorResource(Res.drawable.comment)

    override val computer: ImageVector
        @Composable get() = vectorResource(Res.drawable.computer)

    override val cottage: ImageVector
        @Composable get() = vectorResource(Res.drawable.cottage)

    override val darkMode: ImageVector
        @Composable get() = vectorResource(Res.drawable.dark_mode)

    override val darkModeFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.dark_mode_fill)

    override val displaySettings: ImageVector
        @Composable get() = vectorResource(Res.drawable.display_settings)

    override val doneAll: ImageVector
        @Composable get() = vectorResource(Res.drawable.done_all)

    override val doNotDisturbOn: ImageVector
        @Composable get() = vectorResource(Res.drawable.do_not_disturb_on)

    override val download: ImageVector
        @Composable get() = vectorResource(Res.drawable.download)

    override val edit: ImageVector
        @Composable get() = vectorResource(Res.drawable.edit)

    override val elderlyWoman: ImageVector
        @Composable get() = vectorResource(Res.drawable.elderly_woman)

    override val explicitFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.explicit_fill)

    override val exploreFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.explore_fill)

    override val favorite: ImageVector
        @Composable get() = vectorResource(Res.drawable.favorite)

    override val fileOpen: ImageVector
        @Composable get() = vectorResource(Res.drawable.file_open)

    override val formatBold: ImageVector
        @Composable get() = vectorResource(Res.drawable.format_bold)

    override val formatItalic: ImageVector
        @Composable get() = vectorResource(Res.drawable.format_italic)

    override val formatListBulleted: ImageVector
        @Composable get() = vectorResource(Res.drawable.format_list_bulleted)

    override val formatListNumbered: ImageVector
        @Composable get() = vectorResource(Res.drawable.format_list_numbered)

    override val formatQuote: ImageVector
        @Composable get() = vectorResource(Res.drawable.format_quote)

    override val gavel: ImageVector
        @Composable get() = vectorResource(Res.drawable.gavel)

    override val group: ImageVector
        @Composable get() = vectorResource(Res.drawable.group)

    override val groups: ImageVector
        @Composable get() = vectorResource(Res.drawable.groups)

    override val homeFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.home_fill)

    override val image: ImageVector
        @Composable get() = vectorResource(Res.drawable.image)

    override val inboxFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.inbox_fill)

    override val info: ImageVector
        @Composable get() = vectorResource(Res.drawable.info)

    override val keyboardArrowDown: ImageVector
        @Composable get() = vectorResource(Res.drawable.keyboard_arrow_down)

    override val keyboardArrowLeft: ImageVector
        @Composable get() = vectorResource(Res.drawable.keyboard_arrow_left)

    override val keyboardArrowRight: ImageVector
        @Composable get() = vectorResource(Res.drawable.keyboard_arrow_right)

    override val keyboardArrowUp: ImageVector
        @Composable get() = vectorResource(Res.drawable.keyboard_arrow_up)

    override val keyboardDoubleArrowLeft: ImageVector
        @Composable get() = vectorResource(Res.drawable.keyboard_double_arrow_left)

    override val keyboardDoubleArrowRight: ImageVector
        @Composable get() = vectorResource(Res.drawable.keyboard_double_arrow_right)

    override val lightMode: ImageVector
        @Composable get() = vectorResource(Res.drawable.light_mode)

    override val link: ImageVector
        @Composable get() = vectorResource(Res.drawable.link)

    override val listAlt: ImageVector
        @Composable get() = vectorResource(Res.drawable.list_alt)

    override val localFireDepartment: ImageVector
        @Composable get() = vectorResource(Res.drawable.local_fire_department)

    override val localPolice: ImageVector
        @Composable get() = vectorResource(Res.drawable.local_police)

    override val lock: ImageVector
        @Composable get() = vectorResource(Res.drawable.lock)

    override val logout: ImageVector
        @Composable get() = vectorResource(Res.drawable.logout)

    override val markChatRead: ImageVector
        @Composable get() = vectorResource(Res.drawable.mark_chat_read)

    override val markChatUnread: ImageVector
        @Composable get() = vectorResource(Res.drawable.mark_chat_unread)

    override val menu: ImageVector
        @Composable get() = vectorResource(Res.drawable.menu)

    override val menuOpen: ImageVector
        @Composable get() = vectorResource(Res.drawable.menu_open)

    override val moreHoriz: ImageVector
        @Composable get() = vectorResource(Res.drawable.more_horiz)

    override val moreVert: ImageVector
        @Composable get() = vectorResource(Res.drawable.more_vert)

    override val notifications: ImageVector
        @Composable get() = vectorResource(Res.drawable.notifications)

    override val openInBrowser: ImageVector
        @Composable get() = vectorResource(Res.drawable.open_in_browser)

    override val palette: ImageVector
        @Composable get() = vectorResource(Res.drawable.palette)

    override val pending: ImageVector
        @Composable get() = vectorResource(Res.drawable.pending)

    override val percent: ImageVector
        @Composable get() = vectorResource(Res.drawable.percent)

    override val person: ImageVector
        @Composable get() = vectorResource(Res.drawable.person)

    override val playCircle: ImageVector
        @Composable get() = vectorResource(Res.drawable.play_circle)

    override val preview: ImageVector
        @Composable get() = vectorResource(Res.drawable.preview)

    override val public: ImageVector
        @Composable get() = vectorResource(Res.drawable.public)

    override val reply: ImageVector
        @Composable get() = vectorResource(Res.drawable.reply)

    override val report: ImageVector
        @Composable get() = vectorResource(Res.drawable.report)

    override val reportOff: ImageVector
        @Composable get() = vectorResource(Res.drawable.report_off)

    override val rocketLaunch: ImageVector
        @Composable get() = vectorResource(Res.drawable.rocket_launch)

    override val save: ImageVector
        @Composable get() = vectorResource(Res.drawable.save)

    override val scale: ImageVector
        @Composable get() = vectorResource(Res.drawable.scale)

    override val science: ImageVector
        @Composable get() = vectorResource(Res.drawable.science)

    override val schedule: ImageVector
        @Composable get() = vectorResource(Res.drawable.schedule)

    override val share: ImageVector
        @Composable get() = vectorResource(Res.drawable.share)

    override val search: ImageVector
        @Composable get() = vectorResource(Res.drawable.search)

    override val send: ImageVector
        @Composable get() = vectorResource(Res.drawable.send)

    override val settingsApplications: ImageVector
        @Composable get() = vectorResource(Res.drawable.settings_applications)

    override val settingsFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.settings_fill)

    override val shield: ImageVector
        @Composable get() = vectorResource(Res.drawable.shield)

    override val star: ImageVector
        @Composable get() = vectorResource(Res.drawable.star)

    override val starFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.star_fill)

    override val strikethroughS: ImageVector
        @Composable get() = vectorResource(Res.drawable.strikethrough_s)

    override val styleFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.style_fill)

    override val stylusFountainPenFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.stylus_fountain_pen_fill)

    override val sync: ImageVector
        @Composable get() = vectorResource(Res.drawable.sync)

    override val syncDisabled: ImageVector
        @Composable get() = vectorResource(Res.drawable.sync_disabled)

    override val support: ImageVector
        @Composable get() = vectorResource(Res.drawable.support)

    override val tag: ImageVector
        @Composable get() = vectorResource(Res.drawable.tag)

    override val thumbsUpDownFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.thumbs_up_down_fill)

    override val thunderstorm: ImageVector
        @Composable get() = vectorResource(Res.drawable.thunderstorm)

    override val trendingUp: ImageVector
        @Composable get() = vectorResource(Res.drawable.trending_up)

    override val unfoldLess: ImageVector
        @Composable get() = vectorResource(Res.drawable.unfold_less)

    override val unfoldMore: ImageVector
        @Composable get() = vectorResource(Res.drawable.unfold_more)

    override val update: ImageVector
        @Composable get() = vectorResource(Res.drawable.update)

    override val viewWeekFill: ImageVector
        @Composable get() = vectorResource(Res.drawable.view_week_fill)

    override val verified: ImageVector
        @Composable get() = vectorResource(Res.drawable.verified)

    override val visibility: ImageVector
        @Composable get() = vectorResource(Res.drawable.visibility)

    override val visibilityOff: ImageVector
        @Composable get() = vectorResource(Res.drawable.visibility_off)

    override val volunteerActivism: ImageVector
        @Composable get() = vectorResource(Res.drawable.volunteer_activism)

    override val workspacePremium: ImageVector
        @Composable get() = vectorResource(Res.drawable.workspace_premium)
    // endregion
}
