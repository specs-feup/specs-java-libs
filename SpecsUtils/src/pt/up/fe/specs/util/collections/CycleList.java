package pt.up.fe.specs.util.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * List that cycles through its elements.
 */
public class CycleList<T> {

    private final List<T> elements;
    private int currentIndex;

    public CycleList(Collection<T> elements) {
        this.elements = new ArrayList<>(elements);
        this.currentIndex = 0;
    }

    public CycleList(CycleList<T> cycleList) {
        this(cycleList.elements);
    }

    public T next() {
        var value = elements.get(currentIndex);
        updateIndex();
        return value;
    }

    private void updateIndex() {
        currentIndex++;

        // Reset index if too big
        if (currentIndex == elements.size()) {
            currentIndex = 0;
        }
    }

    @Override
    public String toString() {
        return currentIndex + "@" + elements.toString();
    }

}
