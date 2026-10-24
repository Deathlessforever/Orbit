# Orbit Privacy Notice — Development Draft

**This is a development draft, not the final public privacy policy.** Before store submission, publish a reviewed version at a stable public URL and update it to match the exact release, backend, analytics, crash-reporting tools, and other SDKs.

## Current prototype behavior

The current development build is a local Android prototype. It lets a user create conversation labels and save message text on the same device. The prototype does not send those messages to another person or to an Orbit server.

- **Data entered:** conversation names and message text entered by the user.
- **Where it is stored:** in app-private local storage on the Android device using SharedPreferences JSON.
- **Network transmission:** the current local-only prototype has no messaging backend and does not transmit conversation content to an Orbit server.
- **Encryption:** local conversation content is not encrypted by the app. Do not store sensitive or confidential information in this prototype.
- **Deletion:** the app provides a user control to delete all locally stored conversations. Uninstalling the app also removes its app-private data under normal Android behavior, but users should use the in-app deletion control when available.
- **Analytics and advertising:** no analytics or advertising SDK is intentionally configured in the current prototype. This must be re-checked against the actual release build and all transitive dependencies.
- **Permissions:** the current prototype does not request contacts, microphone, camera, or location permissions.

## Before public messaging service launch

This draft is not sufficient for the future online product. The final policy must explain, as applicable, account identifiers, contact discovery, message routing and storage, device and delivery metadata, push-notification providers, security logs, diagnostics, service providers, international processing, retention periods, account deletion, legal requests, children's privacy, and how users can contact the operator or exercise applicable rights.

Do not claim end-to-end encryption, anonymous use, zero data collection, or that the service cannot access message content unless the shipped architecture and independent review substantiate that exact claim.

## Contact

**Not yet configured.** Add a monitored support/privacy email and the legal name or business identity of the service operator before publishing this policy.

## Version history

- 2026-10-24: initial development draft describing the local-only Android prototype.
