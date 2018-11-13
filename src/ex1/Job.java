package ex1;

import gc.GCField;
import gc.GCObject;

import java.util.Objects;

public class Job {

    @GCField
    private final GCObject<String> name;

    public Job(String name) {
        this.name = new GCObject<>(name, this, false);
    }

    public String getName() {
        return this.name.get();
    }

}
