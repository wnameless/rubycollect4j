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

import cleanzephyr.ruby.collections.blocks.Block;
import cleanzephyr.ruby.collections.blocks.BooleanBlock;
import cleanzephyr.ruby.collections.blocks.IndexBlock;
import cleanzephyr.ruby.collections.blocks.InjectBlock;
import cleanzephyr.ruby.collections.blocks.InjectWithInitBlock;
import cleanzephyr.ruby.collections.blocks.ItemBlock;
import cleanzephyr.ruby.collections.blocks.ItemFromListBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithIndexBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithObjectBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithReturnBlock;
import cleanzephyr.ruby.collections.blocks.ItemToListBlock;
import cleanzephyr.ruby.collections.blocks.ItemTransformBlock;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

/**
 * An interface which contains all methods a Ruby Array should have. A RubyArray
 * is also a Java List.
 *
 * @param <E> element
 */
public interface RubyArray<E> extends List<E> {

  /**
   * Create a RubyArray which contains a union set of two Lists.
   *
   * @param other other List
   * @return new RubyArray
   */
  public RubyArray<E> and(List<E> other);

  /**
   * Create a RubyArray which contains n copies of current RubyArray.
   *
   * @param n number of copies
   * @return new RubyArray
   */
  public RubyArray<E> multiply(int n);

  /**
   * Create a String which all elements are joined by separator.
   *
   * @param separator to join elements
   * @return String
   */
  public String multiply(String separator);

  /**
   * Create a RubyArray which contains elements in both Lists
   *
   * @param other other List
   * @return new RubyArray
   */
  public RubyArray<E> add(List<E> other);

  /**
   * Create a RubyArray which eliminates all elements from other List.
   *
   * @param other other List
   * @return new RubyArray
   */
  public RubyArray<E> minus(List<E> other);

  /**
   * Create a RubyArray starts from a List which contains the target as the
   * first element. Return null if target can't be found.
   *
   * @param <S> generic type of other kind of List
   * @param target the first element of List to find
   * @return new RubyList or null
   */
  public <S> RubyArray<S> assoc(S target);

  public E at(int index);

  public E bsearch(E target, Comparator<? super E> comp);

  public RubyArray<RubyArray<E>> combination(int n);

  public RubyArray<RubyArray<E>> combination(int n, ItemBlock<RubyArray<E>> block);

  public RubyArray<RubyArray<E>> repeatedCombination(int n);

  public RubyArray<RubyArray<E>> repeatedCombination(int n, ItemBlock<RubyArray<E>> block);

  public RubyArray<E> compact();

  public RubyArray<E> compactEx();

  public RubyArray<E> concat(RubyArray<E> other);

  public int count(E target);

  public E delete(E target);

  public E delete(E target, Block<E> block);

  public E deleteAt(int index);

  public RubyArray<E> deleteIf(BooleanBlock<E> block);

  public void each(ItemBlock<E> block);

  public void eachIndex(IndexBlock<E> block);

  public boolean eql(RubyArray<E> other);

  public E fetch(int index);

  public E fetch(int index, E defaultValue);

  public E fetch(int index, ItemBlock<E> block);

  public RubyArray<E> fill(E item);

  public RubyArray<E> fill(E item, int start);

  public RubyArray<E> fill(E item, int start, int length);

  public RubyArray<E> fill(ItemWithReturnBlock<E> block);

  public RubyArray<E> fill(int start, ItemWithReturnBlock<E> block);

  public RubyArray<E> fill(int start, int length, ItemWithReturnBlock<E> block);

  public Integer index(E target);

  public Integer index(BooleanBlock<E> block);

  public <S> RubyArray<S> flatten();

  public RubyArray<E> replace(List<E> other);

  public RubyArray<E> insert(int index, E... args);

  public String inspect();

  public String join();

  public String join(String separator);

  public RubyArray<E> keepIf(BooleanBlock<E> block);

  public E last();

  public RubyArray<E> last(int n);

  public E pop();

  public RubyArray<E> pop(int n);

  public RubyArray<RubyArray<E>> product(RubyArray<E>... arys);

  public void product(RubyArray<RubyArray<E>> arys, ItemBlock<RubyArray<E>> block);

  public RubyArray<E> push(E item);

  public <S> RubyArray<S> rassoc(S target);

  public RubyArray<E> rejectEx(BooleanBlock<E> block);

  public RubyArray<E> replace(RubyArray<E> other);

  public RubyArray<E> reverse();

  public RubyArray<E> reverseEx();

  public Integer rindex(E target);

  public Integer rindex(BooleanBlock<E> block);

  public RubyArray<E> rotate();

  public RubyArray<E> rotateEx();

  public RubyArray<E> rotate(int count);

