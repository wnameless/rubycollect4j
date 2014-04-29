/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2014 Wei-Ming Wu
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

import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.ReduceBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;
import net.sf.rubycollect4j.block.WithInitBlock;
import net.sf.rubycollect4j.block.WithObjectBlock;

public class Ruby {

  private Ruby() {}

  public interface Enumerable<E> extends
      RubyContract.Enumerable<E, Ruby.Enumerator<?>, RubyArray<?>> {

    @Override
    public boolean allʔ();

    @Override
    public boolean allʔ(BooleanBlock<? super E> block);

    @Override
    public boolean anyʔ();

    @Override
    public boolean anyʔ(BooleanBlock<? super E> block);

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
    public <S> RubyArray<S>
        collect(TransformBlock<? super E, ? extends S> block);

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

    @Override
    public int count();

    @Override
    public int count(BooleanBlock<? super E> block);

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

    @Override
    public void cycle(int n, Block<? super E> block);

    @Override
    public void cycle(Block<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> detect();

    @Override
    public E detect(BooleanBlock<? super E> block);

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

    @Override
    public void eachSlice(int n, Block<? super RubyArray<E>> block);

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

    @Override
    public <O> O eachWithObject(O obj,
        WithObjectBlock<? super E, ? super O> block);

    @Override
    public RubyArray<E> entries();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> find();

    @Override
    public E find(BooleanBlock<? super E> block);

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

    @Override
    public Integer findIndex(BooleanBlock<? super E> block);

    @Override
    public Integer findIndex(E target);

    @Override
    public E first();

    @Override
    public RubyArray<E> first(int n);

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

    @Override
    public <S> RubyHash<S, RubyArray<E>> groupBy(
        TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> RubyHash<S, RubyArray<E>> groupBy(final String methodName,
        final Object... args);

    @Override
    public boolean includeʔ(E target);

    @Override
    public E inject(ReduceBlock<E> block);

    @Override
    public <I> I inject(I init, WithInitBlock<? super E, I> block);

    @Override
    public <I> I inject(I init, String methodName);

    @Override
    public E inject(String methodName);

    @Override
    public RubyLazyEnumerator<E> lazy();

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

    @Override
    public E max();

    @Override
    public E max(Comparator<? super E> comp);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> maxBy();

    @Override
    public <S> E maxBy(Comparator<? super S> comp,
        TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> E maxBy(TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> E maxBy(String methodName, Object... args);

    @Override
    public boolean memberʔ(E target);

    @Override
    public E min();

    @Override
    public E min(Comparator<? super E> comp);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> minBy();

    @Override
    public <S> E minBy(Comparator<? super S> comp,
        TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> E minBy(TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> E minBy(String methodName, Object... args);

    @Override
    public RubyArray<E> minmax();

    @Override
    public RubyArray<E> minmax(Comparator<? super E> comp);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> minmaxBy();

    @Override
    public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
        TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> RubyArray<E> minmaxBy(
        TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> RubyArray<E> minmaxBy(String methodName, Object... args);

    @Override
    public boolean noneʔ();

    @Override
    public boolean noneʔ(BooleanBlock<? super E> block);

    @Override
    public boolean oneʔ();

    @Override
    public boolean oneʔ(BooleanBlock<? super E> block);

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> partition();

    @Override
    public RubyArray<RubyArray<E>> partition(BooleanBlock<? super E> block);

    @Override
    public E reduce(ReduceBlock<E> block);

    @Override
    public <I> I reduce(I init, WithInitBlock<? super E, I> block);

    @Override
    public <I> I reduce(I init, String methodName);

    @Override
    public E reduce(String methodName);

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

    @Override
    public RubyArray<E> sort();

    /**
     * {@inheritDoc}
     * 
     * @return {@link Ruby.Enumerator}
     */
    @Override
    public Ruby.Enumerator<E> sortBy();

    @Override
    public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
        TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
        Comparator<? super S> comp2,
        TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> RubyArray<E>
        sortBy(TransformBlock<? super E, ? extends S> block);

    @Override
    public <S> RubyArray<E> sortBy(String methodName, Object... args);

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

    @Override
    public RubyArray<E> toA();

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

    @Override
    public void zip(List<? extends Iterable<? extends E>> others,
        Block<? super RubyArray<E>> block);

  }

  public interface Enumerator<E> extends Enumerable<E>,
      RubyContract.Enumerator<E, Ruby.Enumerator<?>, RubyArray<?>> {}

  public interface LazyEnumerator<E>
      extends
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
    public Ruby.LazyEnumerator<E>
        eachWithIndex(WithIndexBlock<? super E> block);

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

  }

}
