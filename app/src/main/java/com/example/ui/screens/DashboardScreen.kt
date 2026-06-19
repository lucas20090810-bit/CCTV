package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
fun DashboardScreen(viewModel: CCTVViewModel) {
    val currentTab by viewModel.currentTab.collectAsState()
    val isMockLoggedIn by viewModel.isMockLoggedIn.collectAsState()
    val selectedMedia by viewModel.selectedMedia.collectAsState()
    val activePlayingMedia by viewModel.activePlayingMedia.collectAsState()
    var showAdminDashboard by remember { mutableStateOf(false) }

    if (!isMockLoggedIn) {
        AuthScreen(viewModel = viewModel)
    } else {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = DarkSlateBg,
                topBar = {
                    AppHeader(viewModel = viewModel)
                },
                bottomBar = {
                    NavigationBar(
                        containerColor = Color(0xFF121214),
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .testTag("bottom_nav")
                            .navigationBarsPadding()
                    ) {
                        val navItems = listOf(
                            Triple("home", "首頁", Icons.Default.Home),
                            Triple("movies", "電影", Icons.Default.Movie),
                            Triple("tv", "影集", Icons.Default.Tv),
                            Triple("search", "搜尋", Icons.Default.Search),
                            Triple("watchlist", "片單", Icons.Default.Bookmark),
                            Triple("profile", "設定", Icons.Default.Person)
                        )
                        
                        navItems.forEach { (route, label, icon) ->
                            val isSelected = currentTab == route
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { viewModel.selectTab(route) },
                                icon = {
                                    Icon(
                                        imageVector = icon,
                                        contentDescription = label,
                                        tint = if (isSelected) NeonRed else SlateGrey
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) NeonRed else SlateGrey
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = NeonRedGlow
                                )
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    when (currentTab) {
                        "home" -> CinemaHomeScreen(viewModel)
                        "movies" -> MoviesScreen(viewModel)
                        "tv" -> TVShowsScreen(viewModel)
                        "search" -> SearchScreen(viewModel)
                        "watchlist" -> WatchlistScreen(viewModel)
                        "profile" -> ProfileScreen(viewModel, onShowAdmin = { showAdminDashboard = true })
                    }

                    // Bottom detail view overlay sheet
                    selectedMedia?.let { media ->
                        DetailOverlay(
                            media = media,
                            viewModel = viewModel,
                            onClose = { viewModel.selectMedia(null) }
                        )
                    }
                }
            }

            // Fullscreen active player layer
            activePlayingMedia?.let { playing ->
                PlayerScreen(
                    media = playing,
                    viewModel = viewModel,
                    onClose = { viewModel.stopPlayback() }
                )
            }

            // Real-time developer control overlay
            if (showAdminDashboard) {
                AdminDashboardOverlay(
                    viewModel = viewModel,
                    onClose = { showAdminDashboard = false }
                )
            }
        }
    }
}

@Composable
fun AppHeader(viewModel: CCTVViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(horizontal = 24.dp, vertical = 14.dp)
            .testTag("app_header"),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "CCTV",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    letterSpacing = (-1.5).sp
                )
                Text(
                    text = "+",
                    color = NeonRed,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.padding(start = 2.dp, bottom = 12.dp)
                )
            }
            Text(
                text = "STORIES EVERYWHERE.",
                color = SlateGrey,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                modifier = Modifier.offset(y = (-4).dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.selectTab("search") },
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White.copy(alpha = 0.05f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "搜尋",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(18.dp)
                )
            }

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.linearGradient(
                            colors = listOf(Color(0xFF44444F), Color(0xFF1E1E24))
                        )
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.2f), CircleShape)
                    .clickable { viewModel.selectTab("profile") }
                    .testTag("avatar_button"),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = NeonRed.copy(alpha = 0.4f),
                        radius = size.width / 4,
                        center = Offset(size.width / 2, size.height / 2.2f)
                    )
                    drawCircle(
                        color = Color.White.copy(alpha = 0.25f),
                        radius = size.width / 3,
                        center = Offset(size.width / 2, size.height + (size.height / 10))
                    )
                }
            }
        }
    }
}

