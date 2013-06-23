package net.sf.rubycollect4j.range;

public final class SuccessiveInteger implements Successive<Integer> {

  private Integer startPoint;
  private Integer curr;

  public SuccessiveInteger(Integer startPoint) {
    this.startPoint = startPoint;
    this.curr = startPoint;
  }

  @Override
  public Integer curr() {
    return curr;
  }

  @Override
  public Successive<Integer> rewind() {
    curr = startPoint;
    return this;
  }

  @Override
  public Integer succ() {
    return curr + 1;
  }

  @Override
  public Integer succǃ() {
    return curr++;
  }

  @Override
  public int compareTo(Integer o) {
    return curr.compareTo(o);
  }

}
