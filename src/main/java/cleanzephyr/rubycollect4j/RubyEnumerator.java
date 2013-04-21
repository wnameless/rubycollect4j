package cleanzephyr.rubycollect4j;

import cleanzephyr.rubycollect4j.blocks.BooleanBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemToListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;
import java.util.Iterator;
import java.util.Map.Entry;

public class RubyEnumerator<E> implements Iterable<E> {

  private final Iterable<E> iter;

  public RubyEnumerator(Iterable<E> iter) {
    this.iter = iter;
  }

  public Iterator<E> rewind() {
    return iter.iterator();
  }

  public boolean allʔ() {
    return RubyEnumerable.allʔ(iter);
  }

  public boolean allʔ(BooleanBlock<E> block) {
    return RubyEnumerable.allʔ(iter, block);
  }

  public boolean anyʔ() {
    return RubyEnumerable.allʔ(iter);
  }

  public boolean anyʔ(BooleanBlock<E> block) {
    return RubyEnumerable.allʔ(iter, block);
  }

  public <K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block) {
    return RubyEnumerable.chunk(iter, block);
  }

  public <S> RubyArray<S> collect(ItemTransformBlock<E, S> block) {
    return RubyEnumerable.collect(iter, block);
  }

  public RubyEnumerator<E> collect() {
    return RubyEnumerable.collect(iter);
  }

  public <S> RubyArray<S> collectConcat(ItemToListBlock<E, S> block) {
    return RubyEnumerable.collectConcat(iter, block);
  }

  public RubyEnumerator<E> collectConcat() {
    return RubyEnumerable.collectConcat(iter);
  }

  public RubyArray<E> each(ItemBlock<E> block) {
    RubyArray<E> rubyArray = new RubyArrayList<>();
    for (E item : iter) {
      block.yield(item);
      rubyArray.add(item);
    }
    return rubyArray;
  }

  public RubyEnumerator<E> each() {
    return this;
  }

  public RubyArray<E> toA() {
    return RubyEnumerable.toA(iter);
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }
}
