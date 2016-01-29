/*
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

import net.sf.rubycollect4j.util.ComparableEntry;

/**
 * 
 * {@link EachWithObjectIterator} iterates each element with an object.
 * 
 * @param <E>the
 *          type of the elements
 * @param <O>
 *          the type of the object
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class EachWithObjectIterator<E, O>
    implements Iterator<Entry<E, O>> {

  private final Iterator<? extends E> iter;
  private final O obj;

  /**
   * Creates an {@link EachWithObjectIterator}.
   * 
   * @param iter
   *          an Iterator
   * @param obj
   *          an object
   * @throws NullPointerException
   *           if iter or obj is null
   */
  public EachWithObjectIterator(Iterator<? extends E> iter, O obj) {
    if (iter == null || obj == null) throw new NullPointerException();

    this.iter = iter;
    this.obj = obj;
  }

  @Override
  public boolean hasNext() {
    return iter.hasNext();
  }

  @Override
  public Entry<E, O> next() {
    return new ComparableEntry<E, O>(iter.next(), obj);
  }

  @Override
  public void remove() {
    iter.remove();
  }

}
