package net.sf.rubycollect4j.iter;

import java.util.Iterator;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.range.Successive;

public final class RangeIterator<E extends Comparable<E>> implements
    Iterator<E> {

  private final Successive<E> successive;
  private final E endPoint;
  private E curr;

  public RangeIterator(Successive<E> successive, E startPoint, E endPoint) {
    this.successive = successive;
    this.endPoint = endPoint;
    curr = startPoint;
  }

  @Override
  public boolean hasNext() {
    return successive.compare(curr, endPoint) <= 0;
  }

  @Override
  public E next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    E next = curr;
    curr = successive.succ(curr);
    return next;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
