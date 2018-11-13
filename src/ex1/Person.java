package ex1;

import gc.GCField;
import gc.GCObject;

import java.util.Objects;

public class Person {

    @GCField
    private GCObject<String> name;

    @GCField
    private GCObject<Job> job;

    public Person(String name, Job job) {
        this.name = new GCObject<>(name, this, false);
        this.job = new GCObject<>(job, this, false);
    }

    public String getName() {
        return this.name.get();
    }

    public Job getJob() {
        return this.job.get();
    }

    public void setName(String name) {
        this.name = new GCObject<>(name, this, true);
    }

    public void setJob(Job job) {
        this.job = new GCObject<>(job, this, true);
    }

}
