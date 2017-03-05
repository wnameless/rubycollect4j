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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import javax.xml.bind.TypeConstraintException;

import net.sf.rubycollect4j.iter.ChunkIterable;
import net.sf.rubycollect4j.iter.ChunkWhileIterable;
import net.sf.rubycollect4j.iter.CycleIterable;
import net.sf.rubycollect4j.iter.EachConsIterable;
import net.sf.rubycollect4j.iter.EachSliceIterable;
import net.sf.rubycollect4j.iter.EachWithIndexIterable;
import net.sf.rubycollect4j.iter.EachWithObjectIterable;
import net.sf.rubycollect4j.iter.ReverseEachIterable;
import net.sf.rubycollect4j.iter.SliceAfterIterable;
import net.sf.rubycollect4j.iter.SliceBeforeIterable;
import net.sf.rubycollect4j.iter.SliceWhenIterable;

/**
 * {@link RubyEnumerable} is an extension for any Iterable class. It includes
 * all methods refer to the Enumerable module of Ruby.
 * <p>
 * {@link RubyEnumerable} is also a {@link RubyBase.Enumerable}.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public abstract class RubyEnumerable<E> implements RubyBase.Enumerable<E> {

  /**
   * Returns an Iterable of elements.
   * 
   * @return Iterable
   */
  protected abstract Iterable<E> getIterable();

  @Override
  public boolean allʔ() {
    return newRubyLazyEnumerator(getIterable()).allʔ();
  }

  @Override
  public boolean allʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).allʔ(block);
  }

  @Override
  public boolean anyʔ() {
    return newRubyLazyEnumerator(getIterable()).anyʔ();
  }

  @Override
  public boolean anyʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).anyʔ(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public <S> RubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      Function<? super E, ? extends S> block) {
    return newRubyEnumerator(new ChunkIterable<E, S>(getIterable(), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> chunkWhile(
      BiPredicate<? super E, ? super E> block) {
    return newRubyEnumerator(new ChunkWhileIterable<E>(getIterable(), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> collect() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<S> collect(Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(getIterable()).collect(block)
        .toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> collectConcat() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<S> collectConcat(
      Function<? super E, ? extends List<? extends S>> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(getIterable())
        .collectConcat(block).toA();
  }

  @Override
  public int count() {
    return newRubyLazyEnumerator(getIterable()).count();
  }

  @Override
  public int count(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).count(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> cycle() {
    return newRubyEnumerator(new CycleIterable<E>(getIterable()));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> cycle(int n) {
    return newRubyEnumerator(new CycleIterable<E>(getIterable(), n));
  }

  @Override
  public void cycle(int n, Consumer<? super E> block) {
    newRubyLazyEnumerator(getIterable()).cycle(n, block);
  }

  @Override
  public void cycle(Consumer<? super E> block) {
    newRubyLazyEnumerator(getIterable()).cycle(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> detect() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public E detect(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).detect(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> drop(int n) {
    return newRubyLazyEnumerator(getIterable()).drop(n).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : getIterable()) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> dropWhile(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).dropWhile(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> each() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public RubyEnumerable<E> each(Consumer<? super E> block) {
    newRubyEnumerator(getIterable()).each(block);
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> eachCons(int n) {
    return newRubyEnumerator(new EachConsIterable<E>(getIterable(), n));
  }

  @Override
  public void eachCons(int n, Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(getIterable()).eachCons(n, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> eachEntry() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public RubyEnumerable<E> eachEntry(Consumer<? super E> block) {
    getIterable().forEach(item -> block.accept(item));
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    return newRubyEnumerator(new EachSliceIterable<E>(getIterable(), n));
  }

  @Override
  public void eachSlice(int n, Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(getIterable()).eachSlice(n, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return newRubyEnumerator(new EachWithIndexIterable<E>(getIterable()));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public RubyEnumerable<E> eachWithIndex(BiConsumer<? super E, Integer> block) {
    newRubyLazyEnumerator(getIterable()).eachWithIndex(block);
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public <O> RubyEnumerator<Entry<E, O>> eachWithObject(O obj) {
    return newRubyEnumerator(
        new EachWithObjectIterable<E, O>(getIterable(), obj));
  }

  @Override
  public <O> O eachWithObject(O obj, BiConsumer<? super E, ? super O> block) {
    return newRubyLazyEnumerator(getIterable()).eachWithObject(obj, block);
  }

  @Override
  public RubyArray<E> entries() {
    return newRubyArray(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> find() {
    return detect();
  }

  @Override
  public E find(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).find(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> findAll() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> findAll(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).findAll(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> findIndex() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public Integer findIndex(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).findIndex(block);
  }

  @Override
  public Integer findIndex(E target) {
    return newRubyLazyEnumerator(getIterable()).findIndex(target);
  }

  @Override
  public E first() {
    return newRubyLazyEnumerator(getIterable()).first();
  }

  @Override
  public RubyArray<E> first(int n) {
    return newRubyLazyEnumerator(getIterable()).first(n);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> flatMap() {
    return collectConcat();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<S> flatMap(
      Function<? super E, ? extends List<? extends S>> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(getIterable()).flatMap(block)
        .toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> grep(String regex) {
    return newRubyLazyEnumerator(getIterable()).grep(regex).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<S> grep(String regex,
      Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(getIterable())
        .grep(regex, block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> grepV(String regex) {
    return newRubyLazyEnumerator(getIterable()).grepV(regex).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<S> grepV(String regex,
      Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(getIterable())
        .grepV(regex, block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> groupBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> RubyHash<S, RubyArray<E>> groupBy(
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).groupBy(block);
  }

  @Override
  public boolean includeʔ(E target) {
    return newRubyLazyEnumerator(getIterable()).includeʔ(target);
  }

  @Override
  public E inject(BiFunction<E, E, E> block) {
    return newRubyLazyEnumerator(getIterable()).inject(block);
  }

  @Override
  public <I> I inject(I init, BiFunction<I, ? super E, I> block) {
    return newRubyLazyEnumerator(getIterable()).inject(init, block);
  }

  @Override
  public RubyLazyEnumerator<E> lazy() {
    return new RubyLazyEnumerator<E>(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> map() {
    return collect();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public <S> RubyArray<S> map(Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(getIterable()).map(block).toA();
  }

  @Override
  public E max() {
    return newRubyLazyEnumerator(getIterable()).max();
  }

  @Override
  public E max(Comparator<? super E> comp) {
    return newRubyLazyEnumerator(getIterable()).max(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> maxBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> E maxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).maxBy(comp, block);
  }

  @Override
  public <S> E maxBy(Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).maxBy(block);
  }

  @Override
  public boolean memberʔ(E target) {
    return newRubyLazyEnumerator(getIterable()).memberʔ(target);
  }

  @Override
  public E min() {
    return newRubyLazyEnumerator(getIterable()).min();
  }

  @Override
  public E min(Comparator<? super E> comp) {
    return newRubyLazyEnumerator(getIterable()).min(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> minBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> E minBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minBy(comp, block);
  }

  @Override
  public <S> E minBy(Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minBy(block);
  }

  @Override
  public RubyArray<E> minmax() {
    return newRubyLazyEnumerator(getIterable()).minmax();
  }

  @Override
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    return newRubyLazyEnumerator(getIterable()).minmax(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> minmaxBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minmaxBy(comp, block);
  }

  @Override
  public <S> RubyArray<E> minmaxBy(Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minmaxBy(block);
  }

  @Override
  public boolean noneʔ() {
    return newRubyLazyEnumerator(getIterable()).noneʔ();
  }

  @Override
  public boolean noneʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).noneʔ(block);
  }

  @Override
  public boolean oneʔ() {
    return newRubyLazyEnumerator(getIterable()).oneʔ();
  }

  @Override
  public boolean oneʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).oneʔ(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> partition() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public RubyArray<RubyArray<E>> partition(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).partition(block);
  }

  @Override
  public E reduce(BiFunction<E, E, E> block) {
    return newRubyLazyEnumerator(getIterable()).reduce(block);
  }

  @Override
  public <I> I reduce(I init, BiFunction<I, ? super E, I> block) {
    return newRubyLazyEnumerator(getIterable()).reduce(init, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> reject() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> reject(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).reject(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> reverseEach() {
    return newRubyEnumerator(new ReverseEachIterable<E>(getIterable()));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public RubyEnumerable<E> reverseEach(Consumer<? super E> block) {
    for (E item : reverseEach()) {
      block.accept(item);
    }
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> select() {
    return findAll();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> select(Predicate<? super E> block) {
    return findAll(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> sliceAfter(Predicate<? super E> block) {
    return newRubyEnumerator(new SliceAfterIterable<E>(getIterable(), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> sliceAfter(String regex) {
    return newRubyEnumerator(
        new SliceAfterIterable<E>(getIterable(), Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> sliceBefore(Predicate<? super E> block) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(getIterable(), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return newRubyEnumerator(
        new SliceBeforeIterable<E>(getIterable(), Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> sliceWhen(
      BiPredicate<? super E, ? super E> block) {
    return newRubyEnumerator(new SliceWhenIterable<E>(getIterable(), block));
  }

  @Override
  public RubyArray<E> sort() {
    return newRubyLazyEnumerator(getIterable()).sort();
  }

  // @Override
  // public RubyArray<E> sort(Comparator<? super E> comp) {
  // return newRubyLazyEnumerator(getIterable()).sort(comp);
  // }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> sortBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(comp, block);
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(comp1, comp2, block);
  }

  @Override
  public <S> RubyArray<E> sortBy(Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(block);
  }

  @Override
  public BigDecimal sum() {
    BigDecimal sum = new BigDecimal(0);
    for (E item : getIterable()) {
      if (item instanceof Number) {
        Number num = (Number) item;
        sum.add(new BigDecimal(num.toString()));
      } else {
        String type = item == null ? "null" : item.getClass().getSimpleName();
        throw new TypeConstraintException(
            "TypeError: " + type + " can't be coerced into Number");
      }
    }
    return sum;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> take(int n) {
    return newRubyLazyEnumerator(getIterable()).take(n).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : getIterable()) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<E> takeWhile(Predicate<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).takeWhile(block).toA();
  }

  @Override
  public RubyArray<E> toA() {
    return newRubyArray(getIterable());
  }

  @Override
  public <K, V> RubyHash<K, V> toH(Function<E, Entry<K, V>> block) {
    return newRubyLazyEnumerator(getIterable()).toH(block);
  }

  @Override
  public <K, V> RubyHash<K, V> toH(BiFunction<E, E, Entry<K, V>> block) {
    return newRubyLazyEnumerator(getIterable()).toH(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SafeVarargs
  @Override
  public final RubyArray<RubyArray<E>> zip(Iterable<? extends E>... others) {
    return newRubyLazyEnumerator(getIterable()).zip(others).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<RubyArray<E>> zip(
      List<? extends Iterable<? extends E>> others) {
    return newRubyLazyEnumerator(getIterable()).zip(others).toA();
  }

  @Override
  public void zip(List<? extends Iterable<? extends E>> others,
      Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(getIterable()).zip(others, block);
  }

  @Override
  public Iterator<E> iterator() {
    return getIterable().iterator();
  }

  @Override
  public String toString() {
    return "RubyEnumerable{" + getIterable() + "}";
  }

}
