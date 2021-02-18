/*
 * Copyright 2009 SPeCS Research Group.
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
package pt.up.fe.specs.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import pt.up.fe.specs.util.collections.SpecsList;
import pt.up.fe.specs.util.io.PathFilter;
import pt.up.fe.specs.util.providers.ResourceProvider;
import pt.up.fe.specs.util.utilities.ProgressCounter;

/**
 * Methods for quick and simple manipulation of files, folders and other input/output related operations.
 *
 * @author Joao Bispo
 */
public class SpecsIo {

    private static final Set<Character> ILLEGAL_FILENAME_CHARS = new HashSet<>(
            Arrays.asList('\\', '/', ':', '*', '?', '"', '<', '>', '|'));

    private static final String UNIVERSAL_PATH_SEPARATOR = ";";

    /**
     * Helper class for methods that copy resources.
     *
     * @author JoaoBispo
     *
     */
    public static class ResourceCopyData {
        private final File writtenFile;
        private final boolean newFile;

        public ResourceCopyData(File writtenFile, boolean newFile) {
            this.writtenFile = writtenFile;
            this.newFile = newFile;
        }

        public File getFile() {
            return writtenFile;
        }

        public boolean isNewFile() {
            return newFile;
        }
    }

    public static String getNewline() {
        return System.lineSeparator();
    }

    //
    // DEFINITIONS
    //
    /**
     * Default CharSet used in file operations.
     */
    final public static String DEFAULT_CHAR_SET = "UTF-8";

    final private static String DEFAULT_EXTENSION_SEPARATOR = ".";
    // Records the name of the last file appended
    // private static String lastAppeddedFileAbsolutePath = "";
    private static String lastAppeddedFileCanonicalPath = "";

    private final static String DEFAULT_SEPARATOR = ".";

    private final static char DEFAULT_FOLDER_SEPARATOR = '/';

    public static String getDefaultExtensionSeparator() {
        return SpecsIo.DEFAULT_EXTENSION_SEPARATOR;
    }

    /**
     * Helper method which accepts a parent File and a child String as input.
     *
     * @param parentFolder
     * @param child
     * @return
     */
    public static File mkdir(File parentFolder, String child) {
        return mkdir(new File(parentFolder, child));
    }

    /**
     * Helper method which accepts a File as input.
     *
     * @param folder
     * @return
     */
    public static File mkdir(File folder) {
        return mkdir(folder.getPath());
    }

    /**
     * Given a string representing a filepath to a folder, returns a File object representing the folder.
     *
     * <p>
     * If the folder doesn't exist, the method will try to create the folder and necessary sub-folders. If an error
     * occurs (ex.: the folder could not be created, the given path does not represent a folder), throws an exception.
     *
     * *
     * <p>
     * If the given folderpath is an empty string, returns the current working folder.
     *
     * <p>
     * If the method returns it is guaranteed that the folder exists.
     *
     * @param folderpath
     *            String representing a folder.
     * @return a File object representing a folder, or null if unsuccessful.
     */
    public static File mkdir(String folderpath) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (folderpath == null) {
            // Logger.getLogger(IoUtils.class.getName()).warning("Input 'folderpath' is null.");
            throw new RuntimeException("Input 'folderpath' is null");
            // LoggingUtils.msgWarn("Input 'folderpath' is null.");
            // return null;
        }

        // Check if folderpath is empty
        // if (folderpath.isEmpty()) {
        if (SpecsStrings.isEmpty(folderpath)) {
            return SpecsIo.getWorkingDir();
        }

        // Create File object
        File folder = new File(folderpath);

        // The following checks where done in that sequence to avoid having
        // more than one level of if-nesting.

        // Check if File is a folder
        final boolean isFolder = folder.isDirectory();
        if (isFolder) {
            return folder;
        }

        // Check if is a file. If true, stop
        final boolean folderExists = folder.isFile();
        if (folderExists) {
            // folder = folder.getParentFile();
            // Logger.getLogger(IoUtils.class.getName()).log(Level.WARNING,
            // "Path '" + folderpath + "' exists, but " +
            // "doesn''t represent a folder.");
            throw new RuntimeException("Path '" + folderpath + "' exists, but " + "doesn't represent a folder");
            // LoggingUtils.msgInfo("Path '" + folderpath + "' exists, but " + "doesn't represent a folder.");
            // return null;
        }

        // Try to create folder.
        final boolean folderCreated = folder.mkdirs();
        if (folderCreated) {
            try {
                SpecsLogs.msgLib("Folder created (" + folder.getCanonicalPath() + ").");
            } catch (IOException ex) {
                SpecsLogs.msgLib("Folder created (" + folder.getAbsolutePath() + ").");
            }
            return folder;

        }

        // Check if folder exists
        if (folder.exists()) {
            SpecsLogs.msgWarn("Folder created (" + folder.getAbsolutePath() + ") but 'mkdirs' returned false.");
            return folder;
        }

