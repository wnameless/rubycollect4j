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

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;

import java.util.Iterator;
import java.util.NoSuchElementException;

import cleanzephyr.rubycollect4j.RubyArray;

public final class EachSliceIterator<E> implements Iterator<RubyArray<E>> {

  private final Iterator<E> iterator;
  private final int size;

  public EachSliceIterator(Iterator<E> iterator, int size) {
    this.iterator = iterator;
    this.size = size;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> bucket = newRubyArray();
    while (iterator.hasNext() && bucket.size() < size) {
      bucket.add(iterator.next());
    }
    return bucket;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
