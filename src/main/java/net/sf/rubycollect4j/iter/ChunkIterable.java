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


public final class ChunkIterable<E, K> implements
    Iterable<Entry<K, RubyArray<E>>> {
  private final Iterable<E> iterable;
  private final TransformBlock<E, K> block;

  public ChunkIterable(Iterable<E> iterable, TransformBlock<E, K> block) {
    this.iterable = iterable;
    this.block = block;
  }

  @Override
  public Iterator<Entry<K, RubyArray<E>>> iterator() {
    return new ChunkIterator<E, K>(iterable.iterator(), block);
  }

}
