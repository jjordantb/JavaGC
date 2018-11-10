package ex1;

import gc.GCHeapImpl;
import gc.GCObject;

public class FamilyTest {

    public static void main(String[] args) {
        // Init our HeapImpl
        GCHeapImpl.initHeap(20); // Keep it small for tests

        final GCObject<Family> family = new GCObject<>(new Family(), null);
        System.out.println("Created Family, HeapCount: " + GCHeapImpl.getHeap().getCarat());

        final GCObject<Person> dad = new GCObject<>(new Person("Dad", new Job("Oil Patch")), null);
        System.out.println("Created Dad, HeapCount: " + GCHeapImpl.getHeap().getCarat());

        final GCObject<Person> mom = new GCObject<>(new Person("Mom", new Job("Bank Teller")), null);
        System.out.println("Created Mom, HeapCount: " + GCHeapImpl.getHeap().getCarat());

        final GCObject<Person> daughter = new GCObject<>(new Person("Daughter", new Job("Prison Guard")), null);
        System.out.println("Created Daughter, HeapCount: " + GCHeapImpl.getHeap().getCarat());

        final GCObject<Person> son = new GCObject<>(new Person("Son", new Job("Programmer")), null);
        System.out.println("Created Son, HeapCount: " + GCHeapImpl.getHeap().getCarat());

        final GCObject<Person> cousin = new GCObject<>(new Person("Cousin", new Job("Professional Child")), null);
        System.out.println("Created Cousin, HeapCount: " + GCHeapImpl.getHeap().getCarat());

        // It's getting pushed to the stack but since there is no following GCRoot it's not entering the heap
        for (int i = 0; i < 5; i++) {
            cousin.get().setName("Leroy" + i);
            System.out.println("Iteration " + i + " Heap " + GCHeapImpl.getHeap().getCarat());
        }

//        family.get().addPerson(dad.get());
//        family.get().addPerson(mom.get());
//        family.get().addPerson(daughter.get());
//        family.get().addPerson(son.get());
    }

}
