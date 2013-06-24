package net.sf.rubycollect4j.range;

import java.util.Comparator;

public interface Successive<E extends Comparable<E>> extends Comparator<E> {

  E succ(E curr);

}
