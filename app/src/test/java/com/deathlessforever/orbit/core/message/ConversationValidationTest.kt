package com.deathlessforever.orbit.core.message

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ConversationValidationTest {
    @Test
    fun emptyConversationIsValid() {
        assertNull(LocalConversation("c1", "Personal notes", 0L).validationError())
    }

    @Test
    fun blankConversationTitleIsRejected() {
        assertEquals(
            "Conversation title must not be blank",
            LocalConversation("c1", "   ", 1L).validationError()
        )
    }

    @Test
    fun oversizedConversationTitleIsRejected() {
        val conversation = LocalConversation(
            id = "c1",
            title = "x".repeat(LocalConversation.MAX_TITLE_LENGTH + 1),
            createdAtEpochMillis = 1L
        )
        assertEquals("Conversation title is too long", conversation.validationError())
    }

    @Test
    fun negativeConversationTimestampIsRejected() {
        assertEquals(
            "Conversation timestamp must not be negative",
            LocalConversation("c1", "Notes", -1L).validationError()
        )
    }

    @Test
    fun invalidNestedMessageMakesConversationInvalid() {
        val conversation = LocalConversation(
            id = "c1",
            title = "Notes",
            createdAtEpochMillis = 1L,
            messages = listOf(
                ChatMessage("m1", "c1", "local-user", "   ", 1L)
            )
        )
        assertEquals("Conversation contains an invalid message", conversation.validationError())
    }
}
