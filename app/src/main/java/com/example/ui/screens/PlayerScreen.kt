package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Forward10
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PictureInPictureAlt
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay10
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.Subtitles
import androidx.compose.material.icons.filled.SubtitlesOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MediaItem
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSlateBg
import com.example.ui.theme.GoldTop10
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.SlateGrey
import com.example.viewmodel.CCTVViewModel
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun PlayerScreen(
    media: MediaItem,
    viewModel: CCTVViewModel,
    onClose: () -> Unit
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val progress by viewModel.playbackProgress.collectAsState()
    val timeSec by viewModel.playbackTimeSeconds.collectAsState()
    val speed by viewModel.currentSpeed.collectAsState()
    val subEnabled by viewModel.subtitlesEnabled.collectAsState()
    val subText by viewModel.subtitlesText.collectAsState()
    val isPip by viewModel.isPipMode.collectAsState()
    val showSkip by viewModel.showSkipIntro.collectAsState()

    // Setup coordinates for draggable Picture In Picture window
    var pipOffsetX by remember { mutableStateOf(0f) }
    var pipOffsetY by remember { mutableStateOf(0f) }

    // Floating Pulse animation for cinematic atmosphere
    val infiniteTransition = rememberInfiniteTransition(label = "playerPulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    if (isPip) {
        // Renders customized Picture in Picture floating view
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent)
                .testTag("pip_layer")
        ) {
            Box(
                modifier = Modifier
                    .offset { IntOffset(pipOffsetX.roundToInt(), pipOffsetY.roundToInt()) }
                    .size(width = 240.dp, height = 140.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(DarkCardBg)
                    .border(2.dp, NeonRed, RoundedCornerShape(16.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { change, dragAmount ->
                            change.consume()
                            pipOffsetX += dragAmount.x
                            pipOffsetY += dragAmount.y
                        }
                    }
                    .testTag("pip_window"),
                contentAlignment = Alignment.Center
            ) {
                // Interactive miniature player canvas
                PlayerVisualizerCanvas(isPlaying = isPlaying, pulse = pulseScale)

                // Miniature overlays
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = media.title,
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = { viewModel.setPipMode(false) },
                            modifier = Modifier.size(20.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fullscreen,
                                contentDescription = "Maximize",
                                tint = NeonRed,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    // Mini Subtitle in Picture-in-picture
                    if (subEnabled) {
                        Text(
                            text = subText,
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                                .padding(2.dp)
                        )
                    }

                    // Mini Progress bar
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(Color.White.copy(alpha = 0.2f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(progress)
                                .background(NeonRed)
                        )
                    }
                }
            }
        }
    } else {
        // Enforces full immersion landscape standard dark cinematic screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .testTag("cinemic_player_screen")
        ) {
            // Screen wide visualizer canvas representing running frames
            PlayerVisualizerCanvas(isPlaying = isPlaying, pulse = pulseScale)

            // Header HUD Overlay
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Black.copy(alpha = 0.8f), Color.Transparent)
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.08f), CircleShape)
                            .size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Exit Player",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = media.title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "正在串流播放 • Subtitles Active",
                            color = SlateGrey,
                            fontSize = 11.sp
                        )
                    }
                }

                // Header right tools
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Subtitles selector
                    IconButton(onClick = { viewModel.toggleSubtitles() }) {
                        Icon(
                            imageVector = if (subEnabled) Icons.Default.Subtitles else Icons.Default.SubtitlesOff,
                            contentDescription = "Subtitles",
                            tint = if (subEnabled) NeonRed else Color.White
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    // Picture in Picture trigger
                    IconButton(onClick = { viewModel.setPipMode(true) }) {
                        Icon(
                            imageVector = Icons.Default.PictureInPictureAlt,
                            contentDescription = "PIP Mode",
                            tint = Color.White
                        )
                    }
                }
            }

            // Cinematic Subtitle container
            if (subEnabled) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .align(Alignment.BottomCenter)
                        .offset(y = (-110).dp)
                        .background(Color.Black.copy(alpha = 0.65f), RoundedCornerShape(12.dp))
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subText,
                        color = AccentWhite(),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )
                }
            }

            // Skip Intro floating button
            AnimatedVisibility(
                visible = showSkip,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-32).dp, y = (-180).dp)
            ) {
                Button(
                    onClick = { viewModel.skipIntro() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.border(2.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                ) {
                    Text(
                        text = "▶  跳過片頭 (Skip Intro)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }

            // Footer HUD controls panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.85f))
                        )
                    )
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Media Timeline slider
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatTime(timeSec),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.width(60.dp)
                    )

                    Slider(
                        value = progress,
                        onValueChange = {}, // Live reading state purely, or expand scrub support
                        modifier = Modifier.weight(1f),
                        colors = SliderDefaults.colors(
                            activeTrackColor = NeonRed,
                            inactiveTrackColor = Color.White.copy(alpha = 0.15f),
                            thumbColor = NeonRed
                        )
                    )

                    val totalSec = if (media.isMovie) 7200 else 2400
                    Text(
                        text = formatTime(totalSec),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.width(60.dp),
                        textAlign = TextAlign.End
                    )
                }

                // Player physical command control panel
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left speed factor selectors
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        listOf(1.0f, 1.25f, 1.5f, 2.0f).forEach { s ->
                            val isActive = speed == s
                            Box(
                                modifier = Modifier
                                    .padding(end = 6.dp)
                                    .background(
                                        if (isActive) NeonRed else Color.White.copy(alpha = 0.08f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.setSpeed(s) }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "${s}x",
                                    color = if (isActive) Color.White else SlateGrey,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Centered scrubbing triggers
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Rewind 10s
                        IconButton(onClick = { viewModel.skipBackward10s() }) {
                            Icon(
                                imageVector = Icons.Default.Replay10,
                                contentDescription = "Rewind",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Play / Pause Circle
                        Box(
                            modifier = Modifier
                                .background(NeonRed, CircleShape)
                                .size(50.dp)
                                .clickable { viewModel.togglePlayPause() }
                                .border(2.dp, Color.White.copy(alpha = 0.8f), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "PlayPauseState",
                                tint = Color.White,
                                modifier = Modifier.size(30.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Forward 10s
                        IconButton(onClick = { viewModel.skipForward10s() }) {
                            Icon(
                                imageVector = Icons.Default.Forward10,
                                contentDescription = "Forward",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    // Right auto-play next episode indicator
                    IconButton(
                        onClick = {
                            // If series, load temporary next mock index
                            val nextEpMedia = media.copy(
                                id = "${media.id}_next",
                                title = "${media.title} (續看章節)",
                                duration = "40m"
                            )
                            viewModel.startPlayback(nextEpMedia)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Next Episode",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        }
    }
}

// Custom Sci-Fi particle canvas to represent background dynamic rendering elements
@Composable
fun PlayerVisualizerCanvas(isPlaying: Boolean, pulse: Float) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBg)
    ) {
        val cx = size.width / 2
        val cy = size.height / 2

        // Draw elegant circular geometric audio waveforms radiating outwards
        if (isPlaying) {
            val rays = 18
            for (i in 0 until rays) {
                val angle = Math.toRadians((i * (360f / rays)).toDouble())
                // Create erratic bouncing bar sizes representing movie soundtracks
                val amplitude = 80f + (30f * sin(System.currentTimeMillis() / 250f + i)).toFloat()
                
                val innerR = 100f * pulse
                val outerR = (100f + amplitude) * pulse

                val startX = cx + (innerR * cos(angle)).toFloat()
                val startY = cy + (innerR * sin(angle)).toFloat()
                val endX = cx + (outerR * cos(angle)).toFloat()
                val endY = cy + (outerR * sin(angle)).toFloat()

                // Neon active spectrum bars
                drawLine(
                    color = NeonRed.copy(alpha = 0.45f),
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 3f
                )

                // Neon outer aura dots
                drawCircle(
                    color = Color.White.copy(alpha = 0.35f),
                    radius = 3f,
                    center = Offset(endX, endY)
                )
            }
        }

        // Standard cinema centered concentric rings
        drawCircle(
            color = NeonRed.copy(alpha = 0.05f),
            radius = 120f * pulse,
            center = Offset(cx, cy),
            style = Stroke(width = 2f)
        )
        drawCircle(
            color = Color.White.copy(alpha = 0.02f),
            radius = 240f * pulse,
            center = Offset(cx, cy),
            style = Stroke(width = 1f)
        )
    }
}

private fun formatTime(secs: Int): String {
    val hrs = secs / 3600
    val mins = (secs % 3600) / 60
    val remainingSecs = secs % 60
    return if (hrs > 0) {
        String.format("%02d:%02d:%02d", hrs, mins, remainingSecs)
    } else {
        String.format("%02d:%02d", mins, remainingSecs)
    }
}

private fun AccentWhite(): Color = Color(0xFFF5F5FA)
