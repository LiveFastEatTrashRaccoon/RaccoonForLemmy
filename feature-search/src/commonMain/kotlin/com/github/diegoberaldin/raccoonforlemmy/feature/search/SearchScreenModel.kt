package com.github.diegoberaldin.raccoonforlemmy.feature.search

import cafe.adriel.voyager.core.model.ScreenModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.DefaultMviModel
import com.github.diegoberaldin.raccoonforlemmy.core.architecture.MviModel

class SearchScreenModel(
    private val mvi: DefaultMviModel<SearchScreenMviModel.Intent, SearchScreenMviModel.UiState, SearchScreenMviModel.Effect>,
) : ScreenModel,
    MviModel<SearchScreenMviModel.Intent, SearchScreenMviModel.UiState, SearchScreenMviModel.Effect> by mvi
