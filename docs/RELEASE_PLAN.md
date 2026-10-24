# Orbit Public Release Plan

**Status as of 2026-10-24:** early Android local prototype. This plan is a release gate, not a claim that Orbit is ready for public launch.

## Product scope decision

The first public product must be a small, honest, online one-to-one text messenger. Do not market the current local-only prototype as a messaging service. The current Android app saves conversation names and messages on one device; it does not send messages to another person, authenticate accounts, or provide end-to-end encryption.

### Release 0 — engineering preview (current)
- [x] Android Compose shell and local conversation creation.
- [x] Local message persistence and model validation.
- [x] Message lifecycle model and unit tests.
- [x] CI workflow for debug build and JVM unit tests.
- [x] Visible disclosure that messages remain on this device.
- [x] User-initiated deletion of all local conversation data.
- [ ] Verify Android API 36 build and unit tests in CI.
- [ ] Install and exercise the app on physical Android devices.
- [ ] Replace SharedPreferences JSON with a robust local database before storing real users' conversations.

## Release 1 — Android closed beta (not started)

### Product and backend
- [ ] Choose a backend architecture and document the decision. A managed backend can reduce operational burden, but must be evaluated for authentication, data location, pricing, export, and vendor lock-in.
- [ ] Implement account creation, sign-in, sign-out, session renewal, password/account recovery, and abuse-resistant rate limits.
- [ ] Implement authenticated message send/receive, server acknowledgements, recipient-device delivery acknowledgements, reconnect/retry with bounded backoff, expiry, and deduplication.
- [ ] Store messages durably and enforce conversation membership on the server.
- [ ] Add user blocking and reporting, account deletion, and support contact before opening beta to strangers.
- [ ] Keep delivery states truthful: server acceptance is not recipient delivery.
- [ ] Do not claim end-to-end encryption until a reviewed protocol/library, key verification, secure key handling, test coverage, and independent review are in place.

### Client quality
- [ ] Replace prototype SharedPreferences message storage with Room/SQLite and define migration behavior.
- [ ] Add integration tests for network failures, retries, duplicates, ordering, expired messages, unauthorized access, and multi-account isolation.
- [ ] Add UI tests for the primary flows and errors.
- [ ] Add privacy-conscious crash diagnostics and operational monitoring; never log message bodies, credentials, or encryption keys.
- [ ] Validate dynamic text, TalkBack/accessibility, contrast, keyboard navigation, small screens, and localization.
- [ ] Test clean install, update, force-stop/relaunch, low storage, offline/online transitions, and app data deletion on multiple physical devices.

### Security and privacy gates
- [ ] Write and publish a complete privacy policy describing actual collection, processing, sharing, retention, deletion, and support.
- [ ] Complete Google Play Data safety and Apple App Privacy disclosures based on the production SDKs and backend.
- [ ] Publish terms of service and community/abuse rules appropriate to the service.
- [ ] Review authentication, authorization, secrets management, TLS configuration, backups, logs, and dependency vulnerabilities.
- [ ] Commission an independent security review before making strong privacy/security claims.
- [ ] Define retention, account deletion, user support, incident response, and breach handling.

### Android distribution
- [ ] Use a supported Android Gradle Plugin/Gradle/JDK toolchain and target API 36 or the higher level required by Play at submission time.
- [ ] Build a signed release Android App Bundle (AAB); keep signing credentials outside GitHub.
- [ ] Verify versioning, release configuration, dependency/license notices, and no debug-only behavior.
- [ ] Prepare app icon, screenshots, store listing, support email/site, privacy-policy URL, age rating, Data safety form, and reviewer instructions.
- [ ] Enroll testers. For eligible new personal Play Console accounts, satisfy the currently applicable closed-testing requirement before requesting production access.
- [ ] Run a closed beta, triage feedback, and block release on crashes, data-loss bugs, authorization flaws, or misleading delivery/security states.

## Release 2 — iOS beta (not started)

The current Kotlin/Jetpack Compose Android app is not an iOS app. Create a separate iOS client, preferably with Swift/SwiftUI unless a deliberate cross-platform migration is approved.

- [ ] Decide and document shared protocol/API contracts before building the iOS client.
- [ ] Implement equivalent account, messaging, local storage, privacy controls, and error states.
- [ ] Add iOS secure key storage and platform-appropriate lifecycle/network behavior.
- [ ] Test on real iPhones and supported iOS versions; validate VoiceOver and Dynamic Type.
- [ ] Create Apple signing, certificates, identifiers, App Store Connect records, screenshots, privacy details, age rating, and review notes.
- [ ] Provide reviewer access/demo mode for account-based features where needed.
- [ ] Test through TestFlight and resolve all critical findings before App Review.

## Release 3 — public launch (not started)

- [ ] All launch-blocking CI checks pass on the release commit.
- [ ] No unresolved critical/high security issues.
- [ ] No known data-loss or account-isolation defects.
- [ ] Beta feedback and crash metrics reviewed; rollback and support procedures tested.
- [ ] Store listings and all declarations match the actual shipped product.
- [ ] Roll out gradually, monitor service health, and publish a clear support/contact path.

## Definition of done for a public messaging launch

Two separate test accounts on two physical devices can exchange messages over the internet; messages survive restart and transient network loss; retries do not duplicate messages; server-accepted and recipient-delivered states are distinct; authorization prevents cross-account access; account/data deletion works; privacy disclosures match actual behavior; Android and iOS builds pass their respective device and store review processes.

## Current known blockers

1. Orbit is local-only; no backend, account system, network delivery, push notifications, or iOS client.
2. Local prototype storage is not encrypted and must not be used for sensitive conversations.
3. End-to-end encryption is not implemented.
4. No physical-device or user-beta evidence has been recorded in this repository.
5. Store accounts, public policy URLs, store assets, and store submissions have not been completed.
