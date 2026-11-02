package com.example.sharedflowdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.SharedFlow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val demoViewModel: DemoViewModel = viewModel()
            ScreenSetup(demoViewModel.sharedFlow)
        }
    }
}

@androidx.compose.runtime.Composable
fun ScreenSetup(sharedFlow: SharedFlow<Int>) {
    MainScreen(sharedFlow)
}

@androidx.compose.runtime.Composable
fun MainScreen(sharedFlow: SharedFlow<Int>) {
    val collectedValues = remember { mutableStateListOf<Int>() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(sharedFlow, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            sharedFlow.collect { value ->
                println("Collecting $value")
                collectedValues.add(value)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(collectedValues) { value ->
                Text(text = "Collected Value = $value")
            }
        }
    }
}