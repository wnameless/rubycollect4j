package net.sf.rubycollect4j.range;

public interface Successive<E extends Comparable<E>> extends Comparable<E> {

  E curr();

  Successive<E> rewind();

  E succ();

  E succǃ();

}
