package com.sawt.alasdiqa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Waves
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF202033)
private val CardNavy = Color(0xFF30334F)
private val Green = Color(0xFF08C765)
private val Muted = Color(0xFFB9BDCC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { SawtFriendsApp() }
    }
}

private data class Friend(
    val name: String,
    val country: String,
    val language: String,
    val online: Boolean,
    val avatar: String
)

@Composable
fun SawtFriendsApp() {
    val friends = listOf(
        Friend("طارق", "السعودية", "العربية", true, "🌙"),
        Friend("سارة", "العراق", "العربية", true, "🌸"),
        Friend("محمد", "الأردن", "العربية", false, "🎧"),
        Friend("ليان", "الكويت", "العربية", true, "⭐")
    )
    var selectedTab by remember { mutableStateOf("اكتشف") }
    var search by remember { mutableStateOf("") }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Green,
            background = Navy,
            surface = CardNavy
        )
    ) {
        Scaffold(
            containerColor = Navy,
            bottomBar = {
                NavigationBar(containerColor = Color(0xFF292A40)) {
                    NavigationBarItem(
                        selected = selectedTab == "اكتشف",
                        onClick = { selectedTab = "اكتشف" },
                        icon = { Icon(Icons.Default.Search, contentDescription = null) },
                        label = { Text("اكتشف") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "المحادثات",
                        onClick = { selectedTab = "المحادثات" },
                        icon = { Icon(Icons.Default.ChatBubble, contentDescription = null) },
                        label = { Text("المحادثات") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "المفضلة",
                        onClick = { selectedTab = "المفضلة" },
                        icon = { Icon(Icons.Default.Favorite, contentDescription = null) },
                        label = { Text("المفضلة") }
                    )
                    NavigationBarItem(
                        selected = selectedTab == "الإعدادات",
                        onClick = { selectedTab = "الإعدادات" },
                        icon = { Icon(Icons.Default.Settings, contentDescription = null) },
                        label = { Text("حسابي") }
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 18.dp)
            ) {
                Spacer(Modifier.height(22.dp))
                Text(
                    "صوت الأصدقاء",
                    color = Color.White,
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
                Text(
                    "تعرّف على أشخاص جدد وتحدّث معهم",
                    color = Muted,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Right
                )
                Spacer(Modifier.height(18.dp))
                OutlinedTextField(
                    value = search,
                    onValueChange = { search = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("ابحث عن شخص أو دولة", color = Muted) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Green,
                        unfocusedBorderColor = Color(0xFF55566D)
                    )
                )
                Spacer(Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("متصلون الآن", color = Green, fontSize = 13.sp)
                    Text("أشخاص قد ترغب بالتحدث معهم", color = Color.White, fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(10.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    items(friends.filter {
                        it.name.contains(search) || it.country.contains(search) || it.language.contains(search)
                    }) { friend ->
                        FriendCard(friend)
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendCard(friend: Friend) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardNavy)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.End
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(if (friend.online) "● متصل الآن" else "○ غير متصل",
                    color = if (friend.online) Green else Muted, fontSize = 12.sp)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(friend.name, color = Color.White, fontSize = 21.sp, fontWeight = FontWeight.Bold)
                        Text("${friend.country} • يتحدث ${friend.language}", color = Muted, fontSize = 13.sp)
                    }
                    Spacer(Modifier.width(12.dp))
                    Box(
                        modifier = Modifier.size(58.dp).background(Color(0xFF454965), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(friend.avatar, fontSize = 30.sp)
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { /* سنربط زر الاتصال بخدمة المكالمات في المرحلة التالية */ },
                enabled = friend.online,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    disabledContainerColor = Color(0xFF55566D)
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Icon(Icons.Default.Waves, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("دعنا نتحدث", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
