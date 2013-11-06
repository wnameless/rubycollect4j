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
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.RubyContract.Enumerator;
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
import net.sf.rubycollect4j.iter.TransformIterable;
import net.sf.rubycollect4j.iter.ZipIterable;
import net.sf.rubycollect4j.util.PeekingIterator;
import net.sf.rubycollect4j.util.TryComparator;

/**
 * 
 * RubyLazyEnumerator implements most of the methods refer to the
 * Enumerator::Lazy of Ruby language. RubyLazyEnumerator is both Iterable and
 * Iterator and it's also a peeking iterator.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RubyLazyEnumerator<E> implements
    Enumerator<E, RubyLazyEnumerator<?>, RubyLazyEnumerator<?>> {

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
    pIterator = new PeekingIterator<E>(iter.iterator());
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

  @Override
  public <S> RubyLazyEnumerator<Entry<S, RubyArray<E>>> chunk(
      TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(new ChunkIterable<E, S>(iter, block));
  }

  @Override
  public <S> RubyLazyEnumerator<Entry<S, RubyArray<E>>> chunk(
      final String methodName, final Object... args) {
    return newRubyLazyEnumerator(new ChunkIterable<E, S>(iter,
        new TransformBlock<E, S>() {

          @Override
          public S yield(E item) {
            return RubyObject.send(item, methodName, args);
          }

        }));
  }

  @Override
  public RubyLazyEnumerator<E> collect() {
    return this;
  }

  @Override
  public <S> RubyLazyEnumerator<S> collect(TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(new TransformIterable<E, S>(iter, block));
  }

  @Override
  public <S> RubyLazyEnumerator<S> collect(final String methodName,
      final Object... args) {
    return collect(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  @Override
  public RubyLazyEnumerator<E> collectConcat() {
    return this;
  }

  @Override
  public <S> RubyLazyEnumerator<S> collectConcat(
      TransformBlock<E, ? extends List<S>> block) {
    return newRubyLazyEnumerator(new FlattenIterable<E, S>(iter, block));
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

  @Override
  public RubyLazyEnumerator<E> cycle() {
    return newRubyLazyEnumerator(new CycleIterable<E>(iter));
  }

  @Override
  public RubyLazyEnumerator<E> cycle(int n) {
    return newRubyLazyEnumerator(new CycleIterable<E>(iter, n));
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

  @Override
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

  @Override
  public RubyLazyEnumerator<E> drop(int n) {
    return newRubyLazyEnumerator(new DropIterable<E>(iter, n));
  }

  @Override
  public RubyLazyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyLazyEnumerator(rubyArray);
  }

  @Override
  public RubyLazyEnumerator<E> dropWhile(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(new DropWhileIterable<E>(iter, block));
  }

  @Override
  public RubyLazyEnumerator<E> each() {
    return this;
  }

  @Override
  public RubyLazyEnumerator<E> each(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  @Override
  public RubyLazyEnumerator<RubyArray<E>> eachCons(int n) {
    return newRubyLazyEnumerator(new EachConsIterable<E>(iter, n));
  }

  @Override
  public void eachCons(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> cons : eachCons(n)) {
      block.yield(cons);
    }
  }

  @Override
  public RubyLazyEnumerator<E> eachEntry() {
    return this;
  }

  @Override
  public RubyLazyEnumerator<E> eachEntry(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  @Override
  public RubyLazyEnumerator<RubyArray<E>> eachSlice(int n) {
    return newRubyLazyEnumerator(new EachSliceIterable<E>(iter, n));
  }

  @Override
  public void eachSlice(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> ra : new EachSliceIterable<E>(iter, n)) {
      block.yield(ra);
    }
  }

  @Override
  public RubyLazyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return newRubyLazyEnumerator(new EachWithIndexIterable<E>(iter));
  }

  @Override
  public RubyLazyEnumerator<E> eachWithIndex(WithIndexBlock<E> block) {
    int i = 0;
    for (E item : iter) {
      block.yield(item, i);
      i++;
    }
    return this;
  }

  @Override
  public <O> RubyLazyEnumerator<Entry<E, O>> eachWithObject(O obj) {
    return newRubyLazyEnumerator(new EachWithObjectIterable<E, O>(iter, obj));
  }

  @Override
  public <O> O eachWithObject(O obj, WithObjectBlock<E, O> block) {
    for (E item : iter) {
      block.yield(item, obj);
    }
    return obj;
  }

  @Override
  public RubyArray<E> entries() {
    return newRubyArray(iter);
  }

  @Override
  public RubyLazyEnumerator<E> find() {
    return detect();
  }

  @Override
  public RubyLazyEnumerator<E> findAll() {
    return this;
  }

  @Override
  public E find(BooleanBlock<E> block) {
    return detect(block);
  }

  @Override
  public RubyLazyEnumerator<E> findAll(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(new FindAllIterable<E>(iter, block));
  }

  @Override
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
    Iterator<E> elements = iter.iterator();
    if (elements.hasNext())
      return elements.next();
    else
      return null;
  }

  @Override
  public RubyArray<E> first(int n) {
    if (n < 0)
      throw new IllegalArgumentException(
          "ArgumentError: attempt to take negative size");

    Iterator<E> elements = iter.iterator();
    RubyArray<E> rubyArray = newRubyArray();
    for (int i = 0; i < n && elements.hasNext(); i++) {
      rubyArray.add(elements.next());
    }
    return rubyArray;
  }

  @Override
  public RubyLazyEnumerator<E> flatMap() {
    return collectConcat();
  }

  @Override
  public <S> RubyLazyEnumerator<S> flatMap(
      TransformBlock<E, ? extends List<S>> block) {
    return collectConcat(block);
  }

  @Override
  public RubyLazyEnumerator<E> grep(String regex) {
    return newRubyLazyEnumerator(new GrepIterable<E>(iter, regex));
  }

  @Override
  public <S> RubyLazyEnumerator<S>
      grep(String regex, TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(new TransformIterable<E, S>(
        new GrepIterable<E>(iter, regex), block));
  }

  @Override
  public <S> RubyLazyEnumerator<S> grep(String regex, final String methodName,
      final Object... args) {
    return grep(regex, new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  @Override
  public RubyLazyEnumerator<E> groupBy() {
    return this;
  }

  @Override
  public <S> RubyHash<S, RubyArray<E>> groupBy(TransformBlock<E, S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = newRubyHash();
    for (E item : iter) {
      S key = block.yield(item);
      if (!rubyHash.containsKey(key))
        rubyHash.put(key, new RubyArray<E>());

      rubyHash.get(key).add(item);
    }
    return rubyHash;
  }

  @Override
  public <S> RubyHash<S, RubyArray<E>> groupBy(final String methodName,
      final Object... args) {
    return groupBy(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
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
    Iterator<E> elements = iter.iterator();
    if (elements.hasNext())
      result = elements.next();

    while (elements.hasNext()) {
      result = block.yield(result, elements.next());
    }
    return result;
  }

  @Override
  public <I> I inject(I init, WithInitBlock<E, I> block) {
    for (E item : iter) {
      init = block.yield(init, item);
    }
    return init;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <I> I inject(I init, String methodName) {
    I result = init;
    Iterator<E> elements = iter.iterator();
    while (elements.hasNext()) {
      result = (I) RubyObject.send(result, methodName, elements.next());
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  @Override
  public E inject(String methodName) {
    E result = null;
    Iterator<E> elements = iter.iterator();
    if (elements.hasNext())
      result = elements.next();

    while (elements.hasNext()) {
      result = (E) RubyObject.send(result, methodName, elements.next());
    }
    return result;
  }

  @Override
  public RubyLazyEnumerator<E> lazy() {
    return this;
  }

  @Override
  public RubyLazyEnumerator<E> map() {
    return collect();
  }

  @Override
  public <S> RubyLazyEnumerator<S> map(TransformBlock<E, S> block) {
    return collect(block);
  }

  @Override
  public <S> RubyLazyEnumerator<S> map(String methodName, Object... args) {
    return collect(methodName, args);
  }

  @Override
  public E max() {
    return minmax().last();
  }

  @Override
  public E max(Comparator<? super E> comp) {
    return minmax(comp).last();
  }

  @Override
  public RubyLazyEnumerator<E> maxBy() {
    return this;
  }

  @Override
  public <S> E maxBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    return minmaxBy(comp, block).last();
  }

  @Override
  public <S> E maxBy(TransformBlock<E, S> block) {
    return minmaxBy(block).last();
  }

  @Override
  public <S> E maxBy(final String methodName, final Object... args) {
    return maxBy(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  @Override
  public boolean memberʔ(E target) {
    return includeʔ(target);
  }

  @Override
  public E min() {
    return minmax().first();
  }

  @Override
  public E min(Comparator<? super E> comp) {
    return minmax(comp).first();
  }

  @Override
  public RubyLazyEnumerator<E> minBy() {
    return this;
  }

  @Override
  public <S> E minBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    return minmaxBy(comp, block).first();
  }

  @Override
  public <S> E minBy(TransformBlock<E, S> block) {
    return minmaxBy(block).first();
  }

  @Override
  public <S> E minBy(final String methodName, final Object... args) {
    return minBy(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  @Override
  public RubyArray<E> minmax() {
    return minmax(null);
  }

  @SuppressWarnings("unchecked")
  @Override
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    Iterator<E> elements = iter.iterator();
    if (!elements.hasNext())
      return newRubyArray(null, null);

    Comparator<E> tryComp = new TryComparator<E>(comp);
    E max = elements.next();
    E min = max;
    while (elements.hasNext()) {
      E item = elements.next();
      if (tryComp.compare(max, item) < 0)
        max = item;
      if (tryComp.compare(min, item) > 0)
        min = item;
    }
    return newRubyArray(min, max);
  }

  @Override
  public RubyLazyEnumerator<E> minmaxBy() {
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    Iterator<E> elements = iter.iterator();
    if (!elements.hasNext())
      return newRubyArray(null, null);

    Comparator<S> tryComp = new TryComparator<S>(comp);
    E max = elements.next();
    E min = max;
    while (elements.hasNext()) {
      E item = elements.next();
      if (tryComp.compare(block.yield(max), block.yield(item)) < 0)
        max = item;
      if (tryComp.compare(block.yield(min), block.yield(item)) > 0)
        min = item;
    }
    return newRubyArray(min, max);
  }

  @Override
  public <S> RubyArray<E> minmaxBy(TransformBlock<E, S> block) {
    return minmaxBy(null, block);
  }

  @Override
  public <S> RubyArray<E> minmaxBy(final String methodName,
      final Object... args) {
    return minmaxBy(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
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

  @Override
  public RubyLazyEnumerator<E> partition() {
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
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

  @Override
  public RubyLazyEnumerator<E> reject() {
    return this;
  }

  @Override
  public RubyLazyEnumerator<E> reject(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(new RejectIterable<E>(iter, block));
  }

  @Override
  public RubyLazyEnumerator<E> reverseEach() {
    return newRubyLazyEnumerator(new ReverseEachIterable<E>(iter));
  }

  @Override
  public RubyLazyEnumerator<E> reverseEach(Block<E> block) {
    for (E item : reverseEach()) {
      block.yield(item);
    }
    return this;
  }

  @Override
  public RubyLazyEnumerator<E> select() {
    return findAll();
  }

  @Override
  public RubyLazyEnumerator<E> select(BooleanBlock<E> block) {
    return findAll(block);
  }

  @Override
  public RubyLazyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(new SliceBeforeIterable<E>(iter, block));
  }

  @Override
  public RubyLazyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return newRubyLazyEnumerator(new SliceBeforeIterable<E>(iter,
        Pattern.compile(regex)));
  }

  @Override
  public RubyArray<E> sort() {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() <= 1)
      return rubyArray;

    Collections.sort(rubyArray, new TryComparator<E>());
    return rubyArray;
  }

  // @Override
  // public RubyArray<E> sort(Comparator<? super E> comp) {
  // RubyArray<E> rubyArray = newRubyArray(iter);
  // if (rubyArray.size() <= 1)
  // return rubyArray;
  //
  // Collections.sort(rubyArray, new TryComparator<E>(comp));
  // return rubyArray;
  // }

  @Override
  public RubyLazyEnumerator<E> sortBy() {
    return this;
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    RubyArray<E> rubyArray = newRubyArray();
    for (S key : rubyHash.keys().sortǃ(comp)) {
      rubyArray.addAll(rubyHash.get(key));
    }
    return rubyArray;
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, TransformBlock<E, S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    RubyArray<E> rubyArray = newRubyArray();
    for (S key : rubyHash.keys().sortǃ(comp2)) {
      rubyArray.addAll(rubyHash.get(key).sortǃ(comp1));
    }
    return rubyArray;
  }

  @Override
  public <S> RubyArray<E> sortBy(TransformBlock<E, S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    RubyArray<E> rubyArray = newRubyArray();
    for (S key : rubyHash.keys().sortǃ()) {
      rubyArray.addAll(rubyHash.get(key));
    }
    return rubyArray;
  }

  @Override
  public <S> RubyArray<E> sortBy(final String methodName, final Object... args) {
    return sortBy(new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
  }

  @Override
  public RubyLazyEnumerator<E> take(int n) {
    return newRubyLazyEnumerator(new TakeIterable<E>(iter, n));
  }

  @Override
  public RubyLazyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyLazyEnumerator(rubyArray);
  }

  @Override
  public RubyLazyEnumerator<E> takeWhile(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(new TakeWhileIterable<E>(iter, block));
  }

  @Override
  public RubyArray<E> toA() {
    return newRubyArray(iter);
  }

  @Override
  public RubyLazyEnumerator<RubyArray<E>> zip(Iterable<E>... others) {
    return zip(Arrays.asList(others));
  }

  @Override
  public RubyLazyEnumerator<RubyArray<E>>
      zip(List<? extends Iterable<E>> others) {
    return newRubyLazyEnumerator(new ZipIterable<E>(iter, others));
  }

  @Override
  public void
      zip(List<? extends Iterable<E>> others, Block<RubyArray<E>> block) {
    RubyLazyEnumerator<RubyArray<E>> rubyArrays = zip(others);
    for (RubyArray<E> item : rubyArrays) {
      block.yield(item);
    }
  }

  /**
   * Resets the iterator of this RubyLazyEnumerator to the beginning.
   * 
   * @return this RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> rewind() {
    pIterator = new PeekingIterator<E>(iter.iterator());
    return this;
  }

  /**
   * Returns the next element without advancing the iteration.
   * 
   * @return an element
   */
  public E peek() {
    return pIterator.peek();
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }

  @Override
  public boolean hasNext() {
    return pIterator.hasNext();
  }

  @Override
  public E next() {
    return pIterator.next();
  }

  @Override
  public void remove() {
    pIterator.remove();
  }

  @Override
  public String toString() {
    return "RubyLazyEnumerator{" + iter + "}";
  }

}
