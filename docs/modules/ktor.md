# Ktor

The `verbatim-ktor` module provides Ktor HTTP client call tracking and trace parent injection for distributed tracing.

## Overview

This module automatically instruments Ktor HTTP client calls with logging and trace context propagation, enabling end-to-end request tracing across services.

## Installation

```kotlin
dependencies {
    implementation("dev.teogor.verbatim:verbatim-ktor:1.0.0-alpha01")
}
```

## Configuration

```kotlin
import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.ktor.KtorLogging

Verbatim.initialize {
    install(KtorLogging) {
        // Configure logging behavior
        logRequests = true
        logResponses = true
        logHeaders = false  // Avoid logging sensitive headers
    }
}
```

## Features

### Automatic Request Logging

All HTTP requests are automatically logged:

```kotlin
// Request logged:
// [INFO] HttpClient: POST https://api.example.com/users
// [INFO] HttpClient: Response 201 Created (156ms)
```

### Trace Parent Injection

Automatically inject trace context into outgoing requests:

```kotlin
val client = HttpClient {
    install(KtorLogging) {
        injectTraceParent = true
    }
}

// Headers automatically added:
// traceparent: 00-abc123def456-789ghi012-01
// tracestate: vendor=value
```

### Request/Response Attributes

Attach metadata to traces:

```kotlin
install(KtorLogging) {
    logRequestBody = true
    logResponseBody = false  // Avoid large payloads
    maxBodySize = 1024       // Limit logged body size
}
```

### Error Tracking

Failed requests are logged with full context:

```kotlin
// On error:
// [ERROR] HttpClient: POST https://api.example.com/users failed
//   status: 500 Internal Server Error
//   duration: 234ms
//   exception: IOException
```

## Configuration Options

| Option | Type | Default | Description |
|--------|------|---------|-------------|
| `logRequests` | `Boolean` | `true` | Log outgoing requests |
| `logResponses` | `Boolean` | `true` | Log incoming responses |
| `logHeaders` | `Boolean` | `false` | Log request/response headers |
| `logRequestBody` | `Boolean` | `false` | Log request body |
| `logResponseBody` | `Boolean` | `false` | Log response body |
| `injectTraceParent` | `Boolean` | `true` | Inject traceparent header |
| `maxBodySize` | `Int` | `1024` | Max body size to log |

## Usage

### Basic Usage

```kotlin
val client = HttpClient {
    install(KtorLogging)
}

// All requests are automatically logged
val response = client.get("https://api.example.com/users")
```

### With Custom Configuration

```kotlin
val client = HttpClient {
    install(KtorLogging) {
        logRequests = true
        logResponses = true
        logHeaders = true
        logRequestBody = true
        maxBodySize = 2048
    }
}
```

### Excluding Specific Routes

```kotlin
install(KtorLogging) {
    // Don't log health checks
    excludePaths("/health", "/ready")
}
```

### Sensitive Headers

By default, sensitive headers are not logged:

```kotlin
install(KtorLogging) {
    // Headers never logged
    sensitiveHeaders = setOf(
        "Authorization",
        "Cookie",
        "X-Api-Key"
    )
}
```

## Trace Context Propagation

### W3C Trace Context

The module implements the W3C Trace Context standard:

```
traceparent: 00-<trace-id>-<span-id>-<trace-flags>
tracestate: <vendor>=<value>
```

### Distributed Tracing

Trace context automatically propagates across service boundaries:

```
Service A → Service B → Service C
  trace-id: abc123
```

## Example Output

### Request

```
[INFO] HttpClient: GET https://api.example.com/users
  trace_id: abc123def456
  span_id: 789ghi012
```

### Response

```
[INFO] HttpClient: Response 200 OK (156ms)
  trace_id: abc123def456
  span_id: 789ghi012
  status: 200
  duration_ms: 156
```

### Error

```
[ERROR] HttpClient: POST https://api.example.com/users failed
  trace_id: abc123def456
  span_id: 789ghi012
  status: 500
  duration_ms: 234
  exception: IOException
  message: Connection refused
```

## Best Practices

1. **Enable in all environments** - Trace context is lightweight
2. **Exclude sensitive paths** - Don't log health checks or metrics
3. **Limit body logging** - Avoid large payloads in production
4. **Use with tracing module** - Combine with `verbatim-tracing` for full observability

## Next Steps

- [Tracing](tracing.md) - Add detailed performance metrics
- [Crashlytics](crashlytics.md) - Report errors to crash reporters
