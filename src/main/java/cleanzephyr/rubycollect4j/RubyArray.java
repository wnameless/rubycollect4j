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

import cleanzephyr.rubycollect4j.blocks.Block;
import cleanzephyr.rubycollect4j.blocks.BooleanBlock;
import cleanzephyr.rubycollect4j.blocks.IndexBlock;
import cleanzephyr.rubycollect4j.blocks.InjectBlock;
import cleanzephyr.rubycollect4j.blocks.InjectWithInitBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemFromListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithObjectBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithReturnBlock;
import cleanzephyr.rubycollect4j.blocks.ItemToListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;
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

  public RubyArray<E> ㄍ(E item);

  /**
   * Create a RubyArray which contains a intersection set of two Lists.
   *
   * @param other other List
   * @return new RubyArray
   */
  public RubyArray<E> intersection(List<E> other);

  public RubyArray<E> Ⴖ(List<E> other);

  /**
   * Create a RubyArray which contains n copies of current RubyArray.
   *
   * @param n number of copies
   * @return new RubyArray
   */
  public RubyArray<E> multiply(int n);

  public RubyArray<E> X(int n);

  /**
   * Create a String which all elements are joined by separator.
   *
   * @param separator to join elements
   * @return String
   */
  public String multiply(String separator);

  public String X(String separator);

  /**
   * Create a RubyArray which contains elements in both Lists
   *
   * @param other other List
   * @return new RubyArray
   */
  public RubyArray<E> add(List<E> other);

  public RubyArray<E> 十(List<E> other);

  /**
   * Create a RubyArray which eliminates all elements from other List.
   *
   * @param other other List
   * @return new RubyArray
   */
  public RubyArray<E> minus(List<E> other);

  public RubyArray<E> ㄧ(List<E> other);

  /**
   * Create a RubyArray starts from a List which contains the target as the
   * first element. Return null if target can't be found.
   *
   * @param <S> generic type of other kind of List
   * @param target the first element of List to find
   * @return new RubyArray or null
   */
  public <S> RubyArray<S> assoc(S target);

  /**
   * Find the element of specific position. Index is started from 0. -1 is
   * treated as the last element.
   *
   * @param index position of element
   * @return element
   */
  public E at(int index);

  /**
   * Find a element by binary search. Return null if element can't be found.
   *
   * @param target element to find
   * @return element or null
   */
  public E bsearch(E target);

  /**
   * Find a element by binary search. Return null if element can't be found.
   *
   * @param target element to find
   * @return element or null
   */
  public E bsearch(E target, Comparator<? super E> comp);

  /**
   * Generate all combinations with certain length and put them in a RubyArray.
   *
   * @param n length of each combination
   * @return new RubyArray
   */
  public RubyEnumerator<RubyArray<E>> combination(int n);

  /**
   * Generate all combinations with certain length and yield each combination to
   * the block.
   *
   * @param n length of each combination
   * @param block thing to do with each combination
   * @return new RubyArray contains all combinations
   */
  public RubyArray<RubyArray<E>> combination(int n, ItemBlock<RubyArray<E>> block);

  /**
   * Remove all null objects within self and store the rest of elements in a new
   * RubyArray.
   *
   * @return new RubyArray
   */
  public RubyArray<E> compact();

  /**
   * Remove all null objects in self.
   *
   * @return RubyArray
   */
  public RubyArray<E> compactǃ();

  /**
   * Append any List in the end of self.
   *
   * @param other any List
   * @return RubyArray
   */
  public RubyArray<E> concat(List<E> other);

  public int count(E target);

  public E delete(E target);

  public E delete(E target, Block<E> block);

  public E deleteAt(int index);

  public RubyArray<E> deleteIf(BooleanBlock<E> block);

  public RubyArray<E> each(ItemBlock<E> block);

  public RubyArray<E> eachIndex(IndexBlock<E> block);

  public boolean emptyʔ();

  public boolean eqlʔ(RubyArray<E> other);

  public E fetch(int index);

  public E fetch(int index, E defaultValue);

  public E fetch(int index, ItemBlock<Integer> block);

  public RubyArray<E> fill(E item);

  public RubyArray<E> fill(E item, int start);

  public RubyArray<E> fill(E item, int start, int length);

  public RubyArray<E> fill(ItemWithReturnBlock<E> block);

  public RubyArray<E> fill(int start, ItemWithReturnBlock<E> block);

  public RubyArray<E> fill(int start, int length, ItemWithReturnBlock<E> block);

  public Integer index(E target);

  public Integer index(BooleanBlock<E> block);

  public <S> RubyArray<S> flatten();

  /**
   * Generate all repeated combinations with certain length and put them in a
   * RubyArray.
   *
   * @param n length of each combination
   * @return new RubyArray
   */
  public RubyEnumerator<RubyArray<E>> repeatedCombination(int n);

  /**
   * Generate all repeated combinations with certain length and yield each
   * combination to the block.
   *
   * @param n length of each combination
   * @param block thing to do with each combination
   * @return RubyArray
   */
  public RubyArray<E> repeatedCombination(int n, ItemBlock<RubyArray<E>> block);

  public RubyEnumerator<RubyArray<E>> repeatedPermutation(int n);

  public RubyArray<E> repeatedPermutation(int n, ItemBlock<RubyArray<E>> block);

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

  public RubyArray<E> product(RubyArray<RubyArray<E>> arys, ItemBlock<RubyArray<E>> block);

  public RubyArray<E> push(E item);

  public <S> RubyArray<S> rassoc(S target);

  public RubyArray<E> rejectǃ(BooleanBlock<E> block);

  public RubyArray<E> replace(RubyArray<E> other);

  public RubyArray<E> reverse();

  public RubyArray<E> reverseǃ();

  public Integer rindex(E target);

  public Integer rindex(BooleanBlock<E> block);

  public RubyArray<E> rotate();

  public RubyArray<E> rotateǃ();

  public RubyArray<E> rotate(int count);

  public RubyArray<E> rotateǃ(int count);

  public E sample();

  public RubyArray<E> sample(int n);

  public RubyArray<E> selectǃ(BooleanBlock block);

  public E shift();

  public RubyArray<E> shift(int n);

  public RubyArray<E> shuffle();

  public RubyArray<E> shuffleǃ();

  public E slice(int index);

  public RubyArray<E> slice(int index, int length);

  public E sliceǃ(int index);

  public RubyArray<E> sliceǃ(int index, int length);

  public int length();

  public RubyArray<E> uniq();

  public RubyArray<E> uniqǃ();

  public <S> RubyArray<E> uniq(ItemTransformBlock<E, S> block);

  public RubyArray<E> union(RubyArray<E> other);

  public RubyArray<E> U(RubyArray<E> other);

  public RubyArray<E> ǀ(RubyArray<E> other);

  public RubyArray<E> unshift(E item);

  public RubyArray<E> valuesAt(int... indice);

  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others);

  public void zip(RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block);

  public boolean allʔ();

  public boolean allʔ(BooleanBlock block);

  public boolean anyʔ();

  public boolean anyʔ(BooleanBlock block);

  public <K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block);

  public <S> RubyArray<S> collect(ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> collect();

  public <S> RubyArray<S> collectConcat(ItemToListBlock<E, S> block);

  public RubyEnumerator<E> collectConcat();

  public int count();

  public int count(BooleanBlock<E> block);

  public void cycle(ItemBlock<E> block);

  public RubyEnumerator<E> cycle();

  public void cycle(int cycles, ItemBlock<E> block);

  public RubyEnumerator<E> cycle(int cycles);

  public E detect(BooleanBlock<E> block);

  public RubyEnumerator<E> detect();

  public RubyArray<E> drop(int n);

  public RubyArray<E> dropWhile(BooleanBlock block);

  public void eachCons(int n, ItemFromListBlock<E> block);

  public RubyEnumerator<RubyArray<E>> eachCons(int n);

  public RubyArray<E> eachEntry(ItemBlock<E> block);

  public RubyEnumerator<E> eachEntry();

  public void eachSlice(int n, ItemFromListBlock<E> block);

  public RubyEnumerator<RubyArray<E>> eachSlice(int n);

  public RubyArray<E> eachWithIndex(ItemWithIndexBlock<E> vistor);

  public RubyEnumerator<Entry<E, Integer>> eachWithIndex();

  public <S> S eachWithObject(S o, ItemWithObjectBlock<E, S> block);

  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o);

  public RubyArray<E> entries();

  public E find(BooleanBlock<E> block);

  public RubyEnumerator<E> find();

  public E first();

  public RubyArray<E> first(int n);

  public Integer findIndex(E target);

  public Integer findIndex(BooleanBlock<E> block);

  public RubyEnumerator<E> findIndex();

  public RubyArray<E> findAll(BooleanBlock<E> block);

  public RubyEnumerator<E> findAll();

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

  public E min();

  public E min(Comparator<? super E> comp);

  public <S> E minBy(ItemTransformBlock<E, S> block);

  public <S> E minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> minBy();

  public RubyArray<E> minmax();

  public RubyArray<E> minmax(Comparator<? super E> comp);

  public <S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block);

  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> minmaxBy();

  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block);

  public RubyEnumerator<E> partition();

  public RubyEnumerator<RubyArray<E>> permutation();

  public RubyEnumerator<RubyArray<E>> permutation(int n);

  public RubyArray<RubyArray<E>> permutation(int n, ItemBlock<RubyArray<E>> block);

  public boolean noneʔ();

  public boolean noneʔ(BooleanBlock<E> block);

  public boolean oneʔ();

  public boolean oneʔ(BooleanBlock<E> block);

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

  public RubyArray<E> sort();

  public RubyArray<E> sort(Comparator<? super E> comp);

  public <S> RubyArray<E> sortBy(ItemTransformBlock<E, S> block);

  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block);

  public RubyEnumerator<E> sortBy();

  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex);

  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock block);

  public RubyArray<E> take(int n);

  public RubyArray<E> takeWhile(BooleanBlock block);

  public RubyEnumerator<E> takeWhile();

  public RubyArray<E> toA();

  public RubyArray<E> toAry();

  public String toS();
}
