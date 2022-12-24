/*
 *
 * Copyright 2016 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j.iter;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * {@link SliceAfterIterable} iterates all elements by slicing elements into different parts. It
 * ends each slice when any element is true returned by the block or matched by the pattern.
 * 
 * @param <E> the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class SliceAfterIterable<E> implements Iterable<RubyArray<E>> {

  private final Iterable<? extends E> iter;
  private final Predicate<? super E> block;
  private final Pattern pattern;

  /**
   * Creates a {@link SliceAfterIterable}.
   * 
   * @param iter an Iterable
   * @param block to check elements
   * @throws NullPointerException if iter or block is null
   */
  public SliceAfterIterable(Iterable<? extends E> iter, Predicate<? super E> block) {
    if (iter == null || block == null) throw new NullPointerException();

    this.iter = iter;
    this.block = block;
    pattern = null;
  }

  /**
   * Creates a SliceBeforeIterable.
   * 
   * @param iter an Iterable
   * @param pattern to match elements
   * @throws NullPointerException if iter or pattern is null
   */
  public SliceAfterIterable(Iterable<E> iter, Pattern pattern) {
    if (iter == null || pattern == null) throw new NullPointerException();

    this.iter = iter;
    this.block = null;
    this.pattern = pattern;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    if (block != null)
      return new SliceAfterIterator<>(iter.iterator(), block);
    else
      return new SliceAfterIterator<>(iter.iterator(), pattern);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("[");
    int index = 0;
    for (RubyArray<E> item : this) {
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
