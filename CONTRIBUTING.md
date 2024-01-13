# CONTRIBUTING.md aka «the trash Panda's guide to the galaxy»

## 0. Introduction

### 0.1 Welcome

First of all, a word of welcoming to whomever has landed on this page. If you are here, that means
that somehow you have found a link to this project over the Internet, you are possibly interested in
it (either positively or negatively) and you are wondering whether you can contribute in any way.

This is great news, so thank you for your time and dedication. Whether you spend 1 minute or 1 hour
here, it is really highly appreciated.

### 0.2 ToC

With no further ado, here is a short list of what you'll find here. Keep in mind that, like the
project in under continuous development and evolution (hopefully), so is this guide. New sections
will be added and new answers will be provided as long as questions are submitted.

- [section 1](#1-project-overview) will provide a general overview about the project and its
  contributors: who we are, what we are trying to achieve, why are we doing it and how.
- [section 2](#2-community) illustrates the core values that are behind this project, its key goals
  and an acknowledgement to all the people who helped us to achieve those targets.
- [section 3](#3-how-tos) is a collection of operative instructions for those who want to
  contribute, either reporting bugs, asking for new features, submitting feedback, contributing with
  code,
  documentation or translations
- [section 4](#4-setup-the-development-environment) is dedicated to developers wanting to build the
  project locally in order to submit pull requests
- [section 5](#5-project-structure) contains a technical illustration of the tech stack used in the
  project, the architectural design used in the various features and how the project is broken down
  into discrete components that interact with each other
- [section 6](#6-coding-conventions) contains the coding conventions that you should follow when
  submitting PRs, because readability and consistency matter and there are some rules (with
  exceptions) that should ideally be followed everywhere.

## 1 Project overview

### 1.1 Purpose

Raccoon for Lemmy was designed with three goals in mind: on the one side experiment and put to some
stress test with a real world project Kotlin Multiplatform (KMP) and Compose multiplatform, in order
to explore what could be achieved with multiplatform libraries and share as much code as possible
in the `commonMain` source directory. The Android ecosystem of Lemmy apps has many cross-platform
alternatives implemented with Flutter or React, a couple of native options written in Java and just
one native pure-Kotlin application. Raccoon intends to be an Android-first app, but
experimentation on other OSes given the multiplatform nature of its technologies are not
excluded _a priori_.

The second goal was to offer a feature rich Lemmy client mainly aimed at "pro" users, i.e. users who
are not content with just browsing the contents of the Fediverse, creating posts and
answers. This is why Raccoon tried to offer from the very beginning features like:

- a full-fledged explore section that allows multiple result types
- instance info, with the list of communities of foreign instances as well as the possibility to
  visit communities on foreign instances in guest mode
- multi-community, i.e. community aggregation
- moderation tools (for community moderators) and access to community moderation log.

Finally, the third goal was to offer a Lemmy experience where everyone could "feel at home" (see the
[Code of Conduct](#22-code-of-conduct) below). This implies keeping the interface lean by default
but having the possibility to customize a variety of aspects (theme, vote format, default feed/sort
type) and adopt the user's native language. As a consequence, leanness, personalization and
localization are first-class citizens in this project.

### 1.2 About the name

Lemmy users used to refer to themselves as "lemmings" and the platform logo clearly recalls that
animal, so there was this tradition in the early days to use animals names for clients or at least
the original developers of this app thought so (some examples being Jerboa and Fennec). And they
really liked raccoons so they choose that. Raccoons are cunning animals that adapt to live in harsh
conditions, finding a way to survive even with what we consider "trash". They look like masked
criminals but do not harm anyone, they are lazy and chubby and so cute in the end.

In this group, we often quote every raccoon's motto: «Live Fast, Eat Trash» (abbreviated LFET).

## 2 Community

### 2.1 About us

This section is probably going to change over time, anyway we are a small group of volunteers who in
their spare time contribute to the project.

You can find us on [this](https://matrix.to/#/#raccoonforlemmy:matrix.org) Matrix space, where you
can ask more questions in the General room or by writing directly to the administrator.

### 2.2 Code of Conduct

When interacting with other members of the community, be them end-users, developers, supporters or
whatever, remember that we are raccoons, and we follow these principles:

|   | Principle         | Explanation                                                                                                                                       |
|:-:|-------------------|---------------------------------------------------------------------------------------------------------------------------------------------------|
| R | **Respect**       | We respect each other as people, remembering we are part of a group that goes beyond individual opinions, beliefs, preferences and habits.        |
| A | **Availability**  | We support each other with the skills and available time that we have, considering that we are volunteers and we operate on a best-effort basis.  |
| C | **Commitment**    | We are responsible for our choices and we agree that our choices are taken wisely, always considering the impact on other members of the group.   |
| C | **Cohesion**      | We remember that we are part of a community which is bigger than the individual members, so we do unto others as we would have them do unto us.   |
| O | **Objectivity**   | We are committed to telling the truth in the most objective way and, if we express subjective opinions, to do it in a clear and constructive way. |
| O | **Originality**   | We bring our own personal experience and ideas which are acceptable even if "different": everyone's voice matters and deserves to be listened.    |
| N | **Never give up** | We are tenacious and are not afraid by technical challenges, we embrace every difficult task as an opportunity to learn and acquire new skills.   |

### 2.3 Acknowledgements

This project would not be what it is were it not for the huge amount of patience and dedication of
early adopters who sent me continuous feedback and ideas for improvement after every release,
reported bugs, offered to help, submitted translations to their local language, etc.

A special thank to all those who contributed so far (in nearly chronological order):

- [u/rb_c](https://discuss.tchncs.de/u/rb_c)
- [u/heyazorin](https://lemmy.ml/u/heyazorin)
- [u/thegiddystitcher](https://lemm.ee/u/thegiddystitcher)
- [u/SgtAStrawberry](https://lemmy.world/u/SgtAStrawberry)
- [outerair](https://github.com/outerair)
- [u/Wild_Mastic](https://lemmy.world/u/Wild_Mastic)
- all those who reported feedback and ideas through the Lemmy community, GitHub issues, emails,
  private messages, homing pidgeons and every other imaginable media.

## 3. HOW-TOs

### 3.1 General interactions

There are many ways you can interact with members of the community:

- use the [Matrix space](https://matrix.to/#/#raccoonforlemmy:matrix.org) to chat in real time with
  other team members, there are two rooms in the space: "General" is for general information about
  the app development and "Trashcan" is more for smalltalk and random topics.
- use the [Lemmy community](https://lemmy.world/c/raccoonforlemmy) to receive updates about the new
  releases, participate into public discussions in the Lemmy style and provide your feedback or even
  share your memes about raccoons with any other interested people
- use the [GitHub issue tracker](https://github.com/diegoberaldin/RaccoonForLemmy/issues) to report
  bugs or request features
- use the [GitHub discussion section](https://github.com/diegoberaldin/RaccoonForLemmy/discussions)
  for technical questions about the release roadmap, questions about app internationalization, etc.
- finally, if none of the above methods fits your needs you can write an email or send a private
  message to the original developer (my personal information are in the GitHub profile).

Always remember the guidelines contained in the [Code of Conduct](#22-code-of-conduct) to interact
with other people: our values are important and should be applied even with each other and not only
with end users.

### 3.2 Create bug reports

The preferred way to submit a bug report is
the [GitHub issue tracker](https://github.com/diegoberaldin/RaccoonForLemmy/issues).

- use the "New issue" button to open the creation form
- in the "Add a title" field give a synthetic description of the issue, if it affects a specific
  part of the application you can add in square brackets a prefix containing that information,
  e.g. `[post list]` or `post detail` or `[create report]`.
- in the "Add a description" field provide a more detailed explanation of the issue, you can use
  Markdown syntax (in the GitHub flavour) to format text here.
  An ideal bug report contains:
    - the precondition (sequence of steps that lead to the faulty condition, if it is
      deterministic)
    - a description of the error condition (with the expected result and the actual
      result, where the difference between the two is clear)
    - (if you can, optionally) some screenshots that make it clear where the error is
- you can add the "bug" label to the issue or otherwise leave it blank. Do not use the other
  fields (assignee, milestone, etc.) because they are used internally by the team members who will
  take the issue in charge during resolution.

### 3.3 Request features

As for bug reports, the preferred way for feature or change requests is
the [GitHub issue tracker](https://github.com/diegoberaldin/RaccoonForLemmy/issues).
In this case, you should:

- insert a short description in the title field, if the feature is specific to some app domain you
  can use the same square bracket prefix as in bug reports
- insert a more detailed explanation of your idea in the description field, if your feature is
  present in other apps you can provide screenshots or make references to make the request clearer
- you can use the "feature" label to tag the issue, or otherwise leave it blank and let the
  maintainers decide how to triage the issue.

### 3.4 Add a new localization

The preferred way for localizations (l10ns) is to submit a pull request (PR) as detailed in
the [next section](#35-submit-a-pull-request). The project uses
the [moko-resources](https://github.com/icerockdev/moko-resources) library for multiplatform access
to resource files, which for l10n implies having to deal with XMLs in the Android style.

You will have to create a new folder under the `resources/src/commonMain/resources/MR` directory
named after the locale you want to add (following IANA conventions for locales) and create
a `strings.xml` file in it, copying the contents
of [this file](https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/resources/src/commonMain/resources/MR/base/strings.xml)
except for the items that are marked as `translatable="false"` (you can safely remove them).

Modify
the [base](https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/resources/src/commonMain/resources/MR/base/strings.xml)
l10n to add the name of your language in the untranslatable set

```xml

<string name="language_XXX" translatable="false">YYY</string>
```

where XXX is the IANA locale code and YYY if the name you want to appear in the UI (use the endonym
of your language, i.e. the name of that language in the language itself).

After which you'll have to make sure the project compiles, because there are some syntax rules that
must be enforced in string files, e.g.:

- apostrophes (`'`) must be escaped with a backslash (`\'`)
- some special characters must be represented as XML entities so `>` becomes `&gt;`, `<`
  becomes `&lt;`, `&` becomes `&amps;` and so on… (talking of ellipsis, use `…` preferably instead
  of three dots)

If you want you can change the code in the following spots:

- add your flag and language name (mapping your language code to the values) in
  [Extensions.kt](https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/core/utils/src/commonMain/kotlin/com/github/diegoberaldin/raccoonforlemmy/core/utils/Extensions.kt)
- add an option (using your language code) in the `values` array
  in [LanguageBottomSheet.kt](https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/core/commonui/modals/src/commonMain/kotlin/com/github/diegoberaldin/raccoonforlemmy/core/commonui/modals/LanguageBottomSheet.kt)

That's it. You can test that everything works by launching the development app.

However, if you are not a developer and do not feel confident with GitHub's PR mechanism, you can
just download
the [base l10n](https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/resources/src/commonMain/resources/MR/base/strings.xml)
to your local machine, edit the file and send an email to the maintainers with the attachment, we
will take care of the rest.

If you have proposals, want to submit l10n fixes/improvements to existing ones, you can
use [this discussion](https://github.com/diegoberaldin/RaccoonForLemmy/discussions/378) and post an
answer, we will reach out to you and solve the issue.

### 3.5 Submit a pull request

First of all, please fork the repository by using the "Create a new fork" button in the GitHub
console, which will create a copy of the repository in your personal account.

Create a branch from the repository's master branch named after the feature/change you want to
submit, use whatever convention you like (preferably underscores to separate words but as you wish)
and make any commits you want on it.

Push the commits on the remote branch of your forked repository.

Use the "Compare and pull request" button to see a recap of the PR, after which use the "Create pull
request" to submit the request to the upstream repository.

## 4. Setup the development environment

This is a Kotlin Multiplatform (KMP) project that uses the Gradle build tool. The recommended
development environment is Android Studio with the Kotlin Multiplatform Mobile plugin installed.
S
ince the project is using Gradle 8.2.1 with the Android Gradle Plugin (AGP) version 8.2.1 you
should use Android Studio Hedgehog or later (have a
look [here](https://developer.android.com/build/releases/gradle-plugin?hl=en#android_gradle_plugin_and_android_studio_compatibility)
for a compatibility matrix between versions of Gradle, AGP and Android Studio).
Alternatively, you can try and use IntelliJ IDEA or Fleet but some extra steps may be needed to
ensure everything fits and runs together.

In order for Gradle to build, you will need to have a JDK installed on your local development
machine, if you are using stock Android Studio it ships with the JetBrains runtime, you could have a
look in the Settings dialog under the section "Build, Execution, Deployment > Build Tools > Gradle"
in the "Gradle JDK" location drop-down menu. If you want to use your custom JDK (e.g. under Linux
you want to try OpenJDK instead), please make sure that it has a suitable version, according
to [this page](https://docs.gradle.org/8.2/userguide/compatibility.html), so between 8 and 21.

Finally, since building this project requires a lot of RAM due to its multi-module structure and to
the fact that it is quite a complex project, please make sure that the `gradle.properties` file in
the root folder contains proper memory settings for the JVM and the Kotlin compile daemon:

```properties
org.gradle.jvmargs=-Xmx8192M -Dfile.encoding=UTF-8 -Dkotlin.daemon.jvm.options\="-Xmx8192M"
```

## 5. Project structure

This is a Gradle project, it is setup to download a Gradle distribution and resolve dependencies
according to the definitions contained in the `gradle/libs.versions.toml` file.

Also, please note that in the `settings.gradle.kts` file we are using the option:

```
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
```

to reference Gradle subprojects in each `build.gradle.kts` with a type safe notation
e.g. `implementation(projects.core.utils)`.

### 5.1 Tech stack

Here is a list of the technologies used in the project, with a short historical explanation in the
cases where the initial choice changed over time and the reasons why the change was made.

<dl>
<dt>Dependency Injection</dt>
<dd>
The choice here is the <a href="https://github.com/InsertKoinIO/koin">Koin</a> library. The main 
reason it was selected because of its great multiplatform support and the integration with the 
navigation library (which at the beginning of the project was not there yet, but was added later and
proved to work great). You can find module definitions (beware, Gradle modules and Koin modules are
two different concepts and should not be confused) in a `di` package inside each subproject, modules
can include each other and all top-level modules are included in the shared module, more on it in
"Module overview and dependencies".
</dd>
<dt>Navigation</dt>
<dd>
For navigation the <a href="https://github.com/adrielcafe/voyager">Voyager</a> library has been
selected. Again, the choice was driven by its multi-platform support, its very practical approach 
and ease to set up and get going, compared to other multi-platform libraries like Decompose that
were used in the past. Nonetheless, and this lesson was learned the hard way, easiness of use and
compactness mean that things will go smooth in the future, and as the project grew the navigation
library started to show its limits. Part of them were addressed by encapsulating the transition 
logic (to push/pop screens into the navigation stack and open/close modal bottom sheets) into a 
centralized component <a href="https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/core/navigation/src/commonMain/kotlin/com/github/diegoberaldin/raccoonforlemmy/core/navigation/NavigationCoordinator.kt">NavigationCoordinator.kt</a>.
Something similar was done for the navigation drawer in <a href="https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/core/navigation/src/commonMain/kotlin/com/github/diegoberaldin/raccoonforlemmy/core/navigation/DrawerCoordinator.kt">DrawerCoordinator.kt</a>.
Even the DI integration with Koin was not pain-free, the `:core:navigation` module contains some
glue code that is used to work around some of the issues that were encountered.
</dd>
<dt>Networking</dt>
<dd>Here, at least for Android developers, no surprises: the choice was <a href="https://github.com/ktorio/ktor">Ktor</a>
which has great multiplatform support. Instead of using Retrofit, to create network adapters the
<a href="https://github.com/Foso/Ktorfit">Ktorfit</a> library is used, which uses KSP to parse 
annotations and generate code.
</dd>
<dt>Resource management</dt>
<dd>
The <a href="https://github.com/icerockdev/moko-resources">Moko resources</a> library is used as part
of the Moko project. This was a choice I never regretted, it works great (even in edge cases such as
dynamic language configuration independent of device settings, which was a project must-have from
the beginning) and makes it really easy to access strings, icons and fonts in a multiplatform environment.
</dd>
<dt>Image loading</dt>
<dd>
This was something that was expected to be simpler but unfortunately it wasn't. Popular kotlin libraries
do not support multiplatform yet and the only library that advertised it was <a href="https://github.com/Kamel-Media/Kamel">Kamel</a>
which had a major bug while rendering large images, and the community proved quite unfriendly/unwelcoming
when reporting it. The project was already relying on Kamel for many things, from loading images on demand to
Markdown rendering, so deciding to switch was not easy at all. In the end, the iOS part of the project 
continues using it (Raccoon for iOS has by far bigger problems than image rendering, being a virtually
inexistent platform) while the Android counterpart was migrated to <a href="https://github.com/coil-kt/coil">Coil</a>.
Things will change when Coil 3.x will be release, because it will go multiplatform as well.
</dd>
<dt>Preference storage</dt>
<dd>
Here the choice was the <a href="https://github.com/russhwolf/multiplatform-settings">Multiplatform settings</a>
libary which not only works great but also offers support for encryption.
</dd>
<dt>Primary persistence</dt>
<dd>
This project was a chance to experiment with <a href="https://github.com/cashapp/sqldelight">SQLDelight</a>
(in other multiplatform projects other libraries were tested like Exposed), whereas database encryption
is obtained through [SQLCipher Android](https://www.zetetic.net/sqlcipher/sqlcipher-for-android/), formerly
<a href="https://github.com/sqlcipher/android-database-sqlcipher">Android Database SQLCipher</a>.
</dd>
<dt>Markdown rendering</dt>
<dd>
This was another part, like image loading, where KMP is still lacking and things are far more
complicated than it should be. The first approach that was used in the project, and which still survives
in the iOS platform (being it "no man's land" currently) involved using JetBrain's <a href="https://github.com/JetBrains/markdown">Markdown</a> 
library for parsing in conjunction with custom Compose rendering inspired by 
<a href="https://github.com/mikepenz/multiplatform-markdown-renderer">Multiplatform Markdown Renderer</a>.
This approach was promising in the beginning but it has proven to grow more and more difficult to 
support custom Markdown features, such as Lemmy spoilers. For this reason, the Android counterpart
has been completely refactored and migrated to the <a href="https://github.com/noties/Markwon">Markwon</a>
library which is more flexible/extensible albeit more complicated to use, especially if called from 
a multiplatform environment with `expect`/`actual` functions (and image opening/URL opening/custom links
like Lemmy URL references have to be managed). The big star here is MarkwonProvider and its implementation
<a href="https://github.com/diegoberaldin/RaccoonForLemmy/blob/master/core/md/src/androidMain/kotlin/com/github/diegoberaldin/raccoonforlemmy/core/markdown/provider/DefaultMarkwonProvider.kt">DefaultMarkwonProvider.kt</a>.
Parts of the Markwon configuration and usage is inspired by <a href="https://github.com/dessalines/jerboa">Jerboa for Lemmy</a>.
</dd>
</dl>

### 5.2 Module overview and dependencies

### 5.3 Architectural patterns

## 6. Coding conventions

### 6.1 Modularization strategy

### 6.2 General Kotlin rules

### 6.3 Compose rules

### 6.4 Test structure
