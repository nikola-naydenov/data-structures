package ttc.data.structures.queue;

import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * A queue implemented as a CircularBuffer allowing for constant time
 * operations
 * <p>
 * Overwriting of elements is not allowed
 */
public class CircularBufferQueue<E> implements Queue<E> {

    int size = 0;
    final Object[] array;
    int startIndex = 0;
    int endIndex = 0;

    public CircularBufferQueue(final int capacity) {
        this.array = new Object[capacity];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return (startIndex == endIndex)
                && array[startIndex] == null;
    }

    public boolean contains(final Object o) {
        for (Object item : array) {
            if (item.equals(o)) return true;
        }
        return false;
    }

    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private int currIndex = startIndex;

            @Override
            public boolean hasNext() {
                return array[incrementIndex(currIndex)] != null;
            }

            @Override
            public E next() {
                return (E) array[currIndex];
            }
        };
    }

    public Object[] toArray() {
        if (isEmpty()) return new Object[0];
        Object[] r = new Object[size];
        final Iterator<E> iter = this.iterator();
        int i = 0;
        while (iter.hasNext()) {
            r[i++] = iter.next();
        }
        return r;
    }

    public <T> T[] toArray(T[] a) {
        T[] r = a.length >= size ? a :
                (T[]) java.lang.reflect.Array
                        .newInstance(a.getClass().getComponentType(), size);
        final Iterator iter = this.iterator();
        int i = 0;
        while (iter.hasNext() && i < r.length) {
            r[i++] = (T) iter.next();
        }
        return r;
    }

    public boolean add(E e) {
        if (!pushElement(e)) {
            throw new IllegalStateException("Queue is full, cannot add more elements!");
        }
        return true;
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public boolean containsAll(Collection<?> c) {
        return c.stream().allMatch(this::contains);
    }

    public boolean addAll(Collection<? extends E> c) {
        c.forEach(this::add);
        return true;
    }

    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        for (int i = 0; i < array.length; i++) {
            array[i] = null;
        }
        startIndex = 0;
        endIndex = 0;
    }

    public boolean offer(E e) {
        if (e == null) throw new NullPointerException();
        return pushElement(e);
    }

    public E remove() {
        if (array[startIndex] == null) throw new NoSuchElementException("Queue is empty, cannot remove elements!");
        return popElement();
    }

    public E poll() {
        return popElement();
    }

    public E element() {
        if (array[startIndex] == null) throw new NoSuchElementException("Queue is empty");
        return (E) array[startIndex];
    }

    public E peek() {
        return (E) array[startIndex];
    }

    /*
     *  pops an element off the queue
     */
    private E popElement() {
        final E element = (E) array[startIndex];
        array[startIndex] = null;
        if (startIndex != endIndex) {
            startIndex = incrementIndex(startIndex);
            if (size > 0) size--;
        }
        return element;
    }

    /*
     *  pushes an element onto the queue
     */
    protected boolean pushElement(final E element) {
        if (startIndex != endIndex) {
            endIndex = incrementIndex(endIndex);
            if (array[endIndex] != null) {
                endIndex = decrementIndex(endIndex);
                return false;
            }
        }
        array[endIndex] = element;
        size++;
        return true;
    }

    /**
     * increments either start or end index taking into account the length
     * of the array
     *
     * @param index an index
     */
    int incrementIndex(int index) {
        if (index == (array.length - 1))
            index = 0;
        else
            index++;
        return index;
    }

    private int decrementIndex(int index) {
        if (index == 0)
            index = array.length - 1;
        else
            index--;
        return index;
    }
}
