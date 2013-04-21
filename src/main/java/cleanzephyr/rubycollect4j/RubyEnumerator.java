package cleanzephyr.rubycollect4j;

import cleanzephyr.rubycollect4j.blocks.BooleanBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;
import java.util.Iterator;
import java.util.Map.Entry;

public class RubyEnumerator<E> implements Iterator<E> {

  private final Iterable<E> iterable;
  private Iterator<E> iterator;

  public RubyEnumerator(Iterable<E> iterable) {
    this.iterable = iterable;
    iterator = iterable.iterator();
  }

  public Iterator<E> rewind() {
    iterator = iterable.iterator();
    return this;
  }

  public boolean allʔ() {
    return RubyEnumerable.allʔ(iterable);
  }

  public boolean allʔ(BooleanBlock<E> block) {
    return RubyEnumerable.allʔ(iterable, block);
  }

  public boolean anyʔ() {
    return RubyEnumerable.allʔ(iterable);
  }

  public boolean anyʔ(BooleanBlock<E> block) {
    return RubyEnumerable.allʔ(iterable, block);
  }

  public <K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block) {
    return RubyEnumerable.chunk(iterable, block);
  }

  public <S> RubyArray<S> collect(ItemTransformBlock<E, S> block) {
    return RubyEnumerable.collect(iterable, block);
  }

  public static <E> RubyEnumerator<E> collect(Iterable<E> iter) {
    return new RubyEnumerator<>(iter);
  }

  public RubyArray<E> each(ItemBlock<E> block) {
    RubyArray<E> rubyArray = new RubyArrayList<>();
    for (E item : iterable) {
      block.yield(item);
      rubyArray.add(item);
    }
    return rubyArray;
  }

  public RubyEnumerator<E> each() {
    return this;
  }

  public RubyArray<E> toA() {
    return new RubyArrayList(RubyEnumerable.toA(iterable));
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public E next() {
    return iterator.next();
  }

  @Override
  public void remove() {
    iterator.remove();
  }
}
