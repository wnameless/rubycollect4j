package cleanzephyr.rubycollect4j.iter;

import cleanzephyr.rubycollect4j.RubyArray;
import java.util.Iterator;
import java.util.Map.Entry;

import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;

public final class ChunkIterable<E, K> implements Iterable<Entry<K, RubyArray<E>>> {
  private final Iterable<E> iterable;
  private final ItemTransformBlock<E, K> block;

  public ChunkIterable(Iterable<E> iterable, ItemTransformBlock<E, K> block) {
    this.iterable = iterable;
    this.block = block;
  }

  @Override
  public Iterator<Entry<K, RubyArray<E>>> iterator() {
    return new ChunkIterator<>(iterable.iterator(), block);
  }

}
