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
For navigation the <a href="https://github.com/adrielcafe/voyager">Voyager</a> library has been
selected. Again, the choice was driven by its multi-platform support, its very practical approach 
and ease to set up and get going, compared to other multi-platform libraries like Decompose that
were used in the past. Nonetheless, and this lesson was learned the hard way, easiness of use and
compactness mean that things will go smooth in the future, and as the project grew the navigation
library started to show its limits. Part of them were addressed by encapsulating the transition 
logic (to push/pop screens into the navigation stack and open/close modal bottom sheets) into a 
centralized component NavigationCoordinator.kt.
Something similar was done for the navigation drawer in DrawerCoordinator.kt.
Even the DI integration was not totally pain-free, the <code>:core:navigation</code> module contains
some glue code that is used to work around some of the issues that were encountered.
</dd>

<dt>Networking</dt>
<dd>Here, at least for Android developers, no surprises: the choice was <a href="https://github.com/ktorio/ktor">Ktor</a>
which has great multiplatform support. Instead of using Retrofit, to create network adapters the
<a href="https://github.com/Foso/Ktorfit">Ktorfit</a> library is used, which uses KSP to parse 
annotations and generate code.
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
