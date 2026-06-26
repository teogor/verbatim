# Sinks & Formatters

Sinks are destinations for log events. Formatters convert events into strings. Together they form the output pipeline.

## Sinks

### LogSink Interface

```kotlin
interface LogSink {
    fun emit(event: LogEvent)
    fun flush()
}
```

### DefaultLogSink

Platform-native logging (recommended for most use cases):

```kotlin
VerbatimPipeline.addSink(DefaultLogSink())
```

| Platform | Output |
|----------|--------|
| Android | `android.util.Log` (Logcat) |
| Apple | `NSLog` |
| JVM / Linux | `stdout` |
| JS / Wasm | `console` |

### ConsoleSink

Console output with a configurable formatter:

```kotlin
VerbatimPipeline.addSink(ConsoleSink(LogFormatters.default(LogVisuals.Geometric)))
```

### RemoteLogSink

Forward logs to any remote service via a lambda:

```kotlin
VerbatimPipeline.addSink(
    RemoteLogSink(
        logFormatter = LogFormatters.json()
    ) { payload ->
        myHttpClient.post("/logs", payload)
    }
)
```

### TestLogSink

Capture events for unit testing:

```kotlin
val testSink = TestLogSink()

VerbatimPipeline.addSink(testSink)

logger.info { "Test message" }

assertEquals(1, testSink.events.size)
assertEquals("Test message", testSink.lastMessage())
assertTrue(testSink.hasLevel(LogLevel.INFO))
```

### Custom Sinks

Implement `LogSink` for custom destinations:

```kotlin
class FirebaseLogSink(
    private val minLevel: LogLevel = LogLevel.WARN
) : LogSink {

    override fun emit(event: LogEvent) {
        if (event.level.ordinal < minLevel.ordinal) return

        val message = LogFormatters.json().format(event)

        when (event.level) {
            LogLevel.ERROR, LogLevel.FATAL -> {
                val throwable = event.throwable ?: RuntimeException(message)
                FirebaseCrashlytics.getInstance().recordException(throwable)
            }
            else -> FirebaseCrashlytics.getInstance().log(message)
        }
    }

    override fun flush() {}
}
```

## Formatters

### LogFormatter Interface

```kotlin
fun interface LogFormatter {
    fun format(event: LogEvent): String
}
```

### Built-in Formatters

Obtain via `LogFormatters`:

#### Default

Concise single-line output:

```kotlin
LogFormatters.default(LogVisuals.Emojis)
// 💙 [INFO MyApp]: Payment accepted
```

#### JSON

Single-line JSON for log aggregators:

```kotlin
LogFormatters.json()
// {"level":"INFO","levelWeight":3,"logger":"PaymentService","message":"Payment accepted","timestamp":1711785600000}
```

#### Compact

Minimal single-line output:

```kotlin
LogFormatters.compact(LogVisuals.Minimalist)
// [I] PaymentService: Payment accepted
```

#### Pretty

Multi-line human-readable output:

```kotlin
LogFormatters.pretty(LogVisuals.Geometric)
```

Output:
```
🟦 [INFO PaymentService] 2024-01-15T10:30:00Z [main] Payment accepted
  ↳ Exception: Something went wrong
  Attributes: {amount=99.99, currency=USD}
  Context: {request_id=req-123}
```

### Custom Formatters

```kotlin
val myFormatter = LogFormatter { event ->
    "[${event.level}] ${event.loggerName}: ${event.message}"
}

ConsoleSink(myFormatter)
```

### Visual Configuration

All formatters accept a `LogVisualConfig` parameter:

```kotlin
// Use built-in themes
LogFormatters.default(LogVisuals.Emojis)
LogFormatters.pretty(LogVisuals.Geometric)
LogFormatters.compact(LogVisuals.Minimalist)

// Custom theme
val custom = LogVisuals.custom {
    on(LogLevel.INFO) { indicator = "ℹ️"; label = "INF" }
    on(LogLevel.ERROR) { indicator = "❌"; label = "ERR" }
}
LogFormatters.default(custom)
```

## Multiple Sinks

Configure multiple sinks for different destinations:

```kotlin
VerbatimPipeline.addSink(DefaultLogSink())                            // Console
VerbatimPipeline.addSink(RemoteLogSink { payload -> send(payload) })  // Remote
```

## Per-Tag Level Overrides

Override log levels for specific tags:

```kotlin
LogPipeline.minimumLogLevel = LogLevel.INFO
LogPipeline.override("NetworkModule", LogLevel.VERBOSE)
LogPipeline.override("ThirdPartySDK", LogLevel.ERROR)
```

Or via `LoggerConfig`:

```kotlin
Verbatim.install(
    LoggerConfig.Builder()
        .minLevel(LogLevel.INFO)
        .visuals(LogVisuals.Geometric)
        .override("NetworkModule", LogLevel.VERBOSE)
        .override("ThirdPartySDK", LogLevel.ERROR)
        .addSink(DefaultLogSink())
        .build()
)
```

## Next Steps

- [Visual Themes](visual-themes.md) - Customize log output with built-in themes
- [Testing](testing.md) - Verify logging behavior in tests
- [Crashlytics](modules/crashlytics.md) - Abstract crash reporting integration
