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

package pt.up.fe.specs.eclipse;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.tools.ant.BuildEvent;
import org.apache.tools.ant.BuildListener;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.junit.BeforeClass;
import org.junit.Test;

import pt.up.fe.specs.eclipse.Classpath.ClasspathFiles;
import pt.up.fe.specs.eclipse.Classpath.ClasspathParser;
import pt.up.fe.specs.eclipse.Utilities.EclipseProjects;
import pt.up.fe.specs.eclipse.Utilities.UserLibraries;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;

/**
 * @author Joao Bispo
 *
 */
public class XomTester {

    @BeforeClass
    public static void oneTimeSetUp() {
        SpecsSystem.programStandardInit();
    }

    // @Test
    /*
    public void testXml() {

        // String xmlFilename =
        // "C:\\Users\\Joao
        // Bispo\\Work\\Code\\suikasoft-java2\\JavaSe\\.metadata\\.plugins\\org.eclipse.core.runtime\\.settings\\org.eclipse.jdt.core.prefs";
        String xmlFilename = "C:\\Users\\Joao Bispo\\Desktop\\user_test.xml";
        File xmlFile = SpecsIo.existingFile(xmlFilename);

        Document doc = null;
        try {
            Builder parser = new Builder(true);
            doc = parser.build(xmlFile);
            System.out.println("VALUE:" + doc.getValue());
            assertTrue(true);
        } catch (ValidityException ex) {
            doc = ex.getDocument();
        } catch (ParsingException ex) {
            System.out.println("EX.:" + ex);
            System.err.println("Cafe con Leche is malformed today. (How embarrassing!)");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Could not connect to Cafe con Leche. The site may be down.");
            System.exit(1);
        }


    //        	for (int i = 0; i < doc.getChildCount(); i++) {
        	  //  System.out.println("VALUE:" + doc.getChild(i).getValue());
        	//}
        //

    }
    */

    /*
        public static Document getDocument(String xmlContents) {

        }
      */
    /*
    public static Document getDocument(File xmlFile) {
    Document doc = null;
    try {
        Builder parser = new Builder(true);
        // doc = parser.build(xmlContents);
        doc = parser.build(xmlFile);
    } catch (ValidityException ex) {
        System.out.println("Validity test not passed.");
        doc = ex.getDocument();
    } catch (ParsingException ex) {
        System.out.println("Parsing Exception:" + ex);
        // System.err.println("Cafe con Leche is malformed today. (How embarrassing!)");
        // System.exit(1);
    } catch (IOException ex) {
        System.out.println("IOException:" + ex);
        // System.err.println("Could not connect to Cafe con Leche. The site may be down.");
        // System.exit(1);
    }

    return doc;
    }
    */
    // @Test
    /*
    public void testProperties() {
    String PREFIX_PROP_USER_LIB = "org.eclipse.jdt.core.userLibrary.";

    String propsFilename = "C:\\Users\\Joao Bispo\\Work\\Code\\suikasoft-java2\\JavaSe\\.metadata\\.plugins\\org.eclipse.core.runtime\\.settings\\org.eclipse.jdt.core.prefs";
    File propsFile = IoUtils.existingFile(propsFilename);

    Properties properties = PropertiesUtils.load(propsFile);
    for (Object keyObj : properties.keySet()) {
        String key = (String) keyObj;

        if (!key.startsWith(PREFIX_PROP_USER_LIB)) {
    	continue;
        }

        String lib = key.substring(PREFIX_PROP_USER_LIB.length());
        System.out.println("Lib:" + lib);

        String value = properties.getProperty(key);
        //System.out.println("VALUE:" + value);
        File xmlFile = new File("C:\\temp_output\\lib.xml");
        IoUtils.write(xmlFile, value);
        Document doc = getDocument(xmlFile);

        if (doc == null) {
    	System.out.println("Skipping lib.");
    	continue;
        }

        Element element = doc.getRootElement();
        if (!element.getNodeName().equals("userlibrary")) {
    	System.out.println("NOT A USER LIBRARY");
    	continue;
        }

        //listChildren(element, 0);

        for (int i = 0; i < element.getChildCount(); i++) {
    	Node node = element.getChild(i);

    	if(!(node instanceof Element)) {
    	    continue;
    	}

    	Element child = (Element) node;
    	Attribute attrib = child.getAttribute("path");



    	System.out.println("VALUE:" + attrib.getValue());
        }


        /*
        	    for (int i = 0; i < doc.getChildCount(); i++) {
        		System.out.println("VALUE:" + doc.getChild(i).getValue());
        	    }
        */
    /*
    }
    	// System.out.println("PROPS:"+properties);

        }
    */
    /*
    public static void listChildren(Node current, int depth) {

        System.out.print(SpecsStrings.buildLine(" ", depth));

        // printSpaces(depth);
        String data = "";
        if (current instanceof Element) {
            Element temp = (Element) current;
            data = ": " + temp.getQualifiedName();
        } else if (current instanceof ProcessingInstruction) {
            ProcessingInstruction temp = (ProcessingInstruction) current;
            data = ": " + temp.getTarget();
        } else if (current instanceof DocType) {
            DocType temp = (DocType) current;
            data = ": " + temp.getRootElementName();
        } else if (current instanceof Text || current instanceof Comment) {
            String value = current.getValue();
            value = value.replace('\n', ' ').trim();
            if (value.length() <= 20) {
                data = ": " + value;
            } else {
                data = ": " + current.getValue().substring(0, 17) + "...";
            }
        }
        // Attributes are never returned by getChild()
        System.out.println(current.getClass().getName() + data);
        for (int i = 0; i < current.getChildCount(); i++) {
            listChildren(current.getChild(i), depth + 1);
        }

    }
    */
    @Test
    public void testUserLibraries() {
        String workspaceFilename = "C:\\Users\\JoaoBispo\\Work\\Repositories\\specs-java";
        File repositoryFolder = SpecsIo.existingFolder(workspaceFilename);

        EclipseProjects eclipseProjects = EclipseProjects.newFromRepository(repositoryFolder);

        File userLibFile = new File(repositoryFolder, "Utils/Support/configs/specs-java.userlibraries");
        UserLibraries userLibraries = UserLibraries.newInstance(eclipseProjects, userLibFile);

        System.out.println("UserLibraries:\n" + userLibraries);
    }

