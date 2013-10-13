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
import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.RubyArray;

/**
 * ZipIterable iterates each RubyArray of elements which get the same indices
 * among all other Lists.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class ZipIterable<E> implements Iterable<RubyArray<E>> {

  private final Iterable<E> iter;
  private final List<? extends Iterable<E>> others;

  /**
   * Creates a ZipIterable.
   * 
   * @param iter
   *          any Iterable
   * @param others
   *          a List of Iterable
   */
  public ZipIterable(Iterable<E> iter, List<? extends Iterable<E>> others) {
    if (iter == null || others == null)
      throw new NullPointerException();

    this.iter = iter;
    this.others = others;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    List<Iterator<E>> otherIter = new ArrayList<Iterator<E>>();
    for (Iterable<E> i : others) {
      otherIter.add(i.iterator());
    }
    return new ZipIterator<E>(iter.iterator(), otherIter);
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
