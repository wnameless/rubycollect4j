package net.sf.rubycollect4j;

import net.sf.rubycollect4j.iter.RangeIterable;
import net.sf.rubycollect4j.range.DoubleSuccessor;
import net.sf.rubycollect4j.range.IntegerSuccessor;
import net.sf.rubycollect4j.range.LongSuccessor;
import net.sf.rubycollect4j.range.StringSuccessor;
import net.sf.rubycollect4j.range.Successive;

public final class RubyRange<E extends Comparable<E>> extends RubyEnumerable<E> {

  public static RubyRange<Integer> range(int startPoint, int endPoint) {
    return new RubyRange<Integer>(IntegerSuccessor.getInstance(), startPoint,
        endPoint);
  }

  public static RubyRange<String> range(String startPoint, String endPoint) {
    return new RubyRange<String>(StringSuccessor.getInstance(), startPoint,
        endPoint);
  }

  public static RubyRange<Long> range(Long startPoint, Long endPoint) {
    return new RubyRange<Long>(LongSuccessor.getInstance(), startPoint,
        endPoint);
  }

  public static RubyRange<Double> range(Double startPoint, Double endPoint) {
    return new RubyRange<Double>(DoubleSuccessor.getInstance(), startPoint,
        endPoint);
  }

  public RubyRange(Successive<E> successive, E startPoint, E endPoint) {
    super(new RangeIterable<E>(successive, startPoint, endPoint));
  }

}
