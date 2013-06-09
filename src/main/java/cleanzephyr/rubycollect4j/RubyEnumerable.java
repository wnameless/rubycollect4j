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

import static java.util.Map.Entry;
import static cleanzephyr.rubycollect4j.RubyHash.newRubyHash;
import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.InjectBlock;
import cleanzephyr.rubycollect4j.block.InjectWithInitBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;
import cleanzephyr.rubycollect4j.block.ItemToListBlock;
import cleanzephyr.rubycollect4j.block.ItemTransformBlock;
import cleanzephyr.rubycollect4j.block.ItemWithIndexBlock;
import cleanzephyr.rubycollect4j.block.ItemWithObjectBlock;
import cleanzephyr.rubycollect4j.block.ListBlock;
import cleanzephyr.rubycollect4j.iter.ChunkIterable;
import cleanzephyr.rubycollect4j.iter.EachSliceIterable;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.reverse;
import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import cleanzephyr.rubycollect4j.iter.CycleIterable;
import cleanzephyr.rubycollect4j.iter.EachConsIterable;
import com.google.common.collect.Multimap;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author WMW
 * @param <E>
 */
public class RubyEnumerable<E> {

  protected final Iterable<E> iter;

  public RubyEnumerable(Iterable<E> iter) {
    this.iter = iter;
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

  public <K> RubyEnumerator<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block) {
    return new RubyEnumerator<>(new ChunkIterable<E, K>(iter, block));
  }

