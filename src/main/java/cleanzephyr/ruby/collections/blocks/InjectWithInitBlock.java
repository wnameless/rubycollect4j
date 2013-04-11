package cleanzephyr.ruby.collections.blocks;

public interface InjectWithInitBlock<E, S> {

  public S yield(S memo, E item);
}
