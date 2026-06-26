# Getting Started

This guide walks you through installing Verbatim, initializing the engine, and writing your first log statements.

## Installation

### Version Catalog (Recommended)

Add Verbatim to your version catalog:

```toml title="gradle/libs.versions.toml"
[versions]
teogor-verbatim = "1.0.0-alpha01"

[libraries]
teogor-verbatim-core = { module = "dev.teogor.verbatim:verbatim-core", version.ref = "teogor-verbatim" }
```

Then in your module:

```kotlin title="build.gradle.kts"
dependencies {
    implementation(libs.teogor.verbatim.core)
}
```

### Manual Setup

```kotlin title="build.gradle.kts"
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("dev.teogor.verbatim:verbatim-core:1.0.0-alpha01")
        }
    }
}
```

## Platform Setup

Verbatim works out of the box on all supported platforms with no additional configuration:

=== "Android"

    ```kotlin title="MyApplication.kt"
    class MyApp : Application() {
        override fun onCreate() {
            super.onCreate()
            Verbatim.install(
                visuals = LogVisuals.Emojis,
                minLevel = if (BuildConfig.DEBUG) LogLevel.DEBUG else LogLevel.INFO
            )
        }
    }
    ```

=== "iOS"

    ```kotlin title="AppDelegate.kt"
    fun main(args: Array<String>) {
        Verbatim.install(
            visuals = LogVisuals.Geometric,
            minLevel = LogLevel.DEBUG
        )
        // ... rest of app startup
    }
    ```

=== "JVM / Desktop"

    ```kotlin title="Main.kt"
    fun main() {
        Verbatim.install(
            visuals = LogVisuals.Minimalist,
            minLevel = LogLevel.DEBUG
        )
        // ... your application
    }
    ```

=== "JS / Wasm"

    ```kotlin title="Main.kt"
    fun main() {
        Verbatim.install(
            visuals = LogVisuals.AnsiColors,
            minLevel = LogLevel.DEBUG
        )
        // ... your application
    }
    ```

### Advanced Configuration

For more control, use the full `LoggerConfig.Builder`:

```kotlin
Verbatim.install(
    LoggerConfig.Builder()
        .minLevel(LogLevel.DEBUG)
        .visuals(LogVisuals.Geometric)
        .globalAttribute("app_version", "1.0.0")
        .addSink(ConsoleSink(LogFormatters.pretty(LogVisuals.Geometric)))
        .addSink(FileSink(LogFormatters.json(), "logs/app.log"))
        .override("NetworkModule", LogLevel.VERBOSE)
        .build()
)
```

## Basic Usage

### Creating a Logger

```kotlin
val logger = Verbatim.logger(tag = "MyComponent")
```

### Logging Messages

```kotlin
// Lazy evaluation - string only built if level is enabled
logger.debug { "Fetching data from network..." }

// With exceptions
try {
    riskyOperation()
} catch (e: Exception) {
    logger.error(throwable = e) { "Operation failed" }
}

// Structured logging with attributes
logger.info(
    attributes = {
        attr("userId", 123)
        attr("action", "login")
    }
) { "User authenticated" }
```

### Simple API

For quick logging without explicit logger creation:

```kotlin
// Simple logging with default tag
VerbatimLog.d { "Debug message" }
VerbatimLog.i { "Info message" }

// Logging with custom tag
VerbatimLog.d("Network") { "Request sent" }
```

### Tagged Logger

For convenience, use a tagged logger with a fixed tag:

```kotlin
val logger = TaggedLogger("MyComponent")
logger.info { "Component initialized" }
logger.error(exception) { "Operation failed" }
```

### Log Levels

| Level | Weight | Usage |
|-------|--------|-------|
| `VERBOSE` | 1 | Most detailed, development only |
| `DEBUG` | 2 | Debugging information |
| `INFO` | 3 | General informational messages |
| `WARN` | 4 | Potential issues, non-critical |
| `ERROR` | 5 | Errors and failures |
| `FATAL` | 6 | Critical failures that will lead to app termination |
| `OFF` | MAX_VALUE | Disables all logging (config sentinel only) |

## Next Steps

- [Visual Themes](visual-themes.md) - Customize your log output with built-in themes or create your own
- [Core Concepts](core-concepts.md) - Learn about LogEvent, LogLevel, and the logging pipeline
- [Structured Logging](structured-logging.md) - Attach typed metadata to your logs
- [Context Propagation](context-propagation.md) - Thread-safe context across coroutines
- [Sinks & Formatters](sinks-and-formatters.md) - Route logs to multiple destinations
