/*
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

import static net.sf.rubycollect4j.RubyCollections.Hash;
import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.iter.ChunkIterable;
import net.sf.rubycollect4j.iter.ChunkWhileIterable;
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
import net.sf.rubycollect4j.iter.GrepVIterable;
import net.sf.rubycollect4j.iter.RejectIterable;
import net.sf.rubycollect4j.iter.ReverseEachIterable;
import net.sf.rubycollect4j.iter.SliceAfterIterable;
import net.sf.rubycollect4j.iter.SliceBeforeIterable;
import net.sf.rubycollect4j.iter.SliceWhenIterable;
import net.sf.rubycollect4j.iter.TakeIterable;
import net.sf.rubycollect4j.iter.TakeWhileIterable;
import net.sf.rubycollect4j.iter.TransformIterable;
import net.sf.rubycollect4j.iter.ZipIterable;
import net.sf.rubycollect4j.util.PeekingIterator;
import net.sf.rubycollect4j.util.TryComparator;

/**
 * 
 * {@link RubyLazyEnumerator} implements most of the methods refer to the
 * Enumerator::Lazy class of Ruby language.
 * <p>
 * {@link RubyLazyEnumerator} is both Iterable and Iterator and it's also a
 * peeking iterator.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RubyLazyEnumerator<E> implements RubyBase.LazyEnumerator<E> {

  private final Iterable<E> iter;
  private PeekingIterator<E> pIterator;

  /**
   * Returns a {@link RubyLazyEnumerator} which wraps the given Iterable.
   * 
   * @param iter
   *          any Iterable
   * @return {@link RubyLazyEnumerator}
   * @throws NullPointerException
   *           if iter is null
   */
  public static <E> RubyLazyEnumerator<E> of(Iterable<E> iter) {
    Objects.requireNonNull(iter);

    return new RubyLazyEnumerator<E>(iter);
  }

  /**
   * Returns a {@link RubyLazyEnumerator} which copies the elements of given
   * Iterable.
   * 
   * @param iter
   *          any Iterable
   * @return {@link RubyLazyEnumerator}
   * @throws NullPointerException
   *           if iter is null
   */
  public static <E> RubyLazyEnumerator<E> copyOf(Iterable<E> iter) {
    Objects.requireNonNull(iter);

    return new RubyLazyEnumerator<E>(RubyArray.copyOf(iter));
  }

  /**
   * Creates a {@link RubyLazyEnumerator} by given Iterable. It's a wrapper
   * implementation. No defensive copy has been made.
   * 
   * @param iterable
   *          any Iterable
   * @throws NullPointerException
   *           if iterable is null
   */
  public RubyLazyEnumerator(Iterable<E> iterable) {
    Objects.requireNonNull(iterable);

    iter = iterable;
    pIterator = new PeekingIterator<E>(iter.iterator());
  }

  @Override
  public boolean allʔ() {
    for (E item : iter) {
      if (item == null || Boolean.FALSE.equals(item)) return false;
    }
    return true;
  }

  @Override
  public boolean allʔ(Predicate<? super E> block) {
    for (E item : iter) {
      if (block.test(item) == false) return false;
    }
    return true;
  }

  @Override
  public boolean anyʔ() {
    for (E item : iter) {
      if (item != null && !Boolean.FALSE.equals(item)) return true;
    }
    return false;
  }

  @Override
  public boolean anyʔ(Predicate<? super E> block) {
    for (E item : iter) {
      if (block.test(item)) return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <S> RubyLazyEnumerator<Entry<S, RubyArray<E>>> chunk(
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(new ChunkIterable<E, S>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> chunkWhile(
      BiPredicate<? super E, ? super E> block) {
    return newRubyLazyEnumerator(new ChunkWhileIterable<E>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> collect() {
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <S> RubyLazyEnumerator<S> collect(
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(new TransformIterable<E, S>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> collectConcat() {
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <S> RubyLazyEnumerator<S> collectConcat(
      Function<? super E, ? extends List<? extends S>> block) {
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
  public int count(Predicate<? super E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.test(item)) count++;
    }
    return count;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> cycle() {
    return newRubyLazyEnumerator(new CycleIterable<E>(iter));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> cycle(int n) {
    return newRubyLazyEnumerator(new CycleIterable<E>(iter, n));
  }

  @Override
  public void cycle(int n, Consumer<? super E> block) {
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        block.accept(item);
      }
    }
  }

  @Override
  public void cycle(Consumer<? super E> block) {
    while (true) {
      for (E item : iter) {
        block.accept(item);
      }
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> detect() {
    return this;
  }

  @Override
  public E detect(Predicate<? super E> block) {
    for (E item : iter) {
      if (block.test(item)) return item;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> drop(int n) {
    return newRubyLazyEnumerator(new DropIterable<E>(iter, n));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyLazyEnumerator(rubyArray);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> dropWhile(Predicate<? super E> block) {
    return newRubyLazyEnumerator(new DropWhileIterable<E>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> each() {
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> each(Consumer<? super E> block) {
    for (E item : iter) {
      block.accept(item);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> eachCons(int n) {
    return newRubyLazyEnumerator(new EachConsIterable<E>(iter, n));
  }

  @Override
  public void eachCons(int n, Consumer<? super RubyArray<E>> block) {
    for (RubyArray<E> cons : eachCons(n)) {
      block.accept(cons);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> eachEntry() {
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> eachEntry(Consumer<? super E> block) {
    for (E item : iter) {
      block.accept(item);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> eachSlice(int n) {
    return newRubyLazyEnumerator(new EachSliceIterable<E>(iter, n));
  }

  @Override
  public void eachSlice(int n, Consumer<? super RubyArray<E>> block) {
    for (RubyArray<E> ra : new EachSliceIterable<E>(iter, n)) {
      block.accept(ra);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return newRubyLazyEnumerator(new EachWithIndexIterable<E>(iter));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> eachWithIndex(
      BiConsumer<? super E, Integer> block) {
    int i = 0;
    for (E item : iter) {
      block.accept(item, i);
      i++;
    }
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <O> RubyLazyEnumerator<Entry<E, O>> eachWithObject(O obj) {
    return newRubyLazyEnumerator(new EachWithObjectIterable<E, O>(iter, obj));
  }

  @Override
  public <O> O eachWithObject(O obj, BiConsumer<? super E, ? super O> block) {
    for (E item : iter) {
      block.accept(item, obj);
    }
    return obj;
  }

  @Override
  public RubyArray<E> entries() {
    return newRubyArray(iter);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> find() {
    return detect();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> findAll() {
    return this;
  }

  @Override
  public E find(Predicate<? super E> block) {
    return detect(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> findAll(Predicate<? super E> block) {
    return newRubyLazyEnumerator(new FindAllIterable<E>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> findIndex() {
    return this;
  }

  @Override
  public Integer findIndex(Predicate<? super E> block) {
    int index = 0;
    for (E item : iter) {
      if (block.test(item)) return index;

      index++;
    }
    return null;
  }

  @Override
  public Integer findIndex(E target) {
    int index = 0;
    for (E item : iter) {
      if (target == null ? item == null : target.equals(item)) return index;

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
    if (n < 0) throw new IllegalArgumentException(
        "ArgumentError: attempt to take negative size");

    Iterator<E> elements = iter.iterator();
    RubyArray<E> rubyArray = newRubyArray();
    for (int i = 0; i < n && elements.hasNext(); i++) {
      rubyArray.add(elements.next());
    }
    return rubyArray;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> flatMap() {
    return collectConcat();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <S> RubyLazyEnumerator<S> flatMap(
      Function<? super E, ? extends List<? extends S>> block) {
    return collectConcat(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> grep(String regex) {
    return newRubyLazyEnumerator(new GrepIterable<E>(iter, regex));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <S> RubyLazyEnumerator<S> grep(String regex,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(
        new TransformIterable<E, S>(new GrepIterable<E>(iter, regex), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> grepV(String regex) {
    return newRubyLazyEnumerator(new GrepVIterable<E>(iter, regex));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <S> RubyLazyEnumerator<S> grepV(String regex,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(
        new TransformIterable<E, S>(new GrepVIterable<E>(iter, regex), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> groupBy() {
    return this;
  }

  @Override
  public <S> RubyHash<S, RubyArray<E>> groupBy(
      Function<? super E, ? extends S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = newRubyHash();
    for (E item : iter) {
      S key = block.apply(item);
      if (!rubyHash.containsKey(key)) rubyHash.put(key, new RubyArray<E>());

      rubyHash.get(key).add(item);
    }
    return rubyHash;
  }

  @Override
  public boolean includeʔ(E target) {
    for (E item : iter) {
      if (item.equals(target)) return true;
    }
    return false;
  }

  @Override
  public E inject(BiFunction<E, E, E> block) {
    E result = null;
    Iterator<E> elements = iter.iterator();
    if (elements.hasNext()) result = elements.next();

    while (elements.hasNext()) {
      result = block.apply(result, elements.next());
    }
    return result;
  }

  @Override
  public <I> I inject(I init, BiFunction<I, ? super E, I> block) {
    for (E item : iter) {
      init = block.apply(init, item);
    }
    return init;
  }

  @Override
  public RubyLazyEnumerator<E> lazy() {
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> map() {
    return collect();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public <S> RubyLazyEnumerator<S> map(Function<? super E, ? extends S> block) {
    return collect(block);
  }

  @Override
  public E max() {
    return minmax().last();
  }

  @Override
  public E max(Comparator<? super E> comp) {
    return minmax(comp).last();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> maxBy() {
    return this;
  }

  @Override
  public <S> E maxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return minmaxBy(comp, block).last();
  }

  @Override
  public <S> E maxBy(Function<? super E, ? extends S> block) {
    return minmaxBy(block).last();
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

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> minBy() {
    return this;
  }

  @Override
  public <S> E minBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return minmaxBy(comp, block).first();
  }

  @Override
  public <S> E minBy(Function<? super E, ? extends S> block) {
    return minmaxBy(block).first();
  }

  @Override
  public RubyArray<E> minmax() {
    return minmax(null);
  }

  @Override
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    return minmaxBy(comp, item -> item);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> minmaxBy() {
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    Iterator<E> elements = iter.iterator();
    if (!elements.hasNext()) return newRubyArray(null, null);

    Comparator<S> tryComp = new TryComparator<S>(comp);
    E max = elements.next();
    E min = max;
    while (elements.hasNext()) {
      E item = elements.next();
      if (tryComp.compare(block.apply(max), block.apply(item)) < 0) max = item;
      if (tryComp.compare(block.apply(min), block.apply(item)) > 0) min = item;
    }
    return newRubyArray(min, max);
  }

  @Override
  public <S> RubyArray<E> minmaxBy(Function<? super E, ? extends S> block) {
    return minmaxBy(null, block);
  }

  @Override
  public boolean noneʔ() {
    for (E item : iter) {
      if (item != null && !Boolean.FALSE.equals(item)) return false;
    }
    return true;
  }

  @Override
  public boolean noneʔ(Predicate<? super E> block) {
    for (E item : iter) {
      if (block.test(item)) return false;
    }
    return true;
  }

  @Override
  public boolean oneʔ() {
    int count = 0;
    for (E item : iter) {
      if (item != null && !Boolean.FALSE.equals(item)) {
        count++;
        if (count > 1) return false;
      }
    }
    return count == 1;
  }

  @Override
  public boolean oneʔ(Predicate<? super E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.test(item)) {
        count++;
        if (count > 1) return false;
      }
    }
    return count == 1;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> partition() {
    return this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public RubyArray<RubyArray<E>> partition(Predicate<? super E> block) {
    RubyArray<E> trueList = newRubyArray();
    RubyArray<E> falseList = newRubyArray();
    for (E item : iter) {
      if (block.test(item))
        trueList.add(item);
      else
        falseList.add(item);
    }
    return newRubyArray(trueList, falseList);
  }

  @Override
  public E reduce(BiFunction<E, E, E> block) {
    return inject(block);
  }

  @Override
  public <S> S reduce(S init, BiFunction<S, ? super E, S> block) {
    return inject(init, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> reject() {
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> reject(Predicate<? super E> block) {
    return newRubyLazyEnumerator(new RejectIterable<E>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> reverseEach() {
    return newRubyLazyEnumerator(new ReverseEachIterable<E>(iter));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> reverseEach(Consumer<? super E> block) {
    for (E item : reverseEach()) {
      block.accept(item);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> select() {
    return findAll();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> select(Predicate<? super E> block) {
    return findAll(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> sliceAfter(
      Predicate<? super E> block) {
    return newRubyLazyEnumerator(new SliceAfterIterable<E>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> sliceAfter(String regex) {
    return newRubyLazyEnumerator(
        new SliceAfterIterable<E>(iter, Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> sliceBefore(
      Predicate<? super E> block) {
    return newRubyLazyEnumerator(new SliceBeforeIterable<E>(iter, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return newRubyLazyEnumerator(
        new SliceBeforeIterable<E>(iter, Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> sliceWhen(
      BiPredicate<? super E, ? super E> block) {
    return newRubyLazyEnumerator(new SliceWhenIterable<E>(iter, block));
  }

  @Override
  public RubyArray<E> sort() {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() <= 1) return rubyArray;

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

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> sortBy() {
    return this;
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    RubyArray<E> rubyArray = newRubyArray();
    for (S key : rubyHash.keys().sortǃ(comp)) {
      rubyArray.addAll(rubyHash.get(key));
    }
    return rubyArray;
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, Function<? super E, ? extends S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    RubyArray<E> rubyArray = newRubyArray();
    for (S key : rubyHash.keys().sortǃ(comp2)) {
      rubyArray.addAll(rubyHash.get(key).sortǃ(comp1));
    }
    return rubyArray;
  }

  @Override
  public <S> RubyArray<E> sortBy(Function<? super E, ? extends S> block) {
    RubyHash<S, RubyArray<E>> rubyHash = groupBy(block);
    RubyArray<E> rubyArray = newRubyArray();
    for (S key : rubyHash.keys().sortǃ()) {
      rubyArray.addAll(rubyHash.get(key));
    }
    return rubyArray;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> take(int n) {
    return newRubyLazyEnumerator(new TakeIterable<E>(iter, n));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      rubyArray.add(item);
      break;
    }
    return newRubyLazyEnumerator(rubyArray);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> takeWhile(Predicate<? super E> block) {
    return newRubyLazyEnumerator(new TakeWhileIterable<E>(iter, block));
  }

  @Override
  public RubyArray<E> toA() {
    return newRubyArray(iter);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <K, V> RubyHash<K, V> toH() {
    return (RubyHash<K, V>) Hash((RubyArray<? extends List<E>>) toA());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> zip(Iterable<? extends E>... others) {
    return zip(Arrays.asList(others));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<RubyArray<E>> zip(
      List<? extends Iterable<? extends E>> others) {
    return newRubyLazyEnumerator(new ZipIterable<E>(iter, others));
  }

  @Override
  public void zip(List<? extends Iterable<? extends E>> others,
      Consumer<? super RubyArray<E>> block) {
    RubyLazyEnumerator<RubyArray<E>> rubyArrays = zip(others);
    for (RubyArray<E> item : rubyArrays) {
      block.accept(item);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @return this {@link RubyLazyEnumerator}
   */
  @Override
  public RubyLazyEnumerator<E> rewind() {
    pIterator = new PeekingIterator<E>(iter.iterator());
    return this;
  }

  @Override
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
