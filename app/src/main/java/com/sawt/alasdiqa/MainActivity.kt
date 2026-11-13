package com.sawt.alasdiqa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

data class Friend(val id: Int, val name: String, val country: String, val avatar: String, val online: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) { SawtApp() }
        }
    }
}
@Composable
fun SawtApp() {
    val bg = Color(0xFF191D2B); val card = Color(0xFF252A3D); val green = Color(0xFF1DB954)
    var tab by remember { mutableStateOf("اكتشف") }
    var query by remember { mutableStateOf("") }
    var myName by remember { mutableStateOf("مستخدم صوت الأصدقاء") }
    var myCountry by remember { mutableStateOf("العراق") }
    val friends = remember { mutableStateListOf(Friend(1,"طارق","السعودية","🌙",true), Friend(2,"سارة","العراق","🌸",true), Friend(3,"محمد","الأردن","🎧",false)) }
    val favs = remember { mutableStateListOf(2) }
    val history = remember { mutableStateListOf<Pair<Friend, String>>() }
    var calling by remember { mutableStateOf<Friend?>(null) }

    if (calling != null) {
        CallScreen(calling!!, bg, card, green) { dur -> history.add(0, calling!! to dur); calling = null }
        return
    }
    Scaffold(containerColor = bg, bottomBar = {
        NavigationBar(containerColor = card) {
            listOf("اكتشف" to Icons.Default.Search, "المحادثات" to Icons.Default.ChatBubble, "المفضلة" to Icons.Default.Favorite, "حسابي" to Icons.Default.Settings).forEach { (t, ic) ->
                NavigationBarItem(selected = tab == t, onClick = { tab = t }, icon = { Icon(ic, null) }, label = { Text(t) })
            }
        }
    }) { p ->
        Column(Modifier.fillMaxSize().padding(p).padding(18.dp)) {
            Text("صوت الأصدقاء", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            when (tab) {
                "اكتشف" -> {
                    OutlinedTextField(value = query, onValueChange = { query = it }, placeholder = { Text("ابحث بالاسم أو الدولة", color = Color.Gray) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    Spacer(Modifier.height(12.dp))
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(friends.filter { it.name.contains(query, true) || it.country.contains(query, true) }) { f ->
                            FriendItem(f, favs.contains(f.id), card, green, { if (favs.contains(f.id)) favs.remove(f.id) else favs.add(f.id) }, { calling = f })
                        }
                    }
                }
                "المحادثات" -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(history) { (f, d) -> Text("${f.avatar} ${f.name} - مدة المكالمة: $d", color = Color.White, modifier = Modifier.fillMaxWidth().background(card, RoundedCornerShape(12.dp)).padding(16.dp)) }
                }
                "المفضلة" -> LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(friends.filter { favs.contains(it.id) }) { f -> FriendItem(f, true, card, green, { favs.remove(f.id) }, { calling = f }) }
                }
                "حسابي" -> Column(Modifier.background(card, RoundedCornerShape(16.dp)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(value = myName, onValueChange = { myName = it }, label = { Text("الاسم") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                    OutlinedTextField(value = myCountry, onValueChange = { myCountry = it }, label = { Text("الدولة") }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White, unfocusedTextColor = Color.White))
                }
            }
        }
    }
}
@Composable
fun FriendItem(f: Friend, fav: Boolean, card: Color, green: Color, onFav: () -> Unit, onCall: () -> Unit) {
    Column(Modifier.fillMaxWidth().background(card, RoundedCornerShape(18.dp)).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(f.avatar, fontSize = 30.sp); Spacer(Modifier.width(12.dp))
                Column { Text(f.name, color = Color.White, fontSize = 19.sp, fontWeight = FontWeight.Bold); Text(f.country, color = Color.Gray) }
            }
            IconButton(onClick = onFav) { Icon(if (fav) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = if (fav) Color.Red else Color.Gray) }
        }
        Spacer(Modifier.height(10.dp))
        Button(onClick = onCall, enabled = f.online, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = green)) { Text("دعنا نتحدث") }
    }
}

@Composable
fun CallScreen(f: Friend, bg: Color, card: Color, green: Color, onEnd: (String) -> Unit) {
    var sec by remember { mutableIntStateOf(0) }
    var mute by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { while (true) { delay(1000); sec++ } }
    val t = String.format("%02d:%02d", sec / 60, sec % 60)
    Column(Modifier.fillMaxSize().background(bg).padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.SpaceAround) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("مكالمة صوتية نشطة", color = green, fontSize = 18.sp); Spacer(Modifier.height(20.dp))
            Text(f.avatar, fontSize = 60.sp); Text(f.name, color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold); Text(t, color = Color.White, fontSize = 22.sp)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            IconButton(onClick = { mute = !mute }, modifier = Modifier.size(60.dp).background(card, CircleShape)) { Icon(if (mute) Icons.Default.MicOff else Icons.Default.Mic, null, tint = Color.White) }
            IconButton(onClick = { onEnd(t) }, modifier = Modifier.size(64.dp).background(Color.Red, CircleShape)) { Icon(Icons.Default.CallEnd, null, tint = Color.White) }
        }
    }
}