  public <S> RubyArray<S> collect(ItemTransformBlock<E, S> block) {
    RubyArray<S> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(block.yield(item));
    }
    return rubyArray;
  }

  public RubyEnumerator<E> collect() {
    return new RubyEnumerator<>(iter);
  }

  public <S> RubyArray<S> collectConcat(ItemToListBlock<E, S> block) {
    RubyArray<S> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.addAll(block.yield(item));
    }
    return rubyArray;
  }

  public RubyEnumerator<E> collectConcat() {
    return new RubyEnumerator<>(iter);
  }

  public int count() {
    int count = 0;
    for (E item : iter) {
      count++;
    }
    return count;
  }

  public int count(BooleanBlock block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        count++;
      }
    }
    return count;
  }

  public void cycle(ItemBlock block) {
    while (true) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  public RubyEnumerator<E> cycle() {
    return new RubyEnumerator<>(Iterables.cycle(iter));
  }

  public void cycle(int n, ItemBlock block) {
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  public RubyEnumerator<E> cycle(int n) {
    return new RubyEnumerator<>(new CycleIterable<E>(iter, n));
  }

  public E detect(BooleanBlock block) {
    for (E item : iter) {
      if (block.yield(item)) {
        return item;
      }
    }
    return null;
  }

  public RubyEnumerator<E> detect() {
    return new RubyEnumerator(iter);
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

  public RubyArray<E> dropWhile(BooleanBlock block) {
    RubyArray<E> rubyArray = newRubyArray();
    boolean cutPoint = false;
    for (E item : iter) {
      if (block.yield(item) || cutPoint) {
        cutPoint = true;
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public RubyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return new RubyEnumerator(rubyArray);
  }

  public void eachCons(int n, ListBlock<E> block) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    RubyArray<E> list = newRubyArray(iter);
    for (int i = 0; i < list.size(); i++) {
      if (i + n <= list.size()) {
        RubyArray<E> cons = newRubyArray();
        for (int j = i; j < i + n; j++) {
          cons.add(list.get(j));
        }
        block.yield(cons);
      }
    }
  }

  public RubyEnumerator<RubyArray<E>> eachCons(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    return new RubyEnumerator<>(new EachConsIterable<E>(iter, n));
  }

  public RubyArray<E> eachEntry(ItemBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      block.yield(item);
      rubyArray.add(item);
    }
    return rubyArray;
  }

  public RubyEnumerator<E> eachEntry() {
    return new RubyEnumerator(iter);
  }

  public void eachSlice(int n, ListBlock<E> block) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    for (RubyArray<E> ra : new EachSliceIterable<>(iter, n)) {
      block.yield(ra);
    }
  }

  public RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException("invalid slice size");
    }
    return new RubyEnumerator<>(new EachSliceIterable<>(iter, n));
  }

  public RubyArray<E> eachWithIndex(ItemWithIndexBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    int i = 0;
    for (E item : iter) {
      block.yield(item, i);
      rubyArray.add(item);
      i++;
    }
    return rubyArray;
  }

  public RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    RubyArray<Entry<E, Integer>> rubyArray = newRubyArray();
    int i = 0;
    for (E item : iter) {
      rubyArray.add(new AbstractMap.SimpleEntry(item, i));
      i++;
    }
    return new RubyEnumerator(rubyArray);
  }

  public <S> S eachWithObject(S o, ItemWithObjectBlock<E, S> block) {
    for (E item : iter) {
      block.yield(item, o);
    }
    return o;
  }

  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o) {
    RubyArray<Map.Entry<E, S>> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(new AbstractMap.SimpleEntry(item, o));
    }
    return new RubyEnumerator(rubyArray);
  }

  public RubyArray<E> entries() {
    return newRubyArray(iter);
  }

  public E find(BooleanBlock block) {
    return detect(block);
  }

  public RubyEnumerator<E> find() {
    return detect();
  }

  public RubyArray<E> findAll(BooleanBlock block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      if (block.yield(item)) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public RubyEnumerator<E> findAll() {
    return new RubyEnumerator(iter);
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
    RubyArray<E> rubyArray = newRubyArray(iter);
    for (int i = 0; i < n && i < rubyArray.size(); i++) {
      rubyArray.add(rubyArray.get(i));
    }
    return rubyArray;
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

  public RubyEnumerator<E> findIndex() {
    return new RubyEnumerator(iter);
  }

  public <S> RubyArray<S> flatMap(ItemToListBlock<E, S> block) {
    return collectConcat(block);
  }

  public RubyEnumerator<E> flatMap() {
    return collectConcat();
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

  public RubyEnumerator<E> groupBy() {
    return new RubyEnumerator(iter);
  }

  public boolean includeʔ(E target) {
    for (E item : iter) {
      if (item.equals(target)) {
        return true;
      }
    }
    return false;
  }

  public boolean memberʔ(E target) {
    return includeʔ(target);
  }

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

  public E inject(E init, String methodName) {
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

  public <S> RubyArray<S> map(ItemTransformBlock<E, S> block) {
    return collect(block);
  }

  public RubyEnumerator<E> map() {
    return collect();
  }

  public E max() {
    return sort().first();
  }

  public E max(Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.max(list, comp);
  }

  public <S> E maxBy(ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = new RubyEnumerable<>(dst).max();
    return src.get(dst.indexOf(maxDst));
  }

  public <S> E maxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

  public RubyEnumerator<E> maxBy() {
    return new RubyEnumerator(iter);
  }

  public E min() {
    return sort().last();
  }

  public E min(Comparator<? super E> comp) {
    List<E> list = newArrayList(iter);
    return Collections.min(list, comp);
  }

  public <S> E minBy(ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = new RubyEnumerable<>(dst).min();
    return src.get(dst.indexOf(minDst));
  }

  public <S> E minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    List<E> src = newArrayList();
    List<S> dst = newArrayList();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

  public RubyEnumerator<E> minBy() {
    return new RubyEnumerator(iter);
  }

  public RubyArray<E> minmax() {
    RubyArray<E> rubyArray = sort();
    return newRubyArray(rubyArray.last(), rubyArray.first());
  }

  public RubyArray<E> minmax(Comparator<? super E> comp) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    return newRubyArray(Collections.min(rubyArray, comp), Collections.max(rubyArray, comp));
  }

  public <S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = new RubyEnumerable<>(dst).min();
    S maxDst = new RubyEnumerable<>(dst).max();
    return newRubyArray(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return newRubyArray(src.get(dst.indexOf(minDst)), src.get(dst.indexOf(maxDst)));
  }

  public RubyEnumerator<E> minmaxBy() {
    return new RubyEnumerator(iter);
  }

  public RubyArray<RubyArray<E>> partition(BooleanBlock block) {
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

  public RubyEnumerator<E> partition() {
    return new RubyEnumerator(iter);
  }

  public boolean noneʔ() {
    boolean bool = true;
    for (E item : iter) {
      if (item != null) {
        bool = false;
      }
    }
    return bool;
  }

  public boolean noneʔ(BooleanBlock<E> block) {
    boolean bool = true;
    for (E item : iter) {
      if (block.yield(item)) {
        bool = false;
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

  public E reduce(String methodName) {
    return inject(methodName);
  }

  public E reduce(E init, String methodName) {
    return inject(init, methodName);
  }

  public E reduce(InjectBlock<E> block) {
    return inject(block);
  }

  public <S> S reduce(S init, InjectWithInitBlock<E, S> block) {
    return inject(init, block);
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

  public RubyEnumerator<E> reject() {
    return new RubyEnumerator(iter);
  }

  public void reverseEach(ItemBlock block) {
    List<E> list = newArrayList(iter);
    for (E item : reverse(list)) {
      block.yield(item);
    }
  }

  public RubyEnumerator<E> reverseEach() {
    return new RubyEnumerator(Lists.reverse(newArrayList(iter)));
  }

  public RubyArray<E> select(BooleanBlock block) {
    return findAll(block);
  }

  public RubyEnumerator<E> select() {
    return findAll();
  }

  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    RubyArray<RubyArray<E>> rubyArray = newRubyArray();
    Pattern pattern = Pattern.compile(regex);
    RubyArray<E> group = null;
    for (E item : iter) {
      if (group == null) {
        group = newRubyArray();
        group.add(item);
      } else if (pattern.matcher(item.toString()).find()) {
        rubyArray.add(group);
        group = newRubyArray();
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

  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock block) {
    RubyArray<RubyArray<E>> rubyArray = newRubyArray();
    RubyArray<E> group = null;
    for (E item : iter) {
      if (group == null) {
        group = newRubyArray();
        group.add(item);
      } else if (block.yield(item)) {
        rubyArray.add(group);
        group = newRubyArray();
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

  public RubyArray<E> sort() {
    RubyArray<E> rubyArray = newRubyArray(iter);
    Object[] array = rubyArray.toArray();
    Arrays.sort(array);
    return newRubyArray((E[]) array);
  }

  public <S> RubyArray<E> sortBy(ItemTransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      multimap.put(block.yield(item), item);
    }
    List<S> keys = newArrayList(multimap.keySet());
    keys = new RubyEnumerable<>(keys).sort();
    for (S key : keys) {
      Collection<E> coll = multimap.get(key);
      Iterator<E> iterator = coll.iterator();
      while (iterator.hasNext()) {
        sortedList.add(iterator.next());
      }
    }
    return sortedList;
  }

  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
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

  public RubyEnumerator<E> sortBy() {
    return new RubyEnumerator(iter);
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

  public RubyArray<E> takeWhile(BooleanBlock block) {
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

  public RubyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return new RubyEnumerator<>(rubyArray);
  }

  public RubyArray<E> toA() {
    return new RubyArray(newArrayList(iter));
  }

  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    RubyArray<RubyArray<E>> zippedRubyArray = newRubyArray();
    for (int i = 0; i < rubyArray.size(); i++) {
      RubyArray<E> zip = newRubyArray();
      zip.add(rubyArray.at(i));
      for (int j = 0; j < others.length; j++) {
        zip.add(others[j].at(i));
      }
      zippedRubyArray.add(zip);
    }
    return zippedRubyArray;
  }

  public void zip(RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> zippedRubyArray = zip(others.toArray(new RubyArray[others.length()]));
    for (RubyArray<E> item : zippedRubyArray) {
      block.yield(item);
    }
  }
}
