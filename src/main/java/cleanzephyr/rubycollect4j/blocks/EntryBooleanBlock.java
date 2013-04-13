package cleanzephyr.rubycollect4j.blocks;

public interface EntryBooleanBlock<K, V> {

  public boolean yield(K key, V value);
}
