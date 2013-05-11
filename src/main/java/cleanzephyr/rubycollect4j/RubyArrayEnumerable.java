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
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

public interface RubyArrayEnumerable<E> {

  public boolean allʔ();

  public boolean allʔ(BooleanBlock<E> block);

  public boolean anyʔ();

  public boolean anyʔ(BooleanBlock<E> block);

  public <K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block);

  public <S> RubyArray<S> collect(ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> collect();

  public <S> RubyArray<S> collectConcat(ItemToListBlock<E, S> block);

  public RubyEnumerator<E> collectConcat();

  public int count();

  public int count(BooleanBlock<E> block);

  public void cycle(ItemBlock<E> block);

  public RubyEnumerator<E> cycle();

  public void cycle(int n, ItemBlock<E> block);

  public RubyEnumerator<E> cycle(int n);

  public E detect(BooleanBlock<E> block);

  public RubyEnumerator<E> detect();

  public RubyArray<E> drop(int n);

  public RubyArray<E> dropWhile(BooleanBlock block);

  public RubyEnumerator<E> dropWhile();

  public void eachCons(int n, ItemFromListBlock<E> block);

  public RubyEnumerator<RubyArray<E>> eachCons(int n);

  public RubyArray<E> eachEntry(ItemBlock<E> block);

  public RubyEnumerator<E> eachEntry();

  public void eachSlice(int n, ItemFromListBlock<E> block);

  public RubyEnumerator<RubyArray<E>> eachSlice(int n);

  public RubyArray<E> eachWithIndex(ItemWithIndexBlock<E> block);

  public RubyEnumerator<Entry<E, Integer>> eachWithIndex();

  public <S> S eachWithObject(S o, ItemWithObjectBlock<E, S> block);

  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o);

  public RubyArray<E> entries();

  public E find(BooleanBlock<E> block);

  public RubyEnumerator<E> find();

  public RubyArray<E> findAll(BooleanBlock<E> block);

  public RubyEnumerator<E> findAll();

  public E first();

  public RubyArray<E> first(int n);

  public Integer findIndex(E target);

  public Integer findIndex(BooleanBlock<E> block);

  public RubyEnumerator<E> findIndex();

  public <S> RubyArray<S> flatMap(ItemToListBlock<E, S> block);

  public RubyEnumerator<E> flatMap();

  public RubyArray<E> grep(String regex);

  public <S> RubyArray<S> grep(String regex, ItemTransformBlock<E, S> block);

  public <K> RubyHash<K, RubyArray<E>> groupBy(ItemTransformBlock<E, K> block);

  public RubyEnumerator<E> groupBy();

  public boolean includeʔ(E target);

  public boolean memberʔ(E target);

  public E inject(String methodName);

  public E inject(E init, String methodName);

  public E inject(InjectBlock<E> block);

  public <S> S inject(S init, InjectWithInitBlock<E, S> block);

  public <S> RubyArray<S> map(ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> map();

  public E max();

  public E max(Comparator<? super E> comp);

  public <S> E maxBy(ItemTransformBlock<E, S> block);

  public <S> E maxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> maxBy();

  public <E> E min();

  public E min(Comparator<? super E> comp);

  public <S> E minBy(ItemTransformBlock<E, S> block);

  public <S> E minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> minBy();

  public RubyArray<E> minmax();

  public RubyArray<E> minmax(Comparator<? super E> comp);

  public RubyEnumerator<E> minmaxBy();

  public < S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block);

  public < S> RubyArray<E> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public boolean noneʔ();

  public boolean noneʔ(BooleanBlock<E> block);

  public boolean oneʔ();

  public boolean oneʔ(BooleanBlock<E> block);

  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block);

  public RubyEnumerator<E> partition();

  public E reduce(String methodName);

  public E reduce(E init, String methodName);

  public E reduce(InjectBlock<E> block);

  public <S> S reduce(S init, InjectWithInitBlock<E, S> block);

  public RubyArray<E> reject(BooleanBlock block);

  public RubyEnumerator<E> reject();

  public void reverseEach(ItemBlock block);

  public RubyEnumerator<E> reverseEach();

  public RubyArray<E> select(BooleanBlock block);

  public RubyEnumerator<E> select();

  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex);

  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock block);

  public RubyArray<E> sort();

  //public RubyArray<E> sort(Comparator<? super E> comp);

  public <S> RubyArray<E> sortBy(ItemTransformBlock<E, S> block);

  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> sortBy();

  public RubyArray<E> take(int n);

  public RubyArray<E> takeWhile(BooleanBlock block);

  public RubyEnumerator<E> takeWhile();

  public RubyArray<E> toA();

  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others);

  public void zip(RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block);
}
