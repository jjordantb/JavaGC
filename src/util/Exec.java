package util;

public class Exec {

    public static int SLEEP = 500;

    public static void delay(int mils) {
        try {
            Thread.sleep(mils);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
