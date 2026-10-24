package com.deathlessforever.orbit.core.message

/**
 * UI-safe lifecycle states for a message. These states describe evidence observed by
 * the client; they do not themselves send, encrypt, or deliver a message.
 */
enum class MessageStatus {
    QUEUED,
    SERVER_ACCEPTED,
    DELIVERED,
    READ,
    FAILED,
    EXPIRED
}

/**
 * Returns whether an observed event may advance a message from [from] to [to].
 *
 * Server acceptance is not recipient delivery. Delivery requires a recipient-device
 * acknowledgement; read requires an explicit read receipt. A failed message may be
 * queued again only when the user or retry policy starts a new attempt.
 */
fun isAllowedTransition(from: MessageStatus, to: MessageStatus): Boolean {
    if (from == to) return true // duplicate events are idempotent
    return when (from) {
        MessageStatus.QUEUED ->
            to == MessageStatus.SERVER_ACCEPTED ||
                to == MessageStatus.FAILED ||
                to == MessageStatus.EXPIRED

        MessageStatus.SERVER_ACCEPTED ->
            to == MessageStatus.DELIVERED ||
                to == MessageStatus.FAILED ||
                to == MessageStatus.EXPIRED

        MessageStatus.DELIVERED ->
            to == MessageStatus.READ

        MessageStatus.READ,
        MessageStatus.EXPIRED -> false

        MessageStatus.FAILED ->
            to == MessageStatus.QUEUED ||
                to == MessageStatus.EXPIRED
    }
}
