<div align="center">
  <img alt="badge for Android" src="https://img.shields.io/badge/Android-26+-34A853?logo=android" />
  <a href="https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/actions/workflows/build.yml" target="_blank"><img alt="badge for build status" src="https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/actions/workflows/build.yml/badge.svg" /></a>
  <a href="https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/actions/workflows/unit_tests.yml" target="_blank"><img alt="badge for unit test status" src="https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/actions/workflows/unit_tests.yml/badge.svg" /></a>
  <a href="https://codecov.io/gh/LiveFastEatTrashRaccoon/RaccoonForLemmy" target="_blank"><img alt="code coverage badge" src="https://codecov.io/gh/LiveFastEatTrashRaccoon/RaccoonForLemmy/graph/badge.svg?token=Z4OH4LL7LB"/></a>
  <a href="https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/releases" target="_blank"><img alt="GitHub Release" src="https://img.shields.io/github/v/release/LiveFastEatTrashRaccoon/RaccoonForLemmy?include_prereleases"></a>
  <a href="https://f-droid.org/packages/com.livefast.eattrash.raccoonforlemmy.android" target="_blank"><img alt="F-Droid version badge" src="https://img.shields.io/f-droid/v/com.livefast.eattrash.raccoonforlemmy.android?logo=fdroid"></a>
  <img alt="GitHub Downloads (all assets, all releases)" src="https://img.shields.io/github/downloads/LiveFastEatTrashRaccoon/RaccoonForLemmy/total">
  <a href="https://hosted.weblate.org/engage/raccoonforlemmy/"><img src="https://hosted.weblate.org/widget/raccoonforlemmy/svg-badge.svg" alt="translation status" /></a>
</div>

<br />

<div align="center">
  <img alt="application icon" src="https://github.com/user-attachments/assets/47265cc3-2bb0-4c9b-8dfa-4923dea22571" width="180" height="auto" />
</div>

# Raccoon for Lemmy

Raccoon for Lemmy is a client for the federated aggregation and discussion platform Lemmy.
The project is powered by Kotlin Multiplatform (KMP) and Compose Multiplatform (CMP).

It started in 2023 as an exercise to play around with Kotlin and Compose multiplatform and gradually
grew as a fully functional client.

Main features:

- view post feed and comments with different listing and sort types;
- possibility to upvote and downvote (with configurable swipe actions);
- community and user detail (with custom sort types);
- review your own posts and comments (created by you, bookmarked, liked/disliked);
- inbox with replies, mentions and direct messages;
- global search on Lemmy with different result types (all, posts, comments, user, communities);
- create and edit new posts (with optional images);
- cross-post contents to other communities;
- reply to post and comments (and edit replies);
- mark posts as read and hide read contents;
- custom appearance (color scheme, fonts, text sizes, post layout, etc.);
- custom localization (independent of system settings);
- block users, communities and instances;
- report post and comments to moderators;
- support for multiple accounts (and multiple instances) with account-specific settings;
- lazy scrolling (referred to as "zombie mode");
- explore all the communities on a given instance in guest mode;
- multi-community (community aggregation);
- community moderation, instance moderation and moderation log;
- save posts and comments you are creating as drafts to edit them later;
- add custom tags to users to easily recognize them across communities.

## Want to try it out?

Here are some options to install the application on your device, apart from downloading the APKs
from the [Releases](https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/releases) page.

<div align="center">
  <div style="display: flex; flex-flow: row wrap; justify-content: center; align-items: center;">
    <a href="https://github.com/ImranR98/Obtainium/releases"><img alt="Get it on Obtainium" width="200" height="80" src="https://github.com/user-attachments/assets/377575fe-a651-4420-afad-8dee21618c44" /></a>
    <a href="https://f-droid.org/packages/com.livefast.eattrash.raccoonforlemmy.android"><img alt="Get it on F-Droid" width="200" height="80" src="https://fdroid.gitlab.io/artwork/badge/get-it-on.png"></a>
    <a href="https://play.google.com/apps/testing/com.livefast.eattrash.raccoonforlemmy.android"><img alt="Get it on Google Play" width="200" height="80" src="https://github.com/user-attachments/assets/e4b9e82a-8630-45ba-90f4-de4cf3207ee6"></a>
  </div>
</div>

