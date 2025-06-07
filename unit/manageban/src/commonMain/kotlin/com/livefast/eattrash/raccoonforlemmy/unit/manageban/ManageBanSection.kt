package com.livefast.eattrash.raccoonforlemmy.unit.manageban

sealed interface ManageBanSection {
    data object Users : ManageBanSection

    data object Communities : ManageBanSection

    data object Instances : ManageBanSection

    data object Domains : ManageBanSection

    data object StopWords : ManageBanSection
}

fun ManageBanSection.toInt(): Int = when (this) {
    ManageBanSection.StopWords -> 4
    ManageBanSection.Domains -> 3
    ManageBanSection.Instances -> 2
    ManageBanSection.Communities -> 1
    ManageBanSection.Users -> 0
}

fun Int.toManageBanSection(): ManageBanSection = when (this) {
    4 -> ManageBanSection.StopWords
    3 -> ManageBanSection.Domains
    2 -> ManageBanSection.Instances
    1 -> ManageBanSection.Communities
    else -> ManageBanSection.Users
}
