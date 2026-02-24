<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-green?style=for-the-badge&logo=android" alt="Platform">
  <img src="https://img.shields.io/badge/API-24%2B-brightgreen?style=for-the-badge" alt="API">
  <img src="https://img.shields.io/badge/Kotlin-1.8.0-purple?style=for-the-badge&logo=kotlin" alt="Kotlin">
  <img src="https://img.shields.io/badge/License-Proprietary-blue?style=for-the-badge" alt="License">
</p>

<h1 align="center">Plateau Studio Mobile SDK</h1>
<h3 align="center">Build Dynamic Mobile Applications with Ease</h3>

<p align="center">
  <strong>A comprehensive Android SDK for creating dynamic, component-based mobile applications with JavaScript-Native bridge support.</strong>
</p>

---

## Overview

Plateau Studio Mobile SDK is an enterprise-grade Android development kit that enables rapid development of dynamic mobile applications. With **98+ pre-built UI components**, a robust **JavaScript-Native bridge**, and comprehensive **security features**, you can build feature-rich applications faster than ever.

### Key Highlights

| Feature | Description |
|---------|-------------|
| **98+ Components** | Ready-to-use UI elements for any interface |
| **JS-Native Bridge** | Seamless communication between web and native |
| **Enterprise Security** | AES encryption, SSL pinning, secure storage |
| **Dual Map Support** | Google Maps & Huawei Maps integration |
| **Real-time Features** | WebSocket support for live updates |

---

## Documentation

| Document | Description |
|----------|-------------|
| [Getting Started](./GETTING_STARTED.md) | Quick start guide and setup instructions |
| [UI Components](./COMPONENTS.md) | Complete list of 98+ UI components |
| [Bridge API](./BRIDGE_API.md) | JavaScript-Native bridge reference |
| [Security Guide](./SECURITY.md) | Security features and best practices |
| [Capabilities Overview](./CAPABILITIES.md) | Full SDK capabilities matrix |

---

## Quick Start

### Requirements

- Android Studio Arctic Fox or later
- Android SDK 24+ (Android 7.0)
- Kotlin 1.8.0+
- Java 17

### Installation

Add to your `build.gradle`:

```groovy
dependencies {
    implementation 'com.softtech.quick.sdk:quickaggregator:1.7.9'
}
```

### Basic Usage

```kotlin
// Initialize SDK
QuickManager.initialize(context, config)

// Load a page
QuickManager.loadPage("home", params)
```

---

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                     Application Layer                        │
├─────────────────────────────────────────────────────────────┤
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────────────┐  │
│  │   MiniApp   │  │  StudioApp  │  │    Custom App       │  │
│  └─────────────┘  └─────────────┘  └─────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                      Quick SDK Core                          │
│  ┌───────────┐ ┌───────────┐ ┌───────────┐ ┌─────────────┐  │
│  │  Bridge   │ │Components │ │ Rendering │ │   Network   │  │
│  └───────────┘ └───────────┘ └───────────┘ └─────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                    Platform Services                         │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌───────┐  │
│  │ Storage │ │Security │ │Location │ │  Media  │ │  Maps │  │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘ └───────┘  │
└─────────────────────────────────────────────────────────────┘
```

---

## SDK Modules

| Module | Purpose |
|--------|---------|
| `quickmobileandroid` | Core SDK with managers and services |
| `quickbridge` | JavaScript-Native communication engine |
| `quickcomponents` | 98+ pre-built UI components |
| `qcommon` | Shared utilities and models |
| `networkfw` | Network layer with SSL pinning |
| `renderingfw` | UI rendering engine |

---

## Support

For technical support and questions:
- Create an issue in this repository
- Contact: sdk-support@softtech.com.tr

---

<p align="center">
  <sub>Built with care by Softtech</sub>
</p>
