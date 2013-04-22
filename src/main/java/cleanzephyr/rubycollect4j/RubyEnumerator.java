package cleanzephyr.rubycollect4j;

import cleanzephyr.rubycollect4j.blocks.BooleanBlock;
import cleanzephyr.rubycollect4j.blocks.InjectBlock;
import cleanzephyr.rubycollect4j.blocks.InjectWithInitBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemFromListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemToListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithObjectBlock;
import com.google.common.collect.ArrayListMultimap;
import static com.google.common.collect.Lists.newArrayList;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class RubyEnumerator<E> implements RubyArrayEnumerable<E>, Iterable<E> {

  private final Iterable<E> iter;

  public RubyEnumerator(Iterable<E> iter) {
    this.iter = iter;
  }

  public RubyArray<E> each(ItemBlock<E> block) {
    RubyArray<E> rubyArray = new RubyArrayImpl<>();
    for (E item : iter) {
      block.yield(item);
      rubyArray.add(item);
    }
    return rubyArray;
  }

  public RubyEnumerator<E> each() {
    return this;
  }

  public Iterator<E> rewind() {
    return iter.iterator();
  }

  @Override
  public boolean allʔ() {
    return RubyEnumerable.allʔ(iter);
  }

  @Override
  public boolean allʔ(BooleanBlock<E> block) {
    return RubyEnumerable.allʔ(iter, block);
  }

  @Override
  public boolean anyʔ() {
    return RubyEnumerable.allʔ(iter);
  }

  @Override
  public boolean anyʔ(BooleanBlock<E> block) {
    return RubyEnumerable.allʔ(iter, block);
  }

  @Override
  public <K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block) {
    return RubyEnumerable.chunk(iter, block);
  }

  @Override
  public <S> RubyArray<S> collect(ItemTransformBlock<E, S> block) {
    return RubyEnumerable.collect(iter, block);
  }

  @Override
  public RubyEnumerator<E> collect() {
    return RubyEnumerable.collect(iter);
  }

  @Override
  public <S> RubyArray<S> collectConcat(ItemToListBlock<E, S> block) {
    return RubyEnumerable.collectConcat(iter, block);
  }

  @Override
  public RubyEnumerator<E> collectConcat() {
    return RubyEnumerable.collectConcat(iter);
  }

  @Override
  public int count() {
    return RubyEnumerable.count(iter);
  }

  @Override
  public int count(BooleanBlock block) {
    return RubyEnumerable.count(iter, block);
  }

  @Override
  public void cycle(ItemBlock block) {
    RubyEnumerable.cycle(iter, block);
  }

  @Override
  public RubyEnumerator<E> cycle() {
    return RubyEnumerable.cycle(iter);
  }

  @Override
  public void cycle(int n, ItemBlock block) {
    RubyEnumerable.cycle(iter, n, block);
  }

  @Override
  public RubyEnumerator<E> cycle(int n) {
    return RubyEnumerable.cycle(iter, n);
  }

  @Override
  public E detect(BooleanBlock block) {
    return RubyEnumerable.detect(iter, block);
  }

  @Override
  public RubyEnumerator<E> detect() {
    return RubyEnumerable.detect(iter);
  }

  @Override
  public RubyArray<E> drop(int n) {
    return RubyEnumerable.drop(iter, n);
  }

  @Override
  public RubyArray<E> dropWhile(BooleanBlock block) {
    return RubyEnumerable.dropWhile(iter, block);
  }

  @Override
  public RubyEnumerator<E> dropWhile() {
    return RubyEnumerable.dropWhile(iter);
  }

  @Override
  public void eachCons(int n, ItemFromListBlock<E> block) {
    RubyEnumerable.eachCons(iter, n, block);
  }

  @Override
  public RubyEnumerator<RubyArray<E>> eachCons(int n) {
    return RubyEnumerable.eachCons(iter, n);
  }

  @Override
  public RubyArray<E> eachEntry(ItemBlock<E> block) {
    return RubyEnumerable.eachEntry(iter, block);
  }

  @Override
  public RubyEnumerator<E> eachEntry() {
    return RubyEnumerable.eachEntry(iter);
  }

  @Override
  public void eachSlice(int n, ItemFromListBlock<E> block) {
    RubyEnumerable.eachSlice(iter, n, block);
  }

  @Override
  public RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    return RubyEnumerable.eachSlice(iter, n);
  }

  @Override
  public RubyArray<E> eachWithIndex(ItemWithIndexBlock<E> block) {
    return RubyEnumerable.eachWithIndex(iter, block);
  }

  @Override
  public RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return RubyEnumerable.eachWithIndex(iter);
  }

  /**
   *
   * @param <S>
   * @param o
   * @param block
   * @return
   */
  @Override
  public < S> S eachWithObject(S o, ItemWithObjectBlock<E, S> block) {
    return RubyEnumerable.eachWithObject(iter, o, block);
  }

  @Override
  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o) {
    return RubyEnumerable.eachWithObject(iter, o);
  }

  @Override
  public RubyArray<E> entries() {
    return RubyEnumerable.entries(iter);
  }

  @Override
  public E find(BooleanBlock block) {
    return RubyEnumerable.find(iter, block);
  }

  @Override
  public RubyEnumerator<E> find() {
    return RubyEnumerable.find(iter);
  }

  @Override
  public RubyArray<E> findAll(BooleanBlock block) {
    return RubyEnumerable.findAll(iter, block);
  }

  @Override
  public RubyEnumerator<E> findAll() {
    return RubyEnumerable.findAll(iter);
  }

  @Override
  public E first() {
    return RubyEnumerable.first(iter);
  }

  @Override
  public RubyArray<E> first(int n) {
    return RubyEnumerable.first(iter, n);
  }

  @Override
  public Integer findIndex(E target) {
    return RubyEnumerable.findIndex(iter, target);
  }

  @Override
  public Integer findIndex(BooleanBlock<E> block) {
    return RubyEnumerable.findIndex(iter, block);
  }

  @Override
  public RubyEnumerator<E> findIndex() {
    return RubyEnumerable.findIndex(iter);
  }

  @Override
  public <S> RubyArray<S> flatMap(ItemToListBlock<E, S> block) {
    return RubyEnumerable.flatMap(iter, block);
  }

  @Override
  public RubyEnumerator<E> flatMap() {
    return RubyEnumerable.flatMap(iter);
  }

  @Override
  public RubyArray<E> grep(String regex) {
    return RubyEnumerable.grep(iter, regex);
  }

  @Override
  public <S> RubyArray<S> grep(String regex, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.grep(iter, regex, block);
  }

  @Override
  public < K> RubyHash<K, RubyArray<E>> groupBy(ItemTransformBlock<E, K> block) {
    return RubyEnumerable.groupBy(iter, block);
  }

  @Override
  public RubyEnumerator<E> groupBy() {
    return RubyEnumerable.groupBy(iter);
  }

  @Override
  public boolean includeʔ(E target) {
    return RubyEnumerable.includeʔ(iter, target);
  }

  @Override
  public boolean memberʔ(E target) {
    return RubyEnumerable.memberʔ(iter, target);
  }

  @Override
  public E inject(String methodName) {
    return RubyEnumerable.inject(iter, methodName);
  }

  @Override
  public E inject(E init, String methodName) {
    return RubyEnumerable.inject(iter, methodName);
  }

  @Override
  public E inject(InjectBlock<E> block) {
    return RubyEnumerable.inject(iter, block);
  }

  @Override
  public <S> S inject(S init, InjectWithInitBlock<E, S> block) {
    return RubyEnumerable.inject(iter, init, block);
  }

  @Override
  public < S> RubyArray<S> map(ItemTransformBlock<E, S> block) {
    return RubyEnumerable.map(iter, block);
  }

  @Override
  public RubyEnumerator<E> map() {
    return RubyEnumerable.map(iter);
  }

  @Override
  public E max() {
    return sort().last();
  }

  @Override
  public E max(Comparator<? super E> comp) {
    return RubyEnumerable.max(iter, comp);
  }

  @Override
  public <S> E maxBy(ItemTransformBlock<E, S> block) {
    return sortBy(block).last();
  }

  @Override
  public <S> E maxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.maxBy(iter, comp, block);
  }

  @Override
  public RubyEnumerator<E> maxBy() {
    return RubyEnumerable.maxBy(iter);
  }

  @Override
  public E min() {
    return sort().first();
  }

  @Override
  public E min(Comparator<? super E> comp) {
    return RubyEnumerable.min(iter, comp);
  }

  @Override
  public <S> E minBy(ItemTransformBlock<E, S> block) {
    return sortBy(block).first();
  }

  @Override
  public RubyEnumerator<E> minBy() {
    return RubyEnumerable.minBy(iter);
  }

  @Override
  public <S> E minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.minBy(iter, comp, block);
  }

  @Override
  public RubyArray<E> minmax() {
    RubyArray<E> sorted = sort();
    return new RubyArrayImpl(sorted.first(), sorted.last());
  }

  @Override
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    return RubyEnumerable.minmax(iter, comp);
  }

  @Override
  public <S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block) {
    RubyArray<E> sorted = sortBy(block);
    return new RubyArrayImpl(sorted.first(), sorted.last());
  }

  @Override
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.minmaxBy(iter, comp, block);
  }

  @Override
  public RubyEnumerator<E> minmaxBy() {
    return RubyEnumerable.minmaxBy(iter);
  }

  @Override
  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block) {
    return RubyEnumerable.partition(iter, block);
  }

  @Override
  public RubyEnumerator<E> partition() {
    return RubyEnumerable.partition(iter);
  }

  @Override
  public boolean noneʔ() {
    return RubyEnumerable.noneʔ(iter);
  }

  @Override
  public boolean noneʔ(BooleanBlock<E> block) {
    return RubyEnumerable.noneʔ(iter, block);
  }

  @Override
  public boolean oneʔ() {
    return RubyEnumerable.oneʔ(iter);
  }

  @Override
  public boolean oneʔ(BooleanBlock<E> block) {
    return RubyEnumerable.oneʔ(iter, block);
  }

  @Override
  public E reduce(String methodName) {
    return inject(methodName);
  }

  @Override
  public E reduce(E init, String methodName) {
    return inject(init, methodName);
  }

  @Override
  public E reduce(InjectBlock<E> block) {
    return inject(block);
  }

  @Override
  public <S> S reduce(S init, InjectWithInitBlock<E, S> block) {
    return inject(init, block);
  }

  @Override
  public RubyArray<E> reject(BooleanBlock block) {
    return new RubyArrayImpl(RubyEnumerable.reject(iter, block));
  }

  @Override
  public RubyEnumerator<E> reject() {
    return RubyEnumerable.reject(iter);
  }

  @Override
  public void reverseEach(ItemBlock block) {
    RubyEnumerable.reverseEach(iter, block);
  }

  @Override
  public RubyEnumerator<E> reverseEach() {
    return RubyEnumerable.reverseEach(iter);
  }

  @Override
  public RubyArray<E> select(BooleanBlock block) {
    return findAll(block);
  }

  @Override
  public RubyEnumerator<E> select() {
    return findAll();
  }

  @Override
  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return RubyEnumerable.sliceBefore(iter, regex);
  }

  @Override
  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock block) {
    return RubyEnumerable.sliceBefore(iter, block);
  }

  @Override
  public RubyArray<E> sort() {
    Object[] array = newArrayList(iter).toArray();
    Arrays.sort(array);
    return new RubyArrayImpl(array);
  }

  @Override
  public RubyArray<E> sort(Comparator<? super E> comp) {
    return new RubyArrayImpl(RubyEnumerable.sort(iter, comp));
  }

  @Override
  public <S> RubyArray<E> sortBy(ItemTransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    List<E> sortedList = newArrayList();
    for (E item : iter) {
      multimap.put(block.yield(item), item);
    }
    Object[] keys = newArrayList(multimap.keySet()).toArray();
    Arrays.sort(keys);
    for (Object key : keys) {
      Collection<E> coll = multimap.get((S) key);
      Iterator<E> it = coll.iterator();
      while (it.hasNext()) {
        sortedList.add(it.next());
      }
    }
    return new RubyArrayImpl(sortedList);
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return new RubyArrayImpl(RubyEnumerable.sortBy(iter, comp, block));
  }

  @Override
  public RubyEnumerator<E> sortBy() {
    return RubyEnumerable.sortBy(iter);
  }

  @Override
  public RubyArray<E> take(int n) {
    return new RubyArrayImpl(RubyEnumerable.take(iter, n));
  }

  @Override
  public RubyArray<E> takeWhile(BooleanBlock block) {
    return new RubyArrayImpl(RubyEnumerable.takeWhile(iter, block));
  }

  @Override
  public RubyEnumerator<E> takeWhile() {
    return RubyEnumerable.takeWhile(iter);
  }

  @Override
  public RubyArray<E> toA() {
    return RubyEnumerable.toA(iter);
  }

  @Override
  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others) {
    return RubyEnumerable.zip(iter, others);
  }

  @Override
  public void zip(RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block) {
    RubyEnumerable.zip(iter, others, block);
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }
}
