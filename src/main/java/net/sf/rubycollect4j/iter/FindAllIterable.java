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

import net.sf.rubycollect4j.block.BooleanBlock;

/**
 * 
 * FindAllIterable iterates elements which are true returned by the block.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class FindAllIterable<E> implements Iterable<E> {

  private final Iterable<E> iter;
  private final BooleanBlock<E> block;

  /**
   * Creates a FindAllIterable.
   * 
   * @param iter
   *          an Iterable
   * @param block
   *          to check elements
   * @throws NullPointerException
   *           if iter or block is null
   */
  public FindAllIterable(Iterable<E> iter, BooleanBlock<E> block) {
    if (iter == null || block == null)
      throw new NullPointerException();

    this.iter = iter;
    this.block = block;
  }

  @Override
  public Iterator<E> iterator() {
    return new FindAllIterator<E>(iter.iterator(), block);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (E item : this) {
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
