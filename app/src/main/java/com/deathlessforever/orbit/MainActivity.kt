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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deathlessforever.orbit.core.message.ChatMessage
import com.deathlessforever.orbit.core.message.LocalChatStore
import java.util.UUID

private val Ink = Color(0xFF101828)
private val Muted = Color(0xFF667085)
private val Accent = Color(0xFF5B5FEF)
private val Canvas = Color(0xFFF7F8FC)

class MainActivity : ComponentActivity() {
    private lateinit var chatStore: LocalChatStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chatStore = LocalChatStore(applicationContext)
        setContent { MaterialTheme { OrbitHome(chatStore) } }
    }
}

@Composable
private fun OrbitHome(store: LocalChatStore) {
    var query by remember { mutableStateOf("") }
    var newChat by remember { mutableStateOf(false) }
    var contactName by remember { mutableStateOf("") }
    var settings by remember { mutableStateOf(false) }
    var selectedId by remember { mutableStateOf<String?>(null) }
    var conversations by remember { mutableStateOf(store.all()) }
    var refreshToken by remember { mutableIntStateOf(0) }
    LaunchedEffect(refreshToken) { conversations = store.all() }
    val selected = conversations.firstOrNull { it.id == selectedId }

    Surface(Modifier.fillMaxSize(), color = Canvas) {
        Column(Modifier.fillMaxSize()) {
            Row(
                Modifier.fillMaxWidth().padding(start = 20.dp, end = 12.dp, top = 18.dp, bottom = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selected != null) {
                    IconButton(onClick = { selectedId = null }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back to conversations", tint = Ink)
                    }
                } else {
                    Box(Modifier.size(42.dp).clip(RoundedCornerShape(15.dp)).background(Accent), contentAlignment = Alignment.Center) {
                        Text("O", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(selected?.title ?: "Orbit", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Ink, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text(if (selected != null) "Local prototype · this device only" else "Your world, in sync.", fontSize = 12.sp, color = Muted)
                }
                if (selected == null) {
                    IconButton(onClick = { settings = !settings }) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Ink)
                    }
                }
            }

            when {
                selected != null -> {
                    Text(
                        "Messages are saved on this phone only. They are not sent to another person.",
                        Modifier.fillMaxWidth().padding(horizontal = 18.dp, vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp)).background(Color(0xFFE9EAFE)).padding(12.dp),
                        color = Color(0xFF393C91), fontSize = 12.sp
                    )
                    LazyColumn(
                        Modifier.weight(1f).fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(selected.messages, key = { it.id }) { message ->
                            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                                Column(
                                    Modifier.widthIn(max = 300.dp).clip(RoundedCornerShape(18.dp))
                                        .background(Color(0xFFE9EAFE)).padding(horizontal = 14.dp, vertical = 10.dp)
                                ) {
                                    Text(message.body, color = Ink, fontSize = 15.sp)
                                    Spacer(Modifier.height(4.dp))
                                    Text("Saved locally", color = Muted, fontSize = 10.sp)
                                }
                            }
                        }
                        if (selected.messages.isEmpty()) {
                            item {
                                Column(Modifier.fillMaxWidth().padding(top = 70.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Your conversation starts here", color = Ink, fontWeight = FontWeight.SemiBold)
                                    Text("Write a message below to test local storage.", color = Muted, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                    var draft by remember(selected.id) { mutableStateOf("") }
                    Row(
                        Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = draft, onValueChange = { if (it.length <= ChatMessage.MAX_BODY_LENGTH) draft = it },
                            modifier = Modifier.weight(1f), placeholder = { Text("Write a message") },
                            shape = RoundedCornerShape(18.dp), maxLines = 4,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                            keyboardActions = KeyboardActions(onSend = {
                                if (draft.isNotBlank()) {
                                    store.send(ChatMessage(UUID.randomUUID().toString(), selected.id, "local-user", draft.trim(), System.currentTimeMillis()))
                                    draft = ""; refreshToken++
                                }
                            })
                        )
                        Spacer(Modifier.width(8.dp))
                        IconButton(
                            onClick = {
                                if (draft.isNotBlank()) {
                                    store.send(ChatMessage(UUID.randomUUID().toString(), selected.id, "local-user", draft.trim(), System.currentTimeMillis()))
                                    draft = ""; refreshToken++
                                }
                            },
                            enabled = draft.isNotBlank(),
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(if (draft.isNotBlank()) Accent else Color(0xFFE4E7EC))
                        ) {
                            Icon(Icons.Default.Send, contentDescription = "Save message locally", tint = if (draft.isNotBlank()) Color.White else Muted)
                        }
                    }
                }
                settings -> {
                    Column(Modifier.weight(1f).padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Prototype status", color = Ink, fontWeight = FontWeight.SemiBold)
                        Text("Local conversations and message history are saved on this device.", color = Muted)
                        Text("Not implemented: accounts, network delivery, end-to-end encryption, media, group sync, calls, and phone-to-phone offline transport.", color = Muted)
                        Text("Storage note: local prototype data is not encrypted. Do not use it for sensitive messages.", color = Color(0xFFB42318))
                        Button(onClick = { settings = false }) { Text("Back to chats") }
                    }
                }
                else -> {
                    Column(Modifier.padding(horizontal = 20.dp)) {
                        Text("Messages", fontSize = 29.sp, fontWeight = FontWeight.Bold, color = Ink)
                        Text("Your conversations, stored locally.", fontSize = 14.sp, color = Muted)
                        Spacer(Modifier.height(14.dp))
                        OutlinedTextField(
                            value = query, onValueChange = { query = it },
                            modifier = Modifier.fillMaxWidth(), singleLine = true,
                            placeholder = { Text("Search conversations") },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                            shape = RoundedCornerShape(18.dp)
                        )
                    }
                    val visible = conversations.filter { it.title.contains(query, true) || it.messages.lastOrNull()?.body?.contains(query, true) == true }
                    LazyColumn(
                        Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(visible, key = { it.id }) { conversation ->
                            val last = conversation.messages.lastOrNull()
                            Row(
                                Modifier.fillMaxWidth().clip(RoundedCornerShape(18.dp)).background(Color.White)
                                    .clickable { selectedId = conversation.id }.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(Modifier.size(50.dp).clip(CircleShape).background(Color(0xFFE9EAFE)), contentAlignment = Alignment.Center) {
                                    Text(conversation.title.take(2).uppercase(), color = Accent, fontWeight = FontWeight.Bold)
                                }
                                Spacer(Modifier.width(12.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(conversation.title, color = Ink, fontWeight = FontWeight.SemiBold)
                                    Text(last?.body ?: "No messages yet", color = Muted, fontSize = 13.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Text(if (last == null) "New" else "Local", color = Muted, fontSize = 10.sp)
                            }
                        }
                        if (visible.isEmpty()) {
                            item {
                                Column(Modifier.fillMaxWidth().padding(top = 70.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Muted, modifier = Modifier.size(36.dp))
                                    Spacer(Modifier.height(12.dp))
                                    Text(if (query.isBlank()) "No conversations yet" else "No matching conversations", color = Ink, fontWeight = FontWeight.SemiBold)
                                    Text("Create a local conversation to try Orbit.", color = Muted, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                    Row(Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 10.dp), horizontalArrangement = Arrangement.End) {
                        Button(onClick = { newChat = true }, shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(containerColor = Accent)) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("New conversation")
                        }
                    }
                }
            }
        }
    }

    if (newChat) {
        AlertDialog(
            onDismissRequest = { newChat = false; contactName = "" },
            title = { Text("New local conversation") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("This creates a conversation on this phone. It does not contact or notify anyone.", color = Muted, fontSize = 13.sp)
                    OutlinedTextField(
                        value = contactName, onValueChange = { if (it.length <= 80) contactName = it },
                        singleLine = true, label = { Text("Conversation name") }
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (contactName.isNotBlank()) {
                        val id = UUID.randomUUID().toString()
                        val result = store.create(id, contactName.trim(), System.currentTimeMillis())
                        if (result.isSuccess) {
                            refreshToken++
                            selectedId = id
                            settings = false
                            newChat = false
                            contactName = ""
                        }
                    }
                }, enabled = contactName.isNotBlank()) { Text("Create") }
            },
            dismissButton = { TextButton(onClick = { newChat = false; contactName = "" }) { Text("Cancel") } }
        )
    }
}
