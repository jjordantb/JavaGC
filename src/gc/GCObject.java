package gc;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    public GCObject(T t, Object parent, boolean forceHeap) {
        this.t = t;
        this.parent = parent;
        try {
            GCHeapImpl.getMemory().submit(this, forceHeap);
        } catch (GCHeapOverflowException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public GCObject(T t, boolean forceHeap) {
        this(t, null, forceHeap);
    }

    public GCObject(T t) {
        this(t, null, false);
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

    /**
     *
     * Retrieves all of the children of the given object with reflection
     * @return
     */
    public List<GCObject> getChildren() {
        final List<GCObject> children = new ArrayList<>();
        try {
            final Field[] cfs = this.t.getClass().getDeclaredFields();
            for (Field f : cfs) {
                if (f.getAnnotation(GCField.class) != null) {
                    f.setAccessible(true);
                    children.add((GCObject) f.get(this.t));
                } else if (f.getAnnotation(GCList.class) != null) {
                    f.setAccessible(true);
                    final List<GCObject> objs = (List<GCObject>) f.get(this.t);
                    children.addAll(objs);
                }
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return children;
    }

}
