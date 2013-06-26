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
package net.sf.rubycollect4j.range;

/**
 * 
 * IntegerSuccessor generates a successor of any given Integer. It's a singleton
 * object.
 * 
 */
public final class IntegerSuccessor implements Successive<Integer> {

  private static volatile IntegerSuccessor INSTANCE;

  /**
   * Returns the singleton IntegerSuccessor object.
   * 
   * @return a IntegerSuccessor
   */
  public static IntegerSuccessor getInstance() {
    if (INSTANCE == null) {
      synchronized (IntegerSuccessor.class) {
        if (INSTANCE == null) {
          INSTANCE = new IntegerSuccessor();
        }
      }
    }
    return INSTANCE;
  }

  private IntegerSuccessor() {}

  @Override
  public Integer succ(Integer curr) {
    return curr + 1;
  }

  @Override
  public int compare(Integer arg0, Integer arg1) {
    return arg0.compareTo(arg1);
  }

}
