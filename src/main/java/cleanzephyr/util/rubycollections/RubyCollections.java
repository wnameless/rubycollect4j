package cleanzephyr.util.rubycollections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public final class RubyCollections {

  public static <E> RubyArray<E> newRubyArray() {
    return new RubyArray();
  }

  public static <E> RubyArray<E> newRubyArray(Collection<E> coll) {
    return new RubyArray(new ArrayList<>(coll));
  }

  public static <E> RubyArray<E> newRubyArray(E... args) {
    return new RubyArray(Arrays.asList(args), true);
  }

  public static <E> RubyArray<E> ra(E... args) {
    return new RubyArray(Arrays.asList(args), true);
  }

  public static <K, V> RubyHash<K, V> rh(K key, V value) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key, value);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    rh.put(key8, value8);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    rh.put(key8, value8);
    rh.put(key9, value9);
    return rh;
  }

  public static <K, V> RubyHash<K, V> rh(K key1, V value1, K key2, V value2, K key3, V value3, K key4, V value4, K key5, V value5, K key6, V value6, K key7, V value7, K key8, V value8, K key9, V value9, K key10, V value10) {
    RubyHash<K, V> rh = new RubyHash();
    rh.put(key1, value1);
    rh.put(key2, value2);
    rh.put(key3, value3);
    rh.put(key4, value4);
    rh.put(key5, value5);
    rh.put(key6, value6);
    rh.put(key7, value7);
    rh.put(key8, value8);
    rh.put(key9, value9);
    rh.put(key10, value10);
    return rh;
  }

  public static void main(String[] args) {
    RubyArray<Integer> rubyAry = newRubyArray();
    rubyAry.add(1);
    rubyAry.add(2);
    rubyAry.add(3);
    rubyAry.add(4);
    rubyAry.add(5);
    rubyAry.add(6);
    //System.out.println(rubyAry.rotateEx(-1));
    System.out.println(ra(1, 2, 3).permutation());

    System.out.println(rh("a", 1, "b", 2).chunk((k, v) -> {
      return k + k;
    }));

    rh("a", 1, "b", 2, "c", 3).map((k, v) -> {
      return v * 2;
    }).each((v) -> {
      System.out.println(v);
    });
  }
}
