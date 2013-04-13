package cleanzephyr.rubycollect4j.blocks;

public interface InjectBlock<E> {

  public E yield(E memo, E item);
}
