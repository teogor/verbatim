# Compiler Plugin

The `verbatim-compiler-plugin` module provides a Kotlin Compiler IR transformer that permanently purges targeted logging bytecode allocations from production binaries during compilation.

## Overview

While standard runtime filters stop logs from executing, string constants and debugging traces often remain in compiled binaries. The Verbatim Compiler Plugin removes these call sites entirely, reducing binary size and eliminating reverse-engineering risks.

## Installation

```kotlin title="app/build.gradle.kts"
plugins {
    id("dev.teogor.verbatim.compiler") version "1.0.0-alpha01"
}
```

## Configuration

```kotlin title="app/build.gradle.kts"
verbatimCompiler {
    // Completely delete matching call sites from compiled binary
    stripLevelsOnRelease.set(listOf("TRACE", "VERBOSE", "DEBUG"))
}
```

## Features

### Level-Based Stripping

Remove logs by severity level:

```kotlin
verbatimCompiler {
    stripLevelsOnRelease.set(listOf(
        "VERBOSE",  // Remove all verbose logs
        "DEBUG",    // Remove all debug logs
        "TRACE"     // Remove all trace logs
    ))
}
```

### Tag-Based Stripping

Remove logs from specific components:

```kotlin
verbatimCompiler {
    stripTagsOnRelease.set(listOf(
        "NetworkModule",
        "ThirdPartySDK",
        "DebugUtils"
    ))
}
```

### Pattern-Based Stripping

Remove logs matching specific patterns:

```kotlin
verbatimCompiler {
    stripPatternsOnRelease.set(listOf(
        Regex(".*TODO.*"),
        Regex(".*FIXME.*"),
        Regex(".*HACK.*")
    ))
}
```

## How It Works

### Compilation Process

```
Source Code → Kotlin Compiler → IR Transform → Optimized Binary
                              ↑
                    Verbatim Plugin
                    (strips log calls)
```

### Before Stripping

```kotlin
class UserService {
    fun login(username: String) {
        logger.debug { "Attempting login for $username" }
        logger.verbose { "Password hash: ${hash(password)}" }
        // ... actual login logic
    }
}
```

### After Stripping

```kotlin
class UserService {
    fun login(username: String) {
        // ... actual login logic (log calls removed)
    }
}
```

## Build Variants

### Debug Builds

Keep all logs for development:

```kotlin
if (project.hasProperty("release")) {
    verbatimCompiler {
        stripLevelsOnRelease.set(listOf("VERBOSE", "DEBUG"))
    }
}
```

### Release Builds

Strip debug logs in production:

```kotlin
android {
    buildTypes {
        release {
            // Plugin automatically strips configured levels
        }
        debug {
            // Plugin is inactive
        }
    }
}
```

## Stripping Levels

| Level | When to Strip |
|-------|---------------|
| `VERBOSE` | Almost always safe to strip |
| `DEBUG` | Safe for production |
| `INFO` | Keep for production monitoring |
| `WARN` | Keep for production warnings |
| `ERROR` | Never strip |
| `ASSERT` | Never strip |

## Performance Impact

### Binary Size

Removing log calls reduces binary size:

| Log Type | Approximate Size |
|----------|------------------|
| Debug log with interpolation | 50-200 bytes |
| Verbose log with formatting | 100-500 bytes |
| Multiple logs per class | 1-5 KB savings |

### Runtime Performance

No runtime impact - stripping happens at compile time.

## Best Practices

1. **Strip VERBOSE and DEBUG** - Safe for all production builds
2. **Keep INFO and above** - Essential for production monitoring
3. **Test stripping** - Verify app works correctly after stripping
4. **Use with Crashlytics** - Strip debug logs, keep error logs for crash reporters

## Troubleshooting

### Logs Not Appearing in Release

Verify the plugin is configured correctly:

```kotlin
verbatimCompiler {
    stripLevelsOnRelease.set(listOf("VERBOSE", "DEBUG"))
    // INFO, WARN, ERROR, ASSERT will still appear
}
```

### Build Failures

Ensure the plugin is applied to the correct module:

```kotlin
// Apply to app module, not library modules
plugins {
    id("dev.teogor.verbatim.compiler") version "1.0.0-alpha01"
}
```

## Next Steps

- [Testing](../testing.md) - Verify stripping behavior
- [Crashlytics](crashlytics.md) - Combine with crash reporting
