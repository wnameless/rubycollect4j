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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.ReduceBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;
import net.sf.rubycollect4j.block.WithInitBlock;
import net.sf.rubycollect4j.block.WithObjectBlock;
import net.sf.rubycollect4j.iter.ChunkIterable;
import net.sf.rubycollect4j.iter.CycleIterable;
import net.sf.rubycollect4j.iter.EachConsIterable;
import net.sf.rubycollect4j.iter.EachSliceIterable;
import net.sf.rubycollect4j.iter.EachWithIndexIterable;
import net.sf.rubycollect4j.iter.EachWithObjectIterable;
import net.sf.rubycollect4j.iter.SliceBeforeIterable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;
import static net.sf.rubycollect4j.RubyArray.newRubyArray;
import static net.sf.rubycollect4j.RubyEnumerator.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyHash.newRubyHash;

;

/**
 * An extension class for any Iterable object. It includes all methods refer to
 * the Enumerable module of Ruby.
 * 
 * @param <E>
 *          the type of the elements
 */
public class RubyEnumerable<E> implements Iterable<E> {

  protected final Iterable<E> iter;

  /**
   * Build up a RubyEnumerable by given Iterable.
   * 
   * @param <E>
   *          the type of the elements
   * @param iter
   *          an Iterable
   * @return a new RubyEnumerable
   */
  public static <E> RubyEnumerable<E> newRubyEnumerable(Iterable<E> iter) {
    return new RubyEnumerable<E>(iter);
  }

  /**
   * Build up an empty RubyEnumerable.
   * 
   * @param <E>
   *          the type of the elements
   * @return a new RubyEnumerable
   */
  public static <E> RubyEnumerable<E> newRubyEnumerable() {
    return new RubyEnumerable<E>();
  }

  /**
   * Construct by given Iterable.
   * 
   * @param iter
   *          an Iterable
   */
  public RubyEnumerable(Iterable<E> iter) {
    this.iter = iter;
  }

  /**
   * Construct an empty RubyEnumerable.
   */
  public RubyEnumerable() {
    this.iter = newArrayList();
  }

