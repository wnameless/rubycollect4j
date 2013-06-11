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
import java.util.List;

import cleanzephyr.rubycollect4j.RubyArray;

import static com.google.common.collect.Lists.newArrayList;

/**
 * 
 * @author WMW
 * @param <E>
 */
public final class PermutationIterable<E> implements Iterable<RubyArray<E>> {

  private final List<E> list;
  private final int n;

  public PermutationIterable(List<E> List, int n) {
    this.list = List;
    this.n = n;
  }

  public PermutationIterable(Iterable<E> iter, int n) {
    list = newArrayList(iter);
    this.n = n;
  }

  public PermutationIterable(Iterator<E> iter, int n) {
    list = newArrayList(iter);
    this.n = n;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new PermutationIterator<E>(list, n);
  }

}