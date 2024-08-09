<div align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?logo=kotlin" />
  <img src="https://img.shields.io/badge/Gradle-8.8-02303A?logo=gradle" />
  <img src="https://img.shields.io/badge/Android-26+-34A853?logo=android" />
  <img src="https://img.shields.io/badge/Compose-1.6.7-4285F4?logo=jetpackcompose" />
  <img src="https://img.shields.io/github/license/livefasteattrashraccoon/raccoonforlemmy" />
</div>

<br />

<div align="center">
  <img src="https://github.com/user-attachments/assets/47265cc3-2bb0-4c9b-8dfa-4923dea22571" width="250" height="auto" />
</div>

# Raccoon for Lemmy

A Kotlin Multiplatform client for Lemmy.

<div align="center">
<table>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/4f260197-3902-4f95-b2d1-c58c50bdd484" width="310" alt="home screen" />
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/49425175-8949-4aec-bea0-185143fc7096" width="310" alt="post detail" />
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/b3866545-8a32-4a3c-b32f-87ddcc8a7b65" width="310" alt="explore screen" />
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/36249619-57d6-4710-9111-21fcbf624a0c" width="310" alt="community detail" />
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://github.com/user-attachments/assets/70a5996d-fbcb-4d71-9d46-27a20e6b0b94" width="310" alt="inbox screen" />
    </td>
    <td>
      <img src="https://github.com/user-attachments/assets/f1f9097e-b935-4bcf-9956-714fadc41e85" width="310" alt="settings screen" />
    </td>
  </tr>
</table>
</div>

Raccoon for Lemmy is a client for the federated aggregation and discussion platform Lemmy.

The project started as an exercise to play around with Kotlin Multiplatform (KMP) and Compose
multiplatform and gradually grew as a fully functional client with many features.

## Disclaimer

This is an experimental project and some technologies it is build upon are still in pre-production
stage, moreover this is a side-project developed by volunteers in their spare time, so use _at your
own risk_, please don't expect a full-fledged and fully functional app and be prepared to occasional
failures and yet-to-implement features.

Please be willing to contribute if you can, instead of being ready to demand. Thank you.

## Main features

- view post feed and comments with different listing and sort types;
- possibility to upvote and downvote (with configurable swipe actions);
- community and user detail;
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
- save posts and comments you are creating as drafts to edit them later.

Most clients for Lemmy currently offer the first points (with various degrees of completion), so
there is nothing special about Raccoon, whereas the last ones are less common and are directed to
more demanding users.

Concerning customization, the ability to change some aspects like font face or size and app colors,
vote format, bar transparency and so on was of paramount importance from the very beginning.
Similarly, users should be able to use the app in their native language and change the UI language
independently of the system language.

This app is also intended for moderators who want to use their mobile device, offering moderation
tools (feature post, lock post, distinguish comment, remove post/comment, ban users) and the ability
to revert any of these actions. It also has admin tools (purge users/posts/comments/communities,
feature posts locally, hide/unhide communities) to simplify admin moderation without having to
switch to the web UI.

The project is under active development, so expect new features to be added over time. Have a look
on the issues labeled with "feature" in the issue tracker to get an idea of what's going to come next.

If you have ideas, feedback, suggestions or comments remember to speak up and use your voice. You
can add reports or request features and they will be considered.

## Why was the project started?

Because raccoons are so adorable, aren't they? 🦝🦝🦝

Joking apart, one of the main goals was to experiment with KMP and learn how to properly deal with
the challenges of a multiplatform environment, and a medium-sized project like this was an ideal
testing ground for that technology.

Secondly, I felt that the Android ecosystem of Lemmy apps was a little "poor" with few native apps
(fewer open source), while the "market" is dominated by iOS and cross-platform clients.
I️ ❤️ Kotlin, I ❤️ Free and Open Source Software and I ❤️ native app development, so there was a niche
that could be filled.

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

## Technical notes:

- [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
- [Voyager](https://github.com/adrielcafe/voyager) for screen navigation
- [Ktor](https://github.com/ktorio/ktor) and [Ktorfit](https://github.com/Foso/Ktorfit) for
  networking
- [Lyricist](https://github.com/adrielcafe/lyricist) for l10n
- [Multiplatform settings](https://github.com/russhwolf/multiplatform-settings) for encrypted
  preferences
- [SQLDelight](https://github.com/cashapp/sqldelight)
  and [SQLCipher](https://github.com/sqlcipher/sqlcipher) for local persistence
- [Multiplatform Markdown Renderer](https://github.com/mikepenz/multiplatform-markdown-renderer) for
  Markdown rendering

More info about the technologies used in the project can be found in
the [CONTRIBUTING.md](https://github.com/livefasteattrashraccoon/RaccoonForLemmy/blob/master/CONTRIBUTING.md#51-tech-stack).

## Want to try it out?

Here are some options to install the application on your device.

<div align="center">
  <div style="display: flex; flex-flow: row wrap; justify-content: center; align-items: center;">
    <a href="https://github.com/ImranR98/Obtainium/releases"><img width="200" src="https://github.com/user-attachments/assets/377575fe-a651-4420-afad-8dee21618c44" /></a>
  </div>
</div>

<br />

## Want to leave your feedback or report a bug?

- open an issue on this
  project's [issue tracker](https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/issues) to report bugs
  or request new features
- if you are a translator and want to help out with l10n or submit some corrections but you don't
  feel confident with repository forks, pull requests, managing resource files, etc. feel free to
  drop an email or contact the maintainers in any way.

Please remember: every contribution is welcome and everyone's opinion matters here. This is a
community project, open source, ad-free and free of charge, and it belongs to us all so don't be
afraid to get involved.

And don't forget every 🦝's motto: «Live Fast, Eat Trash» (for shortness L. F. E. T.).
