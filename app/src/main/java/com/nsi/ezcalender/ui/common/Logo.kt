import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import coil.size.Size
import com.nsi.ezcalender.R

@Composable
fun EzIcalLogoGif(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val imageLoader = remember {

        ImageLoader.Builder(context)
            .components {
                add(ImageDecoderDecoder.Factory())
            }
            .build()
    }

//    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Image(
        painter = rememberAsyncImagePainter(
            ImageRequest.Builder(context).data(data = R.drawable.logo_gif_transparent)
                .apply(block = {
                    size(Size.ORIGINAL)
                }).build(), imageLoader = imageLoader
        ),
        contentDescription = null,
//            modifier = modifier.fillMaxWidth(),
        modifier = modifier
    )

//    }
}

@Composable
fun EzIcalLogoStatic() {
    Image(
        painter = painterResource(id = R.drawable.logo_static),
        contentDescription = "Logo",
    )
}