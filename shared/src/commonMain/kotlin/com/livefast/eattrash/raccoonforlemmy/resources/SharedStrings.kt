package com.livefast.eattrash.raccoonforlemmy.resources

import androidx.compose.runtime.Composable
import com.livefast.eattrash.raccoonforlemmy.core.l10n.Strings
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import raccoonforlemmy.shared.generated.resources.Res
import raccoonforlemmy.shared.generated.resources.action_activate_zombie_mode
import raccoonforlemmy.shared.generated.resources.action_back_to_top
import raccoonforlemmy.shared.generated.resources.action_chat
import raccoonforlemmy.shared.generated.resources.action_clear_read
import raccoonforlemmy.shared.generated.resources.action_copy_clipboard
import raccoonforlemmy.shared.generated.resources.action_create_community
import raccoonforlemmy.shared.generated.resources.action_create_post
import raccoonforlemmy.shared.generated.resources.action_deactivate_zombie_mode
import raccoonforlemmy.shared.generated.resources.action_downvote
import raccoonforlemmy.shared.generated.resources.action_exit_search
import raccoonforlemmy.shared.generated.resources.action_logout
import raccoonforlemmy.shared.generated.resources.action_quote
import raccoonforlemmy.shared.generated.resources.action_reply
import raccoonforlemmy.shared.generated.resources.action_restore
import raccoonforlemmy.shared.generated.resources.action_save
import raccoonforlemmy.shared.generated.resources.action_search_in_comments
import raccoonforlemmy.shared.generated.resources.action_search_in_community
import raccoonforlemmy.shared.generated.resources.action_toggle_read
import raccoonforlemmy.shared.generated.resources.action_upvote
import raccoonforlemmy.shared.generated.resources.admin_action_mark_as_featured
import raccoonforlemmy.shared.generated.resources.admin_action_purge
import raccoonforlemmy.shared.generated.resources.admin_action_unmark_as_featured
import raccoonforlemmy.shared.generated.resources.advanced_settings_default_language
import raccoonforlemmy.shared.generated.resources.app_icon_alt_1
import raccoonforlemmy.shared.generated.resources.app_icon_classical
import raccoonforlemmy.shared.generated.resources.app_icon_default
import raccoonforlemmy.shared.generated.resources.ban_item_duration_days
import raccoonforlemmy.shared.generated.resources.ban_item_permanent
import raccoonforlemmy.shared.generated.resources.ban_item_remove_data
import raccoonforlemmy.shared.generated.resources.ban_reason_placeholder
import raccoonforlemmy.shared.generated.resources.bar_theme_opaque
import raccoonforlemmy.shared.generated.resources.bar_theme_solid
import raccoonforlemmy.shared.generated.resources.bar_theme_transparent
import raccoonforlemmy.shared.generated.resources.beta
import raccoonforlemmy.shared.generated.resources.block_action_community
import raccoonforlemmy.shared.generated.resources.block_action_user
import raccoonforlemmy.shared.generated.resources.button_add
import raccoonforlemmy.shared.generated.resources.button_cancel
import raccoonforlemmy.shared.generated.resources.button_close
import raccoonforlemmy.shared.generated.resources.button_confirm
import raccoonforlemmy.shared.generated.resources.button_load
import raccoonforlemmy.shared.generated.resources.button_no_stay
import raccoonforlemmy.shared.generated.resources.button_reset
import raccoonforlemmy.shared.generated.resources.button_retry
import raccoonforlemmy.shared.generated.resources.button_yes_quit
import raccoonforlemmy.shared.generated.resources.comment_action_delete
import raccoonforlemmy.shared.generated.resources.community_action_add_favorite
import raccoonforlemmy.shared.generated.resources.community_action_edit
import raccoonforlemmy.shared.generated.resources.community_action_remove_favorite
import raccoonforlemmy.shared.generated.resources.community_action_unsubscribe
import raccoonforlemmy.shared.generated.resources.community_action_view_modlog
import raccoonforlemmy.shared.generated.resources.community_detail_block
import raccoonforlemmy.shared.generated.resources.community_detail_block_instance
import raccoonforlemmy.shared.generated.resources.community_detail_info
import raccoonforlemmy.shared.generated.resources.community_detail_instance_info
import raccoonforlemmy.shared.generated.resources.community_info_comments
import raccoonforlemmy.shared.generated.resources.community_info_daily_active_users
import raccoonforlemmy.shared.generated.resources.community_info_moderators
import raccoonforlemmy.shared.generated.resources.community_info_monthly_active_users
import raccoonforlemmy.shared.generated.resources.community_info_posts
import raccoonforlemmy.shared.generated.resources.community_info_subscribers
import raccoonforlemmy.shared.generated.resources.community_info_weekly_active_users
import raccoonforlemmy.shared.generated.resources.community_set_custom_sort
import raccoonforlemmy.shared.generated.resources.community_set_preferred_language
import raccoonforlemmy.shared.generated.resources.community_visibility_local_only
import raccoonforlemmy.shared.generated.resources.community_visibility_public
import raccoonforlemmy.shared.generated.resources.configure_actions_side_end
import raccoonforlemmy.shared.generated.resources.configure_actions_side_start
import raccoonforlemmy.shared.generated.resources.content_scale_fill_height
import raccoonforlemmy.shared.generated.resources.content_scale_fill_width
import raccoonforlemmy.shared.generated.resources.content_scale_fit
import raccoonforlemmy.shared.generated.resources.copy_both
import raccoonforlemmy.shared.generated.resources.copy_text
import raccoonforlemmy.shared.generated.resources.copy_title
import raccoonforlemmy.shared.generated.resources.create_comment_body
import raccoonforlemmy.shared.generated.resources.create_comment_title
import raccoonforlemmy.shared.generated.resources.create_post_body
import raccoonforlemmy.shared.generated.resources.create_post_community
import raccoonforlemmy.shared.generated.resources.create_post_cross_post_text
import raccoonforlemmy.shared.generated.resources.create_post_name
import raccoonforlemmy.shared.generated.resources.create_post_nsfw
import raccoonforlemmy.shared.generated.resources.create_post_tab_editor
import raccoonforlemmy.shared.generated.resources.create_post_tab_preview
import raccoonforlemmy.shared.generated.resources.create_post_title
import raccoonforlemmy.shared.generated.resources.create_post_url
import raccoonforlemmy.shared.generated.resources.create_report_placeholder
import raccoonforlemmy.shared.generated.resources.create_report_title_comment
import raccoonforlemmy.shared.generated.resources.create_report_title_post
import raccoonforlemmy.shared.generated.resources.dialog_raw_content_text
import raccoonforlemmy.shared.generated.resources.dialog_raw_content_title
import raccoonforlemmy.shared.generated.resources.dialog_raw_content_url
import raccoonforlemmy.shared.generated.resources.dialog_title_add_instance
import raccoonforlemmy.shared.generated.resources.dialog_title_change_instance
import raccoonforlemmy.shared.generated.resources.dialog_title_raw_content
import raccoonforlemmy.shared.generated.resources.dialog_title_select_community
import raccoonforlemmy.shared.generated.resources.edit_comment_title
import raccoonforlemmy.shared.generated.resources.edit_community_header_textual
import raccoonforlemmy.shared.generated.resources.edit_community_item_posting_restricted_to_mods
import raccoonforlemmy.shared.generated.resources.edit_community_item_sidebar
import raccoonforlemmy.shared.generated.resources.edit_community_item_visibility
import raccoonforlemmy.shared.generated.resources.edit_post_title
import raccoonforlemmy.shared.generated.resources.explore_result_type_all
import raccoonforlemmy.shared.generated.resources.explore_result_type_comments
import raccoonforlemmy.shared.generated.resources.explore_result_type_communities
import raccoonforlemmy.shared.generated.resources.explore_result_type_posts
import raccoonforlemmy.shared.generated.resources.explore_result_type_title
import raccoonforlemmy.shared.generated.resources.explore_result_type_users
import raccoonforlemmy.shared.generated.resources.explore_search_placeholder
import raccoonforlemmy.shared.generated.resources.filtered_contents_type
import raccoonforlemmy.shared.generated.resources.home_instance_via
import raccoonforlemmy.shared.generated.resources.home_listing_title
import raccoonforlemmy.shared.generated.resources.home_listing_type_all
import raccoonforlemmy.shared.generated.resources.home_listing_type_local
import raccoonforlemmy.shared.generated.resources.home_listing_type_subscribed
import raccoonforlemmy.shared.generated.resources.home_sort_title
import raccoonforlemmy.shared.generated.resources.home_sort_type_active
import raccoonforlemmy.shared.generated.resources.home_sort_type_controversial
import raccoonforlemmy.shared.generated.resources.home_sort_type_hot
import raccoonforlemmy.shared.generated.resources.home_sort_type_most_comments
import raccoonforlemmy.shared.generated.resources.home_sort_type_new
import raccoonforlemmy.shared.generated.resources.home_sort_type_new_comments
import raccoonforlemmy.shared.generated.resources.home_sort_type_old
import raccoonforlemmy.shared.generated.resources.home_sort_type_scaled
import raccoonforlemmy.shared.generated.resources.home_sort_type_top
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_12_hours
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_12_hours_short
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_6_hours
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_6_hours_short
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_day
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_day_short
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_hour
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_hour_short
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_month
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_month_short
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_week
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_week_short
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_year
import raccoonforlemmy.shared.generated.resources.home_sort_type_top_year_short
import raccoonforlemmy.shared.generated.resources.inbox_chat_message
import raccoonforlemmy.shared.generated.resources.inbox_item_mention
import raccoonforlemmy.shared.generated.resources.inbox_item_reply_comment
import raccoonforlemmy.shared.generated.resources.inbox_item_reply_post
import raccoonforlemmy.shared.generated.resources.inbox_listing_type_all
import raccoonforlemmy.shared.generated.resources.inbox_listing_type_title
import raccoonforlemmy.shared.generated.resources.inbox_listing_type_unread
import raccoonforlemmy.shared.generated.resources.inbox_not_logged_message
import raccoonforlemmy.shared.generated.resources.inbox_notification_content
import raccoonforlemmy.shared.generated.resources.inbox_notification_title
import raccoonforlemmy.shared.generated.resources.inbox_section_mentions
import raccoonforlemmy.shared.generated.resources.inbox_section_messages
import raccoonforlemmy.shared.generated.resources.inbox_section_replies
import raccoonforlemmy.shared.generated.resources.instance_detail_communities
import raccoonforlemmy.shared.generated.resources.instance_detail_title
import raccoonforlemmy.shared.generated.resources.language_de
import raccoonforlemmy.shared.generated.resources.language_en
import raccoonforlemmy.shared.generated.resources.language_es
import raccoonforlemmy.shared.generated.resources.language_fi
import raccoonforlemmy.shared.generated.resources.language_fr
import raccoonforlemmy.shared.generated.resources.language_ga
import raccoonforlemmy.shared.generated.resources.language_it
import raccoonforlemmy.shared.generated.resources.language_pl
import raccoonforlemmy.shared.generated.resources.language_pt
import raccoonforlemmy.shared.generated.resources.language_pt_br
import raccoonforlemmy.shared.generated.resources.language_ua
import raccoonforlemmy.shared.generated.resources.language_zh_cn
import raccoonforlemmy.shared.generated.resources.language_zh_hk
import raccoonforlemmy.shared.generated.resources.language_zh_tw
import raccoonforlemmy.shared.generated.resources.login_field_instance_name
import raccoonforlemmy.shared.generated.resources.login_field_label_optional
import raccoonforlemmy.shared.generated.resources.login_field_password
import raccoonforlemmy.shared.generated.resources.login_field_token
import raccoonforlemmy.shared.generated.resources.login_field_user_name
import raccoonforlemmy.shared.generated.resources.manage_accounts_button_add
import raccoonforlemmy.shared.generated.resources.manage_accounts_title
import raccoonforlemmy.shared.generated.resources.manage_subscriptions_header_multicommunities
import raccoonforlemmy.shared.generated.resources.manage_subscriptions_header_subscriptions
import raccoonforlemmy.shared.generated.resources.manage_user_tag_title
import raccoonforlemmy.shared.generated.resources.message_are_you_sure
import raccoonforlemmy.shared.generated.resources.message_auth_issue
import raccoonforlemmy.shared.generated.resources.message_auth_issue_segue_0
import raccoonforlemmy.shared.generated.resources.message_auth_issue_segue_1
import raccoonforlemmy.shared.generated.resources.message_auth_issue_segue_2
import raccoonforlemmy.shared.generated.resources.message_auth_issue_segue_3
import raccoonforlemmy.shared.generated.resources.message_confirm_exit
import raccoonforlemmy.shared.generated.resources.message_content_deleted
import raccoonforlemmy.shared.generated.resources.message_content_removed
import raccoonforlemmy.shared.generated.resources.message_empty_comments
import raccoonforlemmy.shared.generated.resources.message_empty_list
import raccoonforlemmy.shared.generated.resources.message_error_loading_comments
import raccoonforlemmy.shared.generated.resources.message_generic_error
import raccoonforlemmy.shared.generated.resources.message_image_loading_error
import raccoonforlemmy.shared.generated.resources.message_invalid_field
import raccoonforlemmy.shared.generated.resources.message_missing_field
import raccoonforlemmy.shared.generated.resources.message_no_result
import raccoonforlemmy.shared.generated.resources.message_operation_successful
import raccoonforlemmy.shared.generated.resources.message_read_all_inbox_success
import raccoonforlemmy.shared.generated.resources.message_unsaved_changes
import raccoonforlemmy.shared.generated.resources.message_video_nsfw
import raccoonforlemmy.shared.generated.resources.mod_action_add_mod
import raccoonforlemmy.shared.generated.resources.mod_action_allow
import raccoonforlemmy.shared.generated.resources.mod_action_ban
import raccoonforlemmy.shared.generated.resources.mod_action_lock
import raccoonforlemmy.shared.generated.resources.mod_action_mark_as_distinguished
import raccoonforlemmy.shared.generated.resources.mod_action_mark_as_featured
import raccoonforlemmy.shared.generated.resources.mod_action_open_reports
import raccoonforlemmy.shared.generated.resources.mod_action_remove
import raccoonforlemmy.shared.generated.resources.mod_action_remove_mod
import raccoonforlemmy.shared.generated.resources.mod_action_unlock
import raccoonforlemmy.shared.generated.resources.mod_action_unmark_as_distinguished
import raccoonforlemmy.shared.generated.resources.mod_action_unmark_as_featured
import raccoonforlemmy.shared.generated.resources.moderator_zone_action_contents
import raccoonforlemmy.shared.generated.resources.moderator_zone_title
import raccoonforlemmy.shared.generated.resources.modlog_item_comment_purged
import raccoonforlemmy.shared.generated.resources.modlog_item_comment_removed
import raccoonforlemmy.shared.generated.resources.modlog_item_comment_restored
import raccoonforlemmy.shared.generated.resources.modlog_item_community_purged
import raccoonforlemmy.shared.generated.resources.modlog_item_community_transfer
import raccoonforlemmy.shared.generated.resources.modlog_item_hidden
import raccoonforlemmy.shared.generated.resources.modlog_item_mod_added
import raccoonforlemmy.shared.generated.resources.modlog_item_mod_removed
import raccoonforlemmy.shared.generated.resources.modlog_item_person_purged
import raccoonforlemmy.shared.generated.resources.modlog_item_post_featured
import raccoonforlemmy.shared.generated.resources.modlog_item_post_locked
import raccoonforlemmy.shared.generated.resources.modlog_item_post_purged
import raccoonforlemmy.shared.generated.resources.modlog_item_post_removed
import raccoonforlemmy.shared.generated.resources.modlog_item_post_restored
import raccoonforlemmy.shared.generated.resources.modlog_item_post_unfeatured
import raccoonforlemmy.shared.generated.resources.modlog_item_post_unlocked
import raccoonforlemmy.shared.generated.resources.modlog_item_unhidden
import raccoonforlemmy.shared.generated.resources.modlog_item_user_banned
import raccoonforlemmy.shared.generated.resources.modlog_item_user_unbanned
import raccoonforlemmy.shared.generated.resources.modlog_title
import raccoonforlemmy.shared.generated.resources.multi_community_editor_communities
import raccoonforlemmy.shared.generated.resources.multi_community_editor_icon
import raccoonforlemmy.shared.generated.resources.multi_community_editor_name
import raccoonforlemmy.shared.generated.resources.multi_community_editor_title
import raccoonforlemmy.shared.generated.resources.navigation_drawer_anonymous
import raccoonforlemmy.shared.generated.resources.navigation_drawer_title_bookmarks
import raccoonforlemmy.shared.generated.resources.navigation_drawer_title_drafts
import raccoonforlemmy.shared.generated.resources.navigation_drawer_title_subscriptions
import raccoonforlemmy.shared.generated.resources.navigation_home
import raccoonforlemmy.shared.generated.resources.navigation_inbox
import raccoonforlemmy.shared.generated.resources.navigation_profile
import raccoonforlemmy.shared.generated.resources.navigation_search
import raccoonforlemmy.shared.generated.resources.navigation_settings
import raccoonforlemmy.shared.generated.resources.never
import raccoonforlemmy.shared.generated.resources.notice_banned_user
import raccoonforlemmy.shared.generated.resources.notice_community_local_only
import raccoonforlemmy.shared.generated.resources.post_action_cross_post
import raccoonforlemmy.shared.generated.resources.post_action_edit
import raccoonforlemmy.shared.generated.resources.post_action_hide
import raccoonforlemmy.shared.generated.resources.post_action_report
import raccoonforlemmy.shared.generated.resources.post_action_see_raw
import raccoonforlemmy.shared.generated.resources.post_action_share
import raccoonforlemmy.shared.generated.resources.post_action_unhide
import raccoonforlemmy.shared.generated.resources.post_detail_cross_posts
import raccoonforlemmy.shared.generated.resources.post_detail_load_more_comments
import raccoonforlemmy.shared.generated.resources.post_hour_short
import raccoonforlemmy.shared.generated.resources.post_list_load_more_posts
import raccoonforlemmy.shared.generated.resources.post_minute_short
import raccoonforlemmy.shared.generated.resources.post_reply_source_account
import raccoonforlemmy.shared.generated.resources.post_second_short
import raccoonforlemmy.shared.generated.resources.profile_button_login
import raccoonforlemmy.shared.generated.resources.profile_day_short
import raccoonforlemmy.shared.generated.resources.profile_million_short
import raccoonforlemmy.shared.generated.resources.profile_month_short
import raccoonforlemmy.shared.generated.resources.profile_not_logged_message
import raccoonforlemmy.shared.generated.resources.profile_section_comments
import raccoonforlemmy.shared.generated.resources.profile_section_posts
import raccoonforlemmy.shared.generated.resources.profile_thousand_short
import raccoonforlemmy.shared.generated.resources.profile_upvotes_downvotes
import raccoonforlemmy.shared.generated.resources.profile_year_short
import raccoonforlemmy.shared.generated.resources.report_action_resolve
import raccoonforlemmy.shared.generated.resources.report_action_unresolve
import raccoonforlemmy.shared.generated.resources.report_list_title
import raccoonforlemmy.shared.generated.resources.report_list_type_all
import raccoonforlemmy.shared.generated.resources.report_list_type_title
import raccoonforlemmy.shared.generated.resources.report_list_type_unresolved
import raccoonforlemmy.shared.generated.resources.requires_restart
import raccoonforlemmy.shared.generated.resources.select_action_title
import raccoonforlemmy.shared.generated.resources.select_tab_navigation_title
import raccoonforlemmy.shared.generated.resources.settings_about
import raccoonforlemmy.shared.generated.resources.settings_about_acknowledgements
import raccoonforlemmy.shared.generated.resources.settings_about_app_version
import raccoonforlemmy.shared.generated.resources.settings_about_changelog
import raccoonforlemmy.shared.generated.resources.settings_about_licences
import raccoonforlemmy.shared.generated.resources.settings_about_matrix
import raccoonforlemmy.shared.generated.resources.settings_about_report_email
import raccoonforlemmy.shared.generated.resources.settings_about_report_github
import raccoonforlemmy.shared.generated.resources.settings_about_view_github
import raccoonforlemmy.shared.generated.resources.settings_about_view_lemmy
import raccoonforlemmy.shared.generated.resources.settings_advanced
import raccoonforlemmy.shared.generated.resources.settings_ancillary_font_scale
import raccoonforlemmy.shared.generated.resources.settings_app_icon
import raccoonforlemmy.shared.generated.resources.settings_auto_expand_comments
import raccoonforlemmy.shared.generated.resources.settings_auto_load_images
import raccoonforlemmy.shared.generated.resources.settings_bar_theme
import raccoonforlemmy.shared.generated.resources.settings_blur_nsfw
import raccoonforlemmy.shared.generated.resources.settings_color_aquamarine
import raccoonforlemmy.shared.generated.resources.settings_color_banana
import raccoonforlemmy.shared.generated.resources.settings_color_blue
import raccoonforlemmy.shared.generated.resources.settings_color_custom
import raccoonforlemmy.shared.generated.resources.settings_color_dialog_alpha
import raccoonforlemmy.shared.generated.resources.settings_color_dialog_blue
import raccoonforlemmy.shared.generated.resources.settings_color_dialog_green
import raccoonforlemmy.shared.generated.resources.settings_color_dialog_red
import raccoonforlemmy.shared.generated.resources.settings_color_dialog_title
import raccoonforlemmy.shared.generated.resources.settings_color_gray
import raccoonforlemmy.shared.generated.resources.settings_color_green
import raccoonforlemmy.shared.generated.resources.settings_color_orange
import raccoonforlemmy.shared.generated.resources.settings_color_pink
import raccoonforlemmy.shared.generated.resources.settings_color_purple
import raccoonforlemmy.shared.generated.resources.settings_color_red
import raccoonforlemmy.shared.generated.resources.settings_color_white
import raccoonforlemmy.shared.generated.resources.settings_colors_and_fonts
import raccoonforlemmy.shared.generated.resources.settings_comment_bar_theme
import raccoonforlemmy.shared.generated.resources.settings_comment_bar_theme_blue
import raccoonforlemmy.shared.generated.resources.settings_comment_bar_theme_green
import raccoonforlemmy.shared.generated.resources.settings_comment_bar_theme_multi
import raccoonforlemmy.shared.generated.resources.settings_comment_bar_theme_red
import raccoonforlemmy.shared.generated.resources.settings_comment_bar_thickness
import raccoonforlemmy.shared.generated.resources.settings_comment_font_scale
import raccoonforlemmy.shared.generated.resources.settings_comment_indent_amount
import raccoonforlemmy.shared.generated.resources.settings_configure_content
import raccoonforlemmy.shared.generated.resources.settings_configure_customizations
import raccoonforlemmy.shared.generated.resources.settings_configure_swipe_actions
import raccoonforlemmy.shared.generated.resources.settings_configure_text
import raccoonforlemmy.shared.generated.resources.settings_content_font_family
import raccoonforlemmy.shared.generated.resources.settings_content_font_large
import raccoonforlemmy.shared.generated.resources.settings_content_font_larger
import raccoonforlemmy.shared.generated.resources.settings_content_font_largest
import raccoonforlemmy.shared.generated.resources.settings_content_font_normal
import raccoonforlemmy.shared.generated.resources.settings_content_font_scale
import raccoonforlemmy.shared.generated.resources.settings_content_font_small
import raccoonforlemmy.shared.generated.resources.settings_content_font_smaller
import raccoonforlemmy.shared.generated.resources.settings_content_font_smallest
import raccoonforlemmy.shared.generated.resources.settings_custom_seed_color
import raccoonforlemmy.shared.generated.resources.settings_default_comment_sort_type
import raccoonforlemmy.shared.generated.resources.settings_default_explore_result_type
import raccoonforlemmy.shared.generated.resources.settings_default_explore_type
import raccoonforlemmy.shared.generated.resources.settings_default_inbox_type
import raccoonforlemmy.shared.generated.resources.settings_default_listing_type
import raccoonforlemmy.shared.generated.resources.settings_default_post_sort_type
import raccoonforlemmy.shared.generated.resources.settings_downvote_color
import raccoonforlemmy.shared.generated.resources.settings_dynamic_colors
import raccoonforlemmy.shared.generated.resources.settings_edge_to_edge
import raccoonforlemmy.shared.generated.resources.settings_enable_buttons_to_scroll_between_comments
import raccoonforlemmy.shared.generated.resources.settings_enable_crash_report
import raccoonforlemmy.shared.generated.resources.settings_enable_double_tap
import raccoonforlemmy.shared.generated.resources.settings_enable_swipe_actions
import raccoonforlemmy.shared.generated.resources.settings_enable_toggle_favorite_in_nav_drawer
import raccoonforlemmy.shared.generated.resources.settings_export
import raccoonforlemmy.shared.generated.resources.settings_fade_read_posts
import raccoonforlemmy.shared.generated.resources.settings_font_family_default
import raccoonforlemmy.shared.generated.resources.settings_full_height_images
import raccoonforlemmy.shared.generated.resources.settings_full_width_images
import raccoonforlemmy.shared.generated.resources.settings_hidden_posts
import raccoonforlemmy.shared.generated.resources.settings_hide_navigation_bar
import raccoonforlemmy.shared.generated.resources.settings_import
import raccoonforlemmy.shared.generated.resources.settings_inbox_background_check_period
import raccoonforlemmy.shared.generated.resources.settings_inbox_preview_max_lines
import raccoonforlemmy.shared.generated.resources.settings_include_nsfw
import raccoonforlemmy.shared.generated.resources.settings_infinite_scroll_disabled
import raccoonforlemmy.shared.generated.resources.settings_item_alternate_markdown_rendering
import raccoonforlemmy.shared.generated.resources.settings_item_configure_bottom_navigation_bar
import raccoonforlemmy.shared.generated.resources.settings_item_image_source_path
import raccoonforlemmy.shared.generated.resources.settings_item_open_post_web_page_on_image_click
import raccoonforlemmy.shared.generated.resources.settings_item_random_theme_color
import raccoonforlemmy.shared.generated.resources.settings_language
import raccoonforlemmy.shared.generated.resources.settings_manage_ban
import raccoonforlemmy.shared.generated.resources.settings_manage_ban_action_unban
import raccoonforlemmy.shared.generated.resources.settings_manage_ban_domain_placeholder
import raccoonforlemmy.shared.generated.resources.settings_manage_ban_section_domains
import raccoonforlemmy.shared.generated.resources.settings_manage_ban_section_instances
import raccoonforlemmy.shared.generated.resources.settings_manage_ban_section_stop_words
import raccoonforlemmy.shared.generated.resources.settings_manage_ban_stop_word_placeholder
import raccoonforlemmy.shared.generated.resources.settings_mark_as_read_while_scrolling
import raccoonforlemmy.shared.generated.resources.settings_media_list
import raccoonforlemmy.shared.generated.resources.settings_navigation_bar_titles_visible
import raccoonforlemmy.shared.generated.resources.settings_open_url_external
import raccoonforlemmy.shared.generated.resources.settings_points_short
import raccoonforlemmy.shared.generated.resources.settings_post_body_max_lines
import raccoonforlemmy.shared.generated.resources.settings_post_body_max_lines_unlimited
import raccoonforlemmy.shared.generated.resources.settings_post_layout
import raccoonforlemmy.shared.generated.resources.settings_post_layout_card
import raccoonforlemmy.shared.generated.resources.settings_post_layout_compact
import raccoonforlemmy.shared.generated.resources.settings_post_layout_full
import raccoonforlemmy.shared.generated.resources.settings_prefer_user_nicknames
import raccoonforlemmy.shared.generated.resources.settings_reply_color
import raccoonforlemmy.shared.generated.resources.settings_save_color
import raccoonforlemmy.shared.generated.resources.settings_search_posts_restrict_local_user_search
import raccoonforlemmy.shared.generated.resources.settings_search_posts_restrict_local_user_search_subtitle
import raccoonforlemmy.shared.generated.resources.settings_search_posts_title_only
import raccoonforlemmy.shared.generated.resources.settings_search_posts_title_only_subtitle
import raccoonforlemmy.shared.generated.resources.settings_section_account
import raccoonforlemmy.shared.generated.resources.settings_section_appearance
import raccoonforlemmy.shared.generated.resources.settings_section_debug
import raccoonforlemmy.shared.generated.resources.settings_section_general
import raccoonforlemmy.shared.generated.resources.settings_section_nsfw
import raccoonforlemmy.shared.generated.resources.settings_show_scores
import raccoonforlemmy.shared.generated.resources.settings_show_unread_comments
import raccoonforlemmy.shared.generated.resources.settings_subtitle_image_source_path
import raccoonforlemmy.shared.generated.resources.settings_subtitle_open_post_web_page_on_image_click
import raccoonforlemmy.shared.generated.resources.settings_subtitle_random_theme_color
import raccoonforlemmy.shared.generated.resources.settings_theme_black
import raccoonforlemmy.shared.generated.resources.settings_theme_dark
import raccoonforlemmy.shared.generated.resources.settings_theme_light
import raccoonforlemmy.shared.generated.resources.settings_title_display
import raccoonforlemmy.shared.generated.resources.settings_title_experimental
import raccoonforlemmy.shared.generated.resources.settings_title_font_scale
import raccoonforlemmy.shared.generated.resources.settings_title_pictures
import raccoonforlemmy.shared.generated.resources.settings_title_reading
import raccoonforlemmy.shared.generated.resources.settings_ui_font_family
import raccoonforlemmy.shared.generated.resources.settings_ui_font_scale
import raccoonforlemmy.shared.generated.resources.settings_ui_theme
import raccoonforlemmy.shared.generated.resources.settings_upvote_color
import raccoonforlemmy.shared.generated.resources.settings_url_opening_mode_custom_tabs
import raccoonforlemmy.shared.generated.resources.settings_url_opening_mode_external
import raccoonforlemmy.shared.generated.resources.settings_url_opening_mode_internal
import raccoonforlemmy.shared.generated.resources.settings_use_avatar_as_profile_navigation_icon
import raccoonforlemmy.shared.generated.resources.settings_user_manual
import raccoonforlemmy.shared.generated.resources.settings_vote_format
import raccoonforlemmy.shared.generated.resources.settings_vote_format_aggregated
import raccoonforlemmy.shared.generated.resources.settings_vote_format_hidden
import raccoonforlemmy.shared.generated.resources.settings_vote_format_percentage
import raccoonforlemmy.shared.generated.resources.settings_vote_format_separated
import raccoonforlemmy.shared.generated.resources.settings_web_avatar
import raccoonforlemmy.shared.generated.resources.settings_web_banner
import raccoonforlemmy.shared.generated.resources.settings_web_bio
import raccoonforlemmy.shared.generated.resources.settings_web_bot
import raccoonforlemmy.shared.generated.resources.settings_web_display_name
import raccoonforlemmy.shared.generated.resources.settings_web_email
import raccoonforlemmy.shared.generated.resources.settings_web_email_notifications
import raccoonforlemmy.shared.generated.resources.settings_web_header_contents
import raccoonforlemmy.shared.generated.resources.settings_web_header_notifications
import raccoonforlemmy.shared.generated.resources.settings_web_header_personal
import raccoonforlemmy.shared.generated.resources.settings_web_matrix
import raccoonforlemmy.shared.generated.resources.settings_web_preferences
import raccoonforlemmy.shared.generated.resources.settings_web_show_bot
import raccoonforlemmy.shared.generated.resources.settings_web_show_nsfw
import raccoonforlemmy.shared.generated.resources.settings_web_show_read
import raccoonforlemmy.shared.generated.resources.settings_zombie_mode_interval
import raccoonforlemmy.shared.generated.resources.settings_zombie_mode_scroll_amount
import raccoonforlemmy.shared.generated.resources.share_mode_file
import raccoonforlemmy.shared.generated.resources.share_mode_url
import raccoonforlemmy.shared.generated.resources.sidebar_not_logged_message
import raccoonforlemmy.shared.generated.resources.undetermined
import raccoonforlemmy.shared.generated.resources.user_detail_info
import raccoonforlemmy.shared.generated.resources.user_info_admin
import raccoonforlemmy.shared.generated.resources.user_info_moderates
import raccoonforlemmy.shared.generated.resources.user_tag_color
import raccoonforlemmy.shared.generated.resources.user_tags_title

