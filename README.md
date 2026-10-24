# Orbit

**Private conversations. Reliable delivery. Fewer boundaries.**

Orbit is an ambitious, mobile-first personal messaging project built to compete on the qualities that make people trust a messenger: dependable delivery, privacy, a calm premium experience, and useful communication across weak or missing connectivity.

> **Current stage:** local Android prototype. You can create conversations and save messages on one device. This is not yet an online or phone-to-phone messenger. Features are only considered complete when implemented, tested, and documented.

## Product direction

Orbit aims to bring together:

- **Everyday messaging:** one-to-one chats, groups, replies, reactions, and media.
- **Privacy by design:** end-to-end encryption for supported conversation types, careful key handling, and minimal metadata.
- **Resilient delivery:** clear states for sending, sent, delivered, and read; safe retry and deduplication.
- **Connectivity-aware messaging:** online delivery first, with direct nearby transfer when devices and permissions support it. Offline messages cannot travel globally without a connected path between devices or infrastructure.
- **Accessible, premium UX:** fast, readable, low-friction, multilingual-ready, and respectful of attention.
- **Honest status:** never display “delivered” unless the system has evidence that the recipient device received the message.

## Principles

1. **Trust before growth.** No fake encryption claims, fake delivery receipts, or hidden background relay.
2. **Ship one real path first.** Make a small, testable vertical slice work before adding calls, stories, bots, or many other features.
3. **Security is architecture.** Use established cryptographic protocols and libraries; do not invent cryptography.
4. **Resilient does not mean magical.** Bluetooth/offline relays require proximity, compatible devices, battery, permissions, and a route through participating devices.
5. **Measure real behavior.** Test on physical devices and poor networks; publish limitations and known issues.
6. **Build for maintainability.** Keep transport, storage, cryptography, and UI concerns separated.

## Proposed milestones

- [ ] **M0 — Product and threat model:** define MVP, trust boundaries, message states, and abuse protections.
- [ ] **M1 — App foundation:** Android project, design system, local database, navigation, and automated build checks.
- [ ] **M2 — Real account and online text chat:** authenticated sessions, server-backed message sync, delivery acknowledgements, reconnect/retry, and multi-device-safe IDs.
- [ ] **M3 — Private chats:** integrate a reviewed, established end-to-end encryption protocol and key verification.
- [ ] **M4 — Groups and media:** group membership, upload/download, limits, and secure media handling.
- [ ] **M5 — Nearby offline text:** direct device-to-device exchange with explicit delivery states and user controls.
- [ ] **M6 — Relay and hardening:** opt-in multi-hop forwarding, replay/deduplication controls, rate limits, battery testing, abuse reporting, and independent security review.
- [ ] **M7 — Broader release:** accessibility, localization, iOS strategy, monitoring, recovery, backups, and release process.

## Architecture direction

The first release should prioritize a native Android client and a small, documented service API. Keep the transport interface modular so online delivery and nearby delivery can share message IDs, acknowledgements, expiry, and deduplication rules. Choose backend and cryptographic dependencies only after recording the trade-offs in the architecture decision records.

See [Product specification](docs/PRODUCT_SPEC.md) and [Security model](docs/SECURITY_MODEL.md).

## Development status

### Implemented in the current development branch
- Android Jetpack Compose application shell and conversation list.
- Local conversation creation, local message entry, and history persistence using Android SharedPreferences.
- Input validation for message IDs, conversation IDs, sender IDs, body length, and timestamps.
- Message lifecycle transition rules and unit tests.
- GitHub Actions workflow for Android build and unit-test tasks.

### Not implemented yet
- Remote accounts, identity verification, server/API, or messages delivered to another phone.
- End-to-end encryption or encrypted local storage. **Do not use this prototype for sensitive messages.**
- Group synchronization, attachments, voice/video calls, push notifications, and offline phone-to-phone transport.
- Physical-device verification and production security review.

Local storage is an engineering slice for testing the app flow, not a secure messenger. A green CI run verifies only the tasks it actually executes; it does not establish production readiness.

## Contributing

Work in focused branches, keep changes reviewable, add tests for behavior, and update documentation whenever a protocol or user-visible status changes.
