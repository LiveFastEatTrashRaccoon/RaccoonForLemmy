<div align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.10-7f52ff?logo=kotlin" />
  <img src="https://img.shields.io/badge/platforms-Android,iOS-green" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-1.5.2-3e7fea?logo=jetpackcompose" />
  <img src="https://img.shields.io/github/license/diegoberaldin/RaccoonForLemmy" />
</div>

<br />

<div align="center">
  <img src="https://github.com/diegoberaldin/RaccoonForLemmy/assets/2738294/6785188f-9c2a-4622-ab6b-5aa116d27c31" width="250" height="auto" />
</div>

# Raccoon for Lemmy

A Kotlin Multiplatform Mobile client for Lemmy.

<div align="center">
<table>
  <tr>
    <td><!-- Home -->
      <img src="https://github.com/diegoberaldin/RaccoonForLemmy/assets/2738294/01b3f04b-3900-46dc-b835-efc11c89ab63" width="310" />
    </td>
    <td><!-- Post detail -->
      <img src="https://github.com/diegoberaldin/RaccoonForLemmy/assets/2738294/c303237d-b5df-40bd-acc6-419aeed8de10" width="310" />
    </td>
  </tr>
  <tr>
    <td><!-- Explore -->
      <img src="https://github.com/diegoberaldin/RaccoonForLemmy/assets/2738294/e836c063-f85c-488d-b26a-5aa14a49ec2b" width="310" />
    </td>
    <td><!-- Community detail -->
      <img src="https://github.com/diegoberaldin/RaccoonForLemmy/assets/2738294/e9174244-e0bf-4bcc-bed5-6332e807ce60" width="310" />
    </td>
  </tr>
  <tr>
    <td><!-- Inbox -->
      <img src="https://github.com/diegoberaldin/RaccoonForLemmy/assets/2738294/1aaff3ac-205b-404d-b83e-ce3f4e267e75" width="310" />
    </td>
    <td><!-- Settings -->
      <img src="https://github.com/diegoberaldin/RaccoonForLemmy/assets/2738294/3e5e95bf-d09e-4339-bdaf-4e56dedb2bf3" width="310" />
    </td>
  </tr>
</table>
</div>

This started out as an exercise to play around with KMP and Compose Multiplatform and implement a
Lemmy client and gradually grew to a fully functional client.

The application is under active development, expect new features to be added (e.g. multi-account
support, video support, customizable post layouts, etc.).

## Libraries used:

- [Koin](https://github.com/InsertKoinIO/koin) for dependency injection
- [Voyager](https://github.com/adrielcafe/voyager) for screen navigation
- [Ktor](https://github.com/ktorio/ktor) and [Ktorfit](https://github.com/Foso/Ktorfit) for
  networking in conjunction with kotlinx-serialization for JSON marshalling
- [Moko resources](https://github.com/icerockdev/moko-resources) for resource management
- [Kamel](https://github.com/Kamel-Media/Kamel) for lazy image loading
- [Multiplatform settings](https://github.com/russhwolf/multiplatform-settings) for encrypted
  preferences
- [Markdown](https://github.com/JetBrains/markdown) for markdown parsing
- [SQLDelight](https://github.com/cashapp/sqldelight)
  and [SQLCipher](https://github.com/sqlcipher/sqlcipher) for local persistence

## Credits:

- the `core-api` module is heavily inspired
  by [Jerboa for Lemmy](https://github.com/dessalines/jerboa)
- the `core-md` module is taken
  from [Multiplatform Markdown Renderer](https://github.com/mikepenz/multiplatform-markdown-renderer)
  with some adptations _a posteriori_
- the UI is inspired by the really great [Thunder](https://github.com/thunder-app/thunder) app

## Want to try it out?

- get it on [Obtanium](https://github.com/ImranR98/Obtainium/releases) by simply adding this
  repo `https://github.com/diegoberaldin/RaccoonForLemmy`
