package com.deathlessforever.orbit.core.message

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MessageStatusTest {
    @Test
    fun duplicateEventsAreIdempotent() {
        MessageStatus.entries.forEach { status ->
            assertTrue(isAllowedTransition(status, status))
        }
    }

    @Test
    fun serverAcceptanceIsNotDelivery() {
        assertTrue(isAllowedTransition(MessageStatus.QUEUED, MessageStatus.SERVER_ACCEPTED))
        assertFalse(isAllowedTransition(MessageStatus.QUEUED, MessageStatus.DELIVERED))
        assertFalse(isAllowedTransition(MessageStatus.SERVER_ACCEPTED, MessageStatus.READ))
    }

    @Test
    fun deliveryRequiresServerAcceptedState() {
        assertTrue(isAllowedTransition(MessageStatus.SERVER_ACCEPTED, MessageStatus.DELIVERED))
        assertFalse(isAllowedTransition(MessageStatus.QUEUED, MessageStatus.DELIVERED))
    }

    @Test
    fun readReceiptFollowsDelivery() {
        assertTrue(isAllowedTransition(MessageStatus.DELIVERED, MessageStatus.READ))
        assertFalse(isAllowedTransition(MessageStatus.SERVER_ACCEPTED, MessageStatus.READ))
    }

    @Test
    fun terminalStatesCannotBeReopened() {
        assertFalse(isAllowedTransition(MessageStatus.READ, MessageStatus.QUEUED))
        assertFalse(isAllowedTransition(MessageStatus.EXPIRED, MessageStatus.QUEUED))
        assertFalse(isAllowedTransition(MessageStatus.READ, MessageStatus.FAILED))
    }

    @Test
    fun failedMessageCanBeRetriedButNotReportedAsDelivered() {
        assertTrue(isAllowedTransition(MessageStatus.FAILED, MessageStatus.QUEUED))
        assertTrue(isAllowedTransition(MessageStatus.FAILED, MessageStatus.EXPIRED))
        assertFalse(isAllowedTransition(MessageStatus.FAILED, MessageStatus.DELIVERED))
    }
}
