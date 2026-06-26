# Contributing to Verbatim

Thank you for your interest in contributing to Verbatim! This document provides guidelines and information for contributors.

## Getting Started

### Prerequisites

- JDK 17 or higher
- Android Studio latest stable
- Kotlin Multiplatform plugin

### Building the Project

```bash
# Clone the repository
git clone https://github.com/teogor/verbatim.git
cd verbatim

# Build all modules
./gradlew build

# Run tests
./gradlew test
```

## Project Structure

```
verbatim/
├── verbatim-core/                # Core logging pipeline
├── verbatim-persistence/         # Okio file logging
├── verbatim-middleware/          # PII masking
├── verbatim-tracing/             # Performance metrics
├── verbatim-ui/                  # Compose log viewer
├── verbatim-compiler-plugin/     # IR stripping
├── verbatim-ktor/                # HTTP client tracking
├── verbatim-crashlytics/         # Crash reporting abstraction
├── app/                          # Sample application
├── build-logic/                  # Convention plugins
└── docs/                         # Documentation
```

## Development Workflow

### 1. Fork and Clone

```bash
# Fork on GitHub, then clone
git clone https://github.com/YOUR_USERNAME/verbatim.git
cd verbatim

# Add upstream remote
git remote add upstream https://github.com/teogor/verbatim.git
```

### 2. Create a Branch

```bash
# Create feature branch
git checkout -b feature/your-feature-name

# Or bug fix branch
git checkout -b fix/your-bug-description
```

### 3. Make Changes

- Follow existing code style
- Add tests for new functionality
- Update documentation if needed

### 4. Test Your Changes

```bash
# Run all tests
./gradlew test

# Run specific module tests
./gradlew :verbatim-core:test

# Run Android instrumented tests
./gradlew :app:androidApp:connectedAndroidTest
```

### 5. Commit Your Changes

```bash
# Stage changes
git add .

# Commit with descriptive message
git commit -m "feat: add new logging feature

- Added Feature X
- Updated documentation
- Added tests for new functionality"
```

### 6. Push and Create PR

```bash
# Push to your fork
git push origin feature/your-feature-name

# Create Pull Request on GitHub
```

## Code Style

### Kotlin

- Follow [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful variable and function names
- Add KDoc comments for public APIs

### Commit Messages

Follow [Conventional Commits](https://www.conventionalcommits.org/):

- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation changes
- `style:` - Code style changes
- `refactor:` - Code refactoring
- `test:` - Adding tests
- `chore:` - Maintenance tasks

### Example

```kotlin
/**
 * A sink that forwards log events to a crash reporting engine.
 *
 * This sink bridges Verbatim logs to any crash reporter implementation.
 *
 * @see CrashReportEngine
 */
class CrashSink : LogSink {
    // Implementation
}
```

## Pull Request Guidelines

### Before Submitting

- [ ] Code compiles without errors
- [ ] All tests pass
- [ ] Documentation is updated
- [ ] Code follows project style
- [ ] Commit messages are clear

### PR Description

Include:

1. **What** - Summary of changes
2. **Why** - Motivation for changes
3. **How** - Implementation approach
4. **Testing** - How to test changes

### Example PR

```markdown
## What
Added structured logging support with AttrBuilder DSL.

## Why
Users need to attach typed metadata to log events for better observability.

## How
- Added `attrs` parameter to Logger methods
- Implemented `AttrBuilder` class
- Added tests for attribute serialization

## Testing
- Unit tests for AttrBuilder
- Integration tests with LoggerFactory
- Manual testing on Android and iOS
```

## Reporting Issues

### Bug Reports

Include:

- Device/OS information
- Verbatim version
- Steps to reproduce
- Expected vs actual behavior
- Code sample if applicable

### Feature Requests

Include:

- Use case description
- Proposed API (if applicable)
- Alternatives considered

## Code of Conduct

Please read and follow our [Code of Conduct](CODE_OF_CONDUCT.md).

## License

By contributing to Verbatim, you agree that your contributions will be licensed under the [Apache License 2.0](LICENSE).

## Questions?

- Open an issue for bugs/features
- Start a discussion for questions
- Email: open-source@teogor.dev
