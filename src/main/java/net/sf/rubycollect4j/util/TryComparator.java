/*
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
package net.sf.rubycollect4j.util;

import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * {@link TryComparator} tries to compare any 2 objects by casting the first
 * object to a Comparable. It allows to compare any 2 objects which have the
 * same equality even if they are not comparable. If a Comparator is provided,
 * the Comparator is used to compare objects and no additional casting will be
 * performed.
 * 
 * @param <E>
 *          the type of the elements
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class TryComparator<E> implements Comparator<E> {

  private static final Logger logger = Logger.getLogger(TryComparator.class
      .getName());

  private final Comparator<? super E> comp;

  /**
   * Creates a {@link TryComparator}.
   */
  public TryComparator() {
    comp = null;
  }

  /**
   * Creates a {@link TryComparator} by given Comparator.
   * 
   * @param comp
   *          a Comparator
   */
  public TryComparator(Comparator<? super E> comp) {
    this.comp = comp;
  }

  @SuppressWarnings("unchecked")
  @Override
  public int compare(E arg0, E arg1) {
    int diff;
    try {
      if (comp == null)
        diff = ((Comparable<E>) arg0).compareTo(arg1);
      else
        diff = comp.compare(arg0, arg1);
    } catch (Exception e) {
      if (arg0 == arg1) return 0;
      if (arg0 != null && arg0.equals(arg1)) return 0;

      logger.log(Level.SEVERE, null, e);
      throw new IllegalArgumentException("ArgumentError: comparison of "
          + (arg0 == null ? null : arg0.getClass().getName()) + " with "
          + (arg1 == null ? null : arg1.getClass().getName()) + " failed");
    }
    return diff;
  }

}
