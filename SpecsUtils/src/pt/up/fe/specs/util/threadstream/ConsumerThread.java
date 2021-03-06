package pt.up.fe.specs.util.threadstream;

import java.util.function.Function;

import pt.up.fe.specs.util.SpecsCheck;

/**
 * 
 * @author nuno
 *
 * @param <T>
 *            Type of input object from ObjectStream
 * @param <K>
 *            Type of consumption output
 */
public class ConsumerThread<T, K> implements Runnable {

    private K consumeResult;
    private ObjectStream<T> ostream = null;
    private final Function<ObjectStream<T>, K> consumeFunction;

    protected ConsumerThread(Function<ObjectStream<T>, K> consumeFunction) {
        this.consumeFunction = consumeFunction;
    }

    protected void provide(ObjectStream<T> ostream) {
        this.ostream = ostream;
    }

    public ObjectStream<T> getOstream() {
        return ostream;
    }

    /*
     * Threaded workload
     */
    @Override
    public void run() {
        SpecsCheck.checkNotNull(this.ostream, () -> "Channel for this consumer object has not been provided!");
        this.consumeResult = this.consumeFunction.apply(this.ostream);
    }

    public K getConsumeResult() {
        return consumeResult;
    }
}
