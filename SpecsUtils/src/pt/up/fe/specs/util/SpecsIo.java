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
 * specific language governing permissions and limitations under the License.
 */
package pt.up.fe.specs.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import pt.up.fe.specs.util.collections.SpecsList;
import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Utility methods for input/output operations.
 * <p>
 * Provides static helper methods for reading, writing, and managing files and
 * resources.
 * </p>
 *
 * @author Joao Bispo
 */
public class SpecsIo {

    private static final Set<Character> ILLEGAL_FILENAME_CHARS = new HashSet<>(
            Arrays.asList('\\', '/', ':', '*', '?', '"', '<', '>', '|'));

    private static final String UNIVERSAL_PATH_SEPARATOR = ";";

    public static String getNewline() {
        // return "\n";
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
     */
    public static File mkdir(File parentFolder, String child) {
        return mkdir(new File(parentFolder, child));
    }

    /**
     * Helper method which accepts a File as input.
     *
     */
    public static File mkdir(File folder) {
        return mkdir(folder.getPath());
    }

    /**
     * Given a string representing a filepath to a folder, returns a File object
     * representing the folder.
     *
     * <p>
     * If the folder doesn't exist, the method will try to create the folder and
     * necessary sub-folders. If an error occurs (ex.: the folder could not be
     * created, the given path does not represent a folder), throws an exception.
     *
     * <p>
     * If the given folderpath is an empty string, returns the current working
     * folder.
     *
     * <p>
     * If the method returns it is guaranteed that the folder exists.
     *
     * @param folderpath String representing a folder.
     * @return a File object representing a folder, or null if unsuccessful.
     */
    public static File mkdir(String folderpath) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (folderpath == null) {
            throw new RuntimeException("Input 'folderpath' is null");
        }

        // Check if folderpath is empty
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
            throw new RuntimeException("Path '" + folderpath + "' exists, but " + "doesn't represent a folder");
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
            SpecsLogs.warn("Folder created (" + folder.getAbsolutePath() + ") but 'mkdirs' returned false.");
            return folder;
        }

