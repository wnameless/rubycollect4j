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

/**
 * 
 * {@link GrepIterable} iterates elements which are matched by the regular
 * expression.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class GrepIterable<E> implements Iterable<E> {

  private final Iterable<? extends E> iter;
  private final String regex;

  /**
   * Creates a {@link GrepIterable}.
   * 
   * @param iter
   *          an Iterable
   * @param regex
   *          regular expression
   * @throws NullPointerException
   *           if iter or regex is null
   */
  public GrepIterable(Iterable<? extends E> iter, String regex) {
    if (iter == null || regex == null) throw new NullPointerException();

    this.iter = iter;
    this.regex = regex;
  }

  @Override
  public Iterator<E> iterator() {
    return new GrepIterator<E>(iter.iterator(), regex);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (E item : this) {
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
