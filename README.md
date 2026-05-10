# DiskMan — Vinyl Record Collection Manager

## What is Diskman?

Diskman is a collection management program focused on vinyl records, designed to help collectors keep track of their stock in a simple and centralized way. It handles incoming and outgoing records, purchases, sales, and users.

## Technologies Used

- **Kotlin** — Primary programming language
- **Kotlin Multiplatform (KMP)** — Project structure targeting Android and Desktop (JVM)
- **Compose Multiplatform** — Declarative UI framework shared across platforms
- **Gradle (KTS)** — Build system with Version Catalogs

---

## Project Structure

```
Diskman/
├── composeApp/
│   └── src/
│       ├── commonMain/         # Shared code across all targets
│       │   └── kotlin/
│       ├── androidMain/        # Android-specific code
│       │   └── kotlin/
│       └── jvmMain/            # Desktop (JVM)-specific code
│           └── kotlin/
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── gradle.properties
```

### Module Descriptions

- **`commonMain`** — Contains all business logic and shared UI built with Compose Multiplatform. This is the core of the project: models, repositories, central logic, and screens reusable across all platforms.
- **`androidMain`** — Contains the Android-specific entry point.
- **`jvmMain`** — Contains the Desktop (JVM)-specific entry point.

---

## Architecture

The project follows the **MVVM** architecture with Compose Multiplatform as the presentation layer:

```
UI (Compose) → ViewModel → Repository → Persistence
```

Each layer has a clear responsibility:

- The **UI** is built with Compose and shared across platforms via `commonMain`.
- The **ViewModel** coordinates business logic and persistence.
- The **Repository** manages data access.
- **DTOs** and **Mappers** act as a translation layer between the domain model and the persistence format.

---

## Supported Platforms

| Platform | Description |
|---|---|
| **Android** | Native Android application |
| **Desktop (JVM)** | Desktop application for macOS, Linux and Windows |