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
package cleanzephyr.rubycollect4j;

import java.util.Iterator;

import cleanzephyr.rubycollect4j.block.ItemBlock;

import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import static com.google.common.collect.Lists.newArrayList;

public final class RubyEnumerator<E> extends RubyEnumerable<E> implements
    Iterable<E> {

  public static <E> RubyEnumerator<E> newRubyEnumerator(Iterable<E> iter) {
    return new RubyEnumerator<E>(iter);
  }

  public static <E> RubyEnumerator<E> newRubyEnumerator(Iterator<E> iter) {
    return new RubyEnumerator<E>(iter);
  }

  public RubyEnumerator(Iterable<E> iter) {
    super(iter);
  }

  public RubyEnumerator(Iterator<E> iter) {
    super(newArrayList(iter));
  }

  public RubyArray<E> each(ItemBlock<E> block) {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : iter) {
      block.yield(item);
      rubyArray.add(item);
    }
    return rubyArray;
  }

  public RubyEnumerator<E> each() {
    return this;
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }

}
