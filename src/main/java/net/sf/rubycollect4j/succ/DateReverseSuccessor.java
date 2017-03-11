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

import java.util.Calendar;
import java.util.Date;

/**
 * 
 * {@link DateReverseSuccessor} generates a predecessor of any given Date. It's
 * a singleton object.
 * 
 * @author Wei-Ming Wu
 * 
 */
public final class DateReverseSuccessor implements Successive<Date> {

  private static final DateReverseSuccessor INSTANCE =
      new DateReverseSuccessor();

  private DateReverseSuccessor() {}

  /**
   * Returns a {@link DateReverseSuccessor}.
   * 
   * @return a {@link DateReverseSuccessor}
   */
  public static DateReverseSuccessor getInstance() {
    return DateReverseSuccessor.INSTANCE;
  }

  @Override
  public Date succ(Date curr) {
    Calendar c = Calendar.getInstance();
    c.setTime(curr);
    c.add(Calendar.DATE, -1);
    return c.getTime();
  }

  @Override
  public int compare(Date arg0, Date arg1) {
    return arg1.compareTo(arg0);
  }

  @Override
  public String toString() {
    return "DateReverseSuccessor";
  }

}