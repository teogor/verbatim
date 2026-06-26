# Core Concepts

Understanding Verbatim's core types is essential for effective logging.

## LogEvent

A `LogEvent` is an immutable snapshot of a single log occurrence. It flows through the pipeline to each configured sink.

```kotlin
data class LogEvent(
    val level: LogLevel,           // Severity level
    val loggerName: String,        // Tag/component name
    val message: String?,          // Log message (nullable)
    val throwable: Throwable?,     // Optional exception
    val attributes: Map<String, Any?>,  // Structured metadata
    val context: LogContext,       // Ambient context
    val thread: String,            // Thread name
    val timestamp: Instant         // Event timestamp
)
```

## LogLevel

Severity levels ordered from least to most severe:

```kotlin
enum class LogLevel(val emoji: String) {
    VERBOSE("💜"),  // Most detailed
    DEBUG("💚"),    // Debugging info
    INFO("💙"),     // General info
    WARN("💛"),     // Warnings
    ERROR("❤️"),    // Errors
    FATAL("💔"),    // Critical failures
    OFF("❌")       // Disables all logging
}
```

Setting `minimumLogLevel` passes that level and everything above it.

## Logger

The `Logger` is your primary interface for emitting log events:

```kotlin
val logger = Verbatim.logger(tag = "PaymentService")

// Each method accepts a lazy lambda
logger.verbose { "Detailed trace" }
logger.debug { "Debug info" }
logger.info { "Processing payment" }
logger.warn { "Retry attempt" }
logger.error(throwable = exception) { "Payment failed" }
logger.fatal { "Critical system failure" }

// With structured attributes
logger.info(
    attributes = {
        attr("user_id", "12345")
        attr("amount", 99.99)
    }
) { "Payment processed" }
```

## Lazy Evaluation

All logging calls use lambda syntax. The string is **never evaluated** if the log level is filtered out:

```kotlin
// Good - lambda only evaluated if DEBUG is enabled
logger.debug { "User: ${expensiveComputation()}" }

// Bad - always evaluates regardless of level
logger.debug("User: ${expensiveComputation()}")
```

## Verbatim

The `Verbatim` object is the main entry point for configuring the logging system:

```kotlin
Verbatim.install(
    LoggerConfig.Builder()
        .minLevel(LogLevel.DEBUG)
        .addSink(DefaultLogSink())
        .addSink(ConsoleSink())
        .build()
)

// Create loggers
val logger = Verbatim.logger("MyComponent")
```

## VerbatimPipeline

The `VerbatimPipeline` manages the collection of sinks:

```kotlin
// Add sinks to the pipeline
VerbatimPipeline.addSink(DefaultLogSink())
VerbatimPipeline.addSink(ConsoleSink(LogFormatters.default(showEmoji = true)))

// Remove sinks
VerbatimPipeline.removeSink(someSink)
```

## LogPipeline

The `LogPipeline` handles log level filtering and event processing:

```kotlin
// Set global minimum level
LogPipeline.minimumLogLevel = LogLevel.DEBUG

// Override level for specific tags
LogPipeline.override("NetworkModule", LogLevel.VERBOSE)
LogPipeline.override("ThirdPartySDK", LogLevel.ERROR)
```

## LogSink

Sinks are destinations for log events. Implement the `LogSink` interface to send logs anywhere:

```kotlin
interface LogSink {
    fun emit(event: LogEvent)
    fun flush()
}
```

Built-in sinks:

| Sink | Purpose |
|------|---------|
| `DefaultLogSink` | Platform-native output (Logcat, NSLog, etc.) |
| `ConsoleSink` | Console output with configurable formatter |
| `RemoteLogSink` | Forward to remote services via lambda |
| `TestLogSink` | Capture events for unit tests |

## LogFormatter

Formatters convert a `LogEvent` into a string:

```kotlin
fun interface LogFormatter {
    fun format(event: LogEvent): String
}
```

Built-in formatters:

| Formatter | Output |
|-----------|--------|
| `DefaultLogFormatter` | Human-readable single line |
| `JsonLogFormatter` | JSON for log aggregators |
| `CompactLogFormatter` | Minimal single line |
| `PrettyLogFormatter` | Multi-line with timestamps |

## LogAttributesBuilder

A DSL for building structured log attributes:

```kotlin
val attributes = buildAttributes {
    attr("user_id", "12345")
    attr("request_count", 3)
    attr("is_retry", true)
}
```

## LoggerConfig

Configuration for the logging pipeline:

```kotlin
val config = LoggerConfig.Builder()
    .minLevel(LogLevel.DEBUG)
    .globalAttribute("app_version", "1.0.0")
    .addSink(DefaultLogSink())
    .addSink(ConsoleSink())
    .override("NetworkModule", LogLevel.VERBOSE)
    .build()

Verbatim.install(config)
```

## TaggedLogger

A convenience wrapper for loggers with a fixed tag:

```kotlin
val logger = TaggedLogger("MyComponent")
logger.info { "Component initialized" }
logger.error(exception) { "Operation failed" }
```

## VerbatimLog

A simple API for quick logging without explicit logger creation:

```kotlin
// Simple logging with default tag
VerbatimLog.d { "Debug message" }
VerbatimLog.i { "Info message" }

// Logging with custom tag
VerbatimLog.d("Network") { "Request sent" }

// Creating tagged loggers
val logger = VerbatimLog.withTag("MyComponent")
```

## Next Steps

- [Structured Logging](structured-logging.md) - Attach typed metadata
- [Context Propagation](context-propagation.md) - Thread-safe context
