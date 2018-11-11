package ex1;

import gc.GCList;
import gc.GCObject;

import java.util.ArrayList;
import java.util.function.Predicate;

public class Family {

    @GCList
    private final ArrayList<GCObject<Person>> people;

    public Family() {
        this.people = new ArrayList<>();
    }

    /**
     * Adds a person to the family if it doesn't already contain it
     * @param person
     */
    public void addPerson(final GCObject<Person> person) {
        boolean contains = false;
        for (GCObject<Person> personGCObject : this.people) {
            if (personGCObject.get().equals(person.get())) {
                contains = true;
            }
        }
        if (!contains) {
            people.add(person);
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
