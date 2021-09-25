package com.jmatio.types;

import java.nio.ByteBuffer;

public class MLSingle extends MLNumericArray<Float>
{
    
    /**
     * Create a <code>MLSingle</code> array with given name,
     * and dimensions.
     * 
     * @param name - array name
     * @param dims - array dimensions
     */
    public MLSingle(String name, int[] dims)
    {
        super(name, dims, MLArray.mxSINGLE_CLASS, 0);
    }
    
    public MLSingle(String name, Float[] vals, int m)
    {
        super(name, MLArray.mxSINGLE_CLASS, vals, m);
    }

    public MLSingle(String name, int[] dims, int type, int attributes)
    {
        super(name, dims, type, attributes);
    }

    @Override
    public Float[] createArray(int m, int n)
    {
        return new Float[m*n];
    }

    @Override
    public Float buldFromBytes(byte[] bytes)
    {
        if ( bytes.length != getBytesAllocated() )
        {
            throw new IllegalArgumentException( 
                        "To build from byte array I need array of size: " 
                                + getBytesAllocated() );
        }
        return ByteBuffer.wrap( bytes ).getFloat();
    }

    @Override
    public byte[] getByteArray(Float value)
    {
        int byteAllocated = getBytesAllocated();
        ByteBuffer buff = ByteBuffer.allocate( byteAllocated );
        buff.putFloat( value );
        return buff.array();
    }

    @Override
    public int getBytesAllocated()
    {
        return Float.SIZE >> 3;
    }

    @Override
    public Class<?> getStorageClazz()
    {
        return Float.class;
    }

}
