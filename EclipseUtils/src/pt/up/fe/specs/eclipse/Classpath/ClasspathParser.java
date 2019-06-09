/**
 * Copyright 2013 SPeCS Research Group.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.specs.eclipse.Classpath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.common.base.Preconditions;

// import nu.xom.Attribute;
// import nu.xom.Document;
// import nu.xom.Element;
// import nu.xom.Node;
import pt.up.fe.specs.eclipse.Utilities.EclipseProjects;
import pt.up.fe.specs.eclipse.Utilities.UserLibraries;
import pt.up.fe.specs.eclipse.builder.BuildResource;
import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsXml;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.lazy.Lazy;
import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * @author Joao Bispo
 *
 */
public class ClasspathParser {

    private static final String FILENAME_CLASSPATH = ".classpath";
    private static final String USER_LIBRARY = "org.eclipse.jdt.USER_LIBRARY/";
    private static final String IVY = "org.apache.ivyde.eclipse.cpcontainer.IVYDE_CONTAINER/";
    private static final String JUNIT4 = "org.eclipse.jdt.junit.JUNIT_CONTAINER/4";
    // private static final String JUNIT5 = "org.eclipse.jdt.junit.JUNIT_CONTAINER/5";

    // If true, user libraries declared in a repository will be available to all repositories
    // This is the default behavior in Eclipse, we are replicating it here
    private static final boolean FUSE_USER_LIBRARIES = true;

    private static final Set<String> CONTAINERS_TO_IGNORE = SpecsFactory.newHashSet(Arrays.asList(
            // "org.eclipse.jdt.launching.JRE_CONTAINER", "org.eclipse.jdt.junit.JUNIT_CONTAINER"));
            "org.eclipse.jdt.launching.JRE_CONTAINER"));

    private final EclipseProjects eclipseProjects;
    private final Map<File, UserLibraries> userLibraries;
    private final Lazy<UserLibraries> fusedUserLibraries = Lazy.newInstance(() -> createFusedUserLibraries());

    // TODO: This should be final, but it would need a restructuring of the class
    private File currentProjectFolder = null;
    private List<String> currentSourceFolders;

    private final Map<String, ClasspathFiles> classpathCache;
    private final List<File> junitFiles = new ArrayList<>();

    /**
     * TODO: This should be the preferred constructor, replace others.
     *
     * @param userLibraries
     * @param eclipseProjects
     */
    // private ClasspathParser(EclipseProjects eclipseProjects, Optional<UserLibraries> userLibraries) {
    public ClasspathParser(EclipseProjects eclipseProjects, Map<File, UserLibraries> userLibraries) {
        currentSourceFolders = new ArrayList<>();
        currentProjectFolder = null;
        this.userLibraries = userLibraries;
        this.eclipseProjects = eclipseProjects;

        classpathCache = new HashMap<>();
    }

    public static ClasspathParser newInstance(File repositoryFolder) {
        EclipseProjects eclipseProjects = EclipseProjects.newFromRepository(repositoryFolder);

        // return new ClasspathParser(eclipseProjects, Optional.empty());
        return new ClasspathParser(eclipseProjects, new HashMap<>());
    }

    /**
     * Creates a new ClasspathParser from the folder which contains Eclipse projects, and an exported user libraries
     * file.
     *
     * @param repositoryFolder
     * @param userLibrariesFile
     * @return
     */
    public static ClasspathParser newInstance(File repositoryFolder, File userLibrariesFile) {

        // In case user libraries is null
        if (userLibrariesFile == null) {
            return newInstance(repositoryFolder);
        }

        EclipseProjects eclipseProjects = EclipseProjects.newFromRepository(repositoryFolder);

        UserLibraries repoUserLibraries = UserLibraries.newInstance(eclipseProjects, userLibrariesFile);
        Map<File, UserLibraries> userLibraries = new HashMap<>();
        userLibraries.put(repositoryFolder, repoUserLibraries);

        return new ClasspathParser(eclipseProjects, userLibraries);
    }

    /**
     * Creates a new instance from an Eclipse workspace. By using an Eclipse workspace instead of the folder of a
     * repository (and possibly a user libraries file), it might execute faster by using information already built by
     * Eclipse, instead of building it itself.
     *
     * @param workspaceFolder
     * @return
     */
    public static ClasspathParser newFromWorkspace(File workspaceFolder) {
        return new ClasspathParser(workspaceFolder);
    }

