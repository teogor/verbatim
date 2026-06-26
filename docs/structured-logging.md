# Structured Logging

Structured logging attaches typed key-value metadata to log events, making them machine-readable and searchable in downstream aggregators.

## Why Structured Logging?

```kotlin
// Unstructured - hard to parse programmatically
logger.info { "User 123 logged in from mobile" }

// Structured - searchable by userId, device, action
logger.info(
    attributes = {
        attr("userId", 123)
        attr("device", "mobile")
        attr("action", "login")
    }
) { "User logged in" }
```

## LogAttributesBuilder DSL

Use the `attributes` parameter to attach structured metadata:

```kotlin
logger.info(
    attributes = {
        attr("method", "POST")
        attr("path", "/api/users")
        attr("statusCode", 201)
        attr("duration", 234)
    }
) { "HTTP request completed" }
```

### Supported Types

Attributes support any `Any?` value:

```kotlin
logger.info(
    attributes = {
        attr("string", "hello")
        attr("int", 42)
        attr("double", 3.14)
        attr("boolean", true)
        attr("list", listOf(1, 2, 3))
        attr("nullable", null)
    }
) { "Various types" }
```

## Common Patterns

### HTTP Request Logging

```kotlin
logger.info(
    attributes = {
        attr("method", "POST")
        attr("path", "/api/orders")
        attr("statusCode", 201)
        attr("duration_ms", 156)
        attr("ip", "192.168.1.1")
    }
) { "HTTP request" }
```

### Database Query Logging

```kotlin
logger.debug(
    attributes = {
        attr("query", "SELECT * FROM users WHERE id = ?")
        attr("params", listOf(123))
        attr("execution_time_ms", 45)
        attr("rows_affected", 1)
    }
) { "Query executed" }
```

### Business Event Logging

```kotlin
logger.info(
    attributes = {
        attr("event", "order_created")
        attr("orderId", "ORD-001")
        attr("userId", 789)
        attr("total", 99.99)
        attr("items", 3)
    }
) { "Order created successfully" }
```

### Exception Logging with Context

```kotlin
try {
    processPayment(order)
} catch (e: PaymentException) {
    logger.error(
        throwable = e,
        attributes = {
            attr("orderId", order.id)
            attr("amount", order.total)
            attr("paymentMethod", order.paymentMethod)
        }
    ) { "Payment processing failed" }
}
```

## Output Format

Structured attributes appear in the log output based on the configured formatter:

=== "Default"

    ```
    [INFO] PaymentService: Payment accepted
      Attributes: {amount=99.99, currency=USD}
    ```

=== "JSON"

    ```json
    {
      "level": "INFO",
      "logger": "PaymentService",
      "message": "Payment accepted",
      "attributes": {
        "amount": 99.99,
        "currency": "USD"
      }
    }
    ```

=== "Pretty"

    ```
    [INFO] 2024-01-15T10:30:00Z [main] PaymentService: Payment accepted
      ↳ Exception: Something went wrong
      Attributes: {amount=99.99, currency=USD}
      Context: {request_id=req-123}
    ```

## Best Practices

1. **Use consistent key names** - Choose a naming convention (snake_case or camelCase) and stick to it
2. **Keep attributes relevant** - Only attach data that aids debugging or monitoring
3. **Avoid sensitive data** - Never log passwords, tokens, or PII (use `verbatim-middleware` for masking)
4. **Use semantic keys** - `duration_ms`, `statusCode`, `userId` are more useful than `val1`, `val2`

## Next Steps

- [Context Propagation](context-propagation.md) - Automatically attach context to all logs
- [Middleware](modules/middleware.md) - Mask sensitive data with annotations
