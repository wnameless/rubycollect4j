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
import java.util.NoSuchElementException;

/**
 * @param <E>
 *          the type of the elements
 */
public final class StepIterator<E> implements Iterator<E> {

  private final Iterator<E> iter;
  private final int step;
  private E peek;
  private boolean hasMore = false;

  public StepIterator(Iterator<E> iter, int step) {
    if (step == 0) {
      throw new IllegalArgumentException("step can't be 0");
    }
    if (step < 0) {
      throw new IllegalArgumentException("step can't be negative");
    }
    this.iter = iter;
    this.step = step;
    if (iter.hasNext()) {
      peek = iter.next();
      hasMore = true;
    }
  }

  private E nextElement() {
    E next = peek;
    int step = this.step;
    while (step > 0 && iter.hasNext()) {
      peek = iter.next();
      step--;
    }
    if (step > 0) {
      hasMore = false;
    }
    return next;
  }

  @Override
  public boolean hasNext() {
    return hasMore;
  }

  @Override
  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
