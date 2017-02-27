/*
 *
 * Copyright 2017 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package net.sf.rubycollect4j;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.sf.rubycollect4j.succ.IntegerSuccessor;

/**
 * @author Wei-Ming Wu
 *
 */
public final class Ruby {

  private Ruby() {}

  public static class Array {

    public static <E> RubyArray<E> create() {
      return new RubyArray<>();
    }

    public static <E> RubyArray<E> copyOf(List<E> list) {
      return RubyArray.copyOf(list);
    }

    public static <E> RubyArray<E> of(List<E> list) {
      return new RubyArray<>(list);
    }

    public static <E> RubyArray<E> of(Iterable<E> iter) {
      return new RubyArray<>(iter);
    }

    public static <E> RubyArray<E> of(Iterator<E> iter) {
      RubyArray<E> rubyArray = new RubyArray<>();
      iter.forEachRemaining(e -> rubyArray.add(e));
      return rubyArray;
    }

    @SafeVarargs
    public static <E> RubyArray<E> of(E... elements) {
      RubyArray<E> rubyArray = new RubyArray<>();
      Arrays.asList(elements).forEach(e -> rubyArray.add(e));
      return rubyArray;
    }

  }

  public static class Range {

    public static RubyRange<Integer> of(int start, int end) {
      return new RubyRange<Integer>(IntegerSuccessor.getInstance(), start, end);
    }

  }

  public static void main(String... strings) {
    Ruby.Array.create();
    Ruby.Range.of(1, 100);
    Ruby.Array.of(1, 2, 3);
  }

}
