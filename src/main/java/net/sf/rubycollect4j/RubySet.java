/*
 *
 * Copyright 2013 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * {@link RubySet} implements all methods refer to the Set class of Ruby language.
 * <p>
 * {@link RubySet} is also a Java Set and a {@link RubyBase.Enumerable}.
 *
 * @param <E> the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class RubySet<E>
    implements RubyEnumerable<E>, Set<E>, Comparable<Set<E>>, Serializable {

  private static final long serialVersionUID = 1L;

  private Set<E> set;
  private boolean isFrozen = false;

  /**
   * Returns a {@link RubySet} which wraps the given LinkedHashSet.
   * 
   * @param set any LinkedHashSet
   * @return {@link RubySet}
   * @throws NullPointerException if set is null
   */
  public static <E> RubySet<E> of(LinkedHashSet<E> set) {
    return new RubySet<>(set);
  }

  /**
   * Returns a {@link RubySet} which copies the elements of given Iterable.
   * 
   * @param elements any Iterable
   * @return {@link RubySet}
   * @throws NullPointerException if elements is null
   */
  public static <E> RubySet<E> copyOf(Iterable<E> elements) {
    return new RubySet<>(elements);
  }

  /**
   * Creates a {@link RubySet}.
   */
  public RubySet() {
    set = new LinkedHashSet<>();
  }

  /**
   * Creates a {@link RubySet} by given LinkedHashSet. It's a wrapper implementation. No defensive
   * copy has been made.
   * 
   * @param set any LinkedHashSet
   * @throws NullPointerException if set is null
   */
  public RubySet(LinkedHashSet<E> set) {
    Objects.requireNonNull(set);

    this.set = set;
  }

  /**
   * Creates a {@link RubySet} by given Set.
   * 
   * @param iter any Iterable
   * @throws NullPointerException if iter is null
   */
  public RubySet(Iterable<E> iter) {
    Objects.requireNonNull(iter);

    set = new LinkedHashSet<>();
    iter.forEach(set::add);
  }

  /**
   * Adds the given object to the set and returns self. If the object is already in the set, returns
   * null.
   * 
   * @param e an element
   * @return this {@link RubySet} or null
   */
  public RubySet<E> addʔ(E e) {
    return add(e) ? this : null;
  }

  /**
   * Classifies the set by the return value of the given block and returns a {@link RubyHash} of {S
   * value =&gt; RubySet&lt;E&gt;} pairs.
   * 
   * @param block to transform elements
   * @return {@link RubyHash}
   */
  public <S> RubyHash<S, RubySet<E>> classify(Function<? super E, ? extends S> block) {
    RubyHash<S, RubySet<E>> hash = Ruby.Hash.create();
    set.forEach(e -> {
      S s = block.apply(e);
      if (!hash.containsKey(s)) hash.put(s, new RubySet<E>());

      hash.get(s).add(e);
    });
    return hash;
  }

  /**
   * Replaces the elements with ones returned by {@link #collect(Function)}.
   * 
   * @param block to transform elements
   * @return this {@link RubySet}
   */
  public RubySet<E> collectǃ(Function<? super E, ? extends E> block) {
    LinkedHashSet<E> lhs = new LinkedHashSet<>();
    set.forEach(e -> lhs.add(block.apply(e)));
    set.clear();
    set.addAll(lhs);
    return this;
  }

  /**
   * Deletes the given object from the set and returns self.
   * 
   * @param e an element
   * @return this {@link RubySet}
   */
  public RubySet<E> delete(E e) {
    remove(e);
    return this;
  }

  /**
   * Deletes the given object from the set and returns self. If the object is not in the set,
   * returns null.
   * 
   * @param e an element
   * @return this {@link RubySet} or null
   */
  public RubySet<E> deleteʔ(E e) {
    return remove(e) ? this : null;
  }

  /**
   * Deletes every element of the set for which block evaluates to true, and returns self.
   * 
   * @param block to filter elements
   * @return this {@link RubySet}
   */
  public RubySet<E> deleteIf(Predicate<? super E> block) {
    Iterator<E> iter = set.iterator();
    while (iter.hasNext()) {
      if (block.test(iter.next())) iter.remove();
    }
    return this;
  }

  /**
   * Returns a new set built by duplicating the set, removing every element that appears in the
   * given Iterable.
   * 
   * @param iter an Iterable
   * @return new {@link RubySet}
   */
  public RubySet<E> difference(Iterable<E> iter) {
    RubySet<E> newSet = RubySet.copyOf(set);
    iter.forEach(newSet::remove);
    return newSet;
  }

  /**
   * Returns true if the set and the given Iterable have no element in common.
   * 
   * @param iter an Iterable
   * @return true if the set and the given Iterable have no element in common, false otherwise
   */
  public boolean disjointʔ(Iterable<E> iter) {
    for (E e : iter) {
      if (set.contains(e)) return false;
    }
    return true;
  }

  /**
   * Divides the set into a set of subsets according to the commonality defined by the given block.
   * 
   * @param block to transform elements
   * @return new {@link RubySet} of {@link RubySet}s
   */
  public <S> RubySet<RubySet<E>> divide(Function<? super E, ? extends S> block) {
    RubyHash<S, RubySet<E>> hash = classify(block);
    return RubySet.copyOf(hash.values());
  }

  /**
   * {@inheritDoc}
   * 
   * @return this {@link RubySet}
   */
  @Override
  public RubySet<E> each(Consumer<? super E> block) {
    set.forEach(block::accept);
    return this;
  }

  /**
   * Returns true if the set contains no elements.
   * 
   * @return true if the set contains no elements, false otherwise
   */
  public boolean emptyʔ() {
    return isEmpty();
  }

  /**
   * Returns a new set containing elements exclusive between the set and the given Iterable.
   * (set.exclusive(iter)) is equivalent to (set.union(iter).subtract(set.intersection(iter))).
   * 
   * @param iter an Iterable
   * @return new {@link RubySet}
   */
  public RubySet<E> exclusive(Iterable<E> iter) {
    return union(iter).subtract(intersection(iter));
  }

  /**
   * Returns a new set that is a copy of the set, flattening each containing set recursively.
   * 
   * @return new {@link RubySet}
   */
  @SuppressWarnings("unchecked")
  public <S> RubySet<S> flatten() {
    RubySet<?> rubySet = RubySet.copyOf(set);
    while (rubySet.anyʔ(item -> item instanceof Set)) {
      rubySet = flatten(rubySet, 1);
    }
    return (RubySet<S>) rubySet;
  }

  @SuppressWarnings("unchecked")
  private <S> RubySet<S> flatten(Set<?> set, int n) {
    RubySet<S> rubySet = Ruby.Set.create();
    for (Object item : set) {
      if (item instanceof Set)
        for (Object o : (RubySet<?>) item) {
          rubySet.add((S) o);
        }
      else
        rubySet.add((S) item);
    }
    return rubySet;
  }

  /**
   * Freezes this {@link RubySet}.
   * 
   * @return this {@link RubySet}
   */
  public RubySet<E> freeze() {
    if (!isFrozen) {
      set = Collections.unmodifiableSet(set);
      isFrozen = true;
    }
    return this;
  }

  /**
   * Checks if this {@link RubySet} is frozen.
   * 
   * @return true if this {@link RubySet} is frozen, false otherwise
   */
  public boolean frozenʔ() {
    return isFrozen;
  }

  /**
   * Equivalent to {@link #toString()}.
   * 
   * @return String
   */
  public String inspect() {
    return toString();
  }

  /**
   * Returns true if the set and the given Iterable have at least one element in common.
   * 
   * @return true if the set and the given Iterable have at least one element in common, false
   *         otherwise
   */
  public boolean intersectʔ(Iterable<E> iter) {
    for (E e : iter) {
      if (set.contains(e)) return true;
    }
    return false;
  }

  /**
   * Returns a new set containing elements common to the set and the given Iterable.
   * 
   * @param iter an Iterable
   * @return new {@link RubySet}
   */
  public RubySet<E> intersection(Iterable<E> iter) {
    RubySet<E> newSet = new RubySet<>();
    iter.forEach(e -> {
      if (set.contains(e)) newSet.add(e);
    });
    return newSet;
  }

  /**
   * Deletes every element of the set for which block evaluates to false, and returns self.
   * 
   * @param block to filter elements
   * @return this {@link RubySet}
   */
  public RubySet<E> keepIf(Predicate<? super E> block) {
    Iterator<E> iter = set.iterator();
    while (iter.hasNext()) {
      if (!block.test(iter.next())) iter.remove();
    }
    return this;
  }

  /**
   * Equivalent to {@link #size()}.
   * 
   * @return size of this {@link RubySet}
   */
  public int length() {
    return set.size();
  }

  /**
   * Equivalent to {@link #collectǃ(Function)}.
   * 
   * @param block to transform elements
   * @return this {@link RubySet}
   */
  public RubySet<E> mapǃ(Function<? super E, ? extends E> block) {
    return collectǃ(block);
  }

  /**
   * Merges the elements of the given Iterable to the set and returns self.
   * 
   * @param iter an Iterable
   * @return this {@link RubySet}
   */
  public RubySet<E> merge(Iterable<E> iter) {
    iter.forEach(set::add);
    return this;
  }

  /**
   * Returns true if the set is a proper subset of the given set.
   * 
   * @param set any Set
   * @return true if the set is a proper subset of the given set, false otherwise
   */
  public boolean properSubsetʔ(Set<E> set) {
    if (size() <= set.size()) return false;

    for (E e : set) {
      if (!this.set.contains(e)) return false;
    }
    return true;
  }

  /**
   * Returns true if the set is a proper superset of the given set.
   * 
   * @param set any Set
   * @return true if the set is a proper superset of the given set, false otherwise
   */
  public boolean properSupersetʔ(Set<E> set) {
    if (set.size() <= size()) return false;

    for (E e : this.set) {
      if (!set.contains(e)) return false;
    }
    return true;
  }

  /**
   * Equivalent to {@link #deleteIf(Predicate)}, but returns null if no changes were made.
   * 
   * @param block to transform elements
   * @return this {@link RubySet} or null
   */
  public RubySet<E> rejectǃ(Predicate<? super E> block) {
    int beforeSize = size();
    deleteIf(block);
    return size() == beforeSize ? null : this;
  }

  /**
   * Replaces the contents of the set with the contents of the given Iterable and returns self.
   * 
   * @param iter an Iterable
   * @return this {@link RubySet}
   */
  public RubySet<E> replace(Iterable<E> iter) {
    set.clear();
    iter.forEach(set::add);
    return this;
  }

  /**
   * Equivalent to {@link #keepIf(Predicate)}, but returns null if no changes were made.
   * 
   * @param block to transform elements
   * @return this {@link RubySet} or null
   */
  public RubySet<E> selectǃ(Predicate<? super E> block) {
    int beforeSize = size();
    keepIf(block);
    return size() == beforeSize ? null : this;
  }

  /**
   * Returns true if the set is a subset of the given set.
   * 
   * @param set any Set
   * @return true if the set is a subset of the given set, false otherwise
   */
  public boolean subsetʔ(Set<E> set) {
    if (size() < set.size()) return false;

    for (E e : set) {
      if (!this.set.contains(e)) return false;
    }
    return true;
  }

  /**
   * Deletes every element that appears in the given Iterable and returns self.
   * 
   * @param iter an Iterable
   * @return this {@link RubySet}
   */
  public RubySet<E> subtract(Iterable<E> iter) {
    iter.forEach(set::remove);
    return this;
  }

  /**
   * Returns true if the set is a superset of the given set.
   * 
   * @param set any Set
   * @return true if the set is a superset of the given set, false otherwise
   */
  public boolean supersetʔ(Set<E> set) {
    if (set.size() < size()) return false;

    for (E e : this.set) {
      if (!set.contains(e)) return false;
    }
    return true;
  }

  /**
   * Puts all elements into a {@link Set}.
   * 
   * @return a {@link Set}
   */
  @Override
  public LinkedHashSet<E> toSet() {
    return new LinkedHashSet<>(set);
  }

  /**
   * Returns a new set built by merging the set and the elements of the given Iterable.
   * 
   * @param iter an Iterable
   * @return new {@link RubySet}
   */
  public RubySet<E> union(Iterable<E> iter) {
    RubySet<E> newSet = RubySet.copyOf(set);
    iter.forEach(newSet::add);
    return newSet;
  }

  @Override
  public boolean add(E e) {
    return set.add(e);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return set.addAll(c);
  }

  @Override
  public void clear() {
    set.clear();
  }

  @Override
  public boolean contains(Object o) {
    return set.contains(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return set.containsAll(c);
  }

  @Override
  public boolean isEmpty() {
    return set.isEmpty();
  }

  @Override
  public boolean remove(Object o) {
    return set.remove(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return set.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return set.retainAll(c);
  }

  @Override
  public int size() {
    return set.size();
  }

  @Override
  public Object[] toArray() {
    return set.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return set.toArray(a);
  }

  @Override
  public Iterator<E> iterator() {
    return set.iterator();
  }

  @Override
  public boolean equals(Object o) {
    return set.equals(o);
  }

  @Override
  public int hashCode() {
    return set.hashCode();
  }

  @Override
  public String toString() {
    return set.toString();
  }

  @Override
  public int compareTo(Set<E> arg0) {
    RubyArray<E> thisRa = RubyArray.copyOf(set);
    RubyArray<E> thatRa = RubyArray.copyOf(arg0);
    thisRa.sortǃ();
    thatRa.sortǃ();
    return thisRa.compareTo(thatRa);
  }

}
