# Tracing

The `verbatim-tracing` module provides micro-metrics, execution block timers, and OpenTelemetry-compatible spans for performance telemetry.

## Overview

This module enables fine-grained performance monitoring by wrapping code blocks with timing instrumentation and producing structured span data compatible with OpenTelemetry.

## Installation

```kotlin
dependencies {
    implementation("dev.teogor.verbatim:verbatim-tracing:1.0.0-alpha01")
}
```

## Configuration

```kotlin
import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.tracing.TracingEngine

Verbatim.initialize {
    install(TracingEngine) {
        // Configure trace output
        exportFormat = TraceExportFormat.OPENTELEMETRY
    }
}
```

## Features

### Block Timing

Wrap code blocks to automatically capture execution duration:

```kotlin
import dev.teogor.verbatim.tracing.traceBlock

val result = logger.traceBlock(
    name = "DiskIOConfigDecryption",
    level = LogLevel.INFO
) {
    cryptoEngine.decryptPayload(securedBytes)
}
// Automatically logs: duration_ms=84, block_status=SUCCESS
```

### Trace Attributes

Attach metadata to traces:

```kotlin
val result = logger.traceBlock(
    name = "ApiCall",
    level = LogLevel.DEBUG,
    attributes = mapOf(
        "endpoint" to "/api/users",
        "method" to "GET"
    )
) {
    httpClient.get("/api/users")
}
```

### Error Handling

Traces automatically capture exceptions:

```kotlin
val result = logger.traceBlock(
    name = "RiskyOperation",
    level = LogLevel.ERROR
) {
    performRiskyOperation()
}
// On exception: block_status=FAILED, exception_class, exception_message
```

### OpenTelemetry Spans

Start and end spans manually for complex operations:

```kotlin
import dev.teogor.verbatim.tracing.startSpan

val span = logger.startSpan("CloudSyncOperation")

try {
    span.setAttribute("payload_weight_kb", dataStream.size / 1024)
    cloudDriver.upload(dataStream)
    span.setStatus(SpanStatus.OK)
} catch (e: Exception) {
    span.fail(e)
} finally {
    span.end()
}
```

### Span Hierarchy

Create nested spans for complex operations:

```kotlin
val parentSpan = logger.startSpan("OrderProcessing")

try {
    val validationSpan = parentSpan.startChild("ValidateOrder")
    validateOrder(order)
    validationSpan.end()

    val paymentSpan = parentSpan.startChild("ProcessPayment")
    processPayment(order)
    paymentSpan.end()

    parentSpan.setStatus(SpanStatus.OK)
} catch (e: Exception) {
    parentSpan.fail(e)
} finally {
    parentSpan.end()
}
```

## Trace Output Format

### OpenTelemetry Compatible

```json
{
  "traceId": "abc123",
  "spanId": "def456",
  "parentSpanId": "ghi789",
  "name": "DiskIOConfigDecryption",
  "startTime": "2024-01-15T10:30:00Z",
  "endTime": "2024-01-15T10:30:01Z",
  "durationMs": 84,
  "status": "OK",
  "attributes": {
    "block_status": "SUCCESS"
  }
}
```

### Console Output

```
[TRACE] DiskIOConfigDecryption: 84ms (SUCCESS)
[TRACE] ApiCall: GET /api/users - 156ms (OK)
[TRACE] OrderProcessing > ValidateOrder: 12ms (OK)
[TRACE] OrderProcessing > ProcessPayment: 234ms (OK)
```

## Metrics Collection

Collect aggregate metrics:

```kotlin
val metrics = TracingEngine.getMetrics()

// Average response time
println("Avg response: ${metrics.averageDurationMs("ApiCall")}ms")

// Success rate
println("Success rate: ${metrics.successRate("ApiCall")}%")

// P95 latency
println("P95 latency: ${metrics.percentile("ApiCall", 95)}ms")
```

## Best Practices

1. **Name traces semantically** - Use descriptive names like `DatabaseQuery` not `Trace1`
2. **Add context** - Attach relevant attributes for debugging
3. **Handle errors** - Ensure spans are properly closed in finally blocks
4. **Use hierarchy** - Nest related operations under parent spans

## Next Steps

- [UI](ui.md) - Visualize traces in the on-device console
- [Crashlytics](crashlytics.md) - Report traces to crash reporters
