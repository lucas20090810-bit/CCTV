package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
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
fun AdminDashboardOverlay(
    viewModel: CCTVViewModel,
    onClose: () -> Unit
) {
    var loggedInAsAdmin by remember { mutableStateOf(false) }
    var adminPassword by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.95f))
            .testTag("admin_overlay"),
        contentAlignment = Alignment.Center
    ) {
        if (!loggedInAsAdmin) {
            // Elegant admin login gate
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(DarkSlateBg)
                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                    .padding(28.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Lock Icon
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color.White.copy(alpha = 0.05f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Shield",
                        tint = NeonRed,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "CCTV ADMIN PORTAL",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp,
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    text = "請輸入開發者管理密碼以進行即時內容管理",
                    color = SlateGrey,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                OutlinedTextField(
                    value = adminPassword,
                    onValueChange = {
                        adminPassword = it
                        loginError = ""
                    },
                    label = { Text("管理密碼 (Admin Password)") },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonRed,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                        focusedLabelColor = NeonRed,
                        cursorColor = NeonRed,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                if (loginError.isNotEmpty()) {
                    Text(
                        text = loginError,
                        color = NeonRed,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onClose,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.White
                        ),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("取消", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (adminPassword == "admin123" || adminPassword == "admin") {
                                loggedInAsAdmin = true
                            } else {
                                loginError = "密碼錯誤！提示：請輸入 admin"
                            }
                        },
                        modifier = Modifier.weight(1.5f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonRed,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("進入後台 (Login)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // Actual Admin Dashboard Screen Content
            AdminPanelMain(viewModel = viewModel, onClose = onClose)
        }
    }
}

@Composable
fun AdminPanelMain(
    viewModel: CCTVViewModel,
    onClose: () -> Unit
) {
    var activeTab by remember { mutableStateOf("movies") } // "movies", "users", "banners"
    val catalog by viewModel.catalog.collectAsState()

    // Add / Edit Dialog state
    var showAddEditDialog by remember { mutableStateOf(false) }
    var selectedMediaItemForEdit by remember { mutableStateOf<MediaItem?>(null) }

    Scaffold(
        containerColor = Color(0xFF0C0C0E),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF141418))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Brand Left
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(NeonRed, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Admin",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "CCTV CONTROL ROOM",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Next.js Web-Style Admin Console",
                            color = SlateGrey,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Close Right
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Admin",
                        tint = SlateGrey
                    )
                }
            }
        },
        bottomBar = {
            // Responsive Web Mock Sticky Footer Navigation Bar
            NavigationBar(
                containerColor = Color(0xFF141418),
                modifier = Modifier.height(68.dp),
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = activeTab == "movies",
                    onClick = { activeTab = "movies" },
                    icon = { Icon(Icons.Default.MovieCreation, "Media Catalog") },
                    label = { Text("影片庫管理", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonRed,
                        selectedTextColor = NeonRed,
                        unselectedIconColor = SlateGrey,
                        unselectedTextColor = SlateGrey,
                        indicatorColor = NeonRedGlow
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "banners",
                    onClick = { activeTab = "banners" },
                    icon = { Icon(Icons.Default.ViewCarousel, "Hero Banners") },
                    label = { Text("首頁推薦管理", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonRed,
                        selectedTextColor = NeonRed,
                        unselectedIconColor = SlateGrey,
                        unselectedTextColor = SlateGrey,
                        indicatorColor = NeonRedGlow
                    )
                )
                NavigationBarItem(
                    selected = activeTab == "users",
                    onClick = { activeTab = "users" },
                    icon = { Icon(Icons.Default.SupervisedUserCircle, "User Moderation") },
                    label = { Text("用戶分析稽核", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = NeonRed,
                        selectedTextColor = NeonRed,
                        unselectedIconColor = SlateGrey,
                        unselectedTextColor = SlateGrey,
                        indicatorColor = NeonRedGlow
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (activeTab) {
                "movies" -> {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Quick Add Action
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "媒體庫資源清單",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "現有 ${catalog.size} 部熱播影視內容",
                                    color = SlateGrey,
                                    fontSize = 11.sp
                                )
                            }

                            Button(
                                onClick = {
                                    selectedMediaItemForEdit = null
                                    showAddEditDialog = true
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Icon(Icons.Default.Add, "Add", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("上架影片", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // Film listing scrollable element
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(catalog) { item ->
                                AdminMediaRowItem(
                                    item = item,
                                    onEdit = {
                                        selectedMediaItemForEdit = item
                                        showAddEditDialog = true
                                    },
                                    onDelete = {
                                        viewModel.deleteMedia(item.id)
                                    }
                                )
                            }
                        }
                    }
                }
                "banners" -> {
                    AdminBannersTab(viewModel = viewModel)
                }
                "users" -> {
                    AdminUsersTab(viewModel = viewModel)
                }
            }

            // Slide up custom full screen Add/Edit form Dialog overlay
            if (showAddEditDialog) {
                AddEditMediaDialogScreen(
                    item = selectedMediaItemForEdit,
                    onSave = { newItem ->
                        if (selectedMediaItemForEdit == null) {
                            viewModel.addMedia(newItem)
                        } else {
                            viewModel.editMedia(newItem)
                        }
                        showAddEditDialog = false
                    },
                    onDismiss = { showAddEditDialog = false }
                )
            }
        }
    }
}

@Composable
fun AdminMediaRowItem(
    item: MediaItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(DarkCardBg)
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Visual Type Icon (Movie or TV original series)
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(if (item.isMovie) Color(0xFF1C2C40) else Color(0xFF381B26)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (item.isMovie) Icons.Default.Movie else Icons.Default.Tv,
                    contentDescription = "Type",
                    tint = if (item.isMovie) Color(0xFF5AB6FF) else NeonRed,
                    modifier = Modifier.size(20.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(horizontal = 4.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = item.rating,
                            color = GoldTop10,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.genre} • ${item.year} • ${item.duration}",
                    color = SlateGrey,
                    fontSize = 11.sp,
                    maxLines = 1
                )
            }
        }

        // Action Buttons Row (Right)
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            IconButton(
                onClick = onEdit,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteSweep,
                    contentDescription = "Delete",
                    tint = NeonRed.copy(alpha = 0.9f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

// Full form fields display sheet
@Composable
fun AddEditMediaDialogScreen(
    item: MediaItem?,
    onSave: (MediaItem) -> Unit,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf(item?.title ?: "") }
    var tagline by remember { mutableStateOf(item?.tagline ?: "") }
    var description by remember { mutableStateOf(item?.description ?: "") }
    var rating by remember { mutableStateOf(item?.rating ?: "9.5") }
    var year by remember { mutableStateOf(item?.year ?: "2026") }
    var duration by remember { mutableStateOf(item?.duration ?: "2h 00m") }
    var isMovie by remember { mutableStateOf(item?.isMovie ?: true) }
    var category by remember { mutableStateOf(item?.category ?: "new") } // "hot", "recommend", "ranking", "new", "upcoming"
    var genre by remember { mutableStateOf(item?.genre ?: "懸疑 / 驚悚") }
    var castInput by remember { mutableStateOf(item?.cast?.joinToString(", ") ?: "許光漢, 林柏宏") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.96f))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.95f)
                .clip(RoundedCornerShape(20.dp))
                .background(DarkSlateBg)
                .border(1.dp, Color.White.copy(alpha = 0.08f), RoundedCornerShape(20.dp))
                .padding(24.dp)
        ) {
            // Form title
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (item == null) "上架全新影音資源" else "編輯影視資料",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, "Dismiss", tint = SlateGrey)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Media Type Toggle
                item {
                    Column {
                        Text("影音類型 (Media Type)", color = SlateGrey, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isMovie) NeonRed else Color.White.copy(alpha = 0.05f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { isMovie = true },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "電影 (Movie)",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                )
                            }

                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (!isMovie) NeonRed else Color.White.copy(alpha = 0.05f)
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { isMovie = false },
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "影集 / 番劇 (TV/Anime)",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                )
                            }
                        }
                    }
                }

                // Title field
                item {
                    AdminFieldInput(label = "片名 / 節目名稱", value = title, onValueChange = { title = it })
                }

                // Tagline
                item {
                    AdminFieldInput(label = "促銷宣傳語 (Tagline)", value = tagline, onValueChange = { tagline = it })
                }

                // Description
                item {
                    AdminFieldInput(
                        label = "影視簡介 (Description)",
                        value = description,
                        onValueChange = { description = it },
                        singleLine = false
                    )
                }

                // Grid detail elements
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.weight(1f)) {
                            AdminFieldInput(label = "評分 / 期待度", value = rating, onValueChange = { rating = it })
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            AdminFieldInput(label = "上映年份", value = year, onValueChange = { year = it })
                        }
                        Box(modifier = Modifier.weight(1f)) {
                            AdminFieldInput(label = "影片時長 / 總集數", value = duration, onValueChange = { duration = it })
                        }
                    }
                }

                // Category selection
                item {
                    Column {
                        Text("上傳定位類別 (Home Category)", color = SlateGrey, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val categories = listOf(
                                "hot" to "今日熱門",
                                "recommend" to "為您推薦",
                                "ranking" to "經典排行",
                                "new" to "最新上映"
                            )
                            categories.forEach { (catId, label) ->
                                val selected = category == catId
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (selected) Color.White.copy(alpha = 0.15f) else Color.White.copy(alpha = 0.02f)
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { category = catId }
                                        .border(
                                            0.5.dp,
                                            if (selected) NeonRed else Color.Transparent,
                                            RoundedCornerShape(6.dp)
                                        ),
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = label,
                                        color = if (selected) Color.White else SlateGrey,
                                        fontSize = 10.sp,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // Genre and Cast
                item {
                    AdminFieldInput(label = "題材分類 (Genre)", value = genre, onValueChange = { genre = it })
                }

                item {
                    AdminFieldInput(label = "演員陣容 (Cast - 用逗號半角區隔)", value = castInput, onValueChange = { castInput = it })
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Footer Submit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("拋棄更改")
                }

                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            val castList = castInput.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                            val mediaId = item?.id ?: "media_${System.currentTimeMillis()}"
                            val rankVal = item?.rank ?: 0
                            val episodesSet = if (isMovie) emptyList() else listOf(
                                "Ep 1: 重案疑雲第一集",
                                "Ep 2: 真相背後的影子",
                                "Ep 3: 絕地大突圍",
                                "Ep 4: 被揭露的反噬"
                            )
                            
                            val newItem = MediaItem(
                                id = mediaId,
                                title = title,
                                tagline = tagline,
                                description = description,
                                rating = rating,
                                year = year,
                                duration = duration,
                                isMovie = isMovie,
                                category = category,
                                genre = genre,
                                rank = rankVal,
                                cast = castList,
                                episodes = episodesSet
                            )
                            onSave(newItem)
                        }
                    },
                    modifier = Modifier.weight(1.5f),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                    shape = RoundedCornerShape(8.dp),
                    enabled = title.isNotBlank()
                ) {
                    Text("確認保存上架", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdminFieldInput(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = SlateGrey,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.White.copy(alpha = 0.3f),
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else 4,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun AdminBannersTab(viewModel: CCTVViewModel) {
    var selectedFeaturedVideo by remember { mutableStateOf("rank4") }
    val catalog by viewModel.catalog.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "手頁頂部 Banners 輪播圖配置",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "指定任意熱門影視做為 App 頂部黃金廣告大版面焦點看板",
                    color = SlateGrey,
                    fontSize = 11.sp
                )
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "💎 當前主打焦點 Banner 推廣中:",
                        color = GoldTop10,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )

                    catalog.forEach { item ->
                        val isSelected = selectedFeaturedVideo == item.id
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) Color.White.copy(alpha = 0.05f) else Color.Transparent)
                                .clickable { selectedFeaturedVideo = item.id }
                                .padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.title,
                                color = if (isSelected) NeonRed else Color.White.copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )

                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedFeaturedVideo = item.id },
                                colors = RadioButtonDefaults.colors(selectedColor = NeonRed, unselectedColor = SlateGrey)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminUsersTab(viewModel: CCTVViewModel) {
    val watchlistItems by viewModel.watchList.collectAsState(initial = emptyList())
    val email by viewModel.userEmail.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "用戶活躍度與 moderation 稽核分析面版",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "安全日誌審計與在線活動稽查",
                    color = SlateGrey,
                    fontSize = 11.sp
                )
            }
        }

        // Stats blocks
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                // Total User hours
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text("總播放活躍時長", color = SlateGrey, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("18.4 Hrs", color = ColorsAdmin.GreenOk, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Text("高於本周平均 24%", color = SlateGrey, fontSize = 9.sp)
                    }
                }

                // Watchlist counter
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        Text("收藏夾總承載項", color = SlateGrey, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("${watchlistItems.size} 部電影", color = NeonRed, fontSize = 20.sp, fontWeight = FontWeight.Black)
                        Text("Room SQLite 同步正常", color = SlateGrey, fontSize = 9.sp)
                    }
                }
            }
        }

        // Active user session detail
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "在線終端管理 (On-Device User Sessions)",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkCardBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(ColorsAdmin.GreenOk.copy(alpha = 0.15f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Devices, "Device", tint = ColorsAdmin.GreenOk, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text(text = email ?: "visitor@cctv.com", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(modifier = Modifier.size(6.dp).background(ColorsAdmin.GreenOk, CircleShape))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(text = "Android Stream Client • 在線活躍", color = SlateGrey, fontSize = 10.sp)
                                }
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(ColorsAdmin.GreenOk.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                "免審監控中",
                                color = ColorsAdmin.GreenOk,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

object ColorsAdmin {
    val GreenOk = Color(0xFF00FF66)
}
