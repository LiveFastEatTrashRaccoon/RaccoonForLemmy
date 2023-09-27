package com.github.diegoberaldin.raccoonforlemmy.feature.search.di

import com.github.diegoberaldin.raccoonforlemmy.feature.search.main.ExploreViewModel
import com.github.diegoberaldin.raccoonforlemmy.feature.search.managesubscriptions.ManageSubscriptionsViewModel

expect fun getExploreViewModel(): ExploreViewModel

expect fun getManageSubscriptionsViewModel(): ManageSubscriptionsViewModel