  /**
   * Check if null included.
   * 
   * @return true if null is found, false otherwise
   */
  public boolean allʔ() {
    for (E item : iter) {
      if (item == null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if any result returned by the block is false.
   * 
   * @param block
   *          to check elements
   * @return true if all result are true, false otherwise
   */
  public boolean allʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item) == false) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if any not-null object included.
   * 
   * @return true if not-null object is found, false otherwise
   */
  public boolean anyʔ() {
    for (E item : iter) {
      if (item != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * Check if any result returned by the block is true.
   * 
   * @param block
   *          to check elements
   * @return true if any result are true, false otherwise
   */
  public boolean anyʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Chunk elements to entries. Keys of entries are the result returned by the
   * block. Values of entries are RubyArrays of elements which get the same
   * result returned by the block and aside to each other.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to chunk elements
   * @return a RubyEnumerator
   */
  public <S> RubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      TransformBlock<E, S> block) {
    return newRubyEnumerator(new ChunkIterable<E, S>(iter, block));
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> collect() {
    return newRubyEnumerator(iter);
  }

  /**
   * Store elements which are transformed by the block into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<S> collect(TransformBlock<E, S> block) {
    RubyArray<S> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(block.yield(item));
    }
    return rubyArray;
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> collectConcat() {
    return newRubyEnumerator(iter);
  }

  /**
   * Turn each element to a RubyArray and then flatten it.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyArray
   */
  public <S> RubyArray<S> collectConcat(TransformBlock<E, RubyArray<S>> block) {
    RubyArray<S> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.addAll(block.yield(item));
    }
    return rubyArray;
  }

  /**
   * Count the elements.
   * 
   * @return a int
   */
  public int count() {
    int count = 0;
    for (@SuppressWarnings("unused")
    E item : iter) {
      count++;
    }
    return count;
  }

  /**
   * Count the elements which get true returned by the block.
   * 
   * @param block
   *          to define elements to be counted
   * @return a int
   */
  public int count(BooleanBlock<E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        count++;
      }
    }
    return count;
  }

  /**
   * Generate a sequence from start element to end element and so on infinitely.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> cycle() {
    return newRubyEnumerator(Iterables.cycle(iter));
  }

  /**
   * Generate a sequence from start element to end element, repeat n times.
   * 
   * @param n
   *          times to repeat
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> cycle(int n) {
    return newRubyEnumerator(new CycleIterable<E>(iter, n));
  }

  /**
   * Generate a sequence from start element to end element, repeat n times.
   * Yield each element to the block.
   * 
   * @param n
   *          times to repeat
   * @param block
   *          to yield each element
   */
  public void cycle(int n, Block<E> block) {
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  /**
   * Generate a sequence from start element to end element and so on infinitely.
   * Yield each element to the block.
   * 
   * @param block
   *          to yield each element
   */
  public void cycle(Block<E> block) {
    while (true) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> detect() {
    return newRubyEnumerator(iter);
  }

  /**
   * Find the first element which gets true returned by the block. Return null
   * if element is not found.
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E detect(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item)) {
        return item;
      }
    }
    return null;
  }

  /**
   * Drop the first n elements and store rest to a RubyArray.
   * 
   * @param n
   *          number of elements to drop
   * @return a RubyArray
   */
  public RubyArray<E> drop(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to drop negative size");
    }
    RubyArray<E> rubyArray = newRubyArray();
    int i = 0;
    for (E item : iter) {
      if (i >= n) {
        rubyArray.add(item);
      }
      i++;
    }
    return rubyArray;
  }

  /**
   * Return a RubyEnumerator which contains the first element of this
   * RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

  /**
   * Drop the first n elements until a element get false returned by the block.
   * 
   * @param block
   *          to define which elements to be dropped
   * @return a RubyArray
   */
  public RubyArray<E> dropWhile(BooleanBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    boolean cutPoint = false;
    for (E item : iter) {
      if (!block.yield(item) || cutPoint) {
        cutPoint = true;
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  /**
   * Iterate each element and store the element with n - 1 consecutive elements
   * into a RubyArray.
   * 
   * @param n
   *          number of consecutive elements
   * @return a RubyEnumerator
   * @throws IllegalArgumentException
   *           if n less than or equal to 0
   */
  public RubyEnumerator<RubyArray<E>> eachCons(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    return newRubyEnumerator(new EachConsIterable<E>(iter, n));
  }

  /**
   * Iterate each element and yield the element with n - 1 consecutive elements
   * to the block.
   * 
   * @param n
   *          number of consecutive elements
   * @param block
   *          to yield the RubyArray of consecutive elements
   * @throws IllegalArgumentException
   *           if n less than or equal to 0
   */
  public void eachCons(int n, Block<RubyArray<E>> block) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    for (RubyArray<E> cons : eachCons(n)) {
      block.yield(cons);
    }
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> eachEntry() {
    return newRubyEnumerator(iter);
  }

  /**
   * Yield each elements to the block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyEnumerable
   */
  public RubyEnumerable<E> eachEntry(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Slice elements into RubyArrays with length n.
   * 
   * @param n
   *          size of each slice
   * @return a RubyEnumerator
   * @throws IllegalArgumentException
   *           if n less than or equal to 0
   */
  public RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    return newRubyEnumerator(new EachSliceIterable<E>(iter, n));
  }

  /**
   * Slice elements into RubyArrays with length n and yield them to the block.
   * 
   * @param n
   *          size of each slice
   * @param block
   *          to yield each slice
   */
  public void eachSlice(int n, Block<RubyArray<E>> block) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    for (RubyArray<E> ra : new EachSliceIterable<E>(iter, n)) {
      block.yield(ra);
    }
  }

  /**
   * Iterate elements with their indices by Entry.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return newRubyEnumerator(new EachWithIndexIterable<E>(iter));
  }

  /**
   * Iterate elements with their indices by Entry and yield them to the block.
   * 
   * @param block
   *          to yield each Entry
   * @return this RubyEnumerable
   */
  public RubyEnumerable<E> eachWithIndex(WithIndexBlock<E> block) {
    int i = 0;
    for (E item : iter) {
      block.yield(item, i);
      i++;
    }
    return this;
  }

  /**
   * Iterate elements with the Object S.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param o
   *          any Object
   * @return a RubyEnumerator
   */
  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o) {
    return newRubyEnumerator(new EachWithObjectIterable<E, S>(iter, o));
  }

  /**
   * Iterate elements with the Object S and yield them to the block.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param o
   *          any Object
   * @param block
   *          to yield each Entry
   * @return the Object S
   */
  public <S> S eachWithObject(S o, WithObjectBlock<E, S> block) {
    for (E item : iter) {
      block.yield(item, o);
    }
    return o;
  }

  /**
   * Store each element into a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> entries() {
    return newRubyArray(iter);
  }

  /**
   * Equivalent to detect().
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> find() {
    return detect();
  }

  /**
   * Equivalent to detect().
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E find(BooleanBlock<E> block) {
    return detect(block);
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> findAll() {
    return newRubyEnumerator(iter);
  }

  /**
   * Store elements which get true returned by the block into a RubyArray.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> findAll(BooleanBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      if (block.yield(item)) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> findIndex() {
    return newRubyEnumerator(iter);
  }

  /**
   * Find the index of a element which gets true returned by the block. Return
   * null if nothing is found.
   * 
   * @param block
   *          to check elements
   * @return an Integer or null
   */
  public Integer findIndex(BooleanBlock<E> block) {
    int index = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        return index;
      }
      index++;
    }
    return null;
  }

  /**
   * Find the index of the target element. Return null if target is not found.
   * 
   * @param target
   *          to be found
   * @return an Integer or null
   */
  public Integer findIndex(E target) {
    int index = 0;
    for (E item : iter) {
      if (item.equals(target)) {
        return index;
      }
      index++;
    }
    return null;
  }

  /**
   * Get the first element of this RubyEnumerable. Return null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E first() {
    Iterator<E> iterator = iter.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    } else {
      return null;
    }
  }

  /**
   * Get the first n element of this RubyEnumerable.
   * 
   * @param n
   *          number of elements
   * @return a RubyArray
   */
  public RubyArray<E> first(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to take negative size");
    }
    Iterator<E> it = iter.iterator();
    RubyArray<E> rubyArray = newRubyArray();
    for (int i = 0; i < n && it.hasNext(); i++) {
      rubyArray.add(it.next());
    }
    return rubyArray;
  }

