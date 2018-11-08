package ex1;

import gc.GCObject;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Family {

    private final ArrayList<GCObject<Person>> people;

    public Family() {
        this.people = new ArrayList<>();
    }

    /**
     * Adds a person to the family if it doesn't already contain it
     * @param person
     */
    public void addPerson(final Person person) {
        boolean contains = false;
        for (GCObject<Person> personGCObject : this.people) {
            if (personGCObject.get().equals(person)) {
                contains = true;
            }
        }
        if (!contains) {
            people.add(new GCObject<>(person, this));
        }
    }

    /**
     * Removes person if they're in the family
     * @param person
     */
    public void removePerson(final Person person) {
        this.people.removeIf(personGCObject -> personGCObject.get().equals(person));
    }

    /**
     * Removes person if they match the predicate
     * @param predicate
     */
    public void removePerson(final Predicate<Person> predicate) {
        this.people.removeIf(personGCObject -> predicate.test(personGCObject.get()));
    }

}
