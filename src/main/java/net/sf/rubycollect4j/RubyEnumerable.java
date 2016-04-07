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

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.ReduceBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;
import net.sf.rubycollect4j.block.WithInitBlock;
import net.sf.rubycollect4j.block.WithObjectBlock;
import net.sf.rubycollect4j.iter.ChunkIterable;
import net.sf.rubycollect4j.iter.CycleIterable;
import net.sf.rubycollect4j.iter.EachConsIterable;
import net.sf.rubycollect4j.iter.EachSliceIterable;
import net.sf.rubycollect4j.iter.EachWithIndexIterable;
import net.sf.rubycollect4j.iter.EachWithObjectIterable;
import net.sf.rubycollect4j.iter.ReverseEachIterable;
import net.sf.rubycollect4j.iter.SliceBeforeIterable;
import net.sf.rubycollect4j.iter.TransformIterable;

/**
 * {@link RubyEnumerable} is an extension for any Iterable class. It includes
 * all methods refer to the Enumerable module of Ruby.
 * <p>
 * {@link RubyEnumerable} is also a {@link Ruby.Enumerable}.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public abstract class RubyEnumerable<E> implements Ruby.Enumerable<E> {

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
  public boolean allʔ(BooleanBlock<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).allʔ(block);
  }

  @Override
  public boolean anyʔ() {
    return newRubyLazyEnumerator(getIterable()).anyʔ();
  }

  @Override
  public boolean anyʔ(BooleanBlock<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).anyʔ(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public <S> RubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      TransformBlock<? super E, ? extends S> block) {
    return newRubyEnumerator(new ChunkIterable<E, S>(getIterable(), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   * 
   * @deprecated since 1.9.0, using Java 8 Lambda instead
   */
  @Deprecated
  @Override
  public <S> RubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      final String methodName, final Object... args) {
    return newRubyEnumerator(new ChunkIterable<E, S>(getIterable(),
        new TransformBlock<E, S>() {

          @Override
          public S yield(E item) {
            return RubyObject.send(item, methodName, args);
          }

        }));
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
  @Override
  public <S> RubyArray<S> collect(TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).collect(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   * 
   * @deprecated since 1.9.0, using Java 8 Lambda instead
   */
  @Deprecated
  @Override
  public <S> RubyArray<S> collect(final String methodName, final Object... args) {
    return newRubyLazyEnumerator(
        new TransformIterable<E, S>(getIterable(), new TransformBlock<E, S>() {

          @Override
          public S yield(E item) {
            return RubyObject.send(item, methodName, args);
          }

        })).toA();
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
  @Override
  public <S> RubyArray<S> collectConcat(
      TransformBlock<? super E, ? extends List<? extends S>> block) {
    return newRubyLazyEnumerator(getIterable()).collectConcat(block).toA();
  }

  @Override
  public int count() {
    return newRubyLazyEnumerator(getIterable()).count();
  }

  @Override
  public int count(BooleanBlock<? super E> block) {
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
  public void cycle(int n, Block<? super E> block) {
    newRubyLazyEnumerator(getIterable()).cycle(n, block);
  }

  @Override
  public void cycle(Block<? super E> block) {
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
  public E detect(BooleanBlock<? super E> block) {
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
  public RubyArray<E> dropWhile(BooleanBlock<? super E> block) {
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
  public RubyEnumerable<E> each(Block<? super E> block) {
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
  public void eachCons(int n, Block<? super RubyArray<E>> block) {
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
  public RubyEnumerable<E> eachEntry(Block<? super E> block) {
    for (E item : getIterable()) {
      block.yield(item);
    }
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
  public void eachSlice(int n, Block<? super RubyArray<E>> block) {
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
  public RubyEnumerable<E> eachWithIndex(WithIndexBlock<? super E> block) {
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
    return newRubyEnumerator(new EachWithObjectIterable<E, O>(getIterable(),
        obj));
  }

  @Override
  public <O> O eachWithObject(O obj, WithObjectBlock<? super E, ? super O> block) {
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
  public E find(BooleanBlock<? super E> block) {
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
  public RubyArray<E> findAll(BooleanBlock<? super E> block) {
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
  public Integer findIndex(BooleanBlock<? super E> block) {
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
  @Override
  public <S> RubyArray<S> flatMap(
      TransformBlock<? super E, ? extends List<? extends S>> block) {
    return newRubyLazyEnumerator(getIterable()).flatMap(block).toA();
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
  @Override
  public <S> RubyArray<S> grep(String regex,
      TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).grep(regex, block).toA();
  }

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
      final Object... args) {
    return grep(regex, new TransformBlock<E, S>() {

      @Override
      public S yield(E item) {
        return RubyObject.send(item, methodName, args);
      }

    });
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
      TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).groupBy(block);
  }

  @Deprecated
  @Override
  public <S> RubyHash<S, RubyArray<E>> groupBy(final String methodName,
      final Object... args) {
    return newRubyLazyEnumerator(getIterable()).groupBy(methodName, args);
  }

  @Override
  public boolean includeʔ(E target) {
    return newRubyLazyEnumerator(getIterable()).includeʔ(target);
  }

  @Override
  public E inject(ReduceBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).inject(block);
  }

  @Override
  public <I> I inject(I init, WithInitBlock<? super E, I> block) {
    return newRubyLazyEnumerator(getIterable()).inject(init, block);
  }

  @Deprecated
  @Override
  public <I> I inject(I init, String methodName) {
    return newRubyLazyEnumerator(getIterable()).inject(init, methodName);
  }

  @Deprecated
  @Override
  public E inject(String methodName) {
    return newRubyLazyEnumerator(getIterable()).inject(methodName);
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
  @Override
  public <S> RubyArray<S> map(TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).map(block).toA();
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   * 
   * @deprecated since 1.9.0, using Java 8 Lambda instead
   */
  @Deprecated
  @Override
  public <S> RubyArray<S> map(String methodName, Object... args) {
    return collect(methodName, args);
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
      TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).maxBy(comp, block);
  }

  @Override
  public <S> E maxBy(TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).maxBy(block);
  }

  @Deprecated
  @Override
  public <S> E maxBy(String methodName, Object... args) {
    return newRubyLazyEnumerator(getIterable()).maxBy(methodName, args);
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
      TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minBy(comp, block);
  }

  @Override
  public <S> E minBy(TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minBy(block);
  }

  @Deprecated
  @Override
  public <S> E minBy(String methodName, Object... args) {
    return newRubyLazyEnumerator(getIterable()).minBy(methodName, args);
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
      TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minmaxBy(comp, block);
  }

  @Override
  public <S> RubyArray<E> minmaxBy(TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).minmaxBy(block);
  }

  @Deprecated
  @Override
  public <S> RubyArray<E> minmaxBy(String methodName, Object... args) {
    return newRubyLazyEnumerator(getIterable()).minmaxBy(methodName, args);
  }

  @Override
  public boolean noneʔ() {
    return newRubyLazyEnumerator(getIterable()).noneʔ();
  }

  @Override
  public boolean noneʔ(BooleanBlock<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).noneʔ(block);
  }

  @Override
  public boolean oneʔ() {
    return newRubyLazyEnumerator(getIterable()).oneʔ();
  }

  @Override
  public boolean oneʔ(BooleanBlock<? super E> block) {
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
  public RubyArray<RubyArray<E>> partition(BooleanBlock<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).partition(block);
  }

  @Override
  public E reduce(ReduceBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).reduce(block);
  }

  @Override
  public <I> I reduce(I init, WithInitBlock<? super E, I> block) {
    return newRubyLazyEnumerator(getIterable()).reduce(init, block);
  }

  @Deprecated
  @Override
  public <I> I reduce(I init, String methodName) {
    return newRubyLazyEnumerator(getIterable()).reduce(init, methodName);
  }

  @Deprecated
  @Override
  public E reduce(String methodName) {
    return newRubyLazyEnumerator(getIterable()).reduce(methodName);
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
  public RubyArray<E> reject(BooleanBlock<? super E> block) {
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
  public RubyEnumerable<E> reverseEach(Block<? super E> block) {
    for (E item : reverseEach()) {
      block.yield(item);
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
  public RubyArray<E> select(BooleanBlock<? super E> block) {
    return findAll(block);
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock<? super E> block) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(getIterable(), block));
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyEnumerator}
   */
  @Override
  public RubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(getIterable(),
        Pattern.compile(regex)));
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
      TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(comp, block);
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(comp1, comp2, block);
  }

  @Override
  public <S> RubyArray<E> sortBy(TransformBlock<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(block);
  }

  @Deprecated
  @Override
  public <S> RubyArray<E> sortBy(String methodName, Object... args) {
    return newRubyLazyEnumerator(getIterable()).sortBy(methodName, args);
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
  public RubyArray<E> takeWhile(BooleanBlock<? super E> block) {
    return newRubyLazyEnumerator(getIterable()).takeWhile(block).toA();
  }

  @Override
  public RubyArray<E> toA() {
    return newRubyArray(getIterable());
  }

  /**
   * {@inheritDoc}
   * 
   * @return {@link RubyArray}
   */
  @Override
  public RubyArray<RubyArray<E>> zip(Iterable<? extends E>... others) {
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
      Block<? super RubyArray<E>> block) {
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