  /**
   * Equivalent to collectConcat().
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> flatMap() {
    return collectConcat();
  }

  /**
   * Equivalent to collectConcat().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyArray
   */
  public <S> RubyArray<S> flatMap(TransformBlock<E, RubyArray<S>> block) {
    return collectConcat(block);
  }

  /**
   * Store elements which are matched by regex into a RubyArray.
   * 
   * @param regex
   *          regular expression
   * @return a RubyArray
   */
  public RubyArray<E> grep(String regex) {
    Pattern pattern = Pattern.compile(regex);
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      Matcher matcher = pattern.matcher(item.toString());
      if (matcher.find()) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  /**
   * Store elements which are matched by regex transformed by the block into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param regex
   *          regular expression
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<S> grep(String regex, TransformBlock<E, S> block) {
    Pattern pattern = Pattern.compile(regex);
    RubyArray<S> rubyArray = newRubyArray();
    for (E item : iter) {
      Matcher matcher = pattern.matcher(item.toString());
      if (matcher.find()) {
        rubyArray.add(block.yield(item));
      }
    }
    return rubyArray;
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> groupBy() {
    return newRubyEnumerator(iter);
  }

  /**
   * Put elements with the same result S returned by the block into a pair of S
   * and RubyArray of a RubyHash.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to group each element
   * @return a RubyHash
   */
  public <S> RubyHash<S, RubyArray<E>> groupBy(TransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    for (E item : iter) {
      S key = block.yield(item);
      multimap.put(key, item);
    }
    RubyHash<S, RubyArray<E>> map = newRubyHash();
    for (S key : multimap.keySet()) {
      map.put(key, newRubyArray(multimap.get(key)));
    }
    return map;
  }

  /**
   * Check if target element is included.
   * 
   * @param target
   *          to be searched
   * @return true if target is found,false otherwise
   */
  public boolean includeʔ(E target) {
    for (E item : iter) {
      if (item.equals(target)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Assign the first element as the initial value. Reduce each elements with
   * block, then assign the result back to initial value and so on.
   * 
   * @param block
   *          to reduce each element
   * @return an element
   */
  public E inject(ReduceBlock<E> block) {
    E result = null;
    int i = 0;
    for (E item : iter) {
      if (i == 0) {
        result = item;
      } else {
        result = block.yield(result, item);
      }
      i++;
    }
    return result;
  }

  /**
   * Reduce each elements with block, then assign the result back to initial
   * value and so on.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param block
   *          to reduce each element
   * @return an element S
   */
  public <S> S inject(S init, WithInitBlock<E, S> block) {
    for (E item : iter) {
      init = block.yield(init, item);
    }
    return init;
  }

  /**
   * Reduce each elements with initial value by a method of S, then assign the
   * result back to initial value and so on.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param methodName
   *          method used to reduce elements
   * @return an element S
   */
  @SuppressWarnings("unchecked")
  public <S> S inject(S init, String methodName) {
    S result = init;
    Iterator<E> iterator = iter.iterator();
    while (iterator.hasNext()) {
      E curr = iterator.next();
      try {
        Method[] methods = result.getClass().getDeclaredMethods();
        for (Method method : methods) {
          if (method.getName().equals(methodName)) {
            result = (S) method.invoke(result, curr);
          }
        }
      } catch (SecurityException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
            null, ex);
      } catch (IllegalArgumentException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
            null, ex);
      } catch (IllegalAccessException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
            null, ex);
      } catch (InvocationTargetException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
            null, ex);
      }
    }
    return result;
  }

