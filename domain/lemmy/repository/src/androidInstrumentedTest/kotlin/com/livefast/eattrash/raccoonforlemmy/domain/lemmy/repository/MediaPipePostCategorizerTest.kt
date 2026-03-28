package com.livefast.eattrash.raccoonforlemmy.domain.lemmy.repository

import android.content.Context
import androidx.test.rule.ServiceTestRule
//import androidx.test.runner.AndroidJUnit4
//import io.mockk.mockk
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.test.assertTrue
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.time.DurationUnit
import kotlin.time.toDuration


@RunWith(AndroidJUnit4::class)
class MediaPipePostCategorizerTest {

    @JvmField
    @get:Rule
    val context = androidx.test.core.app.ApplicationProvider.getApplicationContext<Context>()

//    @get:Rule
//    val serviceRule = ServiceTestRule()


    @Test
    fun smoke2a(){
        assertNotNull(context, "Context should be available in instrumented test")
    }

    @Test
    fun smokeb(){
        assertTrue(false, "Running on phone")
    }

    /**
     * cannot run on emulator :( https://github.com/google-ai-edge/mediapipe/issues/5362
     */
    @Test
    fun `classifier`() = runTest(timeout = 5000.toDuration(DurationUnit.MILLISECONDS)) {
//        val context = mockk<Context>(relaxed = true)
        val categorizer = MediaPipePostCategorizer(context, "classifier.tflite")

        val headline = "The latest advancements in quantum computing are reshaping the tech industry."
        val possibleTopics = listOf(
            "Politics", "Technology", "Sports", "Entertainment", "Food",
            "Health", "Science", "Business", "World", "Other"
        )

//        try {
            val result = categorizer.categorize(headline)
            println("Categorization result: $result")

//            if (result != null) {
                // If the model is a general one, it might return labels that are not in our list
                // but we should check if it returns *something* consistent if possible.
                assertNotNull( result, "Result should not be empty")

                assertEquals("positive",actual = result)

//            } else {
//                println("Categorization returned null (expected if native libs or model are missing in unit test)")
//            }
//        } catch (e: UnsatisfiedLinkError) {catch
//            println("MediaPipe native libraries not available in unit test environment. This is expected.")
//        } catch (e: Exception) {
//            println("Categorization failed with exception: ${e.message}")
//        }
    }
}
