# specs-java-libs
Java libraries created or extended (-Plus suffix) by SPeCS research group

## Project Structure

This repository contains multiple Java libraries organized as individual Gradle projects. Each library is self-contained with its own `build.gradle` and `settings.gradle` files.

## Prerequisites

- **Java 17 or higher** - All projects are configured to use Java 17
- **Gradle** - Build automation tool

## Building Projects

### Building a Single Project

To build a specific library, navigate to its directory and run:

```bash
cd <project-name>
./gradle build
```

For example, to build SpecsUtils:
```bash
cd SpecsUtils
./gradle build
```

### Available Gradle Tasks

Common tasks you can run for each project:

- `./gradle build` - Compile, test, and package the project
- `./gradle test` - Run unit tests
- `./gradle jar` - Create JAR file
- `./gradle sourcesJar` - Create sources JAR file
- `./gradle clean` - Clean build artifacts
- `./gradle tasks` - List all available tasks

### Dependencies

Projects use:
- **Maven Central** for external dependencies
- **JUnit 4** for testing
- **Inter-project dependencies** where needed (e.g., `:SpecsUtils`, `:CommonsLangPlus`)

## Development Setup

### IDE Configuration

While you can use any IDE that supports Gradle projects, here are some recommendations:

1. **IntelliJ IDEA**: Import the repository root, and it will automatically detect all Gradle subprojects
2. **VS Code**: Use the Java Extension Pack which includes Gradle support
3. **Eclipse**: Use the Gradle integration plugin and import existing Gradle projects

### Importing Projects

1. Clone this repository
2. Open your IDE
3. Import the root directory as a Gradle project
4. Your IDE should automatically detect and configure all subprojects

## Project List

The repository includes the following libraries:

- **AntTasks** - Custom Ant tasks
- **AsmParser** - Assembly parsing utilities  
- **CommonsCompressPlus** - Extended Apache Commons Compress
- **CommonsLangPlus** - Extended Apache Commons Lang
- **GearmanPlus** - Extended Gearman client
- **GitlabPlus** - GitLab API integration
- **GitPlus** - Git utilities
- **Gprofer** - Profiling utilities
- **GsonPlus** - Extended Google Gson
- **GuiHelper** - GUI utility classes
- **JacksonPlus** - Extended Jackson JSON processing
- **JadxPlus** - Extended JADX decompiler
- **JavaGenerator** - Java code generation utilities
- **jOptions** - Command-line options parser
- **JsEngine** - JavaScript engine integration (GraalVM)
- **LogbackPlus** - Extended Logback logging
- **MvelPlus** - Extended MVEL expression language
- **SlackPlus** - Slack API integration
- **SpecsHWUtils** - Hardware utilities
- **SpecsUtils** - Core utilities library
- **SymjaPlus** - Extended Symja symbolic math
- **tdrcLibrary** - TDRC's library utilities
- **XStreamPlus** - Extended XStream XML processing

## Contributing

When adding new features or fixing bugs:

1. Make your changes in the appropriate project directory
2. Run `./gradle build` to ensure everything compiles and tests pass
3. Follow the existing code style and conventions
4. Add tests for new functionality

## Legacy Information

This project was previously built using Eclipse, Ivy, and Ant. All build configuration has been migrated to Gradle for better dependency management and build automation. There might be some old configuration files (`ivysettings.xml`, `.classpath`, `.project`). These may be kept for historical reference but are no longer used in the build process.
