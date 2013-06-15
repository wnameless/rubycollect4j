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

import cleanzephyr.rebycollect4j.util.ComparableComparator;
import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.InjectBlock;
import cleanzephyr.rubycollect4j.block.InjectWithInitBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;
import cleanzephyr.rubycollect4j.block.ItemToRubyArrayBlock;
import cleanzephyr.rubycollect4j.block.ItemTransformBlock;
import cleanzephyr.rubycollect4j.block.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.block.ItemWithObjectBlock;
import cleanzephyr.rubycollect4j.block.ListBlock;
import cleanzephyr.rubycollect4j.iter.ChunkIterable;
import cleanzephyr.rubycollect4j.iter.CycleIterable;
import cleanzephyr.rubycollect4j.iter.EachConsIterable;
import cleanzephyr.rubycollect4j.iter.EachSliceIterable;
import cleanzephyr.rubycollect4j.iter.EachWithIndexIterable;
import cleanzephyr.rubycollect4j.iter.EachWithObjectIterable;
import cleanzephyr.rubycollect4j.iter.SliceBeforeIterable;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static cleanzephyr.rubycollect4j.RubyEnumerator.newRubyEnumerator;
import static cleanzephyr.rubycollect4j.RubyHash.newRubyHash;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;

/**
 * 
 * @author WMW
 * @param <E>
 */
public class RubyEnumerable<E> implements Iterable<E> {

  protected final Iterable<E> iter;

  public static <E> RubyEnumerable<E> newRubyEnumerable(Iterable<E> iter) {
    return new RubyEnumerable<E>(iter);
  }

  public static <E> RubyEnumerable<E> newRubyEnumerable(E... args) {
    return new RubyEnumerable<E>(args);
  }

  public RubyEnumerable(Iterable<E> iter) {
    this.iter = iter;
  }

  public RubyEnumerable(E... args) {
    this.iter = Arrays.asList(args);
  }

  public boolean allʔ() {
    boolean bool = true;
    for (E item : iter) {
      if (item == null) {
        return false;
      }
    }
    return bool;
  }

  public boolean allʔ(BooleanBlock<E> block) {
    boolean bool = true;
    for (E item : iter) {
      if (block.yield(item) == false) {
        return false;
      }
    }
    return bool;
  }

  public boolean anyʔ() {
    boolean bool = false;
    for (E item : iter) {
      if (item != null) {
        return true;
      }
    }
    return bool;
  }

  public boolean anyʔ(BooleanBlock<E> block) {
    boolean bool = false;
    for (E item : iter) {
      if (block.yield(item)) {
        return true;
      }
    }
    return bool;
  }

