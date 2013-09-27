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

/**
 * 
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
   * @throws NullPointerException
   *           if iter is null
   * @throws NullPointerException
   *           if block is null
   */
  public SliceBeforeIterable(Iterable<E> iter, BooleanBlock<E> block) {
    if (iter == null || block == null)
      throw new NullPointerException();

    this.iter = iter;
    this.block = block;
    pattern = null;
  }

  /**
   * The constructor of the SliceBeforeIterable.
   * 
   * @param iter
   *          an Iterable
   * @param pattern
   *          to match elements
   * @throws NullPointerException
   *           if iter is null
   * @throws NullPointerException
   *           if pattern is null
   */
  public SliceBeforeIterable(Iterable<E> iter, Pattern pattern) {
    if (iter == null || pattern == null)
      throw new NullPointerException();

    this.iter = iter;
    this.block = null;
    this.pattern = pattern;
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
