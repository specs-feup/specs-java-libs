package pt.up.fe.specs.util.threadstream;

import pt.up.fe.specs.util.collections.concurrentchannel.ChannelConsumer;

public class GenericObjectStream<T> extends AObjectStream<T> implements ObjectStream<T> {

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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public void close() throws Exception {
        // TODO Auto-generated method stub
        // TODO: how to implement here??
    }
}
