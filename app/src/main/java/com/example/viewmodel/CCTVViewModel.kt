package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CinemaCatalog
import com.example.data.MediaItem
import com.example.data.WatchlistItem
import com.example.data.WatchlistRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CCTVViewModel(private val repository: WatchlistRepository) : ViewModel() {

    // --- Splash Particle State ---
    private val _splashProgress = MutableStateFlow(0f)
    val splashProgress: StateFlow<Float> = _splashProgress.asStateFlow()

    private val _splashState = MutableStateFlow("aggregating") // "aggregating", "zooming", "completed"
    val splashState: StateFlow<String> = _splashState.asStateFlow()

    // --- Auth State ---
    private val _userEmail = MutableStateFlow<String?>(null)
    val userEmail: StateFlow<String?> = _userEmail.asStateFlow()

    private val _isMockLoggedIn = MutableStateFlow(false)
    val isMockLoggedIn: StateFlow<Boolean> = _isMockLoggedIn.asStateFlow()

    // --- Tab Navigation & Screen Routing ---
    private val _currentTab = MutableStateFlow("home") // "home", "movies", "tv", "search", "watchlist", "profile"
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    // --- Selected Film for Detail overlay ---
    private val _selectedMedia = MutableStateFlow<MediaItem?>(null)
    val selectedMedia: StateFlow<MediaItem?> = _selectedMedia.asStateFlow()

    // --- Watchlist Persistence State from Room SQLite ---
    val watchList = repository.watchlistFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // --- Film Catalog State ---
    val catalog: List<MediaItem> = CinemaCatalog.items

    // --- Interactive Search ---
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filteredItems = MutableStateFlow<List<MediaItem>>(CinemaCatalog.items)
    val filteredItems: StateFlow<List<MediaItem>> = _filteredItems.asStateFlow()

    // --- Cinematic Simulated Multi-player Engine ---
    private val _activePlayingMedia = MutableStateFlow<MediaItem?>(null)
    val activePlayingMedia: StateFlow<MediaItem?> = _activePlayingMedia.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _playbackProgress = MutableStateFlow(0f) // 0f to 1f
    val playbackProgress: StateFlow<Float> = _playbackProgress.asStateFlow()

    private val _currentSpeed = MutableStateFlow(1.0f) // 1.0, 1.25, 1.5, 2.0
    val currentSpeed: StateFlow<Float> = _currentSpeed.asStateFlow()

    private val _subtitlesEnabled = MutableStateFlow(true)
    val subtitlesEnabled: StateFlow<Boolean> = _subtitlesEnabled.asStateFlow()

    private val _subtitlesText = MutableStateFlow("「CCTV」Stories Everywhere.")
    val subtitlesText: StateFlow<String> = _subtitlesText.asStateFlow()

    private val _isPipMode = MutableStateFlow(false)
    val isPipMode: StateFlow<Boolean> = _isPipMode.asStateFlow()

    private val _showSkipIntro = MutableStateFlow(false)
    val showSkipIntro: StateFlow<Boolean> = _showSkipIntro.asStateFlow()

    private val _playbackTimeSeconds = MutableStateFlow(0)
    val playbackTimeSeconds: StateFlow<Int> = _playbackTimeSeconds.asStateFlow()

    private var playbackJob: Job? = null

    init {
        runSplashAnimation()
    }

    // Run custom logo particle logic
    fun runSplashAnimation() {
        viewModelScope.launch {
            _splashState.value = "aggregating"
            _splashProgress.value = 0f
            // Smoothly progress particles over 1.5s
            for (i in 1..30) {
                delay(50)
                _splashProgress.value = i / 30f
            }
            _splashState.value = "zooming"
            delay(500)
            _splashState.value = "completed"
        }
    }

    // Auth Operations
    fun login(email: String) {
        viewModelScope.launch {
            _userEmail.value = if (email.isBlank()) "guest@cctv.com" else email
            _isMockLoggedIn.value = true
        }
    }

    fun logout() {
        _isMockLoggedIn.value = false
        _userEmail.value = null
        _currentTab.value = "home"
    }

    // Tab Operations
    fun selectTab(tab: String) {
        _currentTab.value = tab
    }

    // Detail Operations
    fun selectMedia(item: MediaItem?) {
        _selectedMedia.value = item
    }

    // Bookmark Watchlist Room Persistence Operations
    fun toggleWatchlist(item: MediaItem) {
        viewModelScope.launch {
            val isCurrentlyBookmarked = repository.isBookmarked(item.id)
            if (isCurrentlyBookmarked) {
                repository.removeFromWatchlist(item.id)
            } else {
                repository.addToWatchlist(
                    WatchlistItem(
                        mediaId = item.id,
                        title = item.title,
                        tagline = item.tagline,
                        rating = item.rating,
                        year = item.year,
                        duration = item.duration,
                        genre = item.genre,
                        isMovie = item.isMovie
                    )
                )
            }
        }
    }

    fun isBookmarked(mediaId: String, callback: (Boolean) -> Unit) {
        viewModelScope.launch {
            callback(repository.isBookmarked(mediaId))
        }
    }

    // Search Filtering
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isBlank()) {
            _filteredItems.value = CinemaCatalog.items
        } else {
            _filteredItems.value = CinemaCatalog.items.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.genre.contains(query, ignoreCase = true) ||
                it.cast.any { actor -> actor.contains(query, ignoreCase = true) }
            }
        }
    }

    // Simulated Cinema Player Operations
    fun startPlayback(media: MediaItem) {
        _activePlayingMedia.value = media
        _isPlaying.value = true
        _playbackProgress.value = 0f
        _playbackTimeSeconds.value = 0
        _isPipMode.value = false
        _showSkipIntro.value = false
        _subtitlesText.value = "「CCTV」故事，無處不在。"

        runPlaybackSimulation()
    }

    fun stopPlayback() {
        playbackJob?.cancel()
        _isPlaying.value = false
        _activePlayingMedia.value = null
    }

    fun togglePlayPause() {
        _isPlaying.value = !_isPlaying.value
        if (_isPlaying.value) {
            runPlaybackSimulation()
        } else {
            playbackJob?.cancel()
        }
    }

    fun skipForward10s() {
        val newProgress = (_playbackProgress.value + 0.05f).coerceIn(0f, 1f)
        _playbackProgress.value = newProgress
        val totalSec = if (activePlayingMedia.value?.isMovie == true) 7200 else 2400
        _playbackTimeSeconds.value = (newProgress * totalSec).toInt()
        updateSubtitlesForTime(_playbackTimeSeconds.value)
    }

    fun skipBackward10s() {
        val newProgress = (_playbackProgress.value - 0.05f).coerceIn(0f, 1f)
        _playbackProgress.value = newProgress
        val totalSec = if (activePlayingMedia.value?.isMovie == true) 7200 else 2400
        _playbackTimeSeconds.value = (newProgress * totalSec).toInt()
        updateSubtitlesForTime(_playbackTimeSeconds.value)
    }

    fun setSpeed(speed: Float) {
        _currentSpeed.value = speed
    }

    fun toggleSubtitles() {
        _subtitlesEnabled.value = !_subtitlesEnabled.value
    }

    fun setPipMode(enabled: Boolean) {
        _isPipMode.value = enabled
    }

    fun skipIntro() {
        _playbackProgress.value = 0.15f
        val totalSec = if (activePlayingMedia.value?.isMovie == true) 7200 else 2400
        _playbackTimeSeconds.value = (_playbackProgress.value * totalSec).toInt()
        _showSkipIntro.value = false
        _subtitlesText.value = "跳過片頭成功，精彩節目繼續中"
    }

    private fun runPlaybackSimulation() {
        playbackJob?.cancel()
        playbackJob = viewModelScope.launch {
            val totalSec = if (activePlayingMedia.value?.isMovie == true) 7200 else 2400 // Mock 2 hours or 40 mins
            while (_playbackProgress.value < 1.0f) {
                // Adapt timer speed to user speed factor
                val secondsPerTick = 1f * _currentSpeed.value
                delay(1000)
                if (_isPlaying.value) {
                    _playbackTimeSeconds.value += secondsPerTick.toInt()
                    val nextProgress = _playbackTimeSeconds.value.toFloat() / totalSec
                    if (nextProgress >= 1f) {
                        _playbackProgress.value = 1f
                        break
                    } else {
                        _playbackProgress.value = nextProgress
                    }

                    // Handles intro show up between 5s and 25s
                    _showSkipIntro.value = (_playbackTimeSeconds.value in 5..25)

                    // Subtitle updates
                    updateSubtitlesForTime(_playbackTimeSeconds.value)
                }
            }
            // Auto Next episode or completion banner
            delay(1000)
            _subtitlesText.value = "節目播放完畢。隨時觀看下一集！"
            _isPlaying.value = false
        }
    }

    private fun updateSubtitlesForTime(currentSec: Int) {
        val isMovie = activePlayingMedia.value?.isMovie == true
        val title = activePlayingMedia.value?.title ?: ""
        
        _subtitlesText.value = when {
            currentSec < 5 -> "「CCTV 電影頻道」 榮譽呈現: $title"
            currentSec in 5..10 -> "本片由 CCTV 全球故事網絡數位修復，音效支援 7.1 環繞聲"
            currentSec in 11..15 -> "在廣袤無垠的星空下，人類與未知的命運第一次正面交鋒..."
            currentSec in 16..20 -> "［宏大的背景管弦樂響起，低音震撼沈重］"
            currentSec in 21..25 -> "阿明：「我撿到了這個，真的沒關係嗎？」"
            currentSec in 26..31 -> "大師：「姻緣天注定，一旦撿起來，就推不掉了...」"
            currentSec in 32..38 -> "「當我們凝視深淵時，深淵也正在回望並記錄我們。」"
            currentSec in 39..45 -> "警政署統計，近年智慧晶片洩漏引發的犯罪率上升了 240%"
            currentSec in 46..55 -> "阿雪：「不，這不只是代碼，這是我姐姐的靈魂記錄！」"
            currentSec in 56..70 -> "［背景急促的高空警報音，直升機螺旋槳聲盤旋］"
            currentSec in 71..100 -> "「CCTV 首播精選：精彩片段正持續載入...」"
            else -> "「CCTV Stories Everywhere」- 正持續為您串流精彩大片"
        }
    }
}
