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
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public abstract class RubyHashBase<K, V> implements RubyHashEnumerable<K, V>, Map<K, V> {

  abstract RubyHash<K, V> put(Entry<K, V> entry);

  abstract RubyHash<K, V> put(Entry<K, V>... entries);

  abstract Entry<K, V> assoc(K key);

  abstract RubyHash<K, V> compareByIdentity();

  abstract boolean comparedByIdentityʔ();

  abstract V setDefault(V defaultValue);

  abstract V getDefault();

  abstract V delete(K key);

  abstract RubyHash<K, V> deleteIf(EntryBooleanBlock<K, V> block);

  abstract RubyHash<K, V> each(EntryBlock<K, V> block);

  abstract RubyHash<K, V> eachPair(EntryBlock<K, V> block);

  abstract RubyHash<K, V> eachKey(ItemBlock<K> block);

  abstract RubyHash<K, V> eachValue(ItemBlock<V> block);

  abstract boolean emptyʔ();

  abstract boolean eqlʔ(RubyHash other);

  abstract V fetch(K key);

  abstract V fetch(K key, V defaultValue);

  abstract RubyArray<Entry<K, V>> flatten();

  abstract boolean keyʔ(K key);

  abstract boolean valueʔ(V value);

  abstract int hash();

  abstract RubyHash<K, V> toH();

  abstract RubyHash<K, V> toHash();

  abstract String toS();

  abstract String inspect();

  abstract RubyHash<V, K> invert();

  abstract RubyHash<K, V> keepIf(EntryBooleanBlock<K, V> block);

  abstract K key(V value);

  abstract RubyArray<K> keys();

  abstract int length();

  abstract RubyHash<K, V> merge(Map<K, V> otherHash);

  abstract RubyHash<K, V> mergeEx(Map<K, V> otherHash);

  abstract RubyHash<K, V> update(Map<K, V> otherHash);

  abstract RubyHash<K, V> merge(Map<K, V> otherHash, EntryMergeBlock<K, V> block);

  abstract RubyHash<K, V> mergeǃ(Map<K, V> otherHash, EntryMergeBlock<K, V> block);

  abstract RubyHash<K, V> update(Map<K, V> otherHash, EntryMergeBlock<K, V> block);

  abstract Entry<K, V> rassoc(V value);

  abstract RubyHash<K, V> rejectǃ(EntryBooleanBlock<K, V> block);

  abstract RubyHash<K, V> replace(Map<K, V> otherHash);

  abstract Entry<K, V> shift();

  abstract V store(K key, V value);

  @Override
  public abstract RubyArray<V> values();

  abstract RubyArray<V> valuesAt(K... keys);

  // RubyEnumerable for Hash
  abstract boolean allʔ(EntryBooleanBlock<K, V> block);

  abstract boolean anyʔ(EntryBooleanBlock<K, V> block);

  abstract <S> RubyEnumerator<Entry<S, RubyArray<Entry<K, V>>>> chunk(EntryTransformBlock<K, V, S> block);

  abstract <S> RubyArray<S> collect(EntryTransformBlock<K, V, S> block);

  abstract <S> RubyArray<S> collectConcat(EntryToListBlock<K, V, S> block);

  abstract int count(EntryBooleanBlock<K, V> block);

  abstract void cycle(EntryBlock<K, V> block);

  abstract void cycle(int cycles, EntryBlock<K, V> block);

  abstract Entry<K, V> detect(EntryBooleanBlock<K, V> block);

  abstract RubyArray<Entry<K, V>> dropWhile(EntryBooleanBlock<K, V> block);

  abstract Entry<K, V> find(EntryBooleanBlock<K, V> block);

  abstract RubyArray<Entry<K, V>> findAll(EntryBooleanBlock<K, V> block);

  abstract Integer findIndex(EntryBooleanBlock<K, V> block);

  abstract <S> RubyArray<S> flatMap(EntryToListBlock<K, V, S> block);

  abstract <S> RubyHash<S, RubyArray<Entry<K, V>>> groupBy(EntryTransformBlock<K, V, S> block);

  abstract boolean includeʔ(K key);

  abstract boolean memberʔ(K key);

  abstract <S> S inject(S init, EntryInjectWithInitBlock<K, V, S> block);

  abstract <S> RubyArray<S> map(EntryTransformBlock<K, V, S> block);

  abstract <S> Entry<K, V> maxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  abstract <S> Entry<K, V> minBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  abstract <S> RubyArray<Entry<K, V>> minmaxBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  abstract RubyArray<RubyArray<Entry<K, V>>> partition(EntryBooleanBlock<K, V> block);

  abstract boolean noneʔ(EntryBooleanBlock<K, V> block);

  abstract boolean oneʔ(EntryBooleanBlock<K, V> block);

  abstract <S> S reduce(S init, EntryInjectWithInitBlock<K, V, S> block);

  abstract RubyHash<K, V> reject(EntryBooleanBlock<K, V> block);

  abstract void reverseEach(EntryBlock block);

  abstract RubyArray<Entry<K, V>> select(EntryBooleanBlock<K, V> block);

  abstract RubyEnumerator<RubyArray<Entry<K, V>>> sliceBefore(EntryBooleanBlock<K, V> block);

  abstract <S> RubyHash<K, V> sortBy(EntryTransformBlock<K, V, S> block);

  abstract <S> RubyHash<K, V> sortBy(Comparator<? super S> comp, EntryTransformBlock<K, V, S> block);

  abstract RubyArray<Entry<K, V>> takeWhile(EntryBooleanBlock block);
}
