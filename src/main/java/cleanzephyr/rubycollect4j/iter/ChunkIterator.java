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
package cleanzephyr.rubycollect4j.iter;

import cleanzephyr.rubycollect4j.RubyArray;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import java.util.AbstractMap.SimpleEntry;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

import cleanzephyr.rubycollect4j.block.ItemTransformBlock;

import static com.google.common.collect.Iterators.peekingIterator;
import com.google.common.collect.PeekingIterator;

public final class ChunkIterator<E, K> implements Iterator<Entry<K, RubyArray<E>>> {

  private final PeekingIterator<E> pIterator;
  private final ItemTransformBlock<E, K> block;

  public ChunkIterator(Iterator<E> iterator, ItemTransformBlock<E, K> block) {
    pIterator = peekingIterator(iterator);
    this.block = block;
  }

  private Entry<K, RubyArray<E>> nextElement() {
    K key = block.yield(pIterator.peek());
    RubyArray<E> bucket = newRubyArray();
    while (pIterator.hasNext() && key.equals(block.yield(pIterator.peek()))) {
      bucket.add(pIterator.next());
    }
    return new SimpleEntry<>(key, bucket);
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

}
