package gc;

/**
 * Wrapper class that provides the functionality to mark objects on a higher level
 *
 * @author Jordan Florchinger
 * @param <T>
 */
public class GCObject<T> {

    private boolean marked = false;
    private final T t;
    private final Object parent;

    public GCObject(T t, Object parent) {
        this.t = t;
        this.parent = parent;
    }

    /**
     * Get the Object
     * @return
     */
    public T get() {
        return t;
    }

    /**
     * Used to check for GC roots
     * @return
     */
    public Object getParent() {
        return parent;
    }

    /**
     * Mark the object so it's not collected
     * @param marked
     */
    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    /**
     * Check if the Object is marked to not be collected
     * @return
     */
    public boolean isMarked() {
        return marked;
    }

    /**
     * Check if the GCObject is a GC root
     * @return
     */
    public boolean isGCRoot() {
        return this.parent == null;
    }

}
