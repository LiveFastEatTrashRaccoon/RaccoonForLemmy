<div align="center">
  <img src="https://img.shields.io/badge/Kotlin-2.0.0-7F52FF?logo=kotlin" />
  <img src="https://img.shields.io/badge/Gradle-8.8-02303A?logo=gradle" />
  <img src="https://img.shields.io/badge/Android-26+-34A853?logo=android" />
  <img src="https://img.shields.io/badge/Compose-1.6.7-4285F4?logo=jetpackcompose" />
</div>
This is an archive of the Raccoon For Lemmy project.  The package has been renamed, and all broken linked content <em>should</em> now be fixed.

Below is a slightly modified version of the original README. 
<br />

<div align="center">
  <img src="https://raw.githubusercontent.com/N7-X/RaccoonForLemmy/master/androidApp/src/main/ic_launcher-playstore.png" width="250" height="auto" />
</div>

# Raccoon for Lemmy

A Kotlin Multiplatform client for Lemmy (mainly Android).

Raccoon is a client for the federated aggregation and discussion platform Lemmy.

The project started as an exercise to play around with Kotlin Multiplatform (KMP) and Compose
multiplatform and gradually grew as a fully functional client with many features.

Have a look at the User manual
for a more detailed explanation or
the Technical manual
for some technical notes.

## Main features

- view post feed and comments with different listing and sort types;
- possibility to upvote and downvote (with configurable swipe actions);
- community and user detail (with info about moderators/moderated communities);
- review your posts and comments (created by you, bookmarked, liked/disliked);
- inbox with replies, mentions and direct messages;
- global search with different result types (all, posts, comments, user, communities);
- create and edit new posts (with optional images);
- cross-post contents to other communities;
- reply to post and comments (and edit replies);
- mark posts as read (even while scrolling) and hide read contents;
- custom appearance (color scheme, fonts, text sizes, post layout, etc.);
- custom localization (independent of system settings);
- block users, communities and instances (the latter requires Lemmy >= 0.19);
- report post and comments to moderators with a reason;
- support for multiple accounts (and multiple instances) with account-specific settings;
- lazy scrolling (referred to as "zombie mode");
- explore all the communities on a given instance in guest mode;
- multi-community (community aggregation);
- view the moderation log;
- community moderation tools (examine and resolve reports, ban users, feature posts, block
  further comments from posts, mark comments as distinguished, remove posts/comments, review all
  created posts/comments, edit/create community)
  in your communities);
- save posts and comments you are creating as drafts to edit them later;
- admin tools (purge users/posts/comments/communities, feature posts locally, hide/unhide
  communities);

Most clients for Lemmy currently offer the first points (with various degrees of completion), so
there is nothing special about Raccoon, whereas the last ones are less common and are directed to
more demanding users.

Concerning customization, the ability to change some aspects like font face or size and app colors,
vote format, bar
transparency and so on was of paramount importance from the very beginning. Similarly, users should
be able to use the
app in their native language and change the UI language independently of the system language.

This app is also intended for moderators who want to use their mobile device, offering moderation
tools (feature post,
lock post, distinguish comment, remove post/comment, ban users) and the ability to revert any of
these actions.

The project is under active development, so expect new features to be added over time. Have a look
on the issues labeled
with "feature" in the issue tracker to get an idea of what's going to come next.

If you have ideas, feedback, suggestions or comments remember to speak up and use your voice. You
can add reports or
request features and they will be considered.

## Why was the project started?

Because raccoons are so adorable, aren't they? 🦝🦝🦝

Joking apart, one of the main goals was to experiment with KMP and learn how to properly deal with
the challenges of a
multiplatform environment, and a medium-sized project like this was an ideal testing ground for that
technology.

Secondly, I felt that the Android ecosystem of Lemmy apps was a little "poor" with few native apps (
fewer open source),
while the "market" is dominated by iOS and cross-platform clients. I️ ❤️ Kotlin, I ❤️ Free and Open
Source Software and
I ❤️ native app development, so there was a niche that could be filled.

Developing a new client was an opportunity to add all the good features that were "scattered" across
different apps,
e.g. the feature richness of [Liftoff](https://github.com/liftoff-app/liftoff), the
multi-community feature of
[Summit](https://github.com/idunnololz/summit-for-lemmy) and the polished UI of the really great
[Thunder](https://github.com/thunder-app/thunder) and so on. This app tries to be configurable
enough to make users feel "at home" and choose what they want, while at the same time having a not
too cluttered interface (except for the Settings screen - I know!)

In the third place, this app has been a means to dig deeper inside Lemmy's internals and become more
humble and patient
towards other apps because there are technical difficulties in having to deal with a platform like
Lemmy.

This involves a high level of discretion and personal taste, I know, but this project _is_ all about
experimenting and
learning.

## Technical notes:

The project uses the following technologies:

- [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
- [Voyager](https://github.com/adrielcafe/voyager) for screen navigation
- [Ktor](https://github.com/ktorio/ktor) and [Ktorfit](https://github.com/Foso/Ktorfit) for
  networking
- [Lyricist](https://github.com/adrielcafe/lyricist) for l10n
- [Multiplatform settings](https://github.com/russhwolf/multiplatform-settings) for encrypted
  preferences
- [SQLDelight](https://github.com/cashapp/sqldelight)
  and [SQLCipher](https://github.com/sqlcipher/sqlcipher) for local
  persistence
- [Multiplatform Markdown Renderer](https://github.com/mikepenz/multiplatform-markdown-renderer) for
  Markdown
  rendering

More info about the technologies used in the project can be found in
the CONTRIBUTING.md.

## Want to leave your feedback or report a bug?

- use the Matrix room to chat in real time with
  other team members;
- open an issue on this
  project's issue tracker to report bugs
  or request new features
- create a post on the project's community on
  Lemmy.world for broader questions, opinions, personal feedback, suggestions, insults or whatever
  you feel like writing
- if you are a translator and want to help out with l10n or submit some corrections, check out the
  project on Weblate, so you can review the
  existing messages or add missing ones.

Please remember: every contribution is welcome and everyone's opinion matters here. This is a
community project, open
source, ad-free and free of charge, and it belongs to us all so don't be afraid to get involved.

And don't forget every 🦝's motto: «Live Fast, Eat Trash» (abbreviated L.F.E.T.).
