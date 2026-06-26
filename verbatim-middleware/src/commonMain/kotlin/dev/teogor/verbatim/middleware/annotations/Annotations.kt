package dev.teogor.verbatim.middleware.annotations

@Target(AnnotationTarget.CLASS)
annotation class SensitiveData

@Target(AnnotationTarget.PROPERTY)
annotation class Mask(val replacement: String = "••••")

@Target(AnnotationTarget.PROPERTY)
annotation class Redact
