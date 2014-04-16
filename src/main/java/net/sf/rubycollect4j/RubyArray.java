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
package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.iter.CombinationIterable;
import net.sf.rubycollect4j.iter.EachIndexIterable;
import net.sf.rubycollect4j.iter.PermutationIterable;
import net.sf.rubycollect4j.iter.ProductIterable;
import net.sf.rubycollect4j.iter.RepeatedCombinationIterable;
import net.sf.rubycollect4j.iter.RepeatedPermutationIterable;
import net.sf.rubycollect4j.packer.Directive;
import net.sf.rubycollect4j.packer.Packer;
import net.sf.rubycollect4j.util.TryComparator;

/**
 * 
 * RubyArray implements all methods refer to the Array of Ruby language.
 * RubyArray is also a Java List.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RubyArray<E> extends RubyEnumerable<E> implements List<E>,
    Comparable<RubyArray<E>>, Serializable {

  private static final long serialVersionUID = 1L;

  private List<E> list;
  private boolean isFrozen = false;

  /**
   * Returns a RubyArray which wraps the given List.
   * 
   * @param list
   *          any List
   * @return a RubyArray
   */
  public static <E> RubyArray<E> of(List<E> list) {
    if (list == null)
      throw new NullPointerException();

    return new RubyArray<E>(list);
  }

  /**
   * Returns a RubyArray which copies the elements of given Iterable.
   * 
   * @param list
   *          any Iterable
   * @return a RubyArray
   */
  public static <E> RubyArray<E> copyOf(Iterable<E> elements) {
    if (elements == null)
      throw new NullPointerException();

    List<E> list = new ArrayList<E>();
    for (E e : elements) {
      list.add(e);
    }
    return new RubyArray<E>(list);
  }

  @Override
  protected Iterable<E> getIterable() {
    return list;
  }

  /**
   * Creates a RubyArray.
   */
  public RubyArray() {
    list = new ArrayList<E>();
  }

  /**
   * Creates a RubyArray by given List. It's a wrapper implementation. No
   * defensive copy is made.
   * 
   * @param list
   *          a List
   * @throws NullPointerException
   *           if list is null
   */
  public RubyArray(List<E> list) {
    if (list == null)
      throw new NullPointerException();

    this.list = list;
  }

  /**
   * Creates a RubyArray by given Iterable.
   * 
   * @param iter
   *          an Iterable
   */
  public RubyArray(Iterable<? extends E> iter) {
    if (iter == null)
      throw new NullPointerException();

    list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
  }

  /**
   * Finds a List element which contains the target as the first element.
   * 
   * @param <S>
   *          the type of any List
   * @param target
   *          the first element of returned List
   * @return a List or null
   */
  public <S> RubyArray<S> assoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        @SuppressWarnings("unchecked")
        List<S> lst = (List<S>) item;
        if (lst.size() > 0) {
          if (target == null ? lst.get(0) == null : target.equals(lst.get(0)))
            return RubyArray.copyOf(lst);
        }
      }
    }
    return null;
  }

  /**
   * Returns the element at any index. -1 represents the last index and so on.
   * 
   * @param index
   *          of an element
   * @return an element or null
   */
  public E at(int index) {
    if (list.isEmpty())
      return null;
    else if (index >= 0 && index < list.size())
      return list.get(index);
    else if (index <= -1 && index >= -list.size())
      return list.get(index + list.size());
    else
      return null;
  }

  /**
   * Uses binary search to find an element. Assumes this RubyArray is already
   * sorted.
   * 
   * @param target
   *          to be searched
   * @return an element or null
   */
  public E bsearch(E target) {
    if (list.size() == 0)
      return null;

    int index = Arrays.binarySearch(list.toArray(), target);
    return index < 0 ? null : list.get(index);
  }

  /**
   * Uses binary search with a Comparator to Finds an element. Assumes this
   * RubyArray is already sorted.
   * 
   * @param target
   *          to be searched
   * @param comp
   *          a Comparator is used to sort this RubyArray
   * @return an element or null
   */
  public E bsearch(E target, Comparator<? super E> comp) {
    int index = Collections.binarySearch(list, target, comp);
    return index < 0 ? null : list.get(index);
  }

  /**
   * Uses binary search and a block to find an element. The block needs to
   * compare elements by its own definition and returns an Integer to show the
   * result of comparison (which is much like the result of a Comparator).
   * BinarySearch will be performed based on the comparison result. Assumes this
   * RubyArray is already sorted.
   * 
   * @param block
   *          to filter elements
   * @return an element if target found, null otherwise
   */
  public E bsearch(TransformBlock<? super E, Integer> block) {
    return binarySearch(0, list.size() - 1, block);
  }

  private E binarySearch(int left, int right,
      TransformBlock<? super E, Integer> block) {
    if (right < left)
      return null;

    int mid = (left + right) >>> 1;
    if (block.yield(list.get(mid)) > 0)
      return binarySearch(left, mid - 1, block);
    else if (block.yield(list.get(mid)) < 0)
      return binarySearch(mid + 1, right, block);
    else
      return list.get(mid);
  }

  /**
   * Transforms each element in self.
   * 
   * @param block
   *          to transform elements
   * @return this RubyArray
   */
  public RubyArray<E> collectǃ(TransformBlock<? super E, ? extends E> block) {
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      li.set(block.yield(li.next()));
    }
    return this;
  }

  /**
   * Transforms each element by given method name in self.
   * 
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return this RubyArray
   */
  public RubyArray<E> collectǃ(final String methodName, final Object... args) {
    return collectǃ(new TransformBlock<E, E>() {

      @Override
      public E yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  /**
   * Generates all combinations with length n of this RubyArray.
   * 
   * @param n
   *          length of all combinations
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> combination(int n) {
    return newRubyEnumerator(new CombinationIterable<E>(list, n));
  }

  /**
   * Generates all combinations of length n with elements of this RubyArray and
   * yields them with a block.
   * 
   * @param n
   *          length of all combinations
   * @param block
   *          to yield each combination
   * @return this RubyArray
   */
  public RubyArray<E> combination(int n, Block<? super RubyArray<E>> block) {
    for (RubyArray<E> c : combination(n)) {
      block.yield(c);
    }
    return this;
  }

  /**
   * Removes all null elements and puts the rest into a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> compact() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : list) {
      if (item != null)
        rubyArray.add(item);
    }
    return rubyArray;
  }

  /**
   * Removes all null elements in this RubyArray. Returns null if nothing is
   * removed.
   * 
   * @return this RubyArray or null
   */
  public RubyArray<E> compactǃ() {
    int beforeSize = list.size();
    list.removeAll(Collections.singletonList(null));
    return list.size() == beforeSize ? null : this;
  }

  /**
   * Appends all elements of other List in this RubyArray.
   * 
   * @param other
   *          a List
   * @return this RubyArray
   */
  public RubyArray<E> concat(List<? extends E> other) {
    list.addAll(other);
    return this;
  }

  /**
   * Returns a total number of the target element in this RubyArray.
   * 
   * @param target
   *          to be counted
   * @return the total number
   */
  public int count(E target) {
    return Collections.frequency(list, target);
  }

  /**
   * Deletes all elements which equal to the target. Return null if nothing is
   * deleted.
   * 
   * @param target
   *          to be deleted
   * @return the target or null
   */
  public E delete(E target) {
    int beforeSize = list.size();
    list.removeAll(Collections.singletonList(target));
    return list.size() == beforeSize ? null : target;
  }

  /**
   * Deletes all elements which equals to target. Return the result of the block
   * if nothing is deleted.
   * 
   * @param target
   *          to be deleted
   * @param block
   *          to yield if nothing is deleted
   * @return an element
   */
  public E delete(E target, TransformBlock<? super E, ? extends E> block) {
    int beforeSize = list.size();
    delete(target);
    return list.size() == beforeSize ? block.yield(target) : target;
  }

  /**
   * Deletes the element of any index. -1 represents the last index. Return null
   * if index is not found.
   * 
   * @param index
   *          of an element
   * @return an element or null
   */
  public E deleteAt(int index) {
    if (list.isEmpty())
      return null;
    else if (index >= 0 && index < list.size())
      return list.remove(index);
    else if (index <= -1 && index >= -list.size())
      return list.remove(index + list.size());
    else
      return null;
  }

  /**
   * Returns a RubyEnumerator of this RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> deleteIf() {
    return newRubyEnumerator(this);
  }

  /**
   * Deletes elements which are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this RubyArray
   */
  public RubyArray<E> deleteIf(BooleanBlock<? super E> block) {
    Iterator<E> iter = list.iterator();
    while (iter.hasNext()) {
      E item = iter.next();
      if (block.yield(item))
        iter.remove();
    }
    return this;
  }

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyArray
   */
  @Override
  public RubyArray<E> each(Block<? super E> block) {
    for (E item : list) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Returns a RubyEnumerator of this RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Integer> eachIndex() {
    return newRubyEnumerator(new EachIndexIterable<E>(list));
  }

  /**
   * Yields each index of elements to the block.
   * 
   * @param block
   *          to yield elements
   * @return this RubyArray
   */
  public RubyArray<E> eachIndex(Block<Integer> block) {
    for (int i = 0; i < list.size(); i++) {
      block.yield(i);
    }
    return this;
  }

  /**
   * Checks if this is an empty RubyArray.
   * 
   * @return true if this RubyArray is empty, false otherwise
   */
  public boolean emptyʔ() {
    return list.isEmpty();
  }

  /**
   * Equivalent to equals().
   * 
   * @param o
   *          an Object
   * @return true if 2 objects are equal, false otherwise
   */
  public boolean eqlʔ(Object o) {
    return list.equals(o);
  }

  /**
   * Returns the element at an index. -1 represents the last index and so on.
   * 
   * @param index
   *          of an element
   * @return an element
   * @throws IndexOutOfBoundsException
   *           if index is not found
   */
  public E fetch(int index) {
    if (index >= list.size() || index < -list.size())
      throw new IndexOutOfBoundsException("IndexError: index " + index
          + " outside of array bounds: " + -list.size() + "..." + list.size());

    return at(index);
  }

  /**
   * Returns the element at an index. -1 represents the last index and so on.
   * 
   * @param index
   *          of an element
   * @param defaultValue
   *          the default value while index is not found
   * @return an element
   */
  public E fetch(int index, E defaultValue) {
    if (index >= list.size() || index < -list.size())
      return defaultValue;

    return at(index);
  }

  /**
   * Returns the element at an index. -1 represents the last index and so on.
   * 
   * @param index
   *          of an element
   * @param block
   *          to yield if index is not found
   * @return an element or null
   */
  public E fetch(int index, Block<Integer> block) {
    if (index >= list.size() || index < -list.size()) {
      block.yield(index);
      return null;
    }
    return at(index);
  }

  /**
   * Fills the whole RubyArray with the element.
   * 
   * @param item
   *          to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(E item) {
    return fill(item, 0);
  }

  /**
   * Starts to fill this RubyArray from an index with the element.
   * 
   * @param item
   *          to be filled
   * @param start
   *          where to begin
   * @return this RubyArray
   */
  public RubyArray<E> fill(final E item, int start) {
    return fill(start, new TransformBlock<Integer, E>() {

      @Override
      public E yield(Integer i) {
        return item;
      }

    });
  }

  /**
   * Starts to fill part of this RubyArray from an index with the element.
   * 
   * @param item
   *          to be filled
   * @param start
   *          where to begin
   * @param length
   *          of the interval to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(final E item, int start, int length) {
    return fill(start, length, new TransformBlock<Integer, E>() {

      @Override
      public E yield(Integer i) {
        return item;
      }

    });
  }

  /**
   * Fills the whole RubyArray with elements returned by the block.
   * 
   * @param block
   *          to transform elements to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(TransformBlock<Integer, ? extends E> block) {
    return fill(0, block);
  }

  /**
   * Starts to fill this RubyArray from an index with elements returned by the
   * block.
   * 
   * @param start
   *          where to begin
   * @param block
   *          to transform elements to be filled
   * @return this RubyArray
   */
  public RubyArray<E>
      fill(int start, TransformBlock<Integer, ? extends E> block) {
    if (start <= -list.size())
      return fill(block);

    if (start < 0)
      start += list.size();

    return fill(start, list.size() - start, block);
  }

  /**
   * Starts to fill part of this RubyArray from an index with elements returned
   * by the block.
   * 
   * @param start
   *          where to begin
   * @param length
   *          of the interval to be filled
   * @param block
   *          to transform elements to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(int start, int length,
      TransformBlock<Integer, ? extends E> block) {
    if (start < 0) {
      start += list.size();
      if (start < 0)
        start = 0;
    }
    if (start > list.size()) {
      for (int i = list.size(); i < start; i++) {
        list.add(null);
      }
    }
    for (int i = start; i < list.size() && i < start + length; i++) {
      list.set(i, block.yield(i));
    }
    for (int i = list.size(); i < start + length; i++) {
      list.add(block.yield(i));
    }
    return this;
  }

  /**
   * Flattens RubyArray of RubyArrays to 1 dimension RubyArray.
   * 
   * @param <S>
   *          the type of elements of the flatten RubyArray
   * @return a new RubyArray
   */
  @SuppressWarnings("unchecked")
  public <S> RubyArray<S> flatten() {
    RubyArray<S> rubyArray = newRubyArray();
    List<Object> subLists = new ArrayList<Object>();
    for (E item : list) {
      if (item instanceof List)
        subLists.add(item);
      else
        rubyArray.add((S) item);
    }
    while (!subLists.isEmpty()) {
      List<?> subList = (List<?>) subLists.remove(0);
      for (Object item : subList) {
        if (item instanceof List)
          subLists.add(item);
        else
          rubyArray.add((S) item);
      }
    }
    return rubyArray;
  }

  /**
   * Freezes this RubyArray.
   * 
   * @return this RubyArray
   */
  public RubyArray<E> freeze() {
    if (!isFrozen) {
      list = Collections.unmodifiableList(list);
      isFrozen = true;
    }
    return this;
  }

  /**
   * Checks if this RubyArray is frozen.
   * 
   * @return true if this RubyArray is frozen, false otherwise
   */
  public boolean frozenʔ() {
    return isFrozen;
  }

  /**
   * Equivalent to hashCode().
   * 
   * @return the hash code
   */
  public int hash() {
    return hashCode();
  }

  /**
   * Finds the first index of an element which gets true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public Integer index(BooleanBlock<? super E> block) {
    for (int i = 0; i < list.size(); i++) {
      if (block.yield(list.get(i)))
        return i;
    }
    return null;
  }

  /**
   * Finds the index of an element.
   * 
   * @param target
   *          to be found
   * @return an element or null
   */
  public Integer index(E target) {
    int index = list.indexOf(target);
    return index == -1 ? null : index;
  }

  /**
   * Starts to insert elements to this RubyArray from given index.
   * 
   * @param index
   *          where to begin
   * @param args
   *          elements to be inserted
   * @return this RubyArray
   * @throws IndexOutOfBoundsException
   *           if index is less than -size
   */
  public RubyArray<E> insert(int index, E... args) {
    if (index < -list.size() - 1)
      throw new IndexOutOfBoundsException("IndexError: index " + index
          + " too small for array; minimum: " + -list.size());

    if (index < 0) {
      int relIndex = list.size() + index + 1;
      list.addAll(relIndex, Arrays.asList(args));
    } else if (index <= list.size()) {
      list.addAll(index, Arrays.asList(args));
    } else {
      while (index > list.size()) {
        list.add(null);
      }
      list.addAll(index, Arrays.asList(args));
    }
    return this;
  }

  /**
   * Equivalent to toString().
   * 
   * @return a String
   */
  public String inspect() {
    return toString();
  }

  /**
   * Puts all common elements into a new RubyArray.
   * 
   * @param other
   *          a List
   * @return a new RubyArray
   */
  public RubyArray<E> intersection(List<? extends E> other) {
    Set<E> thatSet = new HashSet<E>(other);
    Set<E> thisSet = new HashSet<E>();
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : list) {
      if (thisSet.add(item) && thatSet.contains(item))
        rubyArray.add(item);
    }
    return rubyArray;
  }

  /**
   * Transforms each element to a String, then joins them.
   * 
   * @return a String
   */
  public String join() {
    return join("");
  }

  /**
   * Transforms each element to a String, then joins them by the separator.
   * 
   * @param separator
   *          used to join elements
   * @return a String
   */
  public String join(String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      if (i > 0)
        sb.append(separator);

      E item = list.get(i);
      if (item != null)
        sb.append(item.toString());
    }
    return sb.toString();
  }

  /**
   * Returns a RubyEnumerator of this RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> keepIf() {
    return newRubyEnumerator(list);
  }

  /**
   * Keeps elements which are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this RubyArray
   */
  public RubyArray<E> keepIf(BooleanBlock<? super E> block) {
    Iterator<E> iter = list.iterator();
    while (iter.hasNext()) {
      E item = iter.next();
      if (!block.yield(item))
        iter.remove();
    }
    return this;
  }

  /**
   * Returns the last element of this RubyArray.
   * 
   * @return an element
   */
  public E last() {
    if (list.isEmpty())
      return null;
    else
      return list.get(list.size() - 1);
  }

  /**
   * Returns last n elements of this RubyArray.
   * 
   * @param n
   *          number of elements
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n is less than 0
   */
  public RubyArray<E> last(int n) {
    if (n < 0)
      throw new IllegalArgumentException("ArgumentError: negative array size");

    int start = list.size() - n;
    if (start < 0)
      start = 0;

    RubyArray<E> rubyArray = newRubyArray();
    rubyArray.addAll(list.subList(start, list.size()));
    return rubyArray;
  }

  /**
   * Equivalent to size().
   * 
   * @return the size of this RubyArray
   */
  public int length() {
    return list.size();
  }

  /**
   * Equivalent to collectǃ().
   * 
   * @param block
   *          to transform elements
   * @return this RubyArray
   */
  public RubyArray<E> mapǃ(TransformBlock<? super E, ? extends E> block) {
    return collectǃ(block);
  }

  /**
   * Equivalent to collectǃ().
   * 
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return this RubyArray
   */
  public RubyArray<E> mapǃ(String methodName, Object... args) {
    return collectǃ(methodName, args);
  }

  /**
   * Multiplies this RubyArray by n times.
   * 
   * @param n
   *          multiply n times
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n is less than 0
   */
  public RubyArray<E> multiply(int n) {
    if (n < 0)
      throw new IllegalArgumentException("ArgumentError: negative argument");

    RubyArray<E> rubyArray = newRubyArray();
    for (int i = 0; i < n; i++) {
      rubyArray.addAll(list);
    }
    return rubyArray;
  }

  /**
   * Equivalent to join().
   * 
   * @param separator
   *          to join elements
   * @return a String
   */
  public String multiply(String separator) {
    return join(separator);
  }

  /**
   * Packs the contents of this RubyArray into a binary string according to the
   * directives in aTemplateString.
   * 
   * @param aTemplateString
   *          a template string
   * @return a binary string
   * @see Directive
   */
  public String pack(String aTemplateString) {
    return Packer.pack(aTemplateString, this);
  }

  /**
   * Generates a RubyEnumerator which contains all permutations of this
   * RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> permutation() {
    return newRubyEnumerator(new PermutationIterable<E>(list, size()));
  }

  /**
   * Generates a RubyEnumerator which contains all permutations with length n of
   * this RubyArray.
   * 
   * @param n
   *          length of each permutation
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> permutation(int n) {
    return newRubyEnumerator(new PermutationIterable<E>(list, n));
  }

  /**
   * Yields all permutations with length n of this RubyArray to the block.
   * 
   * @param n
   *          length of each permutation
   * @param block
   *          to yield each permutation
   * @return this RubyArray
   */
  public RubyArray<E> permutation(int n, Block<? super RubyArray<E>> block) {
    for (RubyArray<E> item : permutation(n)) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Yields all permutations of this RubyArray to the block.
   * 
   * @param block
   *          to yield each permutation
   * @return this RubyArray
   */
  public RubyArray<E> permutation(Block<? super RubyArray<E>> block) {
    for (RubyArray<E> item : permutation()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Appends other List to self into a new RubyArray.
   * 
   * @param other
   *          a List
   * @return a new RubyArray
   */
  public RubyArray<E> plus(List<? extends E> other) {
    RubyArray<E> rubyArray = RubyArray.copyOf(list);
    rubyArray.addAll(other);
    return rubyArray;
  }

  /**
   * Removes the last element of this RubyArray.
   * 
   * @return an element or null
   */
  public E pop() {
    if (list.isEmpty())
      return null;
    else
      return list.remove(list.size() - 1);
  }

  /**
   * Removes the last n element of this RubyArray.
   * 
   * @param n
   *          number of elements
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n is less than 0
   */
  public RubyArray<E> pop(int n) {
    if (n < 0)
      throw new IllegalArgumentException("ArgumentError: negative array size");

    RubyArray<E> rubyArray = newRubyArray();
    while (n > 0 && !list.isEmpty()) {
      rubyArray.unshift(pop());
      n--;
    }
    return rubyArray;
  }

  /**
   * Generates the production of self with other Lists.
   * 
   * @param others
   *          Lists
   * @return a new RubyArray of RubyArrays
   */
  public RubyArray<RubyArray<E>> product(List<? extends E>... others) {
    return newRubyEnumerator(new ProductIterable<E>(this, others)).toA();
  }

  /**
   * Generates the production of self with a List of Lists.
   * 
   * @param others
   *          a List of Lists
   * @return a new RubyArray of RubyArrays
   */
  public RubyArray<RubyArray<E>> product(
      List<? extends List<? extends E>> others) {
    return newRubyEnumerator(new ProductIterable<E>(this, others)).toA();
  }

  /**
   * Generates the productions of self with a List of Lists and yield them to
   * the block.
   * 
   * @param others
   *          a List of Lists
   * @param block
   *          to yield each production
   * @return this RubyArray
   */
  public RubyArray<E> product(List<? extends List<? extends E>> others,
      Block<RubyArray<E>> block) {
    for (RubyArray<E> comb : product(others)) {
      block.yield(comb);
    }
    return this;
  }

  /**
   * Adds the item to the end of this RubyArray.
   * 
   * @param item
   *          an element
   * @return this RubyArray
   */
  public RubyArray<E> push(E item) {
    list.add(item);
    return this;
  }

  /**
   * Adds items to the end of this RubyArray.
   * 
   * @param items
   *          an array of element
   * @return this RubyArray
   */
  public RubyArray<E> push(E... items) {
    list.addAll(Arrays.asList(items));
    return this;
  }

  /**
   * Finds a List element which contains target as the last element.
   * 
   * @param <S>
   *          the type of any List
   * @param target
   *          the last element of returned List
   * @return a List or null
   */
  public <S> RubyArray<S> rassoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        @SuppressWarnings("unchecked")
        List<S> lst = (List<S>) item;
        if (lst.size() > 0) {
          if (target == null ? lst.get(lst.size() - 1) == null : target
              .equals(lst.get(lst.size() - 1)))
            return RubyArray.copyOf(lst);
        }
      }
    }
    return null;
  }

  /**
   * Returns a RubyEnumerator of this RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> rejectǃ() {
    return newRubyEnumerator(list);
  }

  /**
   * Deletes all elements which are false returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this RubyArray
   */
  public RubyArray<E> rejectǃ(BooleanBlock<? super E> block) {
    int beforeSize = list.size();
    RubyArray<E> rubyArray = deleteIf(block);
    return rubyArray.size() == beforeSize ? null : rubyArray;
  }

  /**
   * Generates all repeated combinations of this RubyArray.
   * 
   * @param n
   *          length of each repeated combination
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> repeatedCombination(int n) {
    return newRubyEnumerator(new RepeatedCombinationIterable<E>(list, n));
  }

  /**
   * Generates all repeated combinations with length n of this RubyArray and
   * yield to the block.
   * 
   * @param n
   *          length of each repeated combination
   * @param block
   *          to yield each repeated combination
   * @return this RubyArray
   */
  public RubyArray<E> repeatedCombination(int n,
      Block<? super RubyArray<E>> block) {
    for (RubyArray<E> c : repeatedCombination(n)) {
      block.yield(c);
    }
    return this;
  }

  /**
   * Generates all repeated permutations with length n of this RubyArray.
   * 
   * @param n
   *          length of each repeated permutation
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> repeatedPermutation(int n) {
    return newRubyEnumerator(new RepeatedPermutationIterable<E>(list, n));
  }

  /**
   * Generates all repeated permutations with length n of this RubyArray and
   * yield to the block.
   * 
   * @param n
   *          length of each repeated permutation
   * @param block
   *          to yield each repeated permutation
   * @return this RubyArray
   */
  public RubyArray<E> repeatedPermutation(int n,
      Block<? super RubyArray<E>> block) {
    for (RubyArray<E> perm : repeatedPermutation(n)) {
      block.yield(perm);
    }
    return this;
  }

  /**
   * Replaces all elements of self with other List.
   * 
   * @param other
   *          a List
   * @return this RubyArray
   */
  public RubyArray<E> replace(List<? extends E> other) {
    list.clear();
    list.addAll(other);
    return this;
  }

  /**
   * Reverses this RubyArray and puts the result into a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> reverse() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : list) {
      rubyArray.unshift(item);
    }
    return rubyArray;
  }

  /**
   * Reverses this RubyArray.
   * 
   * @return this RubyArray
   */
  public RubyArray<E> reverseǃ() {
    Collections.reverse(list);
    return this;
  }

  /**
   * Returns a RubyEnumerator of this RubyArray reversed.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> rindex() {
    return newRubyEnumerator(reverse());
  }

  /**
   * Finds the last index of the element which gets true returned by the block.
   * 
   * @param block
   *          to filter the element
   * @return an element or null
   */
  public Integer rindex(BooleanBlock<? super E> block) {
    for (int i = list.size() - 1; i >= 0; i--) {
      if (block.yield(list.get(i)))
        return i;
    }
    return null;
  }

  /**
   * Finds the last index of the target element.
   * 
   * @param target
   *          to be found
   * @return an element or null
   */
  public Integer rindex(E target) {
    int index = list.lastIndexOf(target);
    return index == -1 ? null : index;
  }

  /**
   * Moves the first element to the last and puts them into a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> rotate() {
    RubyArray<E> rubyArray = RubyArray.copyOf(list);
    if (rubyArray.size() > 1)
      rubyArray.push(rubyArray.shift());
    return rubyArray;
  }

  /**
   * Moves the first element to the last.
   * 
   * @return this RubyArray
   */
  public RubyArray<E> rotateǃ() {
    if (list.size() > 1)
      list.add(list.remove(0));
    return this;
  }

  /**
   * Moves the first element to the last n times and puts them into a new
   * RubyArray.
   * 
   * @param n
   *          moves
   * @return a new RubyArray
   */
  public RubyArray<E> rotate(int n) {
    RubyArray<E> rubyArray = RubyArray.copyOf(list);
    if (rubyArray.size() > 1) {
      while (n != 0) {
        if (n > 0) {
          rubyArray.push(rubyArray.shift());
          n--;
        } else {
          rubyArray.unshift(rubyArray.pop());
          n++;
        }
      }
    }
    return rubyArray;
  }

  /**
   * Moves the first element to the last n times.
   * 
   * @param n
   *          times to perform the rotations
   * @return this RubyArray
   */
  public RubyArray<E> rotateǃ(int n) {
    if (list.size() > 1) {
      while (n != 0) {
        if (n > 0) {
          list.add(list.remove(0));
          n--;
        } else {
          list.add(0, list.remove(list.size() - 1));
          n++;
        }
      }
    }
    return this;
  }

  /**
   * Picks an element randomly.
   * 
   * @return an element
   */
  public E sample() {
    if (list.size() > 0)
      return list.get((int) Math.random() * list.size());
    else
      return null;
  }

  /**
   * Picks n element randomly.
   * 
   * @param n
   *          number of elements
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n is less than 0
   */
  public RubyArray<E> sample(int n) {
    if (n < 0)
      throw new IllegalArgumentException(
          "ArgumentError: negative sample number");

    List<Integer> indices = eachIndex().toA();

    RubyArray<E> rubyArray = newRubyArray();
    while (rubyArray.size() < list.size() && rubyArray.size() < n) {
      rubyArray.add(list.get(indices.remove((int) Math.random()
          * indices.size())));
    }
    return rubyArray;
  }

  /**
   * Returns a RubyEnumerator of this RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> selectǃ() {
    return newRubyEnumerator(list);
  }

  /**
   * Selects elements which the result returned by the block are false. Return
   * null if nothing is changed.
   * 
   * @param block
   *          to filter elements
   * @return this RubyArray or null
   */
  public RubyArray<E> selectǃ(BooleanBlock<? super E> block) {
    int beforeSize = list.size();
    keepIf(block);
    return list.size() == beforeSize ? null : this;
  }

  /**
   * Removes the first element.
   * 
   * @return an element
   */
  public E shift() {
    if (list.isEmpty())
      return null;
    else
      return list.remove(0);
  }

  /**
   * Removes the first n element.
   * 
   * @param n
   *          number of elements
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n is less than 0
   */
  public RubyArray<E> shift(int n) {
    if (n < 0)
      throw new IllegalArgumentException("ArgumentError: negative array size");

    RubyArray<E> rubyArray = newRubyArray();
    while (!list.isEmpty() && rubyArray.size() < n) {
      rubyArray.add(list.remove(0));
    }
    return rubyArray;
  }

  /**
   * Shuffles this RubyArray and puts the result into a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> shuffle() {
    RubyArray<E> rubyArray = RubyArray.copyOf(list);
    Collections.shuffle(rubyArray);
    return rubyArray;
  }

  /**
   * Shuffles this RubyArray.
   * 
   * @return this RubyArray
   */
  public RubyArray<E> shuffleǃ() {
    Collections.shuffle(list);
    return this;
  }

  /**
   * Equivalent to at().
   * 
   * @param index
   *          of an element
   * @return an element or null
   */
  public E slice(int index) {
    return at(index);
  }

  /**
   * Slices an interval of this RubyArray into a new RubyArray.
   * 
   * @param index
   *          where to begin
   * @param length
   *          size of returned RubyArray
   * @return a new RubyArray
   */
  public RubyArray<E> slice(int index, int length) {
    RubyArray<E> rubyArray = newRubyArray();
    if (index < -list.size()) {
      return null;
    } else if (index >= list.size()) {
      return null;
    } else {
      if (index < 0)
        index += list.size();

      for (int i = index; i < list.size() && i < index + length; i++) {
        rubyArray.add(list.get(i));
      }
    }
    return rubyArray;
  }

  /**
   * Removes an element out of this RubyArray.
   * 
   * @param index
   *          of the element
   * @return an element
   */
  public E sliceǃ(int index) {
    if (list.isEmpty())
      return null;
    else if (index >= 0 && index < list.size())
      return list.remove(index);
    else if (index <= -1 && index >= -list.size())
      return list.remove(index + list.size());
    else
      return null;
  }

  /**
   * Slices an interval of this RubyArray out of self.
   * 
   * @param index
   *          where to begin
   * @param length
   *          size of returned RubyArray
   * @return a new RubyArray
   */
  public RubyArray<E> sliceǃ(int index, int length) {
    RubyArray<E> rubyArray = newRubyArray();
    if (index < -list.size()) {
      return null;
    } else if (index >= list.size()) {
      return null;
    } else {
      if (index < 0)
        index += list.size();

      for (int i = index; i < list.size() && length > 0;) {
        rubyArray.add(list.remove(i));
        length--;
      }
    }
    return rubyArray;
  }

  /**
   * Sorts this RubyArray.
   * 
   * @return this RubyArray
   */
  public RubyArray<E> sortǃ() {
    if (list.size() <= 1)
      return this;

    Collections.sort(list, new TryComparator<E>());
    return this;
  }

  /**
   * Sorts this RubyArray by given Comparator.
   * 
   * @param comp
   *          a Comparator
   * @return this RubyArray
   */
  public RubyArray<E> sortǃ(Comparator<? super E> comp) {
    if (list.size() <= 1)
      return this;

    Collections.sort(list, new TryComparator<E>(comp));
    return this;
  }

  /**
   * Return a RubyEnumerator of this RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> sortByǃ() {
    return newRubyEnumerator(list);
  }

  /**
   * Sorts elements of this RubyArray by the ordering of elements transformed by
   * the block induced by the Comparator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return this RubyArray
   */
  public <S> RubyArray<E> sortByǃ(Comparator<? super S> comp,
      TransformBlock<? super E, ? extends S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    list.clear();
    for (S key : rubyHash.keys().sortǃ(comp)) {
      list.addAll(rubyHash.get(key));
    }
    return this;
  }

  /**
   * Sorts elements of this RubyArray by the ordering of elements transformed by
   * the block induced by the Comparator for S and applies the Comparator for E
   * again.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp1
   *          a Comparator for E
   * @param comp2
   *          a Comparator for S
   * @param block
   *          to transform elements
   * @return this RubyArray
   */
  public <S> RubyArray<E>
      sortByǃ(Comparator<? super E> comp1, Comparator<? super S> comp2,
          TransformBlock<? super E, ? extends S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    list.clear();
    for (S key : rubyHash.keys().sortǃ(comp2)) {
      list.addAll(rubyHash.get(key).sortǃ(comp1));
    }
    return this;
  }

  /**
   * Sorts elements of this RubyArray by the ordering of elements transformed by
   * the block.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return this RubyArray
   */
  public <S> RubyArray<E> sortByǃ(TransformBlock<? super E, ? extends S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    list.clear();
    for (S key : rubyHash.keys().sortǃ()) {
      list.addAll(rubyHash.get(key));
    }
    return this;
  }

  /**
   * Sorts elements of this RubyArray by the ordering of elements invoked by
   * given method name.
   * 
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return this RubyArray
   */
  public <S> RubyArray<E>
      sortByǃ(final String methodName, final Object... args) {
    return sortByǃ(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  /**
   * Eliminates all elements from other List and puts the result into a new
   * RubyArray.
   * 
   * @param other
   *          a List
   * @return a new RubyArray
   */
  public RubyArray<E> minus(List<? extends E> other) {
    RubyArray<E> rubyArray = RubyArray.copyOf(list);
    rubyArray.removeAll(other);
    return rubyArray;
  }

  /**
   * Equivalent to toString().
   * 
   * @return a String
   */
  public String toS() {
    return list.toString();
  }

  /**
   * Assumes this RubyArray is a matrix and transpose this matrix into a new
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @return a RubyArray of RubyArrays
   * @throws TypeConstraintException
   *           if S is not a List
   * @throws IndexOutOfBoundsException
   *           if all lists are not the same size
   */
  public <S> RubyArray<RubyArray<S>> transpose() {
    if (list.isEmpty())
      return newRubyArray();

    Integer size = null;
    for (E item : list) {
      if (!(item instanceof List))
        throw new TypeConstraintException(
            "TypeError: no implicit conversion of "
                + item.getClass().toString() + " into List");

      if (size == null)
        size = ((List<?>) item).size();
      else if (size != ((List<?>) item).size())
        throw new IndexOutOfBoundsException(
            "IndexError: element size differs (" + ((List<?>) item).size()
                + " should be " + size + ")");
    }
    RubyArray<RubyArray<S>> rubyArray = newRubyArray();
    for (int i = 0; i < size; i++) {
      RubyArray<S> ra = newRubyArray();
      for (E item : list) {
        @SuppressWarnings("unchecked")
        List<S> lst = (List<S>) item;
        ra.add(lst.get(i));
      }
      rubyArray.add(ra);
    }
    return rubyArray;
  }

  /**
   * Puts all distinct elements into a new RubyArray.
   * 
   * @param other
   *          a List
   * @return a new RubyArray
   */
  public RubyArray<E> union(List<? extends E> other) {
    RubyArray<E> rubyArray = RubyArray.copyOf(list);
    rubyArray.addAll(other);
    rubyArray.uniqǃ();
    return rubyArray;
  }

  /**
   * Filters elements uniquely and puts the result into a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> uniq() {
    RubyArray<E> rubyArray = newRubyArray();
    Set<E> set = new HashSet<E>();
    for (E item : list) {
      if (set.add(item))
        rubyArray.add(item);
    }
    return rubyArray;
  }

  /**
   * Filters elements by the result returned by the block uniquely and puts the
   * result into a new RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a new RubyArray
   */
  public <S> RubyArray<E> uniq(TransformBlock<? super E, ? extends S> block) {
    RubyArray<E> rubyArray = newRubyArray();
    Set<S> set = new HashSet<S>();
    for (E item : list) {
      if (set.add(block.yield(item)))
        rubyArray.add(item);
    }
    return rubyArray;
  }

  /**
   * Filters elements by the result invoked by given method name uniquely and
   * puts the result into a new RubyArray.
   * 
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a new RubyArray
   */
  public <S> RubyArray<E> uniq(final String methodName, final Object... args) {
    return uniq(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  /**
   * Filters elements uniquely. Returns null if unchanged.
   * 
   * @return this RubyArray or null
   */
  public RubyArray<E> uniqǃ() {
    int beforeSize = list.size();
    Set<E> set = new HashSet<E>();
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      if (!set.add(li.next()))
        li.remove();
    }
    return list.size() == beforeSize ? null : this;
  }

  /**
   * Filters elements by the result invoked by given method name uniquely and
   * puts the result into a new RubyArray.
   * 
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return this RubyArray or null
   */
  public <S> RubyArray<E> uniqǃ(final String methodName, final Object... args) {
    return uniqǃ(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  /**
   * Filters elements by the result returned by the block uniquely.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return this RubyArray
   */
  public <S> RubyArray<E> uniqǃ(TransformBlock<? super E, ? extends S> block) {
    int beforeSize = list.size();
    Set<S> set = new HashSet<S>();
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      if (!set.add(block.yield(li.next()))) {
        li.remove();
      }
    }
    return list.size() == beforeSize ? null : this;
  }

  /**
   * Puts an element at beginning of this RubyArray
   * 
   * @param item
   *          an element
   * @return this RubyArray
   */
  public RubyArray<E> unshift(E item) {
    list.add(0, item);
    return this;
  }

  /**
   * Puts all elements from given indices into a RubyArray.
   * 
   * @param indices
   *          where elements to be found
   * @return a new RubyArray
   */
  public RubyArray<E> valuesAt(int... indices) {
    RubyArray<E> rubyArray = newRubyArray();
    for (int index : indices) {
      rubyArray.add(at(index));
    }
    return rubyArray;
  }

  /**
   * Puts all elements from given indices into a RubyArray.
   * 
   * @param indices
   *          where elements to be found
   * @return a new RubyArray
   */
  public RubyArray<E> valuesAt(Iterable<Integer> indices) {
    RubyArray<E> rubyArray = newRubyArray();
    for (int index : indices) {
      rubyArray.add(at(index));
    }
    return rubyArray;
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return list.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return list.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public E get(int index) {
    return list.get(index);
  }

  @Override
  public E set(int index, E element) {
    return list.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    list.add(index, element);
  }

  @Override
  public E remove(int index) {
    return list.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return list.listIterator(index);
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  @Override
  public boolean equals(Object o) {
    return list.equals(o);
  }

  @Override
  public int hashCode() {
    return list.hashCode();
  }

  @Override
  public String toString() {
    return list.toString();
  }

  @Override
  public int compareTo(RubyArray<E> arg0) {
    if (arg0 == null)
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + list.getClass().getName() + " with null failed");

    Comparator<E> comp = new TryComparator<E>();
    int diff;
    for (int i = 0; i < list.size() && i < arg0.size(); i++) {
      if ((diff = comp.compare(list.get(i), arg0.get(i))) != 0)
        return diff;
    }

    if (list.size() > arg0.size())
      return 1;
    else if (list.size() < arg0.size())
      return -1;

    return 0;
  }

}
