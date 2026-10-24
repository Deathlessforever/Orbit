# Orbit Security Model — Initial Draft

This document is a starting point for engineering review, not a claim that Orbit is currently secure or end-to-end encrypted.

## Assets to protect

- Message content and attachments.
- Account credentials and session tokens.
- Device identity and encryption keys.
- Contact and group membership information.
- Delivery metadata, presence, and notification content.
- Local message database and backups.

## Initial threat assumptions

Potential attackers include a network observer, a malicious or compromised server operator, a stolen unlocked device, a malicious participant, a spammer, and a relay node that tries to drop, replay, or tamper with forwarded packets.

The first version cannot promise protection against every compromised endpoint, screenshots, recipient copying, or coercive access to an unlocked device.

## Required safeguards before claiming end-to-end encryption

- Select a reviewed, established protocol and maintained implementation; do not design custom cryptography.
- Document identity keys, session setup, rotation, recovery, and multi-device behavior.
- Store sensitive keys using platform-provided secure storage where appropriate.
- Verify message authenticity and integrity before displaying content.
- Keep message bodies and secrets out of logs, crash reports, and analytics.
- Define group membership changes and how they affect access to future messages.
- Document metadata visible to servers and push-notification providers.
- Provide a reproducible test plan and arrange independent security review before broad release.

## Offline relay safeguards to design

- Relay must be opt-in and visible to the user.
- Use authenticated, integrity-protected envelopes; relays should not need plaintext content.
- Enforce expiry, size limits, hop limits, rate limits, and deduplication.
- Prevent a relay from silently claiming final delivery.
- Make battery/data costs and retention behavior clear.
- Do not advertise global offline reachability: messages need a connected chain of participating devices or an online path.

## Release gate

Do not label Orbit “secure,” “anonymous,” “unhackable,” or “end-to-end encrypted” as a blanket claim unless the implemented design, threat model, tests, and review justify the exact wording.
