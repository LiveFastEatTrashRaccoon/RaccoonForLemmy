package com.livefast.eattrash.raccoonforlemmy.core.commonui.modals

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.livefast.eattrash.raccoonforlemmy.core.appearance.theme.Spacing
import com.livefast.eattrash.raccoonforlemmy.core.commonui.lemmyui.UserTagItem
import com.livefast.eattrash.raccoonforlemmy.core.l10n.LocalStrings
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignUserTagBottomSheet(
    sheetScope: CoroutineScope = rememberCoroutineScope(),
    state: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    tags: List<UserTagModel> = emptyList(),
    initiallyCheckedIds: List<Long> = emptyList(),
    onSelect: ((List<Long>) -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    onAddNewTag: (() -> Unit)? = null,
) {
    val selectedIds =
        remember {
            mutableStateListOf<Long>()
        }
    LaunchedEffect(Unit) {
        selectedIds.addAll(initiallyCheckedIds)
    }

    ModalBottomSheet(
        contentWindowInsets = { WindowInsets.navigationBars },
        sheetState = state,
        onDismissRequest = {
            onDismiss?.invoke()
        },
    ) {
        Column(
            modifier = Modifier.padding(bottom = Spacing.xs),
        ) {
            Box {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = LocalStrings.current.manageUserTagsTitle,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                if (onAddNewTag != null) {
                    IconButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            onAddNewTag()
                        },
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = LocalStrings.current.buttonAdd,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(Spacing.xs))
            LazyColumn(
                modifier = Modifier.padding(top = Spacing.m).height(400.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                items(tags) { tag ->
                    UserTagItem(
                        tag = tag,
                        checked = selectedIds.contains(tag.id),
                        onCheck = { checked ->
                            tag.id?.also {
                                if (checked) {
                                    selectedIds.add(it)
                                } else {
                                    selectedIds.remove(it)
                                }
                            }
                        },
                    )
                }
            }
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                onClick = {
                    sheetScope
                        .launch {
                            state.hide()
                        }.invokeOnCompletion {
                            onSelect?.invoke(selectedIds)
                        }
                },
            ) {
                Text(text = LocalStrings.current.buttonConfirm)
            }
        }
    }
}
