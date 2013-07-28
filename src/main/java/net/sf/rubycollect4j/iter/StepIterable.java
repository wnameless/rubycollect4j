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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 
 * StepIterable iterates elements by skipping n elements each time.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class StepIterable<E> implements Iterable<E> {

  private final Iterable<E> iter;
  private final int step;

  /**
   * The constructor of the StepIterable.
   * 
   * @param iter
   *          an Iterable
   * @param step
   *          number of elements to skip
   */
  public StepIterable(Iterable<E> iter, int step) {
    if (step == 0)
      throw new IllegalArgumentException("step can't be 0");
    if (step < 0)
      throw new IllegalArgumentException("step can't be negative");

    this.iter = checkNotNull(iter);
    this.step = step;
  }

  @Override
  public Iterator<E> iterator() {
    return new StepIterator<E>(iter.iterator(), step);
  }

}
