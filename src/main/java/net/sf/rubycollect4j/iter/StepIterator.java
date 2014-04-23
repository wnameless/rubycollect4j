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

/**
 * 
 * {@link StepIterator} iterates elements by skipping n elements each time.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class StepIterator<E> implements Iterator<E> {

  private final Iterator<? extends E> iter;
  private final int step;
  private boolean hasMore = false;
  private boolean isRemovable = false;

  /**
   * Creates a {@link StepIterator}.
   * 
   * @param iter
   *          an Iterator
   * @param step
   *          number of elements to skip
   * @throws NullPointerException
   *           if iter is null
   * @throws IllegalArgumentException
   *           if step is less than or equal to 0
   */
  public StepIterator(Iterator<? extends E> iter, int step) {
    if (iter == null)
      throw new NullPointerException();
    if (step == 0)
      throw new IllegalArgumentException("ArgumentError: step can't be 0");
    if (step < 0)
      throw new IllegalArgumentException(
          "ArgumentError: step can't be negative");

    this.iter = iter;
    this.step = step;
    if (iter.hasNext())
      hasMore = true;
  }

  private void advanceCursor() {
    if (!hasMore) {
      isRemovable = false;
      int step = this.step;
      while (step > 1 && iter.hasNext()) {
        iter.next();
        step--;
      }
      if (step == 1 && iter.hasNext())
        hasMore = true;
    }
  }

  @Override
  public boolean hasNext() {
    advanceCursor();
    return hasMore;
  }

  @Override
  public E next() {
    advanceCursor();
    isRemovable = true;
    hasMore = false;
    return iter.next();
  }

  @Override
  public void remove() {
    if (!isRemovable)
      throw new IllegalStateException();

    isRemovable = false;
    iter.remove();
  }

}
