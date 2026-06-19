package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.CCTVDatabase
import com.example.data.WatchlistRepository
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.CCTVViewModel

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // 1. Initialize Rooms SQLite database and access repository
    val database = CCTVDatabase.getDatabase(applicationContext)
    val repository = WatchlistRepository(database.watchlistDao())

    // 2. Instantiate ViewModels with manual Dependency Injection Factory
    val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CCTVViewModel(repository) as T
        }
    })[CCTVViewModel::class.java]

    setContent {
      MyApplicationTheme {
        var showSplash by remember { mutableStateOf(true) }

        if (showSplash) {
          SplashScreen(
            viewModel = viewModel,
            onTimeout = { showSplash = false }
          )
        } else {
          DashboardScreen(viewModel = viewModel)
        }
      }
    }
  }
}

