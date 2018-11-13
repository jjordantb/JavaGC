package ui;

import gc.GCObject;

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
}
