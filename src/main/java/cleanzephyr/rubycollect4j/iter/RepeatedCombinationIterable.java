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

import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;

import cleanzephyr.rubycollect4j.RubyArray;

/**
 * 
 * @author WMW
 * @param <E>
 */
public final class RepeatedCombinationIterable<E> implements
    Iterable<RubyArray<E>> {

  private final List<E> list;
  private final int n;

  public RepeatedCombinationIterable(List<E> list, int n) {
    this.list = list;
    this.n = n;
  }

  public RepeatedCombinationIterable(Iterable<E> iter, int n) {
    list = newArrayList(iter);
    this.n = n;
  }

  public RepeatedCombinationIterable(Iterator<E> iter, int n) {
    list = newArrayList(iter);
    this.n = n;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new RepeatedCombinationIterator<E>(list, n);
  }

}
