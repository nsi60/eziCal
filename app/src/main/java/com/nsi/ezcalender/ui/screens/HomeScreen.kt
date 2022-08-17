package com.nsi.ezcalender.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.nsi.ezcalender.ui.MainViewModel
import java.io.*
import java.lang.Exception


@Composable
fun HomeScreen(
    mainVewModel: MainViewModel,
    navigateToReadFileScreen: () -> Unit,
    navigateToCreateFileScreen: () -> Unit

) {
    val state = mainVewModel.state
    val context = LocalContext.current

    val intent = remember {
        Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "text/calendar"
        }
    }

    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri: Uri? = result.data?.data
        try {
            // open the user-picked file for reading:
            val inputStream = context.contentResolver.openInputStream(uri!!)
            mainVewModel.setSelectedFileInputStream(inputStream)
            navigateToReadFileScreen()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    HomeScreenContent(
        openFileClicked = {
            filePickerLauncher.launch(intent)
        },
        createNewFileClicked = {
            navigateToCreateFileScreen()
        }
    )

}

@Composable
fun HomeScreenContent(
    openFileClicked: () -> Unit = {},
    createNewFileClicked: () -> Unit = {}

) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            openFileClicked()
        }) {
            Text(text = "Open existing file")
        }

        Button(onClick = {
            createNewFileClicked()
        }) {
            Text(text = "Create new file")
        }
    }
}


