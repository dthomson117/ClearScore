package com.android.clearscore.presentation.common

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.android.clearscore.R

@Composable
fun ScreenLoading(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        modifier = modifier
            .fillMaxSize()
            .testTag(stringResource(R.string.loadingindicator))
    )
}

@Preview
@Composable
fun ScreenLoadingPreview() {
    ScreenLoading()
}