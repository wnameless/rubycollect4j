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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.RubyArray;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

/**
 * 
 * ProductIterable iterates all products by all elements of input Lists.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class ProductIterable<E> implements Iterable<RubyArray<E>> {

  private final List<List<E>> lists = newArrayList();

  /**
   * The constructor of the ProductIterable.
   * 
   * @param self
   *          a List
   * @param others
   *          a List of Lists
   */
  public ProductIterable(List<E> self, List<? extends List<E>> others) {
    lists.add(checkNotNull(self));
    lists.addAll(checkNotNull(others));
  }

  /**
   * The constructor of the ProductIterable.
   * 
   * @param self
   *          a List
   * @param others
   *          an Array of Lists
   */
  public ProductIterable(List<E> self, List<E>... others) {
    lists.add(checkNotNull(self));
    lists.addAll(Arrays.asList(others));
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new ProductIterator<E>(lists);
  }

}
