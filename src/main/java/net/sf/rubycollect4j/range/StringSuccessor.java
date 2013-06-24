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
    char[] chars = curr.toCharArray();

    return null;
  }

  private String[][] partition(String curr) {
    List<List<Character>> parts = newArrayList();
    List<Character> chars = newArrayList();
    for (char c : curr.toCharArray()) {
      if (chars.isEmpty()
          || isAlphanumeric(chars.get(chars.size() - 1)) == isAlphanumeric(c)) {
        chars.add(c);
      } else {
        // parts.add()
      }
    }
    return null;
  }

  private boolean isASCII(char c) {
    return 0 <= (int) c && (int) c <= 127;
  }

  private boolean isAlphanumeric(char c) {
    boolean isNumber = 48 <= (int) c && (int) c <= 57;
    boolean isUpcase = 65 <= (int) c && (int) c <= 90;
    boolean isDowncase = 97 <= (int) c && (int) c <= 122;
    return isNumber || isUpcase || isDowncase;
  }

  @Override
  public int compare(String o1, String o2) {
    // TODO Auto-generated method stub
    return 0;
  }

}
