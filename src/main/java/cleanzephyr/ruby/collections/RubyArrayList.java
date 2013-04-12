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
package cleanzephyr.ruby.collections;

import cleanzephyr.ruby.collections.blocks.Block;
import cleanzephyr.ruby.collections.blocks.BooleanBlock;
import cleanzephyr.ruby.collections.blocks.IndexBlock;
import cleanzephyr.ruby.collections.blocks.InjectBlock;
import cleanzephyr.ruby.collections.blocks.InjectWithInitBlock;
import cleanzephyr.ruby.collections.blocks.ItemBlock;
import cleanzephyr.ruby.collections.blocks.ItemFromListBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithIndexBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithObjectBlock;
import cleanzephyr.ruby.collections.blocks.ItemWithReturnBlock;
import cleanzephyr.ruby.collections.blocks.ItemToListBlock;
import cleanzephyr.ruby.collections.blocks.ItemTransformBlock;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import static com.google.common.collect.Lists.newArrayList;
import com.google.common.collect.Multimap;
import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.regex.Pattern;
import org.uncommons.maths.combinatorics.CombinationGenerator;
import org.uncommons.maths.combinatorics.PermutationGenerator;

public final class RubyArrayList<E> implements RubyArray<E> {

  private final List<E> list;
  private final Random rand = new Random();

  public RubyArrayList() {
    list = newArrayList();
  }

  public RubyArrayList(E... args) {
    list = newArrayList(args);
  }

  public RubyArrayList(List<E> list) {
    this.list = list;
  }

  public RubyArrayList(List<E> list, boolean defensiveCopy) {
    if (defensiveCopy) {
      this.list = newArrayList(list);
    } else {
      this.list = list;
    }
  }

  public RubyArrayList(Collection<E> coll) {
    list = newArrayList(coll);
  }

  public RubyArrayList(Iterator<E> iter) {
    list = newArrayList(iter);
  }

  // Ruby Array methods
  @Override
  public RubyArray<E> and(List<E> other) {
    List<E> andList = newArrayList();
    for (E item : this) {
      if (!andList.contains(item) && list.contains(item) && other.contains(item)) {
        andList.add(item);
      }
    }
    return new RubyArrayList(andList);
  }

