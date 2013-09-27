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
import java.util.Map.Entry;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

/**
 * 
 * ChunkIterable transforms elements first, and then puts the original elements
 * together if elements which are next to each others have the same transformed
 * value. Chucked elements are placed into an Entry which uses transformed value
 * as key and a RubyArray of elements as value.
 * 
 * @param <E>
 *          the type of the elements
 * @param <K>
 *          the type of the transformed elements
 */
public final class ChunkIterable<E, K> implements
    Iterable<Entry<K, RubyArray<E>>> {

  private final Iterable<E> iterable;
  private final TransformBlock<E, K> block;

  /**
   * The constructor of the ChunkIterable.
   * 
   * @param iterable
   *          an Iterable
   * @param block
   *          to transform each element
   * @throws NullPointerException
   *           if iterable is null
   * @throws NullPointerException
   *           if block is null
   */
  public ChunkIterable(Iterable<E> iterable, TransformBlock<E, K> block) {
    if (iterable == null || block == null)
      throw new NullPointerException();

    this.iterable = iterable;
    this.block = block;
  }

  @Override
  public Iterator<Entry<K, RubyArray<E>>> iterator() {
    return new ChunkIterator<E, K>(iterable.iterator(), block);
  }

}
