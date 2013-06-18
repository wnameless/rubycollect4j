package net.sf.rubycollect4j.iter;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.RubyArray;


public class CombinationIterable<E> implements Iterable<RubyArray<E>> {

  private final List<E> list;
  private final int n;

  public CombinationIterable(List<E> list, int n) {
    this.list = list;
    this.n = n;
  }

  public CombinationIterable(Iterable<E> iter, int n) {
    list = newArrayList(iter);
    this.n = n;
  }

  public CombinationIterable(Iterator<E> iter, int n) {
    list = newArrayList(iter);
    this.n = n;
  }

  @Override
  public Iterator<RubyArray<E>> iterator() {
    return new CombinationIterator<E>(list, n);
  }

}
