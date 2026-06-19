package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun DetailOverlay(
    media: MediaItem,
    viewModel: CCTVViewModel,
    onClose: () -> Unit
) {
    val watchlistItems by viewModel.watchList.collectAsState(initial = emptyList())
    val isSaved = watchlistItems.any { it.mediaId == media.id }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.75f))
            .clickable { onClose() }
            .testTag("detail_overlay_scrim")
    ) {
        // High-fidelity Slide up sheet panel
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.78f)
                .align(Alignment.BottomCenter)
                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .background(DarkCardBg)
                .border(
                    width = 1.dp,
                    color = Color.White.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                )
                .clickable(enabled = false) {} // Prevent click-through closing
                .testTag("detail_panel")
        ) {
            // Elegant background top gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                NeonRed.copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Drag handle bar
                Box(
                    modifier = Modifier
                        .size(width = 40.dp, height = 4.dp)
                        .background(Color(0xFF33333C), RoundedCornerShape(2.dp))
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 10.dp)
                )

                // Header close icon button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp, bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (media.isMovie) "CCTV 電影首映" else "CCTV 獨家影集",
                        color = NeonRed,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.06f), RoundedCornerShape(12.dp))
                            .size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }

                // Title details
                Text(
                    text = media.title,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 30.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = media.tagline,
                    color = SlateGrey,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stats rows
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "★ ${media.rating}",
                        color = GoldTop10,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = media.year,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = media.duration,
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF22222B), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = media.genre,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action buttons: Play and Toggle Watchlist (Room persist)
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.startPlayback(media)
                            onClose() // Dismiss detail overlay during full cinema playback
                        },
                        modifier = Modifier
                            .weight(1.8f)
                            .height(48.dp)
                            .testTag("detail_play_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "play")
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("立刻播放", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.toggleWatchlist(media) },
                        modifier = Modifier
                            .weight(1.2f)
                            .height(48.dp)
                            .testTag("watchlist_toggle_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSaved) Color(0xFF1E2822) else Color.White.copy(alpha = 0.08f),
                            contentColor = if (isSaved) Color(0xFF66BB6A) else Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        border = if (isSaved) {
                            androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2E4E38))
                        } else null
                    ) {
                        Icon(
                            imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "watchlist"
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isSaved) "已入片單" else "加入片單",
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = Color(0xFF22222D), thickness = 1.dp)

                // Bottom Content Scroll Section (Details & Cast or Show directory)
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp)
                ) {
                    item {
                        Text(
                            text = "故事大綱",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                        Text(
                            text = media.description,
                            color = SlateGrey,
                            fontSize = 13.sp,
                            lineHeight = 20.sp,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }

                    item {
                        Text(
                            text = "演出陣容",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(bottom = 18.dp)
                        ) {
                            media.cast.forEach { actor ->
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    // Custom avatar mock graphic inside Canvas
                                    Box(
                                        modifier = Modifier
                                            .size(46.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .background(Color(0xFF22222D)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = actor.take(1),
                                            color = NeonRed,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = actor,
                                        color = SlateGrey,
                                        fontSize = 11.sp
                                    )
                                }
                            }
                        }
                    }

                    // Series episode cards if it's a TV show
                    if (!media.isMovie && media.episodes.isNotEmpty()) {
                        item {
                            HorizontalDivider(color = Color(0xFF22222D), thickness = 1.dp)
                            Text(
                                text = "劇集列表 (${media.episodes.size} 集)",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 16.dp, bottom = 10.dp)
                            )
                        }

                        itemsIndexed(media.episodes) { index, epName ->
                            EpisodeRowItem(
                                index = index + 1,
                                name = epName,
                                duration = "45 Mins",
                                onClick = {
                                    // Create a temporary mock episode item to start playing
                                    val episodeMedia = media.copy(
                                        id = "${media.id}_ep_${index + 1}",
                                        title = "${media.title} - $epName",
                                        duration = "45m"
                                    )
                                    viewModel.startPlayback(episodeMedia)
                                    onClose()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EpisodeRowItem(index: Int, name: String, duration: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$index",
                color = NeonRed,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(28.dp)
            )

            Column {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = duration,
                    color = SlateGrey,
                    fontSize = 11.sp
                )
            }
        }

        // Play Mini button icon
        Box(
            modifier = Modifier
                .background(Color.White.copy(alpha = 0.08f), RoundedCornerShape(10.dp))
                .size(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = "Play Episode",
                tint = NeonRed,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
