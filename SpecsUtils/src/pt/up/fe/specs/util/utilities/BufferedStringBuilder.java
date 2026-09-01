/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */

package pt.up.fe.specs.util.utilities;

import java.io.File;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;

/**
 * Similar to a StringBuilder, but buffered, for writing large files.
 * 
 * <p>
 * Default buffer size is close to 1.5MBytes.
 * 
 * @author Joao Bispo
 */
public class BufferedStringBuilder implements AutoCloseable {

    /**
     * INSTANCE VARIABLES
     */
    private final File writeFile;
    private StringBuilder builder;
    private final int bufferCapacity;

    /**
     * Cache of persisted content written via save() so toString() doesn't need to
     * re-read the file from disk. This keeps a consistent snapshot of persisted
     * content and improves performance for repeated toString() calls.
     */
    private final StringBuilder persistedContent = new StringBuilder();

    public final static int DEFAULT_BUFFER_CAPACITY = 800000;
    private static final String newline = System.lineSeparator();

    private boolean isClosed;

    public BufferedStringBuilder(File outputFile) {
        this(validateOutputFile(outputFile), BufferedStringBuilder.DEFAULT_BUFFER_CAPACITY);
    }

    private static File validateOutputFile(File outputFile) {
        if (outputFile == null) {
            throw new IllegalArgumentException("Output file cannot be null");
        }
        return outputFile;
    }

    /**
     * WARNING: The contents of the file given to this class will be erased when the
     * object is created.
     *
     */
    public BufferedStringBuilder(File outputFile, int bufferCapacity) {
        this(outputFile, bufferCapacity, true);
    }

    /**
     * Protected constructor for internal use (e.g., NullStringBuilder)
     */
    protected BufferedStringBuilder(File outputFile, int bufferCapacity, boolean validateFile) {
        if (validateFile && outputFile == null) {
            throw new IllegalArgumentException("Output file cannot be null");
        }
        this.writeFile = outputFile;
        this.bufferCapacity = bufferCapacity;

        // Erase last trace
        if (outputFile != null) {
            SpecsIo.write(outputFile, "");
        }
        this.builder = newStringBuilder();

        this.isClosed = false;
    }

    @Override
    public void close() {
        if (this.isClosed) {
            return;
        }

        save();
        this.builder = null;
        this.isClosed = true;
    }

    public BufferedStringBuilder append(int integer) {
        return append(Integer.toString(integer));
    }

    public BufferedStringBuilder append(Object object) {
        if (object == null) {
            return append("null");
        }
        return append(object.toString());
    }

    /**
     * Appends the system-dependent newline.
     *
     */
    public BufferedStringBuilder appendNewline() {
        return append(BufferedStringBuilder.newline);
    }

    public BufferedStringBuilder append(String string) {

        if (this.builder == null) {
            SpecsLogs.warn("Object has already been closed.");
            return null;
        }

        // Add to StringBuilder
        this.builder.append(string);

        if (this.builder.length() >= this.bufferCapacity) {
            save();
        }

        return this;
    }

    public void save() {
        if (this.writeFile != null && this.builder != null) {
            String toPersist = this.builder.toString();

            // Append to file
            SpecsIo.append(this.writeFile, toPersist);

            // Update persisted content cache
            if (!toPersist.isEmpty()) {
                this.persistedContent.append(toPersist);
            }

            this.builder = newStringBuilder();
        }
    }

    private StringBuilder newStringBuilder() {
        if (this.writeFile == null) {
            return null;
        }

        return new StringBuilder((int) (this.bufferCapacity * 1.10));
    }

    @Override
    public String toString() {
        // If this is a NullStringBuilder (no write file and no builder), return empty
        if (this.writeFile == null && this.builder == null) {
            return "";
        }

        // Compose persisted content (from saves) + current in-memory buffer
        StringBuilder result = new StringBuilder();
        if (this.persistedContent.length() > 0) {
            result.append(this.persistedContent);
        }

        if (this.builder != null && this.builder.length() > 0) {
            result.append(this.builder);
        }

        return result.toString();
    }

}
