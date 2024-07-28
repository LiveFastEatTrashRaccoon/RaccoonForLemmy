package com.github.diegoberaldin.raccoonforlemmy.unit.manageban

sealed interface ManageBanSection {
    data object Users : ManageBanSection

    data object Communities : ManageBanSection

    data object Instances : ManageBanSection

    data object Domains : ManageBanSection
}

fun ManageBanSection.toInt(): Int =
    when (this) {
        ManageBanSection.Communities -> 1
        ManageBanSection.Domains -> 3
        ManageBanSection.Instances -> 2
        ManageBanSection.Users -> 0
    }

fun Int.toManageBanSection(): ManageBanSection =
    when (this) {
        3 -> ManageBanSection.Domains
        2 -> ManageBanSection.Instances
        1 -> ManageBanSection.Communities
        else -> ManageBanSection.Users
    }
