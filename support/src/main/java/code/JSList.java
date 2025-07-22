package code;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JSList<T> extends ArrayList<T> {

    public JSList() {
        super();
    }

    @SuppressWarnings("unchecked")
    public JSList(T... elements) {
        super();
        for (T t : elements) {
            this.add(t);
        }
    }

    public JSList(List<T> list) {
        super(list);
    }

    public <R> JSList<R> map(Function<T, R> function) {
        JSList<R> result = new JSList<>();
        for (T item : this) {
            result.add(function.apply(item));
        }
        return result;
    }

    public <R> JSList<R> map(BiFunction<T, Integer, R> function) {
        JSList<R> result = new JSList<>();
        for (int i = 0; i < this.size(); i++) {
            result.add(function.apply(this.get(i), i));
        }
        return result;
    }

    public JSList<T> filter(Predicate<T> predicate) {
        JSList<T> result = new JSList<>();
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

    public <R> JSList<R> flatMap(Function<T, JSList<R>> mapper) {
        JSList<R> result = new JSList<>();
        for (T item : this) {
            result.addAll(mapper.apply(item));
        }
        return result;
    }

    public JSList<T> slice(int start, int end) {
        if (start < 0) start = 0;
        if (end > this.size()) end = this.size();
        return new JSList<>(this.subList(start, end));
    }

    public JSList<T> slice(int start) {
        return slice(start, this.size());
    }   

    public JSList<T> concat(JSList<T> other) {
        JSList<T> result = new JSList<>(this);
        result.addAll(other);
        return result;
    }   

    @SuppressWarnings("unchecked")
    public JSList<T> concat(T... elements) {
        JSList<T> result = new JSList<>(this);
        Collections.addAll(result, elements);
        return result;
    }   

    public JSList<T> reverse() {
        JSList<T> result = new JSList<>(this);
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
        if (!(o instanceof JSList)) return false;
        JSList<?> JSList = (JSList<?>) o;
        return super.equals(JSList);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public String toString() {
        return "JSList{" + super.toString() + '}';
    }

    @SuppressWarnings("unchecked")
    public static <T> JSList<T> of(T... elements) {
        return new JSList<>(elements);
    }

    public static <T> JSList<T> from(Collection<T> collection) {
        return new JSList<>(new ArrayList<>(collection));
    }

    public static <T> JSList<T> from(T[] array) {
        return new JSList<>(Arrays.asList(array));
    }

    public static <T> JSList<T> empty() {
        return new JSList<>();
    }

    public static <T> JSList<T> ofSize(int size) {
        return new JSList<>(Collections.nCopies(size, null));
    }

    public static <T> JSList<T> ofSize(int size, T defaultValue) {
        JSList<T> array = new JSList<>();
        for (int i = 0; i < size; i++) {
            array.add(defaultValue);
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <T> JSList<T> range(int start, int end) {
        JSList<T> array = new JSList<>();
        for (int i = start; i < end; i++) {
            array.add((T) Integer.valueOf(i));
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public static <T> JSList<T> range(int start, int end, int step) {
        JSList<T> array = new JSList<>();
        for (int i = start; i < end; i += step) {
            array.add((T) Integer.valueOf(i));
        }
        return array;
    }

    public static <T> JSList<T> repeat(T item, int times) {
        JSList<T> array = new JSList<>();
        for (int i = 0; i < times; i++) {
            array.add(item);
        }
        return array;
    }

    public static <T> JSList<T> fromStream(Stream<T> stream) {
        return stream.collect(Collectors.toCollection(JSList::new));
    }

    public static <T> JSList<T> fromIterable(Iterable<T> iterable) {
        JSList<T> array = new JSList<>();
        for (T item : iterable) {
            array.add(item);
        }
        return array;
    }

    public static <T> JSList<T> fromEnumeration(Enumeration<T> enumeration) {
        JSList<T> array = new JSList<>();
        while (enumeration.hasMoreElements()) {
            array.add(enumeration.nextElement());
        }
        return array;
    }

    public static <T> JSList<T> fromIterator(Iterator<T> iterator) {
        JSList<T> array = new JSList<>();
        while (iterator.hasNext()) {
            array.add(iterator.next());
        }
        return array;
    }

    public static <T> JSList<T> fromSpliterator(Spliterator<T> spliterator) {
        JSList<T> array = new JSList<>();
        spliterator.forEachRemaining(array::add);
        return array;
    }

    public static <T> JSList<T> fromCollection(Collection<T> collection) {
        return new JSList<>(new ArrayList<>(collection));
    }

    public static <T> JSList<T> fromArray(T[] array) {
        return new JSList<>(Arrays.asList(array));
    }

    public static <T> JSList<T> fromList(List<T> list) {
        return new JSList<>(list);
    }

    public static <T> JSList<T> fromSet(Set<T> set) {
        return new JSList<>(new ArrayList<>(set));
    }

    public static <T> JSList<T> fromMap(Map<?, T> map) {
        JSList<T> array = new JSList<>();
        for (T value : map.values()) {
            array.add(value);
        }
        return array;
    }

    public static <T> JSList<T> fromOptional(Optional<T> optional) {
        JSList<T> array = new JSList<>();
        optional.ifPresent(array::add);
        return array;
    }

    public static <T> JSList<T> fromOptional(Optional<T> optional, T defaultValue) {
        JSList<T> array = new JSList<>();
        array.add(optional.orElse(defaultValue));
        return array;
    }

    public static <T> JSList<T> fromStream(Stream<T> stream, int limit) {
        return stream.limit(limit).collect(Collectors.toCollection(JSList::new));
    }

}
