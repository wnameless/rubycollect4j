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

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.EntryBooleanBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;

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
    public <S> RubyBase.Enumerator<Entry<S, RubyArray<E>>> chunk(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyBase.Enumerator<Entry<S, RubyArray<E>>> chunk(
        final String methodName, final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> chunkWhile(
        EntryBooleanBlock<? super E, ? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> collect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public <S> RubyArray<S> collect(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyArray<S> collect(final String methodName,
        final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> collectConcat();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public <S> RubyArray<S> collectConcat(
        TransformBlock<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> cycle();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> cycle(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> detect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> drop(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> dropWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> dropWhile(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> each();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    public RubyBase.Enumerable<E> each(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> eachCons(int n);

    @Override
    public void eachCons(int n, Block<? super RubyArray<E>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> eachEntry();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    public RubyBase.Enumerable<E> eachEntry(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> eachSlice(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<Entry<E, Integer>> eachWithIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    public RubyBase.Enumerable<E> eachWithIndex(
        WithIndexBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public <O> RubyBase.Enumerator<Entry<E, O>> eachWithObject(O obj);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> find();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> findAll();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> findAll(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> findIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> flatMap();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public <S> RubyArray<S> flatMap(
        TransformBlock<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> grep(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public <S> RubyArray<S> grep(String regex,
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyArray<S> grep(String regex, final String methodName,
        final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> grepV(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public <S> RubyArray<S> grepV(String regex,
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> groupBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> map();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public <S> RubyArray<S> map(TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyArray<S> map(String methodName, Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> maxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> minBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> minmaxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> partition();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> reject();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> reject(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> reverseEach();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerable}
     */
    @Override
    public RubyBase.Enumerable<E> reverseEach(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> select();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> select(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> sliceAfter(
        BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> sliceAfter(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> sliceBefore(
        BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> sliceBefore(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<RubyArray<E>> sliceWhen(
        EntryBooleanBlock<? super E, ? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> sortBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> take(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.Enumerator}
     */
    @Override
    public RubyBase.Enumerator<E> takeWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<E> takeWhile(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<RubyArray<E>> zip(Iterable<? extends E>... others);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyArray}
     */
    @Override
    public RubyArray<RubyArray<E>> zip(
        List<? extends Iterable<? extends E>> others);

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
    public RubyBase.Enumerator<E> rewind();

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
    public <S> RubyBase.LazyEnumerator<Entry<S, RubyArray<E>>> chunk(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyBase.LazyEnumerator<Entry<S, RubyArray<E>>> chunk(
        final String methodName, final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> collect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public <S> RubyBase.LazyEnumerator<S> collect(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyBase.LazyEnumerator<S> collect(final String methodName,
        final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> collectConcat();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public <S> RubyBase.LazyEnumerator<S> collectConcat(
        TransformBlock<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> cycle();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> cycle(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> detect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> drop(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> dropWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> dropWhile(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> each();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> each(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<RubyArray<E>> eachCons(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> eachEntry();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> eachEntry(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<RubyArray<E>> eachSlice(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<Entry<E, Integer>> eachWithIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> eachWithIndex(
        WithIndexBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public <O> RubyBase.LazyEnumerator<Entry<E, O>> eachWithObject(O obj);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> find();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> findAll();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> findAll(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> findIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> flatMap();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public <S> RubyBase.LazyEnumerator<S> flatMap(
        TransformBlock<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> grep(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public <S> RubyBase.LazyEnumerator<S> grep(String regex,
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyBase.LazyEnumerator<S> grep(String regex,
        final String methodName, final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> groupBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> map();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public <S> RubyBase.LazyEnumerator<S> map(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     * 
     * @deprecated since 1.9.0, using Java 8 Lambda instead
     */
    @Deprecated
    @Override
    public <S> RubyBase.LazyEnumerator<S> map(String methodName,
        Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> maxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> minBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> minmaxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> partition();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> reject();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> reject(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> reverseEach();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> reverseEach(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> select();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> select(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<RubyArray<E>> sliceBefore(
        BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<RubyArray<E>> sliceBefore(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> sortBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> take(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> takeWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> takeWhile(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<RubyArray<E>> zip(
        Iterable<? extends E>... others);

    /**
     * {@inheritDoc}
     * 
     * @return {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<RubyArray<E>> zip(
        List<? extends Iterable<? extends E>> others);

    /**
     * {@inheritDoc}
     * 
     * @return this {@link RubyBase.LazyEnumerator}
     */
    @Override
    public RubyBase.LazyEnumerator<E> rewind();

  }

}
