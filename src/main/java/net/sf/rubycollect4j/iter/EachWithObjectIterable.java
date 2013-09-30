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

/**
 * 
 * EachWithObjectIterable iterates each element with an object.
 * 
 * @param <E>
 *          the type of the elements
 * @param <O>
 *          the type of the object
 */
public final class EachWithObjectIterable<E, O> implements
    Iterable<Entry<E, O>> {

  private final Iterable<E> iter;
  private final O obj;

  /**
   * Creates an EachWithObjectIterable.
   * 
   * @param iter
   *          an Iterable
   * @param obj
   *          an Object
   * @throws NullPointerException
   *           if iter or obj is null
   */
  public EachWithObjectIterable(Iterable<E> iter, O obj) {
    if (iter == null || obj == null)
      throw new NullPointerException();

    this.iter = iter;
    this.obj = obj;
  }

  @Override
  public Iterator<Entry<E, O>> iterator() {
    return new EachWithObjectIterator<E, O>(iter.iterator(), obj);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (Entry<E, O> item : this) {
      if (index == 0)
        sb.append(item);
      else
        sb.append(", " + item);
      index++;
    }
    sb.append("]");
    return sb.toString();
  }

}
