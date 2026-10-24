package com.deathlessforever.orbit.core.message

/**
 * A locally stored message. This model is not a network protocol and does not imply
 * encryption, delivery, or a remote recipient.
 */
data class ChatMessage(
    val id: String,
    val conversationId: String,
    val senderId: String,
    val body: String,
    val createdAtEpochMillis: Long,
    val status: MessageStatus = MessageStatus.QUEUED
) {
    fun validationError(): String? = when {
        id.isBlank() -> "Message ID must not be blank"
        conversationId.isBlank() -> "Conversation ID must not be blank"
        senderId.isBlank() -> "Sender ID must not be blank"
        body.isBlank() -> "Message body must not be blank"
        body.length > MAX_BODY_LENGTH -> "Message exceeds the $MAX_BODY_LENGTH character limit"
        createdAtEpochMillis < 0L -> "Message timestamp must not be negative"
        else -> null
    }

    companion object {
        const val MAX_BODY_LENGTH = 4_000
    }
}

/** A conversation stored on this device. */
data class LocalConversation(
    val id: String,
    val title: String,
    val createdAtEpochMillis: Long,
    val messages: List<ChatMessage> = emptyList()
) {
    fun validationError(): String? = when {
        id.isBlank() -> "Conversation ID must not be blank"
        title.isBlank() -> "Conversation title must not be blank"
        title.length > MAX_TITLE_LENGTH -> "Conversation title is too long"
        createdAtEpochMillis < 0L -> "Conversation timestamp must not be negative"
        messages.any { it.conversationId != id } -> "Every message must belong to this conversation"
        messages.any { it.validationError() != null } -> "Conversation contains an invalid message"
        else -> null
    }

    companion object {
        const val MAX_TITLE_LENGTH = 80
    }
}
