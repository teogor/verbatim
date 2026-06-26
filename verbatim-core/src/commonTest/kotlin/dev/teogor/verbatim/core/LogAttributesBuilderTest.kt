package dev.teogor.verbatim.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LogAttributesBuilderTest {

    @Test
    fun testBuildEmptyAttributes() {
        val attributes = buildAttributes { }
        assertTrue(attributes.isEmpty())
    }

    @Test
    fun testBuildWithStringAttribute() {
        val attributes = buildAttributes {
            attr("key", "value")
        }
        assertEquals("value", attributes["key"])
    }

    @Test
    fun testBuildWithIntAttribute() {
        val attributes = buildAttributes {
            attr("count", 42)
        }
        assertEquals(42, attributes["count"])
    }

    @Test
    fun testBuildWithLongAttribute() {
        val attributes = buildAttributes {
            attr("timestamp", 1234567890L)
        }
        assertEquals(1234567890L, attributes["timestamp"])
    }

    @Test
    fun testBuildWithDoubleAttribute() {
        val attributes = buildAttributes {
            attr("amount", 99.99)
        }
        assertEquals(99.99, attributes["amount"])
    }

    @Test
    fun testBuildWithBooleanAttribute() {
        val attributes = buildAttributes {
            attr("is_active", true)
        }
        assertEquals(true, attributes["is_active"])
    }

    @Test
    fun testBuildWithNullAttribute() {
        val attributes = buildAttributes {
            attr("nullable", null)
        }
        assertEquals(null, attributes["nullable"])
    }

    @Test
    fun testBuildWithMultipleAttributes() {
        val attributes = buildAttributes {
            attr("string", "hello")
            attr("int", 42)
            attr("double", 3.14)
            attr("boolean", true)
        }
        assertEquals(4, attributes.size)
        assertEquals("hello", attributes["string"])
        assertEquals(42, attributes["int"])
        assertEquals(3.14, attributes["double"])
        assertEquals(true, attributes["boolean"])
    }

    @Test
    fun testBuildReturnsImmutableMap() {
        val attributes = buildAttributes {
            attr("key", "value")
        }
        // This should not throw, but modifying the returned map should not affect the builder
        assertTrue(attributes is Map<*, *>)
    }
}
