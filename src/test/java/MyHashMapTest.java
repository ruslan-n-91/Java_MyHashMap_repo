import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyHashMapTest {
    private MyHashMap<String, String> myHashMap;

    @Before
    public void createMyHashMapWithDefaultCapacity() {
        myHashMap = new MyHashMap<>();
        for (int i = 0; i < 30; i++) {
            myHashMap.put(String.valueOf(i), "value " + i);
        }
    }

    @Test
    public void sizeShouldReturnSizeOfMyHashMap() {
        Assert.assertEquals(30, myHashMap.size());
    }

    @Test
    public void putShouldAddNewKeyValuePair() {
        myHashMap.put("50", "value 50");
        Assert.assertEquals(31, myHashMap.size());
        Assert.assertEquals("value 50", myHashMap.get("50"));
    }

    @Test
    public void putShouldChangeValueInPresentKeyValuePair() {
        myHashMap.put("24", "value 999");
        Assert.assertEquals(30, myHashMap.size());
        Assert.assertEquals("value 999", myHashMap.get("24"));
    }

    @Test
    public void getShouldReturnValueForKeyOrNullIfThereIsNoSuchKey() {
        Assert.assertEquals("value 17", myHashMap.get("17"));
        Assert.assertNull(myHashMap.get("77"));
    }

    @Test
    public void removeShouldRemoveKeyValuePair() {
        myHashMap.remove("11");
        Assert.assertEquals(29, myHashMap.size());
        Assert.assertNull(myHashMap.get("11"));
    }

    @Test
    public void removeShouldDoNothingIfThereIsNoSuchKey() {
        myHashMap.remove("81");
        Assert.assertEquals(30, myHashMap.size());
    }

    @Test
    public void containsKeyShouldReturnTrueIfThereSuchKeyValuePairAndFalseOtherwise() {
        Assert.assertTrue(myHashMap.containsKey("15"));
        Assert.assertFalse(myHashMap.containsKey("56"));
    }

    @Test
    public void containsValueShouldReturnTrueIfThereSuchKeyValuePairAndFalseOtherwise() {
        Assert.assertTrue(myHashMap.containsValue("value 7"));
        Assert.assertFalse(myHashMap.containsKey("value 777"));
    }

    @Test
    public void clearShouldRemoveAllKeyValuePairsFromMyHashMap() {
        myHashMap.clear();
        Assert.assertEquals(0, myHashMap.size());
    }

    @Test
    public void entrySetShouldReturnSetOfKeyValuePairs() {
        Set<Map.Entry<String, String>> entrySet = myHashMap.entrySet();

        Map.Entry<String, String> entry3 = new AbstractMap.SimpleEntry<>("3", "value 3");
        Map.Entry<String, String> entry10 = new AbstractMap.SimpleEntry<>("10", "value 10");
        Map.Entry<String, String> entry27 = new AbstractMap.SimpleEntry<>("27", "value 27");

        Assert.assertEquals(myHashMap.size(), entrySet.size());
        Assert.assertTrue(entrySet.contains(entry3));
        Assert.assertTrue(entrySet.contains(entry10));
        Assert.assertTrue(entrySet.contains(entry27));
    }

    @Test
    public void keySetShouldReturnSetOfKeys() {
        Set<String> keySet = myHashMap.keySet();

        Assert.assertEquals(myHashMap.size(), keySet.size());
        Assert.assertTrue(keySet.contains("3"));
        Assert.assertTrue(keySet.contains("10"));
        Assert.assertTrue(keySet.contains("27"));
    }

    @Test
    public void valuesShouldReturnListOfValues() {
        List<String> valuesList = myHashMap.values();

        Assert.assertEquals(myHashMap.size(), valuesList.size());
        Assert.assertTrue(valuesList.contains("value 3"));
        Assert.assertTrue(valuesList.contains("value 10"));
        Assert.assertTrue(valuesList.contains("value 27"));
    }

    @Test
    public void addOneMillionKeyValuePairsInMyHashMap() {
        MyHashMap<String, String> myHashMap2 = new MyHashMap<>(10_000);

        for (int i = 0; i < 1_000_000; i++) {
            myHashMap2.put(String.valueOf(i), "value " + i);
        }

        Assert.assertEquals(1_000_000, myHashMap2.size());
    }
}
