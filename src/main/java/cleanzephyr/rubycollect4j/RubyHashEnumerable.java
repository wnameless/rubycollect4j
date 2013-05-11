package cleanzephyr.rubycollect4j;

import cleanzephyr.rubycollect4j.blocks.BooleanBlock;
import cleanzephyr.rubycollect4j.blocks.InjectBlock;
import cleanzephyr.rubycollect4j.blocks.InjectWithInitBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemToListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithObjectBlock;
import java.util.Comparator;
import java.util.Map.Entry;

public interface RubyHashEnumerable<K, V> {

  public boolean allʔ();

  public boolean allʔ(BooleanBlock<Entry<K, V>> block);

  public boolean anyʔ();

  public boolean anyʔ(BooleanBlock<Entry<K, V>> block);

  public <S> RubyEnumerator<Entry<S, RubyArray<Entry<K, V>>>> chunk(ItemTransformBlock<Entry<K, V>, S> block);

  public <S> RubyArray<S> collect(ItemTransformBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> collect();

  public <S> RubyArray<S> collectConcat(ItemToListBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> collectConcat();

  public int count();

  public int count(BooleanBlock<Entry<K, V>> block);

  public void cycle(ItemBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> cycle();

  public void cycle(int n, ItemBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> cycle(int n);

  public Entry<K, V> detect(BooleanBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> detect();

  public RubyArray<Entry<K, V>> drop(int n);

  public RubyArray<Entry<K, V>> dropWhile(BooleanBlock block);

  public RubyEnumerator<Entry<K, V>> dropWhile();

  public void eachCons(int n, ListBlock<Entry<K, V>> block);

  public RubyEnumerator<RubyArray<Entry<K, V>>> eachCons(int n);

  public RubyArray<Entry<K, V>> eachEntry(ItemBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> eachEntry();

  public void eachSlice(int n, ListBlock<Entry<K, V>> block);

  public RubyEnumerator<RubyArray<Entry<K, V>>> eachSlice(int n);

  public RubyArray<Entry<K, V>> eachWithIndex(ItemWithIndexBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<Entry<K, V>, Integer>> eachWithIndex();

  public <S> S eachWithObject(S o, ItemWithObjectBlock<Entry<K, V>, S> block);

  public <S> RubyEnumerator<Entry<Entry<K, V>, S>> eachWithObject(S o);

  public RubyArray<Entry<K, V>> entries();

  public Entry<K, V> find(BooleanBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> find();

  public RubyArray<Entry<K, V>> findAll(BooleanBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> findAll();

  public Entry<K, V> first();

  public RubyArray<Entry<K, V>> first(int n);

  public Integer findIndex(Entry<K, V> target);

  public Integer findIndex(BooleanBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> findIndex();

  public <S> RubyArray<S> flatMap(ItemToListBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> flatMap();

  public RubyArray<Entry<K, V>> grep(String regex);

  public <S> RubyArray<S> grep(String regex, ItemTransformBlock<Entry<K, V>, S> block);

  public <S> RubyHashBase<S, RubyArray<Entry<K, V>>> groupBy(ItemTransformBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> groupBy();

  public boolean includeʔ(Entry<K, V> target);

  public boolean memberʔ(Entry<K, V> target);

  public Entry<K, V> inject(String methodName);

  public Entry<K, V> inject(Entry<K, V> init, String methodName);

  public Entry<K, V> inject(InjectBlock<Entry<K, V>> block);

  public <S> S inject(S init, InjectWithInitBlock<Entry<K, V>, S> block);

  public <S> RubyArray<S> map(ItemTransformBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> map();

  public Entry<K, V> max();

  public Entry<K, V> max(Comparator<? super Entry<K, V>> comp);

  public <S> Entry<K, V> maxBy(ItemTransformBlock<Entry<K, V>, S> block);

  public <S> Entry<K, V> maxBy(Comparator<? super S> comp, ItemTransformBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> maxBy();

  public Entry<K, V> min();

  public Entry<K, V> min(Comparator<? super Entry<K, V>> comp);

  public <S> Entry<K, V> minBy(ItemTransformBlock<Entry<K, V>, S> block);

  public <S> Entry<K, V> minBy(Comparator<? super S> comp, ItemTransformBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> minBy();

  public RubyArray<Entry<K, V>> minmax();

  public RubyArray<Entry<K, V>> minmax(Comparator<? super Entry<K, V>> comp);

  public RubyEnumerator<Entry<K, V>> minmaxBy();

  public <S> RubyArray<Entry<K, V>> minmaxBy(ItemTransformBlock<Entry<K, V>, S> block);

  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<Entry<K, V>, S> block);

  public boolean noneʔ();

  public boolean noneʔ(BooleanBlock<Entry<K, V>> block);

  public boolean oneʔ();

  public boolean oneʔ(BooleanBlock<Entry<K, V>> block);

  public RubyArray<RubyArray<Entry<K, V>>> partition(BooleanBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> partition();

  public Entry<K, V> reduce(String methodName);

  public Entry<K, V> reduce(Entry<K, V> init, String methodName);

  public Entry<K, V> reduce(InjectBlock<Entry<K, V>> block);

  public <S> S reduce(S init, InjectWithInitBlock<Entry<K, V>, S> block);

  public RubyArray<Entry<K, V>> reject(BooleanBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> reject();

  public void reverseEach(ItemBlock block);

  public RubyEnumerator<Entry<K, V>> reverseEach();

  public RubyArray<Entry<K, V>> select(BooleanBlock block);

  public RubyEnumerator<Entry<K, V>> select();

  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(String regex);

  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(BooleanBlock block);

  public RubyArray<Entry<K, V>> sort();

  public RubyArray<Entry<K, V>> sort(Comparator<? super Entry<K, V>> comp);

  public <S> RubyArray<Entry<K, V>> sortBy(ItemTransformBlock<Entry<K, V>, S> block);

  public <S> RubyArray<Entry<K, V>> sortBy(Comparator<? super S> comp, ItemTransformBlock<Entry<K, V>, S> block);

  public RubyEnumerator<Entry<K, V>> sortBy();

  public RubyArray<Entry<K, V>> take(int n);

  public RubyArray<Entry<K, V>> takeWhile(BooleanBlock block);

  public RubyEnumerator<Entry<K, V>> takeWhile();

  public RubyArray<Entry<K, V>> toA();

  public RubyArray<RubyArray<Entry<K, V>>> zip(RubyArray<Entry<K, V>>... others);

  public void zip(RubyArray<RubyArray<Entry<K, V>>> others, ItemBlock<RubyArray<Entry<K, V>>> block);
}
