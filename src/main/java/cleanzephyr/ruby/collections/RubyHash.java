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
package cleanzephyr.ruby.collections;

import cleanzephyr.ruby.collections.blocks.EntryBlock;
import cleanzephyr.ruby.collections.blocks.EntryBooleanBlock;
import cleanzephyr.ruby.collections.blocks.EntryInjectWithInitBlock;
import cleanzephyr.ruby.collections.blocks.EntryMergeBlock;
import cleanzephyr.ruby.collections.blocks.EntryToListBlock;
import cleanzephyr.ruby.collections.blocks.EntryTransformBlock;
import cleanzephyr.ruby.collections.blocks.InjectBlock;
import cleanzephyr.ruby.collections.blocks.ItemBlock;
import cleanzephyr.ruby.collections.blocks.ItemFromListBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithIndexBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithObjectBlock;
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

  public <S> RubyArray<Entry<S, RubyArrayList<Entry<K, V>>>> chunk(EntryTransformBlock<K, V, S> block);

  public <S> RubyArray<S> collect(EntryTransformBlock<K, V, S> block);

  public <S> RubyArray<S> collectConcat(EntryToListBlock<K, V, S> block);

  public int count();

  public int count(EntryBooleanBlock<K, V> block);

  public void cycle(EntryBlock<K, V> block);

  public void cycle(int cycles, EntryBlock<K, V> block);

  public Entry<K, V> detect(EntryBooleanBlock<K, V> block);

  public RubyArray<Entry<K, V>> drop(int n);

  public RubyArray<Entry<K, V>> dropWhile(EntryBooleanBlock<K, V> block);

  public void eachCons(int n, ItemFromListBlock<Entry<K, V>> block);

  public void eachSlice(int n, ItemFromListBlock<Entry<K, V>> block);

  public void eachWithIndex(ItemWithIndexBlock<Entry<K, V>> block);

  public void eachWithObject(Object o, ItemWithObjectBlock<Entry<K, V>> block);

  public RubyArray<Entry<K, V>> entries();

  public Entry<K, V> find(EntryBooleanBlock<K, V> block);

  public RubyArray<Entry<K, V>> findAll(EntryBooleanBlock<K, V> block);

  public Entry<K, V> first();

  public RubyArray<Entry<K, V>> first(int n);

  public Integer findIndex(Entry<K, V> target);

  public Integer findIndex(EntryBooleanBlock<K, V> block);

  public <S> RubyArray<S> flatMap(EntryToListBlock<K, V, S> block);

  public <S> RubyHash<S, RubyArrayList<Entry<K, V>>> groupBy(EntryTransformBlock<K, V, S> block);

  public boolean includeʔ(K key);

  public boolean memberʔ(K key);

  public Entry<K, V> inject(InjectBlock<Entry<K, V>> block);

  public <S> S inject(S init, EntryInjectWithInitBlock<K, V, S> block);

  public <S> RubyArray<S> map(EntryTransformBlock<K, V, S> block);

  public Entry<K, V> max(Comparator<? super K> comp);

  public <S> Entry<K, V> maxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public Entry<K, V> min(Comparator<? super K> comp);

  public <S> Entry<K, V> minBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public RubyArray<Entry<K, V>> minmax(Comparator<? super K> comp);

  public <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public RubyArray<RubyArray<Entry<K, V>>> partition(EntryBooleanBlock<K, V> block);

  public boolean noneʔ();

  public boolean noneʔ(EntryBooleanBlock<K, V> block);

  public boolean oneʔ();

  public boolean oneʔ(EntryBooleanBlock<K, V> block);

  public Entry<K, V> reduce(InjectBlock<Entry<K, V>> block);

  public <S> S reduce(S init, EntryInjectWithInitBlock<K, V, S> block);

  public RubyHash<K, V> reject(EntryBooleanBlock<K, V> block);

  public void reverseEach(EntryBlock block);

  public RubyArray<Entry<K, V>> select(EntryBooleanBlock<K, V> block);

  public RubyArray<RubyArrayList<Entry<K, V>>> sliceBefore(String regex);

  public RubyArray<RubyArrayList<Entry<K, V>>> sliceBefore(EntryBooleanBlock<K, V> block);

  public RubyHash<K, V> sort(Comparator<? super K> comp);

  public RubyHash<K, V> sort();

  public <S> RubyHash<K, V> sortBy(EntryTransformBlock<K, V, S> block);

  public <S> RubyHash<K, V> sortBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  public RubyArray<Entry<K, V>> take(int n);

  public RubyArray<Entry<K, V>> takeWhile(EntryBooleanBlock block);
}
