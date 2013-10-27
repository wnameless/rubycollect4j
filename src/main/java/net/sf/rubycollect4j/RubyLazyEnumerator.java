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
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import net.sf.rubycollect4j.iter.DropIterable;
import net.sf.rubycollect4j.iter.DropWhileIterable;
import net.sf.rubycollect4j.iter.EachConsIterable;
import net.sf.rubycollect4j.iter.EachSliceIterable;
import net.sf.rubycollect4j.iter.EachWithIndexIterable;
import net.sf.rubycollect4j.iter.EachWithObjectIterable;
import net.sf.rubycollect4j.iter.FindAllIterable;
import net.sf.rubycollect4j.iter.FlattenIterable;
import net.sf.rubycollect4j.iter.GrepIterable;
import net.sf.rubycollect4j.iter.RejectIterable;
import net.sf.rubycollect4j.iter.ReverseEachIterable;
import net.sf.rubycollect4j.iter.SliceBeforeIterable;
import net.sf.rubycollect4j.iter.TakeIterable;
import net.sf.rubycollect4j.iter.TakeWhileIterable;
import net.sf.rubycollect4j.iter.TransformByMethodIterable;
import net.sf.rubycollect4j.iter.TransformIterable;
import net.sf.rubycollect4j.iter.ZipIterable;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * RubyLazyEnumerator implements most of the methods refer to the
 * Enumerator::Lazy of Ruby language. RubyLazyEnumerator is both Iterable and
 * Iterator and it's also a peeking iterator.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RubyLazyEnumerator<E> implements RubyEnumerableBase<E>,
    Iterable<E>, Iterator<E> {

  private final Iterable<E> iter;
  private PeekingIterator<E> pIterator;

  /**
   * Creates a RubyLazyEnumerator by given Iterable.
   * 
   * @param iterable
   *          an Iterable
   * @throws NullPointerException
   *           if iterable is null
   */
  public RubyLazyEnumerator(Iterable<E> iterable) {
    if (iterable == null)
      throw new NullPointerException();

    iter = iterable;
  }

  @Override
  public boolean allʔ() {
    for (E item : iter) {
      if (item == null || Boolean.FALSE.equals(item))
        return false;
    }
    return true;
  }

  @Override
  public boolean allʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item) == false)
        return false;
    }
    return true;
  }

  @Override
  public boolean anyʔ() {
    for (E item : iter) {
      if (item != null && !Boolean.FALSE.equals(item))
        return true;
    }
    return false;
  }

  @Override
  public boolean anyʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item))
        return true;
    }
    return false;
  }

  /**
   * Chunks elements to entries. Keys of entries are the result returned by the
   * block. Values of entries are RubyArrays of elements which get the same
   * result returned by the block and aside to each other. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to chunk elements
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<Entry<S, RubyArray<E>>> chunk(
      TransformBlock<E, S> block) {
    return new RubyLazyEnumerator<Entry<S, RubyArray<E>>>(
        new ChunkIterable<E, S>(iter, block));
  }

  /**
   * Transforms each element by the block. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<S> collect(TransformBlock<E, S> block) {
    return new RubyLazyEnumerator<S>(new TransformIterable<E, S>(iter, block));
  }

  /**
   * Transforms each element by given method. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<S> collect(String methodName, Object... args) {
    return newRubyLazyEnumerator(new TransformByMethodIterable<E, S>(iter,
        methodName, args));
  }

  /**
   * Turns each element into a RubyArray and then flattens it. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<S> collectConcat(
      TransformBlock<E, ? extends List<S>> block) {
    return new RubyLazyEnumerator<S>(new FlattenIterable<E, S>(iter, block));
  }

  @Override
  public int count() {
    int count = 0;
    for (@SuppressWarnings("unused")
    E item : iter) {
      count++;
    }
    return count;
  }

  @Override
  public int count(BooleanBlock<E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item))
        count++;
    }
    return count;
  }

  /**
   * Generates a sequence from first element to last element and so on
   * infinitely. Lazy loading by a RubyLazyEnumerator.
   * 
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> cycle() {
    return new RubyLazyEnumerator<E>(new CycleIterable<E>(iter));
  }

  /**
   * Generates a sequence from first element to last element, repeats n times.
   * Lazy loading by a RubyLazyEnumerator.
   * 
   * @param n
   *          times to repeat
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> cycle(int n) {
    return new RubyLazyEnumerator<E>(new CycleIterable<E>(iter, n));
  }

  @Override
  public void cycle(int n, Block<E> block) {
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  @Override
  public void cycle(Block<E> block) {
    while (true) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> detect() {
    return this;
  }

  @Override
  public E detect(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item))
        return item;
    }
    return null;
  }

  /**
   * Drops the first n elements. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param n
   *          number of elements to drop
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> drop(int n) {
    return new RubyLazyEnumerator<E>(new DropIterable<E>(iter, n));
  }

  /**
   * Drops the first n elements until a element gets false returned by the
   * block. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param block
   *          to define which elements to be dropped
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> dropWhile(BooleanBlock<E> block) {
    return new RubyLazyEnumerator<E>(new DropWhileIterable<E>(iter, block));
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> each() {
    return this;
  }

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   */
  public void each(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
  }

  /**
   * Iterates each element and puts the element with n - 1 consecutive elements
   * into a RubyArray. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param n
   *          number of consecutive elements
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<RubyArray<E>> eachCons(int n) {
    return new RubyLazyEnumerator<RubyArray<E>>(
        new EachConsIterable<E>(iter, n));
  }

  @Override
  public void eachCons(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> cons : eachCons(n)) {
      block.yield(cons);
    }
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> eachEntry() {
    return this;
  }

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> eachEntry(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Slices elements into RubyArrays with length n. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @param n
   *          size of each slice
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<RubyArray<E>> eachSlice(int n) {
    return new RubyLazyEnumerator<RubyArray<E>>(new EachSliceIterable<E>(iter,
        n));
  }

  @Override
  public void eachSlice(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> ra : new EachSliceIterable<E>(iter, n)) {
      block.yield(ra);
    }
  }

  /**
   * Iterates elements with their indices by Entry. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return new RubyLazyEnumerator<Entry<E, Integer>>(
        new EachWithIndexIterable<E>(iter));
  }

  /**
   * Iterates elements with their indices by Entry and yields them to the block.
   * 
   * @param block
   *          to yield each Entry
   * @return a RubyArray
   */
  public RubyArray<E> eachWithIndex(WithIndexBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    int i = 0;
    for (E item : iter) {
      block.yield(item, i);
      rubyArray.add(item);
      i++;
    }
    return rubyArray;
  }

  /**
   * Iterates elements with the object S. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param o
   *          any Object
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<Entry<E, S>> eachWithObject(S o) {
    return new RubyLazyEnumerator<Entry<E, S>>(
        new EachWithObjectIterable<E, S>(iter, o));
  }

  @Override
  public <S> S eachWithObject(S o, WithObjectBlock<E, S> block) {
    for (E item : iter) {
      block.yield(item, o);
    }
    return o;
  }

  @Override
  public RubyArray<E> entries() {
    return newRubyArray(iter);
  }

  /**
   * Equivalent to detect().
   * 
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> find() {
    return detect();
  }

  @Override
  public E find(BooleanBlock<E> block) {
    return detect(block);
  }

  /**
   * Finds all elements which are true returned by the block. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @param block
   *          to filter elements
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> findAll(BooleanBlock<E> block) {
    return new RubyLazyEnumerator<E>(new FindAllIterable<E>(iter, block));
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> findIndex() {
    return this;
  }

  @Override
  public Integer findIndex(BooleanBlock<E> block) {
    int index = 0;
    for (E item : iter) {
      if (block.yield(item))
        return index;

      index++;
    }
    return null;
  }

  @Override
  public Integer findIndex(E target) {
    int index = 0;
    for (E item : iter) {
      if (item.equals(target))
        return index;

      index++;
    }
    return null;
  }

  @Override
  public E first() {
    Iterator<E> iterator = iter.iterator();
    if (iterator.hasNext())
      return iterator.next();
    else
      return null;
  }

  @Override
  public RubyArray<E> first(int n) {
    if (n < 0)
      throw new IllegalArgumentException(
          "ArgumentError: attempt to take negative size");

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
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<S> flatMap(
      TransformBlock<E, ? extends List<S>> block) {
    return collectConcat(block);
  }

  /**
   * Finds all elements which are matched by the regular expression. Lazy
   * loading by a RubyLazyEnumerator.
   * 
   * @param regex
   *          regular expression
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> grep(String regex) {
    return new RubyLazyEnumerator<E>(new GrepIterable<E>(iter, regex));
  }

  /**
   * Finds all elements which are matched by the regular expression and
   * transforms them. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param regex
   *          regular expression
   * @param block
   *          to transform elements
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<S>
      grep(String regex, TransformBlock<E, S> block) {
    return new RubyLazyEnumerator<S>(new TransformIterable<E, S>(
        new GrepIterable<E>(iter, regex), block));
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> groupBy() {
    return this;
  }

  @Override
  public <S> RubyHash<S, RubyArray<E>> groupBy(TransformBlock<E, S> block) {
    Map<S, List<E>> map = new LinkedHashMap<S, List<E>>();
    for (E item : iter) {
      S key = block.yield(item);
      if (!map.containsKey(key))
        map.put(key, new ArrayList<E>());

      map.get(key).add(item);
    }
    RubyHash<S, RubyArray<E>> rubyHash = newRubyHash();
    for (S key : map.keySet()) {
      rubyHash.put(key, newRubyArray(map.get(key)));
    }
    return rubyHash;
  }

  @Override
  public boolean includeʔ(E target) {
    for (E item : iter) {
      if (item.equals(target))
        return true;
    }
    return false;
  }

  @Override
  public E inject(ReduceBlock<E> block) {
    E result = null;
    int i = 0;
    for (E item : iter) {
      if (i == 0)
        result = item;
      else
        result = block.yield(result, item);
      i++;
    }
    return result;
  }

  @Override
  public <S> S inject(S init, WithInitBlock<E, S> block) {
    for (E item : iter) {
      init = block.yield(init, item);
    }
    return init;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> S inject(S init, String methodName) {
    S result = init;
    Iterator<E> iterator = iter.iterator();
    while (iterator.hasNext()) {
      E curr = iterator.next();
      result = (S) RubyObject.send(result, methodName, curr);
    }
    return result;
  }

  @Override
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
        result = (E) RubyObject.send(result, methodName, curr);
      }
      i++;
    }
    return result;
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> lazy() {
    return this;
  }

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<S> map(TransformBlock<E, S> block) {
    return collect(block);
  }

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyLazyEnumerator
   */
  public <S> RubyLazyEnumerator<S> map(String methodName, Object... args) {
    return collect(methodName, args);
  }

  @Override
  public E max() {
    return sort().last();
  }

  @Override
  public E max(Comparator<? super E> comp) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
    if (list.isEmpty())
      return null;

    return Collections.max(list, comp);
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> maxBy() {
    return this;
  }

  @Override
  public <S> E maxBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  @Override
  public <S> E maxBy(TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S maxDst = newRubyEnumerator(dst).max();
    return src.get(dst.indexOf(maxDst));
  }

  @Override
  public boolean memberʔ(E target) {
    return includeʔ(target);
  }

  @Override
  public E min() {
    return sort().first();
  }

  @Override
  public E min(Comparator<? super E> comp) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
    if (list.isEmpty())
      return null;

    return Collections.min(list, comp);
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> minBy() {
    return this;
  }

  @Override
  public <S> E minBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  @Override
  public <S> E minBy(TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S minDst = newRubyEnumerator(dst).min();
    return src.get(dst.indexOf(minDst));
  }

  @Override
  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax() {
    RubyArray<E> rubyArray = sort();
    return newRubyArray(rubyArray.first(), rubyArray.last());
  }

  @Override
  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.isEmpty())
      return newRubyArray(null, null);

    return newRubyArray(Collections.min(rubyArray, comp),
        Collections.max(rubyArray, comp));
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> minmaxBy() {
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return newRubyArray(null, null);

    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return newRubyArray(src.get(dst.indexOf(minDst)),
        src.get(dst.indexOf(maxDst)));
  }

  @Override
  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(TransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return newRubyArray(null, null);

    S minDst = newRubyEnumerator(dst).min();
    S maxDst = newRubyEnumerator(dst).max();
    return newRubyArray(src.get(dst.indexOf(minDst)),
        src.get(dst.indexOf(maxDst)));
  }

  @Override
  public boolean noneʔ() {
    for (E item : iter) {
      if (item != null)
        return false;
    }
    return true;
  }

  @Override
  public boolean noneʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item))
        return false;
    }
    return true;
  }

  @Override
  public boolean oneʔ() {
    int count = 0;
    for (E item : iter) {
      if (item != null) {
        count++;
        if (count > 1)
          return false;
      }
    }
    return count == 1;
  }

  @Override
  public boolean oneʔ(BooleanBlock<E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        count++;
        if (count > 1)
          return false;
      }
    }
    return count == 1;
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> partition() {
    return this;
  }

  @Override
  @SuppressWarnings("unchecked")
  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block) {
    RubyArray<E> trueList = newRubyArray();
    RubyArray<E> falseList = newRubyArray();
    for (E item : iter) {
      if (block.yield(item))
        trueList.add(item);
      else
        falseList.add(item);
    }
    return newRubyArray(trueList, falseList);
  }

  @Override
  public E reduce(ReduceBlock<E> block) {
    return inject(block);
  }

  @Override
  public <S> S reduce(S init, WithInitBlock<E, S> block) {
    return inject(init, block);
  }

  @Override
  public <S> S reduce(S init, String methodName) {
    return inject(init, methodName);
  }

  @Override
  public E reduce(String methodName) {
    return inject(methodName);
  }

  /**
   * Filters all elements which are true returned by the block. Lazy loading by
   * a RubyLazyEnumerator.
   * 
   * @param block
   *          to filter elements
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> reject(BooleanBlock<E> block) {
    return new RubyLazyEnumerator<E>(new RejectIterable<E>(iter, block));
  }

  /**
   * Returns a reversed RubyLazyEnumerator. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> reverseEach() {
    return new RubyLazyEnumerator<E>(new ReverseEachIterable<E>(iter));
  }

  /**
   * Iterates each element reversely by given block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> reverseEach(Block<E> block) {
    for (E item : reverseEach()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Equivalent to findAll().
   * 
   * @param block
   *          to filter elements
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> select(BooleanBlock<E> block) {
    return findAll(block);
  }

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should get true returned by the block. Lazy loading by a
   * RubyLazyEnumerator.
   * 
   * @param block
   *          to check where to do slice
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock<E> block) {
    return new RubyLazyEnumerator<RubyArray<E>>(new SliceBeforeIterable<E>(
        iter, block));
  }

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should be matched by the regex. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param regex
   *          to check where to do slice
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return new RubyLazyEnumerator<RubyArray<E>>(new SliceBeforeIterable<E>(
        iter, Pattern.compile(regex)));
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public RubyArray<E> sort() {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() <= 1)
      return rubyArray;

    try {
      Collections.sort(rubyArray, new Comparator() {

        @Override
        public int compare(Object arg0, Object arg1) {
          return ((Comparable) arg0).compareTo(arg1);
        }

      });
      return rubyArray;
    } catch (Exception e) {
      if (rubyArray.uniq().count() == 1)
        return rubyArray;

      Iterator<E> iter = rubyArray.iterator();
      E sample = iter.next();
      E error = null;
      while (iter.hasNext()) {
        error = iter.next();
        try {
          ((Comparable) sample).compareTo(error);
        } catch (Exception ex) {
          break;
        }
      }
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + (sample == null ? "null" : sample.getClass().getName()) + " with "
          + (error == null ? "null" : error.getClass().getName()) + " failed");
    }
  }

  // @Override
  // public RubyArray<E> sort(Comparator<? super E> comp) {
  // RubyArray<E> rubyArray = newRubyArray(iter);
  // if (rubyArray.size() <= 1)
  // return rubyArray;
  //
  // Collections.sort(rubyArray, comp);
  // return rubyArray;
  // }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> sortBy() {
    return this;
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    Map<S, RubyArray<E>> map = new LinkedHashMap<S, RubyArray<E>>();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      S key = block.yield(item);
      if (!map.containsKey(key))
        map.put(key, new RubyArray<E>());

      map.get(key).add(item);
    }
    List<S> keys = newRubyArray(map.keySet()).sortǃ(comp);
    for (S key : keys) {
      sortedList.addAll(map.get(key).sortǃ());
    }
    return sortedList;
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, TransformBlock<E, S> block) {
    Map<S, RubyArray<E>> map = new LinkedHashMap<S, RubyArray<E>>();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      S key = block.yield(item);
      if (!map.containsKey(key))
        map.put(key, new RubyArray<E>());

      map.get(key).add(item);
    }
    List<S> keys = newRubyArray(map.keySet()).sortǃ(comp2);
    for (S key : keys) {
      sortedList.addAll(map.get(key).sortǃ(comp1));
    }
    return sortedList;
  }

  @Override
  public <S> RubyArray<E> sortBy(TransformBlock<E, S> block) {
    Map<S, RubyArray<E>> map = new LinkedHashMap<S, RubyArray<E>>();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      S key = block.yield(item);
      if (!map.containsKey(key))
        map.put(key, new RubyArray<E>());

      map.get(key).add(item);
    }
    List<S> keys = newRubyArray(map.keySet()).sortǃ();
    for (S key : keys) {
      sortedList.addAll(map.get(key).sortǃ());
    }
    return sortedList;
  }

  /**
   * Takes the first n elements. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param n
   *          number of elements
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> take(int n) {
    return new RubyLazyEnumerator<E>(new TakeIterable<E>(iter, n));
  }

  /**
   * Takes elements until a element gets false returned by the block. Lazy
   * loading by a RubyLazyEnumerator.
   * 
   * @param block
   *          to filter elements
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> takeWhile(BooleanBlock<E> block) {
    return new RubyLazyEnumerator<E>(new TakeWhileIterable<E>(iter, block));
  }

  @Override
  public RubyArray<E> toA() {
    return newRubyArray(iter);
  }

  /**
   * Groups elements which get the same indices among all other Iterables into
   * RubyArrays. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param others
   *          an array of Iterable
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<RubyArray<E>> zip(Iterable<E>... others) {
    return zip(Arrays.asList(others));
  }

  /**
   * Groups elements which get the same indices among all other Lists into
   * RubyArrays. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param others
   *          a List of Iterable
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<RubyArray<E>>
      zip(List<? extends Iterable<E>> others) {
    return new RubyLazyEnumerator<RubyArray<E>>(
        new ZipIterable<E>(iter, others));
  }

  @Override
  public void
      zip(List<? extends Iterable<E>> others, Block<RubyArray<E>> block) {
    RubyLazyEnumerator<RubyArray<E>> zippedRubyArray = zip(others);
    for (RubyArray<E> item : zippedRubyArray) {
      block.yield(item);
    }
  }

  /**
   * Resets the iterator of this RubyLazyEnumerator to the beginning.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> rewind() {
    if (pIterator == null)
      pIterator = new PeekingIterator<E>(iter.iterator());
    pIterator = new PeekingIterator<E>(iter.iterator());
    return this;
  }

  /**
   * Returns the next element without advancing the iteration.
   * 
   * @return an element
   */
  public E peek() {
    if (pIterator == null)
      pIterator = new PeekingIterator<E>(iter.iterator());
    return pIterator.peek();
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }

  @Override
  public boolean hasNext() {
    if (pIterator == null)
      pIterator = new PeekingIterator<E>(iter.iterator());
    return pIterator.hasNext();
  }

  @Override
  public E next() {
    if (pIterator == null)
      pIterator = new PeekingIterator<E>(iter.iterator());
    return pIterator.next();
  }

  @Override
  public void remove() {
    if (pIterator == null)
      pIterator = new PeekingIterator<E>(iter.iterator());
    pIterator.remove();
  }

  @Override
  public String toString() {
    return "RubyLazyEnumerator{" + iter + "}";
  }

}
