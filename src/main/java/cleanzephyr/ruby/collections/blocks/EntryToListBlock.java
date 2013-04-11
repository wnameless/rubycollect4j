package cleanzephyr.ruby.collections.blocks;

import java.util.List;

public interface EntryToListBlock<K, V, S> {

  public List<S> yield(K key, V value);
}
