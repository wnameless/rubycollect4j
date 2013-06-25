package net.sf.rubycollect4j.range;

public final class LongSuccessor implements Successive<Long> {

  private static volatile LongSuccessor INSTANCE;

  public static LongSuccessor getInstance() {
    if (INSTANCE == null) {
      synchronized (LongSuccessor.class) {
        if (INSTANCE == null) {
          INSTANCE = new LongSuccessor();
        }
      }
    }
    return INSTANCE;
  }

  private LongSuccessor() {}

  @Override
  public Long succ(Long curr) {
    return curr + 1;
  }

  @Override
  public int compare(Long arg0, Long arg1) {
    return arg0.compareTo(arg1);
  }

}