package support;

import java.util.*;
import java.util.function.*;

public class JSArray<T> extends ArrayList<T> {

    public JSArray() {
        super();
    }
    
    @SuppressWarnings("unchecked")
    public JSArray(T... elements) {
        super();
        for (T t : elements) {
            this.add(t);
        }
    }

    public JSArray(List<T> list){
        super(list);
    }

    public <R> JSArray<R> map(Function<T, R> function) {
        JSArray<R> result = new JSArray<>();
        for (T item : this) {
            result.add(function.apply(item));
        }
        return result;
    }

    public JSArray<T> filter(Predicate<T> predicate) {
        JSArray<T> result = new JSArray<>();
        for (T item : this) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
    
    public T find(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item)) return item;
        }
        return null;
    }
    
    public boolean some(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item)) return true;
        }
        return false;
    }
    
    public boolean all(Predicate<T> predicate) {
        for (T item : this) {
            if (!predicate.test(item)) return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public void sort() {
        if (this.isEmpty() || !(this.get(0) instanceof Comparable))
            throw new IllegalStateException("Elements must implement Comparable or use a Comparator");
        this.sort((Comparator<? super T>) Comparator.naturalOrder());
    }

    public void sort(Comparator<? super T> comparator) {
        super.sort(comparator);
    }

    public T reduce(BinaryOperator<T> accumulator) {
        if (this.isEmpty()) throw new NoSuchElementException("Reduce of empty array with no initial value");
        T result = this.get(0);
        for (int i = 1; i < this.size(); i++) {
            result = accumulator.apply(result, this.get(i));
        }
        return result;
    }

    public <U> U reduce(U identity, BiFunction<U, ? super T, U> accumulator) {
        U result = identity;
        for (T item : this) {
            result = accumulator.apply(result, item);
        }
        return result;
    }

}