        // Couldn't create folder
        throw new RuntimeException("Path '" + folderpath + "' does not exist and " + "could not be created");
    }

    /**
     * Method to create a File object for a file which should exist.
     *
     * <p>
     * The method does some common checks (ex.: if the file given by filepath
     * exists, if it is a file). If any of the
     * checks fail, throws an exception.
     *
     * @param filepath String representing an existing file.
     * @return a File object representing a file, or null if unsuccessful.
     */
    public static File existingFile(String filepath) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (filepath == null) {
            throw new RuntimeException("Input 'filepath' is null");
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
        }

        // File doesn't exist, return null.
        throw new RuntimeException("Path '" + filepath + "' does not exist");
    }

    /**
     * Helper method that receives a String.
     *
     */
    public static String read(String filename) {
        return read(SpecsIo.existingFile(filename));
    }

    /**
     * Given a File object, returns a String with the contents of the file.
     *
     * <p>
     * If an error occurs (ex.: the File argument does not represent a file) returns
     * null and logs the cause.
     *
     * @param file a File object representing a file.
     * @return a String with the contents of the file.
     */
    public static String read(File file) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (file == null) {
            SpecsLogs.msgInfo("Input 'file' is null.");
            return null;
        }

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            SpecsLogs.warn("FileNotFoundException", e);
            return null;
        }

        return SpecsIo.readHelper(inputStream);
    }

    /**
     * Reads a stream to a String. The stream is closed after it is read.
     *
     */
    public static String read(InputStream inputStream) {
        var result = SpecsIo.readHelper(inputStream);
        return result == null ? "" : result;
    }

    private static String readHelper(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();

        // Try to read the contents of the input stream into the StringBuilder
        // Using 'finally' style 2 as described in
        // http://www.javapractices.com/topic/TopicAction.do?Id=25
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
            SpecsLogs.warn("FileNotFoundException", ex);
            return null;
        } catch (IOException ex) {
            SpecsLogs.warn("IOException", ex);
            return null;
        }

        return stringBuilder.toString();
    }

    /**
     * Given a File object and a String, writes the contents of the String in the
     * file, overwriting everything that was previously in that file.
     *
     * <p>
     * If successful, returns true. If an error occurs (ex.: the File argument does
     * not represent a file) returns false, logs the cause and nothing is written.
     *
     * @param file     a File object representing a file.
     * @param contents a String with the content to write
     * @return true if write is successful. False otherwise.
     */
    public static boolean write(File file, String contents) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (file == null) {
            SpecsLogs.warn("Input 'file' is null.");
            return false;
        }

        if (contents == null) {
            SpecsLogs.warn("Input 'contents' is null.");
            return false;
        }

        return writeAppendHelper(file, contents, false);
    }

    /**
     * Given a File object and a String, writes the contents of the String at the
     * end of the file. If successful, returns true.
     *
     * <p>
     * If an error occurs (ex.: the File argument does not represent a file) returns
     * false, logs the cause and nothing is written.
     *
     * @param file     a File object representing a file.
     * @param contents a String with the content to write
     * @return true if write is successful. False otherwise.
     */
    public static boolean append(File file, String contents) {
        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (file == null) {
            SpecsLogs.warn("Input 'file' is null.");
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
     * Using 'finally' style 2 as described in
     * <a href="http://www.javapractices.com/topic/TopicAction.do?Id=25">Java
     * Practices: Finally and Catch</a>.
     *
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
                    SpecsLogs.warn("Could not create file '" + file + "'");
                    return false;
                }
            }

            if (!file.isFile()) {
                SpecsLogs.warn("Path '" + file + "' is not a file.");
                return false;
            }

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
            isSuccess = false;
        }

        return isSuccess;
    }

    /**
     * Given a filename, removes the extension suffix and the separator.
     *
     * <p>
     * Example: <br>
     * filename: 'readme.txt' <br>
     * separator: '.' <br>
     * result: 'readme'
     *
     * @param filename  a string
     * @param separator the extension separator
     * @return the name of the file without the extension and the separator
     */
    private static String removeExtension(String filename, String separator) {
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
     * @param filename a string
     * @return the name of the file without the extension and the separator
     */
    public static String removeExtension(String filename) {
        return removeExtension(filename, SpecsIo.DEFAULT_EXTENSION_SEPARATOR);
    }

    /**
     * Helper method which receives a file.
     *
     */
    public static String removeExtension(File file) {
        return removeExtension(file.getName());
    }

    /**
     * Note: by default follows symlinks.
     *
     * @param path       a File representing a folder or a file.
     * @param extensions a set of strings
     *
     * @return all the files inside the given folder, excluding other folders, that
     *         have a certain extension as determined by the set.
     */
    public static List<File> getFilesRecursive(File path, Collection<String> extensions) {
        return getFilesRecursive(path, extensions, true);
    }

    private static List<File> getFilesRecursive(File folder, Collection<String> extensions, boolean followSymlinks) {
        return getFilesRecursive(folder, extensions, followSymlinks, path -> false);
    }

    /**
     * @param path           a File representing a folder or a file.
     * @param extensions     a set of strings
     * @param followSymlinks whether to follow symlinks
     * @param cutoffFolders  a predicate to determine if a folder should be cut off
     *
     * @return all the files inside the given folder, excluding other folders, that
     *         have a certain extension as determined by the set.
     */
    private static List<File> getFilesRecursive(File path, Collection<String> extensions, boolean followSymlinks,
            Predicate<File> cutoffFolders) {

        // Make extensions lower-case
        Collection<String> lowerCaseExtensions = extensions.stream().map(String::toLowerCase)
                .collect(Collectors.toSet());

        List<File> files = new ArrayList<>();

        getFilesRecursivePrivate(path, lowerCaseExtensions, followSymlinks, cutoffFolders, files);

        return files;
    }

    private static void getFilesRecursivePrivate(File path, Collection<String> extensions, boolean followSymlinks,
            Predicate<File> cutoffFolders, List<File> foundFiles) {
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

        for (File child : children) {
            getFilesRecursivePrivate(child, extensions, followSymlinks, cutoffFolders, foundFiles);
        }
    }

    /**
     * Note: by default follows symlinks.
     *
     * @param folder    a File representing a folder or a file.
     * @param extension a string
     * @return all the files inside the given folder, excluding other folders, that
     *         have a certain extension.
     */
    public static List<File> getFilesRecursive(File folder, String extension) {
        return getFilesRecursive(folder, List.of(extension), true, path -> false);
    }

    /**
     * Note: by default this follows symlinks.
     *
     * @param path
     *             a File representing a path.
     *
     * @return all the files inside the given folder, excluding other folders.
     */
    public static List<File> getFilesRecursive(File path) {
        return getFilesRecursive(path, true);
    }

    /**
     * @param path           a File representing a path.
     * @param followSymlinks whether to follow symlinks (both files and directories)
     *
     * @return all the files inside the given path, excluding other folders.
     */
    public static List<File> getFilesRecursive(File path, boolean followSymlinks) {
        return getFilesRecursive(path, Collections.emptySet(), followSymlinks, folder -> false);
    }

    /**
     * @param folder a File representing a folder.
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
     * @param folder a File representing a folder.
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
     * @param path a File representing an existing path.
     * @return if path is a folder, returns all the files inside the given folder,
     *         excluding other folders. Otherwise, returns a list with the given
     *         path
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
            return List.of(path);
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

    /**
     * Convenience method which overwrites files by default
     *
     */
    public static List<File> copyFolder(File source, File destination, boolean verbose) {
        return copyFolder(source, destination, verbose, true);
    }

    /**
     * Copies the contents of a folder to another folder.
     *
     */
    private static List<File> copyFolder(File source, File destination, boolean verbose, boolean overwrite) {
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
     * If the destination is an existing folder, copies the file to a file of the
     * same name on that folder (method 'safeFolder' can be used to create the
     * destination folder, before passing it to the function).
     *
     * <p>
     * If verbose is true, warns when overwriting files.
     *
     */
    private static boolean copy(File source, File destination, boolean verbose) {
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

        // Using 'finally' style 2 as described in
        // http://www.javapractices.com/topic/TopicAction.do?Id=25
        boolean success = true;
        try (InputStream in = new FileInputStream(source)) {

            return copy(in, destination);

        } catch (FileNotFoundException ex) {
            SpecsLogs.warn("Could not find file", ex);
            success = false;
        } catch (IOException e) {
            SpecsLogs.warn("Failed to close stream", e);
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
     */
    private static boolean copy(InputStream source, File destination) {
        boolean success = true;

        // Create folders for f2
        File parentFile = destination.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }

        try {
            Files.copy(source, destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
            SpecsLogs.debug(() -> "Copied stream to file '" + destination.getAbsolutePath() + "'.");
        } catch (IOException e) {
            SpecsLogs.warn("IoException while copying stream to file '" + destination + "'", e);
            success = false;
        }
        return success;
    }

    /**
     * Helper method which enables recursion by default.
     *
     * @return true in case the operation was successful (could delete all files, or
     *         the folder does not exit)
     */
    public static boolean deleteFolderContents(File folder) {
        return deleteFolderContents(folder, true);
    }

    private static boolean deleteFolderContents(File folder, boolean recursive) {
        if (!folder.exists()) {
            return true;
        }

        if (!folder.isDirectory()) {
            SpecsLogs.warn("Not a folder");
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
     */
    public static String getResource(ResourceProvider resource) {
        return getResource(resource.getResource());
    }

    /**
     * Given the name of a resource, returns a String with the contents of the
     * resource.
     *
     */
    public static String getResource(String resourceName) {
        try (InputStream inputStream = SpecsIo.resourceToStream(resourceName)) {
            if (inputStream == null) {
                SpecsLogs.warn("Could not get InputStream.");
                return null;
            }

            return SpecsIo.read(inputStream);

        } catch (IOException e) {
            SpecsLogs.warn("Could not open resource '" + resourceName + "'", e);
            return "";
        }
    }

    /**
     * Returns the last name of the resource.
     *
     * <p>
     * Example, if input is 'package/resource.ext', returns 'resource.ext'.
     *
     */
    private static String getResourceName(String resource) {
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
            SpecsLogs.warn("Could not load resource '" + resourceName + "'.");

        }
        return stream;

    }

    public static boolean extractZip(File zipFile, File folder) {
        try (InputStream stream = new FileInputStream(zipFile)) {
            return extractZipResource(stream, folder);
        } catch (IOException e) {
            throw new RuntimeException("Could not unzip file '" + zipFile + "'", e);
        }

    }

    private static boolean extractZipResource(InputStream resource, File folder) {
        boolean success = true;
        if (!folder.isDirectory()) {
            SpecsLogs.warn("Given folder '" + folder.getPath() + "' does not exist.");
            return false;
        }

        // Using 'finally' style 2 as described in
        // http://www.javapractices.com/topic/TopicAction.do?Id=25
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

                SpecsLogs.msgInfo("Unzipping '" + outFile.getPath() + "'");
                unzipFile(zis, outFile);
            }

        } catch (IOException ex) {
            SpecsLogs.warn("IoException while unzipping to folder '" + folder + "'", ex);
            success = false;
        }

        return success;
    }

    /**
     * Reads the contents of ZipInputStream at the current position to a File.
     *
     * <p>
     * Does not close the stream, so that it can be used again for the remaining
     * zipped files.
     *
     */
    private static void unzipFile(ZipInputStream zis, File outFile) throws IOException {
        int size;
        byte[] buffer = new byte[2048];

        // Make sure folder to output file exists
        SpecsIo.mkdir(outFile.getParentFile());

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outFile), buffer.length)) {

            while ((size = zis.read(buffer, 0, buffer.length)) != -1) {
                bos.write(buffer, 0, size);
            }
            bos.flush();
        }
    }

    private static byte[] readAsBytes(File file) {
        int numBytes = (int) file.length();
        return readAsBytes(file, numBytes);
    }

    private static byte[] readAsBytes(File file, int numBytes) {
        // Using 'finally' style 2 as described in
        // http://www.javapractices.com/topic/TopicAction.do?Id=25
        try (InputStream inStream = new FileInputStream(file)) {

            byte[] data = new byte[numBytes];
            inStream.read(data);
            return data;

        } catch (Exception ex) {
            SpecsLogs.warn("Exception while reading bytes from file '" + file + "'", ex);
        }

        return null;
    }

    /**
     * Copies the given list of resources to the execution path. If the files
     * already exist, the method does nothing.
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

        Objects.requireNonNull(destinationFolder, () -> "destinationFolder must not be null");

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
     * Copy the given resource to the destination folder using the full path of the
     * resource. If destination file already exists, does nothing.
     *
     */
    public static File resourceCopy(String resource, File destinationFolder) {
        return resourceCopy(resource, destinationFolder, true);
    }

    /**
     * Copy the given resource to the destination folder. If destination file
     * already exists, overwrites.
     *
     */
    public static File resourceCopy(String resource, File destinationFolder, boolean useResourcePath) {

        return resourceCopy(resource, destinationFolder, useResourcePath, true);
    }

    public static File resourceCopy(String resource, File destinationFolder, boolean useResourcePath,
            boolean overwrite) {

        Objects.requireNonNull(resource, () -> "resource must not be null");
        Objects.requireNonNull(destinationFolder, () -> "destinationFolder must not be null");

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

        try (InputStream stream = SpecsIo.resourceToStream(resource)) {

            if (stream == null) {
                throw new RuntimeException("Resource '" + resource + "' does not exist");
            }

            SpecsIo.copy(stream, destination);
        } catch (IOException e) {
            SpecsLogs.warn("Skipping resource '" + resource + "'.", e);
            return null;
        }

        return destination;
    }

    public static boolean resourceCopyWithName(String resource, String resourceFinalName, File destinationFolder) {

        // Check if destination file already exists
        File destination = new File(destinationFolder, resourceFinalName);
        if (destination.isFile()) {
            return true;
        }

        // Get the resource contents
        try (InputStream stream = SpecsIo.resourceToStream(resource)) {
            if (stream == null) {

                SpecsLogs.warn("Skipping resource '" + resource + "'.");
                return false;
            }

            SpecsIo.copy(stream, destination);
        } catch (IOException e) {
            SpecsLogs.warn("Could not copy resource", e);
            return false;
        }

        return true;
    }

    /**
     * Convert String to InputStream using ByteArrayInputStream class. This class
     * constructor takes the string byte array which can be done by calling the
     * getBytes() method.
     *
     */
    public static InputStream toInputStream(String text) {
        return new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Convert File to InputStream using a buffered FileInputStream class.
     *
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
     * @param fileOrFolder a File representing an existing file or folder.
     * @param extension    a string
     * @return all the files that have a certain extension
     */
    public static SpecsList<File> getFiles(File fileOrFolder, String extension) {
        String suffix = DEFAULT_EXTENSION_SEPARATOR + extension.toLowerCase();
        List<File> fileList = getFiles(fileOrFolder).stream()
                .filter(currentFile -> currentFile.getName().toLowerCase().endsWith(suffix))
                .collect(Collectors.toList());

        return SpecsList.convert(fileList);
    }

    /**
     * Returns the relative path of the file given in parameter, relative to the
     * working folder.
     *
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
     * If the file does not share a common ancestor with baseFile, returns the
     * absolute path to file.
     *
     * @param file The file the user needs the relative path of.
     *
     * @return the relative path of the file given in parameter.
     */
    public static String getRelativePath(File file, File baseFile) {
        return getRelativePath(file, baseFile, false).orElse(null);
    }

    public static Optional<String> getRelativePath(File file, File baseFile, boolean isStrict) {

        if ((file == null) || (baseFile == null)) {
            SpecsLogs.warn("File or baseFile is null. File: " + file + "; BaseFile: " + baseFile);
            return Optional.empty();
        }
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
            // Get absolute path first, to resolve paths such as Windows Desktop, then get
            // canonical
            mainFolder = baseFile.getAbsoluteFile().getCanonicalPath();
            File absoluteFile = file.getAbsoluteFile();
            file = absoluteFile.getCanonicalFile();
        } catch (IOException e) {
            SpecsLogs.warn(
                    "Could not convert given files to canonical paths. File: " + originalFile + "; Base file: "
                            + originalBaseFile,
                    e);
            return Optional.empty();
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

    public static List<String> getParentNames(File file) {
        List<String> names = new ArrayList<>();

        getParentNamesReverse(file, names);
        Collections.reverse(names);
        return names;
    }

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
     */
    public static String getExtension(File file) {
        return getExtension(file.getName());
    }

    /**
     * Returns the extension of a file name (everything that's after the last '.').
     *
     * <p>
     * If the file has no extension, returns an empty String.
     *
     */
    public static String getExtension(String fileName) {
        int extIndex = fileName.lastIndexOf(SpecsIo.DEFAULT_SEPARATOR);
        if (extIndex < 0) {
            return "";
        }

        return fileName.substring(extIndex + 1);
    }

    /**
     * Convenience method, without parent folder.
     *
     * <p>
     * If folder does not exist, throws a RuntimeException.
     *
     */
    public static File existingFolder(String folderpath) {
        return existingFolder(null, folderpath);
    }

    public static File existingFolder(File parentFolder, String foldername) {
        File folder = new File(parentFolder, foldername);

        if (!folder.isDirectory()) {
            throw new RuntimeException("Could not open folder '" + folder.getPath() + "'");
        }

        return folder;
    }

    public static File existingFile(File parent, String filePath) {
        File completeFilepath = new File(parent, filePath);

        return existingFile(completeFilepath.getPath());
    }

    /**
     * Returns the canonical path of the given file. If a problem happens, throws an
     * exception.
     *
     */
    public static String getPath(File file) {

        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException("Could not get canonical file for " + file.getPath());
        }

    }

    /**
     * Returns the parent folder of an existing file.
     *
     */
    public static File getParent(File file) {
        File parentFile = file.getParentFile();

        if (parentFile != null) {
            return parentFile;
        }

        // Try with canonical path, getParent) might not work when using '\' in Linux
        // platforms
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
            url = new URI(urlString).toURL();
        } catch (MalformedURLException | URISyntaxException e) {
            SpecsLogs.msgInfo("Could not create URL from '" + urlString + "'.");
            return null;
        }

        return download(url, outputFolder);
    }

    /**
     * This function downloads the file specified in the URL.
     *
     * @param url The URL of the file to be downloaded.
     * @return if the file could be downloaded, throws IOException otherwise.
     */
    public static File download(URL url, File outputFolder) {
        URLConnection con;

        try {
            con = url.openConnection();
            con.connect();

            // Get filename
            String path = url.getPath();
            String filename = path.substring(path.lastIndexOf('/') + 1);
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

            // Make sure output folder exists
            if (!outputFolder.isDirectory()) {
                outputFolder = SpecsIo.mkdir(outputFolder);
            }

            File outputFile = new File(outputFolder, escapedFilename);

            SpecsLogs.msgInfo("Downloading '" + escapedFilename + "' to '" + outputFolder + "'...");

            Path tempPath = null;
            try {
                tempPath = Files.createTempFile(outputFolder.toPath(), "download_", ".tmp");
                File tempFile = tempPath.toFile();

                try (FileOutputStream os = new FileOutputStream(tempFile);
                        InputStream in = con.getInputStream()) {
                    while ((read = in.read(buffer)) > 0) {
                        os.write(buffer, 0, read);
                    }
                }

                try {
                    Files.move(tempPath, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING,
                            StandardCopyOption.ATOMIC_MOVE);
                } catch (AtomicMoveNotSupportedException atomicMoveException) {
                    SpecsLogs.debug(() -> "Atomic move not supported when downloading '" + escapedFilename
                            + "': " + atomicMoveException.getMessage());
                    Files.move(tempPath, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            } finally {
                final Path pathToDelete = tempPath;
                if (pathToDelete != null) {
                    try {
                        Files.deleteIfExists(pathToDelete);
                    } catch (IOException cleanupException) {
                        SpecsLogs.debug(() -> "Could not delete temporary download file '" + pathToDelete + "': "
                                + cleanupException.getMessage());
                    }
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
     */
    private static String escapeFilename(String filename) {
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
     * Helper method which creates a temporary file in the system temporary folder
     * with extension 'txt'.
     *
     */
    public static File getTempFile() {
        return getTempFile(null, "txt");
    }

    /**
     * Creates a file with a random name in a temporary folder. This file will be
     * deleted when the JVM exits.
     *
     */
    public static File getTempFile(String folderName, String extension) {
        File tempFolder = getTempFolder(folderName);

        // Get a random filename
        File randomFile = new File(tempFolder, UUID.randomUUID() + "." + extension);
        SpecsIo.write(randomFile, "");

        deleteOnExit(randomFile);

        return randomFile;
    }

    /**
     * Code taken from
     * <a href=
     * "http://www.kodejava.org/how-do-i-get-operating-system-temporary-directory-folder/">...</a>
     *
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
        /**
         * FIXME: this is not a good idea, as it can lead to problems when running
         * multiple instances of the same program.
         * at the same time, as the temporary folder will be shared by all instances.
         * A better solution would be to append a UUID to the folder name.
         */
        if (SpecsSystem.isLinux()) {
            String userName = System.getProperty("user.name");
            folderName = folderName == null ? "tmp_" + userName : folderName + "_" + userName;
        }

        File systemTemp = SpecsIo.existingFolder(null, tempDir);

        if (folderName == null) {
            return systemTemp;
        }

        return mkdir(systemTemp, folderName);
    }

    public static Optional<URL> parseUrl(String urlString) {
        if (urlString == null) {
            return Optional.empty();
        }

        try {
            return Optional.of(new URI(urlString).toURL());
        } catch (MalformedURLException | URISyntaxException | IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    /**
     * Returns the canonical file.
     *
     * <p>
     * Calls getAbsoluteFile(), to avoid problems when using paths such as 'Desktop'
     * in Windows, and then transforms to a canonical path.
     *
     * <p>
     * Throws a RuntimeException if it could not obtain the canonical file.
     *
     */
    public static File getCanonicalFile(File file) {

        try {
            return new File(file.getAbsolutePath().trim()).getCanonicalFile();
        } catch (IOException e) {
            SpecsLogs.msgInfo("Could not get canonical file for " + file.getPath() + ", returning absolute file");
            return file.getAbsoluteFile();
        }
    }

    /**
     * Converts all '\' to '/'
     *
     * <p>
     * This method should only be used when manipulating Files as strings.
     * Otherwise, File objects always revert to the system's preferred separator.
     *
     */
    public static String normalizePath(String path) {
        return path.replace('\\', SpecsIo.DEFAULT_FOLDER_SEPARATOR).trim();
    }

    public static String normalizePath(File path) {
        return normalizePath(path.getPath());
    }

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
     */
    public static String getCanonicalPath(File file) {
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
     * @param folder folder to delete
     * @return true if both the folder and its contents could be deleted
     */
    public static boolean deleteFolder(File folder) {
        if (!folder.exists()) {
            return true;
        }

        if (!folder.isDirectory()) {
            SpecsLogs.warn("Given file does not represent a folder:'" + folder + "'");
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
     */
    public static Map<String, File> getFileMap(List<File> sources, Set<String> extensions) {
        return getFileMap(sources, true, extensions);

    }

    /**
     * Maps the canonical path of each file found in the sources folders to its
     * corresponding source folder.
     *
     */
    public static Map<String, File> getFileMap(List<File> sources, boolean recursive, Set<String> extensions) {
        return getFileMap(sources, recursive, extensions, file -> false);
    }

    /**
     *
     * @param cutoffFolders accepts a folder, if returns true, that folder and its
     *                      sub-folders will be ignored from the search
     */
    public static Map<String, File> getFileMap(List<File> sources, boolean recursive, Set<String> extensions,
            Predicate<File> cutoffFolders) {

        Map<String, File> fileMap = new LinkedHashMap<>();

        for (File source : sources) {
            // Convert source to absolute path
            File canonicalSource = SpecsIo.getCanonicalFile(source);
            getFiles(List.of(canonicalSource), recursive, extensions, cutoffFolders)
                    .forEach(file -> fileMap.put(SpecsIo.getCanonicalPath(file), canonicalSource));
        }

        return fileMap;
    }

    public static SpecsList<File> getFiles(List<File> sources, boolean recursive, Collection<String> extensions) {
        return getFiles(sources, recursive, extensions, file -> false);
    }

    public static SpecsList<File> getFiles(List<File> sources, boolean recursive, Collection<String> extensions,
            Predicate<File> cutoffFolders) {
        List<File> sourceFiles = sources.stream()
                .flatMap(path -> fileMapper(path, recursive, extensions, cutoffFolders))
                .filter(file -> extensions.contains(SpecsIo.getExtension(file)))
                .sorted()
                .collect(Collectors.toList());

        // Sort files to keep order across platforms

        return SpecsList.convert(sourceFiles);
    }

    private static Stream<File> fileMapper(File path, boolean recursive, Collection<String> extensions,
            Predicate<File> cutoffFolders) {
        // Test if path should be cut-off
        if (path.isDirectory() && cutoffFolders.test(path)) {
            return Stream.empty();
        }

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
     * Compresses the entries into the given zipFile. Uses basePath to calculate the
     * root of entries in the zip.
     *
     */
    public static void zip(List<File> entries, File basePath, File zipFile) {

        try (FileOutputStream outStream = new FileOutputStream(zipFile);
                ZipOutputStream zip = new ZipOutputStream(outStream)) {

            SpecsLogs.msgInfo("Zipping " + entries.size() + " files to '" + zipFile.getAbsolutePath() + "'");
            for (File entry : entries) {
                // Get relative path, to create ZipEntry
                Optional<String> entryPath = SpecsIo.getRelativePath(entry, basePath, true);

                if (entryPath.isEmpty()) {
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
            SpecsLogs.warn("Exception while zipping archive:\n", e);
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
            SpecsLogs.warn("Could not process path", e);
        }

        return false;
    }

    /**
     * Tests if a folder can be written.
     *
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
            var value = queryLine.substring(equalIndex + 1);

            query.put(key, value);
        }

        return query;
    }

    /**
     * The depth of a given File. If file has the path foo/bar/a.cpp, depth is 3.
     *
     */
    public static int getDepth(File file) {
        if (file == null || file.getPath().isBlank()) {
            return 0;
        }

        // Count 1 for current file
        var depth = 1;
        var parent = file.getParentFile();
        // Add one for each parent
        while (parent != null) {
            depth++;
            file = parent;
            parent = file.getParentFile();
        }

        return depth;
    }

    /**
     * Removes query information of an URL string.
     *
     */
    public static String cleanUrl(String urlString) {
        var url = parseUrl(urlString)
                .orElseThrow(() -> new RuntimeException("Could not parse URL '" + urlString + "'"));

        return url.getProtocol() + "://" + url.getHost() + url.getPath();
    }
}
