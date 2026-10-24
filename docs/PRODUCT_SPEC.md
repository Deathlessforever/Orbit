# Orbit Product Specification — MVP

## 1. Goal

Build a dependable personal messenger with a premium, minimal interface. Compete by making core communication feel trustworthy and resilient—not by claiming feature parity with mature global platforms before it exists.

## 2. Initial users and use cases

- A person sends a private text message to another person.
- The sender can distinguish queued, sent-to-server, delivered-to-recipient-device, and read states.
- The recipient receives messages after reconnecting, without duplicates.
- The user can retry failed sends and understand why a message is pending.
- Nearby users can later exchange text directly when internet access is unavailable, with explicit proximity and delivery limitations.

## 3. MVP scope

### Required for first real vertical slice
- Android app shell and navigation.
- Local message persistence.
- A defined message envelope with stable message ID, conversation ID, sender ID, timestamp, expiry, and payload version.
- Authenticated online send/receive through a service API.
- Server acknowledgement separate from recipient-device delivery acknowledgement.
- Reconnection, retry with backoff, and deduplication.
- Unit and integration tests for message ordering, retries, duplicate events, and offline/online transitions.
- Clear error and pending states in the UI.

### After the vertical slice
- End-to-end encryption using a reviewed, established protocol/library.
- Identity/key verification and secure key storage.
- Groups and media attachments.
- Direct nearby device transfer.
- Opt-in multi-hop relay with abuse prevention and privacy review.
- Voice/video calling only after core messaging is stable.

### Explicitly out of scope for the first release
- Stories, public feeds, payments, AI chatbots, and feature-count competition.
- Claims of anonymous or untraceable messaging.
- Global offline delivery without a path through devices or infrastructure.
- Home-grown cryptographic algorithms.

## 4. Message lifecycle

1. **Draft:** message exists only in the composer.
2. **Queued:** persisted locally and waiting for a usable route.
3. **Server accepted:** service has acknowledged storage/acceptance; this is not recipient delivery.
4. **Delivered:** recipient device has acknowledged receipt.
5. **Read:** recipient client has emitted a read receipt, if enabled by policy.
6. **Failed/expired:** retry policy ended or message lifetime elapsed; the UI explains the state.

Events must be idempotent. A repeated network event must not create duplicate visible messages. The client must not infer delivery from a successful socket write.

## 5. Quality requirements

- No plaintext message bodies in diagnostic logs.
- Encryption and delivery claims must match the implementation.
- Message IDs must be collision-resistant and generated on the client.
- Retry logic must have bounded backoff and respect expiry.
- Permission denial must not crash the app.
- Network loss must not erase locally queued messages.
- Accessibility labels and scalable text are part of the UI, not a later polish task.
- Every release candidate must have build/test evidence and a known-limitations list.

## 6. Success criteria for MVP

- Two separate test accounts on two physical devices can exchange text over the internet.
- Messages survive app restart and temporary connectivity loss.
- Reconnect/retry does not create duplicate messages.
- Sender sees distinct queued, server-accepted, delivered, and failed states.
- Automated tests cover core message lifecycle and retry cases.
- Security claims are documented and independently reviewable before public launch.
