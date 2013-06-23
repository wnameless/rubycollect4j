package net.sf.rubycollect4j.iter;

import java.util.Iterator;

import net.sf.rubycollect4j.range.Successive;

public final class RangeIterator<E extends Comparable<E>> implements
    Iterator<E> {

  private final Successive<E> successive;
  private final E endPoint;

  public RangeIterator(Successive<E> successive, E endPoint) {
    this.successive = successive;
    this.endPoint = endPoint;
  }

  @Override
  public boolean hasNext() {
    return successive.compareTo(endPoint) <= 0;
  }

  @Override
  public E next() {
    return successive.succǃ();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
