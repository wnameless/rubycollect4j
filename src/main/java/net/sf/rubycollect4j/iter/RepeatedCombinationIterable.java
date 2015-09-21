/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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

import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * {@link RepeatedCombinationIterable} generates all repeated combinations into
 * a List with length n.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RepeatedCombinationIterable<E>
    implements Iterable<RubyArray<E>> {

  private final List<? extends E> list;
  private final int n;

  /**
   * Creates a {@link RepeatedCombinationIterable}.
   * 
   * @param list
   *          a List
   * @param n
   *          length of each repeated combination
   * @throws NullPointerException
   *           if list is null
   */
  public RepeatedCombinationIterable(List<? extends E> list, int n) {
    if (list == null) throw new NullPointerException();

    this.list = list;
    this.n = n;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new RepeatedCombinationIterator<E>(list, n);
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
