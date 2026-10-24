package com.deathlessforever.orbit.core.message

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ChatMessageTest {
    @Test
    fun validMessagePassesValidation() {
        assertNull(ChatMessage("m1", "c1", "local-user", "Hello Orbit", 1L).validationError())
    }

    @Test
    fun blankBodyIsRejected() {
        assertEquals("Message body must not be blank", ChatMessage("m1", "c1", "local-user", "  ", 1L).validationError())
    }

    @Test
    fun oversizedBodyIsRejected() {
        val message = ChatMessage("m1", "c1", "local-user", "x".repeat(ChatMessage.MAX_BODY_LENGTH + 1), 1L)
        assertEquals("Message exceeds the ${ChatMessage.MAX_BODY_LENGTH} character limit", message.validationError())
    }

    @Test
    fun negativeTimestampIsRejected() {
        assertEquals("Message timestamp must not be negative", ChatMessage("m1", "c1", "local-user", "Hello", -1L).validationError())
    }

    @Test
    fun conversationRejectsMessageFromAnotherConversation() {
        val item = LocalConversation("c1", "A conversation", 1L, listOf(ChatMessage("m1", "c2", "local-user", "Hello", 1L)))
        assertEquals("Every message must belong to this conversation", item.validationError())
    }
}
