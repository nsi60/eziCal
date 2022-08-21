package com.nsi.ezcalender.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext


@Composable
fun getResourceByName(name: String): String {
    val context = LocalContext.current
    return context.resources.getString(
        context.resources.getIdentifier(
            name,
            "string",
            context.packageName
        )
    )
}