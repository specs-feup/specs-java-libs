/*
 * Copyright 2010 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Opens a stream and reads lines, one by one.
 * 
 * @author Joao Bispo
 */
public class LineStream implements AutoCloseable {

    /**
     * INSTANCE VARIABLES
     */
    private final BufferedReader reader;
    private int currentLineIndex;
    private String nextLine;
    private final Optional<String> name;

    private boolean fileEnded;

    private BufferedStringBuilder dumpFile;

    /**
     * Default CharSet used in file operations.
     */
    public static final String DEFAULT_CHAR_SET = "UTF-8";

    /**
     * Private constructor for static creator method.
     * 
     * @param reader
     */
    private LineStream(BufferedReader reader, Optional<String> filename) {
        this.reader = reader;
        name = filename;

        currentLineIndex = 0;
        fileEnded = false;

        dumpFile = null;

        nextLine = nextLineHelper();

    }

    public void setDumpFile(File file) {
        this.dumpFile = file == null ? null : new BufferedStringBuilder(file);
    }

    /**
     * Helper method which uses the name of the resource as the name of the stream by default.
     * 
     * @param resource
     * @return
     */
    public static LineStream newInstance(ResourceProvider resource) {
        return newInstance(resource, true);
    }

    /**
     * Creates a new LineStream from a resource.
     * 
     * @param resource
     * @param useResourceName
     *            if true, uses the resource name as the name of the line reader. Otherwise, uses no name
     * @return
     */
    public static LineStream newInstance(ResourceProvider resource, boolean useResourceName) {
        final Optional<String> resourceName = useResourceName ? Optional.of(resource.getResourceName()) : Optional
                .empty();

        InputStreamReader reader;
        try {
            reader = new InputStreamReader(SpecsIo.resourceToStream(resource), LineStream.DEFAULT_CHAR_SET);
        } catch (final IOException e) {
            throw new RuntimeException("Problem while using LineReader backed by a resource", e);
        }

        return newInstance(reader, resourceName);
    }

    /**
     * 
     * @param file
     * @return a new LineStream backed by the given file. If the object could not be created, throws a RuntimeException.
     */
    // Cannot close resource, since the stream must remain open after LineStream is created.
    // However, LineStream is a decorator of the FileInputStream, that will close it when the LineStream is closed
    public static LineStream newInstance(File file) {

        try {
            final FileInputStream fileStream = new FileInputStream(file);
            final InputStreamReader streamReader = new InputStreamReader(fileStream, LineStream.DEFAULT_CHAR_SET);
            // return newInstance(streamReader, Optional.of(file.getAbsolutePath()));

            return newInstance(streamReader, Optional.of(file.getName()));
        } catch (final IOException e) {
            throw new RuntimeException("Problem while using LineStream backed by a file", e);
        }
    }

    public static LineStream newInstance(String string) {
        try {
            return newInstance(new ByteArrayInputStream(string.getBytes("UTF-8")), null);
        } catch (final IOException e) {
            throw new RuntimeException("Problem while using LineStream backed by a String", e);
        }

        /*        
        try {
            final InputStreamReader streamReader = new InputStreamReader(
                    new ByteArrayInputStream(string.getBytes("UTF-8")));
        
            return newInstance(streamReader, Optional.empty());
        
        } catch (final IOException e) {
            throw new RuntimeException("Problem while using LineStream backed by a String", e);
        }
        */
    }

    public static LineStream newInstance(InputStream inputStream, String name) {
        final InputStreamReader streamReader = new InputStreamReader(inputStream);
        return newInstance(streamReader, Optional.ofNullable(name));
    }

    /**
     * 
     * @param reader
     * @param name
     * @return a new LineStream backed by the given Reader. If the object could not be created, throws a
     *         RuntimeException.
     */
    public static LineStream newInstance(Reader reader, Optional<String> name) {
        final BufferedReader newReader = new BufferedReader(reader);
        return new LineStream(newReader, name);

    }

    public int getLastLineIndex() {
        return currentLineIndex;
    }

    public Optional<String> getFilename() {
        return name;
    }

    /**
     * TODO: Rename 'next'
     * 
     * @return the next line in the file, or null if the end of the stream has been reached.
     */
    public String nextLine() {
        if (nextLine != null) {
            currentLineIndex++;
        }

        final String currentLine = nextLine;

        nextLine = nextLineHelper();

        // Log to file
        if (dumpFile != null) {
            dumpFile.append(currentLine).append("\n");
        }

        return currentLine;
    }

    /**
     * TODO: Rename hasNext
     * 
     * @return
     */
    public boolean hasNextLine() {
        return nextLine != null;
    }

    private String nextLineHelper() {
        // If file already ended, return null
        if (fileEnded) {
            return null;
        }

        try {
            // Read next line
            final String line = reader.readLine();

            // If we got to the end of the stream mark it, and close reader.
            if (line == null) {
                fileEnded = true;
                reader.close();
            }

            return line;
        } catch (final IOException ex) {
            throw new RuntimeException("Could not read line.", ex);
            // LoggingUtils.msgWarn("Could not read line.", ex);
            // return null;
        }
    }

    /**
     * @return the next line which is not empty, or null if the end of the stream has been reached.
     */
    public String nextNonEmptyLine() {
        for (;;) {
            final String line = nextLine();

            if (line == null) {
                return null;
            }

            if (!line.isEmpty()) {
                return line;
            }
        }
    }

    public static List<String> readLines(File file) {
        return readLines(LineStream.newInstance(file));
    }

    private static List<String> readLines(LineStream lineReader) {
        final List<String> lines = new ArrayList<>();
        String line = null;
        while ((line = lineReader.nextLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

    /**
     * Creates an Iterable over the LineReader. LineReader has to be disposed after use.
     * 
     * @return
     */
    public Iterable<String> getIterable() {
        return new LineReaderIterator();
    }

    /**
     * Implements an Iterable over a LineReader.
     * 
     * @author JoaoBispo
     *
     */
    private class LineReaderIterator implements Iterable<String> {

        @Override
        public Iterator<String> iterator() {
            return new Iterator<String>() {

                @Override
                public boolean hasNext() {
                    return hasNextLine();
                }

                @Override
                public String next() {
                    return nextLine();
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("LineReader does not support 'remove'.");

                }
            };
        }

    }

    /**
     * Creates a stream over the LineReader. LineReader has to be disposed after use.
     * 
     * @return
     */
    public Stream<String> stream() {
        return StreamSupport.stream(getIterable().spliterator(), false);
    }

    @Override
    public void close() {
        try {
            reader.close();
            if (dumpFile != null) {
                dumpFile.close();
            }
        } catch (final IOException e) {
            SpecsLogs.msgWarn("Could not close LineReader.", e);
        }
    }

}
