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
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.providers.ResourceProvider;

/**
 * Reads lines from a String, one by one.
 * 
 * @author Joao Bispo
 */
public class StringLines implements Iterable<String> {

    /**
     * INSTANCE VARIABLES
     */
    private final BufferedReader reader;
    private int currentLineIndex;
    private String nextLine;

    private boolean fileEnded;

    /**
     * Default CharSet used in file operations.
     */
    public static final String DEFAULT_CHAR_SET = "UTF-8";

    /**
     * Private constructor for static creator method.
     *
     */
    private StringLines(BufferedReader reader) {
        this.reader = reader;

        currentLineIndex = 0;
        fileEnded = false;

        nextLine = nextLineHelper();
    }

    /**
     * Builds a StringLines from the given String. If the object could not be
     * created, throws an exception.
     *
     */
    public static StringLines newInstance(String string) {
        StringReader reader = new StringReader(string);
        BufferedReader newReader = new BufferedReader(reader);
        return new StringLines(newReader);
    }

    public int getLastLineIndex() {
        return currentLineIndex;
    }

    /**
     * @return the next line in the file, or null if the end of the stream has been
     *         reached.
     */
    public String nextLine() {
        if (nextLine != null) {
            currentLineIndex++;
        }

        String currentLine = nextLine;

        nextLine = nextLineHelper();

        return currentLine;
    }

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
            String line = reader.readLine();

            // If we got to the end of the stream mark it, and close reader.
            if (line == null) {
                fileEnded = true;
                reader.close();
            }

            return line;
        } catch (IOException ex) {
            throw new RuntimeException("Could not read line.", ex);
        }

    }

    /**
     * @return the next line which is not empty, or null if the end of the stream
     *         has been reached.
     */
    public String nextNonEmptyLine() {
        boolean foundAnswer = false;
        while (!foundAnswer) {
            String line = nextLine();

            if (line == null) {
                return line;
            }

            if (!line.isEmpty()) {
                return line;
            }

        }

        return null;
    }

    public static List<String> getLines(String string) {
        return getLines(StringLines.newInstance(string));
    }

    public static List<String> getLines(ResourceProvider resource) {
        return getLines(newInstance(SpecsIo.getResource(resource)));
    }

    public static List<String> getLines(File file) {
        return getLines(newInstance(SpecsIo.read(file)));
    }

    private static List<String> getLines(StringLines lineReader) {
        List<String> lines = new ArrayList<>();

        String line;
        while ((line = lineReader.nextLine()) != null) {
            lines.add(line);
        }

        return lines;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {

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

    /**
     * Creates a stream over the LineReader. LineReader has to be disposed after
     * use.
     *
     */
    public Stream<String> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}
