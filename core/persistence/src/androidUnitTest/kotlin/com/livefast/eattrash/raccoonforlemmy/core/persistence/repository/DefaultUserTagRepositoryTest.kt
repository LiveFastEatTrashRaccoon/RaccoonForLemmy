package com.livefast.eattrash.raccoonforlemmy.core.persistence.repository

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import app.cash.sqldelight.Query
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UserTagMemberEntity
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UsertagmembersQueries
import com.livefast.eattrash.raccoonforlemmy.core.persistence.UsertagsQueries
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagMemberModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagModel
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.UserTagType
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.isSpecial
import com.livefast.eattrash.raccoonforlemmy.core.persistence.data.toInt
import com.livefast.eattrash.raccoonforlemmy.core.persistence.entities.AppDatabase
import com.livefast.eattrash.raccoonforlemmy.core.persistence.provider.DatabaseProvider
import com.livefast.eattrash.raccoonforlemmy.core.persistence.usertagmembers.GetBy
import com.livefast.eattrash.raccoonforlemmy.core.testutils.DispatcherTestRule
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

class DefaultUserTagRepositoryTest {
    @get:Rule
    val dispatcherTestRule = DispatcherTestRule()

    private val tagQuery = mockk<Query<UserTagEntity>>()
    private val tagQueries =
        mockk<UsertagsQueries>(relaxUnitFun = true) {
            every { getBy(any()) } returns tagQuery
            every { getAllBy(any()) } returns tagQuery
            every { create(any(), any(), any(), any()) } returns mockk(relaxUnitFun = true)
            every { update(any(), any(), any(), any()) } returns mockk(relaxUnitFun = true)
            every { delete(any()) } returns mockk(relaxUnitFun = true)
        }
    private val memberQuery = mockk<Query<UserTagMemberEntity>>()
    private val memberGetByQuery = mockk<Query<GetBy>>()
    private val memberQueries =
        mockk<UsertagmembersQueries>(relaxUnitFun = true) {
            every { create(any(), any()) } returns mockk(relaxUnitFun = true)
            every { delete(any(), any()) } returns mockk(relaxUnitFun = true)
            every { getMembers(any()) } returns memberQuery
            every { getBy(any(), any()) } returns memberGetByQuery
            every { getBy(any(), any()) } returns memberGetByQuery
        }
    private val provider =
        mockk<DatabaseProvider> {
            every { getDatabase() } returns
                mockk<AppDatabase> {
                    every { usertagsQueries } returns tagQueries
                    every { usertagmembersQueries } returns memberQueries
                }
        }
    private val sut = DefaultUserTagRepository(provider)

    @Test
    fun whenGetAll_thenResultAndInteractionsAreAsExpected() =
        runTest {
            val accountId = 1L
            val tagId = 2L
            val model = UserTagModel(id = tagId, name = "tag")
            every { tagQuery.executeAsList() } returns
                listOf(
                    UserTagEntity(
                        id = tagId,
                        name = model.name,
                        account_id = accountId,
                        color = model.color?.toLong(),
                        type = model.type.toInt().toLong(),
                    ),
                )

            val res = sut.getAll(accountId)

            assertEquals(1, res.size)
            assertEquals(model, res.first())
            assertFalse(res.first().isSpecial)
            verify {
                tagQueries.getAllBy(accountId)
            }
        }

    @Test
    fun whenGetById_thenResultAndInteractionsAreAsExpected() =
        runTest {
            val tagId = 1L
            val model = UserTagModel(id = tagId, name = "tag")
            every { tagQuery.executeAsOneOrNull() } returns
                UserTagEntity(
                    id = tagId,
                    name = model.name,
                    account_id = 0L,
                    color = model.color?.toLong(),
                    type = model.type.toInt().toLong(),
                )

            val res = sut.getById(tagId)

            assertNotNull(res)
            assertEquals(model, res)
            verify {
                tagQueries.getBy(tagId)
            }
        }

    @Test
    fun whenGetMembers_thenResultAndInteractionsAreAsExpected() =
        runTest {
            val tagId = 1L
            val model = UserTagMemberModel(userTagId = tagId, username = "user-name")
            every { memberQuery.executeAsList() } returns
                listOf(
                    UserTagMemberEntity(
                        id = 0L,
                        username = model.username,
                        user_tag_id = tagId,
                    ),
                )

            val res = sut.getMembers(tagId)

            assertEquals(1, res.size)
            assertEquals(model, res.first())
            verify {
                memberQueries.getMembers(tagId)
            }
        }

