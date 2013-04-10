package cleanzephyr.util.rubycollections.blocks;

public interface InjectBlock<E> {

  public E yield(E memo, E item);
}
