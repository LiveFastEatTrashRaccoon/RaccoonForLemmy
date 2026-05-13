# Technical manual

This page contains a series of technical notes that can be useful for those who want to contribute
to the development of the app or simply become more familiar with the project structure.

## Module structure

The project has different kinds of modules and, depending on the group a module belongs to, there are some rules about
which other modules it can depend on.

Here is a description of the dependency flow:

- `:androidApp` which is the KMP equivalent of `:app` module in Android-only projects) include `:shared` and can
  include `:core` modules (e.g. for navigation);
- `:shared` is the heart of the KMP application and it virtually includes every other Gradle module as a dependency (it
  contains in the `DiHelper.kt` files the setup of the DI, so it basically needs to see all the
  modules);
- `:feature` modules are included by :shared and include :domain, :core and :unit modules, but they DO not include other
  each other nor any top level module; some unit modules are used just by one feature (e.g. `:unit:postlist` is used
  only by `:feature:home`) in some other cases multiple features use the same unit (e.g. `:unit:zoomableimage` is used
  by both `:feature:home`, `:feature:search`, `:feature:profile` and `:feature:inbox`);
- `:domain` modules can be used by feature and unit modules and can only include core modules; only exception
  is `:domain:inbox` which is a thin layer on top of `:domain:lemmy` so it depends on it (for inbox related functions);
- `:unit` modules are included by feature modules (and `:shared`) and sometimes by other unit modules in case of highly
  reusable parts of the app; the only notable violation to this rule is `:core:commonui:detailopener-impl` which is a
  special module because it is only included by `:shared` (which does the binding between `:detailopener-api`
  and `:detailopener-impl`) and it includes some unit modules but the fact of a unit module included by a core module in
  general should never happen (instead, the reverse is perfectly ok);
- `:core` modules can sometimes include each other (but without cycles, e.g. `:core:markdown`
  includes `:core:commonui:components` / `:core:utils` because it is a mid-level module and something similar happens
  with `:core:persistecnce` which uses `:core:preferences` / `:core:appearance`)  and nothing else; they are in turn
  used by all the other types of modules.

### Top-level modules

The main module (Android-specific) is `:androidApp`, which contains the Application subclass (`MainApplication`) and the
main activity (`MainActivity`). The latter in its `onCreate(Bundle?)` invokes the `MainView` Composable function which
in turns calls `App`, the main entry point of the multiplatform application which is defined in the `:shared` module.

`:shared` is the top module of the multiplatform application, which includes all the other modules and is not included
by anything (except `:androidApp`). In its `commonMain` source set, this module contains `App`, the application entry
point, the definition on the `MainScreen` (and its ViewModel) hosting the main navigation with the bottom tab bar.

It also contains the dependency injection setup as well as some DI modules specific to access
resources or other application-level components.

### Feature modules

These modules correspond to the main functions of the application, i.e. the sections of the main bottom navigation. In
particular:

- `:feature:home` contains the post list tab;
- `:feature:search` contains the Explore tab;
- `:feature:inbox` contains the Inbox tab;
- `:feature:profile` contains the Profile tab;
- `:feature:settings` contains the Settings tab.

### Domain modules

These are purely business logic modules that can be reused to provide application main parts:

- `:domain:identity` contains the repositories and use cases that are related to user identity, authorization and API
  configuration;
- `:domain:lemmy` contains all the Lemmy API interaction logic and is divided into two submodules:
    - `:data` contains all the domain models for Lemmy entities (posts, comments, communities, users, etc);
    - `:pagination` contains the pagination utilities for posts within feeds or comments as well as the navigation logic
      that allows to navigate to previous/next post within a list with automatic pagination;
    - `:repository` contains the repositories that access Lemmy APIs (through the :core:api module) and are used
      to manage the entities contained in the `:data` module;
- `:domain:inbox` contains some uses cases needed to interact with the replies, mentions and private messages
  repositories and coordinate the interaction between inbox-related app components.

### Unit modules

These modules are the building blocks that are used to create user-visible parts of the application, i.e. the various
screens, some of which are reusable in multiple points (e.g. the user detail, community detail or post detail, but also
report/post/comment creation forms, etc.). In some cases even a dialog or a bottom-sheet can become a "unit", especially
if it is used in multiple points or contains a little more than pure UI (e.g. some presentation logic); simple pure-UI
dialogs and sheets are located in the `:core:commonui:modals` module instead (but are being progressively converted to
separate units).

