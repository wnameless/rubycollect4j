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
package cleanzephyr.rubycollect4j.iter;

import java.util.Iterator;

import com.google.common.collect.Iterables;

/**
 * 
 * @author WMW
 * @param <E>
 */
public final class CycleIterable<E> implements Iterable<E> {

  private final Iterable<E> iter;
  private final Integer n;

  public CycleIterable(Iterable<E> iter) {
    this.iter = iter;
    n = null;
  }

  public CycleIterable(Iterable<E> iter, int n) {
    this.iter = iter;
    this.n = n;
  }

  @Override
  public Iterator<E> iterator() {
    if (n == null) {
      return Iterables.cycle(iter).iterator();
    } else {
      return new CycleIterator<>(iter, n);
    }
  }

}
