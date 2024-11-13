package com.comic.mp3_player

import Mp3PlayerViewModel
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.comic.mp3_player.ui.theme.Mp3playerTheme

class MainActivity : ComponentActivity() {
    private val viewModel: Mp3PlayerViewModel by viewModels()
    private val selectAudioLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            viewModel.setUri(this, it)
        }
    }

    private fun pickAudio() {
        selectAudioLauncher.launch("audio/*")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        enableEdgeToEdge()
        setContent {
            Mp3playerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MP3PlayerApp(
                        onPickAudio = { pickAudio() },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@Composable
fun MP3PlayerApp(
    onPickAudio: () -> Unit,
    viewModel: Mp3PlayerViewModel
) {
    val isPlaying by viewModel.isPlaying

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onPickAudio() },
        ) {
            Text("Choose Music")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.play() },
            enabled = !isPlaying
        ) {
            Text("Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.pause() },
            enabled = isPlaying
        ) {
            Text("Pause")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.stop() },
            enabled = isPlaying
        ) {
            Text("Stop")
        }
    }
}
