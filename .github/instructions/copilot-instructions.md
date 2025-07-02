# Copilot Instructions for specs-java-libs

## 1. Repository Overview & Purpose

This repository contains 29+ Java utility libraries developed by the SPeCS Research Group at FEUP. It includes both original libraries and extended versions of popular libraries (with a "-Plus" suffix). The libraries cover a wide range of functionalities, from core utilities to integration with external tools and languages.

## 2. Development Environment & Setup

- **Java Version:** 17 (required for all projects)
- **Build System:** Gradle (with VSCode integration)
- **IDE:** VSCode is recommended for development and project management.

## 3. Architecture & Dependencies

- **Core Library:** `SpecsUtils` is the foundational utility library; most other projects depend on it.
- **Layered Dependencies:** Libraries may depend on each other (e.g., JsEngine depends on CommonsLangPlus, jOptions, SpecsUtils).
- **External Dependencies:** Managed via Maven Central and Gradle.
- **Namespace:** All code is under the `pt.up.fe.specs.*` package structure.

## 4. Library Categories & Descriptions

### Core Utilities
- **SpecsUtils:** Static utility classes for collections, I/O, strings, logging, XML, reflection, threading, and more.
- **CommonsLangPlus:** Extensions to Apache Commons Lang.
- **jOptions:** Configuration and data store management.

### Integration Libraries
- **JsEngine:** JavaScript engine abstraction: GraalVM (for executing JS from Java) and Node.js (for executing Java from JS).
- **GitPlus:** Utilities for Git operations using JGit.
- **GsonPlus, JacksonPlus, XStreamPlus:** Enhanced JSON/XML serialization/deserialization.

### Specialized Tools
- **AsmParser:** Assembly language parsing utilities.
- **JavaGenerator:** Code generation helpers.
- **GuiHelper:** Swing and UI utilities.
- **Z3Helper:** Integration with the Z3 theorem prover.

## 5. Code Patterns & Conventions

- **Static Utility Classes:** Most libraries use static methods for utility operations (e.g., `SpecsCollections`, `SpecsIo`).
- **Provider Pattern:** Common for resource and key management (e.g., `ResourceProvider`, `KeyProvider`).
- **Event System:** Event-driven patterns (e.g., `EventController`, `EventNotifier`).
- **Logging:** Unified logging API with custom handlers (`SpecsLoggers`).

## 6. Build & Development Guidelines

- **Gradle:** Each project has a `build.gradle` specifying dependencies and source sets.
- **Testing:** JUnit is used for unit testing.
- **Project Structure:** Source code is under `src/`, resources under `resources/`, and tests under `test/`.

## 7. Contributing & Extension Guidelines

- **Adding Libraries:** Follow the naming convention (use "-Plus" for extensions).
- **Dependencies:** Prefer SpecsUtils for shared utilities; manage external dependencies via Gradle.
- **Coding Style:** Adhere to the code style and formatting rules in `SupportJavaLibs/configs/`.
- **License:** All code is Apache License 2.0.

## 8. Common Use Cases & Examples

- **File I/O:** Use `SpecsIo` for reading/writing files.
- **Collections:** Use `SpecsCollections` for advanced list/map operations.
- **Configuration:** Use `jOptions` for managing application settings.
- **JavaScript Integration:** Use `JsEngine` to run JS code from Java.
- **Git Operations:** Use `GitPlus` for cloning and managing repositories.

---

**For LLMs:**  
- Use the above structure to understand the repository's architecture, dependencies, and coding conventions.
- When generating code, prefer using SpecsUtils and other core libraries for common tasks.
- Follow the package and naming conventions to ensure consistency.
- Refer to individual library READMEs for more details on specific utilities.
