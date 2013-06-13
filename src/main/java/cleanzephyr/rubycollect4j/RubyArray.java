/**
 *
 * @author Wei-Ming Wu
 *
 *
 * Copyright 2013 Wei-Ming Wu
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
package cleanzephyr.rubycollect4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Random;

import org.apache.commons.collections.comparators.ComparableComparator;

import cleanzephyr.rubycollect4j.block.Block;
import cleanzephyr.rubycollect4j.block.BooleanBlock;
import cleanzephyr.rubycollect4j.block.IndexBlock;
import cleanzephyr.rubycollect4j.block.IndexWithReturnBlock;
import cleanzephyr.rubycollect4j.block.ItemBlock;
import cleanzephyr.rubycollect4j.block.ItemTransformBlock;
import cleanzephyr.rubycollect4j.iter.CombinationIterable;
import cleanzephyr.rubycollect4j.iter.EachIndexIterable;
import cleanzephyr.rubycollect4j.iter.PermutationIterable;
import cleanzephyr.rubycollect4j.iter.ProductIterable;
import cleanzephyr.rubycollect4j.iter.RepeatedCombinationIterable;
import cleanzephyr.rubycollect4j.iter.RepeatedPermutationIterable;

import com.google.common.collect.Lists;

import static cleanzephyr.rubycollect4j.RubyEnumerator.newRubyEnumerator;
import static com.google.common.collect.Lists.newArrayList;

/**
 * 
 * @author WMW
 * @param <E>
 */
public final class RubyArray<E> extends RubyEnumerable<E> implements List<E> {

  private final List<E> list;
  private final Random rand = new Random();

  public static <E> RubyArray<E> newRubyArray() {
    List<E> list = newArrayList();
    return new RubyArray<E>(list);
  }

  public static <E> RubyArray<E> newRubyArray(Iterable<E> iter) {
    return new RubyArray<E>(newArrayList(iter));
  }

  public static <E> RubyArray<E> newRubyArray(Iterator<E> iter) {
    return new RubyArray<E>(newArrayList(iter));
  }

  public static <E> RubyArray<E> newRubyArray(List<E> list) {
    return new RubyArray<E>(list);
  }

  public static <E> RubyArray<E> newRubyArray(List<E> list,
      boolean defensiveCopy) {
    if (defensiveCopy) {
      return newRubyArray(newArrayList(list));
    } else {
      return newRubyArray(list);
    }
  }

  public static <E> RubyArray<E> newRubyArray(E... elements) {
    return new RubyArray<E>(newArrayList(elements));
  }

  private RubyArray(List<E> list) {
    super(list);
    this.list = list;
  }

  public RubyArray<E> ㄍ(E item) {
    return push(item);
  }

  public RubyArray<E> intersection(List<E> other) {
    List<E> andList = newArrayList();
    for (E item : this) {
      if (!andList.contains(item) && list.contains(item)
          && other.contains(item)) {
        andList.add(item);
      }
    }
    return newRubyArray(andList);
  }

  public RubyArray<E> Ⴖ(List<E> other) {
    return intersection(other);
  }

