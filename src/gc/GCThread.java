package gc;

/**
 * Wrapper of a Thread so that we can hide cleanup from the user
 */
public class GCThread implements Runnable {

    private final Runnable runnable;

    public GCThread(Runnable runnable) {
        this.runnable = runnable;
        new Thread(this).start();
    }

    @Override
    public void run() {
        System.out.println("Starting Thread " + Thread.currentThread().getId());
        this.runnable.run(); // Run the "Thread"
        GCHeapImpl.getMemory().endOfThread(); // Clean up the thread
    }

}
