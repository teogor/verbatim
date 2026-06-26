# Visual Themes

Verbatim provides a flexible visual formatting system that decouples log presentation from core log levels. Choose from 5 built-in themes or create your own custom theme.

## Built-in Themes

### Emojis (Default)

Playful theme using colored hearts:

```
💜 VERBOSE MyApp: Starting initialization
💚 DEBUG  MyApp: Loading configuration
💙 INFO   MyApp: Application started
💛 WARN   MyApp: Deprecated API usage
❤️  ERROR  MyApp: Network connection failed
💔 FATAL  MyApp: Out of memory
```

### Geometric

Minimalist theme using colored squares:

```
🟪 VERB MyApp: Starting initialization
🟩 DEBG MyApp: Loading configuration
🟦 INFO MyApp: Application started
🟨 WARN MyApp: Deprecated API usage
🟥 ERRR MyApp: Network connection failed
⬛ FTL  MyApp: Out of memory
```

### Minimalist

Clean theme for production:

```
[V] MyApp: Starting initialization
[D] MyApp: Loading configuration
[I] MyApp: Application started
[W] MyApp: Deprecated API usage
[E] MyApp: Network connection failed
[F] MyApp: Out of memory
```

### NerdFonts

Theme using powerline glyphs (requires Nerd Font):

```
 VRB MyApp: Starting initialization
 DBG MyApp: Loading configuration
 INF MyApp: Application started
 WRN MyApp: Deprecated API usage
 ERR MyApp: Network connection failed
 FTL MyApp: Out of memory
```

### AnsiColors

Terminal theme using ANSI escape codes:

```
\e[35m VERBOSE MyApp: Starting initialization
\e[32m DEBUG   MyApp: Loading configuration
\e[34m INFO    MyApp: Application started
\e[33m WARN    MyApp: Deprecated API usage
\e[31m ERROR   MyApp: Network connection failed
\e[1;31m FATAL   MyApp: Out of memory
```

## Quick Start

```kotlin
// Use a built-in theme
Verbatim.install(visuals = LogVisuals.Geometric)
```

## Custom Themes

### Using the DSL Builder

```kotlin
val customTheme = LogVisuals.custom {
    on(LogLevel.VERBOSE) { indicator = "🔍"; label = "VRB" }
    on(LogLevel.DEBUG)   { indicator = "🛠️"; label = "DBG" }
    on(LogLevel.INFO)    { indicator = "💡"; label = "INF" }
    on(LogLevel.WARN)    { indicator = "⚠️"; label = "WRN" }
    on(LogLevel.ERROR)   { indicator = "🚨"; label = "ERR" }
    on(LogLevel.FATAL)   { indicator = "💀"; label = "FTL" }
}

Verbatim.install(visuals = customTheme)
```

### Using Raw Provider

For complete control over the output format:

```kotlin
val rawTheme = LogVisuals.raw { level, tag ->
    val timestamp = Clock.System.now().formatTime()
    "$timestamp [${level.name}] $tag"
}

Verbatim.install(visuals = rawTheme)
```

## Adaptive Themes

Automatically select themes based on the platform:

```kotlin
// Use defaults for the current platform
val adaptive = LogVisuals.adaptive()

// Custom selection logic
val customAdaptive = LogVisuals.adaptive { platform ->
    when {
        platform.isTerminalAnsiCompliant -> LogVisuals.AnsiColors
        platform.isMobileIdeConsole -> LogVisuals.Emojis
        platform.isCiCdEnvironment -> LogVisuals.Minimalist
        else -> LogVisuals.Geometric
    }
}
```

### Platform Detection

The `PlatformInfo` class provides:

| Property | Description |
|----------|-------------|
| `isTerminalAnsiCompliant` | Terminal supports ANSI escape codes |
| `isMobileIdeConsole` | Running in Android Studio or Xcode |
| `isCiCdEnvironment` | Running in CI/CD (GitHub Actions, Jenkins, etc.) |
| `os` | Operating system name |

## Using with Formatters

Themes work with all formatters:

```kotlin
val sink = ConsoleSink(LogFormatters.pretty(LogVisuals.Geometric))
```

## Using with LoggerConfig

Set the theme in your configuration:

```kotlin
Verbatim.install(
    LoggerConfig.Builder()
        .visuals(LogVisuals.NerdFonts)
        .addSink(ConsoleSink(LogFormatters.pretty(LogVisuals.NerdFonts)))
        .build()
)
```

## Theme Comparison

| Theme | Indicator Style | Best For |
|-------|----------------|----------|
| Emojis | Colored hearts | Mobile apps, casual debugging |
| Geometric | Colored squares | Development, desktop apps |
| Minimalist | Brackets | Production, CI/CD |
| NerdFonts | Powerline glyphs | Terminal users with Nerd Fonts |
| AnsiColors | ANSI escape codes | Remote servers, SSH sessions |

## Next Steps

- [Core Concepts](core-concepts.md) - Learn about LogEvent, LogLevel, and the logging pipeline
- [Sinks & Formatters](sinks-and-formatters.md) - Route logs to multiple destinations
- [Getting Started](getting-started.md) - Installation and basic usage
