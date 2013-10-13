package net.sf.rubycollect4j;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyHash;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.sf.rubycollect4j.iter.DropIterable;
import net.sf.rubycollect4j.iter.DropWhileIterable;
import net.sf.rubycollect4j.iter.EachConsIterable;
import net.sf.rubycollect4j.iter.EachSliceIterable;
import net.sf.rubycollect4j.iter.EachWithIndexIterable;
import net.sf.rubycollect4j.iter.EachWithObjectIterable;
import net.sf.rubycollect4j.iter.FindAllIterable;
import net.sf.rubycollect4j.iter.FlattenIterable;
import net.sf.rubycollect4j.iter.GrepIterable;
import net.sf.rubycollect4j.iter.SliceBeforeIterable;
import net.sf.rubycollect4j.iter.TakeIterable;
import net.sf.rubycollect4j.iter.TakeWhileIterable;
import net.sf.rubycollect4j.iter.TransformIterable;
import net.sf.rubycollect4j.util.PeekingIterator;

public final class LazyRubyEnumerator<E> implements Iterable<E>, Iterator<E> {

  private final Iterable<E> iter;
  private PeekingIterator<E> pIterator;

  public LazyRubyEnumerator(Iterable<E> iter) {
    this.iter = iter;
    pIterator = new PeekingIterator<E>(iter.iterator());
  }

  /**
   * Checks if null included.
   * 
   * @return true if null is found, false otherwise
   */
  public boolean allʔ() {
    for (E item : iter) {
      if (item == null)
        return false;
    }
    return true;
  }

