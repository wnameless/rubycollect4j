/*
 *
 * Copyright 2018 Wei-Ming Wu
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
package net.sf.rubycollect4j.succ;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;

import net.sf.rubycollect4j.util.TryComparator;

/**
 * 
 * {@link TemporalSuccessor} generates a successor of any given {@link Temporal}.
 * 
 * @author Wei-Ming Wu
 * 
 */
public class TemporalSuccessor<T extends Temporal> implements Successive<T> {

  private TemporalUnit temporalUnit;

  @SuppressWarnings("rawtypes")
  private TryComparator tryComparator = new TryComparator();

  /**
   * Creates a {@link TemporalSuccessor}.
   *
   * @param temporalUnit used to generate successive item of a {@link Temporal}, {@link ChronoUnit}
   *        is recommended
   */
  public TemporalSuccessor(TemporalUnit temporalUnit) {
    if (temporalUnit == null) throw new NullPointerException("ChronoUnit can be null");

    this.temporalUnit = temporalUnit;
  }

  @SuppressWarnings("unchecked")
  @Override
  public int compare(T o1, T o2) {
    return tryComparator.compare(o1, o2);
  }

  @SuppressWarnings("unchecked")
  @Override
  public T succ(T curr) {
    return (T) curr.plus(1, temporalUnit);
  }

  @Override
  public String toString() {
    return "TemporalSuccessor{temporalUnit=" + temporalUnit + "}";
  }

}
