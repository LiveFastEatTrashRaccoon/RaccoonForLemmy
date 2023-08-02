package com.github.diegoberaldin.raccoonforlemmy.core_commonui.di

import com.github.diegoberaldin.raccoonforlemmy.core_commonui.postdetail.PostDetailScreenViewModel
import com.github.diegoberaldin.raccoonforlemmy.domain_lemmy.data.PostModel

expect fun getPostDetailScreenViewModel(post: PostModel): PostDetailScreenViewModel
