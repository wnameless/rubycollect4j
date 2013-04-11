package cleanzephyr.ruby.collections.blocks;

public interface EntryBooleanBlock<K, V> {

  public boolean yield(K key, V value);
}