    @Test
    fun whenGetTags_thenResultAndInteractionsAreAsExpected() =
        runTest {
            val accountId = 1L
            val tagId = 2L
            val model = UserTagModel(id = tagId, name = "tag")
            val username = "user-name"
            every { memberGetByQuery.executeAsList() } returns
                listOf(
                    GetBy(
                        id = 0L,
                        name = model.name,
                        username = username,
                        user_tag_id = tagId,
                        account_id = accountId,
                        color = model.color?.toLong(),
                        id_ = tagId,
                        type = model.type.toInt().toLong(),
                    ),
                )

            val res =
                sut.getTags(
                    username = username,
                    accountId = accountId,
                )

            assertEquals(1, res.size)
            assertEquals(model, res.first())
            verify {
                memberQueries.getBy(
                    username = username,
                    account_id = accountId,
                )
            }
        }

    @Test
    fun whenCreate_thenInteractionsAreAsExpected() =
        runTest {
            val model = UserTagModel(name = "tag")
            val accountId = 1L

            sut.create(model = model, accountId = accountId)

            verify {
                tagQueries.create(
                    name = model.name,
                    account_id = accountId,
                    color = model.color?.toLong(),
                    type = model.type.toInt().toLong(),
                )
            }
        }

    @Test
    fun whenUpdate_thenInteractionsAreAsExpected() =
        runTest {
            val tagId = 1L
            val newName = "new-tag"
            val color = Color.Red.toArgb()
            val type = UserTagType.Regular

            sut.update(
                id = tagId,
                name = newName,
                color = color,
                type = type.toInt(),
            )

            verify {
                tagQueries.update(
                    id = tagId,
                    name = newName,
                    color = color.toLong(),
                    type = type.toInt().toLong(),
                )
            }
        }

    @Test
    fun whenDelete_thenInteractionsAreAsExpected() =
        runTest {
            val tagId = 1L

            sut.delete(id = tagId)

            verify {
                tagQueries.delete(id = tagId)
            }
        }

    @Test
    fun whenAddMember_thenInteractionsAreAsExpected() =
        runTest {
            val tagId = 1L
            val username = "user-name"

            sut.addMember(username = username, userTagId = tagId)

            verify {
                memberQueries.create(username = username, user_tag_id = tagId)
            }
        }

    @Test
    fun whenRemoveMember_thenInteractionsAreAsExpected() =
        runTest {
            val tagId = 1L
            val username = "user-name"

            sut.removeMember(username = username, userTagId = tagId)

            verify {
                memberQueries.delete(username = username, user_tag_id = tagId)
            }
        }

    @Test
    fun whenGetBelonging_thenInteractionsAreAsExpected() =
        runTest {
            val accountId = 1L
            val tagId = 2L
            val username = "user-name"
            val model = UserTagModel(name = "tag", id = tagId)
            every { memberGetByQuery.executeAsList() } returns
                listOf(
                    GetBy(
                        id = 0L,
                        name = model.name,
                        username = username,
                        user_tag_id = tagId,
                        account_id = accountId,
                        color = model.color?.toLong(),
                        id_ = tagId,
                        type = model.type.toInt().toLong(),
                    ),
                )

            val res = sut.getBelonging(username = username, accountId = accountId)

            assertEquals(1, res.size)
            assertEquals(model, res.first())
            verify {
                memberQueries.getBy(username = username, account_id = accountId)
            }
        }

    @Test
    fun whenGetSpecialTagColor_thenInteractionsAreAsExpected() =
        runTest {
            val accountId = 1L
            val color = Color.Red.toArgb()
            every { tagQuery.executeAsList() } returns
                listOf(
                    UserTagEntity(
                        id = 2L,
                        name = "me",
                        account_id = accountId,
                        color = color.toLong(),
                        type = UserTagType.Me.toInt().toLong(),
                    ),
                    UserTagEntity(
                        id = 3L,
                        name = "mod",
                        account_id = accountId,
                        color = Color.Green.toArgb().toLong(),
                        type = UserTagType.Moderator.toInt().toLong(),
                    ),
                )

            val res = sut.getSpecialTagColor(type = UserTagType.Me, accountId = accountId)

            assertEquals(color, res)
            verify {
                tagQueries.getAllBy(accountId)
            }
        }
}
