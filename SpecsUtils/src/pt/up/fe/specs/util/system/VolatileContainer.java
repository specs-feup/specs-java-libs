package pt.up.fe.specs.util.system;

public class VolatileContainer<T> {

    private volatile T element;

    public VolatileContainer() {
        this(null);
    }

    public VolatileContainer(T element) {
        this.element = element;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

}
