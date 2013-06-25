package net.sf.rubycollect4j.range;

public final class DoubleSuccessor implements Successive<Double> {

  private static volatile DoubleSuccessor INSTANCE;

  public static DoubleSuccessor getInstance() {
    if (INSTANCE == null) {
      synchronized (LongSuccessor.class) {
        if (INSTANCE == null) {
          INSTANCE = new DoubleSuccessor();
        }
      }
    }
    return INSTANCE;
  }

  private DoubleSuccessor() {}

  @Override
  public Double succ(Double curr) {
    String doubleStr = curr.toString();
    int precision = doubleStr.lastIndexOf('.');
    return curr + 1.0 / Math.pow(10, doubleStr.length() - precision - 1);
  }

  @Override
  public int compare(Double arg0, Double arg1) {
    return arg0.compareTo(arg1);
  }

}