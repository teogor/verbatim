package dev.teogor.verbatim

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.teogor.verbatim.shared.ui.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Verbatim",
    ) {
        App()
    }
}