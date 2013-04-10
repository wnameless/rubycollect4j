package cleanzephyr.util.rubycollections.blocks;

public interface ItemWithIndexBlock<E> {

  public void yield(E item, int index);
}
