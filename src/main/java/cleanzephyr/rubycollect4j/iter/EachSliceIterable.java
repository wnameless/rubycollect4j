package cleanzephyr.rubycollect4j.iter;

import cleanzephyr.rubycollect4j.RubyArray;
import java.util.Iterator;

public final class EachSliceIterable<E> implements Iterable<RubyArray<E>> {
  private final Iterable<E> iterable;
  private final int size;

  public EachSliceIterable(Iterable<E> iterable, int size) {
    this.iterable = iterable;
    this.size = size;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new EachSliceIterator<E>(iterable.iterator(), size);
  }

}
