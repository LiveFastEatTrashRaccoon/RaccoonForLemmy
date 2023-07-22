package com.github.diegoberaldin.raccoonforlemmy.feature_profile

import org.koin.core.module.Module

expect val profileTabModule: Module

expect fun getProfileScreenModel(): ProfileScreenModel