package pt.up.fe.specs.util.threadstream;

public abstract class AObjectStream<T> implements ObjectStream<T> {

    private boolean inited = false;
    private boolean isClosed = false;
    private T currentT, nextT;
    private T poison;

    public AObjectStream(T poison) {
        this.currentT = null;
        this.nextT = null;
        this.poison = poison;
    }

    /*
     * MUST be implemented by children (e.g., may come from a ConcurrentChannel, or
     * Linestream, etc
     */
    protected abstract T consumeFromProvider();

    protected T getNext() {

        if (this.isClosed())
            return null;

        T inst = this.consumeFromProvider();

        // convert poison to null
        if (inst == this.poison) {
            this.isClosed = true;
            inst = null;
        }

        return inst;
    }

    @Override
    public T next() {

        /*
         * First call of getNext is done here instead of the constructor, since
         * the channel may block if this ObjectStream is used (as it should)
         * to read from a ChannelProducer<T> which executes in another thread
         * which may not have yet been launched
         */
        if (this.inited == false) {
            this.nextT = this.getNext();
            this.inited = true;
        }

        if (this.nextT == null) {
            this.isClosed = true;
            return null;
        }

        this.currentT = this.nextT;
        this.nextT = this.getNext();
        return this.currentT;
    }

    @Override
    public T peekNext() {
        return this.nextT;
    }

    @Override
    public boolean hasNext() {
        if (this.inited == false)
            return true;
        else
            return this.nextT != null;
        // return !this.isClosed();
    }

    @Override
    public boolean isClosed() {
        return this.isClosed;
    }
}
