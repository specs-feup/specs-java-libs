package pt.up.fe.specs.util.threadstream;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class GenericObjectStream<T> extends AObjectStream<T> {

    private final ChannelConsumer<T> consumer;

    public GenericObjectStream(ChannelConsumer<T> consumer, T poison) {
        super(poison);
        this.consumer = consumer;
    }

    @Override
    protected T consumeFromProvider() {
        T ret = null;
        try {
            ret = this.consumer.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public void close() {
        // TODO: how to implement here??
    }
}