    // private ClasspathParser(File workspaceFolder, Optional<File> outputFolder) {
    private ClasspathParser(File workspaceFolder) {

        // this.workspaceFolder = workspaceFolder;
        // this.projectName = projectName;
        currentSourceFolders = new ArrayList<>();
        // this.classpathFiles = new HashMap<>();
        /*
        if (outputFolder.isPresent()) {
        File outF = outputFolder.get();
        this.eclipseProjects = EclipseProjects.newFromWorkspace(workspaceFolder).makePathsRelative(outF);
        this.userLibraries = Optional.of(UserLibraries.newInstance(workspaceFolder, eclipseProjects)
        	    .makePathsRelative(outF));
        } else {
        */
        eclipseProjects = EclipseProjects.newFromWorkspace(workspaceFolder);
        // userLibraries = Optional.of(UserLibraries.newInstance(workspaceFolder, eclipseProjects));
        UserLibraries workspaceUserLibraries = UserLibraries.newInstance(workspaceFolder, eclipseProjects);
        userLibraries = new HashMap<>();
        userLibraries.put(workspaceFolder, workspaceUserLibraries);

        classpathCache = new HashMap<>();
        // }

        // parseClasspaths();
    }

    private UserLibraries createFusedUserLibraries() {
        return UserLibraries.newInstance(userLibraries.values());
    }

    /*
    public UserLibraries getUserLibraries() {
    return userLibraries;
    }
    */

    /*
    private void parseClasspaths() {
    
    // Map<String, ClasspathFiles> classpathFiles = new HashMap<>();
    for (String projectName : eclipseProjects.getProjectNames()) {
        FilesetBuilder builder = new FilesetBuilder();
    
        parseClasspath(projectName, builder);
    
        ClasspathFiles classpath = builder.newClasspath(projectName, projectFolder, sourceFolders);
    
        classpathFiles.put(projectName, classpath);
    }
    
    // return classpathFiles;
    }
    */

    public ClasspathFiles getClasspath(String projectName) {
        /*
        FilesetBuilder builder = new FilesetBuilder();
        parseClasspath(projectName, builder);
        
        return builder.newClasspath(projectName, currentProjectFolder, currentSourceFolders);
        */
        ClasspathFiles files = classpathCache.get(projectName);
        if (files == null) {
            FilesetBuilder builder = new FilesetBuilder(projectName);
            parseClasspath(projectName, builder);

            files = builder.newClasspath(projectName, currentProjectFolder, currentSourceFolders);
            classpathCache.put(projectName, files);
        }

        return files;

    }

    public Optional<ClasspathFiles> getClasspathTry(String projectName) {
        try {
            return Optional.of(getClasspath(projectName));
        } catch (Exception e) {
            SpecsLogs.msgInfo("Could not get classpath of project '" + projectName + "':" + e.getMessage());
            return Optional.empty();
        }
    }

    public boolean usesIvy() {
        for (String projectName : getEclipseProjects().getProjectNames()) {
            if (getClasspath(projectName).getIvyPath().isPresent()) {
                return true;
            }
        }

        return false;
    }

    /**
     * @return the eclipseProjects
     */
    public EclipseProjects getEclipseProjects() {
        return eclipseProjects;
    }

    private void parseClasspath(String projectName, FilesetBuilder builder) {
        // System.out.println("-- " + projectName + " --");

        List<String> sourceFolders = new ArrayList<>();

        File projectFolder = getProjectFolder(projectName);

        // Check if builder already parsed this project
        if (builder.hasParsedProject(projectFolder.getPath())) {
            return;
        }

        builder.markProjectAsParsed(projectFolder.getPath());

        File classpathFile = new File(projectFolder, ClasspathParser.FILENAME_CLASSPATH);
        if (!classpathFile.isFile()) {
            SpecsLogs.msgInfo("Ignoring project '" + projectName + "', could not find classpath file '"
                    + ClasspathParser.FILENAME_CLASSPATH + "' in folder '"
                    + projectFolder + "'");
            return;
            // throw new RuntimeException("Could not find classpath file '" + FILENAME_CLASSPATH + "' in folder '"
            // + projectFolder + "'");
        }

        // Document classpath = XomUtils.getDocument(SpecsIo.read(classpathFile), false);
        // Element classpath = SpecsXml.getXmlRoot(classpathFile).getElementById("classpath");
        Document classpath = SpecsXml.getXmlRoot(classpathFile);
        // Element element = classpath.getRootElement();
        // Element element = classpath.getDocumentElement();

        // for (int i = 0; i < element.getChildCount(); i++) {
        // Node child = element.getChild(i);
        // if (!(child instanceof Element)) {
        // continue;
        // }
        // System.out.println("PROJECT: " + projectName);
        // // System.out.println("DOC ELEM: " + classpath.getDocumentElement());
        // System.out.println(
        // "DOC ELEM Children: " + SpecsXml.getElementChildren(classpath.getDocumentElement()));
        // System.out.println(
        // "DOC ELEMs: " + SpecsXml.getElements(classpath.getDocumentElement()));

        for (Element childElem : SpecsXml.getElementChildren(classpath.getDocumentElement(), "classpathentry")) {

            // if (!childElem.getLocalName().equals("classpathentry")) {
            // SpecsLogs.msgWarn("Entry not parsed:" + childElem.getLocalName());
            // continue;
            // }

            // System.out.println("KIND: " + SpecsXml.getAttribute(childElem, "kind"));

            // Get 'kind' value
            // Attribute kindAttribute = childElem.getAttribute("kind");
            Attr kindAttribute = childElem.getAttributeNode("kind");
            String kindValue = kindAttribute.getValue();
            // System.out.println("OLD KIND:" + kindValue);
            // Get 'path' value
            // Attribute pathAttribute = childElem.getAttribute("path");
            Attr pathAttribute = childElem.getAttributeNode("path");
            String pathValue = pathAttribute.getValue();

            // Attribute accessRulesAttribute = childElem.getAttribute("combineaccessrules");
            Attr accessRulesAttribute = childElem.getAttributeNode("combineaccessrules");
            String accessRulesValue = null;
            if (accessRulesAttribute != null) {
                accessRulesValue = accessRulesAttribute.getValue();
            }

            // Get 'exported' value
            // Attribute exportedAttribute = childElem.getAttribute("exported");
            // boolean exported = false;

            // if (exportedAttribute != null) {
            // exported = Boolean.parseBoolean(exportedAttribute.getValue());
            // }

            // Treat the kind "container"
            if (kindValue.equals("con")) {

                // Check if it is one of the containers to ignore
                if (isContainerToIgnore(pathValue)) {
                    SpecsLogs.msgLib("Ignoring " + pathValue);
                    continue;
                }

                // Check if it is a user library
                if (pathValue.startsWith(ClasspathParser.USER_LIBRARY)) {
                    UserLibraries projectUserLibraries = getProjectsUserLibraries(projectName);
                    // if (!userLibraries.isPresent()) {
                    if (projectUserLibraries == null) {
                        SpecsLogs
                                .msgWarn("In project '"
                                        + projectName
                                        + "', found a Eclipse user library reference ('"
                                        + pathValue
                                        + "'). To support it, export the user libraries of your Eclipse workspace and pass it as input.");
                        continue;
                    }

                    String library = pathValue.substring(ClasspathParser.USER_LIBRARY.length());
                    // List<File> jars = userLibraries.get().getJars(library);
                    List<File> jars = projectUserLibraries.getJars(library);
                    if (jars == null) {
                        SpecsLogs.msgWarn("User library '" + library + "' not found, when processing project '"
                                + projectName + "'.");
                        continue;
                    }

                    builder.addJars(jars);
                    continue;
                }

                // Check if it is an Ivy library
                if (pathValue.startsWith(ClasspathParser.IVY)) {

                    // If the builder does not correspond to the project with the Ivy dependency, add transitive Ivy
                    // dependency
                    if (!projectName.equals(builder.getProjectName())) {
                        builder.addProjectWithIvy(projectName);
                        continue;
                    }

                    String ivyPath = getIvyPath(pathValue.substring(ClasspathParser.IVY.length()));
                    builder.addIvyPath(ivyPath);
                    continue;
                }

                // Check if it is an Eclipse JUnit
                if (pathValue.startsWith(ClasspathParser.JUNIT4)) {
                    // Add Junit
                    builder.addJars(getJunitFiles());
                    continue;
                }

                SpecsLogs.msgWarn("Does not know how to interpret container '" + pathValue + "' in project '"
                        + projectName + "', ignoring.");
                continue;
            }

            // Treat the kind "src"
            if (kindValue.equals("src")) {
                // Check if it is a src folder of the project
                if (accessRulesValue == null) {
                    // Check if path value starts with "/" - can represent a
                    // project in older .classpath files
                    if (!pathValue.startsWith("/")) {

                        // Add to sources folder
                        sourceFolders.add(pathValue);
                        continue;
                    }

                } else if (accessRulesValue.equals("true")) {
                    SpecsLogs.msgWarn("Check if it is correct to ignore '" + pathValue + "'");
                    continue;
                }

                // Recursively add project to builder
                parseClasspath(pathValue, builder);
                continue;
            }

            // Treat the kind "out"
            if (kindValue.equals("output")) {

                // Folder might not exist, since project might not have been built yet
                File projectClasses = new File(projectFolder, pathValue);

                builder.addProject(projectName, projectClasses);
            }
        }

        currentSourceFolders = sourceFolders;
        currentProjectFolder = getProjectFolder(projectName);

    }

