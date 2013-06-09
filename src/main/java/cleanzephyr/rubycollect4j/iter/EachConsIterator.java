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
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author WMW
 * @param <E>
 */
public final class EachConsIterator<E> implements Iterator<RubyArray<E>> {

  private final Iterator<E> iterator;
  private final int size;
  private final RubyArray<E> bucket = newRubyArray();

  public EachConsIterator(Iterator<E> iterator, int size) {
    this.iterator = iterator;
    this.size = size;
    fillBucket();
  }

  private void fillBucket() {
    while (iterator.hasNext() && bucket.size() < size) {
      bucket.add(iterator.next());
    }
  }

  private void updateBucket() {
    bucket.deleteAt(0);
    if (iterator.hasNext()) {
      bucket.add(iterator.next());
    }
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> element = newRubyArray(bucket, true);
    updateBucket();
    return element;
  }

  @Override
  public boolean hasNext() {
    return bucket.size() == size;
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }

    return nextElement();
  }

}
