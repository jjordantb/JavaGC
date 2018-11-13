package ui;

import ex1.Family;
import ex1.Job;
import ex1.Person;
import gc.GCHeapImpl;
import gc.GCObject;
import gc.GCThread;
import ui.Frame;
import util.Exec;

public class UITest {

    public static Frame frame = new Frame();

    private static final Runnable runnable = () -> {

        final GCObject<Family> family = new GCObject<>(new Family());
        System.out.println("Created Family, HeapCount: " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        final GCObject<Person> dad = new GCObject<>(new Person("Dad", new Job("Oil Patch")));
        System.out.println("Created Dad, HeapCount: " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        final GCObject<Person> mom = new GCObject<>(new Person("Mom", new Job("Bank Teller")));
        System.out.println("Created Mom, HeapCount: " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        final GCObject<Person> daughter = new GCObject<>(new Person("Daughter", new Job("Prison Guard")));
        System.out.println("Created Daughter, HeapCount: " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        final GCObject<Person> son = new GCObject<>(new Person("Son", new Job("Programmer")));
        System.out.println("Created Son, HeapCount: " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        final GCObject<Person> cousin = new GCObject<>(new Person("Cousin", new Job("Professional Child")));
        System.out.println("Created Cousin, HeapCount: " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        for (int i = 0; i < 5; i++) {
            cousin.get().setName("Leroy" + i);
            System.out.println("Iteration " + i + " Heap " + GCHeapImpl.getMemory().getCarat());
            frame.repaint();
            Exec.delay(Exec.SLEEP);
        }

        family.get().addPerson(dad);
        System.out.println("Added Dad to family " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        family.get().addPerson(mom);
        System.out.println("Added mom to family " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        family.get().addPerson(daughter);
        System.out.println("Added daughter to family " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

        family.get().addPerson(son);
        System.out.println("Added son to family " + GCHeapImpl.getMemory().getCarat());
        frame.repaint();
        Exec.delay(Exec.SLEEP);

    };

    public static void main(String[] args) {
        GCHeapImpl.initHeap(60); // Keep it small for tests
        for (int i = 0; i < 10; i++) { // Run 5 threads
            frame.repaint();
            Exec.delay(Exec.SLEEP * 4);
            new GCThread(runnable);
            frame.repaint();
            Exec.delay(Exec.SLEEP * 4);
        }
    }

}
