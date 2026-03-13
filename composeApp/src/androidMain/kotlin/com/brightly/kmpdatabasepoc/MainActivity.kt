package com.brightly.kmpdatabasepoc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.brightly.kmpdatabasepoc.data.database.DatabaseFactory
import com.brightly.kmpdatabasepoc.ui.App

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            App(DatabaseFactory(applicationContext))

        }
    }
}