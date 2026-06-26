# Persistence

The `verbatim-persistence` module provides Okio-backed file streaming, automated log rotation, and local encryption for crash-resilient local storage.

## Overview

This module enables persistent log storage that survives application restarts and process termination. Logs are written to local files using Okio and can be encrypted for security.

## Installation

```kotlin
dependencies {
    implementation("dev.teogor.verbatim:verbatim-persistence:1.0.0-alpha01")
}
```

## Configuration

```kotlin
import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.persistence.FileLogging

Verbatim.initialize {
    install(FileLogging) {
        maxFileSizeInBytes = 3 * 1024 * 1024  // 3MB per file
        maxHistoryFiles = 4                    // Keep 4 files
        isEncryptionEnabled = true             // Encrypt files
    }
}
```

## Features

### File Rotation

Logs automatically rotate when files reach the configured size limit:

| Parameter | Description | Default |
|-----------|-------------|---------|
| `maxFileSizeInBytes` | Maximum size per file segment | 3MB |
| `maxHistoryFiles` | Number of historical files to keep | 4 |

### Encryption

Enable inline encryption to protect logs at rest:

```kotlin
install(FileLogging) {
    isEncryptionEnabled = true
    // Encryption key is derived from platform keystore
}
```

| Platform | Encryption |
|----------|------------|
| Android | AES-GCM via Jetpack Security |
| iOS | AES-GCM via CryptoKit |
| JVM | AES-GCM via JCA |
| JS / Wasm | AES-GCM via Web Crypto API |

### File Sharing

Access log files for sharing or export:

```kotlin
val logFiles = FileLogging.getLogFiles()
// Returns list of current log file paths
```

## Platform Paths

| Platform | Default Path |
|----------|--------------|
| Android | `/data/user/0/{package}/files/verbatim/` |
| iOS | `Documents/verbatim/` |
| JVM | `~/.local/share/{app}/verbatim/` |
| JS | `localStorage` (limited) |

## Best Practices

1. **Configure rotation** - Set appropriate file sizes for your use case
2. **Enable encryption** - Protect sensitive log data in production
3. **Limit history** - Keep only necessary historical files
4. **Test file cleanup** - Verify files are properly cleaned up

## Next Steps

- [Middleware](middleware.md) - Mask sensitive data before persistence
- [Tracing](tracing.md) - Add performance metrics to logs