Here is a list of the main unit modules and their purpose:

- `:unit:about` contains the About this app dialog
- `:unit:accountsettings` contains the screen of the remote account settings (web preferences)
- `:unit:ban` contains the modal bottom sheet used to ban a user from a community
- `:unit:chat` contains the chat conversation screen
- `:unit:choosecolor` contains the dialogs and bottom sheets used for theme/color selection
- `:unit:communitydetail` contains the community detail screen
- `:unit:communityinfo` contains the community info bottom sheet accessible from community detail
- `:unit:configureswipeactions` contains the screen and bottom sheets to configure swipe actions
- `:unit:configurecontentview` contains the screen used to configure post and comment appearance
- `:unit:createcomment` contains the create comment form
- `:unit:createpost` contains the create post form
- `:unit:drafts` contains the screen uses to display post and comment drafts
- `:unit:drawer` contains the navigation drawer
- `:unit:filteredcontents` contains the screen to access moderated contents, liked/disliked items and bookmarks
- `:unit:instanceinfo` contains the instance info bottom sheet with the list of communities
- `:unit:login` contains the login modal bottom sheet
- `:unit:manageaccounts` contains the modal bottom sheet used to change account
- `:unit:manageban` contains the ban management screen
- `:unit:managesubscriptions` contains the subscription management screen
- `:unit:mentions` contains the mentions section of the Inbox tab
- `:unit:messages` contains the private messages section of the Inbox tab
- `:unit:moderatewithreason` contains the modal bottom sheet for moderation actions that require a reason (remove,
  purge, report, hide)
- `:unit:modlog` contains the moderation log screen
- `:unit:multicommunity` contains the multi-community detail and multi-community editor screens
- `:unit:myaccount` contains the profile logged section of the Profile tab
- `:unit:postdetail` contains the post detail screen
- `:unit:postlist` contains the post list (home) screen
- `:unit:rawcontent` contains the "Raw content" dialog with the custom (Android) text toolbar
- `:unit:replies` contains the replies section of the Inbox tab
- `:unit:reportlist` contains the report list screen (for moderators)
- `:unit:selectcommunity` contains the dialog used to select communities (for cross-posts)
- `:unit:selectinstance` contains the bottom sheet used to change instance in anonymous mode
- `:unit:userdetail` contains the user detail screen
- `:unit:userinfo` contains the user information bottom sheet accessible from user detail
- `:unit:web` contains the internal WebView screen
- `:unit:zoomableimage` contains the image detail screen used to display full-screen images.

### Core modules

These are the foundational blocks containing the design system and various reusable utilities that are called throughout
the whole project. Here is a short description of them:

- `:core:api` contains the services used to interact with Lemmy APIs and all the data transfer objects (DTOs)
  used to send and receive data from the APIs;
- `:core:appearance` contains the look and feel repository which exposes the information about the current theme as
  observable states and allows to change them;
- `:core:architecture` contains the building blocks for the Model-View-Intent architecture used in all the screens of
  the application;
- `:core:commonui` contains a series of submodules that are used to define UI components used in the app and reusable
  UI blocks:
    - `:components`: a collection of components that represent graphical widgets
    - `:detailopener-api` : a utility module used to expose an API to centralize content opening (post detail,
      community, detail, user detail, comment creation and post creation)
    - `:detailopener-impl`: implementation of the detail opener, this is an exception to the module architecture because
      it is a core module which includes unit modules so the important thing is
      that no one **ever** include this module except for `:shared`;
    - `:lemmyui`: graphical components used to represent Lemmy UI (posts, comments, inbox items, etc.) and reusable
      subcomponents such as different types of headers, footers, cards, etc.
    - `:modals`: definition of modal bottom sheets and dialogs that have no presentation logic. This module was
      historically much bigger and over time components were migrated to separate units
      modules;
- `:core:markdown` contains Markdown rendering logic;
- `core:l10n` contains the `L10nManager` interface which acts as a wrapper around loading the
  internationalized messages and/or making them available to the rest of the app;
- `:core:navigation` contains the navigation manager used for stack navigation, bottom sheet navigation and a
  coordinator for the events originated by the navigation drawer;
- `:core:notifications` contains the `NotificationCenter` contract and implementation as well as the event definition,
  this is used as an event bus throughout the whole project;
- `:core:persistence` contains the local database (primary storage) management logic as well as SQLDelight definitions
  of entities and migrations, plus all the local data sources that are used
  to access the database;
