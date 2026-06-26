# UI

The `verbatim-ui` module provides a Compose Multiplatform visual on-device diagnostic console for real-time log viewing.

## Overview

This module enables in-app log viewing during development and QA testing, without requiring a desktop workstation or physical debug cable. Filter, search, and share logs directly from the device.

## Installation

```kotlin
dependencies {
    implementation("dev.teogor.verbatim:verbatim-ui:1.0.0-alpha01")
}
```

## Configuration

```kotlin
import dev.teogor.verbatim.ui.VerbatimLogOverlay
import dev.teogor.verbatim.ui.VerbatimTheme

@Composable
fun AppContentHost() {
    Box(modifier = Modifier.fillMaxSize()) {
        ApplicationNavigationGraph()

        VerbatimLogOverlay(
            enabled = BuildConfig.IS_QA_BUILD,
            theme = VerbatimTheme.Dark,
            maxInMemoryLines = 1500,
            onShareRequested = { archiveFile ->
                // Handle file sharing
            }
        )
    }
}
```

## Features

### Log Overlay

Display a searchable, filterable bottom sheet terminal:

```kotlin
VerbatimLogOverlay(
    enabled = true,                    // Enable/disable overlay
    theme = VerbatimTheme.Dark,        // Light or Dark theme
    maxInMemoryLines = 1500,           // Max lines in memory
    onShareRequested = { file ->       // Share callback
        shareLogArchive(file)
    }
)
```

### Search & Filter

The overlay provides built-in search and filter capabilities:

- **Text search** - Search log messages
- **Level filter** - Filter by log level (DEBUG, INFO, WARN, ERROR)
- **Tag filter** - Filter by logger tag
- **Clear** - Clear current logs

### Log Levels

Visual indicators for different log levels:

| Level | Color | Icon |
|-------|-------|------|
| VERBOSE | Gray | 💜 |
| DEBUG | Green | 💚 |
| INFO | Blue | 💙 |
| WARN | Yellow | 💛 |
| ERROR | Red | ❤️ |
| ASSERT | Dark Red | 💔 |

### Export

Share log archives for debugging:

```kotlin
VerbatimLogOverlay(
    onShareRequested = { archiveFile ->
        // archiveFile is a compressed log archive
        val uri = FileProvider.getUriForFile(context, authority, archiveFile)
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/zip"
            putExtra(Intent.EXTRA_STREAM, uri)
        }
        startActivity(Intent.createChooser(shareIntent, "Share Logs"))
    }
)
```

## Theming

### VerbatimTheme

```kotlin
VerbatimTheme.Dark   // Dark theme (default)
VerbatimTheme.Light  // Light theme
```

### Custom Theme

```kotlin
VerbatimLogOverlay(
    theme = VerbatimTheme.Dark.copy(
        background = Color.Black,
        text = Color.White,
        error = Color.Red
    )
)
```

## Build Variants

Configure the overlay for different build types:

```kotlin
// Debug builds - always enabled
VerbatimLogOverlay(
    enabled = true
)

// QA builds - enabled for QA
VerbatimLogOverlay(
    enabled = BuildConfig.IS_QA_BUILD
)

// Release builds - disabled
VerbatimLogOverlay(
    enabled = false
)
```

## Performance

The overlay is designed for minimal performance impact:

- **Lazy rendering** - Only visible logs are rendered
- **Memory limiting** - Configurable max lines prevent memory issues
- **Background collection** - Log collection happens off the main thread
- **Efficient scrolling** - Uses Compose lazy lists for smooth scrolling

## Platform Support

| Platform | Support |
|----------|---------|
| Android | Full support |
| iOS | Full support |
| Desktop | Full support |
| Web | Limited (no overlay, logs in console) |

## Best Practices

1. **Disable in production** - Only enable for debug/QA builds
2. **Limit memory** - Set appropriate `maxInMemoryLines`
3. **Use filtering** - Encourage users to filter for specific issues
4. **Provide share** - Enable log sharing for bug reports

## Next Steps

- [Tracing](tracing.md) - Add performance metrics to logs
- [Testing](../testing.md) - Test UI behavior
