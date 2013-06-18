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
import java.util.NoSuchElementException;

public final class EachIndexIterator implements Iterator<Integer> {

  private final int size;
  private int index;

  public EachIndexIterator(int size) {
    this.size = size;
  }

  @Override
  public boolean hasNext() {
    return index < size;
  }

  @Override
  public Integer next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return index++;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
