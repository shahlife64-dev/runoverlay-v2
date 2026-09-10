package com.example.telemetryrunner

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { TelemetryRunnerApp() }
    }
}

@Composable
fun TelemetryRunnerApp() {
    val context = LocalContext.current
    var videoUri by remember { mutableStateOf<Uri?>(null) }
    var activityUri by remember { mutableStateOf<Uri?>(null) }
    var offsetSeconds by remember { mutableFloatStateOf(0f) }
    var player by remember { mutableStateOf<ExoPlayer?>(null) }

    val videoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {}
            videoUri = it
        }
    }

    val activityPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            try {
                context.contentResolver.takePersistableUriPermission(
                    it, Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (_: SecurityException) {}
            activityUri = it
        }
    }

    DisposableEffect(videoUri) {
        player?.release()
        player = videoUri?.let {
            ExoPlayer.Builder(context).build().apply {
                setMediaItem(MediaItem.fromUri(it))
                prepare()
                playWhenReady = false
            }
        }
        onDispose { player?.release() }
    }

    MaterialTheme(colorScheme = darkColorScheme(primary = Color(0xFF64B5F6))) {
        Column(
            Modifier.fillMaxSize().background(Color(0xFF101114))
                .verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            Text("Telemetry Runner", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("V1 — running video telemetry overlay", color = Color.LightGray)
            Spacer(Modifier.height(16.dp))

            if (player != null) {
                AndroidView(
                    factory = { ctx -> PlayerView(ctx).apply {
                        this.player = player
                        useController = true
                    }},
                    modifier = Modifier.fillMaxWidth().height(230.dp)
                )
            } else {
                Box(
                    Modifier.fillMaxWidth().height(230.dp)
                        .background(Color.Black, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) { Text("Select a running video", color = Color.Gray) }
            }

            Spacer(Modifier.height(10.dp))
            Button(
                onClick = { videoPicker.launch(arrayOf("video/*")) },
                Modifier.fillMaxWidth()
            ) { Text(if (videoUri == null) "1. Select Video" else "Change Video") }

            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { activityPicker.launch(arrayOf("*/*")) },
                Modifier.fillMaxWidth()
            ) { Text(if (activityUri == null) "2. Select FIT / GPX / TCX" else "Change Activity File") }

            Spacer(Modifier.height(18.dp))
            Text("Telemetry overlay", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))
            Box(
                Modifier.fillMaxWidth().height(180.dp)
                    .background(Color.Black, RoundedCornerShape(12.dp)).padding(16.dp)
            ) {
                Column {
                    Text("10 SEP 2026     09:42:18", color = Color.White, fontWeight = FontWeight.Bold)
                    Text("00:52:31", color = Color.White, fontSize = 27.sp, fontWeight = FontWeight.Bold)
                    Text("12.47 km", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    Text("5:02 /km", color = Color.White, fontSize = 22.sp)
                    Text("♥ 172 bpm   •   178 spm   •   ↑ 84 m", color = Color.White)
                }
            }

            Spacer(Modifier.height(18.dp))
            Text("Video / activity synchronization", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
            Text("Offset: ${offsetSeconds.toInt()} seconds", color = Color.LightGray)
            Slider(
                value = offsetSeconds,
                onValueChange = { offsetSeconds = it },
                valueRange = -120f..120f
            )

            Button(
                enabled = videoUri != null && activityUri != null,
                onClick = { /* Renderer is next milestone */ },
                Modifier.fillMaxWidth()
            ) { Text("Export MP4") }

            Spacer(Modifier.height(18.dp))
            Text(
                "Prototype build: selection, playback, overlay preview and sync controls. " +
                "Actual FIT/GPX/TCX parsing and video compositing are not yet implemented.",
                color = Color.Gray, fontSize = 13.sp
            )
        }
    }
}
