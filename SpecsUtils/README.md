# SpecsUtils

SpecsUtils is a Java utility library developed by the SPeCS Research Group. It provides a comprehensive set of static utility classes and methods to simplify and extend Java development, especially for projects in the SPeCS ecosystem. The library covers a wide range of functionalities, including:

- **Collections**: Helpers for lists, maps, sets, and other collection types.
- **I/O**: File and resource reading/writing utilities.
- **Strings and Numbers**: Parsing, formatting, and manipulation.
- **Logging**: Unified logging API with support for custom handlers and output redirection.
- **XML**: Parsing, validation, and DOM manipulation.
- **Reflection**: Utilities for inspecting and manipulating classes, methods, and fields at runtime.
- **Threading**: Thread management and concurrency helpers.
- **Providers**: Interfaces and helpers for resource and key providers.
- **Date, Math, Random, Path, and more**: Utilities for common programming tasks.

## Features
- Consistent API and coding style across all utilities
- Designed for extensibility and integration with SPeCS tools
- Includes deprecated methods for backward compatibility
- Well-documented with Javadoc and file-level comments

## Usage
Add SpecsUtils as a dependency in your Java project. You can then use the static methods directly, for example:

```java
import pt.up.fe.specs.util.SpecsCollections;

List<String> sublist = SpecsCollections.subList(myList, 2);
```

## Project Structure
- `src/pt/up/fe/specs/util/` - Main utility classes
- `src/pt/up/fe/specs/util/collections/` - Collection-related helpers
- `src/pt/up/fe/specs/util/providers/` - Provider interfaces and helpers
- `src/pt/up/fe/specs/util/reporting/` - Reporting and logging interfaces
- `src/pt/up/fe/specs/util/xml/` - XML utilities
- ...and more

## Authors
Developed and maintained by the SPeCS Research Group at FEUP.
