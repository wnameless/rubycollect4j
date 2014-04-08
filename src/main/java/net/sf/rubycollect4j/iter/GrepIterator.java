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
import java.util.regex.Pattern;

import net.sf.rubycollect4j.util.PeekingIterator;

/**
 * 
 * GrepIterator iterates elements which are matched by the regular expression.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class GrepIterator<E> implements Iterator<E> {

  private final PeekingIterator<E> pIter;
  private final Pattern pattern;

  /**
   * Creates a GrepIterator.
   * 
   * @param iter
   *          an Iterator
   * @param regex
   *          regular expression
   * @throws NullPointerException
   *           if iter or regex is null
   */
  public GrepIterator(Iterator<? extends E> iter, String regex) {
    if (iter == null || regex == null)
      throw new NullPointerException();

    pIter = new PeekingIterator<E>(iter);
    pattern = Pattern.compile(regex);
  }

  private void advanceCursor() {
    while (pIter.hasNext() && !pattern.matcher(pIter.peek().toString()).find()) {
      pIter.next();
    }
  }

  @Override
  public boolean hasNext() {
    advanceCursor();
    return pIter.hasNext();
  }

  @Override
  public E next() {
    advanceCursor();
    return pIter.next();
  }

  @Override
  public void remove() {
    pIter.remove();
  }

}