internal class SharedStrings : Strings {
    override val actionActivateZombieMode: String
        @Composable get() = stringResource(Res.string.action_activate_zombie_mode)
    override val actionBackToTop: String
        @Composable get() = stringResource(Res.string.action_back_to_top)
    override val actionChat: String
        @Composable get() = stringResource(Res.string.action_chat)
    override val actionClearRead: String
        @Composable get() = stringResource(Res.string.action_clear_read)
    override val actionCopyClipboard: String
        @Composable get() = stringResource(Res.string.action_copy_clipboard)
    override val actionCreateCommunity: String
        @Composable get() = stringResource(Res.string.action_create_community)
    override val actionCreatePost: String
        @Composable get() = stringResource(Res.string.action_create_post)
    override val actionDeactivateZombieMode: String
        @Composable get() = stringResource(Res.string.action_deactivate_zombie_mode)
    override val actionDownvote: String
        @Composable get() = stringResource(Res.string.action_downvote)
    override val actionExitSearch: String
        @Composable get() = stringResource(Res.string.action_exit_search)
    override val actionLogout: String
        @Composable get() = stringResource(Res.string.action_logout)
    override val actionQuote: String
        @Composable get() = stringResource(Res.string.action_quote)
    override val actionReply: String
        @Composable get() = stringResource(Res.string.action_reply)
    override val actionRestore: String
        @Composable get() = stringResource(Res.string.action_restore)
    override val actionSave: String
        @Composable get() = stringResource(Res.string.action_save)
    override val actionSearchInComments: String
        @Composable get() = stringResource(Res.string.action_search_in_comments)
    override val actionSearchInCommunity: String
        @Composable get() = stringResource(Res.string.action_search_in_community)
    override val actionToggleRead: String
        @Composable get() = stringResource(Res.string.action_toggle_read)
    override val actionUpvote: String
        @Composable get() = stringResource(Res.string.action_upvote)
    override val adminActionMarkAsFeatured: String
        @Composable get() = stringResource(Res.string.admin_action_mark_as_featured)
    override val adminActionPurge: String
        @Composable get() = stringResource(Res.string.admin_action_purge)
    override val adminActionUnmarkAsFeatured: String
        @Composable get() = stringResource(Res.string.admin_action_unmark_as_featured)
    override val advancedSettingsDefaultLanguage: String
        @Composable get() = stringResource(Res.string.advanced_settings_default_language)
    override val appIconAlt1: String
        @Composable get() = stringResource(Res.string.app_icon_alt_1)
    override val appIconClassical: String
        @Composable get() = stringResource(Res.string.app_icon_classical)
    override val appIconDefault: String
        @Composable get() = stringResource(Res.string.app_icon_default)
    override val banItemDurationDays: String
        @Composable get() = stringResource(Res.string.ban_item_duration_days)
    override val banItemPermanent: String
        @Composable get() = stringResource(Res.string.ban_item_permanent)
    override val banItemRemoveData: String
        @Composable get() = stringResource(Res.string.ban_item_remove_data)
    override val banReasonPlaceholder: String
        @Composable get() = stringResource(Res.string.ban_reason_placeholder)
    override val barThemeOpaque: String
        @Composable get() = stringResource(Res.string.bar_theme_opaque)
    override val barThemeSolid: String
        @Composable get() = stringResource(Res.string.bar_theme_solid)
    override val barThemeTransparent: String
        @Composable get() = stringResource(Res.string.bar_theme_transparent)
    override val beta: String
        @Composable get() = stringResource(Res.string.beta)
    override val blockActionCommunity: String
        @Composable get() = stringResource(Res.string.block_action_community)
    override val blockActionUser: String
        @Composable get() = stringResource(Res.string.block_action_user)
    override val buttonAdd: String
        @Composable get() = stringResource(Res.string.button_add)
    override val buttonCancel: String
        @Composable get() = stringResource(Res.string.button_cancel)
    override val buttonClose: String
        @Composable get() = stringResource(Res.string.button_close)
    override val buttonConfirm: String
        @Composable get() = stringResource(Res.string.button_confirm)
    override val buttonLoad: String
        @Composable get() = stringResource(Res.string.button_load)
    override val buttonNoStay: String
        @Composable get() = stringResource(Res.string.button_no_stay)
    override val buttonReset: String
        @Composable get() = stringResource(Res.string.button_reset)
    override val buttonRetry: String
        @Composable get() = stringResource(Res.string.button_retry)
    override val buttonYesQuit: String
        @Composable get() = stringResource(Res.string.button_yes_quit)
    override val commentActionDelete: String
        @Composable get() = stringResource(Res.string.comment_action_delete)
    override val communityActionAddFavorite: String
        @Composable get() = stringResource(Res.string.community_action_add_favorite)
    override val communityActionEdit: String
        @Composable get() = stringResource(Res.string.community_action_edit)
    override val communityActionRemoveFavorite: String
        @Composable get() = stringResource(Res.string.community_action_remove_favorite)
    override val communityActionUnsubscribe: String
        @Composable get() = stringResource(Res.string.community_action_unsubscribe)
    override val communityActionViewModlog: String
        @Composable get() = stringResource(Res.string.community_action_view_modlog)
    override val communityDetailBlock: String
        @Composable get() = stringResource(Res.string.community_detail_block)
    override val communityDetailBlockInstance: String
        @Composable get() = stringResource(Res.string.community_detail_block_instance)
    override val communityDetailInfo: String
        @Composable get() = stringResource(Res.string.community_detail_info)
    override val communityDetailInstanceInfo: String
        @Composable get() = stringResource(Res.string.community_detail_instance_info)
    override val communityInfoComments: String
        @Composable get() = stringResource(Res.string.community_info_comments)
    override val communityInfoDailyActiveUsers: String
        @Composable get() = stringResource(Res.string.community_info_daily_active_users)
    override val communityInfoModerators: String
        @Composable get() = stringResource(Res.string.community_info_moderators)
    override val communityInfoMonthlyActiveUsers: String
        @Composable get() = stringResource(Res.string.community_info_monthly_active_users)
    override val communityInfoPosts: String
        @Composable get() = stringResource(Res.string.community_info_posts)
    override val communityInfoSubscribers: String
        @Composable get() = stringResource(Res.string.community_info_subscribers)
    override val communityInfoWeeklyActiveUsers: String
        @Composable get() = stringResource(Res.string.community_info_weekly_active_users)
    override val communitySetCustomSort: String
        @Composable get() = stringResource(Res.string.community_set_custom_sort)
    override val communitySetPreferredLanguage: String
        @Composable get() = stringResource(Res.string.community_set_preferred_language)
    override val communityVisibilityLocalOnly: String
        @Composable get() = stringResource(Res.string.community_visibility_local_only)
    override val communityVisibilityPublic: String
        @Composable get() = stringResource(Res.string.community_visibility_public)
    override val configureActionsSideEnd: String
        @Composable get() = stringResource(Res.string.configure_actions_side_end)
    override val configureActionsSideStart: String
        @Composable get() = stringResource(Res.string.configure_actions_side_start)
    override val contentScaleFillHeight: String
        @Composable get() = stringResource(Res.string.content_scale_fill_height)
    override val contentScaleFillWidth: String
        @Composable get() = stringResource(Res.string.content_scale_fill_width)
    override val contentScaleFit: String
        @Composable get() = stringResource(Res.string.content_scale_fit)
    override val copyBoth: String
        @Composable get() = stringResource(Res.string.copy_both)
    override val copyText: String
        @Composable get() = stringResource(Res.string.copy_text)
    override val copyTitle: String
        @Composable get() = stringResource(Res.string.copy_title)
    override val createCommentBody: String
        @Composable get() = stringResource(Res.string.create_comment_body)
    override val createCommentTitle: String
        @Composable get() = stringResource(Res.string.create_comment_title)
    override val createPostBody: String
        @Composable get() = stringResource(Res.string.create_post_body)
    override val createPostCommunity: String
        @Composable get() = stringResource(Res.string.create_post_community)
    override val createPostCrossPostText: String
        @Composable get() = stringResource(Res.string.create_post_cross_post_text)
    override val createPostName: String
        @Composable get() = stringResource(Res.string.create_post_name)
    override val createPostNsfw: String
        @Composable get() = stringResource(Res.string.create_post_nsfw)
    override val createPostTabEditor: String
        @Composable get() = stringResource(Res.string.create_post_tab_editor)
    override val createPostTabPreview: String
        @Composable get() = stringResource(Res.string.create_post_tab_preview)
    override val createPostTitle: String
        @Composable get() = stringResource(Res.string.create_post_title)
    override val createPostUrl: String
        @Composable get() = stringResource(Res.string.create_post_url)
    override val createReportPlaceholder: String
        @Composable get() = stringResource(Res.string.create_report_placeholder)
    override val createReportTitleComment: String
        @Composable get() = stringResource(Res.string.create_report_title_comment)
    override val createReportTitlePost: String
        @Composable get() = stringResource(Res.string.create_report_title_post)
    override val dialogRawContentText: String
        @Composable get() = stringResource(Res.string.dialog_raw_content_text)
    override val dialogRawContentTitle: String
        @Composable get() = stringResource(Res.string.dialog_raw_content_title)
    override val dialogRawContentUrl: String
        @Composable get() = stringResource(Res.string.dialog_raw_content_url)
    override val dialogTitleAddInstance: String
        @Composable get() = stringResource(Res.string.dialog_title_add_instance)
    override val dialogTitleChangeInstance: String
        @Composable get() = stringResource(Res.string.dialog_title_change_instance)
    override val dialogTitleRawContent: String
        @Composable get() = stringResource(Res.string.dialog_title_raw_content)
    override val dialogTitleSelectCommunity: String
        @Composable get() = stringResource(Res.string.dialog_title_select_community)
    override val editCommentTitle: String
        @Composable get() = stringResource(Res.string.edit_comment_title)
    override val editCommunityHeaderTextual: String
        @Composable get() = stringResource(Res.string.edit_community_header_textual)
    override val editCommunityItemPostingRestrictedToMods: String
        @Composable get() =
            stringResource(Res.string.edit_community_item_posting_restricted_to_mods)
    override val editCommunityItemSidebar: String
        @Composable get() = stringResource(Res.string.edit_community_item_sidebar)
    override val editCommunityItemVisibility: String
        @Composable get() = stringResource(Res.string.edit_community_item_visibility)
    override val editPostTitle: String
        @Composable get() = stringResource(Res.string.edit_post_title)
    override val exploreResultTypeAll: String
        @Composable get() = stringResource(Res.string.explore_result_type_all)
    override val exploreResultTypeComments: String
        @Composable get() = stringResource(Res.string.explore_result_type_comments)
    override val exploreResultTypeCommunities: String
        @Composable get() = stringResource(Res.string.explore_result_type_communities)
    override val exploreResultTypePosts: String
        @Composable get() = stringResource(Res.string.explore_result_type_posts)
    override val exploreResultTypeTitle: String
        @Composable get() = stringResource(Res.string.explore_result_type_title)
    override val exploreResultTypeUsers: String
        @Composable get() = stringResource(Res.string.explore_result_type_users)
    override val exploreSearchPlaceholder: String
        @Composable get() = stringResource(Res.string.explore_search_placeholder)
    override val filteredContentsType: String
        @Composable get() = stringResource(Res.string.filtered_contents_type)
    override val homeInstanceVia: String
        @Composable get() = stringResource(Res.string.home_instance_via)
    override val homeListingTitle: String
        @Composable get() = stringResource(Res.string.home_listing_title)
    override val homeListingTypeAll: String
        @Composable get() = stringResource(Res.string.home_listing_type_all)
    override val homeListingTypeLocal: String
        @Composable get() = stringResource(Res.string.home_listing_type_local)
    override val homeListingTypeSubscribed: String
        @Composable get() = stringResource(Res.string.home_listing_type_subscribed)
    override val homeSortTitle: String
        @Composable get() = stringResource(Res.string.home_sort_title)
    override val homeSortTypeActive: String
        @Composable get() = stringResource(Res.string.home_sort_type_active)
    override val homeSortTypeControversial: String
        @Composable get() = stringResource(Res.string.home_sort_type_controversial)
    override val homeSortTypeHot: String
        @Composable get() = stringResource(Res.string.home_sort_type_hot)
    override val homeSortTypeMostComments: String
        @Composable get() = stringResource(Res.string.home_sort_type_most_comments)
    override val homeSortTypeNew: String
        @Composable get() = stringResource(Res.string.home_sort_type_new)
    override val homeSortTypeNewComments: String
        @Composable get() = stringResource(Res.string.home_sort_type_new_comments)
    override val homeSortTypeOld: String
        @Composable get() = stringResource(Res.string.home_sort_type_old)
    override val homeSortTypeScaled: String
        @Composable get() = stringResource(Res.string.home_sort_type_scaled)
    override val homeSortTypeTop12Hours: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_12_hours)
    override val homeSortTypeTop12HoursShort: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_12_hours_short)
    override val homeSortTypeTop6Hours: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_6_hours)
    override val homeSortTypeTop6HoursShort: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_6_hours_short)
    override val homeSortTypeTop: String
        @Composable get() = stringResource(Res.string.home_sort_type_top)
    override val homeSortTypeTopDay: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_day)
    override val homeSortTypeTopDayShort: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_day_short)
    override val homeSortTypeTopHour: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_hour)
    override val homeSortTypeTopHourShort: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_hour_short)
    override val homeSortTypeTopMonth: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_month)
    override val homeSortTypeTopMonthShort: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_month_short)
    override val homeSortTypeTopWeek: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_week)
    override val homeSortTypeTopWeekShort: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_week_short)
    override val homeSortTypeTopYear: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_year)
    override val homeSortTypeTopYearShort: String
        @Composable get() = stringResource(Res.string.home_sort_type_top_year_short)
    override val inboxChatMessage: String
        @Composable get() = stringResource(Res.string.inbox_chat_message)
    override val inboxItemMention: String
        @Composable get() = stringResource(Res.string.inbox_item_mention)
    override val inboxItemReplyComment: String
        @Composable get() = stringResource(Res.string.inbox_item_reply_comment)
    override val inboxItemReplyPost: String
        @Composable get() = stringResource(Res.string.inbox_item_reply_post)
    override val inboxListingTypeAll: String
        @Composable get() = stringResource(Res.string.inbox_listing_type_all)
    override val inboxListingTypeTitle: String
        @Composable get() = stringResource(Res.string.inbox_listing_type_title)
    override val inboxListingTypeUnread: String
        @Composable get() = stringResource(Res.string.inbox_listing_type_unread)
    override val inboxNotLoggedMessage: String
        @Composable get() = stringResource(Res.string.inbox_not_logged_message)
    override val inboxSectionMentions: String
        @Composable get() = stringResource(Res.string.inbox_section_mentions)
    override val inboxSectionMessages: String
        @Composable get() = stringResource(Res.string.inbox_section_messages)
    override val inboxSectionReplies: String
        @Composable get() = stringResource(Res.string.inbox_section_replies)
    override val instanceDetailCommunities: String
        @Composable get() = stringResource(Res.string.instance_detail_communities)
    override val instanceDetailTitle: String
        @Composable get() = stringResource(Res.string.instance_detail_title)
    override val languageDe: String
        @Composable get() = stringResource(Res.string.language_de)
    override val languageEn: String
        @Composable get() = stringResource(Res.string.language_en)
    override val languageEs: String
        @Composable get() = stringResource(Res.string.language_es)
    override val languageFi: String
        @Composable get() = stringResource(Res.string.language_fi)
    override val languageFr: String
        @Composable get() = stringResource(Res.string.language_fr)
    override val languageGa: String
        @Composable get() = stringResource(Res.string.language_ga)
    override val languageIt: String
        @Composable get() = stringResource(Res.string.language_it)
    override val languagePl: String
        @Composable get() = stringResource(Res.string.language_pl)
    override val languagePt: String
        @Composable get() = stringResource(Res.string.language_pt)
    override val languagePtBr: String
        @Composable get() = stringResource(Res.string.language_pt_br)
    override val languageUa: String
        @Composable get() = stringResource(Res.string.language_ua)
    override val languageZhCn: String
        @Composable get() = stringResource(Res.string.language_zh_cn)
    override val languageZhHk: String
        @Composable get() = stringResource(Res.string.language_zh_hk)
    override val languageZhTw: String
        @Composable get() = stringResource(Res.string.language_zh_tw)
    override val loginFieldInstanceName: String
        @Composable get() = stringResource(Res.string.login_field_instance_name)
    override val loginFieldLabelOptional: String
        @Composable get() = stringResource(Res.string.login_field_label_optional)
    override val loginFieldPassword: String
        @Composable get() = stringResource(Res.string.login_field_password)
    override val loginFieldToken: String
        @Composable get() = stringResource(Res.string.login_field_token)
    override val loginFieldUserName: String
        @Composable get() = stringResource(Res.string.login_field_user_name)
    override val manageAccountsButtonAdd: String
        @Composable get() = stringResource(Res.string.manage_accounts_button_add)
    override val manageAccountsTitle: String
        @Composable get() = stringResource(Res.string.manage_accounts_title)
    override val manageSubscriptionsHeaderMulticommunities: String
        @Composable get() =
            stringResource(Res.string.manage_subscriptions_header_multicommunities)
    override val manageSubscriptionsHeaderSubscriptions: String
        @Composable get() =
            stringResource(Res.string.manage_subscriptions_header_subscriptions)
    override val manageUserTagsTitle: String
        @Composable get() = stringResource(Res.string.manage_user_tag_title)
    override val messageAreYouSure: String
        @Composable get() = stringResource(Res.string.message_are_you_sure)
    override val messageAuthIssue: String
        @Composable get() = stringResource(Res.string.message_auth_issue)
    override val messageAuthIssueSegue0: String
        @Composable get() = stringResource(Res.string.message_auth_issue_segue_0)
    override val messageAuthIssueSegue1: String
        @Composable get() = stringResource(Res.string.message_auth_issue_segue_1)
    override val messageAuthIssueSegue2: String
        @Composable get() = stringResource(Res.string.message_auth_issue_segue_2)
    override val messageAuthIssueSegue3: String
        @Composable get() = stringResource(Res.string.message_auth_issue_segue_3)
    override val messageConfirmExit: String
        @Composable get() = stringResource(Res.string.message_confirm_exit)
    override val messageContentDeleted: String
        @Composable get() = stringResource(Res.string.message_content_deleted)
    override val messageContentRemoved: String
        @Composable get() = stringResource(Res.string.message_content_removed)
    override val messageEmptyComments: String
        @Composable get() = stringResource(Res.string.message_empty_comments)
    override val messageEmptyList: String
        @Composable get() = stringResource(Res.string.message_empty_list)
    override val messageErrorLoadingComments: String
        @Composable get() = stringResource(Res.string.message_error_loading_comments)
    override val messageGenericError: String
        @Composable get() = stringResource(Res.string.message_generic_error)
    override val messageImageLoadingError: String
        @Composable get() = stringResource(Res.string.message_image_loading_error)
    override val messageInvalidField: String
        @Composable get() = stringResource(Res.string.message_invalid_field)
    override val messageMissingField: String
        @Composable get() = stringResource(Res.string.message_missing_field)
    override val messageNoResult: String
        @Composable get() = stringResource(Res.string.message_no_result)
    override val messageOperationSuccessful: String
        @Composable get() = stringResource(Res.string.message_operation_successful)
    override val messageReadAllInboxSuccess: String
        @Composable get() = stringResource(Res.string.message_read_all_inbox_success)
    override val messageUnsavedChanges: String
        @Composable get() = stringResource(Res.string.message_unsaved_changes)
    override val messageVideoNsfw: String
        @Composable get() = stringResource(Res.string.message_video_nsfw)
    override val modActionAddMod: String
        @Composable get() = stringResource(Res.string.mod_action_add_mod)
    override val modActionAllow: String
        @Composable get() = stringResource(Res.string.mod_action_allow)
    override val modActionBan: String
        @Composable get() = stringResource(Res.string.mod_action_ban)
    override val modActionLock: String
        @Composable get() = stringResource(Res.string.mod_action_lock)
    override val modActionMarkAsDistinguished: String
        @Composable get() = stringResource(Res.string.mod_action_mark_as_distinguished)
    override val modActionMarkAsFeatured: String
        @Composable get() = stringResource(Res.string.mod_action_mark_as_featured)
    override val modActionOpenReports: String
        @Composable get() = stringResource(Res.string.mod_action_open_reports)
    override val modActionRemove: String
        @Composable get() = stringResource(Res.string.mod_action_remove)
    override val modActionRemoveMod: String
        @Composable get() = stringResource(Res.string.mod_action_remove_mod)
    override val modActionUnlock: String
        @Composable get() = stringResource(Res.string.mod_action_unlock)
    override val modActionUnmarkAsDistinguished: String
        @Composable get() = stringResource(Res.string.mod_action_unmark_as_distinguished)
    override val modActionUnmarkAsFeatured: String
        @Composable get() = stringResource(Res.string.mod_action_unmark_as_featured)
    override val moderatorZoneActionContents: String
        @Composable get() = stringResource(Res.string.moderator_zone_action_contents)
    override val moderatorZoneTitle: String
        @Composable get() = stringResource(Res.string.moderator_zone_title)
    override val modlogItemCommentPurged: String
        @Composable get() = stringResource(Res.string.modlog_item_comment_purged)
    override val modlogItemCommentRemoved: String
        @Composable get() = stringResource(Res.string.modlog_item_comment_removed)
    override val modlogItemCommentRestored: String
        @Composable get() = stringResource(Res.string.modlog_item_comment_restored)
    override val modlogItemCommunityPurged: String
        @Composable get() = stringResource(Res.string.modlog_item_community_purged)
    override val modlogItemCommunityTransfer: String
        @Composable get() = stringResource(Res.string.modlog_item_community_transfer)
    override val modlogItemHidden: String
        @Composable get() = stringResource(Res.string.modlog_item_hidden)
    override val modlogItemModAdded: String
        @Composable get() = stringResource(Res.string.modlog_item_mod_added)
    override val modlogItemModRemoved: String
        @Composable get() = stringResource(Res.string.modlog_item_mod_removed)
    override val modlogItemPersonPurged: String
        @Composable get() = stringResource(Res.string.modlog_item_person_purged)
    override val modlogItemPostFeatured: String
        @Composable get() = stringResource(Res.string.modlog_item_post_featured)
    override val modlogItemPostLocked: String
        @Composable get() = stringResource(Res.string.modlog_item_post_locked)
    override val modlogItemPostPurged: String
        @Composable get() = stringResource(Res.string.modlog_item_post_purged)
    override val modlogItemPostRemoved: String
        @Composable get() = stringResource(Res.string.modlog_item_post_removed)
    override val modlogItemPostRestored: String
        @Composable get() = stringResource(Res.string.modlog_item_post_restored)
    override val modlogItemPostUnfeatured: String
        @Composable get() = stringResource(Res.string.modlog_item_post_unfeatured)
    override val modlogItemPostUnlocked: String
        @Composable get() = stringResource(Res.string.modlog_item_post_unlocked)
    override val modlogItemUnhidden: String
        @Composable get() = stringResource(Res.string.modlog_item_unhidden)
    override val modlogItemUserBanned: String
        @Composable get() = stringResource(Res.string.modlog_item_user_banned)
    override val modlogItemUserUnbanned: String
        @Composable get() = stringResource(Res.string.modlog_item_user_unbanned)
    override val modlogTitle: String
        @Composable get() = stringResource(Res.string.modlog_title)
    override val multiCommunityEditorCommunities: String
        @Composable get() = stringResource(Res.string.multi_community_editor_communities)
    override val multiCommunityEditorIcon: String
        @Composable get() = stringResource(Res.string.multi_community_editor_icon)
    override val multiCommunityEditorName: String
        @Composable get() = stringResource(Res.string.multi_community_editor_name)
    override val multiCommunityEditorTitle: String
        @Composable get() = stringResource(Res.string.multi_community_editor_title)
    override val navigationDrawerAnonymous: String
        @Composable get() = stringResource(Res.string.navigation_drawer_anonymous)
    override val navigationDrawerTitleBookmarks: String
        @Composable get() = stringResource(Res.string.navigation_drawer_title_bookmarks)
    override val navigationDrawerTitleDrafts: String
        @Composable get() = stringResource(Res.string.navigation_drawer_title_drafts)
    override val navigationDrawerTitleSubscriptions: String
        @Composable get() =
            stringResource(Res.string.navigation_drawer_title_subscriptions)
    override val navigationHome: String
        @Composable get() = stringResource(Res.string.navigation_home)
    override val navigationInbox: String
        @Composable get() = stringResource(Res.string.navigation_inbox)
    override val navigationProfile: String
        @Composable get() = stringResource(Res.string.navigation_profile)
    override val navigationSearch: String
        @Composable get() = stringResource(Res.string.navigation_search)
    override val navigationSettings: String
        @Composable get() = stringResource(Res.string.navigation_settings)
    override val never: String
        @Composable get() = stringResource(Res.string.never)
    override val noticeBannedUser: String
        @Composable get() = stringResource(Res.string.notice_banned_user)
    override val noticeCommunityLocalOnly: String
        @Composable get() = stringResource(Res.string.notice_community_local_only)
    override val postActionCrossPost: String
        @Composable get() = stringResource(Res.string.post_action_cross_post)
    override val postActionEdit: String
        @Composable get() = stringResource(Res.string.post_action_edit)
    override val postActionHide: String
        @Composable get() = stringResource(Res.string.post_action_hide)
    override val postActionReport: String
        @Composable get() = stringResource(Res.string.post_action_report)
    override val postActionSeeRaw: String
        @Composable get() = stringResource(Res.string.post_action_see_raw)
    override val postActionShare: String
        @Composable get() = stringResource(Res.string.post_action_share)
    override val postActionUnhide: String
        @Composable get() = stringResource(Res.string.post_action_unhide)
    override val postDetailCrossPosts: String
        @Composable get() = stringResource(Res.string.post_detail_cross_posts)
    override val postDetailLoadMoreComments: String
        @Composable get() = stringResource(Res.string.post_detail_load_more_comments)
    override val postHourShort: String
        @Composable get() = stringResource(Res.string.post_hour_short)
    override val postListLoadMorePosts: String
        @Composable get() = stringResource(Res.string.post_list_load_more_posts)
    override val postMinuteShort: String
        @Composable get() = stringResource(Res.string.post_minute_short)
    override val postReplySourceAccount: String
        @Composable get() = stringResource(Res.string.post_reply_source_account)
    override val postSecondShort: String
        @Composable get() = stringResource(Res.string.post_second_short)
    override val profileButtonLogin: String
        @Composable get() = stringResource(Res.string.profile_button_login)
    override val profileDayShort: String
        @Composable get() = stringResource(Res.string.profile_day_short)
    override val profileMillionShort: String
        @Composable get() = stringResource(Res.string.profile_million_short)
    override val profileMonthShort: String
        @Composable get() = stringResource(Res.string.profile_month_short)
    override val profileNotLoggedMessage: String
        @Composable get() = stringResource(Res.string.profile_not_logged_message)
    override val profileSectionComments: String
        @Composable get() = stringResource(Res.string.profile_section_comments)
    override val profileSectionPosts: String
        @Composable get() = stringResource(Res.string.profile_section_posts)
    override val profileThousandShort: String
        @Composable get() = stringResource(Res.string.profile_thousand_short)
    override val profileUpvotesDownvotes: String
        @Composable get() = stringResource(Res.string.profile_upvotes_downvotes)
    override val profileYearShort: String
        @Composable get() = stringResource(Res.string.profile_year_short)
    override val reportActionResolve: String
        @Composable get() = stringResource(Res.string.report_action_resolve)
    override val reportActionUnresolve: String
        @Composable get() = stringResource(Res.string.report_action_unresolve)
    override val reportListTitle: String
        @Composable get() = stringResource(Res.string.report_list_title)
    override val reportListTypeAll: String
        @Composable get() = stringResource(Res.string.report_list_type_all)
    override val reportListTypeTitle: String
        @Composable get() = stringResource(Res.string.report_list_type_title)
    override val reportListTypeUnresolved: String
        @Composable get() = stringResource(Res.string.report_list_type_unresolved)
    override val requiresRestart: String
        @Composable get() = stringResource(Res.string.requires_restart)
    override val selectActionTitle: String
        @Composable get() = stringResource(Res.string.select_action_title)
    override val selectTabNavigationTitle: String
        @Composable get() = stringResource(Res.string.select_tab_navigation_title)
    override val settingsAbout: String
        @Composable get() = stringResource(Res.string.settings_about)
    override val settingsAboutAcknowledgements: String
        @Composable get() = stringResource(Res.string.settings_about_acknowledgements)
    override val settingsAboutAppVersion: String
        @Composable get() = stringResource(Res.string.settings_about_app_version)
    override val settingsAboutChangelog: String
        @Composable get() = stringResource(Res.string.settings_about_changelog)
    override val settingsAboutLicences: String
        @Composable get() = stringResource(Res.string.settings_about_licences)
    override val settingsAboutMatrix: String
        @Composable get() = stringResource(Res.string.settings_about_matrix)
    override val settingsAboutReportEmail: String
        @Composable get() = stringResource(Res.string.settings_about_report_email)
    override val settingsAboutReportGithub: String
        @Composable get() = stringResource(Res.string.settings_about_report_github)
    override val settingsAboutViewGithub: String
        @Composable get() = stringResource(Res.string.settings_about_view_github)
    override val settingsAboutViewLemmy: String
        @Composable get() = stringResource(Res.string.settings_about_view_lemmy)
    override val settingsAdvanced: String
        @Composable get() = stringResource(Res.string.settings_advanced)
    override val settingsAncillaryFontScale: String
        @Composable get() = stringResource(Res.string.settings_ancillary_font_scale)
    override val settingsAppIcon: String
        @Composable get() = stringResource(Res.string.settings_app_icon)
    override val settingsAutoExpandComments: String
        @Composable get() = stringResource(Res.string.settings_auto_expand_comments)
    override val settingsAutoLoadImages: String
        @Composable get() = stringResource(Res.string.settings_auto_load_images)
    override val settingsBarTheme: String
        @Composable get() = stringResource(Res.string.settings_bar_theme)
    override val settingsBlurNsfw: String
        @Composable get() = stringResource(Res.string.settings_blur_nsfw)
    override val settingsColorAquamarine: String
        @Composable get() = stringResource(Res.string.settings_color_aquamarine)
    override val settingsColorBanana: String
        @Composable get() = stringResource(Res.string.settings_color_banana)
    override val settingsColorBlue: String
        @Composable get() = stringResource(Res.string.settings_color_blue)
    override val settingsColorCustom: String
        @Composable get() = stringResource(Res.string.settings_color_custom)
    override val settingsColorDialogAlpha: String
        @Composable get() = stringResource(Res.string.settings_color_dialog_alpha)
    override val settingsColorDialogBlue: String
        @Composable get() = stringResource(Res.string.settings_color_dialog_blue)
    override val settingsColorDialogGreen: String
        @Composable get() = stringResource(Res.string.settings_color_dialog_green)
    override val settingsColorDialogRed: String
        @Composable get() = stringResource(Res.string.settings_color_dialog_red)
    override val settingsColorDialogTitle: String
        @Composable get() = stringResource(Res.string.settings_color_dialog_title)
    override val settingsColorGray: String
        @Composable get() = stringResource(Res.string.settings_color_gray)
    override val settingsColorGreen: String
        @Composable get() = stringResource(Res.string.settings_color_green)
    override val settingsColorOrange: String
        @Composable get() = stringResource(Res.string.settings_color_orange)
    override val settingsColorPink: String
        @Composable get() = stringResource(Res.string.settings_color_pink)
    override val settingsColorPurple: String
        @Composable get() = stringResource(Res.string.settings_color_purple)
    override val settingsColorRed: String
        @Composable get() = stringResource(Res.string.settings_color_red)
    override val settingsColorWhite: String
        @Composable get() = stringResource(Res.string.settings_color_white)
    override val settingsColorsAndFonts: String
        @Composable get() = stringResource(Res.string.settings_colors_and_fonts)
    override val settingsCommentBarTheme: String
        @Composable get() = stringResource(Res.string.settings_comment_bar_theme)
    override val settingsCommentBarThemeBlue: String
        @Composable get() = stringResource(Res.string.settings_comment_bar_theme_blue)
    override val settingsCommentBarThemeGreen: String
        @Composable get() = stringResource(Res.string.settings_comment_bar_theme_green)
    override val settingsCommentBarThemeMulti: String
        @Composable get() = stringResource(Res.string.settings_comment_bar_theme_multi)
    override val settingsCommentBarThemeRed: String
        @Composable get() = stringResource(Res.string.settings_comment_bar_theme_red)
    override val settingsCommentBarThickness: String
        @Composable get() = stringResource(Res.string.settings_comment_bar_thickness)
    override val settingsCommentFontScale: String
        @Composable get() = stringResource(Res.string.settings_comment_font_scale)
    override val settingsCommentIndentAmount: String
        @Composable get() = stringResource(Res.string.settings_comment_indent_amount)
    override val settingsConfigureContent: String
        @Composable get() = stringResource(Res.string.settings_configure_content)
    override val settingsConfigureCustomizations: String
        @Composable get() = stringResource(Res.string.settings_configure_customizations)
    override val settingsConfigureSwipeActions: String
        @Composable get() = stringResource(Res.string.settings_configure_swipe_actions)
    override val settingsConfigureText: String
        @Composable get() = stringResource(Res.string.settings_configure_text)
    override val settingsContentFontFamily: String
        @Composable get() = stringResource(Res.string.settings_content_font_family)
    override val settingsContentFontLarge: String
        @Composable get() = stringResource(Res.string.settings_content_font_large)
    override val settingsContentFontLarger: String
        @Composable get() = stringResource(Res.string.settings_content_font_larger)
    override val settingsContentFontLargest: String
        @Composable get() = stringResource(Res.string.settings_content_font_largest)
    override val settingsContentFontNormal: String
        @Composable get() = stringResource(Res.string.settings_content_font_normal)
    override val settingsContentFontScale: String
        @Composable get() = stringResource(Res.string.settings_content_font_scale)
    override val settingsContentFontSmall: String
        @Composable get() = stringResource(Res.string.settings_content_font_small)
    override val settingsContentFontSmaller: String
        @Composable get() = stringResource(Res.string.settings_content_font_smaller)
    override val settingsContentFontSmallest: String
        @Composable get() = stringResource(Res.string.settings_content_font_smallest)
    override val settingsCustomSeedColor: String
        @Composable get() = stringResource(Res.string.settings_custom_seed_color)
    override val settingsDefaultCommentSortType: String
        @Composable get() = stringResource(Res.string.settings_default_comment_sort_type)
    override val settingsDefaultExploreResultType: String
        @Composable get() =
            stringResource(Res.string.settings_default_explore_result_type)
    override val settingsDefaultExploreType: String
        @Composable get() = stringResource(Res.string.settings_default_explore_type)
    override val settingsDefaultInboxType: String
        @Composable get() = stringResource(Res.string.settings_default_inbox_type)
    override val settingsDefaultListingType: String
        @Composable get() = stringResource(Res.string.settings_default_listing_type)
    override val settingsDefaultPostSortType: String
        @Composable get() = stringResource(Res.string.settings_default_post_sort_type)
    override val settingsDownvoteColor: String
        @Composable get() = stringResource(Res.string.settings_downvote_color)
    override val settingsDynamicColors: String
        @Composable get() = stringResource(Res.string.settings_dynamic_colors)
    override val settingsEdgeToEdge: String
        @Composable get() = stringResource(Res.string.settings_edge_to_edge)
    override val settingsEnableButtonsToScrollBetweenComments: String
        @Composable get() =
            stringResource(Res.string.settings_enable_buttons_to_scroll_between_comments)
    override val settingsEnableCrashReport: String
        @Composable get() = stringResource(Res.string.settings_enable_crash_report)
    override val settingsEnableDoubleTap: String
        @Composable get() = stringResource(Res.string.settings_enable_double_tap)
    override val settingsEnableSwipeActions: String
        @Composable get() = stringResource(Res.string.settings_enable_swipe_actions)
    override val settingsEnableToggleFavoriteInNavDrawer: String
        @Composable get() =
            stringResource(Res.string.settings_enable_toggle_favorite_in_nav_drawer)
    override val settingsExport: String
        @Composable get() = stringResource(Res.string.settings_export)
    override val settingsFadeReadPosts: String
        @Composable get() = stringResource(Res.string.settings_fade_read_posts)
    override val settingsFontFamilyDefault: String
        @Composable get() = stringResource(Res.string.settings_font_family_default)
    override val settingsFullHeightImages: String
        @Composable get() = stringResource(Res.string.settings_full_height_images)
    override val settingsFullWidthImages: String
        @Composable get() = stringResource(Res.string.settings_full_width_images)
    override val settingsHiddenPosts: String
        @Composable get() = stringResource(Res.string.settings_hidden_posts)
    override val settingsHideNavigationBar: String
        @Composable get() = stringResource(Res.string.settings_hide_navigation_bar)
    override val settingsImport: String
        @Composable get() = stringResource(Res.string.settings_import)
    override val settingsInboxBackgroundCheckPeriod: String
        @Composable get() =
            stringResource(Res.string.settings_inbox_background_check_period)
    override val settingsInboxPreviewMaxLines: String
        @Composable get() = stringResource(Res.string.settings_inbox_preview_max_lines)
    override val settingsIncludeNsfw: String
        @Composable get() = stringResource(Res.string.settings_include_nsfw)
    override val settingsInfiniteScrollDisabled: String
        @Composable get() = stringResource(Res.string.settings_infinite_scroll_disabled)
    override val settingsItemAlternateMarkdownRendering: String
        @Composable get() =
            stringResource(Res.string.settings_item_alternate_markdown_rendering)
    override val settingsItemConfigureBottomNavigationBar: String
        @Composable get() =
            stringResource(Res.string.settings_item_configure_bottom_navigation_bar)
    override val settingsItemImageSourcePath: String
        @Composable get() = stringResource(Res.string.settings_item_image_source_path)
    override val settingsItemOpenPostWebPageOnImageClick: String
        @Composable get() =
            stringResource(Res.string.settings_item_open_post_web_page_on_image_click)
    override val settingsItemRandomThemeColor: String
        @Composable get() = stringResource(Res.string.settings_item_random_theme_color)
    override val settingsLanguage: String
        @Composable get() = stringResource(Res.string.settings_language)
    override val settingsManageBan: String
        @Composable get() = stringResource(Res.string.settings_manage_ban)
    override val settingsManageBanActionUnban: String
        @Composable get() = stringResource(Res.string.settings_manage_ban_action_unban)
    override val settingsManageBanDomainPlaceholder: String
        @Composable get() =
            stringResource(Res.string.settings_manage_ban_domain_placeholder)
    override val settingsManageBanSectionDomains: String
        @Composable get() = stringResource(Res.string.settings_manage_ban_section_domains)
    override val settingsManageBanSectionInstances: String
        @Composable get() =
            stringResource(Res.string.settings_manage_ban_section_instances)
    override val settingsManageBanSectionStopWords: String
        @Composable get() =
            stringResource(Res.string.settings_manage_ban_section_stop_words)
    override val settingsManageBanStopWordPlaceholder: String
        @Composable get() =
            stringResource(Res.string.settings_manage_ban_stop_word_placeholder)
    override val settingsMarkAsReadWhileScrolling: String
        @Composable get() =
            stringResource(Res.string.settings_mark_as_read_while_scrolling)
    override val settingsMediaList: String
        @Composable get() = stringResource(Res.string.settings_media_list)
    override val settingsNavigationBarTitlesVisible: String
        @Composable get() =
            stringResource(Res.string.settings_navigation_bar_titles_visible)
    override val settingsOpenUrlExternal: String
        @Composable get() = stringResource(Res.string.settings_open_url_external)
    override val settingsPointsShort: String
        @Composable get() = stringResource(Res.string.settings_points_short)
    override val settingsPostBodyMaxLines: String
        @Composable get() = stringResource(Res.string.settings_post_body_max_lines)
    override val settingsPostBodyMaxLinesUnlimited: String
        @Composable get() =
            stringResource(Res.string.settings_post_body_max_lines_unlimited)
    override val settingsPostLayout: String
        @Composable get() = stringResource(Res.string.settings_post_layout)
    override val settingsPostLayoutCard: String
        @Composable get() = stringResource(Res.string.settings_post_layout_card)
    override val settingsPostLayoutCompact: String
        @Composable get() = stringResource(Res.string.settings_post_layout_compact)
    override val settingsPostLayoutFull: String
        @Composable get() = stringResource(Res.string.settings_post_layout_full)
    override val settingsPreferUserNicknames: String
        @Composable get() = stringResource(Res.string.settings_prefer_user_nicknames)
    override val settingsReplyColor: String
        @Composable get() = stringResource(Res.string.settings_reply_color)
    override val settingsSaveColor: String
        @Composable get() = stringResource(Res.string.settings_save_color)
    override val settingsSearchPostsTitleOnly: String
        @Composable get() = stringResource(Res.string.settings_search_posts_title_only)
    override val settingsSearchPostsTitleOnlySubtitle: String
        @Composable get() = stringResource(Res.string.settings_search_posts_title_only_subtitle)
    override val settingsSearchRestrictLocalUserSearch: String
        @Composable get() = stringResource(Res.string.settings_search_posts_restrict_local_user_search)
    override val settingsSearchRestrictLocalUserSearchSubtitle: String
        @Composable get() = stringResource(Res.string.settings_search_posts_restrict_local_user_search_subtitle)
    override val settingsSectionAccount: String
        @Composable get() = stringResource(Res.string.settings_section_account)
    override val settingsSectionAppearance: String
        @Composable get() = stringResource(Res.string.settings_section_appearance)
    override val settingsSectionDebug: String
        @Composable get() = stringResource(Res.string.settings_section_debug)
    override val settingsSectionGeneral: String
        @Composable get() = stringResource(Res.string.settings_section_general)
    override val settingsSectionNsfw: String
        @Composable get() = stringResource(Res.string.settings_section_nsfw)
    override val settingsShowScores: String
        @Composable get() = stringResource(Res.string.settings_show_scores)
    override val settingsShowUnreadComments: String
        @Composable get() = stringResource(Res.string.settings_show_unread_comments)
    override val settingsSubtitleImageSourcePath: String
        @Composable get() = stringResource(Res.string.settings_subtitle_image_source_path)
    override val settingsSubtitleOpenPostWebPageOnImageClick: String
        @Composable get() =
            stringResource(Res.string.settings_subtitle_open_post_web_page_on_image_click)
    override val settingsSubtitleRandomThemeColor: String
        @Composable get() = stringResource(Res.string.settings_subtitle_random_theme_color)
    override val settingsThemeBlack: String
        @Composable get() = stringResource(Res.string.settings_theme_black)
    override val settingsThemeDark: String
        @Composable get() = stringResource(Res.string.settings_theme_dark)
    override val settingsThemeLight: String
        @Composable get() = stringResource(Res.string.settings_theme_light)
    override val settingsTitleDisplay: String
        @Composable get() = stringResource(Res.string.settings_title_display)
    override val settingsTitleExperimental: String
        @Composable get() = stringResource(Res.string.settings_title_experimental)
    override val settingsTitleFontScale: String
        @Composable get() = stringResource(Res.string.settings_title_font_scale)
    override val settingsTitlePictures: String
        @Composable get() = stringResource(Res.string.settings_title_pictures)
    override val settingsTitleReading: String
        @Composable get() = stringResource(Res.string.settings_title_reading)
    override val settingsUiFontFamily: String
        @Composable get() = stringResource(Res.string.settings_ui_font_family)
    override val settingsUiFontScale: String
        @Composable get() = stringResource(Res.string.settings_ui_font_scale)
    override val settingsUiTheme: String
        @Composable get() = stringResource(Res.string.settings_ui_theme)
    override val settingsUpvoteColor: String
        @Composable get() = stringResource(Res.string.settings_upvote_color)
    override val settingsUrlOpeningModeCustomTabs: String
        @Composable get() =
            stringResource(Res.string.settings_url_opening_mode_custom_tabs)
    override val settingsUrlOpeningModeExternal: String
        @Composable get() = stringResource(Res.string.settings_url_opening_mode_external)
    override val settingsUrlOpeningModeInternal: String
        @Composable get() = stringResource(Res.string.settings_url_opening_mode_internal)
    override val settingsUseAvatarAsProfileNavigationIcon: String
        @Composable get() =
            stringResource(Res.string.settings_use_avatar_as_profile_navigation_icon)
    override val settingsUserManual: String
        @Composable get() = stringResource(Res.string.settings_user_manual)
    override val settingsVoteFormat: String
        @Composable get() = stringResource(Res.string.settings_vote_format)
    override val settingsVoteFormatAggregated: String
        @Composable get() = stringResource(Res.string.settings_vote_format_aggregated)
    override val settingsVoteFormatHidden: String
        @Composable get() = stringResource(Res.string.settings_vote_format_hidden)
    override val settingsVoteFormatPercentage: String
        @Composable get() = stringResource(Res.string.settings_vote_format_percentage)
    override val settingsVoteFormatSeparated: String
        @Composable get() = stringResource(Res.string.settings_vote_format_separated)
    override val settingsWebAvatar: String
        @Composable get() = stringResource(Res.string.settings_web_avatar)
    override val settingsWebBanner: String
        @Composable get() = stringResource(Res.string.settings_web_banner)
    override val settingsWebBio: String
        @Composable get() = stringResource(Res.string.settings_web_bio)
    override val settingsWebBot: String
        @Composable get() = stringResource(Res.string.settings_web_bot)
    override val settingsWebDisplayName: String
        @Composable get() = stringResource(Res.string.settings_web_display_name)
    override val settingsWebEmail: String
        @Composable get() = stringResource(Res.string.settings_web_email)
    override val settingsWebEmailNotifications: String
        @Composable get() = stringResource(Res.string.settings_web_email_notifications)
    override val settingsWebHeaderContents: String
        @Composable get() = stringResource(Res.string.settings_web_header_contents)
    override val settingsWebHeaderNotifications: String
        @Composable get() = stringResource(Res.string.settings_web_header_notifications)
    override val settingsWebHeaderPersonal: String
        @Composable get() = stringResource(Res.string.settings_web_header_personal)
    override val settingsWebMatrix: String
        @Composable get() = stringResource(Res.string.settings_web_matrix)
    override val settingsWebPreferences: String
        @Composable get() = stringResource(Res.string.settings_web_preferences)
    override val settingsWebShowBot: String
        @Composable get() = stringResource(Res.string.settings_web_show_bot)
    override val settingsWebShowNsfw: String
        @Composable get() = stringResource(Res.string.settings_web_show_nsfw)
    override val settingsWebShowRead: String
        @Composable get() = stringResource(Res.string.settings_web_show_read)
    override val settingsZombieModeInterval: String
        @Composable get() = stringResource(Res.string.settings_zombie_mode_interval)
    override val settingsZombieModeScrollAmount: String
        @Composable get() = stringResource(Res.string.settings_zombie_mode_scroll_amount)
    override val shareModeFile: String
        @Composable get() = stringResource(Res.string.share_mode_file)
    override val shareModeUrl: String
        @Composable get() = stringResource(Res.string.share_mode_url)
    override val sidebarNotLoggedMessage: String
        @Composable get() = stringResource(Res.string.sidebar_not_logged_message)
    override val undetermined: String
        @Composable get() = stringResource(Res.string.undetermined)
    override val userDetailInfo: String
        @Composable get() = stringResource(Res.string.user_detail_info)
    override val userInfoAdmin: String
        @Composable get() = stringResource(Res.string.user_info_admin)
    override val userInfoModerates: String
        @Composable get() = stringResource(Res.string.user_info_moderates)
    override val userTagColor: String
        @Composable get() = stringResource(Res.string.user_tag_color)
    override val userTagsTitle: String
        @Composable get() = stringResource(Res.string.user_tags_title)

    override suspend fun inboxNotificationTitle(): String = getString(Res.string.inbox_notification_title)

    override suspend fun inboxNotificationContent(count: Int): String =
        getPluralString(Res.plurals.inbox_notification_content, count, count)
}
