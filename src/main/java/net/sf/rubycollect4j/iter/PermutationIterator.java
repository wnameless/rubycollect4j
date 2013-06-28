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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;

import static com.google.common.collect.Lists.newArrayList;
import static net.sf.rubycollect4j.RubyArray.newRubyArray;

/**
 * 
 * PermutationIterator generates all permutations of a List with length n.
 * 
 * @param <E>
 *          the type of the elements
 */
public class PermutationIterator<E> implements Iterator<RubyArray<E>> {

  private final List<E> list;
  private final int[] counter;
  private final int[] endStatus;
  private boolean hasMore = true;

  /**
   * The constructor of the PermutationIterator.
   * 
   * @param list
   *          a List
   * @param n
   *          length of each permutation
   */
  public PermutationIterator(List<E> list, int n) {
    this.list = list;
    if (n <= 0 || n > list.size()) {
      counter = new int[0];
      endStatus = new int[0];
      if (n != 0)
        hasMore = false;
    } else {
      counter = new int[n];
      initCounter();
      endStatus = new int[n];
      initEndStatus();
    }
  }

  private void initCounter() {
    for (int i = 0; i < counter.length; i++) {
      counter[i] = i;
    }
  }

  private void initEndStatus() {
    for (int i = 0; i < endStatus.length; i++) {
      endStatus[i] = list.size() - 1 - i;
    }
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> c = newRubyArray();
    for (int i = 0; i < counter.length; i++) {
      c.push(list.get(counter[i]));
    }
    if (Arrays.equals(counter, endStatus)) {
      hasMore = false;
    } else {
      increaseCounter();
    }
    return c;
  }

  private void increaseCounter() {
    for (int i = counter.length - 1; i >= 0; i--) {

      int next = getHigherIndex(i);
      if (next != -1) {
        counter[i] = next;
        return;
      } else if (i != 0) {
        int pre = getHigherIndex(i - 1);
        if (pre != -1) {
          counter[i - 1] = pre;
          for (int j = i; j < counter.length; j++) {
            counter[j] = getLowerIndex(j);
          }
          return;
        }
      }
    }
  }

  private int getHigherIndex(int pos) {
    int current = counter[pos];
    if (current + 1 >= list.size()) {
      return -1;
    }
    List<Integer> indice = getAllIndice();
    for (int i = 0; i <= pos; i++) {
      indice.remove(Integer.valueOf(counter[i]));
    }
    ListIterator<Integer> iter = indice.listIterator();
    while (iter.hasNext()) {
      Integer i = iter.next();
      if (i <= current) {
        iter.remove();
      }
    }
    if (indice.isEmpty()) {
      return -1;
    } else {
      return indice.get(0);
    }
  }

  private int getLowerIndex(int pos) {
    List<Integer> indice = getAllIndice();
    for (int i = 0; i < pos; i++) {
      indice.remove(Integer.valueOf(counter[i]));
    }
    if (indice.isEmpty()) {
      return -1;
    } else {
      return indice.get(0);
    }
  }

  private List<Integer> getAllIndice() {
    List<Integer> indice = newArrayList();
    for (int i = 0; i < list.size(); i++) {
      indice.add(i);
    }
    return indice;
  }

  @Override
  public boolean hasNext() {
    return hasMore;
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