        // Couldn't create folder
        // Logger.getLogger(IoUtils.class.getName()).
        // log(Level.WARNING,"Path '" + folderpath+"' does not exist and " +
        // "could not be created.");
        throw new RuntimeException("Path '" + folderpath + "' does not exist and " + "could not be created");
        // LoggingUtils.msgWarn("Path '" + folderpath + "' does not exist and " + "could not be created.");
        // return null;

    }

    /**
     * Method to create a File object for a file which should exist.
     *
     * <p>
     * The method does some common checks (ex.: if the file given by filepath exists, if it is a file). If any of the
     * checks fail, throws an exception.
     *
     * @param filepath
     *            String representing an existing file.
     * @return a File object representing a file, or null if unsuccessful.
     */
    public static File existingFile(String filepath) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (filepath == null) {
            throw new RuntimeException("Input 'filepath' is null");
            // LoggingUtils.msgWarn("Input 'filepath' is null.");
            // return null;
        }

        // Create File object
        final File file = new File(filepath);

        // Check if File is a file
        final boolean isFile = file.isFile();
        if (isFile) {
            return file;
        }

        // Check if File exists. If true, is not a file.
        final boolean fileExists = file.exists();
        if (fileExists) {
            throw new RuntimeException("Path '" + filepath + "' exists, but doesn't " + "represent a file");
            // LoggingUtils.msgWarn("Path '" + filepath + "' exists, but doesn't " + "represent a file.");
            // return null;
        }

        // File doesn't exist, return null.
        throw new RuntimeException("Path '" + filepath + "' does not exist");
        // LoggingUtils.msgWarn("Path '" + filepath + "' does not exist.");
        // return null;

    }

    /**
     * Helper method that receives a String.
     *
     * @param filename
     * @return
     */
    public static String read(String filename) {
        return read(SpecsIo.existingFile(filename));
    }

    /**
     * Given a File object, returns a String with the contents of the file.
     *
     * <p>
     * If an error occurs (ex.: the File argument does not represent a file) returns null and logs the cause.
     *
     * @param file
     *            a File object representing a file.
     * @return a String with the contents of the file.
     */
    public static String read(File file) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (file == null) {
            SpecsLogs.msgInfo("Input 'file' is null.");
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        // Try to read the contents of the file into the StringBuilder

        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                SpecsIo.DEFAULT_CHAR_SET))) {

            // Read first character. It can't be cast to "char", otherwise the
            // -1 will be converted in a character.
            // First test for -1, then cast.
            int intChar = bufferedReader.read();
            while (intChar != -1) {
                char character = (char) intChar;
                stringBuilder.append(character);
                intChar = bufferedReader.read();
            }

        } catch (FileNotFoundException ex) {
            SpecsLogs.msgInfo("FileNotFoundException: " + ex.getMessage());
            return null;

        } catch (IOException ex) {
            SpecsLogs.msgInfo("IOException: " + ex.getMessage());
            return null;
        }

        return stringBuilder.toString();
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                SpecsLogs.msgWarn("Problem while closing resource", e);
            }
        }
    }

    /**
     * Reads a stream to a String. The stream is closed after it is read.
     *
     * @param inputStream
     * @return
     */
    public static String read(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        // Try to read the contents of the input stream into the StringBuilder
        // Using 'finally' style 2 as described in http://www.javapractices.com/topic/TopicAction.do?Id=25
        try (final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,
                SpecsIo.DEFAULT_CHAR_SET))) {

            // Read first character. It can't be cast to "char", otherwise the
            // -1 will be converted in a character.
            // First test for -1, then cast.
            int intChar = bufferedReader.read();
            while (intChar != -1) {
                char character = (char) intChar;

                stringBuilder.append(character);

                intChar = bufferedReader.read();
            }

        } catch (FileNotFoundException ex) {
            SpecsLogs.msgWarn("FileNotFoundException", ex);
            stringBuilder = new StringBuilder(0);
        } catch (IOException ex) {
            SpecsLogs.msgWarn("IOException", ex);
            stringBuilder = new StringBuilder(0);
        }

        return stringBuilder.toString();
    }

    /**
     * Given a File object and a String, writes the contents of the String in the file, overwriting everything that was
     * previously in that file.
     *
     * <p>
     * If successful, returns true. If an error occurs (ex.: the File argument does not represent a file) returns false,
     * logs the cause and nothing is written.
     *
     * @param file
     *            a File object representing a file.
     * @param contents
     *            a String with the content to write
     * @return true if write is successful. False otherwise.
     */
    public static boolean write(File file, String contents) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (file == null) {
            SpecsLogs.msgWarn("Input 'file' is null.");
            return false;
        }

        if (contents == null) {
            SpecsLogs.msgWarn("Input 'contents' is null.");
            return false;
        }

        return writeAppendHelper(file, contents, false);
    }

    /**
     * Given a File object and a String, writes the contents of the String at the end of the file. If successful,
     * returns true.
     *
     * <p>
     * If an error occurs (ex.: the File argument does not represent a file) returns false, logs the cause and nothing
     * is written.
     *
     * @param file
     *            a File object representing a file.
     * @param contents
     *            a String with the content to write
     * @return true if write is successful. False otherwise.
     */
    public static boolean append(File file, String contents) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (file == null) {
            SpecsLogs.warn("Input 'file' is null.");
            // Logger.getLogger(IoUtils.class.getName()).warning("Input 'file' is null.");
            return false;
        }

        if (contents == null) {
            SpecsLogs.warn("Input 'contents' is null.");
            return false;
        }

        return writeAppendHelper(file, contents, true);
    }

    /**
     * Method shared among write and append.
     *
     * <p>
     * Using 'finally' style 2 as described in <a href="http://www.javapractices.com/topic/TopicAction.do?Id=25">Java
     * Practices: Finally and Catch</a>.
     *
     * @param file
     * @param contents
     * @param append
     * @return
     */
    private static boolean writeAppendHelper(File file, String contents, boolean append) {
        boolean isSuccess = true;

        // Create folders
        if (file.getParent() != null) {
            SpecsIo.mkdir(file.getParent());
        }

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, append),
                SpecsIo.DEFAULT_CHAR_SET))) {

            if (!file.exists()) {
                boolean success = file.createNewFile();
                if (!success) {
                    SpecsLogs.getLogger().warning("Could not create file '" + file + "'");
                    return false;
                }
            }

            if (!file.isFile()) {
                SpecsLogs.getLogger().warning("Path '" + file + "' is not a file.");
                return false;
            }

            // Adapt contents to system newline
            // if (!contents.contains("\r")) {
            // contents = contents.replace("\n", System.getProperty("line.separator"));
            // }

            writer.write(contents, 0, contents.length());

            // Inform about the operation
            if (append) {
                // Check if this is the same file as the last time
                String filePath = file.getCanonicalPath();
                if (!filePath.equals(SpecsIo.lastAppeddedFileCanonicalPath)) {
                    SpecsIo.lastAppeddedFileCanonicalPath = filePath;
                    SpecsLogs.msgLib("Appending file (" + file.getCanonicalPath() + ").");
                }
            } else {
                SpecsLogs.msgLib("File written (" + file.getCanonicalPath() + ").");
            }

        } catch (IOException ex) {
            SpecsLogs.warn("Problems when accessing file '" + file.getPath()
                    + "'. Check if folder exists before writing the file.", ex);
            // SpecsLogs.msgWarn(ex);
            // SpecsLogs.msgInfo("Problems when accessing file '" + file.getPath()
            // + "'. Check if folder exists before writing the file.");
            isSuccess = false;

        }

        return isSuccess;
    }

    /**
     * Given a File object, loads the contents of the file into a Java Properties object.
     *
     * <p>
     * If an error occurs (ex.: the File argument does not represent a file, could not load the Properties object)
     * returns null and logs the cause.
     *
     * @deprecated
     * @param file
     *            a File object representing a file.
     * @return If successfull, a Properties objects with the contents of the file. Null otherwise.
     */
    /*
    public static Properties loadProperties(File file) {
    	// Check null argument. If null, it would raise and exception and stop
    	// the program when used to create the File object.
    	if (file == null) {
    		Logger.getLogger(IoUtils.class.getName()).warning(
    				"Input 'file' is null.");
    		return null;
    	}
    
    	try {
    		Properties props = new Properties();
    		props.load(new java.io.FileReader(file));
    		return props;
    	} catch (IOException ex) {
    		Logger.getLogger(IoUtils.class.getName()).log(Level.WARNING,
    				"IOException: " + ex.getMessage());
    	}
    
    	return null;
    }
     */

    /**
     * Given a filename, removes the extension suffix and the separator.
     *
     * <p>
     * Example: <br>
     * filename: 'readme.txt' <br>
     * separator: '.' <br>
     * result: 'readme'
     *
     * @param filename
     *            a string
     * @param separator
     *            the extension separator
     * @return the name of the file without the extension and the separator
     */
    public static String removeExtension(String filename, String separator) {
        int extIndex = filename.lastIndexOf(separator);
        if (extIndex < 0) {
            return filename;
        }
        return filename.substring(0, extIndex);
    }

    /**
     * Given a filename, removes the extension suffix and the separator.
     *
     * <p>
     * Uses '.' as default separator
     *
     * <p>
     * Example: <br>
     * filename: 'readme.txt' <br>
     * result: 'readme'
     *
     * @param filename
     *            a string
     * @return the name of the file without the extension and the separator
     */
    public static String removeExtension(String filename) {
        return removeExtension(filename, SpecsIo.DEFAULT_EXTENSION_SEPARATOR);
    }

    /**
     * Helper method which receives a file.
     *
     * @param file
     * @return
     */
    public static String removeExtension(File file) {
        return removeExtension(file.getName());
    }

    /**
     * Note: by default follows symlinks.
     *
     * @param path
     *            a File representing a folder or a file.
     *
     * @param extensions
     *            a set of strings
     *
     * @return all the files inside the given folder, excluding other folders, that have a certain extension as
     *         determined by the set.
     */
    public static List<File> getFilesRecursive(File path, Collection<String> extensions) {

        return getFilesRecursive(path, extensions, true);
    }

    public static List<File> getFilesRecursive(File folder, Collection<String> extensions, boolean followSymlinks) {
        return getFilesRecursive(folder, extensions, followSymlinks, path -> false);
    }

    /**
     * @param folder
     *            a File representing a folder or a file.
     *
     * @param extensions
     *            a set of strings
     *
     * @param followSymlinks
     *            whether to follow symlinks
     *
     * @param cutoffFolders
     *
     *
     * @return all the files inside the given folder, excluding other folders, that have a certain extension as
     *         determined by the set.
     */
    public static List<File> getFilesRecursive(File path, Collection<String> extensions, boolean followSymlinks,
            Predicate<File> cutoffFolders) {

        // Make extensions lower-case
        Collection<String> lowerCaseExtensions = extensions.stream().map(ext -> ext.toLowerCase())
                .collect(Collectors.toSet());

        List<File> files = new ArrayList<>();

        getFilesRecursivePrivate(path, lowerCaseExtensions, followSymlinks, cutoffFolders, files);

        return files;
    }

    private static void getFilesRecursivePrivate(File path, Collection<String> extensions, boolean followSymlinks,
            Predicate<File> cutoffFolders, List<File> foundFiles) {

        // List<File> fileList = new ArrayList<>();
        //
        // for (String extension : extensions) {
        // List<File> files = getFilesRecursive(folder, extension, followSymlinks);
        // fileList.addAll(files);
        // }
        //
        // return fileList;

        // if (!path.isDirectory()) {
        if (!path.exists()) {
            SpecsLogs.debug(() -> "Path '" + path + "' does not exist.");
            return;
        }

        // Ignore path if is symlink
        if (!followSymlinks && Files.isSymbolicLink(path.toPath())) {
            return;
        }

        if (path.isFile()) {
            // Test for extension, if set is not empty
            if (!extensions.isEmpty()) {
                String extension = SpecsIo.getExtension(path).toLowerCase();
                if (!extensions.contains(extension)) {
                    return;
                }
            }

            foundFiles.add(path);

            return;
            // if (SpecsIo.getExtension(path).equals(extension)) {
            // return Arrays.asList(path);
            // }
            //
            // return Collections.emptyList();
        }

        if (!path.isDirectory()) {
            SpecsLogs.debug(() -> "Ignoring path that is neither file or folder: " + path);
            return;
        }

        // Must be a folder from this point on
        SpecsCheck.checkArgument(path.isDirectory(), () -> "Expected file to be a folder: " + path);

        // If it should be cut-off, stop processing of this folder
        if (cutoffFolders.test(path)) {
            return;
        }

        // Recursively add files of folder
        File[] children = path.listFiles();
        if (children == null) {
            children = new File[0];
            SpecsLogs.debug("Could not list files of path '" + path.getAbsolutePath() + "'");
        }
        // File[] childrenChecked = children != null ? children : new File[0];
        for (File child : children) {
            getFilesRecursivePrivate(child, extensions, followSymlinks, cutoffFolders, foundFiles);
        }

        /*
        this.extension = extension;
        this.separator = SpecsIo.DEFAULT_EXTENSION_SEPARATOR;
        this.followSymlinks = followSymlinks;
        }
        
        @Override
        public boolean accept(File dir, String name) {
        
        String suffix = separator + extension.toLowerCase();
        
        if (!followSymlinks) {
        
            File f = new File(dir, name);
        
            // Fail if this is a symlink.
            if (Files.isSymbolicLink(f.toPath())) {
                return false;
            }
        }
        
        return name.toLowerCase().endsWith(suffix);
        */
        /*
        // Process files inside folder
        for (File file : path.listFiles()) {
        
        
        
            // Process folder
            if (file.isDirectory()) {
                // If it should be cut-off, stop processing of this folder
                if (cutoffFolders.test(file)) {
                    continue;
                }
        
                // Recursively add files of folder
                getFilesRecursivePrivate(file, extensions, followSymlinks, cutoffFolders, foundFiles);
                continue;
            }
        
            //
            // if (!followSymlinks) {
            //
            // File f = new File(dir, name);
            //
            // // Fail if this is a symlink.
            // if (Files.isSymbolicLink(f.toPath())) {
            // return false;
            // }
            // }
            //
            // return name.toLowerCase().endsWith(suffix);
        
            String extension = SpecsIo.getExtension(file).toLowerCase();
        
            if(extensions.contains(o))
        
            // Add files that pass the extension and symlink rules
            // String suffix = SpecsIo.DEFAULT_EXTENSION_SEPARATOR + extension.toLowerCase();
        }
        */
        /*
        List<File> fileList = new ArrayList<>();
        
        ExtensionFilter filter = new ExtensionFilter(extension, followSymlinks);
        File[] files = path.listFiles(filter);
        
        fileList.addAll(Arrays.asList(files));
        
        // directories
        files = path.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
        
                // Ignore directory if is symlink
                if (!followSymlinks && Files.isSymbolicLink(file.toPath())) {
                    continue;
                }
        
                fileList.addAll(getFilesRecursive(file, extension));
            }
        }
        
        return fileList;
        */
    }

    /**
     * Note: by default follows symlinks.
     *
     * @param folder
     *            a File representing a folder or a file.
     * @param extension
     *            a string
     * @return all the files inside the given folder, excluding other folders, that have a certain extension.
     */
    public static List<File> getFilesRecursive(File folder, String extension) {
        return getFilesRecursive(folder, Arrays.asList(extension), true, path -> false);
        // return getFilesRecursive(folder, extension, true);
    }

    /**
     * @param path
     *            a File representing a folder or a file.
     *
     * @param extension
     *            a string
     *
     * @param followSymlinks
     *            whether to follow symlinks
     *
     * @return all the files inside the given folder, excluding other folders, that have a certain extension.
     */
    /*
    public static List<File> getFilesRecursive(File path, String extension, boolean followSymlinks) {
    
        // if (!path.isDirectory()) {
        if (!path.exists()) {
            SpecsLogs.msgWarn("Path '" + path + "' does not exist.");
            return null;
        }
    
        if (path.isFile()) {
            if (SpecsIo.getExtension(path).equals(extension)) {
                return Arrays.asList(path);
            }
    
            return Collections.emptyList();
        }
    
        List<File> fileList = new ArrayList<>();
    
        ExtensionFilter filter = new ExtensionFilter(extension, followSymlinks);
        File[] files = path.listFiles(filter);
    
        fileList.addAll(Arrays.asList(files));
    
        // directories
        files = path.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
    
                // Ignore directory if is symlink
                if (!followSymlinks && Files.isSymbolicLink(file.toPath())) {
                    continue;
                }
    
                fileList.addAll(getFilesRecursive(file, extension));
            }
        }
    
        return fileList;
    }
    
    */

    /**
     * Note: by default this follows symlinks.
     *
     * @param path
     *            a File representing a path.
     *
     * @return all the files inside the given folder, excluding other folders.
     */
    public static List<File> getFilesRecursive(File path) {
        // return getFilesRecursive(path, Collections.emptySet(), true, folder -> false);
        return getFilesRecursive(path, true);
    }

    /**
     *
     *
     * @param path
     *            a File representing a path.
     *
     * @param followSymlinks
     *            whether to follow symlinks (both files and directories)
     *
     * @return all the files inside the given path, excluding other folders.
     */
    public static List<File> getFilesRecursive(File path, boolean followSymlinks) {
        return getFilesRecursive(path, Collections.emptySet(), followSymlinks, folder -> false);
    }
    /*
    public static List<File> getFilesRecursive(File path, boolean followSymlinks) {
    
        // Special case: path is a single file
        if (path.isFile()) {
            return Arrays.asList(path);
        }
    
        List<File> fileList = new ArrayList<>();
        File[] files = path.listFiles();
    
        if (files == null) {
            // Not a folder
            return fileList;
        }
    
        for (File file : files) {
    
            // Ignore file if is symlink
            if (!followSymlinks && Files.isSymbolicLink(file.toPath())) {
                continue;
            }
    
            if (file.isFile()) {
                fileList.add(file);
            }
        }
    
        for (File file : files) {
    
            if (file.isDirectory()) {
    
                // Ignore directory if is symlink
                if (!followSymlinks && Files.isSymbolicLink(file.toPath())) {
                    continue;
                }
    
                fileList.addAll(getFilesRecursive(file, followSymlinks));
            }
        }
    
        return fileList;
    }
    */

    /**
     * @param folder
     *            a File representing a folder.
     * @return all the folders inside the given folder, excluding other files.
     */
    public static List<File> getFolders(File folder) {
        List<File> fileList = new ArrayList<>();
        File[] files = folder.listFiles();

        if (files == null) {
            return fileList;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                fileList.add(file);
            }
        }

        return fileList;
    }

    /**
     * Do a depth-first listing of all folders inside the given folder.
     *
     * @param folder
     *            a File representing a folder.
     * @return all the folders inside the given folder, excluding other files.
     */
    public static List<File> getFoldersRecursive(File folder) {
        List<File> folderList = new ArrayList<>();

        getFoldersRecursiveHelper(folder, folderList);

        return folderList;
    }

    private static void getFoldersRecursiveHelper(File folder, List<File> folderList) {
        List<File> childFolders = getFolders(folder);

        for (File childFolder : childFolders) {
            getFoldersRecursiveHelper(childFolder, folderList);
        }

        folderList.addAll(childFolders);
    }

    /**
     * @param path
     *            a File representing an existing path.
     * @return if path is a folder, returns all the files inside the given folder, excluding other folders. Otherwise,
     *         returns a list with the given path
     */
    public static SpecsList<File> getFiles(File path) {
        return SpecsList.convert(getFilesPrivate(path));
    }

    private static List<File> getFilesPrivate(File path) {
        // Check if path exists
        if (!path.exists()) {
            SpecsLogs.msgInfo("Given path '" + path + "' does not exist");
            return Collections.emptyList();
        }

        // Check if given File is a single file
        if (path.isFile()) {
            return Arrays.asList(path);
        }

        List<File> fileList = new ArrayList<>();
        File[] files = path.listFiles();

        if (files == null) {
            return fileList;
        }

        for (File file : files) {
            if (file.isFile()) {
                fileList.add(file);
            }
        }

        // Sort files, to keep same order across platforms
        Collections.sort(fileList);

        return fileList;
    }

    public static List<File> getFilesWithExtension(List<File> files, String extension) {
        Set<String> extensions = SpecsFactory.newHashSet();
        extensions.add(extension);

        return getFilesWithExtension(files, extensions);
    }

    public static List<File> getFilesWithExtension(List<File> files, Collection<String> extensions) {
        List<File> mFiles = new ArrayList<>();
        for (File file : files) {
            String fileExtension = SpecsStrings.getExtension(file.getName());
            if (fileExtension == null) {
                continue;
            }

            // if (!fileExtension.equals(extension)) {
            if (!extensions.contains(fileExtension)) {
                continue;
            }

            mFiles.add(file);
        }

        return mFiles;
    }

    /**
     * Convenience method which overwrites files by default
     *
     * @param source
     * @param destination
     * @return
     */
    public static List<File> copyFolder(File source, File destination, boolean verbose) {
        return copyFolder(source, destination, verbose, true);
    }

    /**
     * Copies the contents of a folder to another folder.
     *
     * @param source
     * @param destination
     * @param verbose
     * @param overwrite
     */
    public static List<File> copyFolder(File source, File destination, boolean verbose, boolean overwrite) {
        // public static void copyFolder(File source, File destination, boolean verbose, boolean overwrite) {
        if (!source.isDirectory()) {
            throw new RuntimeException("Source '" + source + "' is not a folder");
        }

        destination = SpecsIo.mkdir(destination);
        if (!destination.isDirectory()) {
            throw new RuntimeException("Destination '" + destination + "' is not a folder");
        }

        // Get all files in source
        List<File> files = SpecsIo.getFilesRecursive(source);
        List<File> copiedFiles = new ArrayList<>();
        for (File file : files) {
            // Get destination file
            String relativePath = SpecsIo.getRelativePath(file, source);

            File destFile = new File(destination, relativePath);

            // Check if destination file exists
            // If overwrite disabled, skip file
            if (!overwrite && destFile.isFile()) {
                continue;
            }

            SpecsIo.copy(file, destFile, verbose);
            copiedFiles.add(destFile);
        }

        return copiedFiles;
    }

    public static boolean copy(File source, File destination) {
        return copy(source, destination, true);
    }

    /**
     * Copies the specified file to the specified location.
     *
     * <p>
     * If the destination is an existing folder, copies the file to a file of the same name on that folder (method
     * 'safeFolder' can be used to create the destination folder, before passing it to the function).
     *
     * <p>
     * If verbose is true, warns when overwriting files.
     *
     * @param source
     * @param destination
     * @param verbose
     * @return
     */
    public static boolean copy(File source, File destination, boolean verbose) {
        // Check if source is a file
        if (!source.isFile()) {
            SpecsLogs.msgInfo("Copy: source file '" + source + "' does not exist.");
            return false;
        }

        // Check if destination is a folder
        // If true, create a destination file with the same name as the source file.
        if (destination.isDirectory()) {
            destination = new File(destination, source.getName());
        }

        // Check if destination already exists
        if (destination.isFile() && verbose) {
            SpecsLogs.msgInfo("Copy: overwriting file '" + destination + "'.");
        }

        // Using 'finally' style 2 as described in http://www.javapractices.com/topic/TopicAction.do?Id=25
        boolean success = true;
        try (InputStream in = new FileInputStream(source)) {

            return copy(in, destination);

        } catch (FileNotFoundException ex) {
            SpecsLogs.msgWarn("Could not find file", ex);
            success = false;
        } catch (IOException e) {
            SpecsLogs.msgWarn("Failed to close stream", e);
            success = false;
        }

        return success;
    }

    /**
     * Copies the contents of the source stream to the destination file.
     *
     * <p>
     * After copy, the source stream is closed.
     *
     * @param source
     * @param destination
     * @return
     */
    public static boolean copy(InputStream source, File destination) {
        // Preconditions.checkArgument(source != null);
        // Preconditions.checkArgument(destination != null);

        boolean success = true;

        File f2 = destination;

        // Create folders for f2
        File parentFile = f2.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }

        // Using 'finally' style 2 as described in http://www.javapractices.com/topic/TopicAction.do?Id=25
        try (OutputStream out = new FileOutputStream(f2); InputStream in = source) {

            // For Overwrite the file.

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            SpecsLogs.msgLib("Copied stream to file '" + destination.getPath() + "'.");

        } catch (IOException e) {
            SpecsLogs.msgWarn("IoException while copying stream to file '" + destination + "'", e);
            success = false;
        }

        return success;
    }

    /**
     * Helper method which enables recursion by default.
     *
     * @param folder
     * @return true in case the operation was successful (could delete all files, or the folder does not exit)
     */
    public static boolean deleteFolderContents(File folder) {
        return deleteFolderContents(folder, true);
    }

    public static boolean deleteFolderContents(File folder, boolean recursive) {
        if (!folder.exists()) {
            return true;
        }

        if (!folder.isDirectory()) {
            SpecsLogs.msgWarn("Not a folder");
            return false;
        }

        SpecsLogs.msgLib("Deleting contents of folder '" + folder + "'");

        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                // Skip file if recursion off
                if (!recursive) {
                    continue;
                }

                deleteFolderContents(file);
            }

            boolean deleted = file.delete();
            if (deleted) {
                SpecsLogs.msgLib("Deleted '" + file + "'");
            } else {
                SpecsLogs.msgLib("Could not delete '" + file + "'");
            }
        }

        return true;
    }

    /**
     * Helper method which accepts a ResourceProvider.
     *
     * @param resource
     * @return
     */
    public static String getResource(ResourceProvider resource) {
        return getResource(resource.getResource());
    }

    /**
     * Given the name of a resource, returns a String with the contents of the resource.
     *
     * @param resourceName
     * @return
     */
    public static String getResource(String resourceName) {
        try (InputStream inputStream = SpecsIo.resourceToStream(resourceName)) {
            if (inputStream == null) {
                SpecsLogs.getLogger().warning("Could not get InputStream.");
                return null;
            }

            return SpecsIo.read(inputStream);

        } catch (IOException e) {
            SpecsLogs.msgWarn("Could not open resource '" + resourceName + "'", e);
            return "";
        }
    }

    /**
     * Returns the last name of the resource.
     *
     * <p>
     * Example, if input is 'package/resource.ext', returns 'resource.ext'.
     *
     * @param resource
     * @return
     */
    public static String getResourceName(String resource) {
        // Try backslash
        int indexOfLastSlash = resource.lastIndexOf('/');

        // Try slash
        if (indexOfLastSlash == -1) {
            indexOfLastSlash = resource.lastIndexOf('\\');
        }

        return resource.substring(indexOfLastSlash + 1);
    }

    public static InputStream resourceToStream(ResourceProvider resource) {
        return resourceToStream(resource.getResource());
    }

    public static boolean hasResource(String resourceName) {
        return hasResource(SpecsIo.class, resourceName);
    }

    public static boolean hasResource(Class<?> aClass, String resourceName) {
        return aClass.getClassLoader().getResource(resourceName) != null;
    }

    public static InputStream resourceToStream(String resourceName) {
        // Obtain the current classloader
        ClassLoader classLoader = SpecsIo.class.getClassLoader();

        // Load the file as a resource
        InputStream stream = classLoader.getResourceAsStream(resourceName);
        if (stream == null) {
            SpecsLogs.msgWarn("Could not load resource '" + resourceName + "'.");

        }
        return stream;

    }

    public static boolean extractZipResource(String resource, File folder) {
        try (InputStream stream = SpecsIo.class.getResourceAsStream(resource)) {
            return extractZipResource(stream, folder);
        } catch (IOException e) {
            throw new RuntimeException("Could not unzip resource '" + resource + "'", e);
        }

    }

    public static boolean extractZip(File zipFile, File folder) {
        try (InputStream stream = new FileInputStream(zipFile)) {
            return extractZipResource(stream, folder);
        } catch (IOException e) {
            throw new RuntimeException("Could not unzip file '" + zipFile + "'", e);
        }

    }

    // public static boolean extractZip(File filename, File folder) {
    // try (InputStream stream = new FileInputStream(filename)) {
    // return extractZipResource(stream, folder);
    // } catch (IOException e) {
    // throw new RuntimeException("Could not unzip file '" + filename + "'", e);
    // }
    //
    // }

    public static boolean extractZipResource(InputStream resource, File folder) {
        boolean success = true;
        if (!folder.isDirectory()) {
            SpecsLogs.msgWarn("Given folder '" + folder.getPath() + "' does not exist.");
            return false;
        }

        // Using 'finally' style 2 as described in http://www.javapractices.com/topic/TopicAction.do?Id=25
        // try (ZipInputStream zis = new ZipInputStream(IoUtils.class.getResourceAsStream(resource))) {
        try (ZipInputStream zis = new ZipInputStream(resource)) {

            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                // If folder, create it
                if (entry.isDirectory()) {
                    File zippedFolder = SpecsIo.mkdir(folder, entry.getName());
                    SpecsLogs.msgInfo("Create folder '" + zippedFolder + "'");
                    continue;
                }

                File outFile = new File(folder, entry.getName());

                // Make sure complete folder structure exists
                // IoUtils.safeFolder(outFile.getParent());

                SpecsLogs.msgInfo("Unzipping '" + outFile.getPath() + "'");
                unzipFile(zis, outFile);
            }

        } catch (IOException ex) {
            SpecsLogs.msgWarn("IoException while unzipping to folder '" + folder + "'", ex);
            success = false;
        }

        return success;
    }

    /**
     * Reads the contents of ZipInputStream at the current position to a File.
     *
     * <p>
     * Does not close the stream, so that it can be used again for the remaining zipped files.
     *
     * @param zis
     * @param outFile
     * @throws FileNotFoundException
     * @throws IOException
     */
    private static void unzipFile(ZipInputStream zis, File outFile) throws FileNotFoundException, IOException {
        int size;
        byte[] buffer = new byte[2048];

        // Make sure folder to output file exists
        SpecsIo.mkdir(outFile.getParentFile());

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile), buffer.length);) {

            while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, size);
            }
            bos.flush();
        }
    }

    /**
     * Converts an object to an array of bytes.
     *
     * @param obj
     * @return
     */
    public static byte[] getBytes(Object obj) {

        try (ObjectOutputStream oos = new ObjectOutputStream(new ByteArrayOutputStream());) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            oos.writeObject(obj);
            oos.flush();
            byte[] data = bos.toByteArray();
            return data;

        } catch (IOException ex) {
            SpecsLogs.msgWarn("IOException while reading bytes from object '" + obj + "'", ex);
            return null;
        }

    }

    /**
     * Recovers a String List from an array of bytes.
     *
     * @param bytes
     * @return
     */
    public static Object getObject(byte[] bytes) {

        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {

            Object readObject = ois.readObject();
            return readObject;

        } catch (ClassNotFoundException ex) {
            SpecsLogs.getLogger().warning(ex.toString());
            return null;
        } catch (IOException ex) {
            SpecsLogs.getLogger().warning(ex.toString());
            return null;
        }

    }

    /**
     * Serializes an object to a file.
     *
     * @param file
     * @param serializableObject
     * @return
     */
    public static boolean writeObject(File file, Object serializableObject) {
        // Transform object into byte array

        try (ObjectOutputStream obj_out = new ObjectOutputStream(new FileOutputStream(file))) {

            obj_out.writeObject(serializableObject);
            SpecsLogs.msgLib("Object written to file '" + file + "'.");

        } catch (IOException ex) {
            SpecsLogs.msgWarn("IOException while writing an object to file '" + file + "'", ex);
            return false;
        }

        return true;
    }

    /**
     * Deserializes an object from a file.
     *
     * @param file
     * @return
     */
    public static Object readObject(File file) {
        Object recovedObject = null;

        try (FileInputStream stream = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(stream);) {

            recovedObject = in.readObject();
            return recovedObject;

        } catch (FileNotFoundException ex) {
            SpecsLogs.msgWarn(ex.toString());
        } catch (IOException ex) {
            SpecsLogs.msgWarn(ex.toString());
        } catch (ClassNotFoundException ex) {
            SpecsLogs.msgWarn(ex.toString());
        }

        return null;

    }

    public static byte[] readAsBytes(File file) {
        int numBytes = (int) file.length();
        return readAsBytes(file, numBytes);
    }

    public static byte[] readAsBytes(File file, int numBytes) {
        // Using 'finally' style 2 as described in http://www.javapractices.com/topic/TopicAction.do?Id=25
        try (InputStream inStream = new FileInputStream(file)) {

            byte[] data = new byte[numBytes];
            inStream.read(data);
            return data;

        } catch (Exception ex) {
            SpecsLogs.msgWarn("Exception while reading bytes from file '" + file + "'", ex);
        }

        return null;
    }

    /**
     * When we don't know the size of the input stream, read until the stream is empty.
     *
     * <p>
     * Closes the stream after reading.
     *
     * @param inStream
     * @return
     */
    public static byte[] readAsBytes(InputStream inStream) {

        List<Byte> bytes = SpecsFactory.newArrayList();

        // Using 'finally' style 2 as described in http://www.javapractices.com/topic/TopicAction.do?Id=25
        try {
            try {
                int aByte = -1;
                while ((aByte = inStream.read()) != -1) {
                    bytes.add(Byte.valueOf((byte) aByte));
                }
            } finally {
                // inStream.read(data);
                inStream.close();
            }

            byte[] byteArray = new byte[bytes.size()];
            for (int i = 0; i < byteArray.length; i++) {
                byteArray[i] = bytes.get(i);
            }

            return byteArray;

        } catch (FileNotFoundException ex) {
            SpecsLogs.msgWarn("File not found", ex);
        } catch (IOException ex) {
            SpecsLogs.msgWarn("IoExpection", ex);
        }
        /*
        finally {
        try {
        	inStream.close();
        } catch (IOException ex) {
        	LoggingUtils.msgWarn("Exception while closing stream.", ex);
        }
        }
         */

        return null;
    }

    /**
     * Copies the given list of resources to the execution path. If the files already exist, the method does nothing.
     *
     * <p>
     * The method assumes that the resource is bundled within the application JAR.
     */
    public static void resourceCopy(Collection<String> resources) {
        for (String resource : resources) {
            resourceCopy(resource);
        }
    }

    public static File resourceCopy(String resource) {
        return resourceCopy(resource, getWorkingDir());
    }

    public static <T extends Enum<T> & ResourceProvider> void resourceCopy(Class<T> resources, File destinationFolder,
            boolean useResourcePath) {

        Preconditions.checkArgument(destinationFolder != null, "destinationFolder must not be null");

        if (resources == null) {
            throw new RuntimeException("resources must not be null");
        }

        for (T anEnum : resources.getEnumConstants()) {
            resourceCopy(anEnum.getResource(), destinationFolder, useResourcePath);
        }
    }

    public static File resourceCopy(ResourceProvider resource, File destinationFolder) {
        return resourceCopy(resource.getResource(), destinationFolder);
    }

    /**
     * Copy the given resource to the destination folder using the full path of the resource. Is destination file
     * already exists, does nothing.
     *
     * @param resource
     * @param destinationFolder
     */
    public static File resourceCopy(String resource, File destinationFolder) {
        return resourceCopy(resource, destinationFolder, true);
    }

    /**
     * Copy the given resource to the destination folder. If destination file already exists, overwrites.
     *
     * @param resource
     * @param destinationFolder
     * @param useResourcePath
     * @return
     */
    public static File resourceCopy(String resource, File destinationFolder, boolean useResourcePath) {

        return resourceCopy(resource, destinationFolder, useResourcePath, true);
    }

    /**
     * Helper method which uses the package of the ResourceProvider as the Context.
     *
     * @param resource
     * @param destinationFolder
     * @param useResourcePath
     * @return the file that was written
     */
    public static ResourceCopyData resourceCopyVersioned(ResourceProvider resource, File destinationFolder,
            boolean useResourcePath) {
        return resourceCopyVersioned(resource, destinationFolder, useResourcePath, resource.getClass());
    }

    /**
     * Copies the given resource to the destination folder. If the file already exists, uses ResourceProvider version
     * method to determine if the file should be overwritten or not.
     *
     * <p>
     * If the file already exists but no versioning information is available in the system, the file is overwritten.
     *
     * <p>
     * The method will use the package of the class indicated in 'context' as the location to store the information
     * about versioning. Keep in mind that calls using the same context will refer to the same local copy of the
     * resource.
     *
     * @param resource
     * @param destinationFolder
     * @param useResourcePath
     * @param context
     * @return the file that was written
     */
    public static ResourceCopyData resourceCopyVersioned(ResourceProvider resource, File destinationFolder,
            boolean useResourcePath, Class<?> context) {

        // Create file
        String resourceOutput = resource.getResource();
        if (!useResourcePath) {
            resourceOutput = SpecsIo.getResourceName(resourceOutput);
        }

        File destination = new File(destinationFolder, resourceOutput);

        // If file does not exist, just write and return
        if (!destination.exists()) {
            return new ResourceCopyData(resourceCopy(resource.getResource(), destinationFolder, useResourcePath, false),
                    true);
        }

        Preferences prefs = Preferences.userNodeForPackage(context);

        // Check version information
        String key = resource.getClass().getSimpleName() + "." + resource.getResource();
        String NOT_FOUND = "<NOT FOUND>";
        String version = prefs.get(key, NOT_FOUND);

        // If current version is the same as the version of the resource just return the existing file
        if (version.equals(resource.getVersion())) {
            return new ResourceCopyData(destination, false);
        }

        // Warn when there is not version information available
        if (version.equals(NOT_FOUND)) {
            SpecsLogs.msgInfo("Resource '" + resource
                    + "' already exists, but no versioning information is available. Overwriting file '" + key
                    + "' and storing information.");
        }

        // Copy resource and store version information
        File writtenFile = resourceCopy(resource.getResource(), destinationFolder, useResourcePath, true);
        prefs.put(key, resource.getVersion());

        assert writtenFile.equals(destination);

        return new ResourceCopyData(writtenFile, true);

    }

    public static File resourceCopy(String resource, File destinationFolder, boolean useResourcePath,
            boolean overwrite) {

        Preconditions.checkArgument(resource != null, "resource must not be null");
        Preconditions.checkArgument(destinationFolder != null, "destinationFolder must not be null");

        // Disabled option, is not good idea not to overwrite
        // overwrite = true;

        // Check if destination file already exists
        String resourceOutput = resource;
        if (!useResourcePath) {
            resourceOutput = SpecsIo.getResourceName(resourceOutput);
        }

        File destination = new File(destinationFolder, resourceOutput);

        if (destination.isFile() && !overwrite) {
            return destination;
        }

        try (InputStream stream = SpecsIo.resourceToStream(resource);) {

            if (stream == null) {
                throw new RuntimeException("Resource '" + resource + "' does not exist");
            }

            SpecsIo.copy(stream, destination);
        } catch (IOException e) {
            SpecsLogs.msgWarn("Skipping resource '" + resource + "'.", e);
            return null;
        }

        return destination;
    }

    public static void resourceCopyWithName(String resource, String resourceFinalName, File destinationFolder) {

        // Check if destination file already exists
        File destination = new File(destinationFolder, resourceFinalName);
        if (destination.isFile()) {
            return;
        }

        // Get the resource contents
        try (InputStream stream = SpecsIo.resourceToStream(resource);) {
            if (stream == null) {

                SpecsLogs.msgWarn("Skipping resource '" + resource + "'.");
                return;
            }

            SpecsIo.copy(stream, destination);
        } catch (IOException e) {
            SpecsLogs.msgWarn("Could not copy resource", e);
            return;
        }
    }

    /**
     * If baseInput path is "C:\inputpath"; If inputFile is "C:\inputpath\aFolder\inputFile.txt"; If outputFolder is
     * "C:\anotherFolder";
     *
     * Returns the String "C:\anotherFolder\aFolder\"
     *
     * @param baseInputPath
     * @param inputFile
     * @param outputFolder
     * @return
     */
    public static String getExtendedFoldername(File baseInputPath, File inputFile, File outputFolder) {

        String baseInputPathname = baseInputPath.getAbsolutePath();
        String baseInputFileParent = inputFile.getParentFile().getAbsolutePath();

        if (!baseInputFileParent.startsWith(baseInputPathname)) {
            // LoggingUtils.getLogger().warning(
            SpecsLogs.msgWarn("Base parent '" + baseInputFileParent + "' does not start with " + "'"
                    + baseInputPathname + "'");
            return null;
        }

        String programFolder = baseInputFileParent.substring(baseInputPathname.length());

        String outputFoldername = outputFolder.getPath() + programFolder;

        return outputFoldername;
    }

    /**
     * Convert String to InputStream using ByteArrayInputStream class. This class constructor takes the string byte
     * array which can be done by calling the getBytes() method.
     *
     * @param text
     * @return
     */
    public static InputStream toInputStream(String text) {
        try {
            return new ByteArrayInputStream(text.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Convert File to InputStream using a buffered FileInputStream class.
     *
     * @param text
     * @return
     */
    public static InputStream toInputStream(File file) {

        try {
            return new BufferedInputStream(new FileInputStream(file));
        } catch (Exception e) {
            throw new RuntimeException("Could not convert file to InputStream", e);
        }
    }

    /**
     * Helper method that filters files that have a certain extension.
     *
     * @param fileOrFolder
     *            a File representing an existing file or folder.
     * @param extension
     *            a string
     * @return all the files that have a certain extension
     */
    // public static List<File> getFiles(File fileOrFolder, String extension) {
    public static SpecsList<File> getFiles(File fileOrFolder, String extension) {
        // ExtensionFilter filter = new ExtensionFilter(extension);
        String suffix = DEFAULT_EXTENSION_SEPARATOR + extension;
        List<File> fileList = getFiles(fileOrFolder).stream()
                .filter(currentFile -> currentFile.getName().endsWith(suffix))
                .collect(Collectors.toList());

        return SpecsList.convert(fileList);
        /*
        File[] files = folder.listFiles(new ExtensionFilter(extension));
        if (files == null) {
            return Collections.emptyList();
        }
        
        ArrayList<File> returnValue = new ArrayList<>();
        
        for (File file : files) {
            returnValue.add(file);
        }
        
        return returnValue;
        */
    }

    /**
     * Taken from here: https://stackoverflow.com/a/31685610/1189808
     *
     * @param folder
     * @param pattern
     * @return
     */
    private static List<File> getFilesWithPattern(File folder, String pattern) {
        List<File> files = new ArrayList<>();

        if (!folder.isDirectory()) {
            SpecsLogs.info("Given folder for getting files with pattern does not exist: '" + folder + "'");
            return files;
        }

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(
                Paths.get(folder.getAbsolutePath()), pattern)) {

            dirStream.forEach(path -> files.add(new File(path.toString())));
        } catch (IOException e) {
            SpecsLogs.info("Exception while getting files with pattern , returning empty list: " + e.getMessage());
        }

        return files;
    }

    public static List<File> getPathsWithPattern(File folder, String pattern, boolean recursive, String filter) {
        return getPathsWithPattern(folder, pattern, recursive, Enum.valueOf(PathFilter.class, filter));
    }

    public static List<File> getPathsWithPattern(File folder, String pattern, boolean recursive, PathFilter filter) {

        // If recursion disabled, use simple version of the function
        // if (!recursive) {
        // return getFilesWithPattern(folder, pattern);
        // }

        List<File> files = new ArrayList<>();

        // Treat recursion separately
        if (recursive) {
            List<File> subFolders = getFolders(folder);
            for (File subFolder : subFolders) {
                files.addAll(getPathsWithPattern(subFolder, pattern, recursive, filter));
            }
        }

        List<File> patternPaths = getFilesWithPattern(folder, pattern);

        for (File currentPatternPath : patternPaths) {
            if (filter.isAllowed(currentPatternPath)) {
                files.add(currentPatternPath);
            }

            /*
            if (currentPatternPath.isDirectory()) {
                if (filter.isAllowed(currentPatternPath)) {
                    files.add(currentPatternPath);
                }
            
                continue;
            }
            
            if (currentPatternPath.isFile()) {
                if (filter.isAllowed(currentPatternPath)) {
                    files.add(currentPatternPath);
                }
            
                continue;
            }
            
            SpecsLogs.msgWarn("Could not hand path, is neither a file or a folder: " + currentPatternPath);
            */
        }

        return files;
    }

    /**
     * Returns true if the folder contains at least one file having the extension "extension".
     *
     * @param folder
     *            The folder to find the extension from.
     * @param extension
     *            The extension to find in the folder.
     *
     * @return true if the folder contains at least one file having the extension "extension".
     */
    /*
    public static boolean contains(File folder, String extension) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("The file given in parameter is not a folder");
        }
    
        File[] files = folder.listFiles(new ExtensionFilter(extension));
    
        if (files == null || files.length == 0) {
            return false;
        }
        return true;
    }
    */

    /**
     * Returns the relative path of the file given in parameter, relative to the working folder.
     *
     * @param file
     * @return
     */
    public static String getRelativePath(File file) {
        return getRelativePath(file, SpecsIo.getWorkingDir());
    }

    /**
     * Returns the path of 'file', relative to 'baseFile'.
     *
     * <p>
     * The output path is normalized to use the '/' as path separator.
     *
     * <p>
     * If the file does not share a common ancestor with baseFile, returns the absolute path to file.
     *
     * @param file
     *            The file the user needs the relative path of.
     *
     * @return the relative path of the file given in parameter.
     */
    public static String getRelativePath(File file, File baseFile) {
        return getRelativePath(file, baseFile, false).get();
    }

    /**
     *
     * @param file
     * @param baseFile
     * @param strict
     *            if true, returns empty Optional if the file is not a sub-path of baseFile.
     * @return
     */
    public static Optional<String> getRelativePath(File file, File baseFile, boolean isStrict) {

        File originalFile = file;
        File originalBaseFile = baseFile;
        if (!baseFile.isDirectory()) {
            baseFile = baseFile.getParentFile();
            if (baseFile == null) {
                baseFile = new File("");
            }
        }

        final String PREVIOUS_FOLDER = "..";

        // Finds the current folder path
        String mainFolder = null;
        try {
            // Get absolute path first, to resolve paths such as Windows Desktop, then get canonical
            mainFolder = baseFile.getAbsoluteFile().getCanonicalPath();
            File absoluteFile = file.getAbsoluteFile();
            file = absoluteFile.getCanonicalFile();
        } catch (IOException e) {
            SpecsLogs.msgWarn(
                    "Could not convert given files to canonical paths. File: " + originalFile + "; Base file: "
                            + originalBaseFile,
                    e);
            return null;
        }

        // If paths are equal, return empty string
        if (file.getPath().equals(mainFolder)) {
            return Optional.of("");
        }

        // Finds the parents of both files
        List<String> currentFileParents = getParentNames(file);
        List<String> mainFolderParents = getParentNames(new File(mainFolder));

        // If first parent is not the same in both lists, return file with absolute path
        if (!currentFileParents.get(0).equals(mainFolderParents.get(0))) {
            if (isStrict) {
                return Optional.empty();
            } else {
                return Optional.of(file.getPath());
            }

        }

        // Find the first different parent
        int nbSimilarParents = 0;

        int currentFileNumParents = currentFileParents.size();

        // If current file is a folder, do not consider the last element for comparison
        if (file.isDirectory()) {
            currentFileNumParents--;
        }

        while (currentFileNumParents > nbSimilarParents && mainFolderParents.size() > nbSimilarParents
                && currentFileParents.get(nbSimilarParents).equals(mainFolderParents.get(nbSimilarParents))) {
            nbSimilarParents++;
        }

        int nbParentToGoBack = mainFolderParents.size() - nbSimilarParents;

        // Writes the relative path
        StringBuilder relativePath = new StringBuilder();

        final String currentSeparator = "/";

        for (int i = 0; i < nbParentToGoBack; i++) {
            relativePath.append(PREVIOUS_FOLDER);
            relativePath.append(currentSeparator);
        }

        for (int i = nbSimilarParents; i < currentFileParents.size() - 1; i++) {
            relativePath.append(currentFileParents.get(i));
            relativePath.append(currentSeparator);
        }

        // Append last element
        relativePath.append(currentFileParents.get(currentFileParents.size() - 1));

        String relativePathResult = relativePath.toString();

        // Normalize path separator
        relativePathResult = relativePathResult.replace('\\', '/');
        return Optional.of(relativePathResult);
    }

    /**
     *
     * @return a File representing the working directory
     */
    public static File getWorkingDir() {
        return new File(".");
    }

    /**
     * Returns the name of each parent folder in an array.
     * <p>
     * The File ./parent1/parent2/file.f will return the value {., parent1, parent2, file.f}
     *
     * @param file
     *            The file to check.
     *
     * @return the name of each parent folder in an array.
     */
    /*
    public static String[] getParentNames(File file) {
    
    final String WINDOWS = "\\";
    final String LINUX = "/";
    
    String[] parents;
    String path = file.getAbsolutePath();
    
    if (path.contains(WINDOWS)) {
    
        parents = path.split(Pattern.quote(WINDOWS));
        // parents = StringUtils.split(path, WINDOWS);
    } else { // if (path.contains(LINUX))
    	 // parents = StringUtils.split(LINUX);
        parents = path.split(Pattern.quote(LINUX));
    }
    
    return parents;
    }
     */

    public static List<String> getParentNames(File file) {
        List<String> names = new ArrayList<>();

        getParentNamesReverse(file, names);
        Collections.reverse(names);
        return names;
    }

    /**
     *
     * @param file
     * @param names
     * @return
     */
    private static void getParentNamesReverse(File file, List<String> names) {
        // add current file name
        names.add(file.getName());

        File parent = file.getParentFile();

        // If null stop
        if (parent == null) {
            return;
        }
        // If empty string stop
        if (parent.getName().isEmpty()) {
            return;
        }

        // Call function recursively
        getParentNamesReverse(parent, names);
    }

    /**
     * Convenience method which accepts a File as input.
     *
     * @param file
     * @return
     */
    public static String getExtension(File file) {
        // String separator = DEFAULT_SEPARATOR;
        String filename = file.getName();

        return getExtension(filename);
        /*
        	int extIndex = filename.lastIndexOf(separator);
        	if (extIndex < 0) {
        	    return "";
        	}
        
        	return filename.substring(extIndex + 1, filename.length());
         */
    }

    /**
     * Returns the extension of a file name (everything that's after the last '.').
     *
     * <p>
     * If the file has no extension, returns an empty String.
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        String separator = SpecsIo.DEFAULT_SEPARATOR;

        int extIndex = fileName.lastIndexOf(separator);
        if (extIndex < 0) {
            return "";
        }

        return fileName.substring(extIndex + 1, fileName.length());
    }

    /**
     * Convenience method, without parent folder.
     *
     * <p>
     * If folder does not exist, throws a RuntimeException.
     *
     * @param foldername
     * @return
     */
    public static File existingFolder(String folderpath) {
        return existingFolder(null, folderpath);
        /*
        File folder = existingFolder(null, folderpath);
        
        if (folder == null) {
        throw new RuntimeException("Folder '" + folderpath + "' not found");
        }
        
        return folder;
         */
    }

    public static File existingFolder(File parentFolder, String foldername) {
        File folder = new File(parentFolder, foldername);

        if (!folder.isDirectory()) {
            throw new RuntimeException("Could not open folder '" + folder.getPath() + "'");
            // LoggingUtils.msgWarn("Could not open folder '" + folder.getPath() + "'");
            // return null;
        }

        return folder;
    }

    public static File existingFile(File parent, String filePath) {
        File completeFilepath = new File(parent, filePath);

        return existingFile(completeFilepath.getPath());
    }

    /**
     * From the given paths, returns a list of existing files. The paths can represent single files or folders.
     *
     * <p>
     * If a folder is given, looks recursively inside the folder.
     *
     * @param paths
     * @param extensions
     * @return
     */
    // public static List<File> existingFiles(List<String> paths, boolean recursive, Collection<String> extensions) {
    // List<File> existingPaths = new ArrayList<>();
    // for (String arg : paths) {
    // File path = new File(arg);
    // if (!path.exists()) {
    // SpecsLogs.info("Ignoring path '" + arg + "', it does not exist");
    // continue;
    // }
    //
    // existingPaths.add(path);
    // }
    //
    // List<File> files = new ArrayList<>();
    // for (File existingPath : existingPaths) {
    // if (existingPath.isDirectory()) {
    // files.addAll(SpecsIo.getFiles(existingPath, recursive, extensions));
    // } else {
    // files.add(existingPath);
    // }
    // }
    //
    // return files;
    // }

    /**
     * Returns the canonical path of the given file. If a problem happens, throws an exception.
     *
     * @param executable
     * @return
     */
    public static String getPath(File file) {

        try {
            return file.getCanonicalPath();
            // String path = file.getCanonicalPath();
            // return normalizePath(path);
        } catch (IOException e) {
            throw new RuntimeException("Could not get canonical file for " + file.getPath());
        }

    }

    /**
     * Returns the parent folder of an existing file.
     *
     * @param existingFile
     * @return
     */
    public static File getParent(File file) {
        File parentFile = file.getParentFile();

        if (parentFile != null) {
            return parentFile;
        }

        // Try with canonical path, getParent) might not work when using '\' in Linux platforms
        parentFile = SpecsIo.getCanonicalFile(file).getParentFile();
        if (parentFile != null) {
            return parentFile;
        }

        file = SpecsIo.existingFile(file.getPath());
        if (file == null) {
            throw new RuntimeException("Given file '" + file + "' does not exist.");
        }

        return file.getAbsoluteFile().getParentFile();
    }

    public static File existingPath(String path) {
        File pathFile = new File(path);

        if (pathFile.isFile()) {
            return SpecsIo.existingFile(path);
        }

        if (pathFile.isDirectory()) {
            return SpecsIo.existingFolder(null, path);
        }

        throw new RuntimeException("Path '" + path + "' does not exist.");
    }

    public static File download(String urlString, File outputFolder) {
        URL url = null;

        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            SpecsLogs.msgInfo("Could not create URL from '" + urlString + "'.");
            return null;
        }

        return download(url, outputFolder);
    }

    /**
     * This function downloads the file specified in the URL.
     *
     * @param url
     *            The URL of the file to be downloaded.
     * @return true if the file could be downloaded, false otherwise
     * @throws IOException
     */
    public static File download(URL url, File outputFolder) {
        URLConnection con;
        // UID uid = new UID();

        try {
            con = url.openConnection();
            con.connect();

            // String type = con.getContentType();
            //
            // if (type == null) {
            // LoggingUtils.msgInfo("Could not get the content type of the URL '" + url + "'");
            // return null;
            //
            // }

            // Get filename
            String path = url.getPath();
            String filename = path.substring(path.lastIndexOf('/') + 1, path.length());
            if (filename.isEmpty()) {
                SpecsLogs.info("Could not get a filename for the url '" + url + "'");
                return null;
            }

            String escapedFilename = SpecsIo.escapeFilename(filename);
            if (!escapedFilename.equals(filename)) {
                SpecsLogs.info("Renamed '" + filename + "' to '" + escapedFilename + "'");
            }

            byte[] buffer = new byte[4 * 1024];
            int read;

            // String[] split = type.split("/");
            // String filename = Integer.toHexString(uid.hashCode()) + "_" + split[split.length - 1];
            // Make sure output folder exists
            if (!outputFolder.isDirectory()) {
                outputFolder = SpecsIo.mkdir(outputFolder);
            }

            File outputFile = new File(outputFolder, escapedFilename);

            SpecsLogs.msgInfo("Downloading '" + escapedFilename + "' to '" + outputFolder + "'...");
            try (FileOutputStream os = new FileOutputStream(outputFile);
                    InputStream in = con.getInputStream()) {

                while ((read = in.read(buffer)) > 0) {
                    os.write(buffer, 0, read);
                }

            }

            return outputFile;
        } catch (IOException e) {
            String urlString = url.toString();
            if (urlString.length() > 512) {
                urlString = urlString.substring(0, 512) + " ... (url truncated)";
            }
            SpecsLogs.msgInfo("IOException while reading URL '" + urlString + "':\n - " + e.getMessage());
            return null;
        }

    }

    /**
     * Replaces characters that are illegal for filenames with '_'.
     *
     * @param filename
     * @return
     */
    public static String escapeFilename(String filename) {
        StringBuilder escapedFilename = new StringBuilder(filename.length());
        for (char aChar : filename.toCharArray()) {

            if (ILLEGAL_FILENAME_CHARS.contains(aChar)) {
                escapedFilename.append("_");
            } else {
                escapedFilename.append(aChar);
            }
        }

        return escapedFilename.toString();
    }

    /**
     * Helper method which creates a temporary file in the system temporary folder with extension 'txt'.
     * 
     * 
     * @return
     */
    public static File getTempFile() {
        return getTempFile(null, "txt");
    }

    /**
     * Creates a file with a random name in a temporary folder. This file will be deleted when the JVM exits.
     * 
     * @param folderName
     * @return
     */
    public static File getTempFile(String folderName, String extension) {
        File tempFolder = getTempFolder(folderName);

        // Get a random filename
        File randomFile = new File(tempFolder, UUID.randomUUID().toString() + "." + extension);
        SpecsIo.write(randomFile, "");

        deleteOnExit(randomFile);

        return randomFile;
    }

    /**
     * A randomly named folder in the OS temporary folder that is deleted when the virtual machine exits.
     * 
     * @return
     */
    public static File newRandomFolder() {
        File tempFolder = getTempFolder();

        // Get a random foldername
        File randomFolder = new File(tempFolder, UUID.randomUUID().toString());
        SpecsIo.mkdir(randomFolder);

        deleteOnExit(randomFolder);

        return randomFolder;
    }

    /**
     * Code taken from http://www.kodejava.org/how-do-i-get-operating-system-temporary-directory-folder/
     *
     * @return
     */
    public static File getTempFolder() {
        return getTempFolder(null);
    }

    public static File getTempFolder(String folderName) {
        // This is the property name for accessing OS temporary directory or
        // folder.
        String property = "java.io.tmpdir";

        // Get the temporary directory and print it.
        String tempDir = System.getProperty(property);

        // If we are on Linux, usually the temporary folder is shared by all users.
        // This can be problematic in regard to read/write permissions
        // Suffix the name of the user to make the temporary folder unique to the user
        if (SpecsSystem.isLinux()) {
            String userName = System.getProperty("user.name");
            folderName = folderName == null ? "tmp_" + userName : folderName + "_" + userName;
            // tempDir = tempDir + "_" + System.getProperty("user.name");
        }

        File systemTemp = SpecsIo.existingFolder(null, tempDir);

        if (folderName == null) {
            return systemTemp;
        }

        return mkdir(systemTemp, folderName);
    }

    /**
     * List directory contents for a resource folder. Not recursive. This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @author Greg Briggs
     * @param aClass
     *            Any java class that lives in the same place as the resources you want.
     * @param path
     *            Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException
     * @throws IOException
     */
    String[] getResourceListing(Class<?> aClass, String path) throws URISyntaxException, IOException {
        URL dirURL = aClass.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
            /*
             * In case of a jar file, we can't actually find a directory.
             * Have to assume the same jar as clazz.
             */
            String me = aClass.getName().replace(".", "/") + ".class";
            dirURL = aClass.getClassLoader().getResource(me);
        }

        if (dirURL.getProtocol().equals("jar")) {
            /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); // strip out only the JAR
            // file
            try (JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"))) {

                Enumeration<JarEntry> entries = jar.entries(); // gives ALL entries in jar
                Set<String> result = new HashSet<>(); // avoid duplicates in case it is a subdirectory
                while (entries.hasMoreElements()) {
                    String name = entries.nextElement().getName();
                    if (name.startsWith(path)) { // filter according to the path
                        String entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            // if it is a subdirectory, we just return the directory name
                            entry = entry.substring(0, checkSubdir);
                        }
                        result.add(entry);
                    }
                }

                return result.toArray(new String[result.size()]);
            }

        }

        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }

    /**
     * Returns a File object pointing to the given file path. If the returned object is different than null, the file
     * exists.
     *
     * <p>
     * The method tries to build a File object using the following order of approaches:<br>
     * - If the given file path is absolute, uses only that information; <br>
     * - If the given parent folder is different than null, uses it as base folder. Otherwise, uses the path alone,
     * relative to the current working folder;
     *
     * @param parentFolder
     * @param filepath
     * @return
     */
    public static File getFile(File parentFolder, String filepath) {

        // Try using absolute path
        File absolutePath = new File(filepath);
        if (absolutePath.isAbsolute()) {
            return existingFile(filepath);
        }

        // Try using parent folder
        return existingFile(parentFolder, filepath);
    }

    public static File getFolder(File parentFolder, String folderpath, boolean exists) {
        // Try using absolute path
        File absoluteFolder = new File(folderpath);
        if (absoluteFolder.isAbsolute()) {
            return getFolderPrivate(null, folderpath, exists);
        }

        // Try using setup file location
        if (parentFolder != null) {
            File folder = getFolderPrivate(parentFolder, folderpath, exists);

            return folder;
        }

        if (exists) {
            File folder = SpecsIo.existingFolder(null, folderpath);

            if (folder == null) {
                SpecsLogs.msgInfo("Could not get existing folder with path '" + folderpath + "'.");
                return null;
            }

            return folder;
        }

        return null;
    }

    /**
     * Returns null if could not return a valid folder.
     *
     * @param parentFolder
     * @return
     */
    private static File getFolderPrivate(File parentFolder, String folderpath, boolean exists) {

        File folder = null;
        if (exists) {
            folder = SpecsIo.existingFolder(parentFolder, folderpath);
        } else {
            folder = SpecsIo.mkdir(parentFolder, folderpath);
        }

        if (folder == null) {
            SpecsLogs.msgInfo("Could not get folder '" + folder + "', which should exist.");
            return null;
        }

        return folder;

    }

    public static Optional<URL> parseUrl(String urlString) {
        try {
            return Optional.of(new URL(urlString));
        } catch (MalformedURLException e) {
            return Optional.empty();
        }
    }

    public static String getUrl(String urlString) {

        try {
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(10000);
            con.setReadTimeout(10000);

            try (InputStream in = con.getInputStream()) {
                return SpecsIo.read(in);
            }

        } catch (Exception e) {
            SpecsLogs.msgWarn("Exception while getting the contents of URL '" + urlString + "'", e);
        }

        return null;
    }

    /*
    public static String getCanonicalPath(File file) {
    try {
        return file.getCanonicalPath();
    } catch (IOException e) {
        throw new RuntimeException("Could not get canonical path for " + file.getPath());
    }
    }
     */

    /**
     * Returns the canonical file.
     *
     * <p>
     * Calls getAbsoluteFile(), to avoid problems when using paths such as 'Desktop' in Windows, and then transforms to
     * a canonical path.
     *
     * <p>
     * Throws a RuntimeException if it could not obtain the canonical file.
     *
     * @param file
     * @return
     */
    public static File getCanonicalFile(File file) {

        try {
            return new File(file.getAbsolutePath().trim()).getCanonicalFile();

            /*
            file = file.getAbsoluteFile().getCanonicalFile();
            
            // return new File(file.getAbsolutePath().replace('\\', '/'));
            return new File(normalizePath(file.getAbsolutePath()));
            */
        } catch (IOException e) {
            SpecsLogs.msgInfo("Could not get canonical file for " + file.getPath() + ", returning absolute file");
            // return new File(normalizePath(file.getAbsolutePath()));
            return file.getAbsoluteFile();
        }
    }

    /**
     * Converts all '\' to '/'
     *
     * <p>
     * This method should only be used when manipulating Files as strings. Otherwise, File objects always revert to the
     * system's preferred separator.
     *
     * @param path
     * @return
     */
    public static String normalizePath(String path) {
        return path.replace('\\', SpecsIo.DEFAULT_FOLDER_SEPARATOR).trim();
    }

    public static String normalizePath(File path) {
        return normalizePath(path.getPath());
    }

    //
    // public static CharSequence getNewline() {
    // return System.getProperty("line.separator");
    // }

    public static char getFolderSeparator() {
        return SpecsIo.DEFAULT_FOLDER_SEPARATOR;
    }

    public static boolean delete(File file) {
        boolean success = file.delete();
        if (!success) {
            SpecsLogs.msgInfo("!Could not delete file '" + file + "'");
        }

        return success;
    }

    /**
     * Returns the canonical path of a file
     *
     * @param file
     * @return
     */
    public static String getCanonicalPath(File file) {
        // return normalizePath(getCanonicalFile(file).getPath());
        return getCanonicalFile(file).getPath();
    }

    public static Optional<File> getJarPath(Class<?> aClass) {
        String jarfilePath = null;

        try {

            jarfilePath = aClass.getProtectionDomain().getCodeSource().getLocation().toURI()
                    .getPath();

        } catch (URISyntaxException e) {
            SpecsLogs.msgLib("Problems decoding URI of jarpath\n" + e.getMessage());
            return Optional.empty();
        }

        // If could not obtain path, return null
        if (jarfilePath == null) {
            return Optional.empty();
        }

        String jarLoc = jarfilePath.substring(0, jarfilePath.lastIndexOf("/") + 1);

        File jarLocFile = new File(jarLoc);

        if (!jarLocFile.getAbsoluteFile().exists()) {
            return Optional.empty();
        }

        return Optional.of(jarLocFile);

    }

    /**
     * Deletes the given folder and all its contents.
     *
     * @param folder
     *            folder to delete
     * @return true if both the folder and its contents could be deleted
     */
    public static boolean deleteFolder(File folder) {
        if (!folder.exists()) {
            return true;
        }

        if (!folder.isDirectory()) {
            SpecsLogs.msgWarn("Given file does not represent a folder:'" + folder + "'");
            return true;
        }

        // Delete contents of folder
        boolean contentsDeleted = SpecsIo.deleteFolderContents(folder, true);
        // Delete folder
        boolean folderDeleted = delete(folder);

        return contentsDeleted && folderDeleted;
    }

    /**
     * Helper method that enables recursion by default.
     *
     * @param sources
     * @param extensions
     * @return
     */
    public static Map<String, File> getFileMap(List<File> sources, Set<String> extensions) {
        return getFileMap(sources, true, extensions);

    }

    /**
     * Maps the canonical path of each file found in the sources folders to its corresponding source folder.
     *
     * @param sources
     * @param recursive
     * @param extensions
     * @return
     */
    public static Map<String, File> getFileMap(List<File> sources, boolean recursive, Set<String> extensions) {
        return getFileMap(sources, recursive, extensions, file -> false);
    }

    /**
     *
     * @param sources
     * @param recursive
     * @param extensions
     * @param cutoffFolders
     *            accepts a folder, if returns true, that folder and its sub-folders will be ignored from the search
     * @return
     */
    public static Map<String, File> getFileMap(List<File> sources, boolean recursive, Set<String> extensions,
            Predicate<File> cutoffFolders) {

        Map<String, File> fileMap = new LinkedHashMap<>();

        for (File source : sources) {
            // Convert source to absolute path
            File canonicalSource = SpecsIo.getCanonicalFile(source);
            // List<String> filenames = getFiles(Arrays.asList(source), extensions);
            // filenames.stream().forEach(filename -> fileMap.put(filename, source));
            getFiles(Arrays.asList(canonicalSource), recursive, extensions, cutoffFolders).stream()
                    .forEach(file -> fileMap.put(SpecsIo.getCanonicalPath(file), canonicalSource));
        }

        return fileMap;
    }

    public static SpecsList<File> getFiles(List<File> sources, boolean recursive, Collection<String> extensions) {
        return getFiles(sources, recursive, extensions, file -> false);
    }

    public static SpecsList<File> getFiles(List<File> sources, boolean recursive, Collection<String> extensions,
            Predicate<File> cutoffFolders) {

        // Function<? super File, ? extends Stream<? extends File>> flatMapper = recursive
        // ? path -> SpecsIo.getFilesRecursive(path).stream()
        // : path -> SpecsIo.getFiles(path).stream();

        List<File> sourceFiles = sources.stream()
                // .flatMap(flatMapper)
                .flatMap(path -> fileMapper(path, recursive, extensions, cutoffFolders))
                // .map(file -> file.getAbsolutePath())
                .filter(file -> extensions.contains(SpecsIo.getExtension(file)))
                .collect(Collectors.toList());

        // System.out.println(
        // "All Sources:" + sourceFiles.stream().map(Object::toString).collect(Collectors.joining(", ")));
        //

        // Sort files to keep order across platforms
        Collections.sort(sourceFiles);
        //
        // System.out.println(
        // "All Sources after:" + sourceFiles.stream().map(Object::toString).collect(Collectors.joining(", ")));

        return SpecsList.convert(sourceFiles);
    }

    private static Stream<File> fileMapper(File path, boolean recursive, Collection<String> extensions,
            Predicate<File> cutoffFolders) {
        // Test if path should be cut-off
        if (path.isDirectory() && cutoffFolders.test(path)) {
            return Stream.empty();
        }

        // // Check if it is a folder that should be ignored
        // if (source.isDirectory() && cutoffFolders.test(source)) {
        // continue;
        // }

        return recursive ? SpecsIo.getFilesRecursive(path, extensions, true, cutoffFolders).stream()
                : SpecsIo.getFiles(path).stream();
    }

    public static void copyFolderContents(File sourceFolder, File destinationFolder) {
        copyFolderContents(sourceFolder, destinationFolder, file -> true);
    }

    public static void copyFolderContents(File sourceFolder, File destinationFolder, Predicate<File> filter) {
        // Get all files in source folder that pass the predicate
        List<File> sourceFiles = SpecsIo.getFilesRecursive(sourceFolder);

        for (File sourceFile : sourceFiles) {
            if (!filter.test(sourceFile)) {
                continue;
            }

            String baseFile = SpecsIo.getRelativePath(sourceFile, sourceFolder);
            File destFile = new File(destinationFolder, baseFile);
            SpecsIo.copy(sourceFile, destFile);
        }

    }

    /**
     * Compresses the entries into the given zipFile. Uses basePath to calculate the root of entries in the zip.
     *
     * @param entries
     * @param basePath
     * @param zipFile
     */
    public static void zip(List<File> entries, File basePath, File zipFile) {

        try (FileOutputStream outStream = new FileOutputStream(zipFile);
                ZipOutputStream zip = new ZipOutputStream(outStream)) {

            ProgressCounter progress = new ProgressCounter(entries.size());
            SpecsLogs.msgInfo("Zipping " + entries.size() + " files to '" + zipFile.getAbsolutePath() + "'");
            for (File entry : entries) {
                SpecsLogs.msgInfo("Zipping '" + entry.getAbsolutePath() + "'... " + progress.next());
                // Get relative path, to create ZipEntry
                Optional<String> entryPath = SpecsIo.getRelativePath(entry, basePath, true);

                if (!entryPath.isPresent()) {
                    SpecsLogs.msgInfo("Entry '" + entry.getAbsolutePath() + "' is not inside base path '"
                            + basePath.getAbsolutePath() + "'");
                    continue;
                }

                ZipEntry zipEntry = new ZipEntry(entryPath.get());
                zip.putNextEntry(zipEntry);

                zip.write(SpecsIo.readAsBytes(entry));
                zip.closeEntry();
            }

        } catch (IOException e) {
            SpecsLogs.msgWarn("Exception while zipping archive:\n", e);
        }
    }

    public static boolean checkFolder(File folder) {
        if (!folder.exists()) {
            SpecsLogs.msgInfo("Given folder does not exist: " + folder);
            return false;
        }

        if (!folder.isDirectory()) {
            SpecsLogs.msgInfo("Given path is not a folder: " + folder);
            return false;
        }

        return true;
    }

    public static boolean checkFile(File file) {
        if (!file.exists()) {
            SpecsLogs.msgInfo("Given file does not exist: " + file);
            return false;
        }

        if (!file.isFile()) {
            SpecsLogs.msgInfo("Given path is not a file: " + file);
            return false;
        }

        return true;
    }

    public static boolean isEmptyFolder(File folder) {
        if (!folder.isDirectory()) {
            return false;
        }

        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(folder.toPath())) {
            boolean hasFile = dirStream.iterator().hasNext();
            return !hasFile;
        } catch (IOException e) {
            SpecsLogs.msgWarn("Could not process path", e);
        }

        return false;
    }

    /**
     * Based on https://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
     *
     * @param file
     * @return
     */
    public static String getMd5(File file) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Could not find MD5 algorithm", e);
        }

        try (InputStream is = Files.newInputStream(Paths.get(file.getAbsolutePath()));
                BufferedInputStream bis = new BufferedInputStream(is);
                DigestInputStream dis = new DigestInputStream(bis, md)) {

            while (dis.read() != -1) {

            }
            /* Read decorated stream (dis) to EOF as normal... */
        } catch (IOException e) {
            throw new RuntimeException("Problems while using file '" + file + "'", e);
        }

        byte[] digest = md.digest();

        return SpecsStrings.bytesToHex(digest);
    }

    public static void closeStreamAfterError(OutputStream stream) {
        // Do nothing if no stream
        if (stream == null) {
            return;
        }

        // Close the stream
        try {
            stream.close();
        } catch (IOException e) {
            SpecsLogs.msgWarn("Exception while closing a stream", e);
        }
    }

    /**
     * Tests if a folder can be written.
     *
     * @param folder
     * @return true if the given path is an existing folder, and can be written
     */
    public static boolean canWriteFolder(File folder) {
        if (!folder.isDirectory()) {
            return false;
        }

        // Get a random filename
        File randomFile = new File(folder, UUID.randomUUID().toString());
        // Try it until it is a file that does not exist
        while (randomFile.isFile()) {
            randomFile = new File(folder, UUID.randomUUID().toString());
        }

        try {
            // Try creating a new file
            randomFile.createNewFile();
            // Delete file
            randomFile.delete();
            return true;
        } catch (IOException e) {
            // Could not write file, assume there are no permissions
            return false;
        }

    }

    /**
     * Tries to look for the given filename in several common folders.
     *
     * <p>
     * Current order is: <br>
     * - In the same folder of the .jar of the given class; <br>
     * - In the current working directory <br>
     *
     * @param filename
     * @return
     */
    public static Optional<File> getLocalFile(String filename, Class<?> aClass) {
        // Check if file exists next to the jar
        Optional<File> jarFolder = SpecsIo.getJarPath(aClass);

        // Return file if next to JAR
        if (jarFolder.isPresent()) {
            File localFile = new File(jarFolder.get(), filename);
            if (localFile.isFile()) {
                return Optional.of(localFile);
            }
        }

        // If no file found next to the jar folder, use current folder
        File currentFolder = SpecsIo.getWorkingDir();

        File localFile = new File(currentFolder, filename);
        if (localFile.isFile()) {
            return Optional.of(localFile);
        }

        return Optional.empty();
    }

    /**
     * Reads a single byte from System.in;
     *
     * @return
     */
    public static int read() {

        try {
            return System.in.read();
        } catch (IOException e) {
            throw new RuntimeException("Could not read input", e);
        }
    }

    // public static String getPathSeparator() {
    // return File.pathSeparator;
    // }

    /**
     * 
     * @param file
     * @param base
     * @return
     */
    public static File removeCommonPath(File file, File base) {
        // Normalize paths
        String normalizedFile = normalizePath(file);
        String normalizedBase = normalizePath(base);

        // If file does not start with base, return file
        if (!normalizedFile.startsWith(normalizedBase)) {
            return file;
        }

        String fileWithoutBase = normalizedFile.substring(normalizedBase.length());
        if (fileWithoutBase.startsWith("/")) {
            fileWithoutBase = fileWithoutBase.substring(1);
        }

        return new File(fileWithoutBase);
    }

    public static void deleteOnExit(File tempFolder) {
        SpecsLogs.debug(() -> "Registered for deletion on exit: " + tempFolder.getAbsolutePath());
        deleteOnExitPrivate(tempFolder);
    }

    private static void deleteOnExitPrivate(File path) {
        // First register top file
        path.deleteOnExit();

        // Obtain all children and register them
        File[] children = path.listFiles();
        if (children == null) {
            return;
        }

        for (File child : children) {
            deleteOnExit(child);
        }

    }

    /**
     * Splits the given String into several paths, according to the path separator.
     * 
     * <p>
     * Always uses the same character as path separator, the semicolon (;).
     * 
     * @param fileList
     * @return
     */
    public static String[] splitPaths(String pathList) {
        return pathList.split(UNIVERSAL_PATH_SEPARATOR);
    }

    public static String getUniversalPathSeparator() {
        return UNIVERSAL_PATH_SEPARATOR;
    }

    public static Map<String, String> parseUrlQuery(URL url) {
        Map<String, String> query = new HashMap<>();

        var queryString = url.getQuery();
        if (queryString == null) {
            return query;
        }

        // Split string
        for (var queryLine : queryString.split("&")) {
            int equalIndex = queryLine.indexOf('=');
            if (equalIndex == -1) {
                SpecsLogs.info("Could not find '=' in URL query '" + queryLine + "'");
                continue;
            }

            var key = queryLine.substring(0, equalIndex);
            var value = queryLine.substring(equalIndex + 1, queryLine.length());

            query.put(key, value);
        }

        return query;
    }

}
