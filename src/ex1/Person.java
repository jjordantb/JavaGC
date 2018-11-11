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
        this.name = new GCObject<>(name, this, true);
        this.job = new GCObject<>(job, this, true);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return Objects.equals(getName(), person.getName()) &&
                Objects.equals(getJob(), person.getJob());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getJob());
    }
}
