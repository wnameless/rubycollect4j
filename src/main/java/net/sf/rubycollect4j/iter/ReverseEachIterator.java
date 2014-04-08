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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * ReverseEachIterator iterates each element reversely.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class ReverseEachIterator<E> implements Iterator<E> {

  private final Iterator<? extends E> iter;
  private Iterator<E> reversedIter;

  /**
   * Creates a ReverseEachIterator.
   * 
   * @param iter
   *          an Iterator
   * @throws NullPointerException
   *           if iter is null
   */
  public ReverseEachIterator(Iterator<? extends E> iter) {
    if (iter == null)
      throw new NullPointerException();

    this.iter = iter;
  }

  private void initReversedIter() {
    List<E> list = new ArrayList<E>();
    while (iter.hasNext()) {
      list.add(0, iter.next());
    }
    reversedIter = list.iterator();
  }

  @Override
  public boolean hasNext() {
    if (reversedIter == null)
      return iter.hasNext();
    else
      return reversedIter.hasNext();
  }

  @Override
  public E next() {
    if (reversedIter == null)
      initReversedIter();

    return reversedIter.next();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
