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

This is mostly an exercise to play around with KMM and Compose Multiplatform and implement a Lemmy client.

The project is still at an early stage and not ready for production, expect things to change and even major changes to the source code.

Libraries used:

- Koin for dependency injection
- Voyager for screen navigation
- Ktor with Ktorfit for networking in conjunction with kotlinx-serialization for JSON marshalling
- Moko resources for resource management
- Kamel for lazy image loading
- Multiplatform settings for encrypted preferences
- Markdown by Jetbrains for markdown parsing
- ... more to come (e.g. SQLdelight for persistence)

Credits: 
- the `core-api` module is heavily inspired by [Jerboa for Lemmy](https://github.com/dessalines/jerboa)
- the `core-md` module is copied from [Multiplatform Markdown Renderer](https://github.com/mikepenz/multiplatform-markdown-renderer)
