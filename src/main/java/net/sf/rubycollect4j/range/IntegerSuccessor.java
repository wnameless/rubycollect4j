package net.sf.rubycollect4j.range;

public final class IntegerSuccessor implements Successive<Integer> {

  private static volatile IntegerSuccessor INSTANCE;

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
    return arg0 - arg1;
  }

}