    // @Test
    /*
    public void parseClasspath() {

    String workspaceFilename = "C:\\Users\\Joao Bispo\\Work\\Code\\suikasoft-java2\\JavaSe\\";
    File workspaceFolder = IoUtils.existingFolder(workspaceFilename);

    String projetName = "MatlabToCTester";
    // String projetName = "SymjaPlus";

    ClasspathParser parser = new ClasspathParser(workspaceFolder);

    ClasspathFiles classpathFiles = parser.getClasspath(projetName);
    System.out.println("PARSED CLASSPATH:" + classpathFiles);
    }
    */

    @Test
    public void createAndRunDeployment() {
        SpecsIo.resourceCopy("jar-in-jar-loader.zip");

        String repositoryFoldername = "C:\\Users\\JoaoBispo\\Work\\Workspaces\\specs-java\\specs-java";
        File repositoryFolder = SpecsIo.existingFolder(repositoryFoldername);

        File userLibrariesFile = new File(repositoryFolder, "Utils\\Support\\configs\\specs-java.userlibraries");

        String projetName = "MatlabWeaver";
        // String projetName = "SymjaPlus";
        String outputFilemame = "matlabWeaver.jar";
        String outputjar = "C:\\temp_output\\deploy\\" + outputFilemame;

        String mainClass = "org.specs.mweaver.MWeaverLauncher";

        ClasspathParser parser = ClasspathParser.newInstance(repositoryFolder, userLibrariesFile);

        ClasspathFiles classpathFiles = parser.getClasspath(projetName);

        StringBuilder jarList = new StringBuilder();
        for (File jarFile : classpathFiles.getJarFiles()) {
            jarList.append(jarFile.getName());
            jarList.append(" ");
        }

        final String prefix = "			";
        StringBuilder fileset = new StringBuilder();
        for (File jarFile : classpathFiles.getJarFiles()) {
            String line = getZipfileset(jarFile);

            fileset.append(prefix);
            fileset.append(line);
            fileset.append("\n");
        }

        // StringBuilder fileset= new StringBuilder();
        for (File projectFolder : classpathFiles.getBinFolders()) {
            String line = getFileset(projectFolder);

            fileset.append(prefix);
            fileset.append(line);
            fileset.append("\n");
        }

        // Replace fields in template
        String template = SpecsIo.getResource(DeployResource.DEPLOY_JAR_IN_JAR_TEMPLATE);

        template = template.replace("<OUTPUT_JAR_FILE>", outputjar);
        template = template.replace("<MAIN_CLASS>", mainClass);
        template = template.replace("<JAR_LIST>", jarList.toString());
        template = template.replace("<FILESET>", fileset.toString());

        // Save script
        SpecsIo.write(new File("build.xml"), template);

        // Run script
        // ProcessUtils.run(Arrays.asList("ant", "build.xml"), IoUtils.getWorkingDir().getPath());
        // Launch ant
        Project project = new Project();
        project.init();

        File buildFile = new File("build.xml");

        // project.addBuildListener(new DefaultLogger());

        ProjectHelper.configureProject(project, buildFile);

        project.addBuildListener(newStdoutListener());
        project.executeTarget("create_run_jar");

        // Check if jar file exists
        File outputJarFile = SpecsIo.existingFile(outputjar);
        if (outputJarFile == null) {
            fail();
        }
    }

    public static BuildListener newStdoutListener() {
        BuildListener outListener = new BuildListener() {

            @Override
            public void taskStarted(BuildEvent arg0) {
                // System.out.println("Task Started: "+arg0.getTask().getTaskName());
                // System.out.println(arg0.getMessage());
            }

            @Override
            public void taskFinished(BuildEvent arg0) {
                // System.out.println(arg0.getMessage());
            }

            @Override
            public void targetStarted(BuildEvent arg0) {
                SpecsLogs.msgInfo("[ANT]:Started target '" + arg0.getTarget() + "'");
                // System.out.println(arg0.getMessage());

            }

            @Override
            public void targetFinished(BuildEvent arg0) {
                SpecsLogs.msgInfo("[ANT]:Finished target '" + arg0.getTarget() + "'");
            }

            @Override
            public void messageLogged(BuildEvent arg0) {
                // System.out.println(arg0.getMessage());

            }

            @Override
            public void buildStarted(BuildEvent arg0) {
                // System.out.println("Build Started");
            }

            @Override
            public void buildFinished(BuildEvent arg0) {
                // System.out.println(arg0.getMessage());

            }
        };

        return outListener;
    }

    /**
     * @param projectFolder
     * @return
     */
    private static String getFileset(File projectFolder) {
        String template = "<fileset dir=\"<FOLDER>\" />";

        template = template.replace("<FOLDER>", projectFolder.getAbsolutePath());

        return template;
    }

    /**
     * @param jarFile
     * @return
     */
    private static String getZipfileset(File jarFile) {
        String template = "<zipfileset dir=\"<FOLDER>\" includes=\"<FILE>\" />";

        template = template.replace("<FOLDER>", jarFile.getParentFile().getAbsolutePath());
        template = template.replace("<FILE>", jarFile.getName());

        return template;
    }
}
