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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * ProductIterator iterates all products of input Lists.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class ProductIterator<E> implements Iterator<RubyArray<E>> {

  private final List<? extends List<E>> lists;
  private final int[] counter;

  /**
   * Creates a ProductIterator.
   * 
   * @param lists
   *          a List of List
   * @throws NullPointerException
   *           if lists is null
   */
  public ProductIterator(List<? extends List<E>> lists) {
    if (lists == null)
      throw new NullPointerException();

    this.lists = lists;
    counter = new int[lists.size()];
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> combination = newRubyArray();
    for (int i = 0; i < counter.length; i++) {
      combination.add(lists.get(i).get(counter[i]));
    }
    increaseCounters();
    return combination;
  }

  private void increaseCounters() {
    for (int i = counter.length - 1; i >= 0; i--) {
      if (counter[i] < lists.get(i).size() - 1) {
        counter[i]++;
        for (int j = i + 1; j < counter.length; j++) {
          counter[j] = 0;
        }
        return;
      } else {
        counter[i] = -1;
      }
    }
  }

  private boolean isLooping() {
    for (int i = 0; i < counter.length; i++) {
      if (lists.get(i).isEmpty())
        return false;
    }
    return Arrays.binarySearch(counter, -1) == -1;
  }

  @Override
  public boolean hasNext() {
    return isLooping();
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
