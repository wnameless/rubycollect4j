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

import java.util.Comparator;
import java.util.List;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.ReduceBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithIndexBlock;
import net.sf.rubycollect4j.block.WithInitBlock;
import net.sf.rubycollect4j.block.WithObjectBlock;

/**
 * 
 * RubyEnumerableBase defines all common eager methods of a RubyRnumerable
 * should have.
 * 
 * @param <E>
 *          the type of the elements
 */
public interface RubyEnumerableBase<E, I extends RubyEnumeratorBase<?, ?, ?>, Z extends RubyEnumerableBase<?, ?, ?>>
    extends Iterable<E> {

  /**
   * Checks if null or false included.
   * 
   * @return true if null or false is found, false otherwise
   */
  public boolean allʔ();

  /**
   * Checks if any result returned by the block is false.
   * 
   * @param block
   *          to check elements
   * @return true if all result are true, false otherwise
   */
  public boolean allʔ(BooleanBlock<E> block);

  /**
   * Checks if any non-null or not-false object included.
   * 
   * @return true if non-null or not-false object is found, false otherwise
   */
  public boolean anyʔ();

  /**
   * Checks if any result returned by the block is true.
   * 
   * @param block
   *          to check elements
   * @return true if any result are true, false otherwise
   */
  public boolean anyʔ(BooleanBlock<E> block);

  /**
   * Chunks elements to entries. Keys of entries are the result returned by the
   * block. Values of entries are RubyArrays of elements which get the same
   * result returned by the block and aside to each other.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to chunk elements
   * @return a RubyEnumeratorBase
   */
  public <S> I chunk(TransformBlock<E, S> block);

  /**
   * Chunks elements to entries. Keys of entries are the result invoked by the
   * given method name. Values of entries are RubyArrays of elements which get
   * the same result returned by the block and aside to each other.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyEnumeratorBase
   */
  public <S> I chunk(final String methodName, final Object... args);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I collect();

  /**
   * Transforms each element by the block.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyEnumerableBase
   */
  public <S> Z collect(TransformBlock<E, S> block);

  /**
   * Transforms each element by given method.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyEnumerableBase
   */
  public <S> Z collect(final String methodName, final Object... args);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I collectConcat();

  /**
   * Turns each element into a RubyArray and then flattens it.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyEnumerableBase
   */
  public <S> Z collectConcat(TransformBlock<E, ? extends List<S>> block);

  /**
   * Counts the elements.
   * 
   * @return a int
   */
  public int count();

  /**
   * Counts the elements which are true returned by the block.
   * 
   * @param block
   *          to define elements to be counted
   * @return a int
   */
  public int count(BooleanBlock<E> block);

  /**
   * Generates a sequence from first element to last element and so on
   * infinitely.
   * 
   * @return a RubyEnumeratorBase
   */
  public I cycle();

  /**
   * Generates a sequence from first element to last element, repeat n times.
   * 
   * @param n
   *          times to repeat
   * @return a RubyEnumeratorBase
   */
  public I cycle(int n);

  /**
   * Generates a sequence from start element to end element, repeat n times.
   * Yields each element to the block.
   * 
   * @param n
   *          times to repeat
   * @param block
   *          to yield each element
   */
  public void cycle(int n, Block<E> block);

  /**
   * Generates a sequence from start element to end element and so on
   * infinitely. Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   */
  public void cycle(Block<E> block);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I detect();

  /**
   * Finds the first element which gets true returned by the block. Returns null
   * if element is not found.
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E detect(BooleanBlock<E> block);

  /**
   * Drops the first n elements.
   * 
   * @param n
   *          number of elements to drop
   * @return a RubyEnumeratorBase
   */
  public Z drop(int n);

  /**
   * Returns a RubyEnumeratorBase which contains the first element of this
   * RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I dropWhile();

  /**
   * Drops the first n elements until a element gets false returned by the
   * block. Lazy loading by a RubyLazyEnumerator.
   * 
   * @param block
   *          to define which elements to be dropped
   * @return a RubyEnumerableBase
   */
  public Z dropWhile(BooleanBlock<E> block);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I each();

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return a RubyEnumerableBase
   */
  public RubyEnumerableBase<E, I, Z> each(Block<E> block);

  /**
   * Iterates each element and puts the element with n - 1 consecutive elements
   * into a RubyArray.
   * 
   * @param n
   *          number of consecutive elements
   * @return a RubyEnumeratorBase
   */
  public I eachCons(int n);

  /**
   * Iterates each element and yields the element with n - 1 consecutive
   * elements to the block.
   * 
   * @param n
   *          number of consecutive elements
   * @param block
   *          to yield the RubyArray of consecutive elements
   */
  public void eachCons(int n, Block<RubyArray<E>> block);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I eachEntry();

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return a RubyEnumerableBase
   */
  public RubyEnumerableBase<E, I, Z> eachEntry(Block<E> block);

  /**
   * Slices elements into RubyArrays with length n.
   * 
   * @param n
   *          size of each slice
   * @return a RubyEnumeratorBase
   */
  public I eachSlice(int n);

  /**
   * Slices elements into RubyArrays with length n and yield them to the block.
   * 
   * @param n
   *          size of each slice
   * @param block
   *          to yield each slice
   */
  public void eachSlice(int n, Block<RubyArray<E>> block);

  /**
   * Iterates elements with their indices by Entry.
   * 
   * @return a RubyEnumeratorBase
   */
  public I eachWithIndex();

  /**
   * YIterates elements with their indices and yields them to the block.
   * 
   * @param block
   *          to yield each element
   * @return a RubyEnumerableBase
   */
  public RubyEnumerableBase<E, I, Z> eachWithIndex(WithIndexBlock<E> block);

  /**
   * Iterates elements with the object S.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param o
   *          an Object
   * @return a RubyEnumeratorBase
   */
  public <S> I eachWithObject(S o);

  /**
   * Iterates elements with the Object S and yield them to the block.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param o
   *          any Object
   * @param block
   *          to yield each Entry
   * @return the Object S
   */
  public <S> S eachWithObject(S o, WithObjectBlock<E, S> block);

  /**
   * Puts each element into a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> entries();

  /**
   * Equivalent to detect().
   * 
   * @return a RubyEnumeratorBase
   */
  public I find();

  /**
   * Equivalent to detect().
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E find(BooleanBlock<E> block);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I findAll();

  /**
   * Finds all elements which are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return a RubyEnumerableBase
   */
  public Z findAll(BooleanBlock<E> block);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I findIndex();

  /**
   * Finds the index of a element which is true returned by the block. Returns
   * null if nothing found.
   * 
   * @param block
   *          to check elements
   * @return an Integer or null
   */
  public Integer findIndex(BooleanBlock<E> block);

  /**
   * Finds the index of the target element. Returns null if target not found.
   * 
   * @param target
   *          to be found
   * @return an Integer or null
   */
  public Integer findIndex(E target);

  /**
   * Gets the first element of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E first();

  /**
   * Gets the first n element of this RubyEnumerable.
   * 
   * @param n
   *          number of elements
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> first(int n);

  /**
   * Equivalent to collectConcat().
   * 
   * @return a RubyEnumeratorBase
   */
  public I flatMap();

  /**
   * Equivalent to collectConcat().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a RubyEnumerableBase
   */
  public <S> Z flatMap(TransformBlock<E, ? extends List<S>> block);

  /**
   * Finds all elements which are matched by the regular expression.
   * 
   * @param regex
   *          regular expression
   * @return a RubyEnumerableBase
   */
  public Z grep(String regex);

  /**
   * Finds all elements which are matched by the regular expression and
   * transforms them.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param regex
   *          regular expression
   * @param block
   *          to transform elements
   * @return a RubyEnumerableBase
   */
  public <S> Z grep(String regex, TransformBlock<E, S> block);

  /**
   * Finds all elements which are matched by the regular expression and invokes
   * them by given method name.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param regex
   *          regular expression
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyEnumerableBase
   */
  public <S> Z
      grep(String regex, final String methodName, final Object... args);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I groupBy();

  /**
   * Puts elements with the same result S returned by the block into a
   * Entry&#60;S, RubyArray&#60;E&#62;&#62;y of a RubyHash.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to group each element
   * @return a RubyHash
   */
  public <S> RubyHash<S, RubyArray<E>> groupBy(TransformBlock<E, S> block);

  /**
   * Puts elements with the same result S invoked by given method name into a
   * Entry&#60;S, RubyArray&#60;E&#62;&#62;y of a RubyHash.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyHash
   */
  public <S> RubyHash<S, RubyArray<E>> groupBy(String methodName,
      Object... args);

  /**
   * Checks if target element included.
   * 
   * @param target
   *          to be searched
   * @return true if target is found,false otherwise
   */
  public boolean includeʔ(E target);

  /**
   * Assigns the first element as the initial value. Reduces each element with
   * block, then assigns the result back to initial value and so on.
   * 
   * @param block
   *          to reduce each element
   * @return an element
   */
  public E inject(ReduceBlock<E> block);

  /**
   * Reduces each element with block, then assigns the result back to initial
   * value and so on.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param block
   *          to reduce each element
   * @return an element S
   */
  public <S> S inject(S init, WithInitBlock<E, S> block);

  /**
   * Reduces each element with initial value by a method of S, then assigns the
   * result back to initial value and so on.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param methodName
   *          method used to reduce elements
   * @return an element S
   * @throws IllegalArgumentException
   *           if method not found
   * @throws RuntimeException
   *           if invocation failed
   */
  public <S> S inject(S init, String methodName);

  /**
   * Assigns the first element as the initial value. Reduces each element with
   * initial value by a method of S, then assigns the result back to initial
   * value and so on.
   * 
   * @param methodName
   *          method used to reduce elements
   * @return an element
   * @throws IllegalArgumentException
   *           if method not found
   * @throws RuntimeException
   *           if invocation failed
   */
  public E inject(String methodName);

  /**
   * Returns a RubyLazyEnumerator.
   * 
   * @return a RubyLazyEnumerator
   */
  public RubyLazyEnumerator<E> lazy();

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I map();

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyEnumerableBase
   */
  public <S> Z map(TransformBlock<E, S> block);

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyEnumerableBase
   */
  public <S> Z map(String methodName, Object... args);

  /**
   * Finds the max element of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E max();

  /**
   * Finds the max element induced by the Comparator of this RubyEnumerable.
   * Returns null if this RubyEnumerable is empty.
   * 
   * @param comp
   *          a Comparator
   * @return an element or null
   */
  public E max(Comparator<? super E> comp);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I maxBy();

  /**
   * Finds the element which is the max element induced by the Comparator
   * transformed by the block of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E maxBy(Comparator<? super S> comp, TransformBlock<E, S> block);

  /**
   * Finds the element which is the max element transformed by the block of this
   * RubyEnumerable. Returns null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E maxBy(TransformBlock<E, S> block);

  /**
   * Finds the element which is the max element invoked by given method name of
   * this RubyEnumerable. Returns null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return an element or null
   */
  public <S> E maxBy(String methodName, Object... args);

  /**
   * Equivalent to includeʔ().
   * 
   * @param target
   *          to be found
   * @return an element or null
   */
  public boolean memberʔ(E target);

  /**
   * Finds the min element of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E min();

  /**
   * Finds the min element induced by the Comparator of this RubyEnumerable.
   * Returns null if this RubyEnumerable is empty.
   * 
   * @param comp
   *          a Comparator
   * @return an element or null
   */
  public E min(Comparator<? super E> comp);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I minBy();

  /**
   * Finds the element which is the min element induced by the Comparator
   * transformed by the block of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E minBy(Comparator<? super S> comp, TransformBlock<E, S> block);

  /**
   * Finds the element which is the min element transformed by the block of this
   * RubyEnumerable. Returns null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return an element or null
   */
  public <S> E minBy(TransformBlock<E, S> block);

  /**
   * Finds the element which is the min element invoked by given method name of
   * this RubyEnumerable. Returns null if this RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return an element or null
   */
  public <S> E minBy(String methodName, Object... args);

  /**
   * Finds the min and max elements of this RubyEnumerable and puts them into a
   * RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> minmax();

  /**
   * Finds the min and max elements induced by the Comparator of this
   * RubyEnumerable and puts them into a RubyArray.
   * 
   * @param comp
   *          a Comparator
   * @return a RubyArray
   */
  public RubyArray<E> minmax(Comparator<? super E> comp);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I minmaxBy();

  /**
   * Finds elements which are the min and max elements induced by the Comparator
   * transformed by the block of this RubyEnumerable and puts them into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      TransformBlock<E, S> block);

  /**
   * Finds elements which is the min and max elements transformed by the block
   * of this RubyEnumerable and puts them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> minmaxBy(TransformBlock<E, S> block);

  /**
   * Finds the element which is the min and max elements element invoked by
   * given method name of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyArray
   */
  public <S> RubyArray<E> minmaxBy(String methodName, Object... args);

  /**
   * Checks if this RubyEnumerable contains only null objects.
   * 
   * @return true if all elements are null, false otherwise
   */
  public boolean noneʔ();

  /**
   * Checks if this RubyEnumerable contains only elements which are false
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if all results of block are false, false otherwise
   */
  public boolean noneʔ(BooleanBlock<E> block);

  /**
   * Checks if this RubyEnumerable contains only one element beside null
   * objects.
   * 
   * @return true if only one element and nulls are found, false otherwise
   */
  public boolean oneʔ();

  /**
   * Checks if this RubyEnumerable contains only one element which are true
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if only one result of block is true, false otherwise
   */
  public boolean oneʔ(BooleanBlock<E> block);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I partition();

  /**
   * Divides elements into 2 groups by the given block.
   * 
   * @param block
   *          to part elements
   * @return a RubyArray of 2 RubyArrays
   */
  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block);

  /**
   * Equivalent to inject().
   * 
   * @param block
   *          to reduce each element
   * @return an element
   */
  public E reduce(ReduceBlock<E> block);

  /**
   * Equivalent to inject().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param block
   *          to reduce each element
   * @return an element S
   */
  public <S> S reduce(S init, WithInitBlock<E, S> block);

  /**
   * Equivalent to inject().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param init
   *          initial value
   * @param methodName
   *          method used to reduce elements
   * @return an element S
   */
  public <S> S reduce(S init, String methodName);

  /**
   * Equivalent to inject().
   * 
   * @param methodName
   *          method used to reduce elements
   * @return an element
   */
  public E reduce(String methodName);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I reject();

  /**
   * Filters all elements which are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return a RubyEnumerableBase
   */
  public Z reject(BooleanBlock<E> block);

  /**
   * Returns a reversed RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I reverseEach();

  /**
   * Iterates each element reversely by given block.
   * 
   * @param block
   *          to yield each element
   * @return a RubyEnumerableBase
   */
  public RubyEnumerableBase<E, I, Z> reverseEach(Block<E> block);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I select();

  /**
   * Equivalent to findAll().
   * 
   * @param block
   *          to filter elements
   * @return a RubyEnumerableBase
   */
  public Z select(BooleanBlock<E> block);

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should get true returned by the block.
   * 
   * @param block
   *          to check where to do slice
   * @return a RubyEnumeratorBase
   */
  public I sliceBefore(BooleanBlock<E> block);

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should be matched by the regex.
   * 
   * @param regex
   *          to check where to do slice
   * @return a RubyEnumeratorBase
   */
  public I sliceBefore(String regex);

  /**
   * Sorts elements of this RubyEnumerable and puts them into a RubyArray.
   * 
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           when any 2 elements are not comparable
   */
  public RubyArray<E> sort();

  // /**
  // * Sorts elements of this RubyEnumerable by given Comparator and puts them
  // * into a RubyArray.
  // *
  // * @param comp
  // * a Comparator
  // * @return a RubyArray
  // */
  // public RubyArray<E> sort(Comparator<? super E> comp);

  /**
   * Returns a RubyEnumeratorBase of this RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I sortBy();

  /**
   * Sorts elements of this RubyEnumerable by the ordering of elements
   * transformed by the block induced by the Comparator and puts them into a
   * RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp
   *          a Comparator
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
      TransformBlock<E, S> block);

  /**
   * Sorts elements of this RubyEnumerable by the ordering of elements
   * transformed by the block induced by the Comparator for S and applies the
   * Comparator for E again before puts them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param comp1
   *          a Comparator for E
   * @param comp2
   *          a Comparator for S
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
      Comparator<? super S> comp2, TransformBlock<E, S> block);

  /**
   * Sorts elements of this RubyEnumerable by the ordering of elements
   * transformed by the block and puts them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> sortBy(TransformBlock<E, S> block);

  /**
   * Sorts elements of this RubyEnumerable by the ordering of elements invoked
   * by given method name and puts them into a RubyArray.
   * 
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @return a RubyArray
   */
  public <S> RubyArray<E> sortBy(String methodName, Object... args);

  /**
   * Takes the first n elements.
   * 
   * @param n
   *          number of elements
   * @return a RubyEnumerableBase
   */
  public Z take(int n);

  /**
   * Returns a RubyEnumeratorBase which contains the first element of this
   * RubyEnumerableBase.
   * 
   * @return a RubyEnumeratorBase
   */
  public I takeWhile();

  /**
   * Takes elements until a element gets false returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return a RubyEnumerableBase
   */
  public Z takeWhile(BooleanBlock<E> block);

  /**
   * Converts this RubyEnumerable into a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> toA();

  /**
   * Groups elements which get the same indices among all other Iterables into
   * RubyArrays.
   * 
   * @param others
   *          an array of Iterable
   * @return a RubyEnumerableBase
   */
  public Z zip(Iterable<E>... others);

  /**
   * Groups elements which get the same indices among all other Lists into
   * RubyArrays.
   * 
   * @param others
   *          a List of Iterable
   * @return a RubyEnumerableBase
   */
  public Z zip(List<? extends Iterable<E>> others);

  /**
   * Groups elements which get the same indices among all other Iterables into
   * RubyArrays and yields them to the block.
   * 
   * @param others
   *          List of Iterable
   * @param block
   *          to yield zipped elements
   */
  public void
      zip(List<? extends Iterable<E>> others, Block<RubyArray<E>> block);

}
