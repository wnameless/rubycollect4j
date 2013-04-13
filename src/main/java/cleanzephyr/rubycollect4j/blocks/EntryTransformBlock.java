package cleanzephyr.rubycollect4j.blocks;

public interface EntryTransformBlock<K, V, S> {

  public S yield(K key, V value);
}
