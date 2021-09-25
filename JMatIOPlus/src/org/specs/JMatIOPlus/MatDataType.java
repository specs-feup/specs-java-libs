package org.specs.JMatIOPlus;

/**
 * MAT-file data types (adapted from JMatIO code)
 * 
 * @author Joao Bispo <joaobispo@gmail.com>
 */
public enum MatDataType {

    UNKNOWN(0, "unknown"),
    INT8(1, "int8", 1),
    UINT8(2, "uint8", 1),
    INT16(3, "int16", 2),
    UINT16(4, "uint16", 2),
    INT32(5, "int32", 4),
    UINT32(6, "uint32", 4),
    SINGLE(7, "single", 4),
    DOUBLE(9, "double", 8),
    INT64(12, "int64", 8),
    UINT64(13, "uint64", 8),
    MATRIX(14, "matrix", 1),
    COMPRESS(15, "compressed", 1),
    UTF8(16, "uft8", 1),
    UTF16(17, "utf16", 2),
    UTF32(18, "utf32", 4);

    public static final int miSIZE_DOUBLE = 8;
    public static final int miSIZE_CHAR = 1;

    // Not sure about these ones, but when returned the value 1 as default, it worked
    public static final int miSIZE_COMPRESSED = 1;
    public static final int miSIZE_MATRIX = 1;
    public static final int miSIZE_UTF8 = 1;
    public static final int miSIZE_UTF16 = 2;

    private final int id;
    private final String type;
    private final int numBytes;

    MatDataType(int id, String type) {
        this(id, type, -1);
    }

    private MatDataType(int id, String type, int numBytes) {
        this.id = id;
        this.type = type;
        this.numBytes = numBytes;
    }

    /**
     * Return number of bytes for given type.
     * 
     * @param type
     *            - <code>MatDataTypes</code>
     * @return
     */
    public int getNumBytes() {
        if (numBytes == -1) {
            throw new RuntimeException("Not defined for '" + this + "'");
        }

        return numBytes;
    }

    /**
     * Get String representation of a data type
     * 
     * @param type
     *            - data type
     * @return - String representation
     */
    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

}
