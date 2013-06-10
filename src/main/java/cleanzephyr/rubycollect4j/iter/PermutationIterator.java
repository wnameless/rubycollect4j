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
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.uncommons.maths.combinatorics.CombinationGenerator;

import org.uncommons.maths.combinatorics.PermutationGenerator;

/**
 *
 * @author WMW
 * @param <E>
 */
public final class PermutationIterator<E> implements Iterator<RubyArray<E>> {

  private final CombinationGenerator<E> cg;
  private PermutationGenerator<E> pg;

  public PermutationIterator(Collection<E> coll, int n) {
    cg = new CombinationGenerator<>(coll, n);
    pg = new PermutationGenerator<>(cg.nextCombinationAsList());
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> perm = newRubyArray(pg.nextPermutationAsList());
    if (!pg.hasMore() && cg.hasMore()) {
      pg = new PermutationGenerator<>(cg.nextCombinationAsList());
    }
    return perm;
  }

  @Override
  public boolean hasNext() {
    return pg.hasMore();
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

}
