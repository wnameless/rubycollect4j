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

import net.sf.rubycollect4j.RubyArray;
import net.sf.rubycollect4j.block.BooleanBlock;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * SliceBeforeIterable iterates all elements by slicing elements into different
 * parts. It performs each slicing when any element is true returned by the
 * block or matched by the pattern.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class SliceBeforeIterable<E> implements Iterable<RubyArray<E>> {

  private final Iterable<E> iter;
  private final BooleanBlock<E> block;
  private final Pattern pattern;

  /**
   * The constructor of the SliceBeforeIterable.
   * 
   * @param iter
   *          an Iterable
   * @param block
   *          to check elements
   */
  public SliceBeforeIterable(Iterable<E> iter, BooleanBlock<E> block) {
    this.iter = checkNotNull(iter);
    this.block = checkNotNull(block);
    pattern = null;
  }

  /**
   * The constructor of the SliceBeforeIterable.
   * 
   * @param iter
   *          an Iterable
   * @param pattern
   *          to match elements
   */
  public SliceBeforeIterable(Iterable<E> iter, Pattern pattern) {
    this.iter = checkNotNull(iter);
    this.block = null;
    this.pattern = checkNotNull(pattern);
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    if (block != null) {
      return new SliceBeforeIterator<E>(iter.iterator(), block);
    } else {
      return new SliceBeforeIterator<E>(iter.iterator(), pattern);
    }
  }

}
