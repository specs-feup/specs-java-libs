package pt.up.fe.specs.util.threadstream;

public interface ObjectStream<T> extends AutoCloseable {

    /*
     * 
     */
    public T next();

    /*
     * 
     */
    public boolean hasNext();

    /*
     * 
     */
    public boolean isClosed();
}
