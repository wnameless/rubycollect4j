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

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * TransformByMethodIterator converts any type of Iterator to another type by
 * given method.
 * 
 * @param <E>
 *          the type of the elements
 * @param <S>
 *          the type of transformed elements
 */
public final class TransformByMethodIterator<E, S> implements Iterator<S> {

  private final PeekingIterator<E> pIter;
  private final String methodName;
  private Method method;

  /**
   * Creates a TransformByMethodIterator.
   * 
   * @param iter
   *          an Iterator
   * @param methodName
   *          name of a Method
   * @throws NullPointerException
   *           if iter or methodName is null
   */
  public TransformByMethodIterator(Iterator<E> iter, String methodName) {
    if (iter == null || methodName == null)
      throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    this.methodName = methodName;
    if (pIter.hasNext()) {
      E item = pIter.peek();
      try {
        method = item.getClass().getMethod(methodName);
      } catch (Exception e) {
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
        throw new IllegalArgumentException("NoMethodError: undefined method `"
            + methodName
            + "' for "
            + pIter.peek()
            + ":"
            + (pIter.peek() == null ? pIter.peek() : pIter.peek().getClass()
                .getName()));
      }
    }
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext();
  }

  @SuppressWarnings("unchecked")
  @Override
  public S next() {
    E item = pIter.next();
    S next = null;
    try {
      next = (S) method.invoke(item);
    } catch (Exception e) {
      Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, e);
      throw new IllegalArgumentException("NoMethodError: undefined method `"
          + methodName + "' for " + item + ":"
          + (item == null ? item : item.getClass().getName()));
    }
    return next;
  }

  @Override
  public void remove() {
    pIter.remove();
  }

}
