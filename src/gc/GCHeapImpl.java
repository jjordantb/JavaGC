package gc;

/**
 * Heap Implementation that handles root references
 *
 * Uses a Singleton for ease of use, sadly
 *
 * @author Jordan Florchinger
 */
public class GCHeapImpl {

    private final GCMemory heap;

    private GCHeapImpl(final int size) {
        this.heap = new GCMemory(size);
    }

    /**
     * Adds a GCObject to the heap, if the GCObject is a root it is added to the calling threads GCRoots container
     * @param object
     */
    public void submit(final GCObject object) throws GCHeapOverflowException {
        if (object.isGCRoot()) {
            this.heap.addRoot(object);
            this.heap.moveToHeap();
        } else {
            this.heap.pushToStack(object);
        }
    }

    /**
     * Gets the current position in the heap
     * @return
     */
    public int getCarat() {
        return this.heap.getCarat();
    }

    /* Singleton Start */

    private static GCHeapImpl heapImpl;

    public static GCHeapImpl getHeap() {
        if (heapImpl == null) {
            heapImpl = new GCHeapImpl(10);
        }
        return heapImpl;
    }

    public static void initHeap(final int size) {
        heapImpl = new GCHeapImpl(size);
    }

}
