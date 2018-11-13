package gc;

import ui.UITest;
import util.Exec;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Heap implementation
 *
 * @author Jordan Florchinger
 */
public class GCMemory {

    private final Object lock = new Object();

    private int carat = 0;
    private GCObject[] heap;
    private int maxSize;
    private boolean blocking = false;

    private final ConcurrentHashMap<Long, List<GCObject>> gcRoots = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Long, Stack<GCObject>> threadStacks = new ConcurrentHashMap<>();

    public GCMemory(final int maxSize) {
        this.maxSize = maxSize;
        this.heap = new GCObject[this.maxSize];
    }


    /**
     * Appends a gc.GCObject to the heap at it's current location
     *
     * @param object
     */
    public void append(final GCObject object) throws GCHeapOverflowException {
        synchronized (this.lock) {
            if (this.carat < this.heap.length) {
                this.heap[carat++] = object;
                // Job is getting created but since the heap is full it's not getting pointed too
            } else {
                this.blocking = true;
                // Run Garbage Collection
                System.out.println("Garbage Collection Reached! " + this.carat + "/" + this.maxSize);
                int garbageRemoved = 0;

                // Mark all of the reference objects from the GCRoots
                for (List<GCObject> roots : this.gcRoots.values()) {
                    for (GCObject o : roots) {
                        o.setMarked(false);
                        this.markRecursive(o, 0);
                    }
                }
                // boolean flag if nothing could be collected
                boolean noneUnMarked = true;
                // Remove all unreferenced objects (set them to null), we can also un-mark all currently market objects
                // We can also compact while sweeping
                final GCObject[] newHeap = new GCObject[this.maxSize];
                this.carat = 0;
                for (int i = 0; i < this.heap.length; i++) {
                    final GCObject o;
                    if ((o = this.get(i)) != null) {
                        if (o.isMarked()) {
                            // Set to un-marked
                            o.setMarked(false);
                            // Store it in new heap
                            newHeap[this.carat++] = o;
                        } else {
                            // It's un-marked so remove it
                            noneUnMarked = false;
                            this.set(i, null);
                            garbageRemoved++;
                        }
                    }
                }
                if (noneUnMarked) {
                    throw new GCHeapOverflowException("Heap is Full, no Garbage to Collect");
                } else {
                    // Garbage has been collected, the compacted heap is newHeap
                    // Update heap to newHeap
                    UITest.frame.repaint();
                    Exec.delay(Exec.SLEEP * 2);
                    this.heap = newHeap;
                    UITest.frame.repaint();
                    Exec.delay(Exec.SLEEP * 2);
                    System.out.println("Removed " + garbageRemoved + " broken references");
                    this.blocking = false;
                }
            }
        }
    }


    /**
     * Move all of the objects from the calling stack to the heap
     */
    public void moveToHeap() throws GCHeapOverflowException {
        final long id = Thread.currentThread().getId();
        final Stack<GCObject> o = this.threadStacks.get(id);
        if (o != null) {
            while (!o.empty()) {
                this.append(o.pop());
            }
        }
    }


    /**
     * Pushes a GCObject to the calling threads Stack
     * @param object
     */
    public void pushToStack(final GCObject object) {
        final long id = Thread.currentThread().getId();
        if (!this.threadStacks.containsKey(id)) {
            this.threadStacks.put(id, new Stack<>());
        }
        final List<GCObject> ts = this.threadStacks.get(id);
        if (!ts.contains(object)) {
            ts.add(object);
        }
    }


    /**
     * Gets the current position in the heap
     * @return
     */
    public int getCarat() {
        return carat;
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


    /**
     * Adds a GCRoot based on the calling thread
     * @param object
     */
    public void addRoot(final GCObject object) {
        final long id = this.getID();
        if (!this.gcRoots.containsKey(id)) {
            this.gcRoots.put(id, new ArrayList<>());
        }
        final List<GCObject> threadRoots = this.gcRoots.get(id);
        if (!threadRoots.contains(object)) {
            threadRoots.add(object);
        }
    }


    /**
     * Recursively goes through and marks objects
     * @param object
     */
    private int markRecursive(final GCObject object, int count) {
        if (object.isMarked()) {
            return count;
        }
        object.setMarked(true);
        List<GCObject> children = object.getChildren();
        System.out.println("Found " + children.size() + " children for " + object.get());
        for (GCObject child : children) {
            this.markRecursive(child, count++);
        }
        return count;
    }

    /**
     * Gets the id for the calling thread
     * @return
     */
    private long getID() {
        return Thread.currentThread().getId();
    }

    /**
     * Removes GCRoots and Thread Stack for the calling thread therefore breaking all references to heap objects
     * that will be cleaned when garbage collection is needed next
     */
    public void cleanUp() {
        synchronized (this.lock) {
            long id = this.getID();
            this.gcRoots.remove(id);
            this.threadStacks.remove(id);
        }
    }

    /**
     * Gets the Stack of the Calling thread
     * @return
     */
    public Stack<GCObject> getStack() {
        final long id = this.getID();
        final Stack<GCObject> o = this.threadStacks.get(id);
        if (o != null) {
            return o;
        }
        return null;
    }

    /**
     * Gets the heap objects
     * @return
     */
    public GCObject[] getHeap() {
        return heap;
    }

    public ConcurrentHashMap<Long, List<GCObject>> getGcRoots() {
        return gcRoots;
    }

    public ConcurrentHashMap<Long, Stack<GCObject>> getThreadStacks() {
        return threadStacks;
    }

    /**
     * Check if the heap is blocking access
     * @return
     */
    public boolean isBlocking() {
        return blocking;
    }

}
