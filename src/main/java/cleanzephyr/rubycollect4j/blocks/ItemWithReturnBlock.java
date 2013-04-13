package cleanzephyr.rubycollect4j.blocks;

public interface ItemWithReturnBlock<E> {

  public E yield(E item);
}