    private UserLibraries getProjectsUserLibraries(String projectName) {
        // Return all libraries, if option to fuse user libraries is enabled
        if (FUSE_USER_LIBRARIES) {
            return fusedUserLibraries.get();
        }

        // Check if there is a defined repo
        Optional<File> repoFolder = eclipseProjects.getProjectRepositoryTry(projectName);

        if (!repoFolder.isPresent()) {
            // Map of user libraries should have only one value, return that value
            Preconditions.checkArgument(userLibraries.size() == 1,
                    "Expected user libraries to have 1 element, it has " + userLibraries.size());

            return userLibraries.values().stream().findFirst().get();
        }

        // Get corresponding user libraries
        return userLibraries.get(repoFolder.get());
    }

    private List<File> getJunitFiles() {
        // Check if files already present
        if (!junitFiles.isEmpty()) {
            return junitFiles;
        }

        // Copy junit files
        List<ResourceProvider> junitResources = Arrays.asList(BuildResource.JUNIT, BuildResource.HAMCREST);

        junitFiles.addAll(junitResources.stream()
                .map(resource -> SpecsIo.resourceCopy(resource.getResource(), new File("."), false, true))
                .collect(Collectors.toList()));

        return junitFiles;
    }

    private static String getIvyPath(String ivyValue) {
        String attributeStart = "ivyXmlPath=";
        String attributeEnd = "&";

        int startIndex = ivyValue.indexOf(attributeStart);
        if (startIndex == -1) {
            throw new NotImplementedException("Could not find '" + attributeStart
                    + "', do not know how to parse the string: " + ivyValue);
        }

        String ivyPath = ivyValue.substring(startIndex + attributeStart.length());

        int endIndex = ivyPath.indexOf(attributeEnd);

        if (endIndex == -1) {
            endIndex = ivyPath.length();
        }

        ivyPath = ivyPath.substring(0, endIndex);

        return ivyPath;
    }

    private File getProjectFolder(String projectName) {
        File projectFolder = eclipseProjects.getProjectFolder(projectName);

        String canonicalPath = null;
        try {
            canonicalPath = projectFolder.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException("Could not get canonical path for '" + canonicalPath + "'", e);
        }

        // Now use canonical path
        canonicalPath = canonicalPath.replace('\\', '/');
        currentProjectFolder = new File(canonicalPath);
        return currentProjectFolder;
    }

    /**
     * @param pathValue
     * @return
     */
    private static boolean isContainerToIgnore(String pathValue) {
        // Just check container until first '/'
        int index = pathValue.indexOf('/');
        if (index != -1) {
            pathValue = pathValue.substring(0, index);
        }

        return ClasspathParser.CONTAINERS_TO_IGNORE.contains(pathValue);
        /*
         * for(String containersToIgnore : CONTAINERS_TO_IGNORE) {
         * if(pathValue.startsWith(containersToIgnore)) { return true; } }
         *
         * return false;
         */
    }

    /**
     * Returns a list of all Eclipse projects the given project depends on.
     *
     * <p>
     * The search is done recursively.
     *
     * @param projectName
     * @return
     */
    public Collection<String> getDependentProjects(String projectName) {
        Set<String> projects = new HashSet<>();

        getDependentProjects(projectName, projects);

        return projects;
    }

    /**
     * Recursive helper method which does all the work.
     *
     * @param projectName
     * @param projects
     * @return
     */
    private void getDependentProjects(String projectName, Set<String> projects) {
        // If project already on the set, ignore
        if (projects.contains(projectName)) {
            return;
        }

        // Add self
        projects.add(projectName);

        // Add all dependencies of the project
        for (String dependentProject : getClasspath(projectName).getDependentProjects()) {
            getDependentProjects(dependentProject, projects);
        }
    }

}
