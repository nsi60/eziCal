import android.content.Context
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nsi.ezcalender.model.Event
import com.nsi.ezcalender.ui.screens.startImplicitIntent
import java.net.URLEncoder

@Composable
fun EventComposable(
    modifier: Modifier = Modifier,
    context: Context,
    event: Event,
    onClick: (Event) -> Unit = {}
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .shadow(elevation = 2.dp)
            .clickable {
                onClick(event)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
//                        horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(event.dtStart?.dayOfMonth.toString())
                Text(event.dtStart?.month.toString())
//                            Text(it.dtStart?.year.toString())

            }
            Column(Modifier.weight(2f)) {
                Text(
                    text = event.summary.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${event.dtStart?.hour.toString()}:${event.dtStart?.minute.toString()} " +
                            "- ${event.dtEnd?.hour.toString()}:${event.dtEnd?.minute.toString()}"
                )
//                Box(modifier = Modifier.width(160.dp)) {
                Text(text = event.location, maxLines = 1, overflow = TextOverflow.Ellipsis)
//                }
            }

        }
    }
}