  /**
   * Checks if any result returned by the block is false.
   * 
   * @param block
   *          to check elements
   * @return true if all result are true, false otherwise
   */
  public boolean allʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item) == false)
        return false;
    }
    return true;
  }

  /**
   * Checks if any not-null object included.
   * 
   * @return true if not-null object is found, false otherwise
   */
  public boolean anyʔ() {
    for (E item : iter) {
      if (item != null)
        return true;
    }
    return false;
  }

  /**
   * Checks if any result returned by the block is true.
   * 
   * @param block
   *          to check elements
   * @return true if any result are true, false otherwise
   */
  public boolean anyʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item))
        return true;
    }
    return false;
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
   * @return a LazyRubyEnumerator
   */
  public <S> LazyRubyEnumerator<Entry<S, RubyArray<E>>> chunk(
      TransformBlock<E, S> block) {
    return new LazyRubyEnumerator<Entry<S, RubyArray<E>>>(
        new ChunkIterable<E, S>(iter, block));
  }

  /**
   * Returns a RubyEnumerator of this RubyEnumerable.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> collect() {
    return new LazyRubyEnumerator<E>(iter);
  }

  /**
   * Transforms each element by the block lazily to a LazyRubyEnumerator.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a LazyRubyEnumerator
   */
  public <S> LazyRubyEnumerator<S> collect(TransformBlock<E, S> block) {
    return new LazyRubyEnumerator<S>(new TransformIterable<E, S>(iter, block));
  }

  /**
   * Turns each element into a RubyArray and then flattens it.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a LazyRubyEnumerator
   */
  public <S> LazyRubyEnumerator<S> collectConcat(
      TransformBlock<E, RubyArray<S>> block) {
    return new LazyRubyEnumerator<S>(new FlattenIterable<E, S>(iter, block));
  }

  /**
   * Counts the elements.
   * 
   * @return a int
   */
  public int count() {
    int count = 0;
    for (@SuppressWarnings("unused")
    E item : iter) {
      count++;
    }
    return count;
  }

  /**
   * Counts the elements which are true returned by the block.
   * 
   * @param block
   *          to define elements to be counted
   * @return a int
   */
  public int count(BooleanBlock<E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item))
        count++;
    }
    return count;
  }

  /**
   * Generates a sequence from start element to end element and so on
   * infinitely.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> cycle() {
    return new LazyRubyEnumerator<E>(new CycleIterable<E>(iter));
  }

  /**
   * Generates a sequence from start element to end element, repeat n times.
   * 
   * @param n
   *          times to repeat
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> cycle(int n) {
    return new LazyRubyEnumerator<E>(new CycleIterable<E>(iter, n));
  }

  /**
   * Generates a sequence from start element to end element, repeat n times.
   * Yields each element to the block.
   * 
   * @param n
   *          times to repeat
   * @param block
   *          to yield each element
   */
  public void cycle(int n, Block<E> block) {
    for (int i = 0; i < n; i++) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  /**
   * Generates a sequence from start element to end element and so on
   * infinitely. Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   */
  public void cycle(Block<E> block) {
    while (true) {
      for (E item : iter) {
        block.yield(item);
      }
    }
  }

  /**
   * Returns a LazyRubyEnumerator which is self.
   * 
   * @return this LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> each() {
    return this;
  }

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return this LazyRubyEnumerator
   */
  public void each(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return this LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> detect() {
    return this;
  }

  /**
   * Finds the first element which gets true returned by the block. Returns null
   * if element is not found.
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E detect(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item))
        return item;
    }
    return null;
  }

  /**
   * Drops the first n elements and store rest to a RubyArray.
   * 
   * @param n
   *          number of elements to drop
   * @return a RubyArray
   */
  public LazyRubyEnumerator<E> drop(int n) {
    return new LazyRubyEnumerator<E>(new DropIterable<E>(iter, n));
  }

  /**
   * Drops the first n elements until a element gets false returned by the
   * block.
   * 
   * @param block
   *          to define which elements to be dropped
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> dropWhile(BooleanBlock<E> block) {
    return new LazyRubyEnumerator<E>(new DropWhileIterable<E>(iter, block));
  }

  /**
   * Iterates each element and stores the element with n - 1 consecutive
   * elements into a RubyArray.
   * 
   * @param n
   *          number of consecutive elements
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<RubyArray<E>> eachCons(int n) {
    return new LazyRubyEnumerator<RubyArray<E>>(
        new EachConsIterable<E>(iter, n));
  }

  /**
   * Iterates each element and yields the element with n - 1 consecutive
   * elements to the block.
   * 
   * @param n
   *          number of consecutive elements
   * @param block
   *          to yield the RubyArray of consecutive elements
   */
  public void eachCons(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> cons : eachCons(n)) {
      block.yield(cons);
    }
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> eachEntry() {
    return new LazyRubyEnumerator<E>(iter);
  }

  /**
   * Yields each element to the block.
   * 
   * @param block
   *          to yield each element
   * @return this LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> eachEntry(Block<E> block) {
    for (E item : iter) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Slices elements into RubyArrays with length n.
   * 
   * @param n
   *          size of each slice
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<RubyArray<E>> eachSlice(int n) {
    return new LazyRubyEnumerator<RubyArray<E>>(new EachSliceIterable<E>(iter,
        n));
  }

  /**
   * Slices elements into RubyArrays with length n and yield them to the block.
   * 
   * @param n
   *          size of each slice
   * @param block
   *          to yield each slice
   */
  public void eachSlice(int n, Block<RubyArray<E>> block) {
    for (RubyArray<E> ra : new EachSliceIterable<E>(iter, n)) {
      block.yield(ra);
    }
  }

  /**
   * Iterates elements with their indices by Entry.
   * 
   * @return a RubyEnumerator
   */
  public LazyRubyEnumerator<Entry<E, Integer>> eachWithIndex() {
    return new LazyRubyEnumerator<Entry<E, Integer>>(
        new EachWithIndexIterable<E>(iter));
  }

  /**
   * Iterates elements with their indices by Entry and yields them to the block.
   * 
   * @param block
   *          to yield each Entry
   * @return a RubyArray
   */
  public RubyArray<E> eachWithIndex(WithIndexBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    int i = 0;
    for (E item : iter) {
      block.yield(item, i);
      rubyArray.add(item);
      i++;
    }
    return rubyArray;
  }

  /**
   * Iterates elements with the Object S.
   * 
   * @param <S>
   *          the type of transformed elements
   * @param o
   *          any Object
   * @return a LazyRubyEnumerator
   */
  public <S> LazyRubyEnumerator<Entry<E, S>> eachWithObject(S o) {
    return new LazyRubyEnumerator<Entry<E, S>>(
        new EachWithObjectIterable<E, S>(iter, o));
  }

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
  public <S> S eachWithObject(S o, WithObjectBlock<E, S> block) {
    for (E item : iter) {
      block.yield(item, o);
    }
    return o;
  }

  /**
   * Stores each element into a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> entries() {
    return newRubyArray(iter);
  }

  /**
   * Equivalent to detect().
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> find() {
    return detect();
  }

  /**
   * Equivalent to detect().
   * 
   * @param block
   *          to filter elements
   * @return an element or null
   */
  public E find(BooleanBlock<E> block) {
    return detect(block);
  }

  /**
   * Finds all elements which are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> findAll(BooleanBlock<E> block) {
    return new LazyRubyEnumerator<E>(new FindAllIterable<E>(iter, block));
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> findIndex() {
    return new LazyRubyEnumerator<E>(iter);
  }

  /**
   * Finds the index of a element which is true returned by the block. Returns
   * null if nothing found.
   * 
   * @param block
   *          to check elements
   * @return an Integer or null
   */
  public Integer findIndex(BooleanBlock<E> block) {
    int index = 0;
    for (E item : iter) {
      if (block.yield(item))
        return index;

      index++;
    }
    return null;
  }

  /**
   * Finds the index of the target element. Returns null if target not found.
   * 
   * @param target
   *          to be found
   * @return an Integer or null
   */
  public Integer findIndex(E target) {
    int index = 0;
    for (E item : iter) {
      if (item.equals(target))
        return index;

      index++;
    }
    return null;
  }

  /**
   * Gets the first element of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E first() {
    Iterator<E> iterator = iter.iterator();
    if (iterator.hasNext())
      return iterator.next();
    else
      return null;
  }

  /**
   * Gets the first n element of this RubyEnumerable.
   * 
   * @param n
   *          number of elements
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           if n less than 0
   */
  public RubyArray<E> first(int n) {
    if (n < 0)
      throw new IllegalArgumentException(
          "ArgumentError: attempt to take negative size");

    Iterator<E> it = iter.iterator();
    RubyArray<E> rubyArray = newRubyArray();
    for (int i = 0; i < n && it.hasNext(); i++) {
      rubyArray.add(it.next());
    }
    return rubyArray;
  }

  /**
   * Equivalent to collectConcat().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to take element and generate a RubyArray
   * @return a LazyRubyEnumerator
   */
  public <S> LazyRubyEnumerator<S>
      flatMap(TransformBlock<E, RubyArray<S>> block) {
    return collectConcat(block);
  }

  /**
   * Finds all elements which are matched by the regular expression.
   * 
   * @param regex
   *          regular expression
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> grep(String regex) {
    return new LazyRubyEnumerator<E>(new GrepIterable<E>(iter, regex));
  }

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
   * @return a LazyRubyEnumerator
   */
  public <S> LazyRubyEnumerator<S>
      grep(String regex, TransformBlock<E, S> block) {
    return new LazyRubyEnumerator<S>(new TransformIterable<E, S>(
        new GrepIterable<E>(iter, regex), block));
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> groupBy() {
    return new LazyRubyEnumerator<E>(iter);
  }

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
  public <S> RubyHash<S, RubyArray<E>> groupBy(TransformBlock<E, S> block) {
    Map<S, List<E>> multimap = new LinkedHashMap<S, List<E>>();
    for (E item : iter) {
      S key = block.yield(item);
      if (!multimap.containsKey(key))
        multimap.put(key, new ArrayList<E>());

      multimap.get(key).add(item);
    }
    RubyHash<S, RubyArray<E>> map = newRubyHash();
    for (S key : multimap.keySet()) {
      map.put(key, newRubyArray(multimap.get(key)));
    }
    return map;
  }

  /**
   * Checks if target element included.
   * 
   * @param target
   *          to be searched
   * @return true if target is found,false otherwise
   */
  public boolean includeʔ(E target) {
    for (E item : iter) {
      if (item.equals(target))
        return true;
    }
    return false;
  }

  /**
   * Assigns the first element as the initial value. Reduces each element with
   * block, then assigns the result back to initial value and so on.
   * 
   * @param block
   *          to reduce each element
   * @return an element
   */
  public E inject(ReduceBlock<E> block) {
    E result = null;
    int i = 0;
    for (E item : iter) {
      if (i == 0)
        result = item;
      else
        result = block.yield(result, item);
      i++;
    }
    return result;
  }

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
  public <S> S inject(S init, WithInitBlock<E, S> block) {
    for (E item : iter) {
      init = block.yield(init, item);
    }
    return init;
  }

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
  @SuppressWarnings("unchecked")
  public <S> S inject(S init, String methodName) {
    S result = init;
    Iterator<E> iterator = iter.iterator();
    while (iterator.hasNext()) {
      E curr = iterator.next();
      try {
        Method[] methods = result.getClass().getDeclaredMethods();
        boolean isInvoked = false;
        for (Method method : methods) {
          if (method.getName().equals(methodName) && !method.isVarArgs()) {
            result = (S) method.invoke(result, curr);
            isInvoked = true;
            break;
          }
        }
        if (!isInvoked) {
          throw new IllegalArgumentException(
              "NoMethodError: undefined method `" + methodName + "' for "
                  + result);
        }
      } catch (IllegalAccessException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
            null, ex);
        throw new RuntimeException(ex);
      } catch (InvocationTargetException ex) {
        Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
            null, ex);
        throw new RuntimeException(ex);
      }
    }
    return result;
  }

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
  @SuppressWarnings("unchecked")
  public E inject(String methodName) {
    E result = null;
    Iterator<E> iterator = iter.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      if (i == 0) {
        result = iterator.next();
      } else {
        E curr = iterator.next();
        try {
          Method[] methods = result.getClass().getDeclaredMethods();
          boolean isInvoked = false;
          for (Method method : methods) {
            if (method.getName().equals(methodName) && !method.isVarArgs()) {
              result = (E) method.invoke(result, curr);
              isInvoked = true;
              break;
            }
          }
          if (!isInvoked) {
            throw new IllegalArgumentException(
                "NoMethodError: undefined method `" + methodName + "' for "
                    + result);
          }
        } catch (IllegalAccessException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
              null, ex);
          throw new RuntimeException(ex);
        } catch (InvocationTargetException ex) {
          Logger.getLogger(RubyEnumerable.class.getName()).log(Level.SEVERE,
              null, ex);
          throw new RuntimeException(ex);
        }
      }
      i++;
    }
    return result;
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> lazy() {
    return new LazyRubyEnumerator<E>(iter);
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> map() {
    return collect();
  }

  /**
   * Equivalent to collect().
   * 
   * @param <S>
   *          the type of transformed elements
   * @param block
   *          to transform elements
   * @return a LazyRubyEnumerator
   */
  public <S> LazyRubyEnumerator<S> map(TransformBlock<E, S> block) {
    return collect(block);
  }

  /**
   * Finds the max element of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E max() {
    return sort().last();
  }

  /**
   * Finds the max element induced by the Comparator of this RubyEnumerable.
   * Returns null if this RubyEnumerable is empty.
   * 
   * @param comp
   *          a Comparator
   * @return an element or null
   */
  public E max(Comparator<? super E> comp) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
    if (list.isEmpty())
      return null;

    return Collections.max(list, comp);
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> maxBy() {
    return new LazyRubyEnumerator<E>(iter);
  }

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
  public <S> E maxBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S maxDst = Collections.max(dst, comp);
    return src.get(dst.indexOf(maxDst));
  }

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
  public <S> E maxBy(TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S maxDst = newRubyEnumerator(dst).max();
    return src.get(dst.indexOf(maxDst));
  }

  /**
   * Equivalent to includeʔ().
   * 
   * @param target
   *          to be found
   * @return an element or null
   */
  public boolean memberʔ(E target) {
    return includeʔ(target);
  }

  /**
   * Finds the min element of this RubyEnumerable. Returns null if this
   * RubyEnumerable is empty.
   * 
   * @return an element or null
   */
  public E min() {
    return sort().first();
  }

  /**
   * Finds the min element induced by the Comparator of this RubyEnumerable.
   * Returns null if this RubyEnumerable is empty.
   * 
   * @param comp
   *          a Comparator
   * @return an element or null
   */
  public E min(Comparator<? super E> comp) {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(item);
    }
    if (list.isEmpty())
      return null;

    return Collections.min(list, comp);
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> minBy() {
    return new LazyRubyEnumerator<E>(iter);
  }

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
  public <S> E minBy(Comparator<? super S> comp, TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S minDst = Collections.min(dst, comp);
    return src.get(dst.indexOf(minDst));
  }

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
  public <S> E minBy(TransformBlock<E, S> block) {
    List<E> src = new ArrayList<E>();
    List<S> dst = new ArrayList<S>();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return null;

    S minDst = newRubyEnumerator(dst).min();
    return src.get(dst.indexOf(minDst));
  }

  /**
   * Finds the min and max elements of this RubyEnumerable and stores them into
   * a RubyArray.
   * 
   * @return a RubyArray
   */
  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax() {
    RubyArray<E> rubyArray = sort();
    return newRubyArray(rubyArray.first(), rubyArray.last());
  }

  /**
   * Finds the min and max elements induced by the Comparator of this
   * RubyEnumerable and stores them into a RubyArray.
   * 
   * @param comp
   *          a Comparator
   * @return a RubyArray
   */
  @SuppressWarnings("unchecked")
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.isEmpty())
      return newRubyArray(null, null);

    return newRubyArray(Collections.min(rubyArray, comp),
        Collections.max(rubyArray, comp));
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> minmaxBy() {
    return new LazyRubyEnumerator<E>(iter);
  }

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
  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp,
      TransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return newRubyArray(null, null);

    S minDst = Collections.min(dst, comp);
    S maxDst = Collections.max(dst, comp);
    return newRubyArray(src.get(dst.indexOf(minDst)),
        src.get(dst.indexOf(maxDst)));
  }

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
  @SuppressWarnings("unchecked")
  public <S> RubyArray<E> minmaxBy(TransformBlock<E, S> block) {
    RubyArray<E> src = newRubyArray();
    RubyArray<S> dst = newRubyArray();
    for (E item : iter) {
      src.add(item);
      dst.add(block.yield(item));
    }
    if (src.isEmpty())
      return newRubyArray(null, null);

    S minDst = newRubyEnumerator(dst).min();
    S maxDst = newRubyEnumerator(dst).max();
    return newRubyArray(src.get(dst.indexOf(minDst)),
        src.get(dst.indexOf(maxDst)));
  }

  /**
   * Checks if this RubyEnumerable contains only null objects.
   * 
   * @return true if all elements are null, false otherwise
   */
  public boolean noneʔ() {
    for (E item : iter) {
      if (item != null)
        return false;
    }
    return true;
  }

  /**
   * Checks if this RubyEnumerable contains only elements which are false
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if all results of block are false, false otherwise
   */
  public boolean noneʔ(BooleanBlock<E> block) {
    for (E item : iter) {
      if (block.yield(item))
        return false;
    }
    return true;
  }

  /**
   * Checks if this RubyEnumerable contains only one element beside null
   * objects.
   * 
   * @return true if only one element and nulls are found, false otherwise
   */
  public boolean oneʔ() {
    int count = 0;
    for (E item : iter) {
      if (item != null) {
        count++;
        if (count > 1)
          return false;
      }
    }
    return count == 1;
  }

  /**
   * Checks if this RubyEnumerable contains only one element which are true
   * returned by the block.
   * 
   * @param block
   *          to check elements
   * @return true if only one result of block is true, false otherwise
   */
  public boolean oneʔ(BooleanBlock<E> block) {
    int count = 0;
    for (E item : iter) {
      if (block.yield(item)) {
        count++;
        if (count > 1)
          return false;
      }
    }
    return count == 1;
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> partition() {
    return new LazyRubyEnumerator<E>(iter);
  }

  /**
   * Divides elements into 2 groups by the given block.
   * 
   * @param block
   *          to part elements
   * @return a RubyArray of 2 RubyArrays
   */
  @SuppressWarnings("unchecked")
  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block) {
    RubyArray<E> trueList = newRubyArray();
    RubyArray<E> falseList = newRubyArray();
    for (E item : iter) {
      if (block.yield(item))
        trueList.add(item);
      else
        falseList.add(item);
    }
    return newRubyArray(trueList, falseList);
  }

  /**
   * Equivalent to inject().
   * 
   * @param block
   *          to reduce each element
   * @return an element
   */
  public E reduce(ReduceBlock<E> block) {
    return inject(block);
  }

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
  public <S> S reduce(S init, WithInitBlock<E, S> block) {
    return inject(init, block);
  }

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
  public <S> S reduce(S init, String methodName) {
    return inject(init, methodName);
  }

  /**
   * Equivalent to inject().
   * 
   * @param methodName
   *          method used to reduce elements
   * @return an element
   */
  public E reduce(String methodName) {
    return inject(methodName);
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> reject() {
    return new LazyRubyEnumerator<E>(iter);
  }

  /**
   * Deletes elements which are true returned by the block.
   * 
   * @param block
   *          to filter elements
   * @return a RubyArray
   */
  public RubyArray<E> reject(BooleanBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      if (!block.yield(item))
        rubyArray.add(item);
    }
    return rubyArray;
  }

  /**
   * Returns a reversed LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> reverseEach() {
    List<E> list = new ArrayList<E>();
    for (E item : iter) {
      list.add(0, item);
    }
    return new LazyRubyEnumerator<E>(list);
  }

  /**
   * Iterates each element reversed by given block.
   * 
   * @param block
   *          to yield each element
   * @return this LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> reverseEach(Block<E> block) {
    for (E item : reverseEach()) {
      block.yield(item);
    }
    return this;
  }

  /**
   * Equivalent to findAll().
   * 
   * @param block
   *          to filter elements
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> select(BooleanBlock<E> block) {
    return findAll(block);
  }

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should get true returned by the block.
   * 
   * @param block
   *          to check where to do slice
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<RubyArray<E>> sliceBefore(BooleanBlock<E> block) {
    return new LazyRubyEnumerator<RubyArray<E>>(new SliceBeforeIterable<E>(
        iter, block));
  }

  /**
   * Groups elements into RubyArrays and the first element of each RubyArray
   * should be matched by the regex.
   * 
   * @param regex
   *          to check where to do slice
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<RubyArray<E>> sliceBefore(String regex) {
    return new LazyRubyEnumerator<RubyArray<E>>(new SliceBeforeIterable<E>(
        iter, Pattern.compile(regex)));
  }

  /**
   * Sorts elements of this RubyEnumerable and stores them into a RubyArray.
   * 
   * @return a RubyArray
   * @throws IllegalArgumentException
   *           when any 2 elements are not comparable
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public RubyArray<E> sort() {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() <= 1)
      return rubyArray;

    try {
      Collections.sort(rubyArray, new Comparator() {

        @Override
        public int compare(Object arg0, Object arg1) {
          return ((Comparable) arg0).compareTo(arg1);
        }

      });
      return rubyArray;
    } catch (Exception e) {
      if (rubyArray.uniq().count() == 1)
        return rubyArray;

      Iterator<E> iter = rubyArray.iterator();
      E sample = iter.next();
      E error = null;
      while (iter.hasNext()) {
        error = iter.next();
        try {
          ((Comparable) sample).compareTo(error);
        } catch (Exception ex) {
          break;
        }
      }
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + (sample == null ? "null" : sample.getClass().getName()) + " with "
          + (error == null ? "null" : error.getClass().getName()) + " failed");
    }
  }

  /**
   * Sorts elements of this RubyEnumerable by given Comparator and stores them
   * into a RubyArray.
   * 
   * @param comp
   *          a Comparator
   * @return a RubyArray
   */
  public RubyArray<E> sort(Comparator<? super E> comp) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    if (rubyArray.size() <= 1)
      return rubyArray;

    Collections.sort(rubyArray, comp);
    return rubyArray;
  }

  /**
   * Returns a LazyRubyEnumerator.
   * 
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> sortBy() {
    return new LazyRubyEnumerator<E>(iter);
  }

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
      TransformBlock<E, S> block) {
    Map<S, RubyArray<E>> multimap = new LinkedHashMap<S, RubyArray<E>>();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      S key = block.yield(item);
      if (!multimap.containsKey(key))
        multimap.put(key, new RubyArray<E>());

      multimap.get(key).add(item);
    }
    List<S> keys = new ArrayList<S>(multimap.keySet());
    Collections.sort(keys, comp);
    for (S key : keys) {
      for (E item : multimap.get(key).sortǃ()) {
        sortedList.add(item);
      }
    }
    return sortedList;
  }

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
      Comparator<? super S> comp2, TransformBlock<E, S> block) {
    Map<S, RubyArray<E>> multimap = new LinkedHashMap<S, RubyArray<E>>();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      S key = block.yield(item);
      if (!multimap.containsKey(key))
        multimap.put(key, new RubyArray<E>());

      multimap.get(key).add(item);
    }
    List<S> keys = new ArrayList<S>(multimap.keySet());
    Collections.sort(keys, comp2);
    for (S key : keys) {
      for (E item : multimap.get(key).sortǃ(comp1)) {
        sortedList.add(item);
      }
    }
    return sortedList;
  }

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
  public <S> RubyArray<E> sortBy(TransformBlock<E, S> block) {
    Map<S, RubyArray<E>> multimap = new LinkedHashMap<S, RubyArray<E>>();
    RubyArray<E> sortedList = newRubyArray();
    for (E item : iter) {
      S key = block.yield(item);
      if (!multimap.containsKey(key))
        multimap.put(key, new RubyArray<E>());

      multimap.get(key).add(item);
    }
    List<S> keys = new ArrayList<S>(multimap.keySet());
    keys = newRubyEnumerator(keys).sort();
    for (S key : keys) {
      for (E item : multimap.get(key).sortǃ()) {
        sortedList.add(item);
      }
    }
    return sortedList;
  }

  /**
   * Takes the first n elements.
   * 
   * @param n
   *          number of elements
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> take(int n) {
    return new LazyRubyEnumerator<E>(new TakeIterable<E>(iter, n));
  }

  /**
   * Takes elements until the result returned by the block is false.
   * 
   * @param block
   *          to filter elements
   * @return a LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> takeWhile(BooleanBlock<E> block) {
    return new LazyRubyEnumerator<E>(new TakeWhileIterable<E>(iter, block));
  }

  /**
   * Converts this RubyEnumerable to a RubyArray.
   * 
   * @return a RubyArray
   */
  public RubyArray<E> toA() {
    return newRubyArray(iter);
  }

  /**
   * Groups elements which get the same indices among all other Lists into
   * RubyArrays.
   * 
   * @param others
   *          Lists
   * @return a RubyArray of RubyArrays
   */
  public RubyArray<RubyArray<E>> zip(List<E>... others) {
    return zip(Arrays.asList(others));
  }

  /**
   * Groups elements which get the same indices among all other Lists into
   * RubyArrays.
   * 
   * @param others
   *          List of Lists
   * @return a RubyArray of RubyArrays
   */
  public RubyArray<RubyArray<E>> zip(List<? extends List<E>> others) {
    RubyArray<E> rubyArray = newRubyArray(iter);
    RubyArray<RubyArray<E>> zippedRubyArray = newRubyArray();
    for (int i = 0; i < rubyArray.size(); i++) {
      RubyArray<E> zip = newRubyArray();
      zip.add(rubyArray.at(i));
      for (int j = 0; j < others.size(); j++) {
        List<E> z = others.get(j);
        if (i < z.size())
          zip.add(z.get(i));
        else
          zip.add(null);
      }
      zippedRubyArray.add(zip);
    }
    return zippedRubyArray;
  }

  /**
   * Groups elements which get the same indices among all other Lists into
   * RubyArrays and yields them to the block.
   * 
   * @param others
   *          List of Lists
   * @param block
   *          to yield zipped elements
   */
  public void zip(List<? extends List<E>> others, Block<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> zippedRubyArray = zip(others);
    for (RubyArray<E> item : zippedRubyArray) {
      block.yield(item);
    }
  }

  /**
   * Resets the iterator of this LazyRubyEnumerator to the beginning.
   * 
   * @return this LazyRubyEnumerator
   */
  public LazyRubyEnumerator<E> rewind() {
    pIterator = new PeekingIterator<E>(iter.iterator());
    return this;
  }

  /**
   * Returns the next element without advancing the iteration.
   * 
   * @return an element
   */
  public E peek() {
    return pIterator.peek();
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }

  @Override
  public boolean hasNext() {
    return pIterator.hasNext();
  }

  @Override
  public E next() {
    return pIterator.next();
  }

  @Override
  public void remove() {
    pIterator.remove();
  }

  @Override
  public String toString() {
    return "LazyRubyEnumerator{" + iter + "}";
  }

}
