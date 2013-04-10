package cleanzephyr.util.rubycollections.blocks;

public interface EntryMergeBlock<K, V> {

  public V yield(K key, V oldval, V newval);
}
