package util;

import java.util.concurrent.ThreadLocalRandom;

public class Exec {

    public static int SLEEP = 500;

    public static void delay(int mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static int nextInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

}