  public RubyArray<E> multiply(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative argument");
    }
    List<E> multiplyList = newArrayList();
    for (int i = 0; i < n; i++) {
      for (E item : list) {
        multiplyList.add(item);
      }
    }
    return newRubyArray(multiplyList);
  }

  public RubyArray<E> X(int n) {
    return multiply(n);
  }

  public String multiply(String separator) {
    return join(separator);
  }

  public String X(String separator) {
    return join(separator);
  }

  public RubyArray<E> add(List<E> other) {
    List<E> addList = newArrayList();
    for (E item : list) {
      addList.add(item);
    }
    for (E item : other) {
      addList.add(item);
    }
    return newRubyArray(addList);
  }

  public RubyArray<E> 十(List<E> other) {
    return add(other);
  }

  public RubyArray<E> minus(List<E> other) {
    List<E> minusList = newArrayList();
    for (E item : list) {
      minusList.add(item);
    }
    for (E item : other) {
      minusList.remove(item);
    }
    return newRubyArray(minusList);
  }

  public RubyArray<E> ㄧ(List<E> other) {
    return minus(other);
  }

  public <S> RubyArray<S> assoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        @SuppressWarnings("unchecked")
        List<S> itemList = (List<S>) item;
        if (itemList.size() > 0 && itemList.get(0).equals(target)) {
          return newRubyArray(itemList, true);
        }
      }
    }
    return null;
  }

  public E at(int index) {
    if (list.isEmpty()) {
      return null;
    } else if (index >= 0 && index < list.size()) {
      return list.get(index);
    } else if (index <= -1 && index >= -list.size()) {
      return list.get(index + list.size());
    } else {
      return null;
    }
  }

  // TODO: Implement bsearch to take code block
  public E bsearch(E target) {
    if (list.size() == 0) {
      return null;
    }
    E sample = list.get(0);
    if (sample instanceof Comparable) {
      @SuppressWarnings("unchecked")
      int index =
          Collections.binarySearch(list, target,
              ComparableComparator.getInstance());
      return index < 0 ? null : list.get(index);
    }
    Object[] array = list.toArray();
    int index = Arrays.binarySearch(array, target);
    return index < 0 ? null : list.get(index);
  }

  public E bsearch(E target, Comparator<? super E> comp) {
    int index = Collections.binarySearch(list, target, comp);
    return index < 0 ? null : list.get(index);
  }

  public RubyEnumerator<RubyArray<E>> combination(int n) {
    RubyArray<RubyArray<E>> comb = newRubyArray();
    if (n < 0) {
      return newRubyEnumerator(comb);
    } else if (n == 0) {
      RubyArray<E> ra = newRubyArray();
      comb.push(ra);
      return newRubyEnumerator(comb);
    } else if (n > list.size()) {
      return newRubyEnumerator(comb);
    } else {
      return newRubyEnumerator(new CombinationIterable<E>(list, n));
    }
  }

  public RubyArray<E> combination(int n, ItemBlock<RubyArray<E>> block) {
    for (RubyArray<E> c : combination(n)) {
      block.yield(c);
    }
    return this;
  }

  public RubyEnumerator<RubyArray<E>> repeatedCombination(int n) {
    RubyArray<RubyArray<E>> rc = newRubyArray();
    if (n < 0) {
      return newRubyEnumerator(rc);
    }
    if (n == 0) {
      RubyArray<E> ra = newRubyArray();
      return newRubyEnumerator(rc.push(ra));
    }
    return newRubyEnumerator(new RepeatedCombinationIterable<E>(list, n));
  }

  public RubyArray<E> repeatedCombination(int n, ItemBlock<RubyArray<E>> block) {
    for (RubyArray<E> c : repeatedCombination(n)) {
      block.yield(c);
    }
    return this;
  }

  public RubyArray<E> compact() {
    RubyArray<E> rubyArray = newRubyArray();
    for (E item : list) {
      if (item != null) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  public RubyArray<E> compactǃ() {
    boolean isCompacted = false;
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item == null) {
        li.remove();
        isCompacted = true;
      }
    }
    return isCompacted ? this : null;
  }

  public RubyArray<E> concat(List<E> other) {
    list.addAll(other);
    return this;
  }

  public int count(E target) {
    int count = 0;
    for (E item : list) {
      if (item.equals(target)) {
        count++;
      }
    }
    return count;
  }

  public E delete(E target) {
    boolean isDeleted = false;
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item.equals(target)) {
        li.remove();
        isDeleted = true;
      }
    }
    return isDeleted ? target : null;
  }

  public E delete(E target, Block<E> block) {
    boolean isDeleted = false;
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (item.equals(target)) {
        li.remove();
        isDeleted = true;
      }
    }
    return isDeleted ? target : block.yield();
  }

  public E deleteAt(int index) {
    if (list.isEmpty()) {
      return null;
    } else if (index >= 0 && index < list.size()) {
      return list.remove(index);
    } else if (index <= -1 && index >= -list.size()) {
      return list.remove(index + list.size());
    } else {
      return null;
    }
  }

  public RubyEnumerator<E> deleteIf() {
    return newRubyEnumerator(this);
  }

  public RubyArray<E> deleteIf(BooleanBlock<E> block) {
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (block.yield(item)) {
        li.remove();
      }
    }
    return this;
  }

  public RubyEnumerator<E> each() {
    return newRubyEnumerator(list);
  }

  public RubyArray<E> each(ItemBlock<E> block) {
    for (E item : list) {
      block.yield(item);
    }
    return this;
  }

  public RubyEnumerator<Integer> eachIndex() {
    return newRubyEnumerator(new EachIndexIterable(list.size()));
  }

  public RubyArray<E> eachIndex(IndexBlock block) {
    for (int i = 0; i < list.size(); i++) {
      block.yield(i);
    }
    return this;
  }

  public boolean emptyʔ() {
    return list.isEmpty();
  }

  public boolean eqlʔ(Object o) {
    return equals(o);
  }

  public E fetch(int index) {
    if (index >= list.size() || index < -list.size()) {
      throw new IllegalArgumentException("index " + index
          + " outside of array bounds: " + -list.size() + "..." + list.size());
    }
    return at(index);
  }

  public E fetch(int index, E defaultValue) {
    if (index >= list.size() || index < -list.size()) {
      return defaultValue;
    }
    return at(index);
  }

  public E fetch(int index, IndexBlock block) {
    if (index >= list.size() || index < -list.size()) {
      block.yield(index);
      return null;
    }
    return at(index);
  }

  public RubyArray<E> fill(E item) {
    for (int i = 0; i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  public RubyArray<E> fill(E item, int start) {
    if (start <= -list.size()) {
      return fill(item);
    }
    if (start < 0) {
      start += list.size();
    }
    for (int i = start; i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  public RubyArray<E> fill(E item, int start, int length) {
    if (start < 0) {
      start += list.size();
      if (start < 0) {
        start = 0;
      }
    }
    if (start > list.size()) {
      for (int i = list.size(); i < start; i++) {
        list.add(null);
      }
    }
    for (int i = start; i < list.size() && i < start + length; i++) {
      list.set(i, item);
    }
    for (int i = list.size(); i < start + length; i++) {
      list.add(item);
    }
    return this;
  }

  public RubyArray<E> fill(IndexWithReturnBlock<E> block) {
    for (int i = 0; i < list.size(); i++) {
      list.set(i, block.yield(i));
    }
    return this;
  }

  public RubyArray<E> fill(int start, IndexWithReturnBlock<E> block) {
    if (start <= -list.size()) {
      return fill(block);
    }
    if (start < 0) {
      start += list.size();
    }
    for (int i = start; i < list.size(); i++) {
      list.set(i, block.yield(i));
    }
    return this;
  }

  public RubyArray<E>
      fill(int start, int length, IndexWithReturnBlock<E> block) {
    if (start < 0) {
      start += list.size();
      if (start < 0) {
        start = 0;
      }
    }
    if (start > list.size()) {
      for (int i = list.size(); i < start; i++) {
        list.add(null);
      }
    }
    for (int i = start; i < list.size() && i < start + length; i++) {
      list.set(i, block.yield(i));
    }
    for (int i = list.size(); i < start + length; i++) {
      list.add(block.yield(i));
    }
    return this;
  }

  @SuppressWarnings("unchecked")
  public <S> RubyArray<S> flatten() {
    RubyArray<S> rubyArray = newRubyArray();
    List<Object> subLists = newArrayList();
    for (E item : list) {
      if (item instanceof List) {
        subLists.add(item);
      } else {
        rubyArray.add((S) item);
      }
    }
    while (!subLists.isEmpty()) {
      if (subLists.get(0) instanceof List) {
        List<?> subList = (List<?>) subLists.remove(0);
        for (Object item : subList) {
          if (item instanceof List) {
            subLists.add(item);
          } else {
            rubyArray.add((S) item);
          }
        }
      } else {
        rubyArray.add((S) subLists.remove(0));
      }
    }
    return rubyArray;
  }

  public Integer index(BooleanBlock<E> block) {
    Integer index = null;
    for (int i = 0; i < list.size(); i++) {
      if (block.yield(list.get(i))) {
        return i;
      }
    }
    return index;
  }

  public Integer index(E target) {
    int idx = list.indexOf(target);
    return idx == -1 ? null : idx;
  }

  public RubyArray<E> replace(List<E> other) {
    list.clear();
    list.addAll(other);
    return this;
  }

  public RubyArray<E> insert(int index, E... args) {
    if (index < -list.size()) {
      throw new IllegalArgumentException("IndexError: index " + index
          + " too small for array; minimum: " + -list.size());
    } else if (index < 0) {
      int relIndex = list.size() + index + 1;
      for (int i = args.length - 1; i >= 0; i--) {
        list.add(relIndex, args[i]);
      }
    } else if (index <= list.size()) {
      for (int i = args.length - 1; i >= 0; i--) {
        list.add(index, args[i]);
      }
    } else {
      while (index > list.size()) {
        list.add(null);
      }
      for (int i = args.length - 1; i >= 0; i--) {
        list.add(index - args.length + 2, args[i]);
      }
    }
    return this;
  }

  public String toS() {
    return list.toString();
  }

  public String inspect() {
    return list.toString();
  }

  public String join() {
    StringBuilder sb = new StringBuilder();
    for (E item : list) {
      sb.append(item.toString());
    }
    return sb.toString();
  }

  public String join(String separator) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < list.size(); i++) {
      if (i > 0 && i != list.size()) {
        sb.append(separator);
      }
      sb.append(list.get(i).toString());
    }
    return sb.toString();
  }

  public RubyArray<E> keepIf(BooleanBlock<E> block) {
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (!(block.yield(item))) {
        li.remove();
      }
    }
    return this;
  }

  public E last() {
    List<E> reverseList = Lists.reverse(list);
    if (reverseList.isEmpty()) {
      return null;
    } else {
      return reverseList.get(0);
    }
  }

  public RubyArray<E> last(int n) {
    List<E> reverseList = Lists.reverse(list);
    RubyArray<E> rubyArray = newRubyArray();
    for (int i = 0; i < n && i < reverseList.size(); i++) {
      rubyArray.add(reverseList.get(i));
    }
    return rubyArray;
  }

  public RubyEnumerator<RubyArray<E>> permutation() {
    return newRubyEnumerator(new PermutationIterable<E>(list, list.size()));
  }

  public RubyEnumerator<RubyArray<E>> permutation(int n) {
    RubyArray<RubyArray<E>> perms = newRubyArray();
    if (n < 0) {
      return newRubyEnumerator(perms);
    } else if (n == 0) {
      RubyArray<E> ra = newRubyArray();
      perms.push(ra);
      return newRubyEnumerator(perms);
    } else if (n > list.size()) {
      return newRubyEnumerator(perms);
    } else {
      return newRubyEnumerator(new PermutationIterable<E>(list, n));
    }
  }

  public RubyArray<E> permutation(int n, ItemBlock<RubyArray<E>> block) {
    for (RubyArray<E> item : permutation(n)) {
      block.yield(item);
    }
    return this;
  }

  public RubyEnumerator<RubyArray<E>> repeatedPermutation(int n) {
    RubyArray<RubyArray<E>> rp = newRubyArray();
    if (n < 0) {
      return newRubyEnumerator(rp);
    }
    if (n == 0) {
      RubyArray<E> ra = newRubyArray();
      return newRubyEnumerator(rp.push(ra));
    }
    return newRubyEnumerator(new RepeatedPermutationIterable<E>(list, n));
  }

  public RubyArray<E> repeatedPermutation(int n, ItemBlock<RubyArray<E>> block) {
    for (RubyArray<E> perm : repeatedPermutation(n)) {
      block.yield(perm);
    }
    return this;
  }

  public E pop() {
    if (list.isEmpty()) {
      return null;
    } else {
      return list.remove(list.size() - 1);
    }
  }

  public RubyArray<E> pop(int n) {
    RubyArray<E> rubyArray = newRubyArray();
    for (int i = 0; i < n; i++) {
      rubyArray.add(0, pop());
    }
    rubyArray.keepIf(new BooleanBlock<E>() {

      @Override
      public boolean yield(E item) {
        return item != null;
      }

    });
    return rubyArray;
  }

  public RubyArray<RubyArray<E>> product(List<List<E>> others) {
    return newRubyEnumerable(new ProductIterable<E>(this, others)).toA();
  }

  public RubyArray<RubyArray<E>> product(List<E>... others) {
    return newRubyEnumerable(new ProductIterable<E>(this, others)).toA();
  }

  public RubyArray<E> product(List<List<E>> others,
      ItemBlock<RubyArray<E>> block) {
    for (RubyArray<E> comb : product(others)) {
      block.yield(comb);
    }
    return this;
  }

  public RubyArray<E> push(E item) {
    list.add(item);
    return this;
  }

  public <S> RubyArray<S> rassoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        @SuppressWarnings("unchecked")
        List<S> itemList = (List<S>) item;
        if (itemList.size() > 0
            && itemList.get(itemList.size() - 1).equals(target)) {
          return newRubyArray(itemList, true);
        }
      }
    }
    return null;
  }

  public RubyArray<E> rejectǃ(BooleanBlock<E> block) {
    int beforeLength = list.size();
    RubyArray<E> rubyArray = deleteIf(block);
    if (rubyArray.size() != beforeLength) {
      return rubyArray;
    } else {
      return null;
    }
  }

  public RubyArray<E> replace(RubyArray<E> other) {
    list.clear();
    list.addAll(other);
    return this;
  }

  public RubyArray<E> reverse() {
    return newRubyArray(Lists.reverse(list));
  }

  public RubyArray<E> reverseǃ() {
    List<E> reversedList = Lists.reverse(list);
    list.clear();
    list.addAll(reversedList);
    return this;
  }

  public Integer rindex(E target) {
    Integer index = null;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (list.get(i).equals(target)) {
        return i;
      }
    }
    return index;
  }

  public Integer rindex(BooleanBlock<E> block) {
    Integer index = null;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (block.yield(list.get(i))) {
        return i;
      }
    }
    return index;
  }

  public RubyArray<E> rotate() {
    RubyArray<E> rubyArray = newRubyArray(list, true);
    if (rubyArray.size() > 0) {
      rubyArray.add(rubyArray.remove(0));
    }
    return rubyArray;
  }

  public RubyArray<E> rotateǃ() {
    if (list.size() > 0) {
      list.add(list.remove(0));
    }
    return this;
  }

  public RubyArray<E> rotate(int count) {
    List<E> rotatedList = newArrayList(list);
    if (rotatedList.size() > 0) {
      while (count != 0) {
        if (count > 0) {
          rotatedList.add(rotatedList.remove(0));
          count--;
        } else if (count < 0) {
          rotatedList.add(0, rotatedList.remove(rotatedList.size() - 1));
          count++;
        }
      }
    }
    return newRubyArray(rotatedList);
  }

  public RubyArray<E> rotateǃ(int count) {
    if (list.size() > 0) {
      while (count != 0) {
        if (count > 0) {
          list.add(list.remove(0));
          count--;
        } else if (count < 0) {
          list.add(0, list.remove(list.size() - 1));
          count++;
        }
      }
    }
    return this;
  }

  public E sample() {
    if (list.size() > 0) {
      return list.get(rand.nextInt(list.size()));
    } else {
      return null;
    }
  }

  public RubyArray<E> sample(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative sample number");
    }
    List<Integer> indice = newArrayList();
    for (int i = 0; i < list.size(); i++) {
      indice.add(i);
    }
    List<E> samples = newArrayList();
    while (samples.size() < list.size() && samples.size() < n) {
      samples.add(list.get(indice.remove(rand.nextInt(indice.size()))));
    }
    return newRubyArray(samples);
  }

  public RubyArray<E> selectǃ(BooleanBlock<E> block) {
    RubyArray<E> rubyArray = select(block);
    if (rubyArray.size() == list.size()) {
      return null;
    } else {
      list.clear();
      list.addAll(rubyArray);
      return this;
    }
  }

  public E shift() {
    if (list.isEmpty()) {
      return null;
    } else {
      return list.remove(0);
    }
  }

  public RubyArray<E> shift(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative array size");
    }
    List<E> shiftedList = newArrayList();
    while (!list.isEmpty() && shiftedList.size() < n) {
      shiftedList.add(list.remove(0));
    }
    return newRubyArray(shiftedList);
  }

  public RubyArray<E> shuffle() {
    List<E> shuffledList = newArrayList(list);
    Collections.shuffle(shuffledList);
    return newRubyArray(shuffledList);
  }

  public RubyArray<E> shuffleǃ() {
    Collections.shuffle(list);
    return this;
  }

  public E slice(int index) {
    return at(index);
  }

  public RubyArray<E> slice(int index, int length) {
    List<E> slicedList = newArrayList();
    if (index < -list.size()) {
      return null;
    } else if (index >= list.size()) {
      return null;
    } else {
      if (index < 0) {
        index += list.size();
      }
      for (int i = index; i < list.size() && i < index + length; i++) {
        slicedList.add(list.get(i));
      }
    }
    return newRubyArray(slicedList);
  }

  public E sliceǃ(int index) {
    if (list.isEmpty()) {
      return null;
    } else if (index >= 0 && index < list.size()) {
      return list.remove(index);
    } else if (index <= -1 && index >= -list.size()) {
      return list.remove(index + list.size());
    } else {
      return null;
    }
  }

  public RubyArray<E> sliceǃ(int index, int length) {
    List<E> slicedList = newArrayList();
    if (index < -list.size()) {
      return null;
    } else if (index >= list.size()) {
      return null;
    } else {
      if (index < 0) {
        index += list.size();
      }
      for (int i = index; i < list.size() && length > 0;) {
        slicedList.add(list.remove(i));
        length--;
      }
    }
    return newRubyArray(slicedList);
  }

  public int length() {
    return list.size();
  }

  public RubyArray<E> uniq() {
    List<E> uniqList = newArrayList();
    for (E item : list) {
      if (!uniqList.contains(item)) {
        uniqList.add(item);
      }
    }
    return newRubyArray(uniqList);
  }

  public RubyArray<E> uniqǃ() {
    RubyArray<E> uniqList = uniq();
    list.clear();
    list.addAll(uniqList);
    return this;
  }

  public <S> RubyArray<E> uniq(ItemTransformBlock<E, S> block) {
    List<E> uniqList = newArrayList();
    List<S> uniqByList = newArrayList();
    for (E item : list) {
      S trans = block.yield(item);
      if (!uniqByList.contains(trans)) {
        uniqByList.add(trans);
        uniqList.add(item);
      }
    }
    return newRubyArray(uniqList);
  }

  public RubyArray<E> union(RubyArray<E> other) {
    List<E> unionList = newArrayList();
    for (E item : list) {
      if (!unionList.contains(item)) {
        unionList.add(item);
      }
    }
    for (E item : other) {
      if (!unionList.contains(item)) {
        unionList.add(item);
      }
    }
    return newRubyArray(unionList);
  }

  public RubyArray<E> U(RubyArray<E> other) {
    return union(other);
  }

  public RubyArray<E> ǀ(RubyArray<E> other) {
    return union(other);
  }

  public RubyArray<E> unshift(E item) {
    list.add(0, item);
    return this;
  }

  public RubyArray<E> valuesAt(int... indice) {
    List<E> values = newArrayList();
    for (int index : indice) {
      values.add(this.at(index));
    }
    return newRubyArray(values);
  }

  @Override
  public int size() {
    return list.size();
  }

  @Override
  public boolean isEmpty() {
    return list.isEmpty();
  }

  @Override
  public boolean contains(Object o) {
    return list.contains(o);
  }

  @Override
  public Iterator<E> iterator() {
    return list.iterator();
  }

  @Override
  public Object[] toArray() {
    return list.toArray();
  }

  @Override
  public <T> T[] toArray(T[] a) {
    return list.toArray(a);
  }

  @Override
  public boolean add(E e) {
    return list.add(e);
  }

  @Override
  public boolean remove(Object o) {
    return list.remove(o);
  }

  @Override
  public boolean containsAll(Collection<?> c) {
    return list.containsAll(c);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    return list.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    return list.addAll(index, c);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    return list.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    return list.retainAll(c);
  }

  @Override
  public void clear() {
    list.clear();
  }

  @Override
  public E get(int index) {
    return list.get(index);
  }

  @Override
  public E set(int index, E element) {
    return list.set(index, element);
  }

  @Override
  public void add(int index, E element) {
    list.add(index, element);
  }

  @Override
  public E remove(int index) {
    return list.remove(index);
  }

  @Override
  public int indexOf(Object o) {
    return list.indexOf(o);
  }

  @Override
  public int lastIndexOf(Object o) {
    return list.lastIndexOf(o);
  }

  @Override
  public ListIterator<E> listIterator() {
    return list.listIterator();
  }

  @Override
  public ListIterator<E> listIterator(int index) {
    return list.listIterator(index);
  }

  @Override
  public List<E> subList(int fromIndex, int toIndex) {
    return list.subList(fromIndex, toIndex);
  }

  @Override
  public boolean equals(Object o) {
    if (!(o instanceof List))
      return false;
    return list.equals(o);
  }

  @Override
  public int hashCode() {
    return list.hashCode();
  }

  @Override
  public String toString() {
    return list.toString();
  }

}
