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
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;

/**
 * 
 * {@link Ruby} contains the general interfaces of RubyCollect4J
 * implementations.
 * <P>
 * For example, {@link RubyArray}, {@link RubyHash}, {@link RubySet},
 * {@link RubyEnumerator} and {@link RubyString} can all be treated as
 * {@link Ruby.Enumerable}.
 *
 * @author Wei-Ming Wu
 *
 */
public final class Ruby {

  private Ruby() {}

  /**
   * 
   * {@link Ruby.Enumerable} includes all methods refer to the Enumerable module
   * of Ruby.
   *
   * @param <E>
   *          the type of the elements
   */
  public interface Enumerable<E>
      extends RubyContract.Enumerable<E, Ruby.Enumerator<?>, RubyArray<?>> {

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public <S> Ruby.Enumerator<Entry<S, RubyArray<E>>> chunk(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public <S> Ruby.Enumerator<Entry<S, RubyArray<E>>> chunk(
        final String methodName, final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> collect();

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
     */
    @Override
    public <S> RubyArray<S> collect(final String methodName,
        final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> collectConcat();

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
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> cycle();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> cycle(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> detect();

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
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> dropWhile();

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
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> each();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerable}
     */
    @Override
    public Ruby.Enumerable<E> each(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<RubyArray<E>> eachCons(int n);

    @Override
    public void eachCons(int n, Block<? super RubyArray<E>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> eachEntry();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerable}
     */
    @Override
    public Ruby.Enumerable<E> eachEntry(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<RubyArray<E>> eachSlice(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<Entry<E, Integer>> eachWithIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerable}
     */
    @Override
    public Ruby.Enumerable<E> eachWithIndex(WithIndexBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public <O> Ruby.Enumerator<Entry<E, O>> eachWithObject(O obj);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> find();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> findAll();

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
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> findIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> flatMap();

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
     */
    @Override
    public <S> RubyArray<S> grep(String regex, final String methodName,
        final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> groupBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> map();

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
     */
    @Override
    public <S> RubyArray<S> map(String methodName, Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> maxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> minBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> minmaxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> partition();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> reject();

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
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> reverseEach();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerable}
     */
    @Override
    public Ruby.Enumerable<E> reverseEach(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> select();

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
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<RubyArray<E>> sliceBefore(
        BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<RubyArray<E>> sliceBefore(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> sortBy();

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
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> takeWhile();

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
   * {@link Ruby.Enumerator} includes all methods refer to the Enumerator class
   * of Ruby.
   *
   * @param <E>
   *          the type of the elements
   */
  public interface Enumerator<E> extends Enumerable<E>,
      RubyContract.Enumerator<E, Ruby.Enumerator<?>, RubyArray<?>> {

    /**
     * {@inheritDoc}
     * 
     * @return this {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> rewind();

  }

  /**
   * 
   * {@link Ruby.LazyEnumerator} includes all methods refer to the
   * LazyEnumerator class of Ruby.
   *
   * @param <E>
   *          the type of the elements
   */
  public interface LazyEnumerator<E> extends
      RubyContract.Enumerator<E, Ruby.LazyEnumerator<?>, Ruby.LazyEnumerator<?>> {

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<Entry<S, RubyArray<E>>> chunk(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<Entry<S, RubyArray<E>>> chunk(
        final String methodName, final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> collect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> collect(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> collect(final String methodName,
        final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> collectConcat();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> collectConcat(
        TransformBlock<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> cycle();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> cycle(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> detect();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> drop(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> dropWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> dropWhile(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> each();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> each(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<RubyArray<E>> eachCons(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> eachEntry();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> eachEntry(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<RubyArray<E>> eachSlice(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<Entry<E, Integer>> eachWithIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> eachWithIndex(
        WithIndexBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <O> Ruby.LazyEnumerator<Entry<E, O>> eachWithObject(O obj);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> find();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> findAll();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> findAll(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> findIndex();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> flatMap();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> flatMap(
        TransformBlock<? super E, ? extends List<? extends S>> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> grep(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> grep(String regex,
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> grep(String regex,
        final String methodName, final Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> groupBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> map();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> map(
        TransformBlock<? super E, ? extends S> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public <S> Ruby.LazyEnumerator<S> map(String methodName, Object... args);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> maxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> minBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> minmaxBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> partition();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> reject();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> reject(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> reverseEach();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> reverseEach(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> select();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> select(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<RubyArray<E>> sliceBefore(
        BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<RubyArray<E>> sliceBefore(String regex);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> sortBy();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> take(int n);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> takeWhile();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> takeWhile(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<RubyArray<E>> zip(
        Iterable<? extends E>... others);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<RubyArray<E>> zip(
        List<? extends Iterable<? extends E>> others);

    /**
     * {@inheritDoc}
     * 
     * @return this {@link Ruby.LazyEnumerator}
     */
    @Override
    public Ruby.LazyEnumerator<E> rewind();

  }

}
