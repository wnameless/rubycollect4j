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
import net.sf.rubycollect4j.iter.TransformByMethodIterable;

/**
 * An extension class for any Iterable object. It includes all methods refer to
 * the Enumerable module of Ruby.
 * 
 * @param <E>
 *          the type of the elements
 */
public abstract class RubyEnumerable<E> implements RubyEnumerableBase<E>,
    Iterable<E> {

  /**
   * Returns the Iterable of this RubyEnumerable.
   * 
   * @return an Iterable of this RubyEnumerable
   */
  protected abstract Iterable<E> getIterable();

  @Override
  public boolean allʔ() {
    return newRubyLazyEnumerator(getIterable()).allʔ();
  }

  @Override
  public boolean allʔ(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).allʔ(block);
  }

  @Override
  public boolean anyʔ() {
    return newRubyLazyEnumerator(getIterable()).anyʔ();
  }

  @Override
  public boolean anyʔ(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).anyʔ(block);
  }

  /**
   * Chunks elements to entries. Keys of entries are the result returned by the
   * block. Values of entries are RubyArrays of elements which get the same
   * result returned by the block and aside to each other.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to chunk elements
   * @return a RubyEnumerator
   */
  public <S> RubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      TransformBlock<E, S> block) {
    return newRubyEnumerator(new ChunkIterable<E, S>(getIterable(), block));
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> collect() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * Puts elements which are transformed by given block into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<S> collect(TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).collect(block).toA();
  }

  /**
   * Puts elements which are transformed by given method into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyArray
   */
  public <S> RubyArray<S> collect(String methodName, Object... args) {
    return newRubyLazyEnumerator(
        new TransformByMethodIterable<E, S>(getIterable(), methodName, args))
        .toA();
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> collectConcat() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * Turns each element into a RubyArray and then flattens it.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyArray
   */
  public <S> RubyArray<S> collectConcat(
      TransformBlock<E, ? extends List<S>> block) {
    return newRubyLazyEnumerator(getIterable()).collectConcat(block).toA();
  }

  @Override
  public int count() {
    return newRubyLazyEnumerator(getIterable()).count();
  }

  @Override
  public int count(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).count(block);
  }

  /**
   * Generates a sequence from first element to last element and so on
   * infinitely.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> cycle() {
    return newRubyEnumerator(new CycleIterable<E>(getIterable()));
  }

  /**
   * Generates a sequence from first element to last element, repeat n times.
   * 
   * @param n
   *          times to repeat
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> cycle(int n) {
    return newRubyEnumerator(new CycleIterable<E>(getIterable(), n));
  }

  @Override
  public void cycle(int n, Block<E> block) {
    newRubyLazyEnumerator(getIterable()).cycle(n, block);
  }

  @Override
  public void cycle(Block<E> block) {
    newRubyLazyEnumerator(getIterable()).cycle(block);
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> detect() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public E detect(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).detect(block);
  }

  /**
   * Drops the first n elements and puts the rest into a RubyArray.
   * 
   * @param n
   *          number of elements to drop
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> drop(int n) {
    return newRubyLazyEnumerator(getIterable()).drop(n).toA();
  }

  /**
   * Returns a RubyEnumerator which contains the first element of this
   * RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> dropWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : getIterable()) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

  /**
   * Drops the first n elements until a element gets false returned by the
   * block.
   * 
   * @param block
   *          to define which elements to be dropped
   * @return a RubyArray
   */
  public RubyArray<E> dropWhile(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).dropWhile(block).toA();
  }

  /**
   * Iterates each element and puts the element with n - 1 consecutive elements
   * into a RubyArray.
   * 
   * @param n
   *          number of consecutive elements
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> eachCons(int n) {
    return newRubyEnumerator(new EachConsIterable<E>(getIterable(), n));
  }

  @Override
  public void eachCons(int n, Block<RubyArray<E>> block) {
    newRubyLazyEnumerator(getIterable()).eachCons(n, block);
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> eachEntry() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyEnumerable
   */
  public RubyEnumerable<E> eachEntry(Block<E> block) {
    for (E item : getIterable()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Slices elements into RubyArrays with length n.
   * 
   * @param n
   *          size of each slice
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> eachSlice(int n) {
    return newRubyEnumerator(new EachSliceIterable<E>(getIterable(), n));
  }

  @Override
  public void eachSlice(int n, Block<RubyArray<E>> block) {
    newRubyLazyEnumerator(getIterable()).eachSlice(n, block);
  }

  /**
   * Iterates elements with their indices by Entry.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return newRubyEnumerator(new EachWithIndexIterable<E>(getIterable()));
  }

  /**
   * Iterates elements with their indices and yields them to the block.
   * 
   * @param block
   *          to yield each element and index
   * @return this RubyEnumerable
   */
  public RubyEnumerable<E> eachWithIndex(WithIndexBlock<E> block) {
    int i = 0;
    for (E item : getIterable()) {
      block.yield(item, i);
      i++;
    }
    return this;
  }

  /**
   * Iterates elements with the object S.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param o
   *          an Object
   * @return a RubyEnumerator
   */
  public <S> RubyEnumerator<Entry<E, S>> eachWithObject(S o) {
    return newRubyEnumerator(new EachWithObjectIterable<E, S>(getIterable(), o));
  }

  @Override
  public <S> S eachWithObject(S o, WithObjectBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).eachWithObject(o, block);
  }

  @Override
  public RubyArray<E> entries() {
    return newRubyArray(getIterable());
  }

  /**
   * Equivalent to detect().
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> find() {
    return detect();
  }

  @Override
  public E find(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).find(block);
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> findAll() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * Puts elements which are true returned by the block into a RubyArray.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> findAll(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).findAll(block).toA();
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> findIndex() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public Integer findIndex(BooleanBlock<E> block) {
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
   * Equivalent to collectConcat().
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> flatMap() {
    return collectConcat();
  }

  /**
   * Equivalent to collectConcat().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyArray
   */
  public <S> RubyArray<S> flatMap(TransformBlock<E, ? extends List<S>> block) {
    return newRubyLazyEnumerator(getIterable()).flatMap(block).toA();
  }

  /**
   * Puts elements which are matched by regex into a RubyArray.
   * 
   * @param regex
   *          regular expression
   * @return a RubyArray
   */
  public RubyArray<E> grep(String regex) {
    return newRubyLazyEnumerator(getIterable()).grep(regex).toA();
  }

  /**
   * Puts elements which are matched by regex transformed by the block into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param regex
   *          regular expression
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<S> grep(String regex, TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).grep(regex, block).toA();
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> groupBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> RubyHash<S, RubyArray<E>> groupBy(TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).groupBy(block);
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
  public <S> S inject(S init, WithInitBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).inject(init, block);
  }

  @Override
  public <S> S inject(S init, String methodName) {
    return newRubyLazyEnumerator(getIterable()).inject(init, methodName);
  }

  @Override
  public E inject(String methodName) {
    return newRubyLazyEnumerator(getIterable()).inject(methodName);
  }

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> lazy() {
    return new RubyLazyEnumerator<E>(getIterable());
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> map() {
    return collect();
  }

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<S> map(TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).map(block).toA();
  }

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyArray
   */
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
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> maxBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> E maxBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).maxBy(comp, block);
  }

  @Override
  public <S> E maxBy(TransformBlock<E, S> block) {
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
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> minBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> E minBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).minBy(comp, block);
  }

  @Override
  public <S> E minBy(TransformBlock<E, S> block) {
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
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> minmaxBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).minmaxBy(comp, block);
  }

  @Override
  public <S> RubyArray<E> minmaxBy(TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).minmaxBy(block);
  }

  @Override
  public boolean noneʔ() {
    return newRubyLazyEnumerator(getIterable()).noneʔ();
  }

  @Override
  public boolean noneʔ(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).noneʔ(block);
  }

  @Override
  public boolean oneʔ() {
    return newRubyLazyEnumerator(getIterable()).oneʔ();
  }

  @Override
  public boolean oneʔ(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).oneʔ(block);
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> partition() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).partition(block);
  }

  @Override
  public E reduce(ReduceBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).reduce(block);
  }

  @Override
  public <S> S reduce(S init, WithInitBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).reduce(init, block);
  }

  @Override
  public <S> S reduce(S init, String methodName) {
    return newRubyLazyEnumerator(getIterable()).reduce(init, methodName);
  }

  @Override
  public E reduce(String methodName) {
    return newRubyLazyEnumerator(getIterable()).reduce(methodName);
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> reject() {
    return newRubyEnumerator(getIterable());
  }

  /**
   * Deletes elements which are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> reject(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).reject(block).toA();
  }

  /**
   * Returns a reversed RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> reverseEach() {
    return newRubyEnumerator(new ReverseEachIterable<E>(getIterable()));
  }

  /**
   * Iterates each element reversely by given block.
   * 
   * @param block
   *          to yield each element
   * @return this RubyEnumerable
   */
  public RubyEnumerable<E> reverseEach(Block<E> block) {
    for (E item : reverseEach()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> select() {
    return findAll();
  }

  /**
   * Equivalent to findAll().
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> select(BooleanBlock<E> block) {
    return findAll(block);
  }

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should get true returned by the block.
   * 
   * @param block
   *          to check where to do slice
   * @return a RubyEnumerator
   */
  public RubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock<E> block) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(getIterable(), block));
  }

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should be matched by the regex.
   * 
   * @param regex
   *          to check where to do slice
   * @return a RubyEnumerator
   */
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
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> sortBy() {
    return newRubyEnumerator(getIterable());
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(comp, block);
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(comp1, comp2, block);
  }

  @Override
  public <S> RubyArray<E> sortBy(TransformBlock<E, S> block) {
    return newRubyLazyEnumerator(getIterable()).sortBy(block);
  }

  /**
   * Puts the first n elements into a RubyArray.
   * 
   * @param n
   *          number of elements
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> take(int n) {
    return newRubyLazyEnumerator(getIterable()).take(n).toA();
  }

  /**
   * Returns a RubyEnumerator which contains the first element of this
   * RubyEnumerable.
   * 
   * @return a RubyEnumerator
   */
  public RubyEnumerator<E> takeWhile() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : getIterable()) {
      rubyArray.add(item);
      break;
    }
    return newRubyEnumerator(rubyArray);
  }

  /**
   * Puts elements into a RubyArray until a element gets false returned by the
   * block.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> takeWhile(BooleanBlock<E> block) {
    return newRubyLazyEnumerator(getIterable()).takeWhile(block).toA();
  }

  @Override
  public RubyArray<E> toA() {
    return newRubyArray(getIterable());
  }

  /**
   * Groups elements which get the same indices among all other Iterables into
   * RubyArrays.
   * 
   * @param others
   *          an array of Iterable
   * @return a RubyArray
   */
  public RubyArray<RubyArray<E>> zip(Iterable<E>... others) {
    return newRubyLazyEnumerator(getIterable()).zip(others).toA();
  }

  /**
   * Groups elements which get the same indices among all other Iterables into
   * RubyArrays.
   * 
   * @param others
   *          a List of Iterable
   * @return a RubyArray
   */
  public RubyArray<RubyArray<E>> zip(List<? extends Iterable<E>> others) {
    return newRubyLazyEnumerator(getIterable()).zip(others).toA();
  }

  @Override
  public void
      zip(List<? extends Iterable<E>> others, Block<RubyArray<E>> block) {
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
