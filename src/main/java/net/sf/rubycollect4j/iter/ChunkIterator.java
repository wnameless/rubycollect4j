/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.util.Iterator;
import java.util.Map.Entry;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;
import net.sf.rubycollect4j.util.ComparableEntry;
import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * {@link ChunkIterator} transforms elements first, and then puts the original
 * elements together if elements which are next to each others have the same
 * transformed value. Chucked elements are placed into an Entry which uses
 * transformed value as key and a RubyArray of elements as value.
 * 
 * @param <E>
 *          the type of the elements
 * @param <K>
 *          the type of the transformed elements
 */
public final class ChunkIterator<E, K>
    implements Iterator<Entry<K, RubyArray<E>>> {

  private final PeekingIterator<E> pIter;
  private final TransformBlock<? super E, ? extends K> block;

  /**
   * Creates a {@link ChunkIterator}.
   * 
   * @param iter
   *          an Iterator
   * @param block
   *          to transform each element
   * @throws NullPointerException
   *           if iterator or block is null
   */
  public ChunkIterator(Iterator<? extends E> iter,
      TransformBlock<? super E, ? extends K> block) {
    if (iter == null || block == null) throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    this.block = block;
  }

  private Entry<K, RubyArray<E>> nextElement() {
    K key = block.yield(pIter.peek());
    RubyArray<E> bucket = newRubyArray();
    while (pIter.hasNext() && key.equals(block.yield(pIter.peek()))) {
      bucket.add(pIter.next());
    }
    return new ComparableEntry<K, RubyArray<E>>(key, bucket);
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext();
  }

  @Override
  public Entry<K, RubyArray<E>> next() {
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
