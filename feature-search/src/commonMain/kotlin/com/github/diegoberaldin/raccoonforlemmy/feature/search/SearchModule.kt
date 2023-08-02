package com.github.diegoberaldin.raccoonforlemmy.feature.search

import org.koin.core.module.Module

expect val searchTabModule: Module

expect fun getSearchScreenModel(): SearchScreenModel
