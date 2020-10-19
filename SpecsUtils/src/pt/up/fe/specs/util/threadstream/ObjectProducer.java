package pt.up.fe.specs.util.threadstream;

public interface ObjectProducer<T> extends AutoCloseable {

    /*
     * 
     */
    default T getPoison() {
        return null;
    };
}
