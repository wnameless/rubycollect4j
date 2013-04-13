package cleanzephyr.rubycollect4j.blocks;

public interface ItemWithIndexBlock<E> {

  public void yield(E item, int index);
}
