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
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * {@link RubyContract} provides few interfaces to define what are the methods a
 * {@link Enumerable} and an {@link Enumerator} should have. It's only for
 * RubyCollect4j to use internally.
 * 
 * @author Wei-Ming Wu
 * 
 */
final class RubyContract {

  private RubyContract() {}

  /**
   * 
   * {@link Enumerable} defines all methods of a Ruby Enumerable should have.
   * 
   * @param <E>
   *          the type of the elements
   * @param <N>
   *          the type of what kind of {@link Enumerator} should be used
   * @param <Z>
   *          the type to define if some methods are lazy or eager loading
   */
  interface Enumerable<E, N extends Enumerator<?, ?, ?>, Z extends Enumerable<?, ?, ?>>
      extends Iterable<E> {

    /**
     * Checks if null and Boolean.FALSE are excluded.
     * 
     * @return true if null and Boolean.FALSE are not found, false otherwise
     */
    public boolean allʔ();

    /**
     * Checks if any result returned by the block is false.
     * 
     * @param block
     *          to check elements
     * @return true if all result are true, false otherwise
     */
    public boolean allʔ(Predicate<? super E> block);

    /**
     * Checks if any non-null and not Boolean.FALSE object is included.
     * 
     * @return true if non-null and not Boolean.FALSE object is found, false
     *         otherwise
     */
    public boolean anyʔ();

    /**
     * Checks if any result returned by the block is true.
     * 
     * @param block
     *          to check elements
     * @return true if any result are true, false otherwise
     */
    public boolean anyʔ(Predicate<? super E> block);

    /**
     * Chunks elements into entries. The key of entry is the result returned by
     * the block. The value of entry is a {@link RubyArray} which contains
     * elements having the same result returned by the block and aside to each
     * other.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to chunk elements
     * @return {@link Enumerator}
     */
    public <S> N chunk(Function<? super E, ? extends S> block);

    /**
     * Chunks elements into entries. The value of entry is a {@link RubyArray}.
     * This method creates a new chunk whenever the block returns false.
     * 
     * @param block
     *          to define which elements to be chunked
     * @return {@link Enumerator}
     */
    public N chunkWhile(BiPredicate<? super E, ? super E> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N collect();

    /**
     * Transforms each element by the block.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to transform elements
     * @return {@link Enumerable}
     */
    public <S> Z collect(Function<? super E, ? extends S> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N collectConcat();

    /**
     * Turns each element into a List and then flattens it.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to take element into a List
     * @return {@link Enumerable}
     */
    public <S> Z collectConcat(
        Function<? super E, ? extends List<? extends S>> block);

    /**
     * Counts the elements.
     * 
     * @return the total number
     */
    public int count();

    /**
     * Counts the elements which are true returned by the block.
     * 
     * @param block
     *          to define elements to be counted
     * @return the total number
     */
    public int count(Predicate<? super E> block);

    /**
     * Generates a sequence from first element to last element and so on
     * infinitely.
     * 
     * @return {@link Enumerator}
     */
    public N cycle();

    /**
     * Generates a sequence from first element to last element, repeat n times.
     * 
     * @param n
     *          times to repeat
     * @return {@link Enumerator}
     */
    public N cycle(int n);

    /**
     * Generates a sequence from start element to end element, repeat n times.
     * Yields each element to the block.
     * 
     * @param n
     *          times to repeat
     * @param block
     *          to yield each element
     */
    public void cycle(int n, Consumer<? super E> block);

    /**
     * Generates a sequence from start element to end element and so on
     * infinitely. Yields each element to the block.
     * 
     * @param block
     *          to yield each element
     */
    public void cycle(Consumer<? super E> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N detect();

    /**
     * Finds the first element which gets true returned by the block. Returns
     * null if element is not found.
     * 
     * @param block
     *          to filter elements
     * @return element or null
     */
    public E detect(Predicate<? super E> block);

    /**
     * Drops the first n elements.
     * 
     * @param n
     *          number of elements to drop
     * @return {@link Enumerable}
     */
    public Z drop(int n);

    /**
     * Returns an enumerator which contains only the first element.
     * 
     * @return {@link Enumerator}
     */
    public N dropWhile();

    /**
     * Drops the first n elements until an element gets false returned by the
     * block.
     * 
     * @param block
     *          to define which elements to be dropped
     * @return {@link Enumerable}
     */
    public Z dropWhile(Predicate<? super E> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N each();

    /**
     * Yields each element to the block.
     * 
     * @param block
     *          to yield each element
     * @return {@link Enumerable}
     */
    public Enumerable<E, N, Z> each(Consumer<? super E> block);

    /**
     * Iterates each element and puts the element with n - 1 consecutive
     * elements into a {@link RubyArray}.
     * 
     * @param n
     *          number of consecutive elements
     * @return {@link Enumerator}
     */
    public N eachCons(int n);

    /**
     * Iterates each element and yields the element with n - 1 consecutive
     * elements to the block.
     * 
     * @param n
     *          number of consecutive elements
     * @param block
     *          to yield the List of consecutive elements
     */
    public void eachCons(int n, Consumer<? super RubyArray<E>> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N eachEntry();

    /**
     * Yields each element to the block.
     * 
     * @param block
     *          to yield each element
     * @return {@link Enumerable}
     */
    public Enumerable<E, N, Z> eachEntry(Consumer<? super E> block);

    /**
     * Slices elements into {@link RubyArray}s with length n.
     * 
     * @param n
     *          size of each slice
     * @return {@link Enumerator}
     */
    public N eachSlice(int n);

    /**
     * Slices elements into {@link RubyArray}s with length n and yield them to
     * the block.
     * 
     * @param n
     *          size of each slice
     * @param block
     *          to yield each slice
     */
    public void eachSlice(int n, Consumer<? super RubyArray<E>> block);

    /**
     * Iterates elements with their indices by Entry.
     * 
     * @return {@link Enumerator}
     */
    public N eachWithIndex();

    /**
     * Iterates elements with their indices and yields them to the block.
     * 
     * @param block
     *          to yield each element
     * @return {@link Enumerable}
     */
    public Enumerable<E, N, Z> eachWithIndex(
        BiConsumer<? super E, Integer> block);

    /**
     * Iterates elements with an object O.
     * 
     * @param <O>
     *          the type of transformed elements
     * @param obj
     *          an object O
     * @return {@link Enumerator}
     */
    public <O> N eachWithObject(O obj);

    /**
     * Iterates elements with an object O and yield them to the block.
     * 
     * @param <O>
     *          the type of transformed elements
     * @param obj
     *          an object O
     * @param block
     *          to yield each Entry
     * @return the object O
     */
    public <O> O eachWithObject(O obj, BiConsumer<? super E, ? super O> block);

    /**
     * Puts each element into a {@link RubyArray}.
     * 
     * @return {@link RubyArray}
     */
    public RubyArray<E> entries();

    /**
     * Equivalent to {@link #detect()}.
     * 
     * @return {@link Enumerator}
     */
    public N find();

    /**
     * Equivalent to {@link #detect(Predicate)}.
     * 
     * @param block
     *          to filter elements
     * @return element or null
     */
    public E find(Predicate<? super E> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N findAll();

    /**
     * Finds all elements which are true returned by the block.
     * 
     * @param block
     *          to filter elements
     * @return {@link Enumerable}
     */
    public Z findAll(Predicate<? super E> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N findIndex();

    /**
     * Returns the index of first element which gets true returned by the block.
     * Returns null if nothing is found.
     * 
     * @param block
     *          to check elements
     * @return Integer or null
     */
    public Integer findIndex(Predicate<? super E> block);

    /**
     * Returns the index of the target element. Returns null if the target is
     * not found.
     * 
     * @param target
     *          to be found
     * @return Integer or null
     */
    public Integer findIndex(E target);

    /**
     * Returns first element. Returns null if elements are empty.
     * 
     * @return element or null
     */
    public E first();

    /**
     * Returns the first n elements.
     * 
     * @param n
     *          number of elements
     * @return {@link RubyArray}
     * @throws IllegalArgumentException
     *           if n is less than 0
     */
    public RubyArray<E> first(int n);

    /**
     * Equivalent to {@link #collectConcat()}.
     * 
     * @return {@link Enumerator}
     */
    public N flatMap();

    /**
     * Equivalent to {@link #collectConcat(Function)}.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to take element into a List
     * @return {@link Enumerable}
     */
    public <S> Z flatMap(
        Function<? super E, ? extends List<? extends S>> block);

    /**
     * Finds all elements which are matched by the regular expression.
     * 
     * @param regex
     *          regular expression
     * @return {@link Enumerable}
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
     * @return {@link Enumerable}
     */
    public <S> Z grep(String regex, Function<? super E, ? extends S> block);

    /**
     * Finds all elements which are not matched by the regular expression.
     * 
     * @param regex
     *          regular expression
     * @return {@link Enumerable}
     */
    public Z grepV(String regex);

    /**
     * Finds all elements which are not matched by the regular expression and
     * transforms them.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param regex
     *          regular expression
     * @param block
     *          to transform elements
     * @return {@link Enumerable}
     */
    public <S> Z grepV(String regex, Function<? super E, ? extends S> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N groupBy();

    /**
     * Puts elements with the same result S returned by the block into a
     * Entry&#60;S, RubyArray&#60;E&#62;&#62;y of a {@link RubyHash}.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to group each element
     * @return {@link RubyHash}
     */
    public <S> RubyHash<S, RubyArray<E>> groupBy(
        Function<? super E, ? extends S> block);

    /**
     * Checks if target element is included.
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
     * @return element
     */
    public E inject(BiFunction<E, E, E> block);

    /**
     * Reduces each element with block, then assigns the result back to initial
     * value and so on.
     * 
     * @param <I>
     *          the type of transformed elements
     * @param init
     *          initial value
     * @param block
     *          to reduce each element
     * @return object I
     */
    public <I> I inject(I init, BiFunction<I, ? super E, I> block);

    /**
     * Returns a {@link RubyLazyEnumerator}.
     * 
     * @return {@link RubyLazyEnumerator}
     */
    public RubyLazyEnumerator<E> lazy();

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N map();

    /**
     * Equivalent to {@link #collect(Function)}.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to transform elements
     * @return {@link Enumerable}
     */
    public <S> Z map(Function<? super E, ? extends S> block);

    /**
     * Finds the max element. Returns null if elements are empty.
     * 
     * @return element or null
     */
    public E max();

    /**
     * Finds the max element compared by the Comparator. Returns null if
     * elements are empty.
     * 
     * @param comp
     *          a Comparator
     * @return element or null
     */
    public E max(Comparator<? super E> comp);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N maxBy();

    /**
     * Finds the max element for outputs transformed by the block and compared
     * by the Comparator. Returns null if elements are empty.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param comp
     *          a Comparator
     * @param block
     *          to transform elements
     * @return element or null
     */
    public <S> E maxBy(Comparator<? super S> comp,
        Function<? super E, ? extends S> block);

    /**
     * Finds the max element for outputs transformed by the block. Returns null
     * if elements are empty.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to transform elements
     * @return element or null
     */
    public <S> E maxBy(Function<? super E, ? extends S> block);

    /**
     * Equivalent to {@link #includeʔ(Object)}.
     * 
     * @param target
     *          to be found
     * @return element or null
     */
    public boolean memberʔ(E target);

    /**
     * Finds the min element. Returns null if elements are empty.
     * 
     * @return element or null
     */
    public E min();

    /**
     * Finds the min element for outputs compared by the Comparator. Returns
     * null if elements are empty.
     * 
     * @param comp
     *          a Comparator
     * @return element or null
     */
    public E min(Comparator<? super E> comp);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N minBy();

    /**
     * Finds the min element for outputs transformed by the block and compared
     * by the Comparator. Returns null if elements are empty.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param comp
     *          a Comparator
     * @param block
     *          to transform elements
     * @return element or null
     */
    public <S> E minBy(Comparator<? super S> comp,
        Function<? super E, ? extends S> block);

    /**
     * Finds the min element for outputs transformed by the block. Returns null
     * if elements are empty.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to transform elements
     * @return element or null
     */
    public <S> E minBy(Function<? super E, ? extends S> block);

    /**
     * Finds the min and max elements.
     * 
     * @return {@link RubyArray}
     */
    public RubyArray<E> minmax();

    /**
     * Finds the min and max elements for outputs compared by the Comparator.
     * 
     * @param comp
     *          a Comparator
     * @return {@link RubyArray}
     */
    public RubyArray<E> minmax(Comparator<? super E> comp);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N minmaxBy();

    /**
     * Finds the min and max elements for outputs transformed by the block and
     * compared by the Comparator.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param comp
     *          a Comparator
     * @param block
     *          to transform elements
     * @return {@link RubyArray}
     */
    public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
        Function<? super E, ? extends S> block);

    /**
     * Finds the min and max elements for outputs transformed by the block.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to transform elements
     * @return {@link RubyArray}
     */
    public <S> RubyArray<E> minmaxBy(Function<? super E, ? extends S> block);

    /**
     * Checks if elements contain only null or Boolean.FALSE objects.
     * 
     * @return true if all elements are null or Boolean.FALSE, false otherwise
     */
    public boolean noneʔ();

    /**
     * Checks if elements contain only elements which are false returned by the
     * block.
     * 
     * @param block
     *          to check elements
     * @return true if all results of block are false, false otherwise
     */
    public boolean noneʔ(Predicate<? super E> block);

    /**
     * Checks if elements contain only one element beside null and
     * Boolean.FALSE.
     * 
     * @return true if only one element beside null and Boolean.FALSE is found,
     *         false otherwise
     */
    public boolean oneʔ();

    /**
     * Checks if elements contain only one element which are true returned by
     * the block.
     * 
     * @param block
     *          to check elements
     * @return true if only one result of block is true, false otherwise
     */
    public boolean oneʔ(Predicate<? super E> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N partition();

    /**
     * Divides elements into 2 groups by the given block.
     * 
     * @param block
     *          to part elements
     * @return {@link RubyArray} of 2 {@link RubyArray}s
     */
    public RubyArray<RubyArray<E>> partition(Predicate<? super E> block);

    /**
     * Equivalent to {@link #inject(ReduceBlock)}.
     * 
     * @param block
     *          to reduce each element
     * @return element
     */
    public E reduce(BiFunction<E, E, E> block);

    /**
     * Equivalent to {@link #inject(Object, WithInitBlock)}.
     * 
     * @param <I>
     *          the type of transformed elements
     * @param init
     *          initial value
     * @param block
     *          to reduce each element
     * @return object I
     */
    public <I> I reduce(I init, BiFunction<I, ? super E, I> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N reject();

    /**
     * Filters all elements which are true returned by the block.
     * 
     * @param block
     *          to filter elements
     * @return {@link Enumerable}
     */
    public Z reject(Predicate<? super E> block);

    /**
     * Returns a reversed enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N reverseEach();

    /**
     * Iterates each element reversely by given block.
     * 
     * @param block
     *          to yield each element
     * @return {@link Enumerable}
     */
    public Enumerable<E, N, Z> reverseEach(Consumer<? super E> block);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N select();

    /**
     * Equivalent to {@link #findAll(Predicate)}
     * 
     * @param block
     *          to filter elements
     * @return {@link Enumerable}
     */
    public Z select(Predicate<? super E> block);

    /**
     * Groups elements into {@link RubyArray}s and the last element of each
     * {@link RubyArray} should get true returned by the block.
     * 
     * @param block
     *          to check where to do slice
     * @return {@link Enumerator}
     */
    public N sliceAfter(Predicate<? super E> block);

    /**
     * Groups elements into {@link RubyArray}s and the last element of each
     * {@link RubyArray} should be matched by the regex.
     * 
     * @param regex
     *          to check where to do slice
     * @return {@link Enumerator}
     */
    public N sliceAfter(String regex);

    /**
     * Groups elements into {@link RubyArray}s and the first element of each
     * {@link RubyArray} should get true returned by the block.
     * 
     * @param block
     *          to check where to do slice
     * @return {@link Enumerator}
     */
    public N sliceBefore(Predicate<? super E> block);

    /**
     * Groups elements into {@link RubyArray}s and the first element of each
     * {@link RubyArray} should be matched by the regex.
     * 
     * @param regex
     *          to check where to do slice
     * @return {@link Enumerator}
     */
    public N sliceBefore(String regex);

    /**
     * Groups elements into {@link RubyArray}s and the first element of each
     * {@link RubyArray} should get true returned by the block.
     * 
     * @param block
     *          to check where to do slice
     * @return {@link Enumerator}
     */
    public N sliceWhen(BiPredicate<? super E, ? super E> block);

    /**
     * Sorts elements and puts them into a {@link RubyArray}.
     * 
     * @return {@link RubyArray}
     * @throws IllegalArgumentException
     *           when any 2 elements are not comparable
     */
    public RubyArray<E> sort();

    // /**
    // * Sorts elements by given Comparator, then puts them into a
    // * {@link RubyArray}.
    // *
    // * @param comp
    // * a Comparator
    // * @return {@link RubyArray}
    // */
    // public RubyArray<E> sort(Comparator<? super E> comp);

    /**
     * Returns an enumerator of elements.
     * 
     * @return {@link Enumerator}
     */
    public N sortBy();

    /**
     * Sorts elements by the ordering of outputs transformed by the block
     * induced by the Comparator, then puts them into a {@link RubyArray}.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param comp
     *          a Comparator
     * @param block
     *          to transform elements
     * @return {@link RubyArray}
     */
    public <S> RubyArray<E> sortBy(Comparator<? super S> comp,
        Function<? super E, ? extends S> block);

    /**
     * Sorts elements by the ordering of outputs transformed by the block
     * induced by the Comparator for S and applies the Comparator for E again
     * before puts them into a {@link RubyArray}.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param comp1
     *          a Comparator for E
     * @param comp2
     *          a Comparator for S
     * @param block
     *          to transform elements
     * @return {@link RubyArray}
     */
    public <S> RubyArray<E> sortBy(Comparator<? super E> comp1,
        Comparator<? super S> comp2, Function<? super E, ? extends S> block);

    /**
     * Sorts elements by the ordering of outputs transformed by the block, then
     * puts them into a {@link RubyArray}.
     * 
     * @param <S>
     *          the type of transformed elements
     * @param block
     *          to transform elements
     * @return {@link RubyArray}
     */
    public <S> RubyArray<E> sortBy(Function<? super E, ? extends S> block);

    /**
     * Adds up all elements.
     * 
     * @return {@link BigDecimal}
     */
    public BigDecimal sum();

    /**
     * Takes the first n elements.
     * 
     * @param n
     *          number of elements
     * @return {@link Enumerable}
     */
    public Z take(int n);

    /**
     * Returns an enumerator which contains only the first element.
     * 
     * @return {@link Enumerator}
     */
    public N takeWhile();

    /**
     * Takes elements until an element gets false returned by the block.
     * 
     * @param block
     *          to filter elements
     * @return {@link Enumerable}
     */
    public Z takeWhile(Predicate<? super E> block);

    /**
     * Puts all elements into a {@link RubyArray}.
     * 
     * @return {@link RubyArray}
     */
    public RubyArray<E> toA();

    /**
     * Creates a {@link RubyHash} by transforming each element into an
     * {@link Entry}.
     * 
     * @return {@link RubyHash}
     */
    public <K, V> RubyHash<K, V> toH(Function<E, Entry<K, V>> block);

    /**
     * Creates a {@link RubyHash} by transforming each 2 elements into an
     * {@link Entry}.
     * 
     * @return {@link RubyHash}
     */
    public <K, V> RubyHash<K, V> toH(BiFunction<E, E, Entry<K, V>> block);

    /**
     * Groups elements which get the same indices among all other Iterables into
     * {@link RubyArray}s.
     * 
     * @param others
     *          an array of Iterable
     * @return {@link Enumerable}
     */
    public Z zip(
        @SuppressWarnings("unchecked") Iterable<? extends E>... others);

    /**
     * Groups elements which get the same indices among all other Lists into
     * {@link RubyArray}s.
     * 
     * @param others
     *          a List of Iterable
     * @return {@link Enumerable}
     */
    public Z zip(List<? extends Iterable<? extends E>> others);

    /**
     * Groups elements which get the same indices among all other Iterables into
     * {@link RubyArray}s and yields them to the block.
     * 
     * @param others
     *          a List of Iterables
     * @param block
     *          to yield zipped elements
     */
    public void zip(List<? extends Iterable<? extends E>> others,
        Consumer<? super RubyArray<E>> block);
  }

  /**
   * 
   * {@link Enumerator} defines all methods of a Ruby Enumerator should have.
   * 
   * @param <E>
   *          the type of the elements
   * @param <N>
   *          the type of what kind of {@link Enumerator} should be used
   * @param <Z>
   *          the type to define if some methods are lazy or eager loading
   */
  interface Enumerator<E, N extends Enumerator<?, ?, ?>, Z extends Enumerable<?, ?, ?>>
      extends Enumerable<E, N, Z>, Iterable<E>, Iterator<E> {

    /**
     * Rewinds this Iterator to the beginning.
     * 
     * @return this {@link Enumerator}
     */
    public N rewind();

    /**
     * Returns the next element without advancing the iteration.
     * 
     * @return element
     */
    public E peek();

  }

}
