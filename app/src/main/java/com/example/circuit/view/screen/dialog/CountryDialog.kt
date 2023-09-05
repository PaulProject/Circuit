package com.example.circuit.view.screen.dialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun CountryDialog(
    name: String,
    onDismissRequest: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        CountryDialog(name)
    }
}

@Composable
private fun CountryDialog(
    name: String,
) {
    Box(
        modifier = Modifier
            .size(200.dp)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(size = 16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(text = name)
    }
}