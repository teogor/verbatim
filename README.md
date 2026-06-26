# 📖 Verbatim

> is a zero-allocation structured logging and telemetry library for Kotlin Multiplatform (KMP) supporting automatic PII masking, performance tracing, and crash sinks.

Unlike standard logging wrappers, **Verbatim** is engineered for efficiency with zero-allocation lazy evaluation, structured telemetry, and a modular architecture that lets you scale from simple console logging to advanced persistence and tracing.

---

## 🚀 Quick Start

### 1. Add Dependency

Add the core dependency to your `commonMain` source set:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("dev.teogor.verbatim:verbatim-core:1.0.0-alpha01")
        }
    }
}
```

### 2. Initialize Verbatim

Initialize the engine once at your application's entry point:

```kotlin
import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.core.LogLevel

Verbatim.initialize {
    minimumLogLevel = LogLevel.DEBUG
}
```

### 3. Basic Usage

Create a logger and start logging using lazy lambdas for zero-overhead in production:

```kotlin
val logger = Verbatim.logger(tag = "MyComponent")

// Lazy evaluation: The string is only built if DEBUG level is enabled
logger.debug { "Fetching data from network..." }

// Structured logging with attributes
logger.info(
    attributes = mapOf("retry_count" to 3)
) { "Request succeeded after retries." }
```

---

## 🏗️ Modular Architecture

Verbatim is built to be modular. Only pull in what you need:

- `verbatim-core`: Core pipeline and console logging.
- `verbatim-persistence`: File-based logging with rotation (powered by Okio).
- `verbatim-middleware`: Privacy masking (PII) and log transformation.
- `verbatim-tracing`: Micro-metrics and OpenTelemetry support.
- `verbatim-ui`: Compose Multiplatform on-device diagnostic console.

---

## 📚 Documentation & Advanced Features

For advanced features like **PII Masking**, **File Persistence**, **Distributed Tracing**, and **Bytecode Stripping**, please visit our documentation:

👉 **[Read the Full Documentation](https://source.teogor.dev/verbatim/)**

---

## 📜 License

Licensed under the Apache License, Version 2.0.
