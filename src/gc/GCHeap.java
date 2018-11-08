package gc;

/**
 * Heap implementation
 *
 * @author Jordan Florchinger
 */
public class GCHeap {

    private int carat = 0;
    private GCObject[] heap;
    private int maxSize;

    public GCHeap(final int maxSize) {
        this.maxSize = maxSize;
        this.heap = new GCObject[this.maxSize];
    }

    /**
     * Appends a gc.GCObject to the heap at it's current location
     *
     * If heap size isn't fixed when overflowing it will allocate double the current size
     * @param object
     * @throws GCHeapOverflowException
     */
    public void append(final GCObject object) throws GCHeapOverflowException {
        if (this.carat < this.heap.length) {
            this.heap[carat++] = object;
        } else {
            // Run Garbage Collection
            /*

                // Double the size of the heap for re-allocation
                final gc.GCObject[] tmpObjects = new gc.GCObject[this.maxSize *= 2];
                // Copy the contents of the current heap to the new one
                gc.GCObject[] heap1 = this.heap;
                for (int i = 0; i < heap1.length; i++) {
                    gc.GCObject o = heap1[i];
                    if (o != null) {
                        tmpObjects[i] = o;
                    }
                }
                this.heap = tmpObjects;

             */
        }
    }

    /**
     * Set an index of the heap to the provided gc.GCObject
     * @param index
     * @param object
     */
    public void set(final int index, final GCObject object) {
        heap[index] = object;
    }

    /**
     * Retrieve a gc.GCObject from the heap
     * @param index
     * @return null if out of bounds
     */
    public GCObject get(final int index) {
        if (index < this.heap.length) {
            return this.heap[index];
        }
        return null;
    }

}
