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
package net.sf.rubycollect4j.iter;

import java.util.Iterator;
import java.util.Objects;

/**
 * 
 * {@link EachIndexIterator} iterates indices of elements instead of elements itself.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class EachIndexIterator<E> implements Iterator<Integer> {

  private final Iterator<? extends E> iter;
  private int index = 0;

  /**
   * Creates an {@link EachIndexIterator}.
   * 
   * @param iter an Iterator
   * @throws NullPointerException if iter is null
   */
  public EachIndexIterator(Iterator<? extends E> iter) {
    Objects.requireNonNull(iter);

    this.iter = iter;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public Integer next() {
    iter.next();
    return index++;
  }

  @Override
  public void remove() {
    iter.remove();
  }

}
