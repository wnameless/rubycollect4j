package net.sf.rubycollect4j.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class PeekingIterator<E> implements Iterator<E> {

  private final Iterator<E> iterator;
  private E peek;
  private boolean hasPeek;

  public PeekingIterator(Iterator<E> iterator) {
    this.iterator = iterator;
    if (this.iterator.hasNext()) {
      peek = this.iterator.next();
      hasPeek = true;
    } else {
      hasPeek = false;
    }
  }

  @Override
  public boolean hasNext() {
    return hasPeek;
  }

  @Override
  public E next() {
    E result = peek;
    if (!hasNext())
      throw new NoSuchElementException();

    if (iterator.hasNext()) {
      peek = iterator.next();
      hasPeek = true;
    } else {
      hasPeek = false;
    }
    return result;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

  public E peek() {
    if (!hasPeek)
      throw new NoSuchElementException();

    return peek;
  }

}
