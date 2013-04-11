package cleanzephyr.ruby.collections.blocks;

public interface TransformBlock<E, S> {

  public S yield(E item);
}
