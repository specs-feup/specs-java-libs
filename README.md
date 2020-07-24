# specs-java-libs
Java libraries created or extended (-Plus suffix) by SPeCS research group

# Configuring Eclipse

  1. Create an Eclipse workspace on a folder outside of the repository. The workspace is local and should not be shared.
  2. Import the configurations in the folder SupportJavaLibs/configs/
      1. Go to Window > Preferences
      2. Go to Java > Code Style > Code Templates, press "Import" and choose "codetemplates.xml"
      3. Go to Java > Code Style > Formatter, press "Import" and choose "java_code_formatter.xml".
      4. Still in Java > Code Style > Formatter, choose "Java Conventions [built-in] - SPeCS" from the "Active profile:" dropdown.
      5. Go to Java Build Path > User Libraries, choose "Import" and then press "Browse...". Select "repo.userlibraries" and then click "OK".
      6. Go to Java > Code Style > Clean Up, choose "Import" and then select "cleanup.xml" and then click "Ok".
      <!-- 6. Go to Ivy > Settings, choose "File System" in the "Ivy settings path" option and browse to "ivysettings.xml". Click "Ok" -->
  3. Import the projects you want to.
      1. For certain projects, you might need to install additional Eclipse plugins ([how to install Eclipse plugins using update site links](#installing-eclipse-plugin-using-update-site-links)), it is recommended that you install the plugins and restart Eclipse before importing the projects. Currently, the used plugins are:
        * JavaCC - http://eclipse-javacc.sourceforge.net/
        * IvyDE - http://www.apache.org/dist/ant/ivyde/updatesite: Install Apache Ivy (tested with 2.4) and Apache IvyDE (tested with 2.2). After installing IvyDE you have to define de ivy settings file:
            *  Go to Window > Preferences > Ivy > Setting > Ivy Setting path > press "Workspace" and choose "ivysettings.xml"
      2. Import projects from Git. Select "Import...->Git->Projects from Git->Existing Local Repository. Here you add the repository, by selecting the folder where you cloned this repository. The default option is "Import Eclipse Projects", do next, and choose the projects you want to import.

# Installing Eclipse plugin using update site links

 1. Go to Help > Install New Software...
 2. Click "Add..."
 3. Choose a name (e.g., JavaCC), add the location of the plugin (e.g., http://eclipse-javacc.sourceforge.net/) and click "Add"
 4. The entry should now appear in the "Work with:" dropdown, choose the plugin
 5. Check the boxes that appear in the area below "Work with", click "Next" and follow the instructions

