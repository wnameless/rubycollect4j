package net.sf.rubycollect4j;

import java.util.Comparator;
import java.util.List;

import net.sf.rubycollect4j.block.Block;
import net.sf.rubycollect4j.block.BooleanBlock;
import net.sf.rubycollect4j.block.ReduceBlock;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.block.WithInitBlock;
import net.sf.rubycollect4j.block.WithObjectBlock;

public interface RubyEnumerableEagerMethods<E> {

  /**
   * Checks if null included.
   * 
   * @return true if null is found, false otherwise
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
   * Checks if any not-null object included.
   * 
   * @return true if not-null object is found, false otherwise
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
   * Finds the first element which gets true returned by the block. Returns null
   * if element is not found.
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E detect(BooleanBlock<E> block);

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
   * Slices elements into RubyArrays with length n and yield them to the block.
   * 
   * @param n
   *          size of each slice
   * @param block
   *          to yield each slice
   */
  public void eachSlice(int n, Block<RubyArray<E>> block);

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
   * Stores each element into a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> entries();

  /**
   * Equivalent to detect().
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E find(BooleanBlock<E> block);

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
   * Finds the min and max elements of this RubyEnumerable and stores them into
   * a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> minmax();

  /**
   * Finds the min and max elements induced by the Comparator of this
   * RubyEnumerable and stores them into a RubyArray.
   * 
   * @param comp
   *          a Comparator
   * @return a RubyArray
   */
  public RubyArray<E> minmax(Comparator<? super E> comp);

  /**
   * Finds elements which are the min and max elements induced by the Comparator
   * transformed by the block of this RubyEnumerable and stores them into a
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
   * of this RubyEnumerable and stores them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> minmaxBy(TransformBlock<E, S> block);

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
   * Sorts elements of this RubyEnumerable and stores them into a RubyArray.
   * 
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           when any 2 elements are not comparable
   */
  public RubyArray<E> sort();

  /**
   * Sorts elements of this RubyEnumerable by given Comparator and stores them
   * into a RubyArray.
   * 
   * @param comp
   *          a Comparator
   * @return a RubyArray
   */
  public RubyArray<E> sort(Comparator<? super E> comp);

  /**
   * Sorts elements of this RubyEnumerable by the ordering of elements
   * transformed by the block induced by the Comparator and stores them into a
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
   * Comparator for E again before stores them into a RubyArray.
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
   * transformed by the block and stores them into a RubyArray.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a RubyArray
   */
  public <S> RubyArray<E> sortBy(TransformBlock<E, S> block);

  /**
   * Converts this RubyEnumerable into a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> toA();

  /**
   * Groups elements which get the same indices among all other Lists into
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