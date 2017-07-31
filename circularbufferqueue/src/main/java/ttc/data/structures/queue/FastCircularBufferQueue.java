package ttc.data.structures.queue;

/**
 * A queue implemented as a CircularBuffer allowing for constant time
 * operations
 * <p>
 * Overwriting of elements is allowed
 */
public class FastCircularBufferQueue<E> extends CircularBufferQueue<E> {

    public FastCircularBufferQueue(final int capacity) {
        super(capacity);
    }

    public boolean add(E e) {
        return pushElement(e);
    }

    /*
     *  pushes an element onto the queue
     */
    protected boolean pushElement(final E element) {
        if (startIndex != endIndex) {
            endIndex = incrementIndex(endIndex);
        }
        array[endIndex] = element;
        size++;
        return true;
    }
}
