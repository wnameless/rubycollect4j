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
  default boolean allʔ() {
    return Ruby.LazyEnumerator.of(this).allʔ();
  }

  @Override
  default boolean allʔ(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).allʔ(block);
  }

  @Override
  default boolean anyʔ() {
    return Ruby.LazyEnumerator.of(this).anyʔ();
  }

  @Override
  default boolean anyʔ(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).anyʔ(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default <S> RubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      Function<? super E, ? extends S> block) {
    return Ruby.Enumerator.of(new ChunkIterable<E, S>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> chunkWhile(
      BiPredicate<? super E, ? super E> block) {
    return Ruby.Enumerator.of(new ChunkWhileIterable<>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> collect() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  default <S> RubyArray<S> collect(Function<? super E, ? extends S> block) {
    return (RubyArray<S>) Ruby.LazyEnumerator.of(this).collect(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> collectConcat() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  default <S> RubyArray<S> collectConcat(
      Function<? super E, ? extends List<? extends S>> block) {
    return (RubyArray<S>) Ruby.LazyEnumerator.of(this).collectConcat(block)
        .toA();
  }

  @Override
  default int count() {
    return Ruby.LazyEnumerator.of(this).count();
  }

  @Override
  default int count(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).count(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> cycle() {
    return Ruby.Enumerator.of(new CycleIterable<>(this));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> cycle(int n) {
    return Ruby.Enumerator.of(new CycleIterable<>(this, n));
  }

  @Override
  default void cycle(int n, Consumer<? super E> block) {
    Ruby.LazyEnumerator.of(this).cycle(n, block);
  }

  @Override
  default void cycle(Consumer<? super E> block) {
    Ruby.LazyEnumerator.of(this).cycle(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> detect() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default E detect(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).detect(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> drop(int n) {
    return Ruby.LazyEnumerator.of(this).drop(n).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = Ruby.Array.create();
    for (E item : this) {
      rubyArray.add(item);
      break;
    }
    return Ruby.Enumerator.of(rubyArray);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> dropWhile(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).dropWhile(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> each() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  default RubyEnumerable<E> each(Consumer<? super E> block) {
    Ruby.Enumerator.of(this).each(block);
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> eachCons(int n) {
    return Ruby.Enumerator.of(new EachConsIterable<>(this, n));
  }

  @Override
  default void eachCons(int n, Consumer<? super RubyArray<E>> block) {
    Ruby.LazyEnumerator.of(this).eachCons(n, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> eachEntry() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  default RubyEnumerable<E> eachEntry(Consumer<? super E> block) {
    this.forEach(item -> block.accept(item));
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    return Ruby.Enumerator.of(new EachSliceIterable<>(this, n));
  }

  @Override
  default void eachSlice(int n, Consumer<? super RubyArray<E>> block) {
    Ruby.LazyEnumerator.of(this).eachSlice(n, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return Ruby.Enumerator.of(new EachWithIndexIterable<>(this));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  default RubyEnumerable<E> eachWithIndex(
      BiConsumer<? super E, Integer> block) {
    Ruby.LazyEnumerator.of(this).eachWithIndex(block);
    return this;
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default <O> RubyEnumerator<Entry<E, O>> eachWithObject(O obj) {
    return Ruby.Enumerator.of(new EachWithObjectIterable<>(this, obj));
  }

  @Override
  default <O> O eachWithObject(O obj, BiConsumer<? super E, ? super O> block) {
    return Ruby.LazyEnumerator.of(this).eachWithObject(obj, block);
  }

  @Override
  default RubyArray<E> entries() {
    return Ruby.Array.copyOf(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> find() {
    return detect();
  }

  @Override
  default E find(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).find(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> findAll() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> findAll(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).findAll(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> findIndex() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default Integer findIndex(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).findIndex(block);
  }

  @Override
  default Integer findIndex(E target) {
    return Ruby.LazyEnumerator.of(this).findIndex(target);
  }

  @Override
  default E first() {
    return Ruby.LazyEnumerator.of(this).first();
  }

  @Override
  default RubyArray<E> first(int n) {
    return Ruby.LazyEnumerator.of(this).first(n);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> flatMap() {
    return collectConcat();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  default <S> RubyArray<S> flatMap(
      Function<? super E, ? extends List<? extends S>> block) {
    return (RubyArray<S>) Ruby.LazyEnumerator.of(this).flatMap(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> grep(String regex) {
    return Ruby.LazyEnumerator.of(this).grep(regex).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  default <S> RubyArray<S> grep(String regex,
      Function<? super E, ? extends S> block) {
    return (RubyArray<S>) Ruby.LazyEnumerator.of(this).grep(regex, block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> grepV(String regex) {
    return Ruby.LazyEnumerator.of(this).grepV(regex).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  default <S> RubyArray<S> grepV(String regex,
      Function<? super E, ? extends S> block) {
    return (RubyArray<S>) Ruby.LazyEnumerator.of(this).grepV(regex, block)
        .toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> groupBy() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default <S> RubyHash<S, RubyArray<E>> groupBy(
      Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).groupBy(block);
  }

  @Override
  default boolean includeʔ(E target) {
    return Ruby.LazyEnumerator.of(this).includeʔ(target);
  }

  @Override
  default E inject(BiFunction<E, E, E> block) {
    return Ruby.LazyEnumerator.of(this).inject(block);
  }

  @Override
  default <I> I inject(I init, BiFunction<I, ? super E, I> block) {
    return Ruby.LazyEnumerator.of(this).inject(init, block);
  }

  @Override
  default RubyLazyEnumerator<E> lazy() {
    return new RubyLazyEnumerator<>(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> map() {
    return collect();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @SuppressWarnings("unchecked")
  @Override
  default <S> RubyArray<S> map(Function<? super E, ? extends S> block) {
    return (RubyArray<S>) Ruby.LazyEnumerator.of(this).map(block).toA();
  }

  @Override
  default E max() {
    return Ruby.LazyEnumerator.of(this).max();
  }

  @Override
  default E max(Comparator<? super E> comp) {
    return Ruby.LazyEnumerator.of(this).max(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> maxBy() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default <S> E maxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).maxBy(comp, block);
  }

  @Override
  default <S> E maxBy(Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).maxBy(block);
  }

  @Override
  default boolean memberʔ(E target) {
    return Ruby.LazyEnumerator.of(this).memberʔ(target);
  }

  @Override
  default E min() {
    return Ruby.LazyEnumerator.of(this).min();
  }

  @Override
  default E min(Comparator<? super E> comp) {
    return Ruby.LazyEnumerator.of(this).min(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> minBy() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default <S> E minBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).minBy(comp, block);
  }

  @Override
  default <S> E minBy(Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).minBy(block);
  }

  @Override
  default RubyArray<E> minmax() {
    return Ruby.LazyEnumerator.of(this).minmax();
  }

  @Override
  default RubyArray<E> minmax(Comparator<? super E> comp) {
    return Ruby.LazyEnumerator.of(this).minmax(comp);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> minmaxBy() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).minmaxBy(comp, block);
  }

  @Override
  default <S> RubyArray<E> minmaxBy(Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).minmaxBy(block);
  }

  @Override
  default boolean noneʔ() {
    return Ruby.LazyEnumerator.of(this).noneʔ();
  }

  @Override
  default boolean noneʔ(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).noneʔ(block);
  }

  @Override
  default boolean oneʔ() {
    return Ruby.LazyEnumerator.of(this).oneʔ();
  }

  @Override
  default boolean oneʔ(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).oneʔ(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> partition() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default RubyArray<RubyArray<E>> partition(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).partition(block);
  }

  @Override
  default E reduce(BiFunction<E, E, E> block) {
    return Ruby.LazyEnumerator.of(this).reduce(block);
  }

  @Override
  default <I> I reduce(I init, BiFunction<I, ? super E, I> block) {
    return Ruby.LazyEnumerator.of(this).reduce(init, block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> reject() {
    return Ruby.Enumerator.of(this);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> reject(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).reject(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> reverseEach() {
    return Ruby.Enumerator.of(new ReverseEachIterable<>(this));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerable}
   */
  @Override
  default RubyEnumerable<E> reverseEach(Consumer<? super E> block) {
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
  default RubyEnumerator<E> select() {
    return findAll();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> select(Predicate<? super E> block) {
    return findAll(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> sliceAfter(Predicate<? super E> block) {
    return Ruby.Enumerator.of(new SliceAfterIterable<>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> sliceAfter(String regex) {
    return Ruby.Enumerator
        .of(new SliceAfterIterable<>(this, Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> sliceBefore(Predicate<? super E> block) {
    return Ruby.Enumerator.of(new SliceBeforeIterable<>(this, block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return Ruby.Enumerator
        .of(new SliceBeforeIterable<>(this, Pattern.compile(regex)));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<RubyArray<E>> sliceWhen(
      BiPredicate<? super E, ? super E> block) {
    return Ruby.Enumerator.of(new SliceWhenIterable<>(this, block));
  }

  @Override
  default RubyArray<E> sort() {
    return Ruby.LazyEnumerator.of(this).sort();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> sortBy() {
    return Ruby.Enumerator.of(this);
  }

  @Override
  default <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).sortBy(comp, block);
  }

  @Override
  default <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).sortBy(comp1, comp2, block);
  }

  @Override
  default <S> RubyArray<E> sortBy(Function<? super E, ? extends S> block) {
    return Ruby.LazyEnumerator.of(this).sortBy(block);
  }

  @Override
  default BigDecimal sum() {
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
  default RubyArray<E> take(int n) {
    return Ruby.LazyEnumerator.of(this).take(n).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  default RubyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = Ruby.Array.create();
    for (E item : this) {
      rubyArray.add(item);
      break;
    }
    return Ruby.Enumerator.of(rubyArray);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<E> takeWhile(Predicate<? super E> block) {
    return Ruby.LazyEnumerator.of(this).takeWhile(block).toA();
  }

  @Override
  default RubyArray<E> toA() {
    return Ruby.Array.copyOf(this);
  }

  @Override
  default <K, V> RubyHash<K, V> toH(Function<E, Entry<K, V>> block) {
    return Ruby.LazyEnumerator.of(this).toH(block);
  }

  @Override
  default <K, V> RubyHash<K, V> toH(BiFunction<E, E, Entry<K, V>> block) {
    return Ruby.LazyEnumerator.of(this).toH(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  default RubyArray<RubyArray<E>> zip(
      List<? extends Iterable<? extends E>> others) {
    return Ruby.LazyEnumerator.of(this).zip(others).toA();
  }

  @Override
  default void zip(List<? extends Iterable<? extends E>> others,
      Consumer<? super RubyArray<E>> block) {
    Ruby.LazyEnumerator.of(this).zip(others, block);
  }

}