- `:core:preferences` contains the shared preferences/user defaults (secondary storage) and relies on the
  multiplatform-settings library to offer a temporary key-value store;
- `:core:resources` is a wrapper around the resource loading (fonts and images mainly) which used to rely on an external
  library and now used the built-in resource management of Compose;
- `:core:utils`: contains a series of helper and utility functions/classes that are used in the project but were not big
  enough to be converted to separate domain/core modules on their own.

On second thoughts:

- `:core:commonui` has still too much in it, especially `:modals` packages should become unit modules;
- `:core:persistence` belongs more to domain modules, e.g. `:domain:accounts`/`:domain:settings` but it is implemented
  as a core module because it is strongly tied to SQLDelight and its generated code which provides the named queries to
  fetch/save data to the local DB.


## Project structure

This is a Gradle project, it is setup to download a Gradle distribution and resolve dependencies
according to the definitions contained in the `gradle/libs.versions.toml` file.

Also, please note that in the `settings.gradle.kts` file we are using the option:

```
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

to reference Gradle subprojects in each `build.gradle.kts` with a type safe notation
e.g. `implementation(projects.core.utils)`.


## Tech stack

Here is a list of the technologies used in the project, with a short historical explanation in the
cases where the initial choice changed over time and the reasons why the change was made.

<dl>
<dt>Dependency Injection</dt>
<dd>
The choice here is the <a href="https://github.com/kosi-libs/Kodein">Kodein</a> library. The main
reason it was selected because of its great multiplatform support and the integration with the
navigation library (which at the beginning of the project was not there yet, but was added later and
proved to work great). You can find module definitions (beware, Gradle modules and DI modules are
two different concepts and should not be confused) in a `di` package inside each subproject, modules
can include each other and all top-level modules are included in the shared module, more on it in
"Module overview and dependencies".
Initially the project started using another very popular DI library,
<a href="https://github.com/InsertKoinIO/koin">Koin</a>, but after discovering that, if you use their
annotation processor to generate modules by reading annotations, builds are not reproducible any
more (which is written <b>nowhere</b> in the documentation), I decided to make a U-turn and move to a more
reliable library and I don't want to hear about Koin ever (ever!) again in my life as a developer.
</dd>

<dt>Navigation</dt>
<dd>
Yhe project started with the <a href="https://github.com/adrielcafe/voyager">Voyager</a> library.
This choice was driven by its multi-platform support, its very practical approach
and ease to set up, compared to other multi-platform libraries (like Decompose) that were available
in 2023. Nonetheless, and this lesson was learned the hard way, easiness of use and
compactness mean that things will go smooth in the future, and as the project grew the navigation
library started to show its limits. Part of them were addressed by encapsulating the transition
logic (to push/pop screens into the navigation stack and open/close modal bottom sheets) into a
centralized <code>NavigationCoordinator</code>.
Something similar was done for the navigation drawer in <code>DrawerCoordinator</code>.
Even the DI integration was not totally pain-free, and the <code>:core:navigation</code> module
started accumulating more and more "glue code" to work around some of the issues that were
encountered (especially for deep links). After JetBrains ported AndroidX navigation to Compose
Multiplatform, I took the opportunity to migrate the whole project to it and abandon Voyager in
favor of a more stardard solution.
</dd>

<dt>Networking</dt>
<dd>Here, at least for Android developers, no surprises: the choice was <a href="https://github.com/ktorio/ktor">Ktor</a>
which has great multiplatform support.
</dd>

<dt>Resource management</dt>
<dd>
Initially the project used the <a href="https://github.com/icerockdev/moko-resources">Moko resources</a>
library to load fonts, icons and all the localized messages used in the UI. It worked great, since in those areas
Compose multiplatform missed the needed functionalities. But as long as the project grew in size and
more complex KSP configurations were needed, having all modules depend on resources became unmaintainable.
This is why I migrated drawable and font loading to Compose built-in system for all kinds of resources.
For localization, after using Moko resources, for some period the project used the
<a href="https://github.com/adrielcafe/lyricist">Lyricist</a> library, which offered flexibility but
at the price of either using XML processors (which do not play well with reproducible builds) to
generate resource files or using non-standard resource formats (e.g. Kotlin files) which are not
easily recognized by third-party translation platforms such as
<a href="https://hosted.weblate.org/engage/raccoonforlemmy/">Weblate</a>.
</dd>

<dt>Image loading</dt>
<dd>
This was something that was expected to be simpler but unfortunately it wasn't. In the beginning
the project used the <a href="https://github.com/Kamel-Media/Kamel">Kamel</a> library
which had a major bug while rendering large images, which took a long time to be considered.
The project was already relying on Kamel for many things, from loading images on demand
to Markdown rendering, so deciding to switch was not easy at all. So as a first step,
<a href="https://github.com/coil-kt/coil">Coil</a> 2.x was adopted on Android while keeping Kamel
on iOS and, finally, when Coil 3.x became stable with multiplatform support all the project has
been completely migrated.
</dd>

<dt>Preference storage</dt>
<dd>
Here the choice was the <a href="https://github.com/russhwolf/multiplatform-settings">Multiplatform settings</a>
libary which not only works great but also offers support for a lot of customization (if you are wondering,
I am aware that <code>EncryptedSharedPreferences</code> is going to be deprecated in the next version
of <code>androidx.security:security-crypto</code> and I am working on a replacement for it).
</dd>

<dt>Primary persistence</dt>
<dd>
This project was a chance to experiment with <a href="https://github.com/cashapp/sqldelight">SQLDelight</a>
(in other multiplatform projects other libraries were tested like Exposed), whereas database encryption
is obtained through <a href="https://www.zetetic.net/sqlcipher/sqlcipher-for-android">SQLCipher Android</a>,
formerly <a href="https://github.com/sqlcipher/android-database-sqlcipher">Android Database SQLCipher</a>.
Since over time Room has become stable even for multiplatform, there is "room" for improvement in the
future to migrate the project to using it, because SQLDelight is a third-party library whereas Room is
the officially recommended and Google-backed persistence solution for KMP apps.
</dd>

<dt>Markdown rendering</dt>
<dd>
This was another part, like image loading, where KMP was at the beginning quite poor. After having given
up for some time and used Markwon (Java + Views) on the Android part of the app, I decided to give a
second chance to <a href="https://github.com/mikepenz/multiplatform-markdown-renderer">Multiplatform
Markdown Renderer</a> which was initially user for the multiplatform source set. The project grew
and matured over time and it made it possible to add custom handlers (like modular plug-ins) which
made possible to support Lemmy's custom features like spoilers. The migration from multiplatform
renderer to Markwon and back to multiplatform renderer was not easy at all, but this project is about
KMP so, as a consequence, a pure Kotlin and pure Compose solution <em>had to</em> be preferred, even
if it implies to sacrifice some functionality for the time being.
</dd>

<dt>Video playback</dt>
<dd>
The initial choice was to write a custom native implementation based on <code>Exoplayer</code> on Android
and on <code>AVPlayer</code> on iOS. This solution kind of worked but had some issues and with later
updates of Compose Multiplatform it was using deprecated functions on iOS. This is why the video player
was eventually migrated to use the
<a href="https://github.com/Chaintech-Network/ComposeMultiplatformMediaPlayer">ComposeMultiplatformMediaPlayer</a>
library.
</dd>

<dt>Theme generation</dt>
<dd>
The application allows to select a custom color and generate a Material 3 color scheme as a
palette originate from that seed color. This is achieved by using the
<a href="https://github.com/jordond/MaterialKolor">MaterialKolor</a> library which was designed to
be multiplatform and works as a charm for the purpose. Thanks!
</dd>

<dt>Reorderable lists</dt>
<dd>
The ability to reorder lists is achieved thanks to the <a href="https://github.com/Calvin-LL/Reorderable">Reorderable</a>
library which starting from version 1.3.1 has become multiplatform. This functionality is still
experimental and is used only in the instance selection bottom sheet for anonymous users.
</dd>

<dt>Web view</dt>
<dd>
Initially a custom web view was implemented, relying on native views (<code>WebView</code> on Android
and <code>WKWebView</code> on iOS), but in the end the project was migrated to a component relying on
the <a href="https://github.com/MohamedRejeb/Calf">Calf</a> library to display portions of the Web.
</dd>
</dl>


## Module structure

The project has different kinds of modules and, depending on the group a module belongs to, there are some rules about
which other modules it can depend on.

Here is a description of the dependency flow:

- `:androidApp` which is the KMP equivalent of `:app` module in Android-only projects) include `:shared` and can
  include `:core` modules (e.g. for navigation);
- `:shared` is the heart of the KMP application and it virtually includes every other Gradle module as a dependency (it
  contains in the `DiHelper.kt` files the setup of the DI, so it basically needs to see all the
  modules);
- `:feature` modules are included by :shared and include :domain, :core and :unit modules, but they DO not include other
  each other nor any top level module; some unit modules are used just by one feature (e.g. `:unit:postlist` is used
  only by `:feature:home`) in some other cases multiple features use the same unit (e.g. `:unit:zoomableimage` is used
  by both `:feature:home`, `:feature:search`, `:feature:profile` and `:feature:inbox`);
- `:domain` modules can be used by feature and unit modules and can only include core modules; only exception
  is `:domain:inbox` which is a thin layer on top of `:domain:lemmy` so it depends on it (for inbox related functions);
- `:unit` modules are included by feature modules (and `:shared`) and sometimes by other unit modules in case of highly
  reusable parts of the app; the only notable violation to this rule is `:core:commonui:detailopener-impl` which is a
  special module because it is only included by `:shared` (which does the binding between `:detailopener-api`
  and `:detailopener-impl`) and it includes some unit modules but the fact of a unit module included by a core module in
  general should never happen (instead, the reverse is perfectly ok);
- `:core` modules can sometimes include each other (but without cycles, e.g. `:core:markdown`
  includes `:core:commonui:components` / `:core:utils` because it is a mid-level module and something similar happens
  with `:core:persistecnce` which uses `:core:preferences` / `:core:appearance`)  and nothing else; they are in turn
  used by all the other types of modules.

### Top-level modules

The main module (Android-specific) is `:androidApp`, which contains the Application subclass (`MainApplication`) and the
main activity (`MainActivity`). The latter in its `onCreate(Bundle?)` invokes the `MainView` Composable function which
in turns calls `App`, the main entry point of the multiplatform application which is defined in the `:shared` module.

`:shared` is the top module of the multiplatform application, which includes all the other modules and is not included
by anything (except `:androidApp`). In its `commonMain` source set, this module contains `App`, the application entry
point, the definition on the `MainScreen` (and its ViewModel) hosting the main navigation with the bottom tab bar.

It also contains the dependency injection setup as well as some DI modules specific to access
resources or other application-level components.

### Feature modules

These modules correspond to the main functions of the application, i.e. the sections of the main bottom navigation. In
particular:

- `:feature:home` contains the post list tab;
- `:feature:search` contains the Explore tab;
- `:feature:inbox` contains the Inbox tab;
- `:feature:profile` contains the Profile tab;
- `:feature:settings` contains the Settings tab.

### Domain modules

These are purely business logic modules that can be reused to provide application main parts:

- `:domain:identity` contains the repositories and use cases that are related to user identity, authorization and API
  configuration;
- `:domain:lemmy` contains all the Lemmy API interaction logic and is divided into two submodules:
    - `:data` contains all the domain models for Lemmy entities (posts, comments, communities, users, etc);
    - `:pagination` contains the pagination utilities for posts within feeds or comments as well as the navigation logic
      that allows to navigate to previous/next post within a list with automatic pagination;
    - `:repository` contains the repositories that access Lemmy APIs (through the :core:api module) and are used
      to manage the entities contained in the `:data` module;
- `:domain:inbox` contains some uses cases needed to interact with the replies, mentions and private messages
  repositories and coordinate the interaction between inbox-related app components.

### Unit modules

These modules are the building blocks that are used to create user-visible parts of the application, i.e. the various
screens, some of which are reusable in multiple points (e.g. the user detail, community detail or post detail, but also
report/post/comment creation forms, etc.). In some cases even a dialog or a bottom-sheet can become a "unit", especially
if it is used in multiple points or contains a little more than pure UI (e.g. some presentation logic); simple pure-UI
dialogs and sheets are located in the `:core:commonui:modals` module instead (but are being progressively converted to
separate units).

Here is a list of the main unit modules and their purpose:

- `:unit:about` contains the About this app dialog
- `:unit:accountsettings` contains the screen of the remote account settings (web preferences)
- `:unit:ban` contains the modal bottom sheet used to ban a user from a community
- `:unit:chat` contains the chat conversation screen
- `:unit:choosecolor` contains the dialogs and bottom sheets used for theme/color selection
- `:unit:communitydetail` contains the community detail screen
- `:unit:communityinfo` contains the community info bottom sheet accessible from community detail
- `:unit:configureswipeactions` contains the screen and bottom sheets to configure swipe actions
- `:unit:configurecontentview` contains the screen used to configure post and comment appearance
- `:unit:createcomment` contains the create comment form
- `:unit:createpost` contains the create post form
- `:unit:drafts` contains the screen uses to display post and comment drafts
- `:unit:drawer` contains the navigation drawer
- `:unit:filteredcontents` contains the screen to access moderated contents, liked/disliked items and bookmarks
- `:unit:instanceinfo` contains the instance info bottom sheet with the list of communities
- `:unit:login` contains the login modal bottom sheet
- `:unit:manageaccounts` contains the modal bottom sheet used to change account
- `:unit:manageban` contains the ban management screen
- `:unit:managesubscriptions` contains the subscription management screen
- `:unit:mentions` contains the mentions section of the Inbox tab
- `:unit:messages` contains the private messages section of the Inbox tab
- `:unit:moderatewithreason` contains the modal bottom sheet for moderation actions that require a reason (remove,
  purge, report, hide)
- `:unit:modlog` contains the moderation log screen
- `:unit:multicommunity` contains the multi-community detail and multi-community editor screens
- `:unit:myaccount` contains the profile logged section of the Profile tab
- `:unit:postdetail` contains the post detail screen
- `:unit:postlist` contains the post list (home) screen
- `:unit:rawcontent` contains the "Raw content" dialog with the custom (Android) text toolbar
- `:unit:replies` contains the replies section of the Inbox tab
- `:unit:reportlist` contains the report list screen (for moderators)
- `:unit:selectcommunity` contains the dialog used to select communities (for cross-posts)
- `:unit:selectinstance` contains the bottom sheet used to change instance in anonymous mode
- `:unit:userdetail` contains the user detail screen
- `:unit:userinfo` contains the user information bottom sheet accessible from user detail
- `:unit:web` contains the internal WebView screen
- `:unit:zoomableimage` contains the image detail screen used to display full-screen images.

### Core modules

These are the foundational blocks containing the design system and various reusable utilities that are called throughout
the whole project. Here is a short description of them:

- `:core:api` contains the services used to interact with Lemmy APIs and all the data transfer objects (DTOs)
  used to send and receive data from the APIs;
- `:core:appearance` contains the look and feel repository which exposes the information about the current theme as
  observable states and allows to change them;
- `:core:architecture` contains the building blocks for the Model-View-Intent architecture used in all the screens of
  the application;
- `:core:commonui` contains a series of submodules that are used to define UI components used in the app and reusable
  UI blocks:
    - `:components`: a collection of components that represent graphical widgets
    - `:detailopener-api` : a utility module used to expose an API to centralize content opening (post detail,
      community, detail, user detail, comment creation and post creation)
    - `:detailopener-impl`: implementation of the detail opener, this is an exception to the module architecture because
      it is a core module which includes unit modules so the important thing is
      that no one **ever** include this module except for `:shared`;
    - `:lemmyui`: graphical components used to represent Lemmy UI (posts, comments, inbox items, etc.) and reusable
      subcomponents such as different types of headers, footers, cards, etc.
    - `:modals`: definition of modal bottom sheets and dialogs that have no presentation logic. This module was
      historically much bigger and over time components were migrated to separate units
      modules;
- `:core:markdown` contains Markdown rendering logic;
- `core:l10n` contains the `L10nManager` interface which acts as a wrapper around loading the
  internationalized messages and/or making them available to the rest of the app;
- `:core:navigation` contains the navigation manager used for stack navigation, bottom sheet navigation and a
  coordinator for the events originated by the navigation drawer;
- `:core:notifications` contains the `NotificationCenter` contract and implementation as well as the event definition,
  this is used as an event bus throughout the whole project;
- `:core:persistence` contains the local database (primary storage) management logic as well as SQLDelight definitions
  of entities and migrations, plus all the local data sources that are used
  to access the database;
- `:core:preferences` contains the shared preferences/user defaults (secondary storage) and relies on the
  multiplatform-settings library to offer a temporary key-value store;
- `:core:resources` is a wrapper around the resource loading (fonts and images mainly) which used to rely on an external
  library and now used the built-in resource management of Compose;
- `:core:utils`: contains a series of helper and utility functions/classes that are used in the project but were not big
  enough to be converted to separate domain/core modules on their own.

On second thoughts:

- `:core:commonui` has still too much in it, especially `:modals` packages should become unit modules;
- `:core:persistence` belongs more to domain modules, e.g. `:domain:accounts`/`:domain:settings` but it is implemented
  as a core module because it is strongly tied to SQLDelight and its generated code which provides the named queries to
  fetch/save data to the local DB.

