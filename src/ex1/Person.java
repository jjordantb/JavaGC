package ex1;

import gc.GCObject;

import java.util.Objects;

public class Person {

    private GCObject<String> name;
    private GCObject<Job> job;

    public Person(String name, Job job) {
        this.name = new GCObject<>(name, this);
        this.job = new GCObject<>(job, this);
    }

    public String getName() {
        return this.name.get();
    }

    public Job getJob() {
        return this.job.get();
    }

    public void setName(String name) {
        this.name = new GCObject<>(name, this);
    }

    public void setJob(Job job) {
        this.job = new GCObject<>(job, this);
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
