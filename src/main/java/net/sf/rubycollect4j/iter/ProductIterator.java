/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;

/**
 * 
 * {@link ProductIterator} iterates all products of input Lists.
 * 
 * @param <E>
 *          the type of the elements
 */
public final class ProductIterator<E> implements Iterator<RubyArray<E>> {

  private final List<List<? extends E>> lists;
  private final int[] counter;

  /**
   * Creates a {@link ProductIterator}.
   * 
   * @param lists
   *          a List of Lists
   * @throws NullPointerException
   *           if lists is null
   */
  public ProductIterator(List<? extends List<? extends E>> lists) {
    if (lists == null) throw new NullPointerException();

    this.lists = new ArrayList<List<? extends E>>();
    for (List<? extends E> list : lists) {
      this.lists.add(new ArrayList<E>(list));
    }
    counter = new int[this.lists.size()];
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> product = newRubyArray();
    for (int i = 0; i < counter.length; i++) {
      product.add(lists.get(i).get(counter[i]));
    }
    increaseCounter();
    return product;
  }

  private void increaseCounter() {
    for (int i = counter.length - 1; i >= 0; i--) {
      if (counter[i] < lists.get(i).size() - 1) {
        counter[i]++;
        for (int j = i + 1; j < counter.length; j++) {
          counter[j] = 0;
        }
        break;
      } else {
        counter[i] = -1;
      }
    }
  }

  private boolean isLooping() {
    for (int i = 0; i < counter.length; i++) {
      if (lists.get(i).isEmpty() || counter[i] == -1) return false;
    }
    return true;
  }

  @Override
  public boolean hasNext() {
    return isLooping();
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) throw new NoSuchElementException();

    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
