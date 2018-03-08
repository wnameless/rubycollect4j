/*
 *
 * Copyright 2017 Wei-Ming Wu
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

import java.time.LocalDateTime;

/**
 * 
 * {@link LocalDateTimeSuccessor} generates a successor of any given
 * {@link LocalDateTime}. It's a singleton object.
 * 
 * @author Wei-Ming Wu
 * 
 */
public class LocalDateTimeSuccessor implements Successive<LocalDateTime> {

  private static final LocalDateTimeSuccessor INSTANCE =
      new LocalDateTimeSuccessor();

  private LocalDateTimeSuccessor() {}

  /**
   * Returns a {@link LocalDateTimeSuccessor}.
   * 
   * @return a {@link LocalDateTimeSuccessor}
   */
  public static LocalDateTimeSuccessor getInstance() {
    return LocalDateTimeSuccessor.INSTANCE;
  }

  @Override
  public LocalDateTime succ(LocalDateTime curr) {
    return curr.plusDays(1);
  }

  @Override
  public int compare(LocalDateTime arg0, LocalDateTime arg1) {
    return arg0.compareTo(arg1);
  }

  @Override
  public String toString() {
    return "LocalDateTimeSuccessor";
  }

}