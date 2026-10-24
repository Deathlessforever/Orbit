package com.deathlessforever.orbit.core.message

import android.content.Context
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
class LocalChatStoreTest {
    private lateinit var context: Context
    private lateinit var store: LocalChatStore

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        context.getSharedPreferences("orbit_local_chat_v1", Context.MODE_PRIVATE)
            .edit().clear().commit()
        store = LocalChatStore(context)
    }

    @Test
    fun createAndSendPersistConversationAndMessage() {
        assertTrue(store.create("c1", "Test chat", 10L).isSuccess)
        val message = ChatMessage("m1", "c1", "local-user", "Hello Orbit", 20L)

        assertTrue(store.send(message).isSuccess)

        val reloaded = LocalChatStore(context).all()
        assertEquals(1, reloaded.size)
        assertEquals("Test chat", reloaded.single().title)
        assertEquals(listOf(message), reloaded.single().messages)
    }

    @Test
    fun rejectsMessageForUnknownConversation() {
        val result = store.send(ChatMessage("m1", "missing", "local-user", "Hello", 20L))

        assertTrue(result.isFailure)
        assertTrue(store.all().isEmpty())
    }

    @Test
    fun rejectsDuplicateMessageIdWithinConversation() {
        store.create("c1", "Test chat", 10L)
        val message = ChatMessage("m1", "c1", "local-user", "Hello", 20L)
        assertTrue(store.send(message).isSuccess)

        val duplicate = store.send(message)

        assertTrue(duplicate.isFailure)
        assertEquals(1, store.all().single().messages.size)
    }

    @Test
    fun deleteRemovesOnlySelectedConversation() {
        store.create("c1", "First", 10L)
        store.create("c2", "Second", 20L)

        assertTrue(store.delete("c1").isSuccess)

        assertEquals(listOf("c2"), store.all().map { it.id })
    }

    @Test
    fun clearAllRemovesEverySavedConversation() {
        store.create("c1", "First", 10L)
        store.create("c2", "Second", 20L)

        assertTrue(store.clearAll().isSuccess)

        assertTrue(store.all().isEmpty())
        assertFalse(context.getSharedPreferences("orbit_local_chat_v1", Context.MODE_PRIVATE)
            .contains("conversations"))
    }
}