  public <K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(
      ItemTransformBlock<E, K> block) {
    return newRubyEnumerator(new ChunkIterable<E, K>(iter, block));
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> collect() {
    return newRubyEnumerator(iter);
  }

  public <S> RubyArray<S> collect(ItemTransformBlock<E, S> block) {
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

  public <S> RubyArray<S> collectConcat(ItemToRubyArrayBlock<E, S> block) {
    RubyArray<S> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.addAll(block.yield(item));
    }
    return rubyArray;
  }

  public int count() {
    int count = 0;
    for (@SuppressWarnings("unused")
    E item : iter) {
      count++;
    }
    return count;
  }

  public int count(BooleanBlock<E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        count++;
      }
    }
    return count;
  }

  public RubyEnumerator<E> cycle() {
    return newRubyEnumerator(Iterables.cycle(iter));
  }

  public RubyEnumerator<E> cycle(int n) {
    return newRubyEnumerator(new CycleIterable<E>(iter, n));
  }

  public void cycle(int n, ItemBlock<E> block) {
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  public void cycle(ItemBlock<E> block) {
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

  public E detect(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item)) {
        return item;
      }
    }
    return null;
  }

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

  public RubyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

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

  public RubyEnumerator<RubyArray<E>> eachCons(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    return newRubyEnumerator(new EachConsIterable<E>(iter, n));
  }

  public void eachCons(int n, ListBlock<E> block) {
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

  public RubyEnumerable<E> eachEntry(ItemBlock<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  public RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    return newRubyEnumerator(new EachSliceIterable<E>(iter, n));
  }

  public void eachSlice(int n, ListBlock<E> block) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    for (RubyArray<E> ra : new EachSliceIterable<E>(iter, n)) {
      block.yield(ra);
    }
  }

  public RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return newRubyEnumerator(new EachWithIndexIterable<E>(iter));
  }

  public RubyEnumerable<E> eachWithIndex(ItemWithIndexBlock<E> block) {
    int i = 0;
    for (E item : iter) {
      block.yield(item, i);
      i++;
    }
    return this;
  }

  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o) {
    return newRubyEnumerator(new EachWithObjectIterable<E, S>(iter, o));
  }

  public <S> S eachWithObject(S o, ItemWithObjectBlock<E, S> block) {
    for (E item : iter) {
      block.yield(item, o);
    }
    return o;
  }

  public RubyArray<E> entries() {
    return newRubyArray(iter);
  }

  public RubyEnumerator<E> find() {
    return detect();
  }

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

  public E first() {
    Iterator<E> iterator = iter.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    } else {
      return null;
    }
  }

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
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> flatMap() {
    return collectConcat();
  }

  public <S> RubyArray<S> flatMap(ItemToRubyArrayBlock<E, S> block) {
    return collectConcat(block);
  }

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

  public <S> RubyArray<S> grep(String regex, ItemTransformBlock<E, S> block) {
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

  public <K> RubyHash<K, RubyArray<E>> groupBy(ItemTransformBlock<E, K> block) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : iter) {
      K key = block.yield(item);
      multimap.put(key, item);
    }
    RubyHash<K, RubyArray<E>> map = newRubyHash();
    for (K key : multimap.keySet()) {
      map.put(key, newRubyArray(multimap.get(key)));
    }
    return map;
  }

  public boolean includeʔ(E target) {
    for (E item : iter) {
      if (item.equals(target)) {
        return true;
      }
    }
    return false;
  }

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

  public E inject(InjectBlock<E> block) {
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

  public <S> S inject(S init, InjectWithInitBlock<E, S> block) {
    for (E item : iter) {
      init = block.yield(init, item);
    }
    return init;
  }

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

  public <S> RubyArray<S> map(ItemTransformBlock<E, S> block) {
    return collect(block);
  }

  public E max() {
    return sort().last();
  }

  public E max(Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.max(list, comp);
  }

  public RubyEnumerator<E> maxBy() {
    return newRubyEnumerator(iter);
  }

  public <S> E
      maxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  public <S> E maxBy(ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = newRubyEnumerable(dst).max();
    return src.get(dst.indexOf(maxDst));
  }

  public boolean memberʔ(E target) {
    return includeʔ(target);
  }

  public E min() {
    return sort().first();
  }

  public E min(Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.min(list, comp);
  }

  public RubyEnumerator<E> minBy() {
    return newRubyEnumerator(iter);
  }

  public <S> E
      minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  public <S> E minBy(ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = newRubyEnumerable(dst).min();
    return src.get(dst.indexOf(minDst));
  }

  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax() {
    RubyArray<E> rubyArray = sort();
    return newRubyArray(rubyArray.first(), rubyArray.last());
  }

  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    return newRubyArray(Collections.min(rubyArray, comp),
        Collections.max(rubyArray, comp));
  }

  public RubyEnumerator<E> minmaxBy() {
    return newRubyEnumerator(iter);
  }

  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      ItemTransformBlock<E, S> block) {
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

  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block) {
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

  public boolean noneʔ() {
    boolean bool = true;
    for (E item : iter) {
      if (item != null) {
        return false;
      }
    }
    return bool;
  }

  public boolean noneʔ(BooleanBlock<E> block) {
    boolean bool = true;
    for (E item : iter) {
      if (block.yield(item)) {
        return false;
      }
    }
    return bool;
  }

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

  public <S> S reduce(S init, String methodName) {
    return inject(init, methodName);
  }

  public E reduce(InjectBlock<E> block) {
    return inject(block);
  }

  public <S> S reduce(S init, InjectWithInitBlock<E, S> block) {
    return inject(init, block);
  }

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

  public RubyArray<E> reject(BooleanBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      if (!(block.yield(item))) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public RubyEnumerator<E> reverseEach() {
    return newRubyEnumerator(Lists.reverse(newArrayList(iter)));
  }

  public RubyEnumerable<E> reverseEach(ItemBlock<E> block) {
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

  public RubyArray<E> select(BooleanBlock<E> block) {
    return findAll(block);
  }

  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock<E> block) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(iter, block));
  }

  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(iter, regex));
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public RubyArray<E> sort() {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() > 0) {
      E sample = rubyArray.first();
      if (sample instanceof Comparable) {
        Collections.sort(rubyArray, new ComparableComparator());
        return rubyArray;
      }
    }
    Object[] array = rubyArray.toArray();
    Arrays.sort(array);
    E[] elements = (E[]) array;
    return newRubyArray(elements);
  }

  /**
   * Return a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> sortBy() {
    return newRubyEnumerator(iter);
  }

  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      ItemTransformBlock<E, S> block) {
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

  public <S> RubyArray<E> sortBy(ItemTransformBlock<E, S> block) {
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

  public RubyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

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

  public RubyArray<E> toA() {
    return newRubyArray(iter);
  }

  public RubyArray<RubyArray<E>> zip(List<E>... others) {
    return zip(Arrays.asList(others));
  }

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

  public void
      zip(List<? extends List<E>> others, ItemBlock<RubyArray<E>> block) {
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
