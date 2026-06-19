package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkSlateBg
import com.example.ui.theme.NeonRed
import com.example.viewmodel.CCTVViewModel
import kotlin.math.cos
import kotlin.math.sin

data class LogoParticle(
    val startX: Float,
    val startY: Float,
    val targetX: Float,
    val targetY: Float,
    val color: Color,
    val size: Float
)

@Composable
fun SplashScreen(viewModel: CCTVViewModel, onTimeout: () -> Unit) {
    val progress by viewModel.splashProgress.collectAsState()
    val state by viewModel.splashState.collectAsState()

    val scaleAnimate by animateFloatAsState(
        targetValue = if (state == "zooming") 1.8f else 1.0f,
        animationSpec = tween(600, easing = FastOutSlowInEasing)
    )

    val fadeAnimate by animateFloatAsState(
        targetValue = if (state == "completed") 0f else 1f,
        animationSpec = tween(400)
    )

    // Setup coordinates for letters 'C', 'C', 'T', 'V'
    val particles = remember {
        val list = mutableListOf<LogoParticle>()
        
        // Let's model letters in a 300x120 container centered at (0,0)
        // Letter C #1 (Arc centered at -100, 0)
        for (i in 0..25) {
            val angle = Math.toRadians((60 + (i * 9)).toDouble()) // Arc from 60 to 285 deg
            val r = 35f
            val tx = -110f + (r * cos(angle)).toFloat()
            val ty = (r * sin(angle)).toFloat()
            list.add(createParticle(tx, ty, true))
        }

        // Letter C #2 (Arc centered at -40, 0)
        for (i in 0..25) {
            val angle = Math.toRadians((60 + (i * 9)).toDouble())
            val r = 35f
            val tx = -40f + (r * cos(angle)).toFloat()
            val ty = (r * sin(angle)).toFloat()
            list.add(createParticle(tx, ty, true))
        }

        // Letter T (Bar at x = 10..40, y = -35. Stem at x = 25, y = -35..35)
        // Top bar
        for (i in 0..15) {
            val tx = 10f + (i * 3.5f)
            val ty = -35f
            list.add(createParticle(tx, ty, false))
        }
        // Stem
        for (i in 0..20) {
            val tx = 36f
            val ty = -35f + (i * 3.5f)
            list.add(createParticle(tx, ty, false))
        }

        // Letter V (Slope left from 70,-35 to 90,35. Slope right from 90,35 to 110,-35)
        for (i in 0..15) {
            val ratio = i / 15f
            val tx = 75f + (20f * ratio)
            val ty = -35f + (70f * ratio)
            list.add(createParticle(tx, ty, false))
        }
        for (i in 0..15) {
            val ratio = i / 15f
            val tx = 95f + (20f * ratio)
            val ty = 35f - (70f * ratio)
            list.add(createParticle(tx, ty, false))
        }

        list
    }

    LaunchedEffect(state) {
        if (state == "completed") {
            onTimeout()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkSlateBg)
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.scale(scaleAnimate)
        ) {
            Box(
                modifier = Modifier
                    .size(width = 320.dp, height = 180.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val cx = size.width / 2
                    val cy = size.height / 2
                    
                    particles.forEach { p ->
                        // Interpolate coordinates based on splashProgress
                        val currentX = cx + p.startX + (p.targetX - p.startX) * progress
                        val currentY = cy + p.startY + (p.targetY - p.startY) * progress
                        
                        // Draw particle item
                        drawCircle(
                            color = p.color,
                            radius = p.size,
                            center = Offset(currentX, currentY),
                            alpha = fadeAnimate
                        )

                        // Draw neat neon background glow for merged letters
                        if (progress > 0.8f) {
                            drawCircle(
                                color = NeonRed.copy(alpha = 0.08f),
                                radius = p.size * 2.5f,
                                center = Offset(currentX, currentY),
                                alpha = fadeAnimate
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle Brand slogan
            Text(
                text = "STORIES EVERYWHERE.",
                color = Color.White.copy(alpha = progress.coerceIn(0f, 1f) * 0.7f),
                letterSpacing = 8.sp,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

private fun createParticle(tx: Float, ty: Float, isBrandRed: Boolean): LogoParticle {
    // Generate scattered points inside screen boundaries of wide canvas
    val ang = Math.random() * 2 * Math.PI
    val r = 250f + (Math.random() * 150f).toFloat() // Scatter wide
    val startX = (r * cos(ang)).toFloat()
    val startY = (r * sin(ang)).toFloat()
    
    val col = if (isBrandRed) {
        if (Math.random() > 0.3) NeonRed else Color.White
    } else {
        if (Math.random() > 0.4) Color.White else NeonRed
    }
    
    val sz = 2.5f + (Math.random() * 3.5f).toFloat()
    
    return LogoParticle(
        startX = startX,
        startY = startY,
        targetX = tx * 2f, // Scale catalog a bit for visibility
        targetY = ty * 2f,
        color = col,
        size = sz
    )
}
