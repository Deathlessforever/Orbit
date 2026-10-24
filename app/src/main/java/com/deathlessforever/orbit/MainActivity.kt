package com.deathlessforever.orbit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF101828)
private val Muted = Color(0xFF667085)
private val Accent = Color(0xFF5B5FEF)
private val Canvas = Color(0xFFF7F8FC)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { OrbitHome() } }
    }
}

private data class ChatPreview(
    val initials: String, val name: String, val message: String,
    val time: String, val unread: Int = 0, val online: Boolean = false
)

private val sampleChats = listOf(
    ChatPreview("AM", "Aarav Mehta", "Sounds good — see you there.", "2m", 2, true),
    ChatPreview("SP", "Sneha Patil", "Sent you the photos ✨", "18m", online = true),
    ChatPreview("TB", "Team Blue", "Kiran: I’ll share the notes tonight", "1h", 4),
    ChatPreview("RK", "Rohan Kumar", "Voice message · 0:14", "3h"),
    ChatPreview("FM", "Family", "Mom: Call when you’re free", "Yesterday")
)

@Composable
private fun OrbitHome() {
    var query by remember { mutableStateOf("") }
    var showNewMessage by remember { mutableStateOf(false) }
    var settings by remember { mutableStateOf(false) }
    val chats = sampleChats.filter {
        it.name.contains(query, true) || it.message.contains(query, true)
    }

    Surface(Modifier.fillMaxSize(), color = Canvas) {
        Column(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxWidth().padding(start = 22.dp, end = 22.dp, top = 24.dp, bottom = 12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(42.dp).clip(RoundedCornerShape(15.dp)).background(Accent), contentAlignment = Alignment.Center) {
                        Text("O", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Orbit", fontSize = 27.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text("Your world, in sync.", fontSize = 13.sp, color = Muted)
                    }
                    IconButton(onClick = { settings = !settings }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Ink)
                    }
                }
                Spacer(Modifier.height(22.dp))
                Text(if (settings) "Settings" else "Messages", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Ink)
                Text(if (settings) "Your preferences, in one place." else "Stay close to the people who matter.", fontSize = 14.sp, color = Muted)
                Spacer(Modifier.height(18.dp))
                OutlinedTextField(
                    value = query, onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(), singleLine = true,
                    placeholder = { Text("Search conversations") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    shape = RoundedCornerShape(18.dp)
                )
            }
            if (settings) {
                Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Orbit is an early prototype.", color = Ink, fontWeight = FontWeight.SemiBold)
                    Text("Accounts, encryption, live message sync, and offline transfer are not connected yet.", color = Muted)
                    Button(onClick = { settings = false }) { Text("Back to chats") }
                }
            } else {
                Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("RECENT", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Muted, letterSpacing = 1.4.sp)
                    Text(chats.size.toString() + " conversations", fontSize = 12.sp, color = Muted)
                }
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 20.dp)
                ) {
                    items(chats, key = { it.name }) { chat -> ChatRow(chat) }
                }
                Row(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 12.dp), horizontalArrangement = Arrangement.End) {
                    Button(onClick = { showNewMessage = !showNewMessage }, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(if (showNewMessage) "Close" else "New message")
                    }
                }
                if (showNewMessage) {
                    Surface(Modifier.fillMaxWidth().padding(horizontal = 22.dp, vertical = 8.dp), shape = RoundedCornerShape(16.dp), color = Color.White, shadowElevation = 2.dp) {
                        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Start a conversation", fontWeight = FontWeight.SemiBold, color = Ink)
                            Text("Contact discovery and sending are not wired up yet. This is UI scaffolding, not a working chat.", color = Muted, fontSize = 13.sp)
                        }
                    }
                }
            }
            Row(Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 34.dp, vertical = 13.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { settings = false }) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = "Chats", tint = if (!settings) Accent else Muted)
                    Text("Chats", color = if (!settings) Accent else Muted, fontSize = 11.sp)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { settings = true }) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings", tint = if (settings) Accent else Muted)
                    Text("Settings", color = if (settings) Accent else Muted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun ChatRow(chat: ChatPreview) {
    Row(
        Modifier.fillMaxWidth().padding(vertical = 4.dp).clip(RoundedCornerShape(18.dp))
            .background(Color.White).clickable { }.padding(horizontal = 14.dp, vertical = 15.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            Box(Modifier.size(52.dp).clip(CircleShape).background(Color(0xFFE9EAFE)), contentAlignment = Alignment.Center) {
                Text(chat.initials, color = Accent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            if (chat.online) Box(Modifier.size(13.dp).clip(CircleShape).background(Color(0xFF22C55E)))
        }
        Spacer(Modifier.width(13.dp))
        Column(Modifier.weight(1f)) {
            Text(chat.name, fontWeight = FontWeight.SemiBold, color = Ink, fontSize = 15.sp)
            Spacer(Modifier.height(4.dp))
            Text(chat.message, color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        Spacer(Modifier.width(8.dp))
        Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(7.dp)) {
            Text(chat.time, color = Muted, fontSize = 11.sp)
            if (chat.unread > 0) {
                Box(Modifier.clip(CircleShape).background(Accent).padding(horizontal = 7.dp, vertical = 3.dp), contentAlignment = Alignment.Center) {
                    Text(chat.unread.toString(), color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
