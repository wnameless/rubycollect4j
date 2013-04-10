package cleanzephyr.util.rubycollections.blocks;

public interface ItemWithReturnBlock<E> {

  public E yield(E item);
}