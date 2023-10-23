// CoilImagesPlugin has package private constructor, so this is a work around.
package io.noties.markwon.image.coil

import android.content.Context
import android.text.style.URLSpan
import android.view.View
import coil.ImageLoader
import coil.request.Disposable
import coil.request.ImageRequest
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.MarkwonSpansFactory
import io.noties.markwon.RenderProps
import io.noties.markwon.SpanFactory
import io.noties.markwon.image.AsyncDrawable
import io.noties.markwon.image.AsyncDrawableSpan
import io.noties.markwon.image.ImageProps
import org.commonmark.node.Image

/*
 * CREDITS:
 * https://github.com/dessalines/jerboa/blob/main/app/src/main/java/com/jerboa/util/markwon/ClickableCoilImagesPlugin.kt
 */
class ClickableCoilImagesPlugin(
    coil: CoilStore,
    imageLoader: ImageLoader,
    private val onOpenImage: ((String) -> Unit)? = null,
) : CoilImagesPlugin(coil, imageLoader) {

    companion object {
        fun create(
            context: Context,
            imageLoader: ImageLoader,
            onOpenImage: ((String) -> Unit)? = null,
        ): ClickableCoilImagesPlugin {
            return ClickableCoilImagesPlugin(
                object : CoilStore {
                    override fun load(drawable: AsyncDrawable): ImageRequest {
                        return ImageRequest.Builder(context)
                            .data(drawable.destination)
                            .build()
                    }

                    override fun cancel(disposable: Disposable) {
                        disposable.dispose()
                    }
                },
                imageLoader,
                onOpenImage
            )
        }
    }

    override fun configureSpansFactory(builder: MarkwonSpansFactory.Builder) {
        builder.setFactory(Image::class.java, ClickableImageFactory(onOpenImage))
    }
}

internal class ClickableImageFactory(val onOpenImage: ((String) -> Unit)? = null) :
    HttpsImageSpanFactory() {

    override fun getSpans(configuration: MarkwonConfiguration, props: RenderProps): Any {
        val image = super.getSpans(configuration, props) as AsyncDrawableSpan
        val clickSpan = object : URLSpan(image.drawable.destination) {
            override fun onClick(view: View) {
                view.cancelPendingInputEvents()
                onOpenImage?.invoke(image.drawable.destination)
            }
        }

        return arrayOf(image, clickSpan)
    }
}

internal open class HttpsImageSpanFactory : SpanFactory {
    override fun getSpans(configuration: MarkwonConfiguration, props: RenderProps): Any? {
        return AsyncDrawableSpan(
            configuration.theme(),
            AsyncDrawable(
                ImageProps.DESTINATION.require(props).toHttps(),
                configuration.asyncDrawableLoader(),
                configuration.imageSizeResolver(),
                ImageProps.IMAGE_SIZE[props],
            ),
            AsyncDrawableSpan.ALIGN_BOTTOM,
            ImageProps.REPLACEMENT_TEXT_IS_LINK[props, false],
        )
    }
}

private fun String.toHttps(): String {
    return if (this.startsWith("http://", true)) {
        this.replaceFirst("http", "https", true)
    } else {
        this
    }
}