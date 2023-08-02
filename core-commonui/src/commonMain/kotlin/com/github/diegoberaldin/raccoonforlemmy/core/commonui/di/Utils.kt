package com.github.diegoberaldin.raccoonforlemmy.core.commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core.commonui.postdetail.PostDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain.lemmy.data.PostModel

expect fun getPostDetailScreenViewModel(post: PostModel): PostDetailScreenViewModel
