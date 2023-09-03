<div align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.8.20-7f52ff?logo=kotlin" />
  <img src="https://img.shields.io/badge/platform-Android,iOS-blue" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-1.4.3-3e7fea?logo=jetpackcompose" />
  <img src="https://img.shields.io/github/license/diegoberaldin/MetaPhrase" />
</div>

<br />

<div align="center">
  <img src="https://github.com/diegoberaldin/RacconForLemmy/assets/2738294/6785188f-9c2a-4622-ab6b-5aa116d27c31" width="250" height="auto" />
</div>

# Raccon for Lemmy

A Kotlin Multiplatform Mobile client for Lemmy.

<div align="center">
<table>
  <tr>
    <td>
      <img src="https://github.com/diegoberaldin/RacconForLemmy/assets/2738294/da834566-6741-4218-99da-ba56b59b7f50" width="310" />
    </td>
    <td>
      <img src="https://github.com/diegoberaldin/RacconForLemmy/assets/2738294/348b6773-b434-4d11-8c41-868e9aafeb5c" width="310" />
    </td>
  </tr>
</table>
</div>

This is mostly an exercise to play around with KMM and Compose Multiplatform and implement a Lemmy
client.

The project is still at an early stage and not ready for production, expect things to change and
even major changes to the source code.

Libraries used:

- [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
- [Voyager](https://github.com/adrielcafe/voyager) for screen navigation
- [Ktor](https://github.com/ktorio/ktor) with [Ktorfit](https://github.com/Foso/Ktorfit) for
  networking in conjunction with kotlinx-serialization for JSON marshalling
- [Moko resources](https://github.com/icerockdev/moko-resources) for resource management
- [Kamel](https://github.com/Kamel-Media/Kamel) for lazy image loading
- [Multiplatform settings](https://github.com/russhwolf/multiplatform-settings) for encrypted
  preferences
- [Markdown](https://github.com/JetBrains/markdown) for markdown parsing
- ... more to come (e.g. SQLdelight for persistence)

Credits:

- the `core-api` module is heavily inspired
  by [Jerboa for Lemmy](https://github.com/dessalines/jerboa)
- the `core-md` module is copied
  from [Multiplatform Markdown Renderer](https://github.com/mikepenz/multiplatform-markdown-renderer)
- the UI is vaguely inspired by the [Thunder](https://github.com/thunder-app/thunder) app
