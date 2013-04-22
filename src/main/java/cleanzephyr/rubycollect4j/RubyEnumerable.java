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

import cleanzephyr.rubycollect4j.blocks.BooleanBlock;
import cleanzephyr.rubycollect4j.blocks.InjectBlock;
import cleanzephyr.rubycollect4j.blocks.InjectWithInitBlock;
import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import cleanzephyr.rubycollect4j.blocks.ItemFromListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemToListBlock;
import cleanzephyr.rubycollect4j.blocks.ItemTransformBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.blocks.ItemWithObjectBlock;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;
import com.google.common.collect.Multimap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap.SimpleEntry;
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

public final class RubyEnumerable {

  /**
   * To determinate if all elements are not null.
   *
   * @param <E> element
   * @param iter iterator of element <E>
   * @return true if all elements are not null, otherwise false
   */
  public static <E> boolean allʔ(Iterable<E> iter) {
    boolean bool = true;
    for (E item : iter) {
      if (item == null) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean allʔ(Iterable<E> iter, BooleanBlock<E> block) {
    boolean bool = true;
    for (E item : iter) {
      if (block.yield(item) == false) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean anyʔ(Iterable<E> iter) {
    boolean bool = false;
    for (E item : iter) {
      if (item != null) {
        bool = true;
      }
    }
    return bool;
  }

  public static <E> boolean anyʔ(Iterable<E> iter, BooleanBlock<E> block) {
    boolean bool = false;
    for (E item : iter) {
      if (block.yield(item)) {
        bool = true;
      }
    }
    return bool;
  }

  public static <E, K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(Iterable<E> iter, ItemTransformBlock<E, K> block) {
    List<Entry<K, RubyArray<E>>> list = newArrayList();
    K prev = null;
    List<E> chunk = new RubyArrayList();
    int count = 0;
    for (E item : iter) {
      K key = block.yield(item);
      if (key.equals(prev)) {
        chunk.add(item);
      } else {
        if (count != 0) {
          list.add(new SimpleEntry(prev, chunk));
        }
        prev = key;
        chunk = newArrayList();
        chunk.add(item);
      }
      count++;
    }
    if (!chunk.isEmpty()) {
      list.add(new SimpleEntry(prev, chunk));
    }
    return new RubyEnumerator<>(list);
  }

  public static <E, S> RubyArray<S> collect(Iterable<E> iter, ItemTransformBlock<E, S> block) {
    RubyArray<S> rubyArray = new RubyArrayList();
    for (E item : iter) {
      rubyArray.add(block.yield(item));
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<E> collect(Iterable<E> iter) {
    return new RubyEnumerator<>(iter);
  }

  public static <E, S> RubyArray<S> collectConcat(Iterable<E> iter, ItemToListBlock<E, S> block) {
    RubyArray<S> rubyArray = new RubyArrayList();
    for (E item : iter) {
      rubyArray.addAll(block.yield(item));
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<E> collectConcat(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> int count(Iterable<E> iter) {
    int count = 0;
    for (E item : iter) {
      count++;
    }
    return count;
  }

  public static <E> int count(Iterable<E> iter, BooleanBlock block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        count++;
      }
    }
    return count;
  }

  public static <E> void cycle(Iterable<E> iter, ItemBlock block) {
    while (true) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  public static <E> RubyEnumerator<E> cycle(Iterable<E> iter) {
    return new RubyEnumerator(Iterables.cycle(iter));
  }

  public static <E> void cycle(Iterable<E> iter, int n, ItemBlock block) {
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  public static <E> RubyEnumerator<E> cycle(Iterable<E> iter, int n) {
    List<E> items = newArrayList();
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        items.add(item);
      }
    }
    return new RubyEnumerator(items);
  }

  public static <E> E detect(Iterable<E> iter, BooleanBlock block) {
    for (E item : iter) {
      if (block.yield(item)) {
        return item;
      }
    }
    return null;
  }

  public static <E> RubyEnumerator<E> detect(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> RubyArray<E> drop(Iterable<E> iter, int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to drop negative size");
    }
    RubyArray<E> rubyArray = new RubyArrayList();
    int i = 0;
    for (E item : iter) {
      if (i >= n) {
        rubyArray.add(item);
      }
      i++;
    }
    return rubyArray;
  }

  public static <E> RubyArray<E> dropWhile(Iterable<E> iter, BooleanBlock block) {
    RubyArray<E> rubyArray = new RubyArrayList();
    boolean cutPoint = false;
    for (E item : iter) {
      if (block.yield(item) || cutPoint) {
        cutPoint = true;
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<E> dropWhile(Iterable<E> iter) {
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return new RubyEnumerator(rubyArray);
  }

  public static <E> void eachCons(Iterable<E> iter, int n, ItemFromListBlock<E> block) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    List<E> list = newArrayList(iter);
    for (int i = 0; i < list.size(); i++) {
      if (i + n <= list.size()) {
        List<E> cons = newArrayList();
        for (int j = i; j < i + n; j++) {
          cons.add(list.get(j));
        }
        block.yield(cons);
      }
    }
  }

  public static <E> RubyEnumerator<RubyArray<E>> eachCons(Iterable<E> iter, int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    RubyArray<RubyArray<E>> allCons = new RubyArrayList();
    List<E> list = newArrayList(iter);
    for (int i = 0; i < list.size(); i++) {
      if (i + n <= list.size()) {
        RubyArray<E> cons = new RubyArrayList();
        for (int j = i; j < i + n; j++) {
          cons.add(list.get(j));
        }
        allCons.add(cons);
      }
    }
    return new RubyEnumerator(allCons);
  }

  public static <E> RubyArray<E> eachEntry(Iterable<E> iter, ItemBlock<E> block) {
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : iter) {
      block.yield(item);
      rubyArray.add(item);
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<E> eachEntry(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> void eachSlice(Iterable<E> iter, int n, ItemFromListBlock<E> block) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    List<E> list = newArrayList(iter);
    int blocks = list.size() % n == 0 ? list.size() / n : list.size() / n + 1;
    for (int i = 0; i < blocks; i++) {
      List<E> cons = newArrayList();
      for (int j = i * n; j < list.size() ? j < i * n + n : false; j++) {
        cons.add(list.get(j));
      }
      block.yield(cons);
    }
  }

  public static <E> RubyEnumerator<RubyArray<E>> eachSlice(Iterable<E> iter, int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    RubyArray<RubyArray<E>> allSlices = new RubyArrayList();
    List<E> list = newArrayList(iter);
    int blocks = list.size() % n == 0 ? list.size() / n : list.size() / n + 1;
    for (int i = 0; i < blocks; i++) {
      RubyArray<E> slice = new RubyArrayList();
      for (int j = i * n; j < list.size() ? j < i * n + n : false; j++) {
        slice.add(list.get(j));
      }
      allSlices.add(slice);
    }
    return new RubyEnumerator(allSlices);
  }

  public static <E> RubyArray<E> eachWithIndex(Iterable<E> iter, ItemWithIndexBlock<E> block) {
    RubyArray<E> rubyArray = new RubyArrayList();
    int i = 0;
    for (E item : iter) {
      block.yield(item, i);
      rubyArray.add(item);
      i++;
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<Entry<E, Integer>> eachWithIndex(Iterable<E> iter) {
    RubyArray<Entry<E, Integer>> rubyArray = new RubyArrayList();
    int i = 0;
    for (E item : iter) {
      rubyArray.add(new SimpleEntry(item, i));
      i++;
    }
    return new RubyEnumerator(rubyArray);
  }

  public static <E, S> S eachWithObject(Iterable<E> iter, S o, ItemWithObjectBlock<E, S> block) {
    for (E item : iter) {
      block.yield(item, o);
    }
    return o;
  }

  public static <E, S> RubyEnumerator<Entry<E, S>> eachWithObject(Iterable<E> iter, S o) {
    RubyArray<Entry<E, S>> rubyArray = new RubyArrayList();
    for (E item : iter) {
      rubyArray.add(new SimpleEntry(item, o));
    }
    return new RubyEnumerator(rubyArray);
  }

  public static <E> RubyArray<E> entries(Iterable<E> iter) {
    return new RubyArrayList(iter);
  }

  public static <E> E find(Iterable<E> iter, BooleanBlock block) {
    return detect(iter, block);
  }

  public static <E> RubyEnumerator<E> find(Iterable<E> iter) {
    return detect(iter);
  }

  public static <E> RubyArray<E> findAll(Iterable<E> iter, BooleanBlock block) {
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : iter) {
      if (block.yield(item)) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<E> findAll(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> E first(Iterable<E> iter) {
    Iterator<E> iterator = iter.iterator();
    if (iterator.hasNext()) {
      return iterator.next();
    } else {
      return null;
    }
  }

  public static <E> RubyArray<E> first(Iterable<E> iter, int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to take negative size");
    }
    RubyArray<E> rubyArray = new RubyArrayList(iter);
    for (int i = 0; i < n && i < rubyArray.size(); i++) {
      rubyArray.add(rubyArray.get(i));
    }
    return rubyArray;
  }

  public static <E> Integer findIndex(Iterable<E> iter, E target) {
    int index = 0;
    for (E item : iter) {
      if (item.equals(target)) {
        return index;
      }
      index++;
    }
    return null;
  }

  public static <E> Integer findIndex(Iterable<E> iter, BooleanBlock<E> block) {
    int index = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        return index;
      }
      index++;
    }
    return null;
  }

  public static <E> RubyEnumerator<E> findIndex(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E, S> RubyArray<S> flatMap(Iterable<E> iter, ItemToListBlock<E, S> block) {
    return collectConcat(iter, block);
  }

  public static <E> RubyEnumerator<E> flatMap(Iterable<E> iter) {
    return collectConcat(iter);
  }

  public static <E> RubyArray<E> grep(Iterable<E> iter, String regex) {
    Pattern pattern = Pattern.compile(regex);
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : iter) {
      Matcher matcher = pattern.matcher(item.toString());
      if (matcher.find()) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public static <E, S> RubyArray<S> grep(Iterable<E> iter, String regex, ItemTransformBlock<E, S> block) {
    Pattern pattern = Pattern.compile(regex);
    RubyArray<S> rubyArray = new RubyArrayList();
    for (E item : iter) {
      Matcher matcher = pattern.matcher(item.toString());
      if (matcher.find()) {
        rubyArray.add(block.yield(item));
      }
    }
    return rubyArray;
  }

  public static <E, K> RubyHash<K, RubyArray<E>> groupBy(Iterable<E> iter, ItemTransformBlock<E, K> block) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : iter) {
      K key = block.yield(item);
      multimap.put(key, item);
    }
    RubyHash<K, RubyArray<E>> map = new RubyLinkedHashMap();
    for (K key : multimap.keySet()) {
      map.put(key, new RubyArrayList(multimap.get(key)));
    }
    return map;
  }

  public static <E> RubyEnumerator<E> groupBy(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> boolean includeʔ(Iterable<E> iter, E target) {
    for (E item : iter) {
      if (item.equals(target)) {
        return true;
      }
    }
    return false;
  }

  public static <E> boolean memberʔ(Iterable<E> iter, E target) {
    return includeʔ(iter, target);
  }

  public static <E> E inject(Iterable<E> iter, String methodName) {
    E result = null;
    Iterator<E> iterator = iter.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      if (i == 0) {
        result = iterator.next();
      } else {
        E curr = iterator.next();
        try {
          Method m = result.getClass().getDeclaredMethod(methodName, result.getClass());
          result = (E) m.invoke(result, curr);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE, null, ex);
        }
      }
      i++;
    }
    return result;
  }

  public static <E> E inject(Iterable<E> iter, E init, String methodName) {
    E result = init;
    Iterator<E> iterator = iter.iterator();
    while (iterator.hasNext()) {
      E curr = iterator.next();
      try {
        Method m = result.getClass().getDeclaredMethod(methodName, result.getClass());
        result = (E) m.invoke(result, curr);
      } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    return result;
  }

  public static <E> E inject(Iterable<E> iter, InjectBlock<E> block) {
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

  public static <E, S> S inject(Iterable<E> iter, S init, InjectWithInitBlock<E, S> block) {
    for (E item : iter) {
      init = block.yield(init, item);
    }
    return init;
  }

  public static <E, S> RubyArray<S> map(Iterable<E> iter, ItemTransformBlock<E, S> block) {
    return collect(iter, block);
  }

  public static <E> RubyEnumerator<E> map(Iterable<E> iter) {
    return collect(iter);
  }

  public static <E> E max(Iterable<E> iter) {
    return sort(iter).first();
  }

  public static <E> E max(Iterable<E> iter, Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.max(list, comp);
  }

  public static <E, S> E maxBy(Iterable<E> iter, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = max(dst);
    return src.get(dst.indexOf(maxDst));
  }

  public static <E, S> E maxBy(Iterable<E> iter, Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  public static <E> RubyEnumerator<E> maxBy(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E > E min(Iterable<E> iter) {
    return sort(iter).last();
  }

  public static <E> E min(Iterable<E> iter, Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.min(list, comp);
  }

  public static <E, S> E minBy(Iterable<E> iter, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = min(dst);
    return src.get(dst.indexOf(minDst));
  }

  public static <E, S> E minBy(Iterable<E> iter, Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  public static <E> RubyEnumerator<E> minBy(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E > RubyArray<E> minmax(Iterable<E> iter) {
    RubyArray<E> rubyArray = sort(iter);
    return new RubyArrayList(rubyArray.last(),rubyArray.first());
  }

  public static <E> RubyArray<E> minmax(Iterable<E> iter, Comparator<? super E> comp) {
    RubyArray<E> rubyArray = new RubyArrayList(iter);
    return new RubyArrayList(Collections.min(rubyArray, comp), Collections.max(rubyArray, comp));
  }

  public static <E, S > RubyArray<E> minmaxBy(Iterable<E> iter, ItemTransformBlock<E, S> block) {
    RubyArray<E> src = new RubyArrayList();
    RubyArray<S> dst = new RubyArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = min(dst);
    S maxDst = max(dst);
    return new RubyArrayList(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  public static <E, S> RubyArray<E> minmaxBy(Iterable<E> iter, Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    RubyArray<E> src = new RubyArrayList();
    RubyArray<S> dst = new RubyArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return new RubyArrayList(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  public static <E> RubyEnumerator<E> minmaxBy(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> RubyArray<RubyArray<E>> partition(Iterable<E> iter, BooleanBlock block) {
    RubyArray<E> trueList = new RubyArrayList();
    RubyArray<E> falseList = new RubyArrayList();
    for (E item : iter) {
      if (block.yield(item)) {
        trueList.add(item);
      } else {
        falseList.add(item);
      }
    }
    return new RubyArrayList(trueList, falseList);
  }

  public static <E> RubyEnumerator<E> partition(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> boolean noneʔ(Iterable<E> iter) {
    boolean bool = true;
    for (E item : iter) {
      if (item != null) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean noneʔ(Iterable<E> iter, BooleanBlock<E> block) {
    boolean bool = true;
    for (E item : iter) {
      if (block.yield(item)) {
        bool = false;
      }
    }
    return bool;
  }

  public static <E> boolean oneʔ(Iterable<E> iter) {
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

  public static <E> boolean oneʔ(Iterable<E> iter, BooleanBlock<E> block) {
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

  public static <E> E reduce(Iterable<E> iter, String methodName) {
    return inject(iter, methodName);
  }

  public static <E> E reduce(Iterable<E> iter, E init, String methodName) {
    return inject(iter, init, methodName);
  }

  public static <E> E reduce(Iterable<E> iter, InjectBlock<E> block) {
    return inject(iter, block);
  }

  public static <E, S> S reduce(Iterable<E> iter, S init, InjectWithInitBlock<E, S> block) {
    return inject(iter, init, block);
  }

  public static <E> RubyArray<E> reject(Iterable<E> iter, BooleanBlock<E> block) {
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : iter) {
      if (!(block.yield(item))) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<E> reject(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> void reverseEach(Iterable<E> iter, ItemBlock block) {
    List<E> list = newArrayList(iter);
    for (E item : reverse(list)) {
      block.yield(item);
    }
  }

  public static <E> RubyEnumerator<E> reverseEach(Iterable<E> iter) {
    return new RubyEnumerator(Lists.reverse(newArrayList(iter)));
  }

  public static <E> RubyArray<E> select(Iterable<E> iter, BooleanBlock block) {
    return findAll(iter, block);
  }

  public static <E> RubyEnumerator<E> select(Iterable<E> iter) {
    return findAll(iter);
  }

  public static <E> RubyEnumerator<RubyArray<E>> sliceBefore(Iterable<E> iter, String regex) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArrayList();
    Pattern pattern = Pattern.compile(regex);
    RubyArray<E> group = null;
    for (E item : iter) {
      if (group == null) {
        group = new RubyArrayList();
        group.add(item);
      } else if (pattern.matcher(item.toString()).find()) {
        rubyArray.add(group);
        group = new RubyArrayList();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      rubyArray.add(group);
    }
    return new RubyEnumerator(rubyArray);
  }

  public static <E> RubyEnumerator<RubyArray<E>> sliceBefore(Iterable<E> iter, BooleanBlock block) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArrayList();
    RubyArray<E> group = null;
    for (E item : iter) {
      if (group == null) {
        group = new RubyArrayList();
        group.add(item);
      } else if (block.yield(item)) {
        rubyArray.add(group);
        group = new RubyArrayList();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      rubyArray.add(group);
    }
    return new RubyEnumerator(rubyArray);
  }

  public static <E> RubyArray<E> sort(Iterable<E> iter) {
    RubyArray<E> rubyArray = new RubyArrayList(iter);
    Object[] array = rubyArray.toArray();
    Arrays.sort(array);
    return new RubyArrayList(array);
  }

  public static <E> RubyArray<E> sort(Iterable<E> iter, Comparator<? super E> comp) {
    RubyArray<E> rubyArray = new RubyArrayList(iter);
    Collections.sort(rubyArray, comp);
    return rubyArray;
  }

  public static <E, S> RubyArray<E> sortBy(Iterable<E> iter, ItemTransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    RubyArray<E> sortedList = new RubyArrayList();
    for (E item : iter) {
      multimap.put(block.yield(item), item);
    }
    List<S> keys = newArrayList(multimap.keySet());
    keys = sort(keys);
    for (S key : keys) {
      Collection<E> coll = multimap.get(key);
      Iterator<E> iterator = coll.iterator();
      while (iterator.hasNext()) {
        sortedList.add(iterator.next());
      }
    }
    return sortedList;
  }

  public static <E, S> RubyArray<E> sortBy(Iterable<E> iter, Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    RubyArray<E> sortedList = new RubyArrayList();
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

  public static <E> RubyEnumerator<E> sortBy(Iterable<E> iter) {
    return new RubyEnumerator(iter);
  }

  public static <E> RubyArray<E> take(Iterable<E> iter, int n) {
    if (n < 0) {
      throw new IllegalArgumentException("attempt to take negative size");
    }
    RubyArray<E> rubyArray = new RubyArrayList();
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

  public static <E> RubyArray<E> takeWhile(Iterable<E> iter, BooleanBlock block) {
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : iter) {
      if (block.yield(item)) {
        rubyArray.add(item);
      } else {
        return rubyArray;
      }
    }
    return rubyArray;
  }

  public static <E> RubyEnumerator<E> takeWhile(Iterable<E> iter) {
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return new RubyEnumerator(rubyArray);
  }

  public static <E> RubyArray<E> toA(Iterable<E> iter) {
    return new RubyArrayList(newArrayList(iter));
  }

  public static <E> RubyArray<RubyArray<E>> zip(Iterable<E> iter, RubyArray<E>... others) {
    RubyArray<E> rubyArray = new RubyArrayList(iter);
    RubyArray<RubyArray<E>> zippedRubyArray = new RubyArrayList<>();
    for (int i = 0; i < rubyArray.size(); i++) {
      RubyArray<E> zip = new RubyArrayList();
      zip.add(rubyArray.at(i));
      for (int j = 0; j < others.length; j++) {
        zip.add(others[j].at(i));
      }
      zippedRubyArray.add(zip);
    }
    return zippedRubyArray;
  }

  public static <E> void zip(Iterable<E> iter, RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> zippedRubyArray = zip(iter, others.toArray(new RubyArrayList[others.length()]));
    for (RubyArray<E> item : zippedRubyArray) {
      block.yield(item);
    }
  }
}
