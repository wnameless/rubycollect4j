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
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;

import static net.sf.rubycollect4j.RubyArray.newRubyArray;
import static net.sf.rubycollect4j.RubyCollections.newPair;

/**
 * @param <E>
 *          the type of the elements
 * @param <K>
 *          the type of the transformed elements
 */
public final class ChunkIterator<E, K> implements
    Iterator<Entry<K, RubyArray<E>>> {

  private final PeekingIterator<E> pIterator;
  private final TransformBlock<E, K> block;

  public ChunkIterator(Iterator<E> iterator, TransformBlock<E, K> block) {
    pIterator = Iterators.peekingIterator(iterator);
    this.block = block;
  }

  private Entry<K, RubyArray<E>> nextElement() {
    K key = block.yield(pIterator.peek());
    RubyArray<E> bucket = newRubyArray();
    while (pIterator.hasNext() && key.equals(block.yield(pIterator.peek()))) {
      bucket.add(pIterator.next());
    }
    return newPair(key, bucket);
  }

  @Override
  public boolean hasNext() {
    return pIterator.hasNext();
  }

  @Override
  public Entry<K, RubyArray<E>> next() {
    if (!pIterator.hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
