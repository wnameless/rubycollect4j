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

import net.sf.rubycollect4j.block.TransformBlock;

/**
 * Convert any type of Iterator to another type by given TransformBlock.
 * 
 * @param <E>
 *          the type of the elements
 * @param <S>
 *          the type of transformed elements
 */
public final class TransformIterator<E, S> implements Iterator<S> {

  private final Iterator<E> iter;
  private final TransformBlock<E, S> block;

  /**
   * Create a TransformIterator by given Iterator and TransformBlock.
   * 
   * @param iter
   *          of any type
   * @param block
   *          to transform elements
   * @throws NullPointerException
   *           if iterable or block is null
   */
  public TransformIterator(Iterator<E> iter, TransformBlock<E, S> block) {
    if (iter == null || block == null)
      throw new NullPointerException();

    this.iter = iter;
    this.block = block;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public S next() {
    return block.yield(iter.next());
  }

  @Override
  public void remove() {
    iter.remove();
  }

}
