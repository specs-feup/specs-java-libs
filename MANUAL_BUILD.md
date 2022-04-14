# Instructions for Manually Building SPeCS Java Projects

The projects under this repository are configured to easily be built automatically using Eclipse.
If you are okay with working with these projects in Eclipse, you can configure it with the guide
in the repository's (/README.md)[readme file]. If you need a fast way to build one of the projects
from the command line, you can use the (http://specs.fe.up.pt/tools/eclipse-build.jar)[eclipse-build]
tool, ((https://github.com/specs-feup/specs-java-tools/tree/master/EclipseBuild)[source code here]).
This tool can generate an Ant build file for a project based on its Eclipse build configuration and
also fetch external dependencies, and then build the project.

> TODO: link to documentation for using the eclipse-build tool

The following documentation aims to support understanding how the projects are currently built, and
allow any future retooling efforts to proceed with more confidence.

## Tooling

Besides the obvious dependency on a working Java toolchain, there are some further tools that might
be needed to build the projects:
 * External (Maven) dependencies are being fetched using Apache Ivy.
 * Testing is done through JUnit 4.
 * Several projects make use of JavaCC to generate parsers
 * ANTLR might be used in some external projects that make use of this documentation

To understand with tools are needed to build a specific project, check the .project file for it. It
will contain a list under `<natures>`, which determine the tools that will be used.

## Dependencies

### Fetching external dependencies

Each project may depend on a set of external dependencies.

Jar dependencies are currently being fetched from Maven repositories using Apache Ivy.
You can find the Maven repositories being searched in the (/ivysettings.xml)[Ivy settings file at the root of the repository].
The dependencies for each project are then specified in its respective `ivy.xml` file. If there is no
such file, there should be no external jars being fetched for that project.

External source dependencies must be fetched manually. You should clone the repository that contains
those dependencies and import those projects into Eclipse. To know which repositories need to be cloned
to access the required sources, you can check the `eclipse.build` file for the project being built; it
will start with a list of GitHub URLs.

### Specifying dependencies for a project

Each project specifies its dependencies on a `.classpath` file.

 * Internal sources of the project are included using a relative path.
 * External source dependencies are included using an absolute path, and the combineaccessrules attribute is set to 'false'.
 * Managed dependencies are included using containers. The JRE container includes the standard Java libraries, the IvyDE container includes dependencies fetched with Ivy, the JUnit container includes the JUnit libraries, etc.

## Build actions

You can check the .project file for each project to get an idea of the required build steps. For example, consider the following steps:

```xml
<buildSpec>
    <buildCommand>
        <name>sf.eclipse.javacc.javaccbuilder</name>
        <arguments>
        </arguments>
    </buildCommand>
    <buildCommand>
        <name>org.eclipse.jdt.core.javabuilder</name>
        <arguments>
        </arguments>
    </buildCommand>
</buildSpec>
```

This means that first the JavaCC tool will be run to generate a parser, and then a Java build will occur,
probably generating a Jar file for the project.

Some projects might have building or running configurations under their /run directory, warranting more
specific build documentation.

## Final Remarks

These instructions mostly apply to compiling Java libraries or programs that are on the simpler end.
Some other projects depend on other languages, such as C++, or might perform more specific build tasks.
In that case, the maintainers should consider documenting that project's build steps in a specific
BUILDING.md file.
