package cleanzephyr.util.rubycollections.blocks;

public interface EntryBooleanBlock<K, V> {

  public boolean yield(K key, V value);
}