  public RubyArray<E> rotateEx(int count);

  public E sample();

  public RubyArray<E> sample(int n);

  public RubyArray<E> selectEx(BooleanBlock block);

  public E shift();

  public RubyArray<E> shift(int n);

  public RubyArray<E> shuffle();

  public RubyArray<E> shuffleEx();

  public E slice(int index);

  public RubyArray<E> slice(int index, int length);

  public E sliceEx(int index);

  public RubyArray<E> sliceEx(int index, int length);

  public int length();

  public RubyArray<E> uniq();

  public RubyArray<E> uniqEx();

  public <S> RubyArray<E> uniq(ItemTransformBlock<E, S> block);

  public RubyArray<E> union(RubyArray<E> other);

  public RubyArray<E> unshift(E item);

  public RubyArray<E> valuesAt(int... indice);

  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others);

  public void zip(RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block);

  public boolean hasAll();

  public boolean hasAll(BooleanBlock block);

  public <K> RubyArray<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block);

  public <S> RubyArray<E> collect(ItemTransformBlock<E, S> block);

  public <S> RubyArray<S> collectConcat(ItemToListBlock<E, S> block);

  public int count();

  public int count(BooleanBlock<E> block);

  public void cycle(ItemBlock<E> block);

  public void cycle(int cycles, ItemBlock<E> block);

  public E detect(E target);

  public E detect(BooleanBlock<E> block);

  public RubyArray<E> drop(int n);

  public RubyArray<E> dropWhile(BooleanBlock block);

  public void eachCons(int n, ItemFromListBlock<E> block);

  public void eachSlice(int n, ItemFromListBlock<E> block);

  public void eachWithIndex(ItemWithIndexBlock<E> vistor);

  public void eachWithObject(Object o, ItemWithObjectBlock<E> block);

  public RubyArray<E> entries();

  public E find(E target);

  public E find(BooleanBlock<E> block);

  public E first();

  public RubyArray<E> first(int n);

  public Integer findIndex(E target);

  public Integer findIndex(BooleanBlock<E> block);

  public RubyArray<E> findAll(BooleanBlock<E> block);

  public <S> RubyArray<S> flatMap(ItemToListBlock<E, S> block);

  public RubyArray<E> grep(String regex);

  public <S> RubyArray<S> grep(String regex, ItemTransformBlock<E, S> block);

  public <K> RubyHash<K, RubyArray<E>> groupBy(ItemTransformBlock<E, K> block);

  public boolean include(E target);

  public boolean hasMember(E target);

  public E inject(String methodName);

  public E inject(E init, String methodName);

  public E inject(InjectBlock<E> block);

  public <S> S inject(S init, InjectWithInitBlock<E, S> block);

  public <S> RubyArray<S> map(ItemTransformBlock<E, S> block);

  public E max();

  public E max(Comparator<? super E> comp);

  public <S> E maxBy(ItemTransformBlock<E, S> block);

  public <S> E maxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public E min();

  public E min(Comparator<? super E> comp);

  public <S> E minBy(ItemTransformBlock<E, S> block);

  public <S> E minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyArray<E> minmax();

  public RubyArray<E> minmax(Comparator<? super E> comp);

  public <S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block);

  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block);

  public RubyArray<RubyArray<E>> permutation();

  public RubyArray<RubyArray<E>> permutation(int n);

  public RubyArray<RubyArray<E>> permutation(int n, ItemBlock<RubyArray<E>> block);

  public RubyArray<RubyArray<E>> repeatedPermutation(int n);

  public RubyArray<RubyArray<E>> repeatedPermutation(int n, ItemBlock<RubyArray<E>> block);

  public boolean hasNone();

  public boolean hasNone(BooleanBlock<E> block);

  public boolean hasOne();

  public boolean hasOne(BooleanBlock<E> block);

  public E reduce(String methodName);

  public E reduce(E init, String methodName);

  public E reduce(InjectBlock<E> block);

  public <S> S reduce(S init, InjectWithInitBlock<E, S> block);

  public RubyArray<E> reject(BooleanBlock block);

  public void reverseEach(ItemBlock block);

  public RubyArray<E> select(BooleanBlock block);

  public RubyArray<E> sort();

  public RubyArray<E> sort(Comparator<? super E> comp);

  public <S> RubyArray<E> sortBy(ItemTransformBlock<E, S> block);

  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyArray<RubyArray<E>> sliceBefore(String regex);

  public RubyArray<RubyArray<E>> sliceBefore(BooleanBlock block);

  public RubyArray<E> take(int n);

  public RubyArray<E> takeWhile(BooleanBlock block);

  public RubyArray<E> toA();

  public RubyArray<E> toAry();

  public String toS();
}
