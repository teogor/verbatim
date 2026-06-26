# Testing

Verbatim provides built-in testing utilities to verify logging behavior in unit tests.

## TestLogSink

`TestLogSink` captures log events for assertion:

```kotlin
@Test
fun logErrorWhenOperationFails() {
    val testSink = TestLogSink()
    Verbatim.install(
        LoggerConfig.Builder()
            .minLevel(LogLevel.DEBUG)
            .addSink(testSink)
            .build()
    )
    val logger = Verbatim.logger("MyClass")

    logger.error(
        attributes = {
            attr("operation", "save")
        }
    ) { "Failed to save data" }

    assertEquals(1, testSink.events.size)
    val event = testSink.events[0]
    assertEquals(LogLevel.ERROR, event.level)
    assertEquals("Failed to save data", event.message)
    assertEquals("MyClass", event.loggerName)
    assertEquals("save", event.attributes["operation"])
}
```

## Testing Context Propagation

Verify that context is correctly attached to log events:

```kotlin
@Test
fun propagatesContextToNestedLogs() {
    val testSink = TestLogSink()
    Verbatim.install(
        LoggerConfig.Builder()
            .minLevel(LogLevel.DEBUG)
            .addSink(testSink)
            .build()
    )
    val logger = Verbatim.logger("ContextTest")

    withLogContext(LogContext(mapOf("requestId" to "req-123"))) {
        logger.info { "Log 1" }
        logger.info { "Log 2" }
    }

    testSink.events.forEach { event ->
        assertEquals("req-123", event.context["requestId"])
    }
}
```

## Testing Coroutine Context

Verify context survives suspension points:

```kotlin
@Test
fun coroutineContextSurvivesSuspension() = runTest {
    val testSink = TestLogSink()
    Verbatim.install(
        LoggerConfig.Builder()
            .minLevel(LogLevel.DEBUG)
            .addSink(testSink)
            .build()
    )
    val logger = Verbatim.logger("CoroutineTest")

    withLogContext("requestId" to "req-123") {
        delay(10)
        logger.info { "After delay" }
    }

    assertEquals("req-123", testSink.events[0].context["requestId"])
}
```

## TestLogSink API

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `events` | `List<LogEvent>` | All captured events |
| `size` | `Int` | Number of captured events |

### Methods

| Method | Description |
|--------|-------------|
| `clear()` | Remove all captured events |
| `hasLevel(level)` | Check if any event has the specified level |
| `messagesWithLevel(level)` | Get all messages with the specified level |
| `lastEvent()` | Get the last captured event |
| `lastMessage()` | Get the message of the last captured event |

## Example: Testing a ViewModel

```kotlin
class LoginViewModel(private val logger: Logger) {

    suspend fun login(email: String, password: String): Boolean {
        logger.info(
            attributes = {
                attr("email", email)
            }
        ) { "Login attempt" }

        return try {
            authenticate(email, password)
            logger.info { "Login successful" }
            true
        } catch (e: Exception) {
            logger.error(throwable = e) { "Login failed" }
            false
        }
    }
}

class LoginViewModelTest {

    @Test
    fun loginSuccess() = runTest {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .addSink(testSink)
                .build()
        )

        val logger = Verbatim.logger("LoginViewModel")
        val viewModel = LoginViewModel(logger)

        val result = viewModel.login("user@example.com", "password")

        assertTrue(result)
        assertEquals(2, testSink.events.size)
        assertEquals(LogLevel.INFO, testSink.events[0].level)
        assertEquals("Login successful", testSink.events[1].message)
    }

    @Test
    fun loginFailure() = runTest {
        val testSink = TestLogSink()
        Verbatim.install(
            LoggerConfig.Builder()
                .minLevel(LogLevel.DEBUG)
                .addSink(testSink)
                .build()
        )

        val logger = Verbatim.logger("LoginViewModel")
        val viewModel = LoginViewModel(logger)

        val result = viewModel.login("user@example.com", "wrong")

        assertFalse(result)
        assertEquals(2, testSink.events.size)
        assertEquals(LogLevel.ERROR, testSink.events[1].level)
    }
}
```

## Best Practices

1. **Reset state between tests** - Create fresh `TestLogSink` instances
2. **Test all log levels** - Verify filtering behavior
3. **Test context propagation** - Ensure context flows correctly
4. **Test error cases** - Verify exception logging
