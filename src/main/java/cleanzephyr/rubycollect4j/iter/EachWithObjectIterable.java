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
import java.util.Map.Entry;

/**
 * 
 * @author WMW
 * @param <E>
 * @param <O>
 */
public final class EachWithObjectIterable<E, O> implements
    Iterable<Entry<E, O>> {

  private final Iterable<E> iter;
  private final O obj;

  public EachWithObjectIterable(Iterable<E> iter, O obj) {
    this.iter = iter;
    this.obj = obj;
  }

  @Override
  public Iterator<Entry<E, O>> iterator() {
    return new EachWithObjectIterator<>(iter.iterator(), obj);
  }

}
