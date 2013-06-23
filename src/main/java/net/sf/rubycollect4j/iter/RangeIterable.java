package net.sf.rubycollect4j.iter;

import java.util.Iterator;

import net.sf.rubycollect4j.range.Successive;

public final class RangeIterable<E extends Comparable<E>> implements
    Iterable<E> {

  private final Successive<E> successive;
  private final E endPoint;

  public RangeIterable(Successive<E> successive, E endPoint) {
    this.successive = successive;
    this.endPoint = endPoint;
  }

  @Override
  public Iterator<E> iterator() {
    return new RangeIterator<E>(successive.rewind(), endPoint);
  }

}
