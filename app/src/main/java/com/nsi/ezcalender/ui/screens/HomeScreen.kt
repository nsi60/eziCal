package com.nsi.ezcalender.ui.screens

import EzIcalLogoGif
import EzIcalLogoStatic
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nsi.ezcalender.MainViewModel
import com.nsi.ezcalender.R
import com.nsi.ezcalender.State


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

        var switchState by rememberSaveable {
            mutableStateOf(true)
        }

        var text by rememberSaveable { mutableStateOf(state.value.icsFileUrl ?: "") }
        var animateLogo by rememberSaveable { mutableStateOf(true) }
        val interactionSource = remember { MutableInteractionSource() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(1f)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    animateLogo = !animateLogo
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = animateLogo,
                enter = slideInVertically(),
                exit = slideOutHorizontally()
            ) {
                EzIcalLogoGif()
            }
            AnimatedVisibility(
                visible = !animateLogo,
                enter = slideInHorizontally(),
                exit = slideOutVertically(

                )
            ) {
                EzIcalLogoStatic()
            }

        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .weight(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    stringResource(id = R.string.openUrlText), color = if (switchState) {
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
                    stringResource(id = R.string.openFileText), color = if (!switchState) {
                        Color.Gray
                    } else {
                        Color.Unspecified
                    }
                )
            }

            if (switchState) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .background(
                            color = Color.Unspecified,
                            shape = CircleShape,
                        ),
                    value = stringResource(id = R.string.openFileDescription), onValueChange = {},
                    enabled = false,
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Gray,
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
                    Text(text = stringResource(id = R.string.openFileButtonText))
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
                    label = { Text(text = stringResource(id = R.string.eventUrlLabel)) },
                    placeholder = { Text(text = stringResource(id = R.string.eventUrlPlaceholder)) },
//                colors = TextFieldDefaults.textFieldColors(
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent,
//                ),
                    maxLines = 1
                )

                Button(onClick = {
                    openFromUrlCLicked(text)
                }) {
                    Text(text = stringResource(id = R.string.openUrlButtonText))
                }
            }
        }

    }
}


