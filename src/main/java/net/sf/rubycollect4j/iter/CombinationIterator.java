package net.sf.rubycollect4j.iter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import net.sf.rubycollect4j.RubyArray;

import static net.sf.rubycollect4j.RubyArray.newRubyArray;



public class CombinationIterator<E> implements Iterator<RubyArray<E>> {

  private final List<E> list;
  private final int[] counter;
  private final int[] endStatus;
  private boolean hasMore = true;

  public CombinationIterator(List<E> list, int n) {
    this.list = list;
    this.counter = new int[n];
    initCounter();
    this.endStatus = new int[n];
    initEndStatus();
    if (!isLooping() && !Arrays.equals(counter, endStatus)) {
      hasMore = false;
    }
  }

  private void initCounter() {
    for (int i = 0; i < counter.length; i++) {
      counter[i] = i;
    }
  }

  private void initEndStatus() {
    for (int i = endStatus.length - 1; i >= 0; i--) {
      endStatus[i] = list.size() - (endStatus.length - i);
    }
  }

  private boolean isLooping() {
    for (int i = 0; i < counter.length; i++) {
      if (counter[i] < endStatus[i]) {
        return true;
      }
    }
    return false;
  }

  private RubyArray<E> nextElement() {
    RubyArray<E> c = newRubyArray();
    for (int i = 0; i < counter.length; i++) {
      c.push(list.get(counter[i]));
    }
    if (Arrays.equals(counter, endStatus)) {
      hasMore = false;
    } else {
      increaseCounter();
    }
    return c;
  }

  private void increaseCounter() {
    for (int i = counter.length - 1; i >= 0; i--) {
      if (counter[i] < list.size() - (counter.length - i)) {
        counter[i]++;
        return;
      } else if (i != 0
          && counter[i - 1] != list.size() - (counter.length - i + 1)) {
        counter[i - 1]++;
        for (int j = i; j < counter.length; j++) {
          counter[j] = counter[j - 1] + 1;
        }
        return;
      }
    }
  }

  @Override
  public boolean hasNext() {
    return hasMore;
  }

  @Override
  public RubyArray<E> next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    return nextElement();
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
