<h1 align="center">Bridge API Reference</h1>
<p align="center"><strong>JavaScript-Native Communication Bridge</strong></p>

---

## Overview

The Quick Bridge enables seamless communication between JavaScript code and native Android functionality. All bridge methods are accessible via the `nb_` prefix.

---

## API Categories

- [Component Management](#component-management)
- [Network Requests](#network-requests)
- [Data Storage](#data-storage)
- [Cookie Management](#cookie-management)
- [Cryptography](#cryptography)
- [Theme & UI](#theme--ui)
- [Location Services](#location-services)
- [Device Information](#device-information)
- [Navigation & Dialogs](#navigation--dialogs)
- [Regional Settings](#regional-settings)
- [File Operations](#file-operations)
- [Event Lifecycle](#event-lifecycle)
- [Utilities](#utilities)

---

## Component Management

APIs for creating, modifying, and managing UI components.

### `nb_createComponent`
Creates a new UI component dynamically.

```javascript
nb_createComponent({
  type: "VButton",
  props: { text: "Click Me" },
  parentId: "container1"
});
```

### `nb_deleteComponent`
Removes a component from the view hierarchy.

```javascript
nb_deleteComponent({ componentId: "button1" });
```

### `nb_setProperty`
Sets a property value on a component.

```javascript
nb_setProperty({
  pageId: "home",
  componentId: "textField1",
  prop: "value",
  value: "Hello World"
});
```

### `nb_getProperty`
Retrieves a property value from a component.

```javascript
const value = nb_getProperty({
  pageId: "home",
  componentId: "textField1",
  prop: "value"
});
```

### `nb_setVisibility`
Controls component visibility.

```javascript
nb_setVisibility({
  pageId: "home",
  componentId: "panel1",
  visible: false
});
```

### `nb_componentHasProp`
Checks if a component has a specific property.

```javascript
const hasProp = nb_componentHasProp({
  componentName: "VTextField",
  propName: "maxLength"
});
```

### `nb_executeMethod2`
Executes a method on a component.

```javascript
nb_executeMethod2({
  pageId: "home",
  componentId: "form1",
  method: "validate",
  params: []
});
```

---

## Network Requests

APIs for HTTP communication.

### `nb_get`
Performs an HTTP GET request.

```javascript
nb_get({
  url: "https://api.example.com/data",
  headers: { "Authorization": "Bearer token" },
  success: (response) => { /* handle success */ },
  fail: (error) => { /* handle error */ },
  options: { timeout: 30000 }
});
```

### `nb_post`
Performs an HTTP POST request.

```javascript
nb_post({
  url: "https://api.example.com/data",
  body: { name: "John", email: "john@example.com" },
  headers: { "Content-Type": "application/json" },
  success: (response) => { /* handle success */ },
  fail: (error) => { /* handle error */ }
});
```

### `nb_put`
Performs an HTTP PUT request.

```javascript
nb_put({
  url: "https://api.example.com/data/1",
  body: { name: "Updated Name" },
  headers: {},
  success: callback,
  fail: errorCallback
});
```

### `nb_delete`
Performs an HTTP DELETE request.

```javascript
nb_delete({
  url: "https://api.example.com/data/1",
  headers: {},
  success: callback,
  fail: errorCallback
});
```

---

## Data Storage

APIs for persistent data storage.

### Local Storage

```javascript
// Save data
nb_setLocalStorage({ key: "username", value: "john_doe" });

// Retrieve data
const username = nb_getItemFromLocalStorage({ key: "username" });

// Remove data
nb_removeFromLocalStorage({ key: "username" });
```

### Encrypted Storage

Secure storage with AES encryption.

```javascript
// Save encrypted
nb_setToEncryptedStorage({ key: "token", value: "secret_token" });

// Retrieve encrypted
const token = nb_getFromEncryptedStorage({ key: "token" });

// Remove encrypted
nb_removeItemFromEncryptedStorage({ key: "token" });

// Clear all encrypted storage
nb_clearEncryptedStorage();
```

### Permanent Storage (Database)

SQLite-based persistent storage.

```javascript
// Save to database
nb_setPermanentStore({ key: "settings", value: jsonData });

// Retrieve from database
const settings = nb_getPermanentStore({ key: "settings" });

// Delete from database
nb_deletePermanentStore({ key: "settings" });

// Get all entries
const allData = nb_getAllPermanentStore();

// Switch database
nb_changePermanentDb({ dbName: "user_data" });

// Delete database
nb_deletePermanentDb({ dbName: "old_data" });

// Get current database name
const dbName = nb_getCurrentPermanentDbName();
```

### Mock Storage

For testing and development.

```javascript
// Write mock data
nb_mock_writeDisk({ key: "mockKey", data: "mockValue" });

// Read mock data
const mockData = nb_mock_readDisk({ key: "mockKey" });
```

---

## Cookie Management

APIs for managing cookies.

```javascript
// Set cookie
nb_setCookie({ key: "session", value: "abc123" });

// Get cookie
const session = nb_getCookie({ key: "session" });

// Get all cookies
const allCookies = nb_getAllCookies();

// Remove cookie
nb_removeCookie({ key: "session" });

// Clear all cookies
nb_clearCookies();
```

---

## Cryptography

APIs for encryption, hashing, and digital signatures.

### Key Generation

```javascript
// Generate RSA key pair
const keyPair = nb_cryp1raphy_generateKeyPair();
// Returns: { publicKey: "...", privateKey: "..." }
```

### Hashing

```javascript
// SHA-512 hash
const hash = nb_cryptography_hash_sha512({ value: "data to hash" });

// General hash
const hash = nb_hash({
  algorithm: "SHA-256",
  value: "data to hash"
});
```

### Encryption/Decryption

```javascript
// Encrypt data
const encrypted = nb_encrypt({
  data: "sensitive data",
  key: publicKey
});

// Decrypt data
const decrypted = nb_decrypt({
  data: encryptedData,
  key: privateKey
});
```

### Digital Signatures

```javascript
// Sign data
const signature = nb_cryptography_sign({
  value: "data to sign",
  privateKey: privateKey,
  padding: "PKCS1",
  digest: "SHA256"
});
```

---

## Theme & UI

APIs for theme and UI management.

```javascript
// Set theme
nb_setTheme({ themeName: "dark" });

// Get available themes
const themes = nb_getThemeList();
// Returns: ["light", "dark", "custom"]
```

---

## Location Services

APIs for device location.

```javascript
// Get current position
nb_getCurrentPosition({
  success: (position) => {
    console.log(position.latitude, position.longitude);
  },
  fail: (error) => {
    console.log(error.message);
  }
});
```

---

## Device Information

APIs for device and client information.

```javascript
// Get device ID
const deviceId = nb_getDeviceId();

// Get client info
const clientInfo = nb_getClientInfo();
// Returns: { platform, version, deviceModel, osVersion, ... }

// Get deep link parameters
const params = nb_getDeepLinkParams();

// Get network connection status
const networkStatus = nb_getNetworkConnection();
// Returns: { connected: true, type: "wifi" }

// Listen for network changes
nb_listenNetworkConnection({
  onChange: (status) => {
    console.log("Network changed:", status);
  }
});
```

---

## Navigation & Dialogs

APIs for navigation and user dialogs.

### Navigation

```javascript
// Go to dashboard
nb_goDashboard();

// Navigate to native screen
nb_goNative({
  screenId: "settings",
  args: { section: "profile" },
  transition: { type: "slide", direction: "left" }
});

// Redirect to URL
nb_redirect({ url: "https://example.com" });
```

### Dialogs

```javascript
// Show alert
nb_alert({
  title: "Warning",
  text: "Are you sure?",
  category: "warning",
  actionButtons: [
    { text: "Cancel", action: "cancel" },
    { text: "OK", action: "confirm" }
  ]
});
```

### Host Triggers

```javascript
// Trigger host event
nb_hostTrigger({
  method: "customMethod",
  params: { key: "value" }
});
```

---

## Regional Settings

APIs for localization and regional settings.

```javascript
// Get available regions
const regions = nb_getRegionList();

// Set region
nb_setRegion({ regionName: "tr-TR" });

// Get formatting definitions
const formatting = nb_getFormattingDefinitionByCurrentRegion();
// Returns: { dateFormat, numberFormat, currencySymbol, ... }
```

---

## File Operations

APIs for file handling.

```javascript
// Download file
nb_download({
  fileName: "document.pdf",
  fileContent: base64Content
});

// Share data
nb_shareData({
  fileName: "share.txt",
  data: "Content to share"
});

// Copy to clipboard
nb_copyToClipboard({ text: "Copied text" });
```

---

## Event Lifecycle

APIs for page and event lifecycle.

```javascript
// Mark event as complete
nb_eventComplete({
  pageId: "home",
  componentId: "button1",
  eventName: "onClick"
});

// Notify page completion
nb_PageCompleted({ pageId: "home" });

// Notify page render start
nb_PageRenderStarted({ pageId: "home" });

// Update validation
nb_updateCompValidation({
  pageId: "home",
  componentId: "form1",
  valid: true,
  message: ""
});
```

---

## Utilities

General utility APIs.

```javascript
// High resolution timer
const time = nb_highResTimer();

// Generate UUID
const uuid = nb_randomUUID();

// Show/hide loading
nb_showLoading({ show: true });
nb_showLoading({ show: false });

// Logout
nb_logout();

// Base64 encoding/decoding
const encoded = btoa("Hello World");
const decoded = atob(encoded);

// Logging
consolelog({ message: "Debug info", level: "debug" });
bulkLog({ logs: [log1, log2, log3] });
```

---

## Error Handling

All bridge methods support error callbacks:

```javascript
nb_post({
  url: "https://api.example.com/data",
  body: {},
  success: (response) => {
    // Handle success
  },
  fail: (error) => {
    // error.code - Error code
    // error.message - Error message
    // error.status - HTTP status (if applicable)
  }
});
```

---

<p align="center">
  <a href="./COMPONENTS.md">← Components</a> •
  <a href="./README.md">Home</a> •
  <a href="./SECURITY.md">Security →</a>
</p>
