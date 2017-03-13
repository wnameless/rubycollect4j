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
public interface RubyEnumerable<E> extends RubyBase.Enumerable<E> {

  @Override
  public default boolean allʔ() {
    return newRubyLazyEnumerator(this).allʔ();
  }

  @Override
  public default boolean allʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).allʔ(block);
  }

  @Override
  public default boolean anyʔ() {
    return newRubyLazyEnumerator(this).anyʔ();
  }

  @Override
  public default boolean anyʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).anyʔ(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default <S> RubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      Function<? super E, ? extends S> block) {
    return newRubyEnumerator(new ChunkIterable<E, S>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> chunkWhile(
      BiPredicate<? super E, ? super E> block) {
    return newRubyEnumerator(new ChunkWhileIterable<E>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> collect() {
    return newRubyEnumerator(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public default <S> RubyArray<S> collect(
      Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(this).collect(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> collectConcat() {
    return newRubyEnumerator(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public default <S> RubyArray<S> collectConcat(
      Function<? super E, ? extends List<? extends S>> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(this).collectConcat(block)
        .toA();
  }

  @Override
  public default int count() {
    return newRubyLazyEnumerator(this).count();
  }

  @Override
  public default int count(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).count(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> cycle() {
    return newRubyEnumerator(new CycleIterable<E>(this));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> cycle(int n) {
    return newRubyEnumerator(new CycleIterable<E>(this, n));
  }

  @Override
  public default void cycle(int n, Consumer<? super E> block) {
    newRubyLazyEnumerator(this).cycle(n, block);
  }

  @Override
  public default void cycle(Consumer<? super E> block) {
    newRubyLazyEnumerator(this).cycle(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> detect() {
    return newRubyEnumerator(this);
  }

  @Override
  public default E detect(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).detect(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<E> drop(int n) {
    return newRubyLazyEnumerator(this).drop(n).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : this) {
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
  public default RubyArray<E> dropWhile(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).dropWhile(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> each() {
    return newRubyEnumerator(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public default RubyEnumerable<E> each(Consumer<? super E> block) {
    newRubyEnumerator(this).each(block);
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> eachCons(int n) {
    return newRubyEnumerator(new EachConsIterable<E>(this, n));
  }

  @Override
  public default void eachCons(int n, Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(this).eachCons(n, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> eachEntry() {
    return newRubyEnumerator(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public default RubyEnumerable<E> eachEntry(Consumer<? super E> block) {
    this.forEach(item -> block.accept(item));
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    return newRubyEnumerator(new EachSliceIterable<E>(this, n));
  }

  @Override
  public default void eachSlice(int n, Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(this).eachSlice(n, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return newRubyEnumerator(new EachWithIndexIterable<E>(this));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public default RubyEnumerable<E> eachWithIndex(
      BiConsumer<? super E, Integer> block) {
    newRubyLazyEnumerator(this).eachWithIndex(block);
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default <O> RubyEnumerator<Entry<E, O>> eachWithObject(O obj) {
    return newRubyEnumerator(new EachWithObjectIterable<E, O>(this, obj));
  }

  @Override
  public default <O> O eachWithObject(O obj,
      BiConsumer<? super E, ? super O> block) {
    return newRubyLazyEnumerator(this).eachWithObject(obj, block);
  }

  @Override
  public default RubyArray<E> entries() {
    return newRubyArray(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> find() {
    return detect();
  }

  @Override
  public default E find(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).find(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> findAll() {
    return newRubyEnumerator(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<E> findAll(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).findAll(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> findIndex() {
    return newRubyEnumerator(this);
  }

  @Override
  public default Integer findIndex(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).findIndex(block);
  }

  @Override
  public default Integer findIndex(E target) {
    return newRubyLazyEnumerator(this).findIndex(target);
  }

  @Override
  public default E first() {
    return newRubyLazyEnumerator(this).first();
  }

  @Override
  public default RubyArray<E> first(int n) {
    return newRubyLazyEnumerator(this).first(n);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> flatMap() {
    return collectConcat();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public default <S> RubyArray<S> flatMap(
      Function<? super E, ? extends List<? extends S>> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(this).flatMap(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<E> grep(String regex) {
    return newRubyLazyEnumerator(this).grep(regex).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public default <S> RubyArray<S> grep(String regex,
      Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(this).grep(regex, block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<E> grepV(String regex) {
    return newRubyLazyEnumerator(this).grepV(regex).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public default <S> RubyArray<S> grepV(String regex,
      Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(this).grepV(regex, block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> groupBy() {
    return newRubyEnumerator(this);
  }

  @Override
  public default <S> RubyHash<S, RubyArray<E>> groupBy(
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).groupBy(block);
  }

  @Override
  public default boolean includeʔ(E target) {
    return newRubyLazyEnumerator(this).includeʔ(target);
  }

  @Override
  public default E inject(BiFunction<E, E, E> block) {
    return newRubyLazyEnumerator(this).inject(block);
  }

  @Override
  public default <I> I inject(I init, BiFunction<I, ? super E, I> block) {
    return newRubyLazyEnumerator(this).inject(init, block);
  }

  @Override
  public default RubyLazyEnumerator<E> lazy() {
    return new RubyLazyEnumerator<E>(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> map() {
    return collect();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  public default <S> RubyArray<S> map(Function<? super E, ? extends S> block) {
    return (RubyArray<S>) newRubyLazyEnumerator(this).map(block).toA();
  }

  @Override
  public default E max() {
    return newRubyLazyEnumerator(this).max();
  }

  @Override
  public default E max(Comparator<? super E> comp) {
    return newRubyLazyEnumerator(this).max(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> maxBy() {
    return newRubyEnumerator(this);
  }

  @Override
  public default <S> E maxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).maxBy(comp, block);
  }

  @Override
  public default <S> E maxBy(Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).maxBy(block);
  }

  @Override
  public default boolean memberʔ(E target) {
    return newRubyLazyEnumerator(this).memberʔ(target);
  }

  @Override
  public default E min() {
    return newRubyLazyEnumerator(this).min();
  }

  @Override
  public default E min(Comparator<? super E> comp) {
    return newRubyLazyEnumerator(this).min(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> minBy() {
    return newRubyEnumerator(this);
  }

  @Override
  public default <S> E minBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).minBy(comp, block);
  }

  @Override
  public default <S> E minBy(Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).minBy(block);
  }

  @Override
  public default RubyArray<E> minmax() {
    return newRubyLazyEnumerator(this).minmax();
  }

  @Override
  public default RubyArray<E> minmax(Comparator<? super E> comp) {
    return newRubyLazyEnumerator(this).minmax(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> minmaxBy() {
    return newRubyEnumerator(this);
  }

  @Override
  public default <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).minmaxBy(comp, block);
  }

  @Override
  public default <S> RubyArray<E> minmaxBy(
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).minmaxBy(block);
  }

  @Override
  public default boolean noneʔ() {
    return newRubyLazyEnumerator(this).noneʔ();
  }

  @Override
  public default boolean noneʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).noneʔ(block);
  }

  @Override
  public default boolean oneʔ() {
    return newRubyLazyEnumerator(this).oneʔ();
  }

  @Override
  public default boolean oneʔ(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).oneʔ(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> partition() {
    return newRubyEnumerator(this);
  }

  @Override
  public default RubyArray<RubyArray<E>> partition(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).partition(block);
  }

  @Override
  public default E reduce(BiFunction<E, E, E> block) {
    return newRubyLazyEnumerator(this).reduce(block);
  }

  @Override
  public default <I> I reduce(I init, BiFunction<I, ? super E, I> block) {
    return newRubyLazyEnumerator(this).reduce(init, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> reject() {
    return newRubyEnumerator(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<E> reject(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).reject(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> reverseEach() {
    return newRubyEnumerator(new ReverseEachIterable<E>(this));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  public default RubyEnumerable<E> reverseEach(Consumer<? super E> block) {
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
  public default RubyEnumerator<E> select() {
    return findAll();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<E> select(Predicate<? super E> block) {
    return findAll(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> sliceAfter(
      Predicate<? super E> block) {
    return newRubyEnumerator(new SliceAfterIterable<E>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> sliceAfter(String regex) {
    return newRubyEnumerator(
        new SliceAfterIterable<E>(this, Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> sliceBefore(
      Predicate<? super E> block) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return newRubyEnumerator(
        new SliceBeforeIterable<E>(this, Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<RubyArray<E>> sliceWhen(
      BiPredicate<? super E, ? super E> block) {
    return newRubyEnumerator(new SliceWhenIterable<E>(this, block));
  }

  @Override
  public default RubyArray<E> sort() {
    return newRubyLazyEnumerator(this).sort();
  }

  // @Override
  // public default RubyArray<E> sort(Comparator<? super E> comp) {
  // return newRubyLazyEnumerator(this).sort(comp);
  // }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> sortBy() {
    return newRubyEnumerator(this);
  }

  @Override
  public default <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).sortBy(comp, block);
  }

  @Override
  public default <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).sortBy(comp1, comp2, block);
  }

  @Override
  public default <S> RubyArray<E> sortBy(
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(this).sortBy(block);
  }

  @Override
  public default BigDecimal sum() {
    BigDecimal sum = new BigDecimal(0);
    for (E item : this) {
      if (item instanceof Number) {
        Number num = (Number) item;
        sum = sum.add(new BigDecimal(num.toString()));
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
  public default RubyArray<E> take(int n) {
    return newRubyLazyEnumerator(this).take(n).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public default RubyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : this) {
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
  public default RubyArray<E> takeWhile(Predicate<? super E> block) {
    return newRubyLazyEnumerator(this).takeWhile(block).toA();
  }

  @Override
  public default RubyArray<E> toA() {
    return newRubyArray(this);
  }

  @Override
  public default <K, V> RubyHash<K, V> toH(Function<E, Entry<K, V>> block) {
    return newRubyLazyEnumerator(this).toH(block);
  }

  @Override
  public default <K, V> RubyHash<K, V> toH(
      BiFunction<E, E, Entry<K, V>> block) {
    return newRubyLazyEnumerator(this).toH(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<RubyArray<E>> zip(
      @SuppressWarnings("unchecked") Iterable<? extends E>... others) {
    return newRubyLazyEnumerator(this).zip(others).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public default RubyArray<RubyArray<E>> zip(
      List<? extends Iterable<? extends E>> others) {
    return newRubyLazyEnumerator(this).zip(others).toA();
  }

  @Override
  public default void zip(List<? extends Iterable<? extends E>> others,
      Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(this).zip(others, block);
  }

}
