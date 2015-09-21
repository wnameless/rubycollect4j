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
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.TransformBlock;

/**
 * 
 * {@link FlattenIterator} iterates each element and flattens it.
 * 
 * @param <E>
 *          the type of the elements
 * @param <S>
 *          the type of the transformed elements
 */
public final class FlattenIterator<E, S> implements Iterator<S> {

  private final Iterator<? extends E> iter;
  private final TransformBlock<? super E, ? extends List<? extends S>> block;
  private final RubyArray<S> buffer = newRubyArray();

  /**
   * Creates a {@link FlattenIterator}.
   * 
   * @param iter
   *          an Iterator
   * @throws NullPointerException
   *           if iter or block is null
   */
  public FlattenIterator(Iterator<? extends E> iter,
      TransformBlock<? super E, ? extends List<? extends S>> block) {
    if (iter == null || block == null) throw new NullPointerException();

    this.iter = iter;
    this.block = block;
  }

  private void nextElement() {
    while (buffer.isEmpty() && iter.hasNext()) {
      E item = iter.next();
      if (item == null)
        buffer.add(null);
      else
        buffer.concat(block.yield(item));
    }
  }

  @Override
  public boolean hasNext() {
    nextElement();
    return !buffer.isEmpty();
  }

  @Override
  public S next() {
    if (!hasNext()) throw new NoSuchElementException();

    return buffer.pop();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
