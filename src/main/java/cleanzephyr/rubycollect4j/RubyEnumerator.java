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
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class RubyEnumerator<E> implements Iterable<E> {

  private final Iterable<E> iter;

  public RubyEnumerator(Iterable<E> iter) {
    this.iter = iter;
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

  public int count() {
    return RubyEnumerable.count(iter);
  }

  public int count(BooleanBlock block) {
    return RubyEnumerable.count(iter, block);
  }

  public void cycle(ItemBlock block) {
    RubyEnumerable.cycle(iter, block);
  }

  public RubyEnumerator<E> cycle() {
    return RubyEnumerable.cycle(iter);
  }

  public void cycle(int n, ItemBlock block) {
    RubyEnumerable.cycle(iter, n, block);
  }

  public RubyEnumerator<E> cycle(int n) {
    return RubyEnumerable.cycle(iter, n);
  }

  public E detect(BooleanBlock block) {
    return RubyEnumerable.detect(iter, block);
  }

  public RubyEnumerator<E> detect() {
    return RubyEnumerable.detect(iter);
  }

  public RubyArray<E> drop(int n) {
    return RubyEnumerable.drop(iter, n);
  }

  public RubyArray<E> dropWhile(BooleanBlock block) {
    return RubyEnumerable.dropWhile(iter, block);
  }

  public RubyEnumerator<E> dropWhile() {
    return RubyEnumerable.dropWhile(iter);
  }

  public void eachCons(int n, ItemFromListBlock<E> block) {
    RubyEnumerable.eachCons(iter, n, block);
  }

  public RubyEnumerator<RubyArray<E>> eachCons(int n) {
    return RubyEnumerable.eachCons(iter, n);
  }

  public RubyArray<E> eachEntry(ItemBlock<E> block) {
    return RubyEnumerable.eachEntry(iter, block);
  }

  public RubyEnumerator<E> eachEntry() {
    return RubyEnumerable.eachEntry(iter);
  }

  public void eachSlice(int n, ItemFromListBlock<E> block) {
    RubyEnumerable.eachSlice(iter, n, block);
  }

  public RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    return RubyEnumerable.eachSlice(iter, n);
  }

  public RubyArray<E> eachWithIndex(ItemWithIndexBlock<E> block) {
    return RubyEnumerable.eachWithIndex(iter, block);
  }

  public RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return RubyEnumerable.eachWithIndex(iter);
  }

  public < S> S eachWithObject(S o, ItemWithObjectBlock<E, S> block) {
    return RubyEnumerable.eachWithObject(iter, o, block);
  }

  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o) {
    return RubyEnumerable.eachWithObject(iter, o);
  }

  public RubyArray<E> entries() {
    return RubyEnumerable.entries(iter);
  }

  public E find(BooleanBlock block) {
    return RubyEnumerable.find(iter, block);
  }

  public RubyEnumerator<E> find(Iterable<E> iter) {
    return RubyEnumerable.find(iter);
  }

  public RubyArray<E> findAll(BooleanBlock block) {
    return RubyEnumerable.findAll(iter, block);
  }

  public RubyEnumerator<E> findAll() {
    return RubyEnumerable.findAll(iter);
  }

  public E first() {
    return RubyEnumerable.first(iter);
  }

  public RubyArray<E> first(int n) {
    return RubyEnumerable.first(iter, n);
  }

  public Integer findIndex(E target) {
    return RubyEnumerable.findIndex(iter, target);
  }

  public Integer findIndex(BooleanBlock<E> block) {
    return RubyEnumerable.findIndex(iter, block);
  }

  public RubyEnumerator<E> findIndex() {
    return RubyEnumerable.findIndex(iter);
  }

  public <S> RubyArray<S> flatMap(ItemToListBlock<E, S> block) {
    return RubyEnumerable.flatMap(iter, block);
  }

  public RubyEnumerator<E> flatMap() {
    return RubyEnumerable.flatMap(iter);
  }

  public RubyArray<E> grep(String regex) {
    return RubyEnumerable.grep(iter, regex);
  }

  public <S> RubyArray<S> grep(String regex, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.grep(iter, regex, block);
  }

  public < K> RubyHash<K, RubyArray<E>> groupBy(ItemTransformBlock<E, K> block) {
    return RubyEnumerable.groupBy(iter, block);
  }

  public RubyEnumerator<E> groupBy() {
    return RubyEnumerable.groupBy(iter);
  }

  public boolean includeʔ(E target) {
    return RubyEnumerable.includeʔ(iter, target);
  }

  public boolean memberʔ(E target) {
    return RubyEnumerable.memberʔ(iter, target);
  }

  public E inject(String methodName) {
    return RubyEnumerable.inject(iter, methodName);
  }

  public E inject(E init, String methodName) {
    return RubyEnumerable.inject(iter, methodName);
  }

  public E inject(InjectBlock<E> block) {
    return RubyEnumerable.inject(iter, block);
  }

  public <S> S inject(S init, InjectWithInitBlock<E, S> block) {
    return RubyEnumerable.inject(iter, init, block);
  }

  public < S> RubyArray<S> map(ItemTransformBlock<E, S> block) {
    return RubyEnumerable.map(iter, block);
  }

  public RubyEnumerator<E> map(Iterable<E> iter) {
    return RubyEnumerable.map(iter);
  }

  public <E extends Comparable<E>> E max(Iterable<E> iter) {
    List<E> list = newArrayList(iter);
    return Collections.max(list);
  }

  public E max() {
    return sort().last();
  }

  public E max(Comparator<? super E> comp) {
    return RubyEnumerable.max(iter, comp);
  }

  public <S> E maxBy(ItemTransformBlock<E, S> block) {
    return sortBy(block).last();
  }

  public <S> E maxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.maxBy(iter, comp, block);
  }

  public RubyEnumerator<E> maxBy() {
    return RubyEnumerable.maxBy(iter);
  }

  public E min() {
    return sort().first();
  }

  public E min(Comparator<? super E> comp) {
    return RubyEnumerable.min(iter, comp);
  }

  public <S> E minBy(ItemTransformBlock<E, S> block) {
    return sortBy(block).first();
  }

  public RubyEnumerator<E> minBy() {
    return RubyEnumerable.minBy(iter);
  }

  public <S> E minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.minBy(iter, comp, block);
  }

  public RubyArray<E> minmax() {
    RubyArray<E> sorted = sort();
    return new RubyArrayList(sorted.first(), sorted.last());
  }

  public RubyArray<E> minmax(Comparator<? super E> comp) {
    return RubyEnumerable.minmax(iter, comp);
  }

  public <S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block) {
    RubyArray<E> sorted = sortBy(block);
    return new RubyArrayList(sorted.first(), sorted.last());
  }

  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.minmaxBy(iter, comp, block);
  }

  public RubyEnumerator<E> minmaxBy() {
    return RubyEnumerable.minmaxBy(iter);
  }

  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block) {
    return RubyEnumerable.partition(iter, block);
  }

  public RubyEnumerator<E> partition() {
    return RubyEnumerable.partition(iter);
  }

  public boolean noneʔ() {
    return RubyEnumerable.noneʔ(iter);
  }

  public boolean noneʔ(BooleanBlock<E> block) {
    return RubyEnumerable.noneʔ(iter, block);
  }

  public boolean oneʔ() {
    return RubyEnumerable.oneʔ(iter);
  }

  public boolean oneʔ(BooleanBlock<E> block) {
    return RubyEnumerable.oneʔ(iter, block);
  }

  public E reduce(String methodName) {
    return inject(methodName);
  }

  public E reduce(E init, String methodName) {
    return inject(init, methodName);
  }

  public E reduce(InjectBlock<E> block) {
    return inject(block);
  }

  public <S> S reduce(S init, InjectWithInitBlock<E, S> block) {
    return inject(init, block);
  }

  public RubyArray<E> reject(BooleanBlock block) {
    return new RubyArrayList(RubyEnumerable.reject(iter, block));
  }

  public RubyEnumerator<E> reject() {
    return RubyEnumerable.reject(iter);
  }

  public void reverseEach(ItemBlock block) {
    RubyEnumerable.reverseEach(iter, block);
  }

  public RubyEnumerator<E> reverseEach() {
    return RubyEnumerable.reverseEach(iter);
  }

  public RubyArray<E> select(BooleanBlock block) {
    return findAll(block);
  }

  public RubyEnumerator<E> select() {
    return findAll();
  }

  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return RubyEnumerable.sliceBefore(iter, regex);
  }

  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock block) {
    return RubyEnumerable.sliceBefore(iter, block);
  }

  public RubyArray<E> sort() {
    Object[] array = newArrayList(iter).toArray();
    Arrays.sort(array);
    return new RubyArrayList(array);
  }

  public RubyArray<E> sort(Comparator<? super E> comp) {
    return new RubyArrayList(RubyEnumerable.sort(iter, comp));
  }

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
    return new RubyArrayList(sortedList);
  }

  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.sortBy(iter, comp, block));
  }

  public RubyEnumerator<E> sortBy() {
    return RubyEnumerable.sortBy(iter);
  }

  public RubyArray<E> take(int n) {
    return new RubyArrayList(RubyEnumerable.take(iter, n));
  }

  public RubyArray<E> takeWhile(BooleanBlock block) {
    return new RubyArrayList(RubyEnumerable.takeWhile(iter, block));
  }

  public RubyEnumerator<E> takeWhile() {
    return RubyEnumerable.takeWhile(iter);
  }

  public RubyArray<E> toA() {
    return RubyEnumerable.toA(iter);
  }

  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others) {
    return RubyEnumerable.zip(iter, others);
  }

  public void zip(RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block) {
    RubyEnumerable.zip(iter, others, block);
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }
}
