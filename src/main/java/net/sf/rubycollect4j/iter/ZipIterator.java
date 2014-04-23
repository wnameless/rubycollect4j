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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.util.Iterator;

import net.sf.rubycollect4j.RubyArray;

/**
 * {@link ZipIterator} iterates each {@link RubyArray} of elements which get the
 * same indices among all other Lists.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class ZipIterator<E> implements Iterator<RubyArray<E>> {

  private final Iterator<? extends E> iter;
  private final RubyArray<? extends Iterator<? extends E>> others;

  /**
   * Creates a {@link ZipIterator}.
   * 
   * @param iter
   *          an Iterator
   * @param others
   *          an Iterable of Iterators
   */
  public ZipIterator(Iterator<? extends E> iter,
      Iterable<? extends Iterator<? extends E>> others) {
    if (iter == null || others == null)
      throw new NullPointerException();

    this.iter = iter;
    this.others = newRubyArray(others);
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public RubyArray<E> next() {
    RubyArray<E> element = newRubyArray();
    element.add(iter.next());
    for (Iterator<? extends E> i : others) {
      if (i.hasNext())
        element.add(i.next());
      else
        element.add(null);
    }
    return element;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
