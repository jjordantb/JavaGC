package gc;

import util.Exec;

import java.util.List;
import java.util.Map;
import java.util.Stack;

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
    public void submit(final GCObject object, boolean forceHeap) throws GCHeapOverflowException {
        while (this.heap.isBlocking()) {
            Exec.delay(100);
        }
        if (forceHeap) {
            this.heap.append(object);
        } else {
            if (object.isGCRoot()) {
                this.heap.addRoot(object);
                this.heap.moveToHeap();
            } else {
                this.heap.pushToStack(object);
            }
        }
    }

    /**
     * Gets the current position in the heap
     * @return
     */
    public int getCarat() {
        return this.heap.getCarat();
    }

    /**
     * Get the size of the calling thread's stack
     * @return
     */
    public int getStackSize() {
        final Stack<GCObject> stack;
        return (stack = this.heap.getStack()) != null ? stack.size() : -1;
    }

    /**
     * Call before the end of the thread which clears roots and therefor enabling garbage collection on their
     * broke references
     */
    public void endOfThread() {
        this.heap.cleanUp();
    }

    /**
     * Gets the heap
     * @return
     */
    public GCObject[] getHeap() {
        return this.heap.getHeap();
    }

    public Map<Long, List<GCObject>> getRoots() {
        return this.heap.getGcRoots();
    }

    public Map<Long, Stack<GCObject>> getStacks() {
        return this.heap.getThreadStacks();
    }

    /* Singleton Start */

    private static GCHeapImpl heapImpl;

    public static GCHeapImpl getMemory() {
        if (heapImpl == null) {
            heapImpl = new GCHeapImpl(10);
        }
        return heapImpl;
    }

    public static void initHeap(final int size) {
        heapImpl = new GCHeapImpl(size);
    }

}
