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

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * 
 * @author WMW
 * @param <E>
 */
public final class CycleIterator<E> implements Iterator<E> {

  private final Iterable<E> iter;
  private Integer n;
  private Iterator<E> it;

  public CycleIterator(Iterable<E> iter, int n) {
    this.iter = iter;
    this.n = n;
    it = iter.iterator();
  }

  private E nextElement() {
    E next = it.next();
    if (!it.hasNext()) {
      it = iter.iterator();
      n--;
    }
    return next;
  }

  @Override
  public boolean hasNext() {
    return n > 0 && it.hasNext();
  }

  @Override
  public E next() {
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
