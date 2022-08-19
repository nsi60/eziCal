package com.nsi.ezcalender

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.nsi.ezcalender.impl.ICSReader
import com.nsi.ezcalender.ui.EzCalenderApp
import com.nsi.ezcalender.ui.theme.EzCalenderTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


var PERMISSIONS = arrayOf(
    Manifest.permission.MANAGE_EXTERNAL_STORAGE,
    Manifest.permission.READ_EXTERNAL_STORAGE,
    Manifest.permission.WRITE_EXTERNAL_STORAGE,
    Manifest.permission.INTERNET

)

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var icsReader: ICSReader

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        )
        { permissions ->
            for (isGranted in permissions.values) {
                if (isGranted) {
                    println("granted")

                    // Permission is granted. Continue the action or workflow in your
                    // app.
                } else {
                    println("not granted")
                    // Explain to the user that the feature is unavailable because the
                    // features requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!Environment.isExternalStorageManager()) {
            val getPermission = Intent()
            getPermission.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
            startActivity(getPermission)
        }

        if (!hasPermission()) {
            requestPermissionLauncher.launch(PERMISSIONS)
        }

        setContent {
            EzCalenderTheme {
                EzCalenderApp()
            }
        }
    }

    private fun hasPermission(): Boolean {
        var granted = false
        for (permission in PERMISSIONS) {
            val result =
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            granted = granted && result == PackageManager.PERMISSION_GRANTED
        }
        return granted
    }


}
