package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DarkCardBg
import com.example.ui.theme.DarkSlateBg
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.SlateGrey
import com.example.viewmodel.CCTVViewModel

@Composable
fun AuthScreen(viewModel: CCTVViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf("") }
    var animatedVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        animatedVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkSlateBg, Color(0xFF020202))
                )
            )
            .testTag("auth_screen"),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = animatedVisible,
            enter = fadeIn(tween(800)) + slideInVertically(initialOffsetY = { 100 }, animationSpec = tween(800))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .clip(RoundedCornerShape(24.dp))
                    .background(DarkCardBg)
                    .border(
                        width = 1.dp,
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Glow text Logo "CCTV"
                Text(
                    text = "CCTV",
                    color = NeonRed,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "STORIES EVERYWHERE",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 6.sp,
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                Text(
                    text = "原創與極致的串流影音體驗",
                    color = SlateGrey,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Input values
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        errorMsg = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("email_input"),
                    label = { Text("電子郵件 (Email)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "EmailIcon",
                            tint = if (email.isNotEmpty()) NeonRed else SlateGrey
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonRed,
                        unfocusedBorderColor = WarmGreyBorder(),
                        focusedLabelColor = NeonRed,
                        cursorColor = NeonRed,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        errorMsg = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("password_input"),
                    label = { Text("登入密碼 (Password)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "LockIcon",
                            tint = if (password.isNotEmpty()) NeonRed else SlateGrey
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonRed,
                        unfocusedBorderColor = WarmGreyBorder(),
                        focusedLabelColor = NeonRed,
                        cursorColor = NeonRed,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                if (errorMsg.isNotEmpty()) {
                    Text(
                        text = errorMsg,
                        color = NeonRed,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .align(Alignment.Start)
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))

                // Submit button
                Button(
                    onClick = {
                        if (email.isBlank()) {
                            errorMsg = "請輸入有效的電子郵件地址"
                        } else if (password.length < 6) {
                            errorMsg = "密碼長度必須大於 6 位數"
                        } else {
                            viewModel.login(email)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("login_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonRed,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "start",
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = "開啟沉浸之旅 (Sign In)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Quick test guest pass
                TextButton(
                    onClick = {
                        viewModel.login("visitor@cctv.com")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("visitor_login_button")
                ) {
                    Text(
                        text = "快速體驗 (Browse as Guest)",
                        color = NeonRed,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun WarmGreyBorder(): Color = Color(0xFF2E2E38)
