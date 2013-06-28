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

import java.util.Iterator;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * EachConsIterable iterates each element by a window of size n. It returns a
 * RubyArray which includes n consecutive elements within this window.
 * 
 * @param <E>
 *          the type of the elements
 * @throws IllegalArgumentException
 *           if size less than or equal to 0
 */
public final class EachConsIterable<E> implements Iterable<RubyArray<E>> {

  private final Iterable<E> iter;
  private final int size;

  /**
   * The constructor of the EachConsIterable.
   * 
   * @param iter
   *          an Iterable
   * @param size
   *          of the window
   */
  public EachConsIterable(Iterable<E> iter, int size) {
    if (size <= 0) {
      throw new IllegalArgumentException("invalid size");
    }
    this.iter = iter;
    this.size = size;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new EachConsIterator<E>(iter.iterator(), size);
  }

}
