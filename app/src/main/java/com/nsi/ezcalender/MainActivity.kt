package com.nsi.ezcalender

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import com.nsi.ezcalender.ui.EzCalenderApp
import com.nsi.ezcalender.ui.MainViewModel
import com.nsi.ezcalender.ui.theme.EzCalenderTheme


var PERMISSIONS = arrayOf(
    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE
)

class MainActivity : ComponentActivity() {


//    private val requestPermissionLauncher =
//        registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        )
//        { permissions ->
//            for (isGranted in permissions.values) {
//                if (isGranted) {
//                    println("granted")
//
//                    // Permission is granted. Continue the action or workflow in your
//                    // app.
//                } else {
//                    println("not granted")
//                    // Explain to the user that the feature is unavailable because the
//                    // features requires a permission that the user has denied. At the
//                    // same time, respect the user's decision. Don't link to system
//                    // settings in an effort to convince the user to change their
//                    // decision.
//                }
//            }
//        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Environment.isExternalStorageManager()) {
            val getPermission = Intent()
            getPermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            startActivity(getPermission)
        }

//        if (!hasPermission()) {
//            requestPermissionLauncher.launch(PERMISSIONS)
//        }

        val mainVewModel: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            EzCalenderTheme {
                EzCalenderApp(mainVewModel)
            }
        }
    }

//    private fun hasPermission(): Boolean {
//        var granted = false
//        for (permission in PERMISSIONS) {
//            val result =
//                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
//            granted = granted && result == PackageManager.PERMISSION_GRANTED
//        }
//        return granted
//    }




}
