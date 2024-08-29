package org.arul.todo.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorIndicator(message: String?=null){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 28.dp),
        contentAlignment = Alignment.Center
    ){
        Text(text = message ?: "Empty")
    }
}