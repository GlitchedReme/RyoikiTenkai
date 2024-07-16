package utils;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ObjectQueue<T> {
    ArrayList<T> list = new ArrayList<>();
    int index;

    public void reset() {
        index = 0;
    }

    public T get(Supplier<T> generator) {
        if (list.size() <= index) {
            list.add(generator.get());
        }
        return list.get(index++);
    }
}
