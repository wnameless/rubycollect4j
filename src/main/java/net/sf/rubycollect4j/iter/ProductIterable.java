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
package net.sf.rubycollect4j.iter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * ProductIterable iterates all products by all elements of input Lists.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class ProductIterable<E> implements Iterable<RubyArray<E>> {

  private final List<List<E>> lists = new ArrayList<List<E>>();

  /**
   * Creates an ProductIterable.
   * 
   * @param self
   *          a List
   * @param others
   *          a List of List
   * @throws NullPointerException
   *           if self or others is null
   */
  public ProductIterable(List<E> self, List<? extends List<E>> others) {
    if (self == null || others == null)
      throw new NullPointerException();

    lists.add(self);
    lists.addAll(others);
  }

  /**
   * Creates an ProductIterable.
   * 
   * @param self
   *          a List
   * @param others
   *          an array of List
   * @throws NullPointerException
   *           if self is null
   */
  public ProductIterable(List<E> self, List<E>... others) {
    if (self == null)
      throw new NullPointerException();

    lists.add(self);
    lists.addAll(Arrays.asList(others));
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new ProductIterator<E>(lists);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (RubyArray<E> item : this) {
      if (index == 0)
        sb.append(item);
      else
        sb.append(", " + item);
      index++;
    }
    sb.append("]");
    return sb.toString();
  }

}