> [!TIP]
> On Obtainium, please make sure to check the "Include pre-releases" option: by doing so you will be
> able to receive all alpha and beta builds automatically.

## Want to leave your feedback or report a bug?

<div align="center">
  <a href="https://livefasteattrashraccoon.github.io/RaccoonForLemmy" target="_blank"><img alt="badge for website" src="https://img.shields.io/badge/website-9812db?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iNDBweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSI0MHB4IiBmaWxsPSIjZThlYWVkIj48cGF0aCBkPSJNNDgwLTgwcS04MyAwLTE1Ni0zMS41VDE5Ny0xOTdxLTU0LTU0LTg1LjUtMTI3VDgwLTQ4MHEwLTgzIDMxLjUtMTU2VDE5Ny03NjNxNTQtNTQgMTI3LTg1LjVUNDgwLTg4MHE4MyAwIDE1NiAzMS41VDc2My03NjNxNTQgNTQgODUuNSAxMjdUODgwLTQ4MHEwIDgzLTMxLjUgMTU2VDc2My0xOTdxLTU0IDU0LTEyNyA4NS41VDQ4MC04MFptLTQyLTY4di04MC42N3EtMzQuMzMgMC01OC4xNy0yNS4xNlEzNTYtMjc5IDM1Ni0zMTMuMzNWLTM1NkwxNTUuMzMtNTU2LjY3cS00LjMzIDE5LjM0LTYuNSAzOC4zNC0yLjE2IDE5LTIuMTYgMzguMzMgMCAxMjcgODIuODMgMjIyVDQzOC0xNDhabTI4OC0xMDZxMjEuMzMtMjMuMzMgMzcuNjctNDkuODMgMTYuMzMtMjYuNSAyNy41LTU1LjM0IDExLjE2LTI4LjgzIDE2LjY2LTU5LjE2IDUuNS0zMC4zNCA1LjUtNjEuNjcgMC0xMDMuMzMtNTYuODMtMTg4VDYwNC42Ny03OTEuMzNWLTc3NHEwIDM0LjMzLTIzLjg0IDU5LjUtMjMuODMgMjUuMTctNTguMTYgMjUuMTdINDM4djg0LjY2cTAgMTctMTIuODMgMjguMTctMTIuODQgMTEuMTctMjkuODQgMTEuMTdoLTgyVi00ODBoMjUycTE3IDAgMjguMTcgMTIuNSAxMS4xNyAxMi41IDExLjE3IDI5LjV2MTI0LjY3aDQycTI4IDAgNDkuNjYgMTYuNVE3MTgtMjgwLjMzIDcyNi0yNTRaIi8+PC9zdmc+" /></a>
  <a href="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/main" target="_blank"><img alt="badge for user manual" src="https://img.shields.io/badge/user%20manual-3c00c7?logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIGhlaWdodD0iNDBweCIgdmlld0JveD0iMCAtOTYwIDk2MCA5NjAiIHdpZHRoPSI0MHB4IiBmaWxsPSIjZThlYWVkIj48cGF0aCBkPSJNNTYwLTU3MC42N3YtNTQuNjZxMzMtMTQgNjcuNS0yMXQ3Mi41LTdxMjYgMCA1MSA0dDQ5IDEwdjUwLjY2cS0yNC05LTQ4LjUtMTMuNXQtNTEuNS00LjVxLTM4IDAtNzMgOS41dC02NyAyNi41Wm0wIDIyMFYtNDA2cTMzLTEzLjY3IDY3LjUtMjAuNXQ3Mi41LTYuODNxMjYgMCA1MSA0dDQ5IDEwdjUwLjY2cS0yNC05LTQ4LjUtMTMuNXQtNTEuNS00LjVxLTM4IDAtNzMgOXQtNjcgMjdabTAtMTEwdi01NC42NnEzMy0xNCA2Ny41LTIxdDcyLjUtN3EyNiAwIDUxIDR0NDkgMTB2NTAuNjZxLTI0LTktNDguNS0xMy41dC01MS41LTQuNXEtMzggMC03MyA5LjV0LTY3IDI2LjVabS0zMDggMTU0cTUxLjM4IDAgMTAwLjAyIDExLjg0UTQwMC42Ny0yODMgNDQ4LTI1OS4zM3YtNDE2cS00My42Ny0yOC05NC4wOC00M3QtMTAxLjkyLTE1cS0zNy4zMyAwLTczLjUgOC42NlExNDIuMzMtNzE2IDEwNi42Ny03MDJ2NDIxLjMzUTEzOS0yOTQgMTc2LjgzLTMwMC4zM3EzNy44NC02LjM0IDc1LjE3LTYuMzRabTI2Mi42NyA0Ny4zNHE0OC0yMy42NyA5NC44My0zNS41IDQ2LjgzLTExLjg0IDk4LjUtMTEuODQgMzcuMzMgMCA3NS44MyA2dDY5LjUgMTYuNjd2LTQxOHEtMzMuNjYtMTYtNzAuNzEtMjMuNjctMzcuMDUtNy42Ni03NC42Mi03LjY2LTUxLjY3IDAtMTAwLjY3IDE1dC05Mi42NiA0M3Y0MTZaTTQ4MS4zMy0xNjBxLTUwLTM4LTEwOC42Ni01OC42N1EzMTQtMjM5LjMzIDI1Mi0yMzkuMzNxLTM4LjM2IDAtNzUuMzUgOS42Ni0zNi45OCA5LjY3LTcyLjY1IDI1LTIyLjQgMTEtNDMuMi0yLjMzUTQwLTIyMC4zMyA0MC0yNDUuMzN2LTQ2OS4zNHEwLTEzLjY2IDYuNS0yNS4zM1E1My03NTEuNjcgNjYtNzU4cTQzLjMzLTIxLjMzIDkwLjI2LTMxLjY3UTIwMy4xOS04MDAgMjUyLTgwMHE2MS4zMyAwIDExOS41IDE2LjMzIDU4LjE3IDE2LjM0IDEwOS44MyA0OS42NyA1MS0zMy4zMyAxMDguNS00OS42N1E2NDcuMzMtODAwIDcwOC04MDBxNDguNTggMCA5NS4yOSAxMC4zM1E4NTAtNzc5LjMzIDg5My4zMy03NThxMTMgNi4zMyAxOS44NCAxOCA2LjgzIDExLjY3IDYuODMgMjUuMzN2NDY5LjM0cTAgMjYuMjYtMjEuNSAzOS45NnQtNDMuMTcuN3EtMzUtMTYtNzEuOTgtMjUuMzMtMzYuOTktOS4zMy03NS4zNS05LjMzLTYyIDAtMTE5LjMzIDIxLTU3LjM0IDIxLTEwNy4zNCA1OC4zM1ptLTIwNC0zMzAuNjdaIi8+PC9zdmc+" /></a>
  <a href="https://matrix.to/#/#raccoonforlemmyapp:matrix.org" target="_blank"><img alt="Matrix logo" src="https://img.shields.io/badge/matrix-009900?logo=Matrix" /></a>
  <a href="https://lemmy.world/c/raccoonforlemmyapp" target="_blank"><img alt="Lemmy logo" src="https://img.shields.io/badge/lemmy-ff0066?logo=Lemmy" /></a>
