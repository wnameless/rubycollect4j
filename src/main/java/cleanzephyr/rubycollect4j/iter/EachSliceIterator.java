package cleanzephyr.rubycollect4j.iter;

import cleanzephyr.rubycollect4j.RubyArray;
import java.util.Iterator;
import java.util.NoSuchElementException;


public final class EachSliceIterator<E> implements Iterator<RubyArray<E>> {
  private final Iterator<E> iterator;
  private final int size;

  public EachSliceIterator(Iterator<E> iterator, int size) {
    this.iterator = iterator;
    this.size = size;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> bucket = new RubyArray<>();
    while (iterator.hasNext() && bucket.size() < size) {
      bucket.add(iterator.next());
    }
    return bucket;
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext())
      throw new NoSuchElementException();

    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
