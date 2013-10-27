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

import net.sf.rubycollect4j.RubyObject;
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
  private final Object[] args;

  /**
   * Creates a TransformByMethodIterator.
   * 
   * @param iter
   *          an Iterator
   * @param methodName
   *          name of a Method
   * @param args
   *          arguments of a Method
   * @throws NullPointerException
   *           if iter or methodName is null
   */
  public TransformByMethodIterator(Iterator<E> iter, String methodName,
      Object... args) {
    if (iter == null || methodName == null)
      throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    this.methodName = methodName;
    this.args = args;
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext();
  }

  @Override
  public S next() {
    return RubyObject.send(pIter.next(), methodName, args);
  }

  @Override
  public void remove() {
    pIter.remove();
  }

}
