package dev.teogor.verbatim.server

import dev.teogor.verbatim.Verbatim
import dev.teogor.verbatim.core.LogLevel
import dev.teogor.verbatim.core.LoggerConfig
import dev.teogor.verbatim.shared.core.getPlatform
import kotlinx.coroutines.runBlocking

fun main(): Unit = runBlocking {
    val platform = getPlatform()
    println("=== Verbatim Server App ===")
    println("Platform: ${platform.name}")
    println()

    // Install with structured logging and global attributes
    Verbatim.install(
        config = LoggerConfig.Builder()
            .minLevel(LogLevel.DEBUG)
            .globalAttribute("app_version", "1.0.0-alpha01")
            .globalAttribute("environment", "development")
            .globalAttribute("region", "eu-west-1")
            .build()
    )

    val logger = Verbatim.logger("Server")

    // Server startup
    logger.info { "Initializing server..." }
    logger.debug { "Loading configuration from application.conf" }
    logger.info { "Configuration loaded" }

    // Binding
    logger.info(
        attributes = {
            attr("port", 8080)
            attr("host", "0.0.0.0")
            attr("protocol", "HTTP/2")
        }
    ) { "Binding to port 8080" }

    logger.info { "Server started on http://0.0.0.0:8080" }

    // Simulate request processing
    val requests = listOf(
        Triple("GET", "/api/users", 200),
        Triple("POST", "/api/users", 201),
        Triple("GET", "/api/users/42", 200),
        Triple("DELETE", "/api/users/42", 204),
        Triple("GET", "/api/orders", 500),
    )

    for ((method, path, status) in requests) {
        val requestId = "req-${kotlin.random.Random.nextLong(10000, 99999)}"
        val latency = (10..2000).random()

        logger.info(
            attributes = {
                attr("request_id", requestId)
                attr("method", method)
                attr("path", path)
                attr("status", status)
                attr("latency_ms", latency)
            }
        ) { "Processing $method $path -> $status" }

        if (status >= 500) {
            logger.error(
                throwable = RuntimeException("Internal server error"),
                attributes = {
                    attr("request_id", requestId)
                    attr("method", method)
                    attr("path", path)
                }
            ) { "Request failed with $status" }
        } else if (latency > 1000) {
            logger.warn(
                attributes = {
                    attr("request_id", requestId)
                    attr("latency_ms", latency)
                    attr("threshold_ms", 1000)
                }
            ) { "Slow request detected" }
        }
    }

    // Database operations
    logger.debug { "Opening database connection pool" }
    logger.info(
        attributes = {
            attr("pool_size", 10)
            attr("host", "db.example.com")
            attr("port", 5432)
        }
    ) { "Database pool initialized" }

    // Shutdown
    logger.info { "Shutting down server..." }
    logger.debug { "Closing database connections" }
    logger.debug { "Removing signal handlers" }
    logger.info { "Server stopped" }

    println()
    println("=== Done ===")
}
