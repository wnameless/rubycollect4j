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
package net.sf.rubycollect4j.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.HashMultiset;

import static com.google.common.collect.Sets.newHashSet;
import static net.sf.rubycollect4j.RubyCollections.ra;

/**
 * 
 * ListSet wraps any List into a Set. It is simply a wrapper and allows
 * duplicate elements within the Set. ListSet will be changed at the same time
 * if the backing List is modified.
 * 
 * @param <E>
 *          the type of elements
 */
public final class ListSet<E> implements Set<E> {

  private final List<E> list;

  /**
   * 
   * The constructor of ListSet.
   * 
   * @param list
   *          any List
   */
  public ListSet(List<E> list) {
    this.list = list;
  }

  @Override
  public boolean add(E e) {
    return list.add(e);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return list.addAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public Iterator<E> iterator() {
    return list.iterator();
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }

  @SuppressWarnings("unchecked")
  @Override
  public boolean equals(Object o) {
    if (o instanceof ListSet) {
      @SuppressWarnings("rawtypes")
      ListSet ls = (ListSet) o;
      return HashMultiset.create(list).equals(HashMultiset.create(ls.list));
    } else if (o instanceof Set) {
      @SuppressWarnings("rawtypes")
      Set set = (Set) o;
      return set.equals(this);
    }
    return false;
  }

  @Override
  public int hashCode() {
    if (ra(list).uniq().count() != list.size()) {
      return HashMultiset.create(list).hashCode();
    } else {
      return newHashSet(list).hashCode();
    }
  }

  @Override
  public String toString() {
    return list.toString();
  }

}
