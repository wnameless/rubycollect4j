/*
 *
 * Copyright 2016 Wei-Ming Wu
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
package net.sf.rubycollect4j.extension;

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newRubyEnumerator;
import static net.sf.rubycollect4j.RubyCollections.newRubyLazyEnumerator;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.RubyHash;
import net.sf.rubycollect4j.iter.ChunkIterable;
import net.sf.rubycollect4j.iter.ChunkWhileIterable;
import net.sf.rubycollect4j.iter.CycleIterable;
import net.sf.rubycollect4j.iter.EachConsIterable;
import net.sf.rubycollect4j.iter.EachSliceIterable;
import net.sf.rubycollect4j.iter.EachWithIndexIterable;
import net.sf.rubycollect4j.iter.EachWithObjectIterable;
import net.sf.rubycollect4j.iter.ReverseEachIterable;
import net.sf.rubycollect4j.iter.SliceAfterIterable;
import net.sf.rubycollect4j.iter.SliceBeforeIterable;
import net.sf.rubycollect4j.iter.SliceWhenIterable;

/**
 * 
 * {@link RubyIterables} is simply a utility class. It provides the Ruby style
 * way to manipulate any Iterable.
 * 
 * @author Wei-Ming Wu
 *
 */
public final class RubyIterables {

  private RubyIterables() {}

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#allʔ()
   */
  public static <E> boolean allʔ(Iterable<E> in) {
    return newRubyLazyEnumerator(in).allʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#allʔ(Predicate)
   */
  public static <E> boolean allʔ(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).allʔ(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#anyʔ()
   */
  public static <E> boolean anyʔ(Iterable<E> in) {
    return newRubyLazyEnumerator(in).anyʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#anyʔ(Predicate)
   */
  public static <E> boolean anyʔ(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).anyʔ(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#chunk(Function)
   */
  public static <E, S> Iterable<Entry<S, RubyArray<E>>> chunk(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return newRubyEnumerator(new ChunkIterable<E, S>(in, block));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#chunkWhile(BiPredicate)
   */
  public static <E> Iterable<RubyArray<E>> chunkWhile(Iterable<E> in,
      BiPredicate<? super E, ? super E> block) {
    return newRubyEnumerator(new ChunkWhileIterable<E>(in, block));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#collect(Function)
   */
  @SuppressWarnings("unchecked")
  public static <E, S> List<S> collect(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return (List<S>) newRubyLazyEnumerator(in).collect(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#collectConcat(Function)
   */
  @SuppressWarnings("unchecked")
  public static <E, S> List<S> collectConcat(Iterable<E> in,
      Function<? super E, ? extends List<? extends S>> block) {
    return (List<S>) newRubyLazyEnumerator(in).collectConcat(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#count()
   */
  public static <E> int count(Iterable<E> in) {
    return newRubyLazyEnumerator(in).count();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#count(Predicate)
   */
  public static <E> int count(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).count(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#cycle()
   */
  public static <E> Iterable<E> cycle(Iterable<E> in) {
    return new CycleIterable<E>(in);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#cycle(Consumer)
   */
  public static <E> void cycle(Iterable<E> in, Consumer<? super E> block) {
    newRubyLazyEnumerator(in).cycle(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#cycle(int)
   */
  public static <E> Iterable<E> cycle(Iterable<E> in, int n) {
    return new CycleIterable<E>(in, n);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#cycle(int, Consumer)
   */
  public static <E> void cycle(Iterable<E> in, int n,
      Consumer<? super E> block) {
    newRubyLazyEnumerator(in).cycle(n, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#detect(Predicate)
   */
  public static <E> E detect(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).detect(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#drop(int)
   */
  public static <E> List<E> drop(Iterable<E> in, int n) {
    return newRubyLazyEnumerator(in).drop(n).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#dropWhile(Predicate)
   */
  public static <E> List<E> dropWhile(Iterable<E> in,
      Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).dropWhile(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#each(Consumer)
   */
  public static <E> Iterable<E> each(Iterable<E> in,
      Consumer<? super E> block) {
    newRubyEnumerator(in).each(block);
    return in;
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachCons(int)
   */
  public static <E> Iterable<? extends List<E>> eachCons(Iterable<E> in,
      int n) {
    return new EachConsIterable<E>(in, n);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachCons(int, Consumer)
   */
  public static <E> void eachCons(Iterable<E> in, int n,
      Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(in).eachCons(n, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachEntry(Consumer)
   */
  public static <E> Iterable<E> eachEntry(Iterable<E> in,
      Consumer<? super E> block) {
    for (E item : in) {
      block.accept(item);
    }
    return in;
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachSlice(int)
   */
  public static <E> Iterable<? extends List<E>> eachSlice(Iterable<E> in,
      int n) {
    return newRubyEnumerator(new EachSliceIterable<E>(in, n));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachSlice(int, Consumer)
   */
  public static <E> void eachSlice(Iterable<E> in, int n,
      Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(in).eachSlice(n, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachWithIndex()
   */
  public static <E> Iterable<? extends Entry<E, Integer>> eachWithIndex(
      Iterable<E> in) {
    return new EachWithIndexIterable<E>(in);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachWithIndex(WithIndexBlock)
   */
  public static <E> Iterable<E> eachWithIndex(Iterable<E> in,
      BiConsumer<? super E, Integer> block) {
    newRubyLazyEnumerator(in).eachWithIndex(block);
    return in;
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachWithObject(Object)
   */
  public static <E, O> Iterable<Entry<E, O>> eachWithObject(Iterable<E> in,
      O obj) {
    return new EachWithObjectIterable<E, O>(in, obj);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#eachWithObject(Object,
   *      WithObjectBlock)
   */
  public static <E, O> O eachWithObject(Iterable<E> in, O obj,
      BiConsumer<? super E, ? super O> block) {
    return newRubyLazyEnumerator(in).eachWithObject(obj, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#entries()
   */
  public static <E> List<E> entries(Iterable<E> in) {
    return newRubyArray(in);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#find(Predicate)
   */
  public static <E> E find(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).find(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#find(Predicate)
   */
  public static <E> List<E> findAll(Iterable<E> in,
      Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).findAll(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#findIndex(Predicate)
   */
  public static <E> Integer findIndex(Iterable<E> in,
      Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).findIndex(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#findIndex(Object)
   */
  public static <E> Integer findIndex(Iterable<E> in, E target) {
    return newRubyLazyEnumerator(in).findIndex(target);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#first()
   */
  public static <E> E first(Iterable<E> in) {
    return newRubyLazyEnumerator(in).first();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#first(int)
   */
  public static <E> List<E> first(Iterable<E> in, int n) {
    return newRubyLazyEnumerator(in).first(n);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#flatMap(Function)
   */
  @SuppressWarnings("unchecked")
  public static <S, E> List<S> flatMap(Iterable<E> in,
      Function<? super E, ? extends List<? extends S>> block) {
    return (List<S>) newRubyLazyEnumerator(in).flatMap(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#grep(String)
   */
  public static <E> List<E> grep(Iterable<E> in, String regex) {
    return newRubyLazyEnumerator(in).grep(regex).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#grep(String, Function)
   */
  @SuppressWarnings("unchecked")
  public static <S, E> List<S> grep(Iterable<E> in, String regex,
      Function<? super E, ? extends S> block) {
    return (List<S>) newRubyLazyEnumerator(in).grep(regex, block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#grepV(String)
   */
  public static <E> List<E> grepV(Iterable<E> in, String regex) {
    return (List<E>) newRubyLazyEnumerator(in).grepV(regex).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#grepV(String, Function)
   */
  @SuppressWarnings("unchecked")
  public static <S, E> List<S> grepV(Iterable<E> in, String regex,
      Function<? super E, ? extends S> block) {
    return (List<S>) newRubyLazyEnumerator(in).grepV(regex, block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#groupBy(Function)
   */
  public static <S, E> Map<S, ? extends List<E>> groupBy(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).groupBy(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#includeʔ(Object)
   */
  public static <E> boolean includeʔ(Iterable<E> in, E target) {
    return newRubyLazyEnumerator(in).includeʔ(target);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#inject(ReduceBlock)
   */
  public static <E> E inject(Iterable<E> in, BiFunction<E, E, E> block) {
    return newRubyLazyEnumerator(in).inject(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#inject(Object, WithInitBlock)
   */
  public static <I, E> I inject(Iterable<E> in, I init,
      BiFunction<I, ? super E, I> block) {
    return newRubyLazyEnumerator(in).inject(init, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#lazy()
   */
  public static <E> Iterable<E> lazy(Iterable<E> in) {
    return newRubyLazyEnumerator(in);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#map(Function)
   */
  @SuppressWarnings("unchecked")
  public static <S, E> List<S> map(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return (List<S>) newRubyLazyEnumerator(in).map(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#max()
   */
  public static <E> E max(Iterable<E> in) {
    return newRubyLazyEnumerator(in).max();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#max(Comparator)
   */
  public static <E> E max(Iterable<E> in, Comparator<? super E> comp) {
    return newRubyLazyEnumerator(in).max(comp);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#maxBy(Function)
   */
  public static <S, E> E maxBy(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).maxBy(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#maxBy(Comparator, Function)
   */
  public static <S, E> E maxBy(Iterable<E> in, Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).maxBy(comp, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#memberʔ(Object) TransformBlock)
   */
  public static <E> boolean memberʔ(Iterable<E> in, E target) {
    return newRubyLazyEnumerator(in).memberʔ(target);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#min()
   */
  public static <E> E min(Iterable<E> in) {
    return newRubyLazyEnumerator(in).min();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#min(Comparator)
   */
  public static <E> E min(Iterable<E> in, Comparator<? super E> comp) {
    return newRubyLazyEnumerator(in).min(comp);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#minBy(Function)
   */
  public static <S, E> E minBy(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).minBy(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#minBy(Comparator, Function)
   */
  public static <S, E> E minBy(Iterable<E> in, Comparator<? super S> comp,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).minBy(comp, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#minmax()
   */
  public static <E> List<E> minmax(Iterable<E> in) {
    return newRubyLazyEnumerator(in).minmax();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#minmax(Comparator)
   */
  public static <E> List<E> minmax(Iterable<E> in, Comparator<? super E> comp) {
    return newRubyLazyEnumerator(in).minmax(comp);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#minmaxBy(Function)
   */
  public static <S, E> List<E> minmaxBy(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).minmaxBy(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#minmaxBy(Comparator, Function)
   */
  public static <S, E> List<E> minmaxBy(Iterable<E> in,
      Comparator<? super S> comp, Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).minmaxBy(comp, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#noneʔ()
   */
  public static <E> boolean noneʔ(Iterable<E> in) {
    return newRubyLazyEnumerator(in).noneʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#noneʔ(Predicate)
   */
  public static <E> boolean noneʔ(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).noneʔ(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#oneʔ()
   */
  public static <E> boolean oneʔ(Iterable<E> in) {
    return newRubyLazyEnumerator(in).oneʔ();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#oneʔ(Predicate)
   */
  public static <E> boolean oneʔ(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).oneʔ(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#partition(Predicate)
   */
  public static <E> List<? extends List<E>> partition(Iterable<E> in,
      Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).partition(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#reduce(ReduceBlock)
   */
  public static <E> E reduce(Iterable<E> in, BiFunction<E, E, E> block) {
    return newRubyLazyEnumerator(in).reduce(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#reduce(Object, WithInitBlock)
   */
  public static <I, E> I reduce(Iterable<E> in, I init,
      BiFunction<I, ? super E, I> block) {
    return newRubyLazyEnumerator(in).reduce(init, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#reject(Predicate)
   */
  public static <E> List<E> reject(Iterable<E> in, Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).reject(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#reverseEach()
   */
  public static <E> Iterable<E> reverseEach(Iterable<E> in) {
    return newRubyEnumerator(new ReverseEachIterable<E>(in));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#reverseEach(Consumer)
   */
  public static <E> Iterable<E> reverseEach(Iterable<E> in,
      Consumer<? super E> block) {
    for (E item : reverseEach(in)) {
      block.accept(item);
    }
    return in;
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#select(Predicate)
   */
  public static <E> List<E> select(Iterable<E> in, Predicate<? super E> block) {
    return findAll(in, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sliceAfter(Predicate)
   */
  public static <E> Iterable<? extends List<E>> sliceAfter(Iterable<E> in,
      Predicate<? super E> block) {
    return newRubyEnumerator(new SliceAfterIterable<E>(in, block));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sliceAfter(String)
   */
  public static <E> Iterable<? extends List<E>> sliceAfter(Iterable<E> in,
      String regex) {
    return newRubyEnumerator(
        new SliceAfterIterable<E>(in, Pattern.compile(regex)));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sliceBefore(Predicate)
   */
  public static <E> Iterable<? extends List<E>> sliceBefore(Iterable<E> in,
      Predicate<? super E> block) {
    return newRubyEnumerator(new SliceBeforeIterable<E>(in, block));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sliceBefore(String)
   */
  public static <E> Iterable<? extends List<E>> sliceBefore(Iterable<E> in,
      String regex) {
    return newRubyEnumerator(
        new SliceBeforeIterable<E>(in, Pattern.compile(regex)));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sliceWhen(BiPredicate)
   */
  public static <E> Iterable<? extends List<E>> sliceWhen(Iterable<E> in,
      BiPredicate<? super E, ? super E> block) {
    return newRubyEnumerator(new SliceWhenIterable<E>(in, block));
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sort()
   */
  public static <E> List<E> sort(Iterable<E> in) {
    return newRubyLazyEnumerator(in).sort();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sortBy(Function)
   */
  public static <S, E> List<E> sortBy(Iterable<E> in,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).sortBy(block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sortBy(Comparator, Function)
   */
  public static <S, E> List<E> sortBy(Iterable<E> in,
      Comparator<? super S> comp, Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).sortBy(comp, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#sortBy(Comparator, Comparator,
   *      Function)
   */
  public static <S, E> List<E> sortBy(Iterable<E> in,
      Comparator<? super E> comp1, Comparator<? super S> comp2,
      Function<? super E, ? extends S> block) {
    return newRubyLazyEnumerator(in).sortBy(comp1, comp2, block);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#take(int)
   */
  public static <E> List<E> take(Iterable<E> in, int n) {
    return newRubyLazyEnumerator(in).take(n).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#takeWhile(Predicate)
   */
  public static <E> List<E> takeWhile(Iterable<E> in,
      Predicate<? super E> block) {
    return newRubyLazyEnumerator(in).takeWhile(block).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#toA()
   */
  public static <E> List<E> toA(Iterable<E> in) {
    return newRubyArray(in);
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#toH()
   */
  public static <E, K, V> RubyHash<K, V> toH(Iterable<E> in) {
    return newRubyArray(in).toH();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#zip(Iterable...)
   */
  @SafeVarargs
  public static <E> List<? extends List<E>> zip(Iterable<E> in,
      Iterable<? extends E>... others) {
    return newRubyLazyEnumerator(in).zip(others).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#zip(List)
   */
  public static <E> List<? extends List<E>> zip(Iterable<E> in,
      List<? extends Iterable<? extends E>> others) {
    return newRubyLazyEnumerator(in).zip(others).toA();
  }

  /**
   * @see net.sf.rubycollect4j.RubyEnumerable#zip(List, Consumer)
   */
  public static <E> void zip(Iterable<E> in,
      List<? extends Iterable<? extends E>> others,
      Consumer<? super RubyArray<E>> block) {
    newRubyLazyEnumerator(in).zip(others, block);
  }

}
