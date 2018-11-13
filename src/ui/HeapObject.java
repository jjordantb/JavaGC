package ui;

import gc.GCObject;
import util.Exec;

import java.awt.*;

public class HeapObject {

    private final Rectangle bounds;
    private final GCObject object;

    public HeapObject(GCObject object, Rectangle r) {
        this.object = object;
        this.bounds = r;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public GCObject getObject() {
        return object;
    }

    public Point getPointer(boolean left) {
        return new Point(this.bounds.x + (left ? this.bounds.width : 0), this.bounds.y + (this.bounds.height / 2));
    }

}
