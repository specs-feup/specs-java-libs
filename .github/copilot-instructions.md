# SPeCS Java Libraries - Copilot Instructions

## Repository Overview

This repository contains 24 Java libraries developed by the SPeCS research group, organized as independent Gradle projects. Each library extends or enhances existing Java libraries (indicated by the "-Plus" suffix) or provides custom utilities. The repository uses Java 17, Gradle for builds, and follows a multi-project structure without a root Gradle configuration.

**Repository Statistics:**
- 24 main Java libraries + 1 legacy project (SpecsHWUtils)
- ~200K+ lines of Java code across all projects
- Mixed testing frameworks: JUnit 5 (modern projects) and JUnit 4 (legacy projects)
- Inter-project dependencies centered around SpecsUtils as the core utility library

## Build Instructions

### Prerequisites
- **Java 17 or higher** (REQUIRED - all projects target Java 17)
- **Gradle 8.13+** (system Gradle installation - NO gradlew wrapper in repo)

### Building Individual Projects

**ALWAYS navigate to the specific project directory before running Gradle commands.** Each project is self-contained with its own `build.gradle` and `settings.gradle`.

```bash
cd <project-name>
gradle build        # Compile, test, package, and validate coverage (includes tests)
gradle test         # Run tests only (without packaging)
gradle jar          # Create JAR file
gradle clean        # Clean build artifacts
```

### Build Dependencies Order

Due to inter-project dependencies, build in this order when building multiple projects:

1. **Core Libraries (no dependencies):**
   - CommonsLangPlus, CommonsCompressPlus, GsonPlus, XStreamPlus

2. **SpecsUtils** (most projects depend on this)

3. **All other projects** (depend on SpecsUtils and/or other core libraries)

### Key Build Considerations

- **SpecsUtils tests are slow** - can take several minutes to complete
- **`gradle build` includes tests** - build will fail if tests fail or coverage is insufficient
- Projects use **different testing frameworks**:
  - Modern projects: JUnit 5 with Mockito, AssertJ + Jacoco coverage validation
  - Legacy projects: JUnit 4
- **Coverage requirements**: Modern projects with Jacoco enforce 80% minimum test coverage
- **No parallel builds** - run projects sequentially to avoid dependency conflicts
- **Inter-project dependencies** use syntax like `implementation ':SpecsUtils'`

### Common Build Issues & Solutions

1. **Dependency not found errors**: Ensure dependent projects are built first (e.g., build SpecsUtils before projects that depend on it)
2. **Java version errors**: Verify Java 17+ is active (`java -version`)
3. **Test timeouts**: SpecsUtils tests can take 5+ minutes - be patient
4. **Coverage failures**: Modern projects require 80% test coverage - add tests if build fails due to insufficient coverage
5. **Memory issues**: For large projects, use `gradle build -Xmx2g` if needed

## Project Layout

### Root Structure
```
├── .github/workflows/nightly.yml    # CI pipeline
├── README.md                        # Project documentation
├── LICENSE                          # Apache 2.0 license
├── .gitignore                       # Ignores build/, .gradle/, etc.
└── [24 Java library directories]/
```

### Individual Project Structure
```
ProjectName/
├── build.gradle         # Gradle build configuration
├── settings.gradle      # Project settings
├── src/                 # Main source code
├── test/                # Unit tests (JUnit 4 or 5)
├── resources/           # Resources (optional)
├── bin/                 # Eclipse-generated (ignore)
└── build/               # Generated build artifacts
```

### Key Libraries and Their Purpose

**Core Infrastructure:**
- **SpecsUtils** - Core utilities, most other projects depend on this
- **CommonsLangPlus** - Extended Apache Commons Lang utilities
- **jOptions** - Command-line options and configuration management

**External Integrations:**
- **GitPlus** - Git operations and utilities
- **GitlabPlus** - GitLab API integration
- **SlackPlus** - Slack API integration
- **JsEngine** - JavaScript execution via GraalVM

**Data Processing:**
- **GsonPlus** - Extended JSON processing with Google Gson
- **JacksonPlus** - Extended JSON processing with Jackson
- **XStreamPlus** - Extended XML processing

**Development Tools:**
- **JavaGenerator** - Java code generation utilities
- **AntTasks** - Custom Ant build tasks

### Legacy Projects (No Gradle builds)
- **SpecsHWUtils** - Hardware utilities (Eclipse project only)  

## Continuous Integration

### GitHub Actions Workflow
File: `.github/workflows/nightly.yml`

**Triggers:** Push to any branch, manual workflow dispatch
**Environment:** Ubuntu latest, Java 17 (Temurin), Gradle current

**Build Process:**
1. Sequentially builds and tests all 24 Gradle projects
2. Uses `gradle build test` for each project
3. Fails if any project fails to build or test
4. Publishes JUnit test reports
5. Generates dependency graphs

### Tested Projects (in CI order):
AntTasks, AsmParser, CommonsCompressPlus, CommonsLangPlus, GitlabPlus, GitPlus, Gprofer, GsonPlus, GuiHelper, JacksonPlus, JadxPlus, JavaGenerator, jOptions, JsEngine, LogbackPlus, MvelPlus, SlackPlus, SpecsUtils, SymjaPlus, tdrcLibrary, XStreamPlus

### Local Validation Steps
1. **Build specific project**: `cd ProjectName && gradle build`
2. **Run tests**: `cd ProjectName && gradle test`
3. **Check code coverage** (for projects with Jacoco): `gradle jacocoTestReport`
4. **Validate dependencies**: Ensure dependent projects build successfully

## Development Guidelines

### Code Style & Conventions
- Java 17 language features are preferred
- Follow existing patterns within each project
- Add tests for new functionality (JUnit 5 for new code)
- Use appropriate testing framework for the project (check existing tests)

### Testing Approach
- **Modern projects**: JUnit 5 + Mockito + AssertJ with Jacoco coverage enforcement (80% minimum)
- **Legacy projects**: JUnit 4
- **Coverage validation**: Jacoco runs automatically with tests and enforces minimum coverage thresholds
- **Test locations**: Tests in `test/` directory, following package structure

### Making Changes
1. Identify the correct project for your changes
2. Check project's testing framework and conventions
3. Build the project first: `cd ProjectName && gradle build`
4. Make changes following existing patterns
5. Add/update tests appropriately
6. Re-run `gradle build` to ensure everything works
7. For projects with dependencies, test dependent projects as well

### Key Files to Check
- `build.gradle` - Dependencies, Java version, testing framework
- `src/` - Main source code structure and patterns
- `test/` - Testing approach and existing test structure
- `README.md` (if present) - Project-specific documentation

## Trust These Instructions

These instructions are comprehensive and validated. Only search for additional information if:
1. A specific command fails with an unexpected error
2. You encounter a build configuration not covered here
3. Project-specific documentation contradicts these general guidelines

Always check the project's individual `build.gradle` for dependencies and testing setup before making changes.
