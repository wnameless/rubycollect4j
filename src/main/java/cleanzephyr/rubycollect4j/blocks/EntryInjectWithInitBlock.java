package cleanzephyr.rubycollect4j.blocks;

import java.util.Map;

public interface EntryInjectWithInitBlock<K, V, S> {

  public S yield(S memo, Map.Entry<K, V> item);
}