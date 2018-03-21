package mapReduce;

//--------------- Key Value pair for Map Reduce framework ------------------------
public class KeyValue<K, V> {
//--------------------- Variables ---------------------------
    private final K key;
    private final V value;
// ----------------------- Constructor --------------------------------------
    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }
//------------------ Methods Section -------------------------------------------
    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }
//--------------------------------------------------------------------------------
}
