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

import static net.sf.rubycollect4j.RubyCollections.newRubyArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * {@link RepeatedCombinationIterator} generates all repeated combinations into
 * a List with length n.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class RepeatedCombinationIterator<E> implements
    Iterator<RubyArray<E>> {

  private final List<? extends E> list;
  private final int[] counter;
  private final int[] endStatus;
  private final int end;
  private boolean hasMore = true;

  /**
   * Creates a {@link RepeatedCombinationIterator}.
   * 
   * @param list
   *          a List
   * @param n
   *          length of each repeated combination
   * @throws NullPointerException
   *           if list is null
   */
  public RepeatedCombinationIterator(List<? extends E> list, int n) {
    if (list == null)
      throw new NullPointerException();

    this.list = new ArrayList<E>(list);
    end = this.list.size() - 1;
    if (n <= 0) {
      counter = new int[0];
      endStatus = new int[0];
      if (n != 0)
        hasMore = false;
    } else {
      counter = new int[n];
      endStatus = new int[n];
      Arrays.fill(endStatus, end);
    }
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> c = newRubyArray();
    for (int i = 0; i < counter.length; i++) {
      c.add(list.get(counter[i]));
    }
    if (Arrays.equals(counter, endStatus))
      hasMore = false;
    else
      increaseCounter();
    return c;
  }

  private void increaseCounter() {
    for (int i = counter.length - 1; i >= 0; i--) {
      if (counter[i] < end) {
        counter[i]++;
        break;
      } else if (counter[i - 1] != end) {
        counter[i - 1]++;
        for (int j = i; j < counter.length; j++) {
          counter[j] = counter[i - 1];
        }
        break;
      }
    }
  }

  @Override
  public boolean hasNext() {
    return hasMore;
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext())
      throw new NoSuchElementException();

    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
