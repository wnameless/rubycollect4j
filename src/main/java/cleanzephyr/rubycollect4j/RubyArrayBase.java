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
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithReturnBlock;
import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;
import java.util.Comparator;
import java.util.List;

/**
 * An interface which contains all methods a Ruby Array should have. A RubyArray
 * is also a Java List.
 *
 * @param <E> element
 */
public abstract class RubyArrayBase<E> implements RubyArrayEnumerable<E>, List<E> {

  abstract RubyArray<E> ㄍ(E item);

  /**
   * Create a RubyArray which contains a intersection set of two Lists.
   *
   * @param other other List
   * @return new RubyArray
   */
  abstract RubyArray<E> intersection(List<E> other);

  abstract RubyArray<E> Ⴖ(List<E> other);

  /**
   * Create a RubyArray which contains n copies of current RubyArray.
   *
   * @param n number of copies
   * @return new RubyArray
   */
  abstract RubyArray<E> multiply(int n);

  abstract RubyArray<E> X(int n);

  /**
   * Create a String which all elements are joined by separator.
   *
   * @param separator to join elements
   * @return String
   */
  abstract String multiply(String separator);

  abstract String X(String separator);

  /**
   * Create a RubyArray which contains elements in both Lists
   *
   * @param other other List
   * @return new RubyArray
   */
  abstract RubyArray<E> add(List<E> other);

  abstract RubyArray<E> 十(List<E> other);

  /**
   * Create a RubyArray which eliminates all elements from other List.
   *
   * @param other other List
   * @return new RubyArray
   */
  abstract RubyArray<E> minus(List<E> other);

  abstract RubyArray<E> ㄧ(List<E> other);

  /**
   * Create a RubyArray starts from a List which contains the target as the
   * first element. Return null if target can't be found.
   *
   * @param <S> generic type of other kind of List
   * @param target the first element of List to find
   * @return new RubyArray or null
   */
  abstract <S> RubyArray<S> assoc(S target);

  /**
   * Find the element of specific position. Index is started from 0. -1 is
   * treated as the last element.
   *
   * @param index position of element
   * @return element
   */
  abstract E at(int index);

  /**
   * Find a element by binary search. Return null if element can't be found.
   *
   * @param target element to find
   * @return element or null
   */
  abstract E bsearch(E target);

  /**
   * Find a element by binary search. Return null if element can't be found.
   *
   * @param target element to find
   * @return element or null
   */
  abstract E bsearch(E target, Comparator<? super E> comp);

  /**
   * Generate all combinations with certain length and put them in a RubyArray.
   *
   * @param n length of each combination
   * @return new RubyArray
   */
  abstract RubyEnumerator<RubyArray<E>> combination(int n);

  /**
   * Generate all combinations with certain length and yield each combination to
   * the block.
   *
   * @param n length of each combination
   * @param block thing to do with each combination
   * @return new RubyArray contains all combinations
   */
  abstract RubyArray<RubyArray<E>> combination(int n, ItemBlock<RubyArray<E>> block);

  /**
   * Remove all null objects within self and store the rest of elements in a new
   * RubyArray.
   *
   * @return new RubyArray
   */
  abstract RubyArray<E> compact();

  /**
   * Remove all null objects in self.
   *
   * @return RubyArray
   */
  abstract RubyArray<E> compactǃ();

  /**
   * Append any List in the end of self.
   *
   * @param other any List
   * @return RubyArray
   */
  abstract RubyArray<E> concat(List<E> other);

  abstract int count(E target);

  abstract E delete(E target);

  abstract E delete(E target, Block<E> block);

  abstract E deleteAt(int index);

  abstract RubyArray<E> deleteIf(BooleanBlock<E> block);

  abstract RubyArray<E> each(ItemBlock<E> block);

  abstract RubyArray<E> eachIndex(IndexBlock<E> block);

  abstract boolean emptyʔ();

  abstract boolean eqlʔ(RubyArray<E> other);

  abstract E fetch(int index);

