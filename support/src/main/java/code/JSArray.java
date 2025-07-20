package code;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    public JSArray(List<T> list) {
        super(list);
    }

    public <R> JSArray<R> map(Function<T, R> function) {
        JSArray<R> result = new JSArray<>();
        for (T item : this) {
            result.add(function.apply(item));
        }
        return result;
    }

    public <R> JSArray<R> map(BiFunction<T, Integer, R> function) {
        JSArray<R> result = new JSArray<>();
        for (int i = 0; i < this.size(); i++) {
            result.add(function.apply(this.get(i), i));
        }
        return result;
    }

    public JSArray<T> filter(Predicate<T> predicate) {
        JSArray<T> result = new JSArray<>();
        for (T item : this) {
            if (predicate.test(item)) 
                result.add(item);
        }
        return result;
    }

    public T find(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item))
                return item;
        }
        return null;
    }

    public boolean some(Predicate<T> predicate) {
        for (T item : this) {
            if (predicate.test(item))
                return true;
        }
        return false;
    }

    public boolean all(Predicate<T> predicate) {
        for (T item : this) {
            if (!predicate.test(item))
                return false;
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
        if (this.isEmpty())
            throw new NoSuchElementException("Reduce of empty array with no initial value");
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

    public <R> JSArray<R> flatMap(Function<T, JSArray<R>> mapper) {
        JSArray<R> result = new JSArray<>();
        for (T item : this) {
            result.addAll(mapper.apply(item));
        }
        return result;
    }

    public JSArray<T> slice(int start, int end) {
        if (start < 0) start = 0;
        if (end > this.size()) end = this.size();
        return new JSArray<>(this.subList(start, end));
    }

    public JSArray<T> slice(int start) {
        return slice(start, this.size());
    }   

    public JSArray<T> concat(JSArray<T> other) {
        JSArray<T> result = new JSArray<>(this);
        result.addAll(other);
        return result;
    }   

    public JSArray<T> concat(T... elements) {
        JSArray<T> result = new JSArray<>(this);
        Collections.addAll(result, elements);
        return result;
    }   

    public JSArray<T> reverse() {
        JSArray<T> result = new JSArray<>(this);
        Collections.reverse(result);
        return result;
    }

    public T first() {
        if (this.isEmpty()) return null;
        return this.get(0);
    }

    public T last() {
        if (this.isEmpty()) return null;
        return this.get(this.size() - 1);
    }

    public boolean isEmpty() {
        return this.size() == 0;
    }

    public int size() {
        return super.size();
    }

    public T get(int index) {
        return super.get(index);
    }

    public boolean add(T item) {
        return super.add(item);
    }       

    public void add(int index, T item) {
        super.add(index, item);
    }

    public boolean remove(Object item) {
        return super.remove(item);
    }

    public T remove(int index) {
        return super.remove(index);
    }

    public void clear() {
        super.clear();
    }

    public boolean contains(Object item) {
        return super.contains(item);
    }

    public int indexOf(Object item) {
        return super.indexOf(item);
    }

    public int lastIndexOf(Object item) {
        return super.lastIndexOf(item);
    }

    public Object[] toArray() {
        return super.toArray();
    }

    public <R> R[] toArray(R[] a) {
        return super.toArray(a);
    }

    public Iterator<T> iterator() {
        return super.iterator();
    }

    public Spliterator<T> spliterator() {
        return super.spliterator();
    }

    public Stream<T> stream() {
        return super.stream();
    }

    public Stream<T> parallelStream() {
        return super.parallelStream();
    }

    public void forEach(Consumer<? super T> action) {
        super.forEach(action);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JSArray)) return false;
        JSArray<?> jsArray = (JSArray<?>) o;
        return super.equals(jsArray);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "JSArray{" + super.toString() + '}';
    }

    public static <T> JSArray<T> of(T... elements) {
        return new JSArray<>(elements);
    }

    public static <T> JSArray<T> from(Collection<T> collection) {
        return new JSArray<>(new ArrayList<>(collection));
    }

    public static <T> JSArray<T> from(T[] array) {
        return new JSArray<>(Arrays.asList(array));
    }

    public static <T> JSArray<T> empty() {
        return new JSArray<>();
    }

    public static <T> JSArray<T> ofSize(int size) {
        return new JSArray<>(Collections.nCopies(size, null));
    }

    public static <T> JSArray<T> ofSize(int size, T defaultValue) {
        JSArray<T> array = new JSArray<>();
        for (int i = 0; i < size; i++) {
            array.add(defaultValue);
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <T> JSArray<T> range(int start, int end) {
        JSArray<T> array = new JSArray<>();
        for (int i = start; i < end; i++) {
            array.add((T) Integer.valueOf(i));
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <T> JSArray<T> range(int start, int end, int step) {
        JSArray<T> array = new JSArray<>();
        for (int i = start; i < end; i += step) {
            array.add((T) Integer.valueOf(i));
        }
        return array;
    }

    public static <T> JSArray<T> repeat(T item, int times) {
        JSArray<T> array = new JSArray<>();
        for (int i = 0; i < times; i++) {
            array.add(item);
        }
        return array;
    }

    public static <T> JSArray<T> fromStream(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(JSArray::new));
    }

    public static <T> JSArray<T> fromIterable(Iterable<T> iterable) {
        JSArray<T> array = new JSArray<>();
        for (T item : iterable) {
            array.add(item);
        }
        return array;
    }

    public static <T> JSArray<T> fromEnumeration(Enumeration<T> enumeration) {
        JSArray<T> array = new JSArray<>();
        while (enumeration.hasMoreElements()) {
            array.add(enumeration.nextElement());
        }
        return array;
    }

    public static <T> JSArray<T> fromIterator(Iterator<T> iterator) {
        JSArray<T> array = new JSArray<>();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static <T> JSArray<T> fromSpliterator(Spliterator<T> spliterator) {
        JSArray<T> array = new JSArray<>();
        spliterator.forEachRemaining(array::add);
        return array;
    }

    public static <T> JSArray<T> fromCollection(Collection<T> collection) {
        return new JSArray<>(new ArrayList<>(collection));
    }

    public static <T> JSArray<T> fromArray(T[] array) {
        return new JSArray<>(Arrays.asList(array));
    }

    public static <T> JSArray<T> fromList(List<T> list) {
        return new JSArray<>(list);
    }

    public static <T> JSArray<T> fromSet(Set<T> set) {
        return new JSArray<>(new ArrayList<>(set));
    }

    public static <T> JSArray<T> fromMap(Map<?, T> map) {
        JSArray<T> array = new JSArray<>();
        for (T value : map.values()) {
            array.add(value);
        }
        return array;
    }

    public static <T> JSArray<T> fromOptional(Optional<T> optional) {
        JSArray<T> array = new JSArray<>();
        optional.ifPresent(array::add);
        return array;
    }

    public static <T> JSArray<T> fromOptional(Optional<T> optional, T defaultValue) {
        JSArray<T> array = new JSArray<>();
        array.add(optional.orElse(defaultValue));
        return array;
    }

    public static <T> JSArray<T> fromStream(Stream<T> stream, int limit) {
        return stream.limit(limit).collect(Collectors.toCollection(JSArray::new));
    }


    

}
