package miguel.project.foundation.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import miguel.project.foundation.R

@Composable
fun CardView(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val smallPadding = dimensionResource(R.dimen.padding_small)
    val smallElevation = dimensionResource(R.dimen.elevation_small)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick?.invoke()
            },
        elevation = smallElevation
    )
    {
        Surface(
            modifier = Modifier
                .padding(all = smallPadding),
            content = content
        )
    }
}