package cleanzephyr.rubycollect4j.iter;

import cleanzephyr.rubycollect4j.RubyArray;
import static com.google.common.collect.Lists.newArrayList;

import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;

import static com.google.common.collect.Iterators.peekingIterator;
import com.google.common.collect.PeekingIterator;

public final class ChunkIterator<E, K> implements Iterator<Entry<K, RubyArray<E>>> {
  private final PeekingIterator<E> pIterator;
  private final ItemTransformBlock<E, K> block;

  public ChunkIterator(Iterator<E> iterator, ItemTransformBlock<E, K> block) {
    pIterator = peekingIterator(iterator);
    this.block = block;
  }

  private Entry<K, RubyArray<E>> nextElement() {
    K key = block.yield(pIterator.peek());
    RubyArray<E> bucket = new RubyArray<>();
    while (pIterator.hasNext() && key.equals(block.yield(pIterator.peek()))) {
      bucket.add(pIterator.next());
    }
    return new SimpleEntry<>(key, bucket);
  }

  @Override
  public boolean hasNext() {
    return pIterator.hasNext();
  }

  @Override
  public Entry<K, RubyArray<E>> next() {
    if (!pIterator.hasNext())
      throw new NoSuchElementException();

    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
