package dev.teogor.verbatim.shared.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LoggerConfig
import dev.teogor.verbatim.core.Platform
import dev.teogor.verbatim.core.sinks.RemoteLogSink
import dev.teogor.verbatim.core.sinks.TestLogSink
import dev.teogor.verbatim.core.visuals.LogVisuals
import dev.teogor.verbatim.shared.core.getPlatform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = MaterialTheme.colorScheme.primary,
            primaryContainer = MaterialTheme.colorScheme.primaryContainer,
        )
    ) {
        var currentScreen by remember { mutableStateOf(Screen.Simple) }

        Scaffold(
            contentWindowInsets = WindowInsets.systemBars,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Verbatim Demo - ${currentScreen.title}",
                            style = MaterialTheme.typography.titleMedium,
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
                )
            },
            bottomBar = {
                ScreenBottomBar(
                    currentScreen = currentScreen,
                    onScreenSelected = { currentScreen = it },
                )
            },
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
            ) {
                when (currentScreen) {
                    Screen.Simple -> SimpleScreen()
                    Screen.Structured -> StructuredScreen()
                    Screen.Coroutine -> CoroutineScreen()
                    Screen.VisualThemes -> VisualThemesScreen()
                    Screen.Platform -> PlatformScreen()
                    Screen.CrashConfig -> CrashConfigScreen()
                    Screen.Pipeline -> PipelineScreen()
                    Screen.Test -> TestScreen()
                }
            }
        }
    }
}

@Composable
private fun ScreenBottomBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .navigationBarsPadding()
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        // Row 1: first 4 screens
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Screen.entries.take(4).forEach { screen ->
                FilterChip(
                    selected = currentScreen == screen,
                    onClick = { onScreenSelected(screen) },
                    label = {
                        Text(
                            text = screen.shortTitle,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }

        // Row 2: last 4 screens
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Screen.entries.drop(4).forEach { screen ->
                FilterChip(
                    selected = currentScreen == screen,
                    onClick = { onScreenSelected(screen) },
                    label = {
                        Text(
                            text = screen.shortTitle,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }
    }
}

// ==================== Screen Implementations ====================

@Composable
private fun SimpleScreen() {
    val platform = remember { getPlatform() }
    var logOutput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Basic API Demo",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Demonstrates the core logging API with different log levels.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.VERBOSE, visuals = LogVisuals.Minimalist)
                val log = Verbatim.logger("Simple")
                log.verbose { "Verbose: detailed info for developers" }
                log.debug { "Debug: useful during debugging" }
                log.info { "Info: general operational message" }
                log.warn { "Warn: something might be wrong" }
                log.error { "Error: something failed" }
                logOutput = "Logged at VERBOSE level on ${platform.name}"
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Log All Levels (VERBOSE)")
        }

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.INFO, visuals = LogVisuals.Emojis)
                val log = Verbatim.logger("Simple")
                log.verbose { "This will NOT appear (below INFO)" }
                log.debug { "This will NOT appear (below INFO)" }
                log.info { "This WILL appear (meets INFO threshold)" }
                log.warn { "This WILL appear" }
                logOutput = "Filtered at INFO level - verbose/debug messages hidden"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text("Log with INFO Filter")
        }

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Emojis)
                val log = Verbatim.logger("Simple")
                log.warn { "Warning with no attributes" }
                log.error { "Error with no attributes" }
                logOutput = "Simple messages without attributes"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
        ) {
            Text("Log Without Attributes")
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

@Composable
private fun StructuredScreen() {
    var logOutput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Structured Logging",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Log events with structured key-value attributes for rich context.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Geometric)
                val log = Verbatim.logger("Auth")
                log.info(
                    attributes = {
                        attr("user_id", 42)
                        attr("action", "login")
                        attr("method", "oauth2")
                    }
                ) { "User authenticated" }
                logOutput = "Logged with auth attributes (user_id, action, method)"
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Log with Attributes")
        }

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Geometric)
                val log = Verbatim.logger("Network")
                log.warn(
                    attributes = {
                        attr("request_id", "req-001")
                        attr("latency_ms", 1523)
                        attr("endpoint", "/api/users")
                    }
                ) { "Slow request detected" }
                logOutput = "Warning with request attributes"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text("Log Warning with Attributes")
        }

        Button(
            onClick = {
                Verbatim.install(
                    config = LoggerConfig.Builder()
                        .minLevel(LogLevel.DEBUG)
                        .visuals(LogVisuals.Geometric)
                        .globalAttribute("app_version", "1.0.0")
                        .globalAttribute("build", "debug")
                        .build()
                )
                val log = Verbatim.logger("App")
                log.info(
                    attributes = {
                        attr("event", "startup")
                    }
                ) { "App started" }
                logOutput = "Global attributes (app_version, build) merged with local attributes"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
        ) {
            Text("Log with Global Attributes")
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

@Composable
private fun CoroutineScreen() {
    var logOutput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Coroutine Context",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Log events carry coroutine context automatically via LogContextElement.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Minimalist)
                val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
                scope.launch {
                    val log = Verbatim.logger("Coroutine")
                    log.info { "Inside coroutine scope" }
                    log.debug { "Coroutine context is active" }
                }
                logOutput = "Logged from coroutine scope"
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Log from Coroutine")
        }

        Button(
            onClick = {
                Verbatim.install(
                    config = LoggerConfig.Builder()
                        .minLevel(LogLevel.DEBUG)
                        .visuals(LogVisuals.Minimalist)
                        .globalAttribute("coroutine", "demo")
                        .build()
                )
                val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
                scope.launch {
                    val log = Verbatim.logger("Coroutine")
                    log.info(
                        attributes = {
                            attr("thread", Platform.currentThreadName())
                        }
                    ) { "Thread name in attributes" }
                }
                logOutput = "Coroutine with thread name attribute"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text("Coroutine with Thread Info")
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

@Composable
private fun VisualThemesScreen() {
    var selectedTheme by remember { mutableStateOf("Emojis") }
    var logOutput by remember { mutableStateOf("") }

    val themes = listOf(
        "Emojis" to LogVisuals.Emojis,
        "Geometric" to LogVisuals.Geometric,
        "Minimalist" to LogVisuals.Minimalist,
        "NerdFonts" to LogVisuals.NerdFonts,
        "AnsiColors" to LogVisuals.AnsiColors,
    )

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Visual Themes",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Choose a visual theme for log formatting. Each theme provides different level indicators.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Theme chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            themes.forEach { (name, _) ->
                FilterChip(
                    selected = selectedTheme == name,
                    onClick = {
                        selectedTheme = name
                        val visual = themes.first { it.first == name }.second
                        Verbatim.install(minLevel = LogLevel.DEBUG, visuals = visual)
                        val log = Verbatim.logger("Theme")
                        log.debug { "Debug message" }
                        log.info { "Info message" }
                        log.warn { "Warning message" }
                        log.error { "Error message" }
                        logOutput = "Theme: $name - logs generated"
                    },
                    label = { Text(name, style = MaterialTheme.typography.labelSmall) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                )
            }
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

@Composable
private fun PlatformScreen() {
    val platform = remember { getPlatform() }
    var logOutput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Platform Info",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Platform-specific utilities and environment access.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Platform: ${platform.name}", style = MaterialTheme.typography.bodyLarge)
                Text(
                    "Thread: ${Platform.currentThreadName()}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.AnsiColors)
                val log = Verbatim.logger("Platform")
                log.info(
                    attributes = {
                        attr("platform", platform.name)
                        attr("thread", Platform.currentThreadName())
                        attr("java_version", Platform.getProperty("java.version") ?: "N/A")
                    }
                ) { "Platform info logged" }
                logOutput = "Platform: ${platform.name}, Thread: ${Platform.currentThreadName()}"
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Log Platform Info")
        }

        Button(
            onClick = {
                val envVars = listOf("PATH", "HOME", "USER", "JAVA_HOME")
                val found = envVars.mapNotNull { name ->
                    Platform.getenv(name)?.let { "$name=$it" }
                }
                logOutput = if (found.isNotEmpty()) {
                    "Environment variables:\n${found.joinToString("\n")}"
                } else {
                    "No environment variables accessible (expected on web/native)"
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text("Read Environment Variables")
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

@Composable
private fun CrashConfigScreen() {
    var logOutput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Crash Config",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Configure crash severity and FATAL level behavior.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("CrashSeverity Levels:", style = MaterialTheme.typography.bodyLarge)
                Text("LOW - Non-critical, logged only", style = MaterialTheme.typography.bodyMedium)
                Text(
                    "MEDIUM - Important, may need attention",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    "HIGH - Critical, requires immediate attention",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text("FATAL - Application will crash", style = MaterialTheme.typography.bodyMedium)
            }
        }

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Emojis)
                val log = Verbatim.logger("Crash")

                try {
                    log.fatal { "System out of memory" }
                } catch (e: RuntimeException) {
                    logOutput = "FATAL threw RuntimeException: ${e.message}"
                }
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Trigger FATAL (throws RuntimeException)")
        }

        Button(
            onClick = {
                Verbatim.install(minLevel = LogLevel.DEBUG, visuals = LogVisuals.Emojis)
                val log = Verbatim.logger("Crash")
                log.error(
                    throwable = IllegalStateException("Disk full"),
                    attributes = {
                        attr("disk", "/dev/sda1")
                        attr("free_bytes", 0)
                    }
                ) { "Error with throwable attached" }
                logOutput = "Error logged with throwable and attributes"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text("Log Error with Throwable")
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

@Composable
private fun PipelineScreen() {
    var logOutput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Pipeline & Sinks",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Configure multiple sinks and custom formatters for different outputs.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val captured = mutableListOf<String>()
                Verbatim.install(
                    config = LoggerConfig.Builder()
                        .minLevel(LogLevel.DEBUG)
                        .addSink(
                            RemoteLogSink { formatted -> captured.add(formatted) }
                        )
                        .build()
                )
                val log = Verbatim.logger("Pipeline")
                log.info { "Message 1" }
                log.warn { "Message 2" }
                logOutput = "Captured ${captured.size} logs in RemoteLogSink:\n${
                    captured.joinToString("\n") { "  -> $it" }
                }"
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("RemoteLogSink Demo")
        }

        Button(
            onClick = {
                val testSink = TestLogSink()
                Verbatim.install(
                    config = LoggerConfig.Builder()
                        .minLevel(LogLevel.DEBUG)
                        .addSink(testSink)
                        .build()
                )
                val log = Verbatim.logger("Pipeline")
                log.info { "Test message 1" }
                log.warn { "Test message 2" }
                log.error { "Test message 3" }
                logOutput = "TestLogSink captured ${testSink.events.size} events:\n" +
                        "  WARN+ present: ${testSink.hasLevel(LogLevel.WARN)}\n" +
                        "  Last level: ${testSink.lastEvent()?.level}\n" +
                        "  Last message: ${testSink.lastEvent()?.message}"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text("TestLogSink Demo")
        }

        Button(
            onClick = {
                Verbatim.install(
                    LoggerConfig.Builder()
                        .minLevel(LogLevel.DEBUG)
                        .override("NetworkModule", LogLevel.VERBOSE)
                        .override("QuietModule", LogLevel.ERROR)
                        .build()
                )
                val netLog = Verbatim.logger("NetworkModule")
                val quietLog = Verbatim.logger("QuietModule")
                val appLog = Verbatim.logger("AppModule")

                netLog.debug { "Network debug (VERBOSE override)" }
                quietLog.debug { "Quiet debug (ERROR override - hidden)" }
                appLog.debug { "App debug (global DEBUG)" }
                appLog.warn {
                    "App warn (global DEBUG)"

                }
                logOutput = "Per-tag overrides: NetworkModule@VERBOSE, QuietModule@ERROR"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
            ),
        ) {
            Text("Per-Tag Level Overrides")
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

@Composable
private fun TestScreen() {
    val testSink = remember { TestLogSink() }
    var logOutput by remember { mutableStateOf("") }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Test Verification",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Capture logs in-memory and verify them in tests.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                Verbatim.install(
                    config = LoggerConfig.Builder()
                        .minLevel(LogLevel.DEBUG)
                        .addSink(testSink)
                        .build()
                )
                val log = Verbatim.logger("Test")
                log.info { "Info for verification" }
                log.warn { "Warning for verification" }
                log.error { "Error for verification" }
                logOutput = "Captured ${testSink.events.size} events"
            },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text("Generate Test Logs")
        }

        Button(
            onClick = {
                logOutput = buildString {
                    appendLine("=== TestLogSink Verification ===")
                    appendLine("Total events: ${testSink.events.size}")
                    appendLine("Has VERBOSE: ${testSink.hasLevel(LogLevel.VERBOSE)}")
                    appendLine("Has DEBUG: ${testSink.hasLevel(LogLevel.DEBUG)}")
                    appendLine("Has INFO: ${testSink.hasLevel(LogLevel.INFO)}")
                    appendLine("Has WARN: ${testSink.hasLevel(LogLevel.WARN)}")
                    appendLine("Has ERROR: ${testSink.hasLevel(LogLevel.ERROR)}")
                    appendLine("Last event level: ${testSink.lastEvent()?.level}")
                    appendLine("Last event message: ${testSink.lastEvent()?.message}")
                    appendLine("Events by level:")
                    LogLevel.entries.filter { it != LogLevel.OFF }.forEach { level ->
                        val count = testSink.events.count { it.level == level }
                        appendLine("  $level: $count")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
            ),
        ) {
            Text("Verify Captured Logs")
        }

        Button(
            onClick = {
                testSink.clear()
                logOutput = "TestLogSink cleared. Events: ${testSink.events.size}"
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
            ),
        ) {
            Text("Clear Captured Logs")
        }

        if (logOutput.isNotEmpty()) {
            LogOutputCard(logOutput)
        }
    }
}

// ==================== UI Components ====================

@Composable
private fun LogOutputCard(text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(12.dp),
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = FontFamily.Monospace,
                fontSize = 11.sp,
            ),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ==================== Screen Enum ====================

private enum class Screen(val title: String, val shortTitle: String) {
    Simple("Basic Logging", "Simple"),
    Structured("Structured", "Struct"),
    Coroutine("Coroutines", "Coroutine"),
    VisualThemes("Themes", "Theme"),
    Platform("Platform", "Platform"),
    CrashConfig("Crash Config", "Crash"),
    Pipeline("Pipeline", "Pipeline"),
    Test("Test Verify", "Test"),
}
