package cleanzephyr.ruby.collections.blocks;

public interface InjectBlock<E> {

  public E yield(E memo, E item);
}
