# Context Propagation

Context propagation allows you to attach common fields to all logs within a scope, eliminating repetitive attribute passing.

## Why Context Propagation?

```kotlin
// Without context - repeat requestId everywhere
logger.info(attributes = { attr("requestId", "req-123") }) { "Starting request" }
logger.info(attributes = { attr("requestId", "req-123") }) { "Processing data" }
logger.info(attributes = { attr("requestId", "req-123") }) { "Request complete" }

// With context - set once, inherited automatically
withLogContext("requestId" to "req-123") {
    logger.info { "Starting request" }
    logger.info { "Processing data" }
    logger.info { "Request complete" }
}
```

## Basic Usage

### withLogContext

Use `withLogContext` to scope context in coroutines:

```kotlin
val context = LogContext(
    mapOf(
        "requestId" to "req-123",
        "userId" to 456
    )
)

withLogContext(context) {
    logger.info { "Processing request" }
    logger.debug { "Validating input" }
    logger.info { "Request completed" }
}
```

All logs within the block automatically include the context values.

### Convenience Overload

For quick context creation:

```kotlin
withLogContext(
    "requestId" to "req-123",
    "userId" to 456
) {
    logger.info { "Processing request" }
}
```

### Nested Context

Contexts merge automatically. Inner keys override outer keys on collision:

```kotlin
val traceContext = LogContext(mapOf("traceId" to "trace-123"))
val spanContext = LogContext(mapOf("spanId" to "span-456"))

withLogContext(traceContext) {
    logger.info { "Outer scope" }  // has traceId

    withLogContext(spanContext) {
        logger.info { "Inner scope" }  // has traceId + spanId
    }

    logger.info { "Back to outer" }  // has traceId only
}
```

### Return Values

`withLogContext` propagates the block's return value:

```kotlin
val result = withLogContext(context) {
    processRequest() // return value is propagated
}
```

## Coroutine Support

### The Problem

Without coroutine support, context is stored in a `ThreadLocal`. When a coroutine suspends and resumes on a different thread, the context is lost.

### Solution: withLogContext

The coroutine-aware API solves this on all platforms:

```kotlin
import dev.teogor.verbatim.coroutines.withLogContext

suspend fun handleRequest(requestId: String) {
    withLogContext("requestId" to requestId) {
        logger.info { "Starting request" }

        withContext(Dispatchers.IO) {       // thread hop - context still present
            delay(100)                      // suspension - context still present
            logger.debug { "Fetching data" }
        }

        logger.info { "Request complete" }
    }
}
```

### Platform Behavior

| Platform | Mechanism |
|----------|-----------|
| JVM / Android | `LogContextElement` implements `ThreadContextElement` |
| iOS / macOS | Single-threaded, context set directly |
| JS / Wasm | Single-threaded, same as Apple |

### Attaching to CoroutineScope

Attach a fixed context to an entire scope:

```kotlin
import dev.teogor.verbatim.coroutines.LogContextElement
import dev.teogor.verbatim.core.LogContext

val scope = CoroutineScope(
    Dispatchers.IO + LogContextElement(
        LogContext(mapOf("service" to "payment-api"))
    )
)

scope.launch {
    logger.info { "All coroutines in this scope carry service=payment-api" }
}
```

### Accessing Active Context

Read the current context from within a `withLogContext` block:

```kotlin
withLogContext("requestId" to "req-1") {
    val element = currentCoroutineContext()[LogContextElement]
    println(element?.context)  // LogContext({requestId=req-1})
}
```

## Best Practices

1. **Use semantic keys** - `requestId`, `traceId`, `userId` are more useful than `ctx1`
2. **Keep context lightweight** - Only include IDs and minimal metadata
3. **Use withLogContext in coroutines** - Always use the coroutine-safe version for multi-threaded dispatchers
4. **Nest thoughtfully** - Inner context overrides outer, so be aware of key collisions

## Next Steps

- [Sinks & Formatters](sinks-and-formatters.md) - Route logs to multiple destinations
- [Testing](testing.md) - Verify context propagation in tests
