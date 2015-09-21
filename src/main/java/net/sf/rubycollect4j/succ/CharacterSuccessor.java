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
package net.sf.rubycollect4j.succ;

/**
 * 
 * {@link CharacterSuccessor} generates a successor of any given Character. It's
 * a singleton object.
 * 
 */
public final class CharacterSuccessor implements Successive<Character> {

  private static final CharacterSuccessor INSTANCE = new CharacterSuccessor();

  private CharacterSuccessor() {}

  /**
   * Returns a {@link CharacterSuccessor}.
   * 
   * @return a {@link CharacterSuccessor}
   */
  public static CharacterSuccessor getInstance() {
    return CharacterSuccessor.INSTANCE;
  }

  @Override
  public int compare(Character o1, Character o2) {
    return o1.compareTo(o2);
  }

  @Override
  public Character succ(Character curr) {
    return (char) (curr + 1);
  }

  @Override
  public String toString() {
    return "CharacterSuccessor";
  }

}
