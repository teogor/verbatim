# Crashlytics

The `verbatim-crashlytics` module provides an abstract sink for integrating Verbatim with any crash reporting system. Users provide their own engine implementation.

## Overview

This module does not depend on any specific crash reporting SDK. Instead, it defines interfaces that you implement to bridge Verbatim logs to your preferred crash reporter (Firebase Crashlytics, Sentry, Bugsnag, Datadog, etc.).

## Installation

```kotlin
dependencies {
    implementation("dev.teogor.verbatim:verbatim-crashlytics:1.0.0-alpha01")
}
```

## Configuration

```kotlin
import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.crashlytics.CrashSink
import dev.teogor.verbatim.crashlytics.VerbatimCrashlytics

Verbatim.initialize {
    install(CrashSink) {
        engine = MyCrashEngine()  // Your implementation
        minLevel = LogLevel.ERROR
    }
}
```

## Abstract Interfaces

### CrashReportEngine

Implement this interface to provide your crash reporting backend:

```kotlin
interface CrashReportEngine {
    fun recordException(t: Throwable, attributes: Map<String, Any?> = emptyMap())
    fun log(message: String, severity: CrashSeverity = CrashSeverity.INFO)
}
```

### CrashSeverity

Severity levels for crash reporting:

```kotlin
enum class CrashSeverity {
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    FATAL
}
```

## Example Implementations

### Firebase Crashlytics

```kotlin
class FirebaseCrashEngine : CrashReportEngine {
    private val crashlytics = FirebaseCrashlytics.getInstance()

    override fun recordException(t: Throwable, attributes: Map<String, Any?>) {
        crashlytics.recordException(t)
        attributes.forEach { (key, value) ->
            crashlytics.setCustomKey(key.take(40), value?.toString()?.take(100) ?: "null")
        }
    }

    override fun log(message: String, severity: CrashSeverity) {
        crashlytics.log(message)
    }
}
```

### Sentry

```kotlin
class SentryCrashEngine : CrashReportEngine {
    override fun recordException(t: Throwable, attributes: Map<String, Any?>) {
        Sentry.captureException(t).apply {
            attributes.forEach { (key, value) ->
                setTag(key, value?.toString() ?: "null")
            }
        }
    }

    override fun log(message: String, severity: CrashSeverity) {
        val sentryLevel = when (severity) {
            CrashSeverity.DEBUG -> SentryLevel.DEBUG
            CrashSeverity.INFO -> SentryLevel.INFO
            CrashSeverity.WARNING -> SentryLevel.WARNING
            CrashSeverity.ERROR -> SentryLevel.ERROR
            CrashSeverity.FATAL -> SentryLevel.FATAL
        }
        Sentry.captureMessage(message, sentryLevel)
    }
}
```

### Bugsnag

```kotlin
class BugsnagCrashEngine : CrashReportEngine {
    override fun recordException(t: Throwable, attributes: Map<String, Any?>) {
        Bugsnag.notify(t) { error ->
            attributes.forEach { (key, value) ->
                error.addMetadata("verbatim", key, value)
            }
        }
    }

    override fun log(message: String, severity: CrashSeverity) {
        val bugsnagLevel = when (severity) {
            CrashSeverity.DEBUG -> Severity.DEBUG
            CrashSeverity.INFO -> Severity.INFO
            CrashSeverity.WARNING, CrashSeverity.ERROR -> Severity.WARNING
            CrashSeverity.FATAL -> Severity.ERROR
        }
        Bugsnag.notify(message, bugsnagLevel)
    }
}
```

### Custom HTTP Endpoint

```kotlin
class HttpCrashEngine(
    private val endpoint: String,
    private val httpClient: HttpClient
) : CrashReportEngine {
    override fun recordException(t: Throwable, attributes: Map<String, Any?>) {
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.post(endpoint) {
                setBody(mapOf(
                    "type" to "exception",
                    "message" to t.message,
                    "stackTrace" to t.stackTraceToString(),
                    "attributes" to attributes
                ))
            }
        }
    }

    override fun log(message: String, severity: CrashSeverity) {
        CoroutineScope(Dispatchers.IO).launch {
            httpClient.post(endpoint) {
                setBody(mapOf(
                    "type" to "log",
                    "message" to message,
                    "severity" to severity.name
                ))
            }
        }
    }
}
```

## Sink Configuration

### CrashSink

The `CrashSink` class bridges log events to your engine:

```kotlin
val crashSink = CrashSink().apply {
    engine = MyCrashEngine()
    minLevel = LogLevel.ERROR  // Only forward ERROR and above
}

VerbatimPipeline.addSink(crashSink)
```

### Level Filtering

Control which log levels are forwarded:

```kotlin
CrashSink().apply {
    engine = MyCrashEngine()
    minLevel = LogLevel.WARN   // WARN, ERROR, FATAL
}
```

### VerbatimCrashlytics Helper

Use the convenience object for quick setup:

```kotlin
VerbatimCrashlytics.install(MyCrashEngine()) {
    minLevel = LogLevel.ERROR
}
```

## Output Format

Logs forwarded to the crash reporter include:

```
[ERROR] PaymentService: Payment failed | attrs={orderId=ORD-001, amount=99.99} | ctx={requestId=req-123}
```

## Best Practices

1. **Filter appropriately** - Don't send verbose/debug logs to crash reporters
2. **Add context** - Include request IDs and user IDs for debugging
3. **Handle failures gracefully** - Your engine should not throw exceptions
4. **Batch when possible** - Some crash reporters support batch uploads
5. **Test your engine** - Verify logs reach the crash reporter correctly

## Next Steps

- [Compiler Plugin](compiler-plugin.md) - Strip logs from release builds
- [Testing](../testing.md) - Test crash reporting integration
