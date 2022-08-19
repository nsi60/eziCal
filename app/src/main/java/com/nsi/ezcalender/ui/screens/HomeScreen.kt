package com.nsi.ezcalender.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.State
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
            mainVewModel.readSelectedFile()
            navigateToReadFileScreen()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    HomeScreenContent(
        state = state,
        openFileClicked = {
            filePickerLauncher.launch(intent)
        },
        createNewFileClicked = {
            navigateToCreateFileScreen()
        }
    ) {
        mainVewModel.setInputUrl(it)
        mainVewModel.openFromUrl()
        navigateToReadFileScreen()
    }

}

@Composable
fun HomeScreenContent(
    state: MutableState<State>,
    openFileClicked: () -> Unit = {},
    createNewFileClicked: () -> Unit = {},
    openFromUrlCLicked: (String) -> Unit = {}


) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        var switchState by remember {
            mutableStateOf(true)
        }

        var text by remember { mutableStateOf(TextFieldValue(state.value.icsFileUrl ?: "")) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Open URL", color = if (switchState) {
                    Color.Gray
                } else {
                    Color.Unspecified
                }
            )
            Switch(
                checked = switchState,
                onCheckedChange = { switchState = it },
                colors = SwitchDefaults.colors(uncheckedTrackColor = MaterialTheme.colors.secondaryVariant)
            )
            Text(
                "Open file", color = if (!switchState) {
                    Color.Gray
                } else {
                    Color.Unspecified
                }
            )
        }

        if (switchState) {
            OutlinedTextField(modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .background(
                    color = Color.Unspecified,
                    shape = CircleShape,
                ), value = "Open a file on the device.", onValueChange = {},
                enabled = false,
                colors = TextFieldDefaults.textFieldColors(
                    textColor = Color.Transparent,
                    disabledTextColor = Color.Unspecified,
                    backgroundColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = TextStyle(textAlign = TextAlign.Center),
                maxLines = 1,
            )
            Button(onClick = {
                openFileClicked()
            }) {
                Text(text = "Open existing file")
            }

        } else {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .background(
                        color = Color.Unspecified,
                        shape = CircleShape,
                    ),
                value = text,
                onValueChange = { newText ->
                    text = newText
                },
                label = { Text(text = "URL") },
                placeholder = { Text(text = "https://") },
//                colors = TextFieldDefaults.textFieldColors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                ),
                maxLines = 1
            )

            Button(onClick = {
                openFromUrlCLicked(text.text)
            }) {
                Text(text = "Open from url")
            }
        }

    }
}