// ==========================================
// 1. 首頁 (CinemaHomeScreen)
// ==========================================
@Composable
fun CinemaHomeScreen(viewModel: CCTVViewModel) {
    val items by viewModel.catalog.collectAsState()
    val continueWatchingItems by viewModel.continueWatchingList.collectAsState(initial = emptyList())
    
    // Choose specific sci-fi hack 2055 as hero item
    val heroItem = items.firstOrNull { it.id == "rank4" } ?: items.firstOrNull() ?: return


    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("home_screen"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        // Hero Wide Banner
        item {
            HeroBanner(media = heroItem, viewModel = viewModel)
        }

        // Today's Hot
        item {
            val hotMovies = items.filter { it.category == "hot" }
            Spacer(modifier = Modifier.height(16.dp))
            MediaHorizontalRow(
                title = "今日熱門 (Trending Today)",
                mediaList = hotMovies,
                viewModel = viewModel,
                isWidescreen = true
            )
        }

        // Taiwan Top 10 rankings!
        item {
            val rankings = items.filter { it.category == "ranking" }.sortedBy { it.rank }
            Spacer(modifier = Modifier.height(20.dp))
            RankingTop10Row(
                title = "台灣排行榜 Top 10",
                mediaList = rankings,
                viewModel = viewModel
            )
        }

        // Dedicated recommendations
        item {
            val recommends = items.filter { it.category == "recommend" }
            Spacer(modifier = Modifier.height(20.dp))
            MediaHorizontalRow(
                title = "為你推薦 (Recommended for You)",
                mediaList = recommends,
                viewModel = viewModel
            )
        }

        // Continue watching (Simulate watching states)
        item {
            val watchingItem1 = items.firstOrNull { it.id == "rank1" }
            val watchingItem2 = items.firstOrNull { it.id == "rec2" }
            val watchings = listOfNotNull(watchingItem1, watchingItem2)
            Spacer(modifier = Modifier.height(20.dp))
            MediaHorizontalRow(
                title = "正在觀看 (Continue Watching)",
                mediaList = watchings,
                viewModel = viewModel,
                showProgress = true
            )
        }

        // New arrivals
        item {
            val news = items.filter { it.category == "new" }
            Spacer(modifier = Modifier.height(20.dp))
            MediaHorizontalRow(
                title = "最新上架 (New Releases)",
                mediaList = news,
                viewModel = viewModel,
                showNewBadge = true
            )
        }

        // Upcoming Releases
        item {
            val upcomings = items.filter { it.category == "upcoming" }
            Spacer(modifier = Modifier.height(20.dp))
            MediaHorizontalRow(
                title = "即將上映 (Upcoming)",
                mediaList = upcomings,
                viewModel = viewModel,
                showUpcomingBadge = true
            )
        }

        // Floating Progress / Continue Watching Banner (Artistic Flair Theme) - Connect with actual Room DB values!
        val lastWatchedRecord = continueWatchingItems.firstOrNull()
        if (lastWatchedRecord != null) {
            val matchedItem = items.firstOrNull { it.id == lastWatchedRecord.mediaId }
            if (matchedItem != null) {
                item {
                    Spacer(modifier = Modifier.height(28.dp))
                    ArtisticContinueWatchingBanner(
                        media = matchedItem,
                        progress = lastWatchedRecord.progress,
                        viewModel = viewModel
                    )
                }
            }
        } else {
            // Default highlight if they haven't watched anything yet (first launch)
            val fallbackItem = items.firstOrNull { it.id == "rank1" } ?: items.firstOrNull()
            fallbackItem?.let { matchedItem ->
                item {
                    Spacer(modifier = Modifier.height(28.dp))
                    ArtisticContinueWatchingBanner(
                        media = matchedItem,
                        progress = 0.25f, // Suggested teaser highlight
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// ==========================================
// Hero Banner Widget
// ==========================================
@Composable
fun HeroBanner(media: MediaItem, viewModel: CCTVViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(440.dp)
            .background(DarkCardBg)
    ) {
        // Custom background graphics representing cyberpunk neon city
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val brush = Brush.verticalGradient(
                        colors = listOf(
                            NeonRed.copy(alpha = 0.25f),
                            Color(0xFF0F0E13).copy(alpha = 0.1f),
                            DarkSlateBg
                        )
                    )
                    drawRect(brush = brush)
                    
                    // Draw atmospheric cyberpunk neon gridlines
                    val gridWidth = size.width
                    val gridHeight = size.height
                    val lines = 8
                    for (i in 0..lines) {
                        val y = gridHeight * (i.toFloat() / lines)
                        drawLine(
                            color = NeonRed.copy(alpha = 0.04f * (i.toFloat() / lines)),
                            start = Offset(0f, y),
                            end = Offset(gridWidth, y),
                            strokeWidth = 2f
                        )
                    }
                }
        )

        // Title layout and glowing text details
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Transparent,
                            DarkSlateBg.copy(alpha = 0.7f),
                            DarkSlateBg
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(modifier = Modifier.fillMaxWidth(0.9f)) {
                // Slogan Tag
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .background(NeonRed, RoundedCornerShape(4.dp))
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "精選首播",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = media.genre,
                        color = SlateGrey,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Cinematic main title
                Text(
                    text = media.title,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 38.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Slogan text
                Text(
                    text = media.tagline,
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = media.description,
                    color = SlateGrey,
                    fontSize = 12.sp,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Buttons: Play Now & Info
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { viewModel.startPlayback(media) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "play"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("觀看 (Play Now)", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = { viewModel.selectMedia(media) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.White.copy(alpha = 0.15f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "info"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("詳情 (More Info)")
                    }
                }
            }
        }
    }
}

// ==========================================
// Horizontal Scroll Reels
// ==========================================
@Composable
fun MediaHorizontalRow(
    title: String,
    mediaList: List<MediaItem>,
    viewModel: CCTVViewModel,
    isWidescreen: Boolean = false,
    showProgress: Boolean = false,
    showNewBadge: Boolean = false,
    showUpcomingBadge: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mediaList) { item ->
                MovieCard(
                    media = item,
                    onClick = { viewModel.selectMedia(item) },
                    isWidescreen = isWidescreen,
                    showProgress = showProgress,
                    showNewBadge = showNewBadge,
                    showUpcomingBadge = showUpcomingBadge
                )
            }
        }
    }
}

// Taiwan Top 10 layout
@Composable
fun RankingTop10Row(
    title: String,
    mediaList: List<MediaItem>,
    viewModel: CCTVViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
            Text(
                text = "LIVE FROM TAIWAN",
                color = NeonRed,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "VIEW ALL",
                    color = SlateGrey,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.clickable { viewModel.selectTab("movies") }
                )
            }
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            items(mediaList.size) { index ->
                val item = mediaList[index]
                val itemRank = index + 1
                Box(
                    modifier = Modifier
                        .width(180.dp)
                        .height(170.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    // Giant Neon Ranking Number layered on the background
                    Text(
                        text = "$itemRank",
                        color = Color(0xFF161620),
                        fontSize = 130.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.SansSerif,
                        modifier = Modifier
                            .offset(x = (-8).dp, y = (10).dp)
                            .drawBehind {
                                // Draw glowing shadow behind text for modern neon look
                                drawCircle(
                                    color = NeonRed.copy(alpha = 0.08f),
                                    radius = 80f,
                                    center = Offset(40f, 120f)
                                )
                            }
                    )

                    // Card shifted to overlap rightwards
                    Box(
                        modifier = Modifier
                            .offset(x = 65.dp, y = 0.dp)
                    ) {
                        MovieCard(
                            media = item,
                            onClick = { viewModel.selectMedia(item) },
                            isWidescreen = false,
                            cardHeight = 150
                        )
                    }
                }
            }
        }
    }
}

// Universal Movie Card
@Composable
fun MovieCard(
    media: MediaItem,
    onClick: () -> Unit,
    isWidescreen: Boolean = false,
    showProgress: Boolean = false,
    showNewBadge: Boolean = false,
    showUpcomingBadge: Boolean = false,
    cardHeight: Int = 160
) {
    val widthValue = if (isWidescreen) 240.dp else 115.dp
    val heightValue = cardHeight.dp

    Card(
        modifier = Modifier
            .width(widthValue)
            .height(heightValue)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(10.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = DarkCardBg
        )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // High-fidelity dynamic Canvas cover
            Canvas(modifier = Modifier.fillMaxSize()) {
                val brush = Brush.verticalGradient(
                    colors = if (media.id.startsWith("rank")) {
                        listOf(Color(0xFF26121D), Color(0xFF13091B))
                    } else if (media.isMovie) {
                        listOf(Color(0xFF131D2A), Color(0xFF0F121C))
                    } else {
                        listOf(Color(0xFF1D221F), Color(0xFF0F1311))
                    }
                )
                drawRect(brush = brush)

                // Draw atmospheric grid or simple glowing visual circle
                drawCircle(
                    color = NeonRed.copy(alpha = 0.12f),
                    radius = size.minDimension / 1.5f,
                    center = Offset(size.width, 0f)
                )
            }

            // Text info overlaid elegantly
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = if (media.isMovie) "電影" else "影集",
                            color = NeonRed,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "★",
                            color = GoldTop10,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = if (media.rating.startsWith("期")) "9.5" else media.rating,
                            color = AccentWhite(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = media.title,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(2.dp))
                    
                    Text(
                        text = "${media.year} • ${media.duration}",
                        color = SlateGrey,
                        fontSize = 9.sp
                    )
                }
            }

            // Progress bar simulation for "Continue Watching"
            if (showProgress) {
                val pct = if (media.id == "rank1") 0.65f else 0.35f
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color.Gray.copy(alpha = 0.3f))
                        .align(Alignment.BottomStart)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(pct)
                            .background(NeonRed)
                    )
                }
            }

            // New Badge
            if (showNewBadge) {
                Box(
                    modifier = Modifier
                        .background(NeonRed, RoundedCornerShape(bottomEnd = 6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "NEW",
                        color = Color.White,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            // Upcoming Badge
            if (showUpcomingBadge) {
                Box(
                    modifier = Modifier
                        .background(GoldTop10, RoundedCornerShape(bottomStart = 6.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "期",
                        color = Color.Black,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

// ==========================================
// 2. 電影頁 (MoviesScreen)
// ==========================================
@Composable
fun MoviesScreen(viewModel: CCTVViewModel) {
    val allItems by viewModel.catalog.collectAsState()
    val items = allItems.filter { it.isMovie }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("movies_screen")
    ) {
        // Section Title
        Text(
            text = "電影大觀 (Cinematic Movies)",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                MovieCard(
                    media = item,
                    onClick = { viewModel.selectMedia(item) },
                    cardHeight = 150
                )
            }
        }
    }
}

// ==========================================
// 3. 影集頁 (TVShowsScreen)
// ==========================================
@Composable
fun TVShowsScreen(viewModel: CCTVViewModel) {
    val allItems by viewModel.catalog.collectAsState()
    val items = allItems.filter { !it.isMovie }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("tv_shows_screen")
    ) {
        // Section Title
        Text(
            text = "旗艦影集 (Original TV Shows)",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items) { item ->
                MovieCard(
                    media = item,
                    onClick = { viewModel.selectMedia(item) },
                    cardHeight = 150
                )
            }
        }
    }
}

// ==========================================
// 4. 搜尋頁 (SearchScreen)
// ==========================================
@Composable
fun SearchScreen(viewModel: CCTVViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    val list by viewModel.filteredItems.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("search_screen")
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = { viewModel.updateSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            placeholder = { Text("搜尋電影、影集、導演或演員...", color = SlateGrey) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = NeonRed
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = NeonRed,
                unfocusedBorderColor = Color(0xFF23232D),
                focusedContainerColor = DarkCardBg,
                unfocusedContainerColor = DarkCardBg,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "🔎",
                        fontSize = 40.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = "未搜尋到任何符合的影片",
                        color = SlateGrey,
                        fontSize = 14.sp
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(list) { item ->
                    MovieCard(
                        media = item,
                        onClick = { viewModel.selectMedia(item) },
                        cardHeight = 150
                    )
                }
            }
        }
    }
}

// ==========================================
// 5. 我的片單 (WatchlistScreen) - Connect Flow values from Room SQLite Database
// ==========================================
@Composable
fun WatchlistScreen(viewModel: CCTVViewModel) {
    val list by viewModel.watchList.collectAsState(initial = emptyList())
    val allItems by viewModel.catalog.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("watchlist_screen")
    ) {
        Text(
            text = "我的片單 (CCTV Watchlist)",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.padding(16.dp)
        )

        if (list.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎬",
                        fontSize = 48.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    Text(
                        text = "您的收藏夾目前為空",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "在首頁選擇心儀的電影並點選「加入片單」，即可在此快速離線查找並觀看。",
                        color = SlateGrey,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(list) { localItem ->
                    val matchedCatalogItem = allItems.firstOrNull { it.id == localItem.mediaId }
                    if (matchedCatalogItem != null) {
                        Box(contentAlignment = Alignment.TopEnd) {
                            MovieCard(
                                media = matchedCatalogItem,
                                onClick = { viewModel.selectMedia(matchedCatalogItem) },
                                cardHeight = 150
                            )
                            // Clean red deletion indicator badge
                            Box(
                                modifier = Modifier
                                    .offset(x = (-4).dp, y = 4.dp)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.7f), CircleShape)
                                    .clickable { viewModel.toggleWatchlist(matchedCatalogItem) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove",
                                    tint = NeonRed,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// 6. 設定與個人檔案 (ProfileScreen)
// ==========================================
@Composable
fun ProfileScreen(viewModel: CCTVViewModel, onShowAdmin: () -> Unit) {
    val email by viewModel.userEmail.collectAsState()
    var streamingQuality by remember { mutableStateOf("Ultra HD 4K") }
    var useSubtitles by remember { mutableStateOf(true) }
    var subtitleLanguage by remember { mutableStateOf("繁體中文 (Traditional Chinese)") }
    var hasCleanedCaches by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .testTag("profile_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(NeonRed, CircleShape)
                    .border(2.dp, Color.White, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "V",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = email ?: "CCTV 尊榮會員",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Premium Titanium Account",
                color = GoldTop10,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "串流設定 (Streaming Parameters)",
                    color = NeonRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            item {
                SettingRow(title = "播放畫質", value = streamingQuality) {
                    streamingQuality = if (streamingQuality.contains("4K")) "Full HD 1080P" else "Ultra HD 4K"
                }
            }

            item {
                SettingRow(title = "默認字幕顯示", value = if (useSubtitles) "開啟" else "關閉") {
                    useSubtitles = !useSubtitles
                }
            }

            item {
                settingDivider()
            }

            item {
                Text(
                    text = "快取與存儲 (Storage Cache Control)",
                    color = NeonRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                SettingRow(title = "緩存容量清理", value = if (hasCleanedCaches) "已清理 (0.0 MB)" else "清理快取 (184.2 MB)") {
                    hasCleanedCaches = true
                }
            }

            item {
                settingDivider()
            }

            item {
                Text(
                    text = "品牌與團隊資訊 (Brand info)",
                    color = NeonRed,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                SettingRow(title = "品牌版本", value = "v1.0.5 Cinematic Master") {}
            }

            item {
                SettingRow(title = "品牌標語", value = "Stories Everywhere.") {}
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
                // Developer Dashboard entry
                Button(
                    onClick = onShowAdmin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .border(1.dp, NeonRed.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonRed.copy(alpha = 0.1f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Admin",
                        tint = NeonRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("進入開發者管理後台 (Admin Panel)", fontWeight = FontWeight.Bold, color = Color.White)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { viewModel.logout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White.copy(alpha = 0.12f),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("安全登出 (Log Out)", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun SettingRow(title: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, color = Color.White, fontSize = 14.sp)
        Text(text = value, color = SlateGrey, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun settingDivider() {
    HorizontalDivider(
        color = Color(0xFF22222D),
        thickness = 1.dp,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}

@Composable
fun ArtisticContinueWatchingBanner(media: MediaItem, progress: Float, viewModel: CCTVViewModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFF0F0F12))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.05f),
                shape = RoundedCornerShape(24.dp)
            )
            .clickable { viewModel.selectMedia(media) }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Simulated Thumbnail (Left)
            Box(
                modifier = Modifier
                    .size(width = 48.dp, height = 64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF222226))
                    .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.BottomStart
            ) {
                // Miniature cover graphic
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = listOf(NeonRed.copy(alpha = 0.15f), Color(0xFF0F0F12))
                        )
                    )
                }
                
                // Red mini progress line
                Box(
                    modifier = Modifier
                        .fillMaxWidth(coerceProgress(progress))
                        .height(3.dp)
                        .background(NeonRed)
                )
            }
 
            // Central Info Section
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "繼續觀看 (CONTINUE WATCHING)",
                    color = SlateGrey,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp
                )
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = media.title,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                // Custom Premium visual progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Color(0xFF22222B), CircleShape)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(coerceProgress(progress))
                            .fillMaxHeight()
                            .background(NeonRed, CircleShape)
                            .drawBehind {
                                // Subtle Red Glow aura
                                drawCircle(
                                    color = NeonRed.copy(alpha = 0.4f),
                                    radius = 8f,
                                    center = Offset(size.width, size.height / 2f)
                                )
                            }
                    )
                }
            }
 
            // Quick Play Button (Right)
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .border(1.dp, Color.White.copy(alpha = 0.15f), CircleShape)
                    .clickable { viewModel.startPlayback(media) },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "繼續觀看播放",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun coerceProgress(p: Float): Float {
    return if (p <= 0.01f) 0.05f else if (p > 1.0f) 1.0f else p
}

// Helper colors
private fun AccentWhite(): Color = Color(0xFFF5F5FA)
