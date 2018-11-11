package ex1;

import gc.GCField;
import gc.GCObject;

import java.util.Objects;

public class Job {

    @GCField
    private final GCObject<String> name;

    public Job(String name) {
        this.name = new GCObject<>(name, this, true);
    }

    public String getName() {
        return this.name.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(name, job.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

}
