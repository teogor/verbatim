# Middleware

The `verbatim-middleware` module provides PII (Personally Identifiable Information) scrubbing, regex compliance patterns, and annotation-driven metadata masking.

## Overview

This module ensures sensitive data is automatically masked before logs reach secondary storage or external analytics tools. Use annotations on data classes or configure regex patterns for flexible PII detection.

## Installation

```kotlin
dependencies {
    implementation("dev.teogor.verbatim:verbatim-middleware:1.0.0-alpha01")
}
```

## Configuration

```kotlin
import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.middleware.PiiMasking

Verbatim.initialize {
    install(PiiMasking) {
        // Custom regex patterns
        maskRegex(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}")) // Emails

        // Key-based masking
        maskKey("password")
        maskKey("access_token")
        maskKey("cvv")
        maskKey("ssn")
    }
}
```

## Features

### Annotation-Driven Masking

Annotate data classes to automatically mask sensitive fields:

```kotlin
import dev.teogor.verbatim.middleware.annotations.Mask
import dev.teogor.verbatim.middleware.annotations.Redact
import dev.teogor.verbatim.middleware.annotations.SensitiveData

@SensitiveData
data class PaymentDetails(
    val referenceId: String,
    @Mask(replacement = "••••-••••-••••") val billingCard: String,
    @Redact val routingPin: String
)
```

### Annotations

| Annotation | Purpose | Example |
|------------|---------|---------|
| `@SensitiveData` | Mark entire class as containing sensitive data | Data classes with PII |
| `@Mask` | Replace field with custom mask | `@Mask(replacement = "***")` |
| `@Redact` | Completely remove field from output | `@Redact val password: String` |

### Usage

```kotlin
val transaction = PaymentDetails(
    referenceId = "ref_00192",
    billingCard = "4111222233334444",
    routingPin = "9912"
)

logger.info { "Processing: $transaction" }
// Output: Processing: PaymentDetails(referenceId=ref_00192, billingCard=••••-••••-••••, routingPin=[REDACTED])
```

### Regex Patterns

Configure custom patterns for flexible PII detection:

```kotlin
install(PiiMasking) {
    // Email addresses
    maskRegex(Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}"))

    // Phone numbers
    maskRegex(Regex("\\+?\\d{1,4}?[-.\\s]?\\(?\\d{1,3}?\\)?[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,4}[-.\\s]?\\d{1,9}"))

    // Credit card numbers
    maskRegex(Regex("\\d{4}[-\\s]?\\d{4}[-\\s]?\\d{4}[-\\s]?\\d{4}"))

    // Social Security Numbers
    maskRegex(Regex("\\d{3}-\\d{2}-\\d{4}"))
}
```

### Key-Based Masking

Mask specific keys in structured attributes:

```kotlin
install(PiiMasking) {
    maskKey("password")
    maskKey("access_token")
    maskKey("secret")
    maskKey("authorization")
}

logger.info(
    attrs = mapOf(
        "userId" to 123,
        "password" to "hunter2",  // Will be masked
        "action" to "login"
    )
) { "User login attempt" }
// Output: User login attempt | attrs={userId=123, password=***, action=login}
```

## Built-In Patterns

| Pattern | Regex | Description |
|---------|-------|-------------|
| Email | `[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}` | Email addresses |
| Phone | `\+?\d{1,4}?[-.\s]?\(?\d{1,3}?\)?[-.\s]?\d{1,4}[-.\s]?\d{1,4}[-.\s]?\d{1,9}` | Phone numbers |
| Credit Card | `\d{4}[-\s]?\d{4}[-\s]?\d{4}[-\s]?\d{4}` | Credit card numbers |
| SSN | `\d{3}-\d{2}-\d{4}` | Social Security Numbers |
| IP Address | `\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}` | IPv4 addresses |

## Best Practices

1. **Use annotations** - Mark sensitive data classes with `@SensitiveData`
2. **Configure patterns** - Add regex patterns for your specific PII types
3. **Test masking** - Verify sensitive data is properly masked
4. **Layer defenses** - Combine annotation and pattern-based masking

## Next Steps

- [Persistence](persistence.md) - Store masked logs securely
- [Testing](../testing.md) - Verify masking behavior in tests
