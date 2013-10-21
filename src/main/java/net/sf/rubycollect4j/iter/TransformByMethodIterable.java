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

/**
 * TransformByMethodIterable converts any type of Iterable to another type by
 * given method.
 * 
 * @param <E>
 *          the type of the elements
 * @param <S>
 *          the type of transformed elements
 */
public final class TransformByMethodIterable<E, S> implements Iterable<S> {

  private final Iterable<E> iter;
  private final String methodName;

  /**
   * Creates a TransformByMethodIterable.
   * 
   * @param iter
   *          an Iterable
   * @param methodName
   *          name of a Method
   * @throws NullPointerException
   *           if iter or methodName is null
   */
  public TransformByMethodIterable(Iterable<E> iter, String methodName) {
    if (iter == null || methodName == null)
      throw new NullPointerException();

    this.iter = iter;
    this.methodName = methodName;
  }

  @Override
  public Iterator<S> iterator() {
    return new TransformByMethodIterator<E, S>(iter.iterator(), methodName);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (S item : this) {
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
