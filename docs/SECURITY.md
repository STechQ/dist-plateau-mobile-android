<h1 align="center">Security Guide</h1>
<p align="center"><strong>Enterprise-Grade Security Features</strong></p>

---

## Overview

Plateau SDK implements multiple layers of security to protect your application and user data. This guide covers all security features and best practices.

---

## Security Features

| Feature | Description | Status |
|---------|-------------|--------|
| AES Encryption | 256-bit data encryption | Enabled |
| SSL/TLS | Secure network communication | Enabled |
| Certificate Pinning | Prevent MITM attacks | Configurable |
| Secure Storage | Encrypted SharedPreferences | Enabled |
| Android Keystore | Hardware-backed key storage | Enabled |
| RSA Key Pairs | Public/private key generation | Available |
| SHA-512 Hashing | Secure data hashing | Available |
| Digital Signatures | Sign and verify data | Available |

---

## Data Encryption

### Encrypted Storage

All sensitive data can be stored using AES-256 encryption:

```javascript
// Store encrypted data
nb_setToEncryptedStorage({
  key: "user_token",
  value: "sensitive_token_value"
});

// Retrieve encrypted data
const token = nb_getFromEncryptedStorage({
  key: "user_token"
});
```

### How It Works

```
┌─────────────┐    ┌─────────────┐    ┌─────────────┐
│  Plain Text │───▶│ AES-256-GCM │───▶│  Encrypted  │
│    Data     │    │  Encryption │    │    Data     │
└─────────────┘    └─────────────┘    └─────────────┘
                         │
                         ▼
                ┌─────────────────┐
                │ Android Keystore│
                │   (Master Key)  │
                └─────────────────┘
```

### Encryption APIs

```javascript
// Custom encryption
const encrypted = nb_encrypt({
  data: "sensitive data",
  algorithm: "AES-256-GCM",
  key: encryptionKey
});

// Decryption
const decrypted = nb_decrypt({
  data: encrypted,
  algorithm: "AES-256-GCM",
  key: encryptionKey
});
```

---

## Network Security

### SSL/TLS Support

All network communications use TLS 1.2+ by default:

- Automatic HTTPS enforcement
- Strong cipher suites
- Certificate validation

### Certificate Pinning

Prevent man-in-the-middle attacks by pinning certificates:

```json
// In app settings
{
  "security": {
    "certificatePinning": {
      "enabled": true,
      "certificates": [
        {
          "domain": "api.example.com",
          "pins": [
            "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
          ]
        }
      ]
    }
  }
}
```

### SSL Pinning Flow

```
┌────────────┐     ┌────────────┐     ┌────────────┐
│   Client   │────▶│  Validate  │────▶│   Server   │
│  Request   │     │Certificate │     │  Response  │
└────────────┘     └────────────┘     └────────────┘
                         │
                         ▼
              ┌─────────────────────┐
              │ Compare with Pinned │
              │    Certificates     │
              └─────────────────────┘
                         │
              ┌──────────┴──────────┐
              ▼                     ▼
         ┌────────┐            ┌────────┐
         │  Match │            │No Match│
         │Continue│            │ Reject │
         └────────┘            └────────┘
```

---

## Cryptographic Operations

### RSA Key Generation

Generate secure RSA key pairs:

```javascript
// Generate 2048-bit RSA key pair
const keyPair = nb_cryp1raphy_generateKeyPair();

console.log(keyPair.publicKey);   // Base64 public key
console.log(keyPair.privateKey);  // Base64 private key
```

### Secure Hashing

Hash sensitive data using SHA-512:

```javascript
// SHA-512 hash
const hash = nb_cryptography_hash_sha512({
  value: "password123"
});

// Result: 64-character hex string
```

### Digital Signatures

Sign and verify data integrity:

```javascript
// Sign data
const signature = nb_cryptography_sign({
  value: "data to sign",
  privateKey: keyPair.privateKey,
  padding: "PKCS1",
  digest: "SHA256"
});

// Signature can be verified server-side
```

---

## Android Keystore Integration

The SDK leverages Android Keystore for secure key management:

### Features

- Hardware-backed key storage (when available)
- Keys never leave the secure hardware
- Biometric authentication support
- Key usage restrictions

### Implementation

```kotlin
// Internal SDK implementation
val keyStore = KeyStore.getInstance("AndroidKeyStore")
keyStore.load(null)

val keyGenerator = KeyGenerator.getInstance(
    KeyProperties.KEY_ALGORITHM_AES,
    "AndroidKeyStore"
)
```

---

## Permission Management

### Runtime Permissions

The SDK handles Android runtime permissions properly:

| Permission | Purpose | When Requested |
|------------|---------|----------------|
| `CAMERA` | QR scanning, photo capture | On first use |
| `LOCATION` | GPS positioning, maps | On first use |
| `READ_EXTERNAL_STORAGE` | File access | On first use |
| `WRITE_EXTERNAL_STORAGE` | File saving | On first use |

### Permission Flow

```javascript
// The SDK automatically handles permission requests
// when features requiring permissions are accessed

nb_getCurrentPosition({
  success: (position) => {
    // Permission was granted
  },
  fail: (error) => {
    if (error.code === "PERMISSION_DENIED") {
      // Permission was denied
    }
  }
});
```

---

## URL & Schema Validation

### Allowed URLs

Configure allowed URLs for network requests:

```json
{
  "security": {
    "allowedUrls": [
      "https://api.example.com/*",
      "https://cdn.example.com/*"
    ]
  }
}
```

### Allowed Schemas

Configure allowed deep link schemas:

```json
{
  "security": {
    "allowedSchemas": [
      "myapp://",
      "https://"
    ]
  }
}
```

### Validation Flow

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│   URL/Schema │────▶│   Validate   │────▶│    Allow/    │
│    Request   │     │   Against    │     │    Reject    │
└──────────────┘     │   Whitelist  │     └──────────────┘
                     └──────────────┘
```

---

## Secure Payment

For payment processing, use the secure payment method:

```javascript
nb_makeSecurePayment({
  amount: 100.00,
  currency: "TRY",
  cardData: encryptedCardData,
  success: (result) => {
    // Payment successful
  },
  fail: (error) => {
    // Payment failed
  }
});
```

### Payment Security Features

- End-to-end encryption
- PCI-DSS compliance support
- Tokenization support
- 3D Secure integration

---

## Best Practices

### Do's

- Always use encrypted storage for sensitive data
- Enable certificate pinning for production
- Use strong encryption algorithms (AES-256, SHA-512)
- Validate all URLs before making requests
- Request only necessary permissions

### Don'ts

- Don't store sensitive data in plain text
- Don't disable SSL verification in production
- Don't hardcode API keys or secrets
- Don't log sensitive information
- Don't bypass permission requirements

---

## Security Checklist

Before releasing your app:

- [ ] Encrypted storage enabled for sensitive data
- [ ] Certificate pinning configured
- [ ] URL whitelist configured
- [ ] Schema whitelist configured
- [ ] Debug logging disabled
- [ ] ProGuard/R8 enabled
- [ ] API keys secured
- [ ] Permission handling tested

---

## Reporting Vulnerabilities

If you discover a security vulnerability, please report it to:

**security@softtech.com.tr**

We appreciate responsible disclosure and will acknowledge your contribution.

---

<p align="center">
  <a href="./BRIDGE_API.md">← Bridge API</a> •
  <a href="./README.md">Home</a> •
  <a href="./CAPABILITIES.md">Capabilities →</a>
</p>
