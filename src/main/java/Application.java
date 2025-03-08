
public class Application {
    public static void main(String[] args) {
        // some tests
        MyHashMap<String, String> myHashMap = new MyHashMap<>();

        for (int i = 200; i < 215; i++) {
            myHashMap.put(String.valueOf(i), "value " + i);
        }

        System.out.println("Key 211 value = " + myHashMap.get("211"));
        System.out.println("Contains key 211? = " + myHashMap.containsKey("211"));

        myHashMap.remove("211");

        System.out.println("Key 211 value = " + myHashMap.get("211"));
        System.out.println("Contains key 211? = " + myHashMap.containsKey("211"));

        myHashMap.put("100", "string 100");
        myHashMap.put("100", "string 105488");

        myHashMap.remove("159546");

        System.out.println("Key 100 value = " + myHashMap.get("100"));
        System.out.println("Contains key 100? = " + myHashMap.containsKey("100"));

        System.out.println("Size of myHashMap = " + myHashMap.size());
    }
}
