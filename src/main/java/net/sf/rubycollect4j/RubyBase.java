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

import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * {@link RubyBase} contains the general interfaces of RubyCollect4J
 * implementations.
 * <P>
 * For example, {@link RubyArray}, {@link RubyHash}, {@link RubySet},
 * {@link RubyEnumerator} and {@link RubyString} can all be treated as
 * {@link RubyBase.Enumerable}.
 *
 * @author Wei-Ming Wu
 *
 */
final class RubyBase {

  private RubyBase() {}

  /**
   * 
   * {@link RubyBase.Enumerable} includes all methods refer to the Enumerable
   * module of Ruby.
   *
   * @param <E>
   *          the type of the elements
   */
  interface Enumerable<E>
      extends RubyContract.Enumerable<E, RubyBase.Enumerator<?>, RubyArray<?>> {

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    <S> RubyBase.Enumerator<Entry<S, RubyArray<E>>> chunk(
        Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> chunkWhile(
        BiPredicate<? super E, ? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> collect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    <S> RubyArray<S> collect(Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> collectConcat();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    <S> RubyArray<S> collectConcat(
        Function<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> cycle();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> cycle(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> detect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> drop(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> dropWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> dropWhile(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> each();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    RubyBase.Enumerable<E> each(Consumer<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> eachCons(int n);

    @Override
    void eachCons(int n, Consumer<? super RubyArray<E>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> eachEntry();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    RubyBase.Enumerable<E> eachEntry(Consumer<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> eachSlice(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<Entry<E, Integer>> eachWithIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    RubyBase.Enumerable<E> eachWithIndex(BiConsumer<? super E, Integer> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    <O> RubyBase.Enumerator<Entry<E, O>> eachWithObject(O obj);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> find();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> findAll();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> findAll(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> findIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> flatMap();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    <S> RubyArray<S> flatMap(
        Function<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> grep(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    <S> RubyArray<S> grep(String regex, Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> grepV(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    <S> RubyArray<S> grepV(String regex,
        Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> groupBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> map();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    <S> RubyArray<S> map(Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> maxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> minBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> minmaxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> partition();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> reject();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> reject(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> reverseEach();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    RubyBase.Enumerable<E> reverseEach(Consumer<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> select();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> select(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> sliceAfter(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> sliceAfter(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> sliceBefore(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> sliceBefore(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<RubyArray<E>> sliceWhen(
        BiPredicate<? super E, ? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> sortBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> take(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> takeWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<E> takeWhile(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    RubyArray<RubyArray<E>> zip(List<? extends Iterable<? extends E>> others);

  }

  /**
   * 
   * {@link RubyBase.Enumerator} includes all methods refer to the Enumerator
   * class of Ruby.
   *
   * @param <E>
   *          the type of the elements
   */
  interface Enumerator<E> extends Enumerable<E>,
      RubyContract.Enumerator<E, RubyBase.Enumerator<?>, RubyArray<?>> {

    /**
     * {@inheritDoc}
     * 
     * @return this {@link RubyBase.Enumerator}
     */
    @Override
    RubyBase.Enumerator<E> rewind();

  }

  /**
   * 
   * {@link RubyBase.LazyEnumerator} includes all methods refer to the
   * LazyEnumerator class of Ruby.
   *
   * @param <E>
   *          the type of the elements
   */
  interface LazyEnumerator<E> extends
      RubyContract.Enumerator<E, RubyBase.LazyEnumerator<?>, RubyBase.LazyEnumerator<?>> {

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    <S> RubyBase.LazyEnumerator<Entry<S, RubyArray<E>>> chunk(
        Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> collect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    <S> RubyBase.LazyEnumerator<S> collect(
        Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> collectConcat();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    <S> RubyBase.LazyEnumerator<S> collectConcat(
        Function<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> cycle();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> cycle(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> detect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> drop(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> dropWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> dropWhile(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> each();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> each(Consumer<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<RubyArray<E>> eachCons(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> eachEntry();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> eachEntry(Consumer<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<RubyArray<E>> eachSlice(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<Entry<E, Integer>> eachWithIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> eachWithIndex(
        BiConsumer<? super E, Integer> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    <O> RubyBase.LazyEnumerator<Entry<E, O>> eachWithObject(O obj);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> find();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> findAll();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> findAll(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> findIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> flatMap();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    <S> RubyBase.LazyEnumerator<S> flatMap(
        Function<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> grep(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    <S> RubyBase.LazyEnumerator<S> grep(String regex,
        Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> groupBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> map();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    <S> RubyBase.LazyEnumerator<S> map(Function<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> maxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> minBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> minmaxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> partition();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> reject();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> reject(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> reverseEach();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> reverseEach(Consumer<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> select();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> select(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<RubyArray<E>> sliceBefore(
        Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<RubyArray<E>> sliceBefore(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> sortBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> take(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> takeWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> takeWhile(Predicate<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<RubyArray<E>> zip(
        List<? extends Iterable<? extends E>> others);

    /**
     * {@inheritDoc}
     * 
     * @return this {@link RubyBase.LazyEnumerator}
     */
    @Override
    RubyBase.LazyEnumerator<E> rewind();

  }

}
