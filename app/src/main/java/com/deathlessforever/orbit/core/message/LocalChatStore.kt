package com.deathlessforever.orbit.core.message

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

/**
 * Prototype-only local persistence. SharedPreferences is not encrypted and must not
 * hold sensitive production messages.
 */
class LocalChatStore(context: Context) {
    private val prefs = context.applicationContext.getSharedPreferences("orbit_local_chat_v1", Context.MODE_PRIVATE)

    @Synchronized
    fun all(): List<LocalConversation> = runCatching {
        val array = JSONArray(prefs.getString("conversations", "[]"))
        buildList {
            for (i in 0 until array.length()) {
                decode(array.getJSONObject(i))?.let(::add)
            }
        }.sortedByDescending { it.messages.lastOrNull()?.createdAtEpochMillis ?: it.createdAtEpochMillis }
    }.getOrDefault(emptyList())

    @Synchronized
    fun create(id: String, title: String, now: Long): Result<LocalConversation> {
        val item = LocalConversation(id, title.trim(), now)
        item.validationError()?.let { return Result.failure(IllegalArgumentException(it)) }
        if (all().any { it.id == id }) return Result.failure(IllegalArgumentException("Duplicate conversation ID"))
        return save(all() + item).map { item }
    }

    @Synchronized
    fun send(message: ChatMessage): Result<LocalConversation> {
        message.validationError()?.let { return Result.failure(IllegalArgumentException(it)) }
        val items = all()
        val index = items.indexOfFirst { it.id == message.conversationId }
        if (index < 0) return Result.failure(IllegalArgumentException("Conversation not found"))
        if (items[index].messages.any { it.id == message.id }) return Result.failure(IllegalArgumentException("Duplicate message ID"))
        val updated = items[index].copy(messages = items[index].messages + message)
        val next = items.toMutableList().apply { set(index, updated) }
        return save(next).map { updated }
    }

    @Synchronized
    fun delete(id: String): Result<Unit> = save(all().filterNot { it.id == id })

    /** Permanently removes all local conversations from this app installation. */
    @Synchronized
    fun clearAll(): Result<Unit> = runCatching {
        check(prefs.edit().remove("conversations").commit()) { "Local data deletion failed" }
    }

    private fun save(items: List<LocalConversation>): Result<Unit> = runCatching {
        val array = JSONArray()
        items.forEach { item ->
            item.validationError()?.let { throw IllegalArgumentException(it) }
            array.put(encode(item))
        }
        check(prefs.edit().putString("conversations", array.toString()).commit()) { "Local save failed" }
    }

    private fun encode(item: LocalConversation) = JSONObject().apply {
        put("id", item.id)
        put("title", item.title)
        put("createdAt", item.createdAtEpochMillis)
        put("messages", JSONArray().also { arr ->
            item.messages.forEach { m ->
                arr.put(JSONObject().apply {
                    put("id", m.id); put("conversationId", m.conversationId)
                    put("senderId", m.senderId); put("body", m.body)
                    put("createdAt", m.createdAtEpochMillis); put("status", m.status.name)
                })
            }
        })
    }

    private fun decode(obj: JSONObject): LocalConversation? = runCatching {
        val id = obj.getString("id")
        val arr = obj.optJSONArray("messages") ?: JSONArray()
        val messages = buildList {
            for (i in 0 until arr.length()) {
                val m = arr.getJSONObject(i)
                add(ChatMessage(m.getString("id"), m.getString("conversationId"), m.getString("senderId"), m.getString("body"), m.getLong("createdAt"), MessageStatus.valueOf(m.getString("status"))))
            }
        }
        LocalConversation(id, obj.getString("title"), obj.getLong("createdAt"), messages).takeIf { it.validationError() == null }
    }.getOrNull()
}
