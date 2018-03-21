package mapReduce;

import java.util.*;

public class Shuffler {
	public List<KeyValue<String, List>> execute(List<KeyValue> input) {

        List<KeyValue<String, List>> output;
        output = shuffle(input);
        return output;
	}
	private List<KeyValue<String, List>> shuffle(List<KeyValue> input) {

        List<KeyValue<String, List>> output = new ArrayList<>();
        HashMap<String, List<Object>> hMap = new HashMap<>();
        for (KeyValue kv : input) {
            String key = (String) kv.getKey();
            Object value = kv.getValue();

 //---------------------------------- Removing duplicate keys ---------------------------------------
            if (!hMap.containsKey(key)) {
                List<Object> values = new ArrayList<>();
                values.add(value);
                hMap.put(key, values);
            } else {
                List<Object> values = hMap.get(key);
                values.add(value);
                hMap.put(key, values);
            }
        }

 //----------------------- ----- Adding the HashMap content to the output list ---------------------------------
        for (Map.Entry entry : hMap.entrySet()) {
            String key = (String) entry.getKey();
            List values = (List) entry.getValue();

            output.add(new KeyValue<>(key, values));
        }

//----------------------------------- Sorting the list ------------------------------------
        output.sort(Comparator.comparing(KeyValue::getKey));

        return output;
    }
//---------------------------------------------------------------------------------------------------	
}
