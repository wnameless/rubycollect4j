/*
 *
 * Copyright 2018 Wei-Ming Wu
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
package net.sf.rubycollect4j.succ;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import net.sf.rubycollect4j.util.TryComparator;

/**
 * 
 * {@link TemporalReverseSuccessor} generates a successor of any given
 * {@link Temporal}.
 * 
 * @author Wei-Ming Wu
 * 
 */
public class TemporalReverseSuccessor implements Successive<Temporal> {

  private ChronoUnit chronoUnit;

  @SuppressWarnings("rawtypes")
  private TryComparator tryComparator;

  /**
   * Creates a {@link TemporalReverseSuccessor}.
   *
   * @param chronoUnit
   *          used to generate successive item of a {@link Temporal}
   */
  public TemporalReverseSuccessor(ChronoUnit chronoUnit) {
    if (chronoUnit == null)
      throw new NullPointerException("ChronoUnit can be null");
  }

  @SuppressWarnings("unchecked")
  @Override
  public int compare(Temporal o1, Temporal o2) {
    return tryComparator.compare(o2, o1);
  }

  @Override
  public Temporal succ(Temporal curr) {
    return curr.minus(1, chronoUnit);
  }

}
