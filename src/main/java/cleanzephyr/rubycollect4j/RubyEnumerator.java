package cleanzephyr.rubycollect4j;

import cleanzephyr.rubycollect4j.blocks.ItemBlock;
import static com.google.common.collect.Lists.newArrayList;
import java.util.Iterator;

public final class RubyEnumerator<E> extends RubyEnum<E> implements Iterable<E> {

  public RubyEnumerator(Iterable<E> iter) {
    super(iter);
  }

  public RubyEnumerator(Iterator<E> it) {
    super(newArrayList(it));
  }

  public RubyArray<E> each(ItemBlock<E> block) {
    RubyArray<E> rubyArray = new RubyArray<>();
    for (E item : iter) {
      block.yield(item);
      rubyArray.add(item);
    }
    return rubyArray;
  }

  public RubyEnumerator<E> each() {
    return this;
  }

  @Override
  public Iterator<E> iterator() {
    return iter.iterator();
  }
}
