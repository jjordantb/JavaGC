package gc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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
     */
    public void append(final GCObject object) {
        if (this.carat < this.heap.length) {
            this.heap[carat++] = object;
        } else {
            // Run Garbage Collection
            // Mark all of the reference objects
            for (GCObject test : this.heap) {
                if (test.isGCRoot()) {
                    this.markRecursive(test);
                }
            }
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
                        this.set(i, null);
                    }
                }
            }
            // Garbage has been collected, the compacted heap is newHeap
            // Update heap to newHeap
            this.heap = newHeap;
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


    /**
     * Recursively goes through and marks objects
     * @param object
     */
    private void markRecursive(final GCObject object) {
        if (object.isMarked()) {
            return;
        }
        object.setMarked(true);
        for (GCObject child : this.getChildren(object)) {
            this.markRecursive(child);
        }
    }


    /**
     * Retrieves all of the children of the given object with reflection
     * @param parent
     * @return
     */
    private List<GCObject> getChildren(final GCObject parent) {
        final List<GCObject> children = new ArrayList<>();
        final Field[] fields = parent.getClass().getDeclaredFields();
        for (Field field : fields) {
            // Check if the field is being tracked by the garbage collector
            if (field.getDeclaringClass().getName().contains("GCObject")) {
                try {
                    children.add((GCObject) field.get(parent));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return children;
    }

}
