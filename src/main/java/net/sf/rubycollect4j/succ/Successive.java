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
package net.sf.rubycollect4j.succ;

import java.util.Comparator;

/**
 * 
 * {@link Successive} interface defines a common approach to generate a
 * successive item of any given element.
 * 
 * @param <E>
 *          the type of the elements
 */
public interface Successive<E> extends Comparator<E> {

  /**
   * Returns the successor of given element.
   * 
   * @param curr
   *          an element
   * @return the successor of given element
   */
  E succ(E curr);

}