  abstract E fetch(int index, E defaultValue);

  abstract E fetch(int index, ItemBlock<Integer> block);

  abstract RubyArray<E> fill(E item);

  abstract RubyArray<E> fill(E item, int start);

  abstract RubyArray<E> fill(E item, int start, int length);

  abstract RubyArray<E> fill(ItemWithReturnBlock<E> block);

  abstract RubyArray<E> fill(int start, ItemWithReturnBlock<E> block);

  abstract RubyArray<E> fill(int start, int length, ItemWithReturnBlock<E> block);

  abstract Integer index(E target);

  abstract Integer index(BooleanBlock<E> block);

  abstract <S> RubyArray<S> flatten();

  /**
   * Generate all repeated combinations with certain length and put them in a
   * RubyArray.
   *
   * @param n length of each combination
   * @return new RubyArray
   */
  abstract RubyEnumerator<RubyArray<E>> repeatedCombination(int n);

  /**
   * Generate all repeated combinations with certain length and yield each
   * combination to the block.
   *
   * @param n length of each combination
   * @param block thing to do with each combination
   * @return RubyArray
   */
  abstract RubyArray<E> repeatedCombination(int n, ItemBlock<RubyArray<E>> block);

  abstract RubyEnumerator<RubyArray<E>> repeatedPermutation(int n);

  abstract RubyArray<E> repeatedPermutation(int n, ItemBlock<RubyArray<E>> block);

  abstract RubyArray<E> replace(List<E> other);

  abstract RubyArray<E> insert(int index, E... args);

  abstract String inspect();

  abstract String join();

  abstract String join(String separator);

  abstract RubyArray<E> keepIf(BooleanBlock<E> block);

  abstract E last();

  abstract RubyArray<E> last(int n);

  abstract E pop();

  abstract RubyArray<E> pop(int n);

  abstract RubyArray<RubyArray<E>> product(RubyArray<E>... arys);

  abstract RubyArray<E> product(RubyArray<RubyArray<E>> arys, ItemBlock<RubyArray<E>> block);

  abstract RubyArray<E> push(E item);

  abstract <S> RubyArray<S> rassoc(S target);

  abstract RubyArray<E> rejectǃ(BooleanBlock<E> block);

  abstract RubyArray<E> replace(RubyArray<E> other);

  abstract RubyArray<E> reverse();

  abstract RubyArray<E> reverseǃ();

  abstract Integer rindex(E target);

  abstract Integer rindex(BooleanBlock<E> block);

  abstract RubyArray<E> rotate();

  abstract RubyArray<E> rotateǃ();

  abstract RubyArray<E> rotate(int count);

  abstract RubyArray<E> rotateǃ(int count);

  abstract E sample();

  abstract RubyArray<E> sample(int n);

  abstract RubyArray<E> selectǃ(BooleanBlock block);

  abstract E shift();

  abstract RubyArray<E> shift(int n);

  abstract RubyArray<E> shuffle();

  abstract RubyArray<E> shuffleǃ();

  abstract E slice(int index);

  abstract RubyArray<E> slice(int index, int length);

  abstract E sliceǃ(int index);

  abstract RubyArray<E> sliceǃ(int index, int length);

  abstract int length();

  abstract RubyArray<E> uniq();

  abstract RubyArray<E> uniqǃ();

  abstract <S> RubyArray<E> uniq(ItemTransformBlock<E, S> block);

  abstract RubyArray<E> union(RubyArray<E> other);

  abstract RubyArray<E> U(RubyArray<E> other);

  abstract RubyArray<E> ǀ(RubyArray<E> other);

  abstract RubyArray<E> unshift(E item);

  abstract RubyArray<E> valuesAt(int... indice);

  abstract RubyEnumerator<RubyArray<E>> permutation();

  abstract RubyEnumerator<RubyArray<E>> permutation(int n);

  abstract RubyArray<RubyArray<E>> permutation(int n, ItemBlock<RubyArray<E>> block);

  abstract RubyArray<E> toAry();

  abstract String toS();
}
