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

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

public final class StringSuccessor implements Successive<String> {

  private static volatile StringSuccessor INSTANCE;

  public static StringSuccessor getInstance() {
    if (INSTANCE == null) {
      synchronized (IntegerSuccessor.class) {
        if (INSTANCE == null) {
          INSTANCE = new StringSuccessor();
        }
      }
    }
    return INSTANCE;
  }

  private StringSuccessor() {}

  @Override
  public String succ(String curr) {
    List<List<Character>> parts = partition(curr);
    if (parts.size() > 1
        || (parts.size() == 1 && isAlphanumeric(parts.get(0).get(0)))) {
      nextAlphanumeric(parts);
    } else {
      nextUTF8(parts.get(0));
    }
    StringBuilder sb = new StringBuilder();
    for (List<Character> chars : parts) {
      for (Character c : chars) {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  private void nextAlphanumeric(List<List<Character>> parts) {
    boolean carry = false;
    for (int i = parts.size() - 1; i >= 0; i--) {
      if (!(isAlphanumeric(parts.get(i).get(0))))
        continue;
      if (!(carry = increaseAlphanumeric(parts.get(i))))
        break;
    }
    if (carry) {
      for (List<Character> chars : parts) {
        Character c = chars.get(0);
        if (isAlphanumeric(c)) {
          if (48 <= (int) c && (int) c <= 57)
            chars.add(0, '1');
          if (65 <= (int) c && (int) c <= 90)
            chars.add(0, 'A');
          if (97 <= (int) c && (int) c <= 122)
            chars.add(0, 'a');
          break;
        }
      }
    }
  }

  private boolean increaseAlphanumeric(List<Character> alphanums) {
    boolean carry = false;
    for (int i = alphanums.size() - 1; i >= 0; i--) {
      char c = increaseASCII(alphanums.get(i));
      alphanums.set(i, c);
      if ((int) c == 48 || (int) c == 65 || (int) c == 97) {
        carry = true;
      } else {
        carry = false;
        break;
      }
    }
    return carry;
  }

  private char increaseASCII(char c) {
    if ((int) c == 57) {
      return (char) 48;
    } else if ((int) c == 90) {
      return (char) 65;
    } else if ((int) c == 122) {
      return (char) 97;
    } else {
      return (char) ((int) c + 1);
    }
  }

  private void nextUTF8(List<Character> utf8) {
    boolean carry = false;
    for (int i = utf8.size() - 1; i >= 0; i--) {
      char c = increaseUTF8(utf8.get(i));
      utf8.set(i, c);
      if ((int) c != 1) {
        carry = false;
        break;
      } else {
        carry = true;
      }
    }
    if (carry) {
      utf8.add(0, (char) 1);
    }
  }

  private char increaseUTF8(char c) {
    if ((int) c < 65535) {
      return (char) ((int) c + 1);
    } else {
      return (char) 1;
    }
  }

  private List<List<Character>> partition(String curr) {
    List<List<Character>> parts = newArrayList();
    List<Character> chars = newArrayList();
    for (char c : curr.toCharArray()) {
      if (chars.isEmpty()
          || isAlphanumeric(chars.get(chars.size() - 1)) == isAlphanumeric(c)) {
        chars.add(c);
      } else {
        parts.add(chars);
        chars = newArrayList();
        chars.add(c);
      }
    }
    if (!(chars.isEmpty())) {
      parts.add(chars);
    }
    return parts;
  }

  private boolean isAlphanumeric(char c) {
    boolean isNumber = 48 <= (int) c && (int) c <= 57;
    boolean isUpcase = 65 <= (int) c && (int) c <= 90;
    boolean isDowncase = 97 <= (int) c && (int) c <= 122;
    return isNumber || isUpcase || isDowncase;
  }

  @Override
  public int compare(String o1, String o2) {
    if (o1.matches("^\\d+(\\.\\d+)?$") && o2.matches("^\\d+(\\.\\d+)?$")) {
      return Double.valueOf(o1).compareTo(Double.valueOf(o2));
    }
    if (o1.length() > o2.length()) {
      return 1;
    }
    return o1.compareTo(o2);
  }

}
