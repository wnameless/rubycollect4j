package cleanzephyr.rubycollect4j.blocks;

/**
 * An interface for lambda expression to yield the key and the value of a map
 * entry.
 *
 * @param <K> key of a map entry
 * @param <V> value of a map entry
 */
public interface EntryBlock<K, V> {

  public void yield(K key, V value);
}