  /**
   * Assign the first element as the initial value. Reduce each elements with
   * initial value by a method of S, then assign the result back to initial
   * value and so on.
   * 
   * @param methodName
   *          method used to reduce elements
   * @return an element
   */
  @SuppressWarnings("unchecked")
  public E inject(String methodName) {
    E result = null;
    Iterator<E> iterator = iter.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      if (i == 0) {
        result = iterator.next();
      } else {
        E curr = iterator.next();
        try {
          Method[] methods = result.getClass().getDeclaredMethods();
          for (Method method : methods) {
            if (method.getName().equals(methodName)) {
              result = (E) method.invoke(result, curr);
            }
          }
        } catch (SecurityException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
              null, ex);
        } catch (IllegalArgumentException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
              null, ex);
        } catch (IllegalAccessException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
              null, ex);
        } catch (InvocationTargetException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
              null, ex);
        }
      }
      i++;
    }
    return result;
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> map() {
    return collect();
  }

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<S> map(TransformBlock<E, S> block) {
    return collect(block);
  }

  /**
   * Find the max element of this RubyEnumerable. Return null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E max() {
    return sort().last();
  }

  /**
   * Find the max element induced by the Comparator of this RubyEnumerable.
   * Return null if this RubyEnumerable is empty.
   * 
   * @param comp
   *          a Comparator
   * @return an element or null
   */
  public E max(Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.max(list, comp);
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> maxBy() {
    return newRubyEnumerator(iter);
  }

  /**
   * Find the element which is the max element induced by the Comparator
   * transformed by the block of this RubyEnumerable. Return null if this
   * RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E maxBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  /**
   * Find the element which is the max element transformed by the block of this
   * RubyEnumerable. Return null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E maxBy(TransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = newRubyEnumerable(dst).max();
    return src.get(dst.indexOf(maxDst));
  }

  /**
   * Equivalent to includeʔ().
   * 
   * @param target
   *          to be found
   * @return an element or null
   */
  public boolean memberʔ(E target) {
    return includeʔ(target);
  }

  /**
   * Find the min element of this RubyEnumerable. Return null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E min() {
    return sort().first();
  }

  /**
   * Find the min element induced by the Comparator of this RubyEnumerable.
   * Return null if this RubyEnumerable is empty.
   * 
   * @param comp
   *          a Comparator
   * @return an element or null
   */
  public E min(Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.min(list, comp);
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> minBy() {
    return newRubyEnumerator(iter);
  }

  /**
   * Find the element which is the min element induced by the Comparator
   * transformed by the block of this RubyEnumerable. Return null if this
   * RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E minBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  /**
   * Find the element which is the min element transformed by the block of this
   * RubyEnumerable. Return null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E minBy(TransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = newRubyEnumerable(dst).min();
    return src.get(dst.indexOf(minDst));
  }

  /**
   * Find the min and max elements of this RubyEnumerable and store them into a
   * RubyArray.
   * 
   * @return a RubyArray
   */
  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax() {
    RubyArray<E> rubyArray = sort();
    return newRubyArray(rubyArray.first(), rubyArray.last());
  }

  /**
   * Find the min and max elements induced by the Comparator of this
   * RubyEnumerable and store them into a RubyArray.
   * 
   * @param comp
   *          a Comparator
   * @return a RubyArray
   */
  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    return newRubyArray(Collections.min(rubyArray, comp),
        Collections.max(rubyArray, comp));
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> minmaxBy() {
    return newRubyEnumerator(iter);
  }

  /**
   * Find elements which are the min and max elements induced by the Comparator
   * transformed by the block of this RubyEnumerable and store them into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return newRubyArray(src.get(dst.indexOf(minDst)),
        src.get(dst.indexOf(maxDst)));
  }

  /**
   * Find elements which is the min and max elements transformed by the block of
   * this RubyEnumerable and store them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(TransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = newRubyEnumerable(dst).min();
    S maxDst = newRubyEnumerable(dst).max();
    return newRubyArray(src.get(dst.indexOf(minDst)),
        src.get(dst.indexOf(maxDst)));
  }

  /**
   * Check if this RubyEnumerable contains only null objects.
   * 
   * @return true if all elements are null, false otherwise
   */
  public boolean noneʔ() {
    for (E item : iter) {
      if (item != null) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if this RubyEnumerable contains only elements which get false
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if all results of block are false, false otherwise
   */
  public boolean noneʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Check if this RubyEnumerable contains only one element beside null objects.
   * 
   * @return true if only one element and nulls are found, false otherwise
   */
  public boolean oneʔ() {
    int count = 0;
    for (E item : iter) {
      if (item != null) {
        count++;
        if (count > 1) {
          return false;
        }
      }
    }
    return count == 1;
  }

  /**
   * Check if this RubyEnumerable contains only one element which get true
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if only one result of block is true, false otherwise
   */
  public boolean oneʔ(BooleanBlock<E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        count++;
        if (count > 1) {
          return false;
        }
      }
    }
    return count == 1;
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> partition() {
    return newRubyEnumerator(iter);
  }

  /**
   * Divide elements into 2 groups by the given block.
   * 
   * @param block
   *          to part elements
   * @return a RubyArray of 2 RubyArrays
   */
  @SuppressWarnings("unchecked")
  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block) {
    RubyArray<E> trueList = newRubyArray();
    RubyArray<E> falseList = newRubyArray();
    for (E item : iter) {
      if (block.yield(item)) {
        trueList.add(item);
      } else {
        falseList.add(item);
      }
    }
    return newRubyArray(trueList, falseList);
  }

  /**
   * Equivalent to inject().
   * 
   * @param block
   *          to reduce each element
   * @return an element
   */
  public E reduce(ReduceBlock<E> block) {
    return inject(block);
  }

  /**
   * Equivalent to inject().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param block
   *          to reduce each element
   * @return an element S
   */
  public <S> S reduce(S init, WithInitBlock<E, S> block) {
    return inject(init, block);
  }

  /**
   * Equivalent to inject().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param methodName
   *          method used to reduce elements
   * @return an element S
   */
  public <S> S reduce(S init, String methodName) {
    return inject(init, methodName);
  }

  /**
   * Equivalent to inject().
   * 
   * @param methodName
   *          method used to reduce elements
   * @return an element
   */
  public E reduce(String methodName) {
    return inject(methodName);
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> reject() {
    return newRubyEnumerator(iter);
  }

  /**
   * Delete elements which get true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> reject(BooleanBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      if (!(block.yield(item))) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  /**
   * Return a reversed RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> reverseEach() {
    return newRubyEnumerator(Lists.reverse(newArrayList(iter)));
  }

  /**
   * Iterate each element reversed by given block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyEnumerable
   */
  public RubyEnumerable<E> reverseEach(Block<E> block) {
    List<E> list = newArrayList(iter);
    for (E item : reverse(list)) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> select() {
    return findAll();
  }

  /**
   * Equivalent to findAll().
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> select(BooleanBlock<E> block) {
    return findAll(block);
  }

  /**
   * Group elements into RubyArrays and the first element of each RubyArray
   * should get true returned by the block.
   * 
   * @param block
   *          to check where to do slice
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock<E> block) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(iter, block));
  }

  /**
   * Group elements into RubyArrays and the first element of each RubyArray
   * should be matched by the regex.
   * 
   * @param regex
   *          to check where to do slice
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(iter, regex));
  }

  /**
   * Sort elements of this RubyEnumerable into a RubyArray.
   * 
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           when any 2 elements are not comparable
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public RubyArray<E> sort() {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() <= 1) {
      return rubyArray;
    }
    E sample = rubyArray.first();
    if (sample instanceof Comparable) {
      Collections.sort(rubyArray, new Comparator() {

        @Override
        public int compare(Object arg0, Object arg1) {
          return ((Comparable) arg0).compareTo(arg1);
        }

      });
      return rubyArray;
    } else {
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + rubyArray.get(0).getClass() + " with "
          + rubyArray.get(1).getClass() + " failed");
    }
  }

  /**
   * Sort elements of this RubyEnumerable by given Comparator into a RubyArray.
   * 
   * @param comp
   *          a Comparator
   * @return a RubyArray
   */
  public RubyArray<E> sort(Comparator<E> comp) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() <= 1) {
      return rubyArray;
    }
    Collections.sort(rubyArray, comp);
    return rubyArray;
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> sortBy() {
    return newRubyEnumerator(iter);
  }

  /**
   * Sort elements of this RubyEnumerable by the ordering of elements
   * transformed by the block induced by the Comparator into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      multimap.put(block.yield(item), item);
    }
    List<S> keys = newArrayList(multimap.keySet());
    Collections.sort(keys, comp);
    for (S key : keys) {
      Collection<E> coll = multimap.get(key);
      Iterator<E> iterator = coll.iterator();
      while (iterator.hasNext()) {
        sortedList.add(iterator.next());
      }
    }
    return sortedList;
  }

  /**
   * Sort elements of this RubyEnumerable by the ordering of elements
   * transformed by the block into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> sortBy(TransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      multimap.put(block.yield(item), item);
    }
    List<S> keys = newArrayList(multimap.keySet());
    keys = newRubyEnumerable(keys).sort();
    for (S key : keys) {
      Collection<E> coll = multimap.get(key);
      Iterator<E> iterator = coll.iterator();
      while (iterator.hasNext()) {
        sortedList.add(iterator.next());
      }
    }
    return sortedList;
  }

  /**
   * Store the first n elements into a RubyArray.
   * 
   * @param n
   *          number of elements
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> take(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to take negative size");
    }
    RubyArray<E> rubyArray = newRubyArray();
    int i = 0;
    for (E item : iter) {
      if (i < n) {
        rubyArray.add(item);
      } else {
        return rubyArray;
      }
      i++;
    }
    return rubyArray;
  }

  /**
   * Return a RubyEnumerator which contains the first element of this
   * RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

  /**
   * Store element into a RubyArray from beginning until the result returned by
   * the block is false.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> takeWhile(BooleanBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      if (block.yield(item)) {
        rubyArray.add(item);
      } else {
        return rubyArray;
      }
    }
    return rubyArray;
  }

  /**
   * Convert this RubyEnumerable to a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> toA() {
    return newRubyArray(iter);
  }

  /**
   * Group elements which get the same indices among all other Lists into
   * RubyArrays.
   * 
   * @param others
   *          Lists
   * @return a RubyArray of RubyArrays
   */
  public RubyArray<RubyArray<E>> zip(List<E>... others) {
    return zip(Arrays.asList(others));
  }

  /**
   * Group elements which get the same indices among all other Lists into
   * RubyArrays.
   * 
   * @param others
   *          List of Lists
   * @return a RubyArray of RubyArrays
   */
  public RubyArray<RubyArray<E>> zip(List<? extends List<E>> others) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    RubyArray<RubyArray<E>> zippedRubyArray = newRubyArray();
    for (int i = 0; i < rubyArray.size(); i++) {
      RubyArray<E> zip = newRubyArray();
      zip.add(rubyArray.at(i));
      for (int j = 0; j < others.size(); j++) {
        List<E> z = others.get(j);
        if (i < z.size()) {
          zip.add(z.get(i));
        } else {
          zip.push(null);
        }
      }
      zippedRubyArray.add(zip);
    }
    return zippedRubyArray;
  }

  /**
   * Group elements which get the same indices among all other Lists into
   * RubyArrays and yield them to the block.
   * 
   * @param others
   *          List of Lists
   * @param block
   *          to yield zipped elements
   */
  public void zip(List<? extends List<E>> others, Block<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> zippedRubyArray = zip(others);
    for (RubyArray<E> item : zippedRubyArray) {
      block.yield(item);
    }
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }

}
