/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package cleanzephyr.rubycollect4j;

import cleanzephyr.rubycollect4j.blocks.EntryBlock;
import cleanzephyr.rubycollect4j.blocks.EntryBooleanBlock;
import cleanzephyr.rubycollect4j.blocks.EntryInjectWithInitBlock;
import cleanzephyr.rubycollect4j.blocks.EntryMergeBlock;
import cleanzephyr.rubycollect4j.blocks.EntryToListBlock;
import cleanzephyr.rubycollect4j.blocks.EntryTransformBlock;
import cleanzephyr.rubycollect4j.blocks.InjectBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemFromListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithObjectBlock;
import java.util.Comparator;
import java.util.Map;

public interface RubyHash<K, V> extends Map<K, V> {

  public RubyHash<K, V> put(Entry<K, V> entry);

  public RubyHash<K, V> put(Entry<K, V>... entries);

  public Entry<K, V> assoc(K key);

  public RubyHash<K, V> compareByIdentity();

  public boolean comparedByIdentityʔ();

  public V setDefault(V defaultValue);

  public V getDefault();

  public V delete(K key);

  public RubyHash<K, V> deleteIf(EntryBooleanBlock<K, V> block);

  public RubyHash<K, V> each(EntryBlock<K, V> block);

  public RubyArray<Entry<K, V>> eachEntry(ItemBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<K, V>> eachEntry();

  public RubyHash<K, V> eachPair(EntryBlock<K, V> block);

  public RubyHash<K, V> eachKey(ItemBlock<K> block);

  public RubyHash<K, V> eachValue(ItemBlock<V> block);

  public boolean emptyʔ();

  public boolean eqlʔ(RubyHash other);

  public V fetch(K key);

  public V fetch(K key, V defaultValue);

  public RubyArray<Entry<K, V>> flatten();

  public boolean keyʔ(K key);

  public boolean valueʔ(V value);

  public int hash();

  public RubyHash<K, V> toH();

  public RubyHash<K, V> toHash();

  public RubyArray<Entry<K, V>> toA();

  public String toS();

  public String inspect();

  public RubyHash<V, K> invert();

  public RubyHash<K, V> keepIf(EntryBooleanBlock<K, V> block);

  public K key(V value);

  public RubyArray<K> keys();

  public int length();

  public RubyHash<K, V> merge(Map<K, V> otherHash);

  public RubyHash<K, V> mergeEx(Map<K, V> otherHash);

  public RubyHash<K, V> update(Map<K, V> otherHash);

  public RubyHash<K, V> merge(Map<K, V> otherHash, EntryMergeBlock<K, V> block);

  public RubyHash<K, V> mergeǃ(Map<K, V> otherHash, EntryMergeBlock<K, V> block);

  public RubyHash<K, V> update(Map<K, V> otherHash, EntryMergeBlock<K, V> block);

  public Entry<K, V> rassoc(V value);

  public RubyHash<K, V> rejectǃ(EntryBooleanBlock<K, V> block);

  public RubyHash<K, V> replace(Map<K, V> otherHash);

  public Entry<K, V> shift();

  public V store(K key, V value);

  @Override
  public RubyArray<V> values();

  public RubyArray<V> valuesAt(K... keys);

  public boolean allʔ();

  public boolean allʔ(EntryBooleanBlock<K, V> block);

  public boolean anyʔ();

  public boolean anyʔ(EntryBooleanBlock<K, V> block);

  public <S> RubyEnumerator<Entry<S, RubyArrayList<Entry<K, V>>>> chunk(EntryTransformBlock<K, V, S> block);

  public <S> RubyArray<S> collect(EntryTransformBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> collect();

  public <S> RubyArray<S> collectConcat(EntryToListBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> collectConcat();

  public int count();

  public int count(EntryBooleanBlock<K, V> block);

  public void cycle(EntryBlock<K, V> block);

  public void cycle(int cycles, EntryBlock<K, V> block);

  public Entry<K, V> detect(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> detect();

  public RubyArray<Entry<K, V>> drop(int n);

  public RubyArray<Entry<K, V>> dropWhile(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> dropWhile();

  public void eachCons(int n, ItemFromListBlock<Entry<K, V>> block);

  public RubyEnumerator<RubyArray<Entry<K, V>>> eachCons(int n);

  public void eachSlice(int n, ItemFromListBlock<Entry<K, V>> block);

  public RubyEnumerator<RubyArray<Entry<K, V>>> eachSlice(int n);

  public RubyArray<Entry<K, V>> eachWithIndex(ItemWithIndexBlock<Entry<K, V>> block);

  public RubyEnumerator<Entry<Entry<K, V>, Integer>> eachWithIndex();

  public <S> S eachWithObject(S o, ItemWithObjectBlock<Entry<K, V>, S> block);

  public <S> RubyEnumerator<Entry<Entry<K, V>, S>> eachWithObject(S o);

  public RubyArray<Entry<K, V>> entries();

  public Entry<K, V> find(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> find();

  public RubyArray<Entry<K, V>> findAll(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> findAll();

  public Entry<K, V> first();

  public RubyArray<Entry<K, V>> first(int n);

  public Integer findIndex(Entry<K, V> target);

  public Integer findIndex(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> findIndex();

  public <S> RubyArray<S> flatMap(EntryToListBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> flatMap();

  public <S> RubyHash<S, RubyArrayList<Entry<K, V>>> groupBy(EntryTransformBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> groupBy();

  public boolean includeʔ(K key);

  public boolean memberʔ(K key);

  public Entry<K, V> inject(InjectBlock<Entry<K, V>> block);

  public <S> S inject(S init, EntryInjectWithInitBlock<K, V, S> block);

  public <S> RubyArray<S> map(EntryTransformBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> map();

  public Entry<K, V> max(Comparator<? super K> comp);

  public <S> Entry<K, V> maxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> maxBy();

  public Entry<K, V> min(Comparator<? super K> comp);

  public <S> Entry<K, V> minBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> minBy();

  public RubyArray<Entry<K, V>> minmax(Comparator<? super K> comp);

  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> minmaxBy();

  public RubyArray<RubyArray<Entry<K, V>>> partition(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> partition();

  public boolean noneʔ();

  public boolean noneʔ(EntryBooleanBlock<K, V> block);

  public boolean oneʔ();

  public boolean oneʔ(EntryBooleanBlock<K, V> block);

  public Entry<K, V> reduce(InjectBlock<Entry<K, V>> block);

  public <S> S reduce(S init, EntryInjectWithInitBlock<K, V, S> block);

  public RubyHash<K, V> reject(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> reject();

  public void reverseEach(EntryBlock block);

  public RubyEnumerator<Entry<K, V>> reverseEach();

  public RubyArray<Entry<K, V>> select(EntryBooleanBlock<K, V> block);

  public RubyEnumerator<Entry<K, V>> select();

  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(String regex);

  public RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(EntryBooleanBlock<K, V> block);

  public RubyHash<K, V> sort(Comparator<? super K> comp);

  public RubyHash<K, V> sort();

  public <S> RubyHash<K, V> sortBy(EntryTransformBlock<K, V, S> block);

  public <S> RubyHash<K, V> sortBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public RubyEnumerator<Entry<K, V>> sortBy();

  public RubyArray<Entry<K, V>> take(int n);

  public RubyArray<Entry<K, V>> takeWhile(EntryBooleanBlock block);

  public RubyEnumerator<Entry<K, V>> takeWhile();
}
