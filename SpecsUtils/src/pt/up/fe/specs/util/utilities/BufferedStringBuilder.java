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

    public static BufferedStringBuilder nullStringBuilder() {
        return new NullStringBuilder();
    }

    /**
     * INSTANCE VARIABLES
     */
    private final File writeFile;
    private StringBuilder builder;
    private final int bufferCapacity;

    public final static int DEFAULT_BUFFER_CAPACITY = 800000;
    private static String newline = System.getProperty("line.separator");

    private boolean isClosed;

    public BufferedStringBuilder(File outputFile) {
        this(outputFile, BufferedStringBuilder.DEFAULT_BUFFER_CAPACITY);
    }

    /**
     * WARNING: The contents of the file given to this class will be erased when the object is created.
     * 
     * @param outputFile
     */
    public BufferedStringBuilder(File outputFile, int bufferCapacity) {
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
        // IoUtils.append(writeFile, builder.toString());
        this.builder = null;
        this.isClosed = true;
    }

    public BufferedStringBuilder append(int integer) {
        return append(Integer.toString(integer));
    }

    public BufferedStringBuilder append(Object object) {
        return append(object.toString());
    }

    /**
     * Appends the system-dependent newline.
     * 
     * @return
     */
    public BufferedStringBuilder appendNewline() {
        return append(BufferedStringBuilder.newline);
    }

    public BufferedStringBuilder append(String string) {

        if (this.builder == null) {
            SpecsLogs.getLogger().warning("Object has already been closed.");
            return null;
        }

        // Add to StringBuilder
        this.builder.append(string);
        // System.out.println("BUILDER ("+this.hashCode()+"):\n"+builder.toString());

        // if (builder.length() > DEFAULT_BUFFER_CAPACITY) {
        if (this.builder.length() >= this.bufferCapacity) {
            // System.out.println("ADASDADADADASD BUILDER ("+this.hashCode()+"):\n"+builder.toString());
            save();
        }

        return this;
    }

    public void save() {
        SpecsIo.append(this.writeFile, this.builder.toString());
        this.builder = newStringBuilder();
    }

    private StringBuilder newStringBuilder() {
        if (this.writeFile == null) {
            return null;
        }

        return new StringBuilder((int) (this.bufferCapacity * 1.10));
    }

}
