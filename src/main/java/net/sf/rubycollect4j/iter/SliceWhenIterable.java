/*
 *
 * Copyright 2016 Wei-Ming Wu
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
import net.sf.rubycollect4j.block.EntryBooleanBlock;

/**
 * 
 * {@link SliceWhenIterable} iterates all elements by slicing elements into
 * different parts. It performs each slicing when any element is true returned
 * by the block.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class SliceWhenIterable<E> implements Iterable<RubyArray<E>> {

  private final Iterable<? extends E> iter;
  private final EntryBooleanBlock<? super E, ? super E> block;

  /**
   * Creates a {@link SliceWhenIterable}.
   * 
   * @param iter
   *          an Iterable
   * @param block
   *          to check elements
   * @throws NullPointerException
   *           if iter or block is null
   */
  public SliceWhenIterable(Iterable<? extends E> iter,
      EntryBooleanBlock<? super E, ? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    this.iter = iter;
    this.block = block;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new SliceWhenIterator<E>(iter.iterator(), block);
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