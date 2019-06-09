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

package pt.up.fe.specs.eclipse.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pt.up.fe.specs.util.SpecsFactory;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsXml;
import pt.up.fe.specs.util.properties.SpecsProperties;

/**
 * @author Joao Bispo
 *
 */
public class UserLibraries {

    private static final String PATH_PROPERTIES = ".metadata/.plugins/org.eclipse.core.runtime/.settings/org.eclipse.jdt.core.prefs";
    private static final String PREFIX_PROP_USER_LIB = "org.eclipse.jdt.core.userLibrary.";

    private final Map<String, List<File>> userLibraries;

    UserLibraries(Map<String, List<File>> userLibraries) {
        this.userLibraries = Collections.unmodifiableMap(userLibraries);
    }

    /**
     * Creates a new UserLibraries from a collection of other UserLibraries.
     *
     * @param userLibrariesCollection
     * @return
     */
    public static UserLibraries newInstance(Collection<UserLibraries> userLibrariesCollection) {
        Map<String, List<File>> fusedUserLibraries = new HashMap<>();

        for (UserLibraries userLibraries : userLibrariesCollection) {
            fusedUserLibraries.putAll(userLibraries.userLibraries);
        }

        return new UserLibraries(fusedUserLibraries);
    }

    /**
     * Creates a new UserLibraries object.
     *
     * @param eclipseProjects
     * @param userLibrariesFile
     * @return
     */
    public static UserLibraries newInstance(EclipseProjects eclipseProjects, File userLibrariesFile) {
        return new UserLibrariesParser(eclipseProjects, userLibrariesFile).parse();
    }

    /**
     * Always returns a UserLibraries object.
     *
     * @param workspace
     * @return
     */
    public static UserLibraries newInstance(File workspace,
            EclipseProjects eclipseProjects) {

        // Get properties file
        File propertiesFile = SpecsIo.existingFile(workspace, UserLibraries.PATH_PROPERTIES);

        // Parse properties
        Properties properties = SpecsProperties.newInstance(propertiesFile).getProperties();

        // Create map
        Map<String, List<File>> userLibraries = SpecsFactory.newHashMap();

        for (Object keyObj : properties.keySet()) {
            String key = (String) keyObj;

            if (!key.startsWith(UserLibraries.PREFIX_PROP_USER_LIB)) {
                continue;
            }

            String libName = key.substring(UserLibraries.PREFIX_PROP_USER_LIB.length());
            // System.out.println("Lib:" + libName);

            String value = properties.getProperty(key);
            // System.out.println("VALUE:" + value);
            // File xmlFile = new File("C:\\temp_output\\lib.xml");
            // IoUtils.write(xmlFile, value);
            // Document doc = XomUtils.getDocument(xmlFile);

            // Document doc = XomUtils.getDocument(value, false);
            Document doc = SpecsXml.getXmlRoot(value);

            if (doc == null) {
                SpecsLogs.msgInfo("Skipping lib '" + libName + "', could not get info");
                continue;
            }

            // Element element = doc.getRootElement();
            Element element = doc.getDocumentElement();

            // Sanity check
            if (!element.getNodeName().equals("userlibrary")) {
                SpecsLogs.msgWarn("NOT A USER LIBRARY: " + element.getNodeName());
                continue;
            }

            Optional<List<File>> jarFiles = getLibraryJars(eclipseProjects, element);

            if (!jarFiles.isPresent()) {
                SpecsLogs.msgInfo("Skipping lib '" + libName + "', could not get JAR file");
                continue;
            }

            // Add found jars
            userLibraries.put(libName, jarFiles.get());

        }

        return new UserLibraries(userLibraries);
    }

    private static Optional<List<File>> getLibraryJars(EclipseProjects eclipseProjects, Element element) {
        // Create List
        List<File> jarFiles = SpecsFactory.newArrayList();

        // Check children
        // for (int i = 0; i < element.getChildCount(); i++) {
        // Node node = element.getChild(i);
        //
        // if (!(node instanceof Element)) {
        // continue;
        // }
        //
        // Element child = (Element) node;
        //
        for (Element child : SpecsXml.getElementChildren(element)) {
            // Attribute attrib = child.getAttribute("path");
            Attr attrib = child.getAttributeNode("path");

            Optional<File> jarFile = getJar(attrib.getValue(), eclipseProjects);

            if (!jarFile.isPresent()) {
                return Optional.empty();
            }

            jarFiles.add(jarFile.get());
        }

        return Optional.of(jarFiles);
    }

    private static Optional<File> getJar(String value, EclipseProjects eclipseProjects) {
        // If starts with '/', remove it
        if (value.startsWith("/")) {
            value = value.substring(1);
        }

        // Get index of first '/'
        int splitIndex = value.indexOf('/');

        // Get project name
        String projectName = value.substring(0, splitIndex);

        File projectFolder = eclipseProjects.getProjectFolder(projectName);

        // Get file
        String filepath = value.substring(splitIndex + 1);

        // Check if file exists
        File jarFile = new File(projectFolder, filepath);
        if (!jarFile.isFile()) {
            SpecsLogs.msgInfo("Could not find User Library jar: '" + jarFile + "'");
            return Optional.empty();
        }

        // File jarFile = IoUtils.existingFile(projectFolder, filepath);

        return Optional.of(jarFile);
    }

    public List<File> getJars(String libraryName) {
        return userLibraries.get(libraryName);
    }

    public Collection<String> getLibraries() {
        return userLibraries.keySet();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return userLibraries.toString();
    }

    /**
     * Creates a new UserLibraries with paths relative to the given folder.
     *
     * @param rootFolder
     * @return
     */
    public UserLibraries makePathsRelative(File rootFolder) {

        Map<String, List<File>> relativeUserLibraries = new HashMap<>();

        for (String key : userLibraries.keySet()) {
            List<File> files = userLibraries.get(key);
            List<File> newFiles = new ArrayList<>();

            for (File file : files) {
                String relativeFilename = SpecsIo.getRelativePath(file, rootFolder);
                if (relativeFilename == null) {
                    throw new RuntimeException("Could not convert path '" + file + "' to relative path using as base '"
                            + rootFolder + "'");
                }

                // Add new file
                newFiles.add(new File(relativeFilename));
            }

            // Replace file list
            // userLibraries.put(key, newFiles);
            relativeUserLibraries.put(key, newFiles);

        }

        return new UserLibraries(relativeUserLibraries);
    }
}
