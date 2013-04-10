package cleanzephyr.util.rubycollections.blocks;

public interface EntryTransformBlock<K, V, S> {

  public S yield(K key, V value);
}
