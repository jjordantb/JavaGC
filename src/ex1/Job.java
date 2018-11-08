package ex1;

import gc.GCObject;

public class Job {

    private final GCObject<String> name;

    public Job(GCObject<String> name) {
        this.name = name;
    }

}
