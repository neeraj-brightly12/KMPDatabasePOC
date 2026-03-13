package com.brightly.kmpdatabasepoc

import androidx.compose.ui.window.ComposeUIViewController
import com.brightly.kmpdatabasepoc.data.database.DatabaseFactory
import com.brightly.kmpdatabasepoc.ui.App

fun MainViewController() = ComposeUIViewController {

    App(DatabaseFactory())

}