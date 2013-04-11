package cleanzephyr.ruby.collections.blocks;

public interface ItemTransformBlock<E, S> {

  public S yield(E item);
}
