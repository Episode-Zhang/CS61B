package gitlet.utils;

import java.util.HashMap;

/**
 * MemCache class mainly maintains a kv-table, it will be used for messaging between publishers and subscribers.
 *
 * @author jeffrey zhang
 * @version 1.0
 */
public class MemCache {

    private HashMap<String, Object> table;

    private static volatile MemCache instance;

    private MemCache() {
        table = new HashMap<>();
    }

    public static MemCache getInstance() {
        if (instance == null) {
            synchronized (MemCache.class) {
                if (instance == null) {
                    instance = new MemCache();
                }
            }
        }
        return instance;
    }

    /** put a k-v pair to the mem table. */
    public void set(String key, Object value) {
        table.put(key, value);
    }

    /** retrieve value by key in mem table. */
    public Object get(String key) {
        Object value = null;
        if (table.containsKey(key)) {
            value = table.get(key);
        }
        return value;
    }
}
