package com.dicoding.forumdiskusiapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PostComponent(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    userName: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        UserComponent(userName = userName)
        Text(
            text = title,
            fontSize = 21.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = body,
            fontSize = 14.sp,
            textAlign = TextAlign.Justify,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )

    }
}

@Preview(showBackground = true)
@Composable
private fun PostItemPreview() {
    PostComponent(
        title = "Ini lagu enak banget coy, sumpah brooo enak banget lagunyaaa",
        userName = "Aditya",
        body = "AWODOAWDAWODAWODAJWDOASJKODKWAODKOASKDOWd"
    )
}