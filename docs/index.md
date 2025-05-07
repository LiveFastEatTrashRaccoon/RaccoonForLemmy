---
layout: default
---

Welcome to the homepage of Raccoon for Lemmy!

If you need some guidance about how to use the app, please have a look at
the [User manual](user_manual/main).

# Table of contents

- [project overview](#overview)
- [rationale](#rationale)
- [key app features](#key-app-features)
- [credits and acknowledgements](#credits-and-acknowledgements)
- [further reading](#further-reading)

# Overview

Raccoon is a mobile client for Lemmy, a federated aggregation and discussion platform. The
app is implemented as a Kotlin Multiplatform (KMP) project, mainly focusing on the Android platform.

The project was started as an exercise to play around with Kotlin Multiplatform and Compose
multiplatform and gradually grew as a fully functional client with several features.

# Rationale

There are several reasons why this project was started:

- experimenting and making some stress tests with a real world project Kotlin Multiplatform (KMP)
  and Compose multiplatform, in order to explore what could be achieved with multiplatform libraries
  and share as much code as possible in the `commonMain` source directory;
- offering a feature rich Lemmy client mainly aimed at "pro" users, i.e. users who
  are not content with just browsing the contents of the Fediverse, creating posts and
  answers but be able to customize the app and "feel at home" because of its theme, its localization
  and its behaviour that each user has chosen;
- digging deeper inside Lemmy's internals and understand better what it is like to work with a
  decentralized platform; we believe that the Lemmy project has a lot of potential and part of its
  success depends on users being able to enjoy the experience on robust and well-done clients.

In the Lemmy ecosystem for Android, there are many cross-platform alternatives either implemented
with Flutter or React, a couple of native options written in Java and just one native pure-Kotlin
application. We️ ❤️ Kotlin, we ❤️ Free and Open Source Software and we ❤️ native app development, so
there was a niche to be filled and we are trying our best to do so.

Another important factor which determined the creation of a new client was that different projects
had very interesting features but no one had them all at the same time. Lemmur first and Liftoff
then were great apps with a lot of advanced features (e.g. the possibility to explore all the
communities of an external instance in guest mode). Summit is another great project with unique
features like multi-communities, a high degree of customization and very good performance. Finally,
Thunder has a really appealing and polished UI. All these apps have been and continue to be sources
of inspiration and motivation for Raccoon, which aims at having all the good sides of them, in a
Kotlin open source app.

If you are wondering why the app is called like that, aren't raccoons just adorable? 🦝 Jokes aside,
Lemmy users used to refer to themselves as "lemmings" and the platform logo clearly recalls that
animal, so there was this tradition in the early days to use animals names for clients (some
examples being Lemmur, Jerboa and Fennec). The original developers of this app really liked raccoons
so they choose that. Raccoons are cunning animals that adapt to live in harsh conditions, finding a
way to survive even with what we consider "trash". They look like masked criminals but do not harm
anyone, they are lazy, chubby and cute.

As long as you are on this page, don't forget every raccoon's motto: «Live Fast, Eat Trash»
(abbreviated L.F.E.T.).

# Key app features

Here is a list of the most important features of the app:

- view post feed and comments with different listing and sort types;
- possibility to upvote and downvote (with optional and configurable swipe actions);
- community and user detail (with info about moderators/moderated communities);
- user profile with one's own posts, comments and saved items;
- inbox with replies, mentions and direct messages in the form of a chat;
- full-fledged explore section with different result types (all, posts, comments, user,
  communities);
- create and edit new posts (with optional images);
- cross-post contents to other communities;
- reply to post and comments (and edit replies);
- mark posts as read (even while scrolling) and hide read contents;
- seeing raw post/comment source;
- custom appearance (color scheme, fonts, text sizes, post layout, etc.);
- custom localization (independent of system settings);
- block users, communities and instances (the latter requires Lemmy >= 0.19);
- report post and comments to moderators;
- support for multiple accounts (and multiple instances) with account-specific settings;
- lazy scrolling (referred to as "zombie mode");
- explore all the communities on a given instance in guest mode (instance info);
- multi-community (community aggregation);
- report post and comments with a reason;
- view the moderation log;
- community moderation tools (examine and resolve reports, ban users, feature posts, block
  further comments from posts, mark comments as distinguished, remove posts/comments, review all
  created posts/comments, edit/create community);
- save posts and comments you are creating as drafts to edit them later;
- admin tools (purge users/posts/comments/communities, feature posts locally, hide/unhide
  communities);

Most clients for Lemmy currently offer the first points (with various degrees of completion),
whereas the last ones are trickier and less common, so they are directed to more demanding users,
who like to explore the Lemmy ecosystem, play around with settings and fine-tune their client to
their needs.

Concerning customization, the ability to change some aspects like font face or size and app
colors, vote format, bar transparency and so on was of paramount importance from the very beginning.
Similarly, users should be able to use the app in their native language and change the UI language
independently from the system language, so localization is a first-class citizen in this project
too.

For moderators, it is also nice to be able to moderate content from your mobile device instead of
using the web interface, and in the beginning moderation tools were rare among mobile clients.
This app tried to bridge this gap and offer moderation tools (feature post, lock post, distinguish
comment, remove post/comment, ban users and the ability to revert any of these actions).

# Credits and acknowledgements

## Credits

A saying from the original developer:
<blockquote>
«Whenever in doubt, anguish or uncertainty, look at the code
of Jerboa for Lemmy».
</blockquote>

It is without any doubt that this project has a gratitude debt
towards [Jerboa](https://github.com/dessalines/jerboa), mainly in two crucial aspects of the app
such as comment processing to reconstruct the tree with missing nodes, where the memoized algorithm
is inspired by Jerboa's.

The UI is inspired (in principle, rather than in actual implementation) on the
great [Thunder](https://github.com/thunder-app/thunder) app.

The ideas for some of the features come from [Liftoff](https://github.com/liftoff-app/liftoff), e.g.
the guest mode, while some other come from [Summit](https://github.com/idunnololz/summit-for-lemmy),
which is again both source of inspiration and admiration.

Towards all the authors and contributors of these projects, a great "thank you" 🙏️🙏️🙏️

## Acknowledgements

A special thanks goes to all those who contributed so far (in nearly chronological order):

- [u/rb_c](https://discuss.tchncs.de/u/rb_c)
- [u/heyazorin](https://lemmy.ml/u/heyazorin)
- [u/thegiddystitcher](https://lemm.ee/u/thegiddystitcher)
- [u/SgtAStrawberry](https://lemmy.world/u/SgtAStrawberry)
- [outerair](https://hosted.weblate.org/user/outer_air)
- [u/Wild_Mastic](https://lemmy.world/u/Wild_Mastic)
- [reusityback](https://github.com/reusityback)
- [starry-shivam](https://github.com/starry-shivam)
- [u/fisco](https://lemmy.ml/u/fisco)
- [u/Suoko](https://feddit.it/u/Suoko)
- [u/Mannivu](https://feddit.it/u/Mannivu)
- [u/Kir](https://feddit.it/u/Kir)
- [u/NicKoehler](https://feddit.it/u/NicKoehler)
- [Tmpod](https://github.com/Tmpod)
- [u/cloudless](https://lemmy.cafe/u/cloudless)
- [u/squirrel](https://discuss.tchncs.de/u/squirrel)
- [N7-X](https://github.com/N7-X)
- [gallegonovato](https://hosted.weblate.org/user/gallegonovato)
- [Edanas](https://hosted.weblate.org/user/Edanas)
- [u/sag](https://lemm.ee/u/sag)
- [u/am41](https://sh.itjust.works/u/am41)
- [aindriu80](https://github.com/aindriu80)
- [monstorix](https://hosted.weblate.org/user/monstorix)
- [gnu-ewm](https://hosted.weblate.org/user/gnu-ewm)
- [beyanibash](https://hosted.weblate.org/user/beyanibash)
- [Tigg](https://hosted.weblate.org/user/tigg)
- [Matth7878](https://github.com/Matth7878)
- [TamilNeram](https://github.com/TamilNeram)
- [pHama](https://hosted.weblate.org/user/pHama)
- [Coool](https://github.com/Coool)
- all those who reported feedback and ideas through the Lemmy community, GitHub issues, emails,
  private messages, homing pigeons and every other imaginable media.

This project would not be what it is were it not for the huge amount of patience and dedication of
these early adopters who sent me continuous feedback and ideas for improvement after every release,
reported bugs, offered to help, submitted translations to their local language, offered help to
other users and made the community a better place, etc.

You are **awesome**… THANKS ❤️🦝️❤️

# Further reading

If what you have read so far sounds interesting and you want to know more, here are some useful
links:

- check out the [User manual](user_manual/main);
- have a look at the more detailed documentation in the [Technical manual](tech_manual/main);
- consult
  the [CONTRIBUTING](https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/blob/master/CONTRIBUTING.md)
  guide.

If, on the other hand, you just want to interact with the community, provide suggestions, report
bugs, contribute with translations or tell your opinion, you can:

- subscribe to our [Lemmy community](https://lemmy.world/c/raccoonforlemmyapp) to receive updates
  about the new releases, participate into public discussions in the Lemmy style and provide your
  feedback or suggestions;
- join the [Matrix room](https://matrix.to/#/#raccoonforlemmyapp:matrix.org) of this project to
  reach out to the developers and other users;
- use the GitHub [issue tracker](https://github.com/LiveFastEatTrashRaccoon/RaccoonForLemmy/issues)
  to report
  bugs or request features;
- finally, if none of the above methods fits your needs you
  can write an [email](mailto://livefast.eattrash.raccoon@gmail.com) or reach out to me
  on [Matrix](https://matrix.to/#/@dieguitux8623:matrix.org).