  @Override
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
    return new RubyArrayList(multiplyList);
  }

  @Override
  public String multiply(String separator) {
    return this.join(separator);
  }

  @Override
  public RubyArray<E> add(List<E> other) {
    List<E> addList = newArrayList();
    for (E item : list) {
      addList.add(item);
    }
    for (E item : other) {
      addList.add(item);
    }
    return new RubyArrayList(addList);
  }

  @Override
  public RubyArray<E> minus(List<E> other) {
    List<E> minusList = newArrayList();
    for (E item : list) {
      minusList.add(item);
    }
    for (E item : other) {
      minusList.remove(item);
    }
    return new RubyArrayList(minusList);
  }

  @Override
  public <S> RubyArray<S> assoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        List itemList = (List) item;
        if (itemList.size() > 0 && itemList.get(0).equals(target)) {
          return new RubyArrayList(itemList, true);
        }
      }
    }
    return null;
  }

  @Override
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

  @Override
  public E bsearch(E target) {
    Object[] array = list.toArray();
    int index = Arrays.binarySearch(array, target);
    return index < 0 ? null : list.get(index);
  }

  @Override
  public E bsearch(E target, Comparator<? super E> comp) {
    int index = Collections.binarySearch(list, target, comp);
    return index < 0 ? null : list.get(index);
  }

  @Override
  public RubyArray<RubyArray<E>> combination(int n) {
    RubyArray<RubyArray<E>> comb = new RubyArrayList();
    if (n < 0) {
      return comb;
    } else if (n == 0) {
      comb.add(new RubyArrayList<E>());
      return comb;
    } else if (n > list.size()) {
      return comb;
    } else {
      CombinationGenerator<E> cg = new CombinationGenerator<>(list, n);
      for (List<E> combination : cg) {
        comb.add(new RubyArrayList<E>(combination));
      }
    }
    return comb;
  }

  @Override
  public RubyArray<RubyArray<E>> combination(int n, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> comb = combination(n);
    for (RubyArray<E> item : comb) {
      block.yield(item);
    }
    return comb;
  }

  @Override
  public RubyArray<RubyArray<E>> repeatedCombination(int n) {
    RubyArray<RubyArray<E>> rp = new RubyArrayList();
    if (n < 0) {
      return rp;
    }
    if (n == 0) {
      return rp.push(new RubyArrayList());
    }
    int[] counter = new int[n];
    repeatedCombinationLoop(counter, 0, list.size() - 1, (count) -> {
      RubyArray<E> c = new RubyArrayList();
      for (int i = 0; i < count.length; i++) {
        c.push(list.get(count[i]));
      }
      rp.add(c);
    });
    return rp;
  }

  private void repeatedCombinationLoop(int[] counter, int start, int end, ItemBlock<int[]> block) {
    int[] endStatus = new int[counter.length];
    Arrays.fill(endStatus, end);
    do {
      block.yield(counter);
      increaseCombinationLoopCounter(counter, start, end);
    } while (!Arrays.equals(counter, endStatus));
    block.yield(counter);
  }

  private void increaseCombinationLoopCounter(int[] counter, int start, int end) {
    for (int i = counter.length - 1; i >= 0; i--) {
      if (counter[i] < end) {
        counter[i]++;
        return;
      } else if (i != 0
              && counter[i - 1] != end) {
        counter[i - 1]++;
        for (int j = i; j < counter.length; j++) {
          counter[j] = counter[ i - 1];
        }
        return;
      }
    }
  }

  public RubyArray<RubyArray<E>> repeatedCombination(int n, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> rp = new RubyArrayList();
    if (n < 0) {
      return rp;
    }
    if (n == 0) {
      rp.push(new RubyArrayList());
      block.yield(rp.first());
      return rp;
    }
    int[] counter = new int[n];
    repeatedCombinationLoop(counter, 0, list.size() - 1, (count) -> {
      RubyArray<E> c = new RubyArrayList();
      for (int i = 0; i < count.length; i++) {
        c.push(list.get(count[i]));
      }
      block.yield(c);
      rp.add(c);
    });
    return rp;
  }

  @Override
  public RubyArray<E> compact() {
    RubyArray<E> rubyArray = new RubyArrayList();
    for (E item : list) {
      if (item != null) {
        rubyArray.add(item);
      }
    }
    return rubyArray;
  }

  @Override
  public RubyArrayList<E> compactEx() {
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

  @Override
  public RubyArray<E> concat(RubyArray<E> other) {
    list.addAll(other);
    return this;
  }

  @Override
  public int count(E target) {
    int count = 0;
    for (E item : list) {
      if (item.equals(target)) {
        count++;
      }
    }
    return count;
  }

  @Override
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

  @Override
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

  @Override
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

  @Override
  public RubyArrayList<E> deleteIf(BooleanBlock<E> block) {
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (block.yield(item)) {
        li.remove();
      }
    }
    return this;
  }

  @Override
  public void each(ItemBlock<E> block) {
    for (E item : list) {
      block.yield(item);
    }
  }

  @Override
  public void eachIndex(IndexBlock<E> block) {
    for (int i = 0; i < list.size(); i++) {
      block.yield(i);
    }
  }

  @Override
  public boolean eql(RubyArray<E> other) {
    return this.equals(other);
  }

  @Override
  public E fetch(int index) {
    return at(index);
  }

  @Override
  public E fetch(int index, E defaultValue) {
    E target = at(index);
    if (target == null) {
      return defaultValue;
    } else {
      return target;
    }
  }

  @Override
  public E fetch(int index, ItemBlock<E> block) {
    E target = at(index);
    if (target == null) {
      block.yield(target);
      return null;
    } else {
      return target;
    }
  }

  @Override
  public RubyArrayList<E> fill(E item) {
    for (int i = 0; i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  @Override
  public RubyArrayList<E> fill(E item, int start) {
    for (int i = start; i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  @Override
  public RubyArrayList<E> fill(E item, int start, int length) {
    for (int i = start; i < start + length && i < list.size(); i++) {
      list.set(i, item);
    }
    return this;
  }

  @Override
  public RubyArrayList<E> fill(ItemWithReturnBlock<E> block) {
    for (int i = 0; i < list.size(); i++) {
      list.set(i, block.yield(list.get(i)));
    }
    return this;
  }

  @Override
  public RubyArrayList<E> fill(int start, ItemWithReturnBlock<E> block) {
    for (int i = start; i < list.size(); i++) {
      list.set(i, block.yield(list.get(i)));
    }
    return this;
  }

  @Override
  public RubyArrayList<E> fill(int start, int length, ItemWithReturnBlock<E> block) {
    for (int i = start; i < start + length && i < list.size(); i++) {
      list.set(i, block.yield(list.get(i)));
    }
    return this;
  }

  @Override
  public Integer index(E target) {
    Integer index = null;
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).equals(target)) {
        return i;
      }
    }
    return index;
  }

  @Override
  public Integer index(BooleanBlock<E> block) {
    Integer index = null;
    for (int i = 0; i < list.size(); i++) {
      if (block.yield(list.get(i))) {
        return i;
      }
    }
    return index;
  }

  @Override
  public <S> RubyArrayList<S> flatten() {
    RubyArrayList<S> rubyArray = new RubyArrayList();
    List subLists = newArrayList();
    for (E item : list) {
      if (item instanceof List) {
        subLists.add(item);
      } else {
        rubyArray.add((S) item);
      }
    }
    while (!subLists.isEmpty()) {
      if (subLists.get(0) instanceof List) {
        List subList = (List) subLists.remove(0);
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

  @Override
  public RubyArrayList<E> replace(List<E> other) {
    list.clear();
    list.addAll(other);
    return this;
  }

  @Override
  public RubyArrayList<E> insert(int index, E... args) {
    if (index < -(list.size() + 1)) {
      return null;
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
      System.out.println(list);
      for (int i = args.length - 1; i >= 0; i--) {
        list.add(index - args.length + 2, args[i]);
      }
    }
    return this;
  }

  @Override
  public String inspect() {
    return list.toString();
  }

  @Override
  public String join() {
    StringBuilder sb = new StringBuilder();
    for (E item : list) {
      sb.append(item.toString());
    }
    return sb.toString();
  }

  @Override
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

  @Override
  public RubyArrayList<E> keepIf(BooleanBlock<E> block) {
    ListIterator<E> li = list.listIterator();
    while (li.hasNext()) {
      E item = li.next();
      if (!(block.yield(item))) {
        li.remove();
      }
    }
    return this;
  }

  @Override
  public E last() {
    List<E> reverseList = Lists.reverse(list);
    if (reverseList.isEmpty()) {
      return null;
    } else {
      return reverseList.get(0);
    }
  }

  @Override
  public RubyArrayList<E> last(int n) {
    List<E> reverseList = Lists.reverse(list);
    RubyArrayList<E> rubyArray = new RubyArrayList();
    for (int i = 0; i < n && i < reverseList.size(); i++) {
      rubyArray.add(reverseList.get(i));
    }
    return rubyArray;
  }

  @Override
  public E pop() {
    if (list.isEmpty()) {
      return null;
    } else {
      return list.remove(list.size() - 1);
    }
  }

  @Override
  public RubyArrayList<E> pop(int n) {
    RubyArrayList<E> rubyArray = new RubyArrayList();
    for (int i = 0; i < n; i++) {
      rubyArray.add(0, pop());
    }
    rubyArray.keepIf((val) -> {
      return val != null;
    });
    return rubyArray;
  }

  @Override
  public RubyArray<RubyArray<E>> product(RubyArray<E>... arys) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArrayList();
    RubyArray<E>[] others = new RubyArrayList[arys.length + 1];
    others[0] = this;
    System.arraycopy(arys, 0, others, 1, arys.length);
    int[] counters = new int[others.length];
    while (isLooping(counters, others)) {
      RubyArrayList<E> combination = new RubyArrayList();
      for (int i = 0; i < counters.length; i++) {
        combination.add(others[i].get(counters[i]));
      }
      rubyArray.add(combination);
      increaseCounters(counters, others);
    }
    return rubyArray;
  }

  private void increaseCounters(int[] counters, RubyArray<E>[] others) {
    for (int i = counters.length - 1; i >= 0; i--) {
      if (counters[i] < others[i].size() - 1) {
        counters[i]++;
        for (int j = i + 1; j < counters.length; j++) {
          counters[j] = 0;
        }
        return;
      } else {
        counters[i] = -1;
      }
    }
  }

  private boolean isLooping(int[] counters, RubyArray<E>[] others) {
    for (int i = 0; i < counters.length; i++) {
      if (others[i].isEmpty()) {
        return false;
      }
    }
    return Arrays.binarySearch(counters, -1) == -1;
  }

  @Override
  public void product(RubyArray<RubyArray<E>> arys, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> combinations = product(arys.toArray(new RubyArrayList[arys.length()]));
    for (RubyArray<E> comb : combinations) {
      block.yield(comb);
    }
  }

  @Override
  public RubyArray<E> push(E item) {
    list.add(item);
    return this;
  }

  @Override
  public <S> RubyArray<S> rassoc(S target) {
    for (E item : list) {
      if (item instanceof List) {
        List itemList = (List) item;
        if (itemList.size() > 0 && itemList.get(itemList.size() - 1).equals(target)) {
          return new RubyArrayList(itemList, true);
        }
      }
    }
    return null;
  }

  @Override
  public RubyArray<E> rejectEx(BooleanBlock<E> block) {
    int beforeLength = list.size();
    RubyArray<E> rubyArray = deleteIf(block);
    if (rubyArray.size() != beforeLength) {
      return rubyArray;
    } else {
      return null;
    }
  }

  @Override
  public RubyArray<E> replace(RubyArray<E> other) {
    list.clear();
    list.addAll(other);
    return this;
  }

  @Override
  public RubyArray<E> reverse() {
    return new RubyArrayList(Lists.reverse(list));
  }

  @Override
  public RubyArray<E> reverseEx() {
    List<E> reversedList = Lists.reverse(list);
    list.clear();
    list.addAll(reversedList);
    return this;
  }

  @Override
  public Integer rindex(E target) {
    Integer index = null;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (list.get(i).equals(target)) {
        return i;
      }
    }
    return index;
  }

  @Override
  public Integer rindex(BooleanBlock<E> block) {
    Integer index = null;
    for (int i = list.size() - 1; i >= 0; i--) {
      if (block.yield(list.get(i))) {
        return i;
      }
    }
    return index;
  }

  @Override
  public RubyArray<E> rotate() {
    RubyArray<E> rubyArray = new RubyArrayList<>(list, true);
    if (rubyArray.size() > 0) {
      rubyArray.add(rubyArray.remove(0));
    }
    return rubyArray;
  }

  @Override
  public RubyArray<E> rotateEx() {
    if (list.size() > 0) {
      list.add(list.remove(0));
    }
    return this;
  }

  @Override
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
    return new RubyArrayList<>(rotatedList);
  }

  @Override
  public RubyArray<E> rotateEx(int count) {
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

  @Override
  public E sample() {
    if (list.size() > 0) {
      return list.get(rand.nextInt(list.size()));
    } else {
      return null;
    }
  }

  @Override
  public RubyArray<E> sample(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative sample number");
    }
    List<E> copyList = newArrayList(list);
    List<E> samples = newArrayList();
    while (samples.size() < list.size() && samples.size() < n) {
      samples.add(copyList.remove(rand.nextInt(copyList.size())));
    }
    return new RubyArrayList(samples);
  }

  @Override
  public RubyArray<E> selectEx(BooleanBlock block) {
    RubyArray<E> rubyArray = select(block);
    if (rubyArray.size() == list.size()) {
      return null;
    } else {
      list.clear();
      list.addAll(rubyArray);
      return this;
    }
  }

  @Override
  public E shift() {
    if (list.isEmpty()) {
      return null;
    } else {
      return list.remove(0);
    }
  }

  @Override
  public RubyArray<E> shift(int n) {
    if (n < 0) {
      throw new IllegalArgumentException("negative array size");
    }
    List<E> shiftedList = newArrayList();
    while (!list.isEmpty() && shiftedList.size() < n) {
      shiftedList.add(list.remove(0));
    }
    return new RubyArrayList(shiftedList);
  }

  @Override
  public RubyArray<E> shuffle() {
    List<E> shuffledList = newArrayList(list);
    Collections.shuffle(shuffledList);
    return new RubyArrayList(shuffledList);
  }

  @Override
  public RubyArray<E> shuffleEx() {
    Collections.shuffle(list);
    return this;
  }

  @Override
  public E slice(int index) {
    return at(index);
  }

  @Override
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
    return new RubyArrayList(slicedList);
  }

  @Override
  public E sliceEx(int index) {
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

  @Override
  public RubyArray<E> sliceEx(int index, int length) {
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
    return new RubyArrayList(slicedList);
  }

  @Override
  public int length() {
    return list.size();
  }

  @Override
  public RubyArray<E> uniq() {
    List<E> uniqList = newArrayList();
    for (E item : list) {
      if (!uniqList.contains(item)) {
        uniqList.add(item);
      }
    }
    return new RubyArrayList(uniqList);
  }

  @Override
  public RubyArray<E> uniqEx() {
    RubyArray<E> uniqList = uniq();
    list.clear();
    list.addAll(uniqList);
    return this;
  }

  @Override
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
    return new RubyArrayList(uniqList);
  }

  @Override
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
    return new RubyArrayList(unionList);
  }

  @Override
  public RubyArray<E> unshift(E item) {
    list.add(0, item);
    return this;
  }

  @Override
  public RubyArray<E> valuesAt(int... indice) {
    List<E> values = newArrayList();
    for (int index : indice) {
      values.add(this.at(index));
    }
    return new RubyArrayList(values);
  }

  @Override
  public RubyArray<RubyArray<E>> zip(RubyArray<E>... others) {
    RubyArray<RubyArray<E>> zippedRubyArray = new RubyArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      RubyArray<E> zip = new RubyArrayList();
      zip.add(this.at(i));
      for (int j = 0; j < others.length; j++) {
        zip.add(others[j].at(i));
      }
      zippedRubyArray.add(zip);
    }
    return zippedRubyArray;
  }

  @Override
  public void zip(RubyArray<RubyArray<E>> others, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> zippedRubyArray = zip(others.toArray(new RubyArrayList[others.length()]));
    for (RubyArray<E> item : zippedRubyArray) {
      block.yield(item);
    }
  }

  // Ruby Enumerable methods
  @Override
  public boolean hasAll() {
    return RubyEnumerable.hasAll(list);
  }

  @Override
  public boolean hasAll(BooleanBlock block) {
    return RubyEnumerable.hasAll(list, block);
  }

  @Override
  public <K> RubyArray<Entry<K, RubyArray<E>>> chunk(ItemTransformBlock<E, K> block) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : list) {
      K key = block.yield(item);
      multimap.put(key, item);
    }
    RubyArray<Entry<K, RubyArray<E>>> rubyArray = new RubyArrayList();
    for (K key : multimap.keySet()) {
      rubyArray.add(new SimpleEntry<>(key, new RubyArrayList(multimap.get(key))));
    }
    return rubyArray;
  }

  @Override
  public <S> RubyArray<E> collect(ItemTransformBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.collect(list, block));
  }

  @Override
  public <S> RubyArray<S> collectConcat(ItemToListBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.collectConcat(list, block));
  }

  @Override
  public int count() {
    return RubyEnumerable.count(list);
  }

  @Override
  public int count(BooleanBlock<E> block) {
    return RubyEnumerable.count(list, block);
  }

  @Override
  public void cycle(ItemBlock<E> block) {
    RubyEnumerable.cycle(list, block);
  }

  @Override
  public void cycle(int cycles, ItemBlock<E> block) {
    RubyEnumerable.cycle(list, cycles, block);
  }

  @Override
  public E detect(E target) {
    return RubyEnumerable.detect(list, target);
  }

  @Override
  public E detect(BooleanBlock<E> block) {
    return RubyEnumerable.detect(list, block);
  }

  @Override
  public RubyArray<E> drop(int n) {
    return new RubyArrayList(RubyEnumerable.drop(list, n));
  }

  @Override
  public RubyArray<E> dropWhile(BooleanBlock block) {
    return new RubyArrayList(RubyEnumerable.dropWhile(list, block));
  }

  @Override
  public void eachCons(int n, ItemFromListBlock<E> block) {
    RubyEnumerable.eachCons(list, n, block);
  }

  @Override
  public void eachSlice(int n, ItemFromListBlock<E> block) {
    RubyEnumerable.eachSlice(list, n, block);
  }

  @Override
  public void eachWithIndex(ItemWithIndexBlock<E> vistor) {
    RubyEnumerable.eachWithIndex(list, vistor);
  }

  @Override
  public void eachWithObject(Object o, ItemWithObjectBlock<E> block) {
    RubyEnumerable.eachWithObject(list, o, block);
  }

  @Override
  public RubyArray<E> entries() {
    return new RubyArrayList(RubyEnumerable.entries(list));
  }

  @Override
  public E find(E target) {
    return RubyEnumerable.find(list, target);
  }

  @Override
  public E find(BooleanBlock<E> block) {
    return RubyEnumerable.find(list, block);
  }

  @Override
  public E first() {
    return RubyEnumerable.first(list);
  }

  @Override
  public RubyArray<E> first(int n) {
    return new RubyArrayList(RubyEnumerable.first(list, n));
  }

  @Override
  public Integer findIndex(E target) {
    return RubyEnumerable.findIndex(list, target);
  }

  @Override
  public Integer findIndex(BooleanBlock<E> block) {
    return RubyEnumerable.findIndex(list, block);
  }

  @Override
  public RubyArray<E> findAll(BooleanBlock<E> block) {
    return new RubyArrayList(RubyEnumerable.findAll(list, block));
  }

  @Override
  public <S> RubyArray<S> flatMap(ItemToListBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.flatMap(list, block));
  }

  @Override
  public RubyArray<E> grep(String regex) {
    return new RubyArrayList(RubyEnumerable.grep(list, regex));
  }

  @Override
  public <S> RubyArrayList<S> grep(String regex, ItemTransformBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.grep(list, regex, block));
  }

  @Override
  public <K> RubyHash<K, RubyArray<E>> groupBy(ItemTransformBlock<E, K> block) {
    Multimap<K, E> multimap = ArrayListMultimap.create();
    for (E item : list) {
      K key = block.yield(item);
      multimap.put(key, item);
    }
    RubyHash<K, RubyArray<E>> hash = new RubyLinkedHashMap();
    for (K key : multimap.keySet()) {
      hash.put(key, new RubyArrayList(multimap.get(key)));
    }
    return hash;
  }

  @Override
  public boolean include(E target) {
    return RubyEnumerable.include(list, target);
  }

  @Override
  public boolean hasMember(E target) {
    return RubyEnumerable.hasMember(list, target);
  }

  @Override
  public E inject(String methodName) {
    return RubyEnumerable.inject(list, methodName);
  }

  @Override
  public E inject(E init, String methodName) {
    return RubyEnumerable.inject(list, init, methodName);
  }

  @Override
  public E inject(InjectBlock<E> block) {
    return RubyEnumerable.inject(list, block);
  }

  @Override
  public <S> S inject(S init, InjectWithInitBlock<E, S> block) {
    return RubyEnumerable.inject(this, init, block);
  }

  @Override
  public <S> RubyArray<S> map(ItemTransformBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.map(list, block));
  }

  @Override
  public E max() {
    return sort().last();
  }

  @Override
  public E max(Comparator<? super E> comp) {
    return RubyEnumerable.max(list, comp);
  }

  @Override
  public <S> E maxBy(ItemTransformBlock<E, S> block) {
    return sortBy(block).last();
  }

  @Override
  public <S> E maxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.maxBy(list, comp, block);
  }

  @Override
  public E min() {
    return sort().first();
  }

  @Override
  public E min(Comparator<? super E> comp) {
    return RubyEnumerable.min(list, comp);
  }

  @Override
  public <S> E minBy(ItemTransformBlock<E, S> block) {
    return sortBy(block).first();
  }

  @Override
  public <S> E minBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return RubyEnumerable.minBy(list, comp, block);
  }

  @Override
  public RubyArray<E> minmax() {
    RubyArray<E> sorted = sort();
    return new RubyArrayList(sorted.first(), sorted.last());
  }

  @Override
  public RubyArray<E> minmax(Comparator<? super E> comp) {
    return new RubyArrayList(RubyEnumerable.minmax(list, comp));
  }

  @Override
  public <S> RubyArray<E> minmaxBy(ItemTransformBlock<E, S> block) {
    RubyArray<E> sorted = sortBy(block);
    return new RubyArrayList(sorted.first(), sorted.last());
  }

  @Override
  public <S> RubyArray<E> minmaxBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.minmaxBy(list, comp, block));
  }

  @Override
  public RubyArray<RubyArray<E>> partition(BooleanBlock<E> block) {
    RubyArray<E> trueList = new RubyArrayList();
    RubyArray<E> falseList = new RubyArrayList();
    for (E item : list) {
      if (block.yield(item)) {
        trueList.add(item);
      } else {
        falseList.add(item);
      }
    }
    RubyArray<RubyArray<E>> rubyArray = new RubyArrayList();
    rubyArray.add(trueList);
    rubyArray.add(falseList);
    return rubyArray;
  }

  @Override
  public RubyArray<RubyArray<E>> permutation() {
    RubyArray<RubyArray<E>> perms = new RubyArrayList();
    PermutationGenerator<E> pg = new PermutationGenerator<>(list);
    while (pg.hasMore()) {
      perms.add(new RubyArrayList<>(pg.nextPermutationAsList()));
    }
    return perms;
  }

  @Override
  public RubyArray<RubyArray<E>> permutation(int n) {
    RubyArray<RubyArray<E>> perms = new RubyArrayList();
    if (n < 0) {
      return perms;
    } else if (n == 0) {
      perms.add(new RubyArrayList<E>());
      return perms;
    } else if (n > list.size()) {
      return perms;
    } else {
      RubyArray<RubyArray<E>> combs = combination(n);
      for (RubyArray<E> comb : combs) {
        PermutationGenerator<E> pg = new PermutationGenerator<>(comb);
        while (pg.hasMore()) {
          perms.add(new RubyArrayList<E>(pg.nextPermutationAsList()));
        }
      }
    }
    return perms.uniq();
  }

  @Override
  public RubyArray<RubyArray<E>> permutation(int n, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> perms = permutation(n);
    for (RubyArray<E> item : perms) {
      block.yield(item);
    }
    return perms;
  }

  @Override
  public RubyArray<RubyArray<E>> repeatedPermutation(int n) {
    RubyArray<RubyArray<E>> rp = new RubyArrayList();
    if (n < 0) {
      return rp;
    }
    if (n == 0) {
      return rp.push(new RubyArrayList());
    }
    int[] counter = new int[n];
    repeatedPermutationLoop(counter, 0, list.size() - 1, (count) -> {
      RubyArray<E> c = new RubyArrayList();
      for (int i = 0; i < count.length; i++) {
        c.push(list.get(count[i]));
      }
      rp.add(c);
    });
    return rp;
  }

  private void repeatedPermutationLoop(int[] counter, int start, int end, ItemBlock<int[]> block) {
    int[] endStatus = new int[counter.length];
    Arrays.fill(endStatus, end);
    do {
      block.yield(counter);
      increasePermutationLoopCounter(counter, start, end);
    } while (!Arrays.equals(counter, endStatus));
    block.yield(counter);
  }

  private void increasePermutationLoopCounter(int[] counter, int start, int end) {
    for (int i = counter.length - 1; i >= 0; i--) {
      if (counter[i] < end) {
        counter[i]++;
        return;
      } else if (i != 0
              && counter[i - 1] != end) {
        counter[i - 1]++;
        for (int j = i; j < counter.length; j++) {
          counter[j] = start;
        }
        return;
      }
    }
  }

  public RubyArray<RubyArray<E>> repeatedPermutation(int n, ItemBlock<RubyArray<E>> block) {
    RubyArray<RubyArray<E>> rp = new RubyArrayList();
    if (n < 0) {
      return rp;
    }
    if (n == 0) {
      rp.push(new RubyArrayList());
      block.yield(rp.first());
      return rp;
    }
    int[] counter = new int[n];
    repeatedCombinationLoop(counter, 0, list.size() - 1, (count) -> {
      RubyArray<E> c = new RubyArrayList();
      for (int i = 0; i < count.length; i++) {
        c.push(list.get(count[i]));
      }
      block.yield(c);
      rp.add(c);
    });
    return rp;
  }

  @Override
  public boolean hasNone() {
    return RubyEnumerable.hasNone(list);
  }

  @Override
  public boolean hasNone(BooleanBlock<E> block) {
    return RubyEnumerable.hasNone(list, block);
  }

  @Override
  public boolean hasOne() {
    return RubyEnumerable.hasOne(list);
  }

  @Override
  public boolean hasOne(BooleanBlock<E> block) {
    return RubyEnumerable.hasOne(list, block);
  }

  @Override
  public E reduce(String methodName) {
    return inject(methodName);
  }

  @Override
  public E reduce(E init, String methodName) {
    return inject(init, methodName);
  }

  @Override
  public E reduce(InjectBlock<E> block) {
    return inject(block);
  }

  @Override
  public <S> S reduce(S init, InjectWithInitBlock<E, S> block) {
    return inject(init, block);
  }

  @Override

  public RubyArray<E> reject(BooleanBlock block) {
    return new RubyArrayList(RubyEnumerable.reject(list, block));
  }

  @Override
  public void reverseEach(ItemBlock block) {
    RubyEnumerable.reverseEach(list, block);
  }

  @Override
  public RubyArray<E> select(BooleanBlock block) {
    return findAll(block);
  }

  @Override
  public RubyArray<E> sort() {
    Object[] array = list.toArray();
    Arrays.sort(array);
    return new RubyArrayList(array);
  }

  @Override
  public RubyArray<E> sort(Comparator<? super E> comp) {
    return new RubyArrayList(RubyEnumerable.sort(list, comp));
  }

  @Override
  public <S> RubyArray<E> sortBy(ItemTransformBlock<E, S> block) {
    Multimap<S, E> multimap = ArrayListMultimap.create();
    List<E> sortedList = newArrayList();
    for (E item : list) {
      multimap.put(block.yield(item), item);
    }
    Object[] keys = newArrayList(multimap.keySet()).toArray();
    Arrays.sort(keys);
    for (Object key : keys) {
      Collection<E> coll = multimap.get((S) key);
      Iterator<E> iter = coll.iterator();
      while (iter.hasNext()) {
        sortedList.add(iter.next());
      }
    }
    return new RubyArrayList(sortedList);
  }

  @Override
  public <S> RubyArray<E> sortBy(Comparator<? super S> comp, ItemTransformBlock<E, S> block) {
    return new RubyArrayList(RubyEnumerable.sortBy(list, comp, block));
  }

  @Override
  public RubyArray<RubyArray<E>> sliceBefore(String regex) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArrayList();
    Pattern pattern = Pattern.compile(regex);
    RubyArrayList<E> group = null;
    for (E item : list) {
      if (group == null) {
        group = new RubyArrayList();
        group.add(item);
      } else if (pattern.matcher(item.toString()).find()) {
        rubyArray.add(group);
        group = new RubyArrayList();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      rubyArray.add(group);
    }
    return rubyArray;
  }

  @Override
  public RubyArray<RubyArray<E>> sliceBefore(BooleanBlock block) {
    RubyArray<RubyArray<E>> rubyArray = new RubyArrayList();
    RubyArray<E> group = null;
    for (E item : list) {
      if (group == null) {
        group = new RubyArrayList();
        group.add(item);
      } else if (block.yield(item)) {
        rubyArray.add(group);
        group = new RubyArrayList();
        group.add(item);
      } else {
        group.add(item);
      }
    }
    if (group != null && !group.isEmpty()) {
      rubyArray.add(group);
    }
    return rubyArray;
  }

  @Override
  public RubyArray<E> take(int n) {
    return new RubyArrayList(RubyEnumerable.take(list, n));
  }

  @Override
  public RubyArray<E> takeWhile(BooleanBlock block) {
    return new RubyArrayList(RubyEnumerable.takeWhile(list, block));
  }

  @Override
  public RubyArray<E> toA() {
    return this;
  }

  @Override
  public RubyArray<E> toAry() {
    return this;
  }

  @Override
  public String toS() {
    return list.toString();
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
    return list.addAll(c);
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
