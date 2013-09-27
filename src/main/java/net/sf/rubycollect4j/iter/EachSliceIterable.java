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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Iterator;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * EachSliceIterable iterates each element by a window of size n. It returns a
 * RubyArray which includes n consecutive elements within this window, then it
 * moves the position to the very next element behind the window and so on.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class EachSliceIterable<E> implements Iterable<RubyArray<E>> {

  private final Iterable<E> iter;
  private final int size;

  /**
   * The constructor of the EachSliceIterable.
   * 
   * @param iter
   *          an Iterabel
   * @param size
   *          of the window
   * @throws IllegalArgumentException
   *           if size less than or equal to 0
   */
  public EachSliceIterable(Iterable<E> iter, int size) {
    checkArgument(size > 0);
    this.iter = checkNotNull(iter);
    this.size = size;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new EachSliceIterator<E>(iter.iterator(), size);
  }

}
