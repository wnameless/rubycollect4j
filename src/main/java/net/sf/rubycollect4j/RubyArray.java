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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;
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

/**
 * 
 * RubyArray implements all methods refer to the Array of Ruby language.
 * RubyArray is also a Java List.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RubyArray<E> extends RubyEnumerable<E> implements List<E> {

  private final List<E> list;
  private final Random rand = new Random();

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
   * Creates a RubyArray by given List.
   * 
   * @param list
   *          any List
   * @throws NullPointerException
   *           if list is null
   */
  public RubyArray(List<E> list) {
    if (list == null)
      throw new NullPointerException();

    this.list = list;
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
        List<S> itemList = (List<S>) item;
        if (itemList.size() > 0 && itemList.get(0).equals(target))
          return newRubyArray(itemList, true);
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
    if (isEmpty()) {
      return null;
    } else if (index >= 0 && index < size()) {
      return get(index);
    } else if (index <= -1 && index >= -size()) {
      return get(index + size());
    } else {
      return null;
    }
  }

  /**
   * Uses binary search to find an element. Assume this RubyArray is already
   * sorted.
   * 
   * @param target
   *          to be searched
   * @return an element or null
   */
  public E bsearch(E target) {
    if (size() == 0) {
      return null;
    }
    int index = Arrays.binarySearch(list.toArray(), target);
    return index < 0 ? null : get(index);
  }

  /**
   * Uses binary search with a Comparator to Finds an element. Assume this
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
    return index < 0 ? null : get(index);
  }

  /**
   * Uses binary search and a block to find an element. The block needs to
   * compare elements by its own definition and returns a Integer to show the
   * result of comparison (which is much like the result of a Comparator).
   * BinarySearch will be performed based on the comparison result. Assume this
   * RubyArray is already sorted.
   * 
   * @param block
   *          to filter elements
   * @return an element if target found, null otherwise
   */
  public E bsearch(TransformBlock<E, Integer> block) {
    return binarySearch(list, block, 0, list.size() - 1);
  }

  private E binarySearch(List<E> list, TransformBlock<E, Integer> block,
      int left, int right) {
    if (right < left)
      return null;

    int mid = (left + right) >>> 1;
    if (block.yield(list.get(mid)) > 0) {
      return binarySearch(list, block, left, mid - 1);
    } else if (block.yield(list.get(mid)) < 0) {
      return binarySearch(list, block, mid + 1, right);
    } else {
      return list.get(mid);
    }
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
  public RubyArray<E> combination(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> c : combination(n)) {
      block.yield(c);
    }
    return this;
  }

  /**
   * Removes all null elements and stores the rest in a new RubyArray.
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
    boolean isCompacted = false;
    ListIterator<E> li = listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item == null) {
        li.remove();
        isCompacted = true;
      }
    }
    return isCompacted ? this : null;
  }

  /**
   * Appends all elements of other List in this RubyArray.
   * 
   * @param other
   *          a List
   * @return this RubyArray
   */
  public RubyArray<E> concat(List<E> other) {
    addAll(other);
    return this;
  }

  /**
   * Returns the number of target element in this RubyArray.
   * 
   * @param target
   *          to be counted
   * @return an int
   */
  public int count(E target) {
    int count = 0;
    for (E item : list) {
      if (item.equals(target))
        count++;
    }
    return count;
  }

  /**
   * Deletes all elements which equals to target. Return null if nothing is
   * deleted.
   * 
   * @param target
   *          to be deleted
   * @return the target or null
   */
  public E delete(E target) {
    boolean isDeleted = false;
    ListIterator<E> li = listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item.equals(target)) {
        li.remove();
        isDeleted = true;
      }
    }
    return isDeleted ? target : null;
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
  public E delete(E target, TransformBlock<E, E> block) {
    boolean isDeleted = false;
    ListIterator<E> li = listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item.equals(target)) {
        li.remove();
        isDeleted = true;
      }
    }
    return isDeleted ? target : block.yield(target);
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
    if (isEmpty()) {
      return null;
    } else if (index >= 0 && index < size()) {
      return remove(index);
    } else if (index <= -1 && index >= -size()) {
      return remove(index + size());
    } else {
      return null;
    }
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
  public RubyArray<E> deleteIf(BooleanBlock<E> block) {
    ListIterator<E> li = listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (block.yield(item))
        li.remove();
    }
    return this;
  }

  /**
   * Returns a RubyEnumerator of this RubyArray.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> each() {
    return newRubyEnumerator(list);
  }

  public RubyArray<E> each(Block<E> block) {
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
    for (int i = 0; i < size(); i++) {
      block.yield(i);
    }
    return this;
  }

  /**
   * Checks if this is an empty RubyArray.
   * 
   * @return true if this RubyArray is empty, false otherwise.
   */
  public boolean emptyʔ() {
    return isEmpty();
  }

  /**
   * Equivalent to equals().
   * 
   * @param o
   *          any Object
   * @return true if 2 objects are equal, false otherwise
   */
  public boolean eqlʔ(Object o) {
    return equals(o);
  }

  /**
   * Returns the element at any index. -1 represents the last index and so on.
   * 
   * @param index
   *          of an element
   * @return an element
   * @throws IndexOutOfBoundsException
   *           if index is not found
   */
  public E fetch(int index) {
    if (index >= size() || index < -size())
      throw new IndexOutOfBoundsException("index " + index
          + " outside of array bounds: " + -size() + "..." + size());

    return at(index);
  }

  /**
   * Returns the element at any index. -1 represents the last index and so on.
   * 
   * @param index
   *          of an element
   * @param defaultValue
   *          the default value while index is not found
   * @return an element
   */
  public E fetch(int index, E defaultValue) {
    if (index >= size() || index < -size())
      return defaultValue;

    return at(index);
  }

  /**
   * Returns the element at any index. -1 represents the last index and so on.
   * 
   * @param index
   *          of an element
   * @param block
   *          to yield if index is not found
   * @return an element or null
   */
  public E fetch(int index, Block<Integer> block) {
    if (index >= size() || index < -size()) {
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
    for (int i = 0; i < size(); i++) {
      set(i, item);
    }
    return this;
  }

  /**
   * Starts to fill the RubyArray from an index with the element.
   * 
   * @param item
   *          to be filled
   * @param start
   *          where to begin
   * @return this RubyArray
   */
  public RubyArray<E> fill(E item, int start) {
    if (start <= -size())
      return fill(item);

    if (start < 0)
      start += size();

    for (int i = start; i < size(); i++) {
      set(i, item);
    }
    return this;
  }

  /**
   * Starts to fill part of the RubyArray from an index with the element.
   * 
   * @param item
   *          to be filled
   * @param start
   *          where to begin
   * @param length
   *          define the interval to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(E item, int start, int length) {
    if (start < 0) {
      start += size();
      if (start < 0)
        start = 0;
    }
    if (start > size()) {
      for (int i = size(); i < start; i++) {
        add(null);
      }
    }
    for (int i = start; i < size() && i < start + length; i++) {
      set(i, item);
    }
    for (int i = size(); i < start + length; i++) {
      add(item);
    }
    return this;
  }

  /**
   * Fills the whole RubyArray with elements returned by the block.
   * 
   * @param block
   *          to transform elements to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(TransformBlock<Integer, E> block) {
    for (int i = 0; i < size(); i++) {
      set(i, block.yield(i));
    }
    return this;
  }

  /**
   * Starts to fill the RubyArray from an index with the element returned by the
   * block.
   * 
   * @param start
   *          where to begin
   * @param block
   *          to transform elements to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(int start, TransformBlock<Integer, E> block) {
    if (start <= -size())
      return fill(block);

    if (start < 0)
      start += size();

    for (int i = start; i < size(); i++) {
      set(i, block.yield(i));
    }
    return this;
  }

  /**
   * Starts to fill part of the RubyArray from an index with the element
   * returned by the block.
   * 
   * @param start
   *          where to begin
   * @param length
   *          define the interval to be filled
   * @param block
   *          to transform elements to be filled
   * @return this RubyArray
   */
  public RubyArray<E> fill(int start, int length,
      TransformBlock<Integer, E> block) {
    if (start < 0) {
      start += size();
      if (start < 0) {
        start = 0;
      }
    }
    if (start > size()) {
      for (int i = size(); i < start; i++) {
        add(null);
      }
    }
    for (int i = start; i < size() && i < start + length; i++) {
      set(i, block.yield(i));
    }
    for (int i = size(); i < start + length; i++) {
      add(block.yield(i));
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
   * Equivalent to hashCode().
   * 
   * @return an int
   */
  public int hash() {
    return hashCode();
  }

  /**
   * Finds the index of the element which is true returned by the block.
   * 
   * @param block
   *          to filter the target
   * @return an element or null
   */
  public Integer index(BooleanBlock<E> block) {
    Integer index = null;
    for (int i = 0; i < size(); i++) {
      if (block.yield(get(i)))
        return i;
    }
    return index;
  }

  /**
   * Finds the index of the target element.
   * 
   * @param target
   *          to be found
   * @return an element or null
   */
  public Integer index(E target) {
    int index = indexOf(target);
    return index == -1 ? null : index;
  }

  /**
   * Startss to insert elements to this RubyArray from the index.
   * 
   * @param index
   *          to insert from
   * @param args
   *          elements to be inserted
   * @return this RubyArray
   * @throws IndexOutOfBoundsException
   *           if index less than -size
   */
  public RubyArray<E> insert(int index, E... args) {
    if (index < -size()) {
      throw new IndexOutOfBoundsException("IndexError: index " + index
          + " too small for array; minimum: " + -size());
    } else if (index < 0) {
      int relIndex = size() + index + 1;
      for (int i = args.length - 1; i >= 0; i--) {
        add(relIndex, args[i]);
      }
    } else if (index <= size()) {
      for (int i = args.length - 1; i >= 0; i--) {
        add(index, args[i]);
      }
    } else {
      while (index > size()) {
        add(null);
      }
      for (int i = args.length - 1; i >= 0; i--) {
        add(index - args.length + 2, args[i]);
      }
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
   * Stores all common elements into a new RubyArray.
   * 
   * @param other
   *          any List
   * @return a new RubyArray
   */
  public RubyArray<E> intersection(List<E> other) {
    List<E> andList = new ArrayList<E>();
    for (E item : this) {
      if (!andList.contains(item) && contains(item) && other.contains(item))
        andList.add(item);
    }
    return newRubyArray(andList);
  }

  /**
   * Transforms each element to a String, then join them.
   * 
   * @return a String
   */
  public String join() {
    StringBuilder sb = new StringBuilder();
    for (E item : list) {
      if (item != null)
        sb.append(item.toString());
    }
    return sb.toString();
  }

  /**
   * Transforms each element to a String, then join them by the separator.
   * 
   * @param separator
   *          to join elements
   * @return a String
   */
  public String join(String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < size(); i++) {
      if (i > 0)
        sb.append(separator);

      E item = get(i);
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
   * Keeps elements which are false returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return this RubyArray
   */
  public RubyArray<E> keepIf(BooleanBlock<E> block) {
    ListIterator<E> li = listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (!(block.yield(item)))
        li.remove();
    }
    return this;
  }

  /**
   * Returns the last element of this RubyArray.
   * 
   * @return an element
   */
  public E last() {
    if (isEmpty())
      return null;
    else
      return get(size() - 1);
  }

  /**
   * Returns last n elements of this RubyArray.
   * 
   * @param n
   *          number of elements
   * @return a new RubyArray
   */
  public RubyArray<E> last(int n) {
    RubyArray<E> rubyArray = newRubyArray();
    for (int i = size() - 1; i >= size() - n && i >= 0; i--) {
      rubyArray.unshift(get(i));
    }
    return rubyArray;
  }

  /**
   * Equivalent to size().
   * 
   * @return an int
   */
  public int length() {
    return size();
  }

  /**
   * Multiplies this RubyArray by n times.
   * 
   * @param n
   *          multiply n times
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> multiply(int n) {
    if (n < 0)
      throw new IllegalArgumentException("negative argument");

    List<E> multiplyList = new ArrayList<E>();
    for (int i = 0; i < n; i++) {
      for (E item : list) {
        multiplyList.add(item);
      }
    }
    return newRubyArray(multiplyList);
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
  public RubyArray<E> permutation(int n, Block<RubyArray<E>> block) {
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
  public RubyArray<E> permutation(Block<RubyArray<E>> block) {
    for (RubyArray<E> item : permutation()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Appends other List to self into a new RubyArray.
   * 
   * @param other
   *          any List
   * @return a new RubyArray
   */
  public RubyArray<E> plus(List<E> other) {
    List<E> addList = new ArrayList<E>();
    for (E item : list) {
      addList.add(item);
    }
    for (E item : other) {
      addList.add(item);
    }
    return newRubyArray(addList);
  }

  /**
   * Removes the last element of this RubyArray.
   * 
   * @return an element or null
   */
  public E pop() {
    if (isEmpty())
      return null;
    else
      return remove(size() - 1);
  }

  /**
   * Removes the last n element of this RubyArray.
   * 
   * @param n
   *          number of elements
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> pop(int n) {
    if (n < 0)
      throw new IllegalArgumentException("ArgumentError: negative array size");

    RubyArray<E> rubyArray = newRubyArray();
    while (n > 0 && !isEmpty()) {
      rubyArray.add(0, pop());
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
  public RubyArray<RubyArray<E>> product(List<E>... others) {
    return newRubyEnumerator(new ProductIterable<E>(this, others)).toA();
  }

  /**
   * Generates the production of self with List of Lists.
   * 
   * @param others
   *          List of Lists
   * @return a new RubyArray of RubyArrays
   */
  public RubyArray<RubyArray<E>> product(List<? extends List<E>> others) {
    return newRubyEnumerator(new ProductIterable<E>(this, others)).toA();
  }

  /**
   * Generates the productions of self with List of Lists and yield them to the
   * block.
   * 
   * @param others
   *          List of Lists
   * @param block
   *          to yield each production
   * @return this RubyArray
   */
  public RubyArray<E> product(List<? extends List<E>> others,
      Block<RubyArray<E>> block) {
    for (RubyArray<E> comb : product(others)) {
      block.yield(comb);
    }
    return this;
  }

  /**
   * Equivalent to add().
   * 
   * @param item
   *          an element
   * @return this RubyArray
   */
  public RubyArray<E> push(E item) {
    add(item);
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
        List<S> itemList = (List<S>) item;
        if (itemList.size() > 0
            && itemList.get(itemList.size() - 1).equals(target))
          return newRubyArray(itemList, true);
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
  public RubyArray<E> rejectǃ(BooleanBlock<E> block) {
    int beforeLength = size();
    RubyArray<E> rubyArray = deleteIf(block);
    if (rubyArray.size() != beforeLength)
      return rubyArray;
    else
      return null;
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
  public RubyArray<E> repeatedCombination(int n, Block<RubyArray<E>> block) {
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
  public RubyArray<E> repeatedPermutation(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> perm : repeatedPermutation(n)) {
      block.yield(perm);
    }
    return this;
  }

  /**
   * Replaces all elements of self with other List
   * 
   * @param other
   *          any List
   * @return this RubyArray
   */
  public RubyArray<E> replace(List<E> other) {
    clear();
    addAll(other);
    return this;
  }

  /**
   * Reverses this RubyArray and store into a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> reverse() {
    List<E> reversedList = new ArrayList<E>(list);
    Collections.reverse(reversedList);
    return newRubyArray(reversedList);
  }

  /**
   * Reverses this RubyArray.
   * 
   * @return this RubyArray
   */
  public RubyArray<E> reverseǃ() {
    int size = size();
    for (int i = 0; i < size; i++) {
      add(i, remove(size - 1));
    }
    return this;
  }

  /**
   * Returns a RubyEnumerator of this RubyArray reversed.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> rindex() {
    List<E> reversedList = new ArrayList<E>(list);
    Collections.reverse(reversedList);
    return newRubyEnumerator(reversedList);
  }

  /**
   * Finds the last index of the element which is true returned by the block.
   * 
   * @param block
   *          to filter the element
   * @return an element or null
   */
  public Integer rindex(BooleanBlock<E> block) {
    Integer index = null;
    for (int i = size() - 1; i >= 0; i--) {
      if (block.yield(get(i)))
        return i;
    }
    return index;
  }

  /**
   * Finds the last index of the target element.
   * 
   * @param target
   *          to be found
   * @return an element or null
   */
  public Integer rindex(E target) {
    int index = lastIndexOf(target);
    return index == -1 ? null : index;
  }

  /**
   * Moves the first element to the last and store them to a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> rotate() {
    RubyArray<E> rubyArray = newRubyArray(list, true);
    if (rubyArray.size() > 1)
      rubyArray.add(rubyArray.remove(0));

    return rubyArray;
  }

  /**
   * Moves the first element to the last.
   * 
   * @return this RubyArray
   */
  public RubyArray<E> rotateǃ() {
    if (size() > 1)
      add(remove(0));

    return this;
  }

  /**
   * Moves the first element to the last n times and store them to a new
   * RubyArray.
   * 
   * @param n
   *          moves
   * @return a new RubyArray
   */
  public RubyArray<E> rotate(int n) {
    List<E> rotatedList = new ArrayList<E>(list);
    if (rotatedList.size() > 1) {
      while (n != 0) {
        if (n > 0) {
          rotatedList.add(rotatedList.remove(0));
          n--;
        } else {
          rotatedList.add(0, rotatedList.remove(rotatedList.size() - 1));
          n++;
        }
      }
    }
    return newRubyArray(rotatedList);
  }

  /**
   * Moves the first element to the last n times.
   * 
   * @param n
   *          times to perform the rotations
   * @return this RubyArray
   */
  public RubyArray<E> rotateǃ(int n) {
    if (size() > 1) {
      while (n != 0) {
        if (n > 0) {
          add(remove(0));
          n--;
        } else {
          add(0, remove(size() - 1));
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
    if (size() > 0)
      return get(rand.nextInt(size()));
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
   *           if n less than 0
   */
  public RubyArray<E> sample(int n) {
    if (n < 0)
      throw new IllegalArgumentException("negative sample number");

    List<Integer> indices = new ArrayList<Integer>();
    for (int i = 0; i < size(); i++) {
      indices.add(i);
    }
    List<E> samples = new ArrayList<E>();
    while (samples.size() < size() && samples.size() < n) {
      samples.add(get(indices.remove(rand.nextInt(indices.size()))));
    }
    return newRubyArray(samples);
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
  public RubyArray<E> selectǃ(BooleanBlock<E> block) {
    int beforeSize = size();
    keepIf(block);
    if (size() == beforeSize)
      return null;
    else
      return this;
  }

  /**
   * Removes the first element.
   * 
   * @return an element
   */
  public E shift() {
    if (isEmpty()) {
      return null;
    } else {
      return remove(0);
    }
  }

  /**
   * Removes the first n element.
   * 
   * @param n
   *          number of elements
   * @return a new RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> shift(int n) {
    if (n < 0)
      throw new IllegalArgumentException("negative array size");

    List<E> shiftedList = new ArrayList<E>();
    while (!isEmpty() && shiftedList.size() < n) {
      shiftedList.add(remove(0));
    }
    return newRubyArray(shiftedList);
  }

  /**
   * Shuffles this RubyArray and store into a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> shuffle() {
    List<E> shuffledList = new ArrayList<E>(list);
    Collections.shuffle(shuffledList);
    return newRubyArray(shuffledList);
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
   *          where to start
   * @param length
   *          size of returned RubyArray
   * @return a new RubyArray
   */
  public RubyArray<E> slice(int index, int length) {
    List<E> slicedList = new ArrayList<E>();
    if (index < -size()) {
      return null;
    } else if (index >= size()) {
      return null;
    } else {
      if (index < 0) {
        index += size();
      }
      for (int i = index; i < size() && i < index + length; i++) {
        slicedList.add(get(i));
      }
    }
    return newRubyArray(slicedList);
  }

  /**
   * Removes an element out of this RubyArray.
   * 
   * @param index
   *          of the element
   * @return an element
   */
  public E sliceǃ(int index) {
    if (isEmpty()) {
      return null;
    } else if (index >= 0 && index < size()) {
      return remove(index);
    } else if (index <= -1 && index >= -size()) {
      return remove(index + size());
    } else {
      return null;
    }
  }

  /**
   * Slices an interval of this RubyArray out of self.
   * 
   * @param index
   *          where to start
   * @param length
   *          size of returned RubyArray
   * @return a new RubyArray
   */
  public RubyArray<E> sliceǃ(int index, int length) {
    List<E> slicedList = new ArrayList<E>();
    if (index < -size()) {
      return null;
    } else if (index >= size()) {
      return null;
    } else {
      if (index < 0) {
        index += size();
      }
      for (int i = index; i < size() && length > 0;) {
        slicedList.add(remove(i));
        length--;
      }
    }
    return newRubyArray(slicedList);
  }

  /**
   * Sorts this RubyArray.
   * 
   * @return this RubyArray
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public RubyArray<E> sortǃ() {
    if (size() <= 1)
      return this;

    E sample = first();
    if (sample instanceof Comparable) {
      Collections.sort(list, new Comparator() {

        @Override
        public int compare(Object arg0, Object arg1) {
          return ((Comparable) arg0).compareTo(arg1);
        }

      });
      return this;
    } else {
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + get(0).getClass() + " with " + get(1).getClass() + " failed");
    }
  }

  /**
   * Sorts this RubyArray by given Comparator.
   * 
   * @param comp
   *          a Comparator
   * @return this RubyArray
   */
  public RubyArray<E> sortǃ(Comparator<E> comp) {
    if (size() <= 1) {
      return this;
    }
    Collections.sort(list, comp);
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
      TransformBlock<E, S> block) {
    Map<S, List<E>> map = new LinkedHashMap<S, List<E>>();
    for (E item : list) {
      S key = block.yield(item);
      if (!map.containsKey(key))
        map.put(key, new ArrayList<E>());

      map.get(key).add(item);
    }
    List<S> keys = new ArrayList<S>(map.keySet());
    Collections.sort(keys, comp);
    clear();
    for (S key : keys) {
      Collection<E> coll = map.get(key);
      Iterator<E> iterator = coll.iterator();
      while (iterator.hasNext()) {
        list.add(iterator.next());
      }
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
  public <S> RubyArray<E> sortByǃ(TransformBlock<E, S> block) {
    Map<S, List<E>> map = new LinkedHashMap<S, List<E>>();
    for (E item : list) {
      S key = block.yield(item);
      if (!map.containsKey(key))
        map.put(key, new ArrayList<E>());

      map.get(key).add(item);
    }
    List<S> keys = new ArrayList<S>(map.keySet());
    keys = newRubyEnumerator(keys).sort();
    clear();
    for (S key : keys) {
      Collection<E> coll = map.get(key);
      Iterator<E> iterator = coll.iterator();
      while (iterator.hasNext()) {
        list.add(iterator.next());
      }
    }
    return this;
  }

  /**
   * Eliminate all elements from other List and store into a new RubyArray.
   * 
   * @param other
   *          any List
   * @return a new RubyArray
   */
  public RubyArray<E> minus(List<E> other) {
    List<E> minusList = new ArrayList<E>();
    for (E item : list) {
      minusList.add(item);
    }
    for (E item : other) {
      List<E> target = new ArrayList<E>();
      target.add(item);
      minusList.removeAll(target);
    }
    return newRubyArray(minusList);
  }

  /**
   * Equivalent to toString().
   * 
   * @return a String
   */
  public String toS() {
    return toString();
  }

  /**
   * Assume this RubyArray is a matrix and transpose this matrix into a new
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
  @SuppressWarnings("rawtypes")
  public <S> RubyArray<RubyArray<S>> transpose() {
    if (isEmpty()) {
      return newRubyArray();
    }
    Integer size = null;
    for (E item : list) {
      if (!(item instanceof List)) {
        throw new TypeConstraintException(
            "TypeError: no implicit conversion of "
                + item.getClass().toString() + " into List");
      }
      if (size == null) {
        size = ((List) item).size();
      } else if (size != ((List) item).size()) {
        throw new IndexOutOfBoundsException(
            "IndexError: element size differs (" + ((List) item).size()
                + " should be " + size + ")");
      }
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
   * Equivalent to union().
   * 
   * @param other
   *          any List
   * @return a new RubyArray
   */
  public RubyArray<E> U(RubyArray<E> other) {
    return union(other);
  }

  /**
   * Store all distinct elements into a new RubyArray.
   * 
   * @param other
   *          any List
   * @return a new RubyArray
   */
  public RubyArray<E> union(List<E> other) {
    List<E> unionList = new ArrayList<E>();
    for (E item : list) {
      if (!unionList.contains(item)) {
        unionList.add(item);
      }
    }
    for (E item : other) {
      if (!unionList.contains(item)) {
        unionList.add(item);
      }
    }
    return newRubyArray(unionList);
  }

  /**
   * Unique this RubyArray and store the result to a new RubyArray.
   * 
   * @return a new RubyArray
   */
  public RubyArray<E> uniq() {
    Set<E> uniqSet = new LinkedHashSet<E>();
    for (E item : list) {
      uniqSet.add(item);
    }
    return newRubyArray(uniqSet);
  }

  /**
   * Unique this RubyArray by the result returned by the block and store the
   * result to a new RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a new RubyArray
   */
  public <S> RubyArray<E> uniq(TransformBlock<E, S> block) {
    List<E> uniqList = new ArrayList<E>();
    Set<S> uniqSet = new LinkedHashSet<S>();
    for (E item : list) {
      S trans = block.yield(item);
      if (!uniqSet.contains(trans)) {
        uniqSet.add(trans);
        uniqList.add(item);
      }
    }
    return newRubyArray(uniqList);
  }

  /**
   * Unique this RubyArray. Return null if unchanged.
   * 
   * @return this RubyArray or null
   */
  public RubyArray<E> uniqǃ() {
    int beforeSize = size();
    RubyArray<E> uniqList = uniq();
    clear();
    addAll(uniqList);
    return size() == beforeSize ? null : this;
  }

  /**
   * Unique this RubyArray by the result returned by the block.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return this RubyArray
   */
  public <S> RubyArray<E> uniqǃ(TransformBlock<E, S> block) {
    int beforeSize = size();
    List<E> uniqList = new ArrayList<E>();
    Set<S> uniqSet = new LinkedHashSet<S>();
    for (E item : list) {
      S trans = block.yield(item);
      if (!uniqSet.contains(trans)) {
        uniqSet.add(trans);
        uniqList.add(item);
      }
    }
    clear();
    addAll(uniqList);
    return size() == beforeSize ? null : this;
  }

  /**
   * Put an element at beginning of this RubyArray
   * 
   * @param item
   *          an element
   * @return this RubyArray
   */
  public RubyArray<E> unshift(E item) {
    add(0, item);
    return this;
  }

  /**
   * Store all elements from indices in a RubyArray.
   * 
   * @param indices
   *          where elements to be found
   * @return a new RubyArray
   */
  public RubyArray<E> valuesAt(int... indices) {
    List<E> values = new ArrayList<E>();
    for (int index : indices) {
      values.add(this.at(index));
    }
    return newRubyArray(values);
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

}
