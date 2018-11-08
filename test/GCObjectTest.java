import gc.GCObject;

public class GCObjectTest {

    public static void main(String[] args) {
        final GCObject<String> object = new GCObject<>("HELLO", null);
        System.out.println(object.get());
    }

}