</div>

Reach out to the community on Lemmy, Matrix or file a report in the
[issue tracker](https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/issues)
here on GitHub. Alternatively, you can always write an
[email](mailto://livefast.eattrash.raccoon@gmail.com).

Please remember: every contribution is welcome and everyone's opinion matters here. This is a
community project, open source, ad-free and free of charge, and it belongs to us all so don't be
afraid to get involved.

And don't forget every 🦝's motto: `#livefasteattrash`.

## Would you like to improve the existing translation?

<div align="center">
  <a href="https://hosted.weblate.org/engage/raccoonforlemmy/">
    <img src="https://hosted.weblate.org/widget/raccoonforlemmy/app/287x66-black.png" alt="translation status on Weblate" />
  </a>
</div>

Translators and localizators, your work is of paramount importance to make sure user experience is
of high quality on the app! If you want to help with the localization, please have a look at the
project on Weblate and submit your changes.

## Screenshots

<div align="center">
<table>
  <tr>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/home_fab.png" width="310" alt="home screen" />
    </td>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/post_detail.png" width="310" alt="post detail" />
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/explore_communities.png" width="310" alt="explore screen" />
    </td>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/community_detail.png" width="310" alt="community detail" />
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/community_info.png" width="310" alt="inbox screen" />
    </td>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/user_detail.png" width="310" alt="settings screen" />
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/inbox_replies.png" width="310" alt="inbox screen" />
    </td>
    <td>
      <img src="https://livefasteattrashraccoon.github.io/RaccoonForLemmy/user_manual/images/settings_1.png" width="310" alt="settings screen" />
    </td>
  </tr>
</table>
</div>

## Why was the project started?

Because raccoons are so adorable, aren't they? 🦝🦝🦝

Joking apart, one of the main goals was to experiment with KMP and learn how to properly deal with
the challenges of a multiplatform environment, and a medium-sized project like this was an ideal
testing ground for that technology.

Secondly, I felt that the Android ecosystem of Lemmy apps in 2023 was a little "poor" with few
native apps (fewer open source), while the "market" is dominated by iOS and cross-platform clients.
I️ ❤️ Kotlin, I ❤️ Free and Open Source Software and I ❤️ native app development, so there was a
niche that could be filled.

Developing a new client was an opportunity to add all the good features that were "scattered" across
different apps, e.g. the feature richness of [Liftoff](https://github.com/liftoff-app/liftoff),
the multi-community feature of [Summit](https://github.com/idunnololz/summit-for-lemmy) and the
polished UI of the really great [Thunder](https://github.com/thunder-app/thunder) and so on.
This app tries to be configurable enough to make users feel "at home" and choose what they want,
while at the same time having a not too cluttered interface.

In the third place, this app has been a means to dig deeper inside Lemmy's internals and become more
humble and patient towards other apps because there are technical difficulties in having to deal
with a platform like Lemmy.

This project _is_ all about experimenting and learning, so please be patient.

The app is intended both to regular users and moderators. To the formers, it offers the ability to
change a lot of aspects like font face or size and app colors, post layout, vote format, and so
on in order to create a tailor-made solution for their needs and personal tastes.

For moderators and admins who want to use their mobile device, the app provides a set of moderation
tools (feature post, lock post, distinguish comment, remove post/comment, ban users) and the ability
to revert any of these actions. It also has admin tools (purge users/posts/comments/communities,
feature posts locally, hide/unhide communities) to simplify admin moderation without having to
switch to the web UI.

## Technical notes:

- [Calf](https://github.com/MohamedRejeb/Calf) for a web view implementation;
- [Coil](https://github.com/coil-kt/coil) for image loading;
- [Compose Multiplatform](https://www.jetbrains.com/lp/compose-multiplatform) for UI;
- [Compose ColorPicker](https://github.com/skydoves/colorpicker-compose) for custom color selection;
- [Compose Multiplatform Media Player](https://github.com/Chaintech-Network/ComposeMultiplatformMediaPlayer)
  for video playback;
- [Kodein](https://github.com/kosi-libs/Kodein) for dependency injection;
- [Ktor](https://ktor.io/) for networking;
- [Multiplatform Markdown Renderer](https://github.com/mikepenz/multiplatform-markdown-renderer) for
  Markdown rendering
- [Multiplatform Settings](https://github.com/russhwolf/multiplatform-settings) for encrypted shared
  preferences;
- [MaterialKolor](https://github.com/jordond/MaterialKolor) for custom theme generation;
- [SQLDelight](https://github.com/cashapp/sqldelight)
  and [SQLCipher](https://github.com/sqlcipher/sqlcipher) for local persistence
- [AndroidX Compose navigation](https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-navigation.htm)
  for navigation;

More info about the technologies used in the project can be found in
the [CONTRIBUTING.md](https://github.com/livefasteattrashraccoon/RaccoonForLemmy/blob/master/CONTRIBUTING.md#51-tech-stack).

## Disclaimers

> [!WARNING]
> This is an experimental project and some technologies it is build upon are still in pre-production
> stage, moreover this is a side-project developed by volunteers in their spare time, so use
> _at your own risk_.

This means you should be prepared to occasional failures, yet-to-implement features and areas
where some polish is needed.

> [!WARNING]
> Starting from version 1.13.0-beta01 (118) the application ID has changed so if you are running
> Raccoon 1.13.0-20240730-pre or earlier version you are not receiving updates any more and you are
> encouraged to upgrade. You can save a backup of your settings using the "Export settings
> to file" option in the "Advanced Setting" screen and reimporting the JSON using "Import settings
> from file" in the new app.

The app is maintained by the same people and in the same spirit, it was just a package name change
and the repository ownership was moved to an organization.
