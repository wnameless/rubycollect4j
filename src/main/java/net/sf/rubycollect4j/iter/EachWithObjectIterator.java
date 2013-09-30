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

import static net.sf.rubycollect4j.RubyCollections.newPair;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * 
 * EachWithObjectIterator iterates each element with an object.
 * 
 * @param <E>the type of the elements
 * @param <O>
 *          the type of the object
 */
public final class EachWithObjectIterator<E, O> implements
    Iterator<Entry<E, O>> {

  private final Iterator<E> iter;
  private final O obj;

  /**
   * Creates an EachWithObjectIterator.
   * 
   * @param iter
   *          an Iterator
   * @param obj
   *          an Object
   * @throws NullPointerException
   *           if iter or obj is null
   */
  public EachWithObjectIterator(Iterator<E> iter, O obj) {
    if (iter == null || obj == null)
      throw new NullPointerException();

    this.iter = iter;
    this.obj = obj;
  }

  private Entry<E, O> nextElement() {
    return newPair(iter.next(), obj);
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public Entry<E, O> next() {
    if (!hasNext())
      throw new NoSuchElementException();

    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
