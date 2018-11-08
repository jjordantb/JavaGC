package gc;

/**
 * Heap Implementation that handles root references
 *
 * Uses a Singleton for ease of use, sadly
 *
 * @author Jordan Florchinger
 */
public class GCHeapImpl {

    private final GCHeap heap;

    private GCHeapImpl(final int size) {
        this.heap = new GCHeap(size);
    }

    /* Singleton Start */

    private static GCHeapImpl heapImpl;

    public static GCHeapImpl getHeap() {
        if (heapImpl == null) {
            heapImpl = new GCHeapImpl(10);
        }
        return heapImpl;
    }

    public static void initHeap(final int size, final boolean fixedSize) {
        heapImpl = new GCHeapImpl(size);
    }

}
