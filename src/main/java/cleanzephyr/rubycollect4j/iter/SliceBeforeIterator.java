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

import cleanzephyr.rubycollect4j.RubyArray;
import static cleanzephyr.rubycollect4j.RubyArray.newRubyArray;
import cleanzephyr.rubycollect4j.block.BooleanBlock;
import com.google.common.collect.Iterators;
import com.google.common.collect.PeekingIterator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

/**
 *
 * @author WMW
 * @param <E>
 */
public final class SliceBeforeIterator<E> implements Iterator<RubyArray<E>> {

  private final PeekingIterator<E> pIter;
  private final BooleanBlock<E> block;
  private final Pattern pattern;

  public SliceBeforeIterator(Iterator<E> iter, BooleanBlock<E> block) {
    pIter = Iterators.peekingIterator(iter);
    this.block = block;
    pattern = null;
  }

  public SliceBeforeIterator(Iterator<E> iter, Pattern pattern) {
    pIter = Iterators.peekingIterator(iter);
    block = null;
    this.pattern = pattern;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> element = newRubyArray();
    if (block != null) {
      do {
        element.add(pIter.next());
      } while (pIter.hasNext() && !block.yield(pIter.peek()));
    } else {
      do {
        element.add(pIter.next());
      } while (pIter.hasNext() && !pattern.matcher(pIter.peek().toString()).find());
    }
    return element;
  }

  @Override
  public boolean hasNext() {
    return pIter.hasNext();
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

}
