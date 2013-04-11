package cleanzephyr.ruby.collections.blocks;

public interface ItemWithReturnBlock<E> {

  public E yield(E item);
}