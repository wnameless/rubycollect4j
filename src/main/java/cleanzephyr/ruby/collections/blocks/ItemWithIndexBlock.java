package cleanzephyr.ruby.collections.blocks;

public interface ItemWithIndexBlock<E> {

  public void yield(E item, int index);
}
