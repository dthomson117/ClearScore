package com.android.clearscore.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Default from project
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

val progressBrush: Brush =
    Brush.sweepGradient(
        0f to Color.Yellow,
        .2f to Color.Yellow,
        .5f to Color.Green,
        .74f to Color.Green,
        .76f to Color.Red,
        .9f to Color.Red,
        1f to Color.Yellow
    )