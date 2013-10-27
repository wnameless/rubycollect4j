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
package net.sf.rubycollect4j;

import net.sf.rubycollect4j.iter.ChunkIterableTest;
import net.sf.rubycollect4j.iter.ChunkIteratorTest;
import net.sf.rubycollect4j.iter.CombinationIterableTest;
import net.sf.rubycollect4j.iter.CombinationIteratorTest;
import net.sf.rubycollect4j.iter.ComparableEntryIterableTest;
import net.sf.rubycollect4j.iter.ComparableEntryIteratorTest;
import net.sf.rubycollect4j.iter.CycleIterableTest;
import net.sf.rubycollect4j.iter.CycleIteratorTest;
import net.sf.rubycollect4j.iter.DropIterableTest;
import net.sf.rubycollect4j.iter.DropIteratorTest;
import net.sf.rubycollect4j.iter.DropWhileIterableTest;
import net.sf.rubycollect4j.iter.DropWhileIteratorTest;
import net.sf.rubycollect4j.iter.EachConsIterableTest;
import net.sf.rubycollect4j.iter.EachConsIteratorTest;
import net.sf.rubycollect4j.iter.EachIndexIterableTest;
import net.sf.rubycollect4j.iter.EachIndexIteratorTest;
import net.sf.rubycollect4j.iter.EachLineIterableTest;
import net.sf.rubycollect4j.iter.EachLineIteratorTest;
import net.sf.rubycollect4j.iter.EachSliceIterableTest;
import net.sf.rubycollect4j.iter.EachSliceIteratorTest;
import net.sf.rubycollect4j.iter.EachWithIndexIterableTest;
import net.sf.rubycollect4j.iter.EachWithIndexIteratorTest;
import net.sf.rubycollect4j.iter.EachWithObjectIterableTest;
import net.sf.rubycollect4j.iter.EachWithObjectIteratorTest;
import net.sf.rubycollect4j.iter.FindAllIterableTest;
import net.sf.rubycollect4j.iter.FindAllIteratorTest;
import net.sf.rubycollect4j.iter.FlattenIterableTest;
import net.sf.rubycollect4j.iter.FlattenIteratorTest;
import net.sf.rubycollect4j.iter.GrepIterableTest;
import net.sf.rubycollect4j.iter.GrepIteratorTest;
import net.sf.rubycollect4j.iter.OrderedEntrySetIterableTest;
import net.sf.rubycollect4j.iter.OrderedEntrySetIteratorTest;
import net.sf.rubycollect4j.iter.PermutationIterableTest;
import net.sf.rubycollect4j.iter.PermutationIteratorTest;
import net.sf.rubycollect4j.iter.ProductIterableTest;
import net.sf.rubycollect4j.iter.ProductIteratorTest;
import net.sf.rubycollect4j.iter.RangeIterableTest;
import net.sf.rubycollect4j.iter.RangeIteratorTest;
import net.sf.rubycollect4j.iter.RejectIterableTest;
import net.sf.rubycollect4j.iter.RejectIteratorTest;
import net.sf.rubycollect4j.iter.RepeatedCombinationIterableTest;
import net.sf.rubycollect4j.iter.RepeatedCombinationIteratorTest;
import net.sf.rubycollect4j.iter.RepeatedPermutationIterableTest;
import net.sf.rubycollect4j.iter.RepeatedPermutationIteratorTest;
import net.sf.rubycollect4j.iter.ReverseEachIterableTest;
import net.sf.rubycollect4j.iter.ReverseEachIteratorTest;
import net.sf.rubycollect4j.iter.SliceBeforeIterableTest;
import net.sf.rubycollect4j.iter.SliceBeforeIteratorTest;
import net.sf.rubycollect4j.iter.StepIterableTest;
import net.sf.rubycollect4j.iter.StepIteratorTest;
import net.sf.rubycollect4j.iter.TakeIterableTest;
import net.sf.rubycollect4j.iter.TakeIteratorTest;
import net.sf.rubycollect4j.iter.TakeWhileIterableTest;
import net.sf.rubycollect4j.iter.TakeWhileIteratorTest;
import net.sf.rubycollect4j.iter.TransformIterableTest;
import net.sf.rubycollect4j.iter.TransformIteratorTest;
import net.sf.rubycollect4j.iter.ZipIterableTest;
import net.sf.rubycollect4j.iter.ZipIteratorTest;
import net.sf.rubycollect4j.packer.ByteUtilTest;
import net.sf.rubycollect4j.packer.DirectiveTest;
import net.sf.rubycollect4j.packer.PackerTest;
import net.sf.rubycollect4j.succ.DateSuccessorTest;
import net.sf.rubycollect4j.succ.DoubleSuccessorTest;
import net.sf.rubycollect4j.succ.IntegerSuccessorTest;
import net.sf.rubycollect4j.succ.LongSuccessorTest;
import net.sf.rubycollect4j.succ.StringSuccessorTest;
import net.sf.rubycollect4j.util.ComparableEntryTest;
import net.sf.rubycollect4j.util.LinkedIdentityMapTest;
import net.sf.rubycollect4j.util.ListSetTest;
import net.sf.rubycollect4j.util.PeekingIteratorTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ RubyArrayTest.class, RubyCollectionsTest.class,
    RubyDateTest.class, RubyDirTest.class, RubyEnumerableTest.class,
    RubyEnumeratorTest.class, RubyFileTest.class, RubyHashTest.class,
    RubyIOTest.class, RubyKernelTest.class, RubyLazyEnumeratorTest.class,
    RubyObjectTest.class, RubyRangeTest.class, DateSuccessorTest.class,
    DoubleSuccessorTest.class, IntegerSuccessorTest.class,
    LongSuccessorTest.class, StringSuccessorTest.class,
    LinkedIdentityMapTest.class, ListSetTest.class, ChunkIterableTest.class,
    ChunkIteratorTest.class, CombinationIterableTest.class,
    CombinationIteratorTest.class, ComparableEntryIterableTest.class,
    ComparableEntryIteratorTest.class, CycleIterableTest.class,
    CycleIteratorTest.class, DropIterableTest.class, DropIteratorTest.class,
    DropWhileIterableTest.class, DropWhileIteratorTest.class,
    EachConsIterableTest.class, EachConsIteratorTest.class,
    EachIndexIterableTest.class, EachIndexIteratorTest.class,
    EachLineIterableTest.class, EachLineIteratorTest.class,
    EachSliceIterableTest.class, EachSliceIteratorTest.class,
    EachWithIndexIterableTest.class, EachWithIndexIteratorTest.class,
    EachWithObjectIterableTest.class, EachWithObjectIteratorTest.class,
    FindAllIterableTest.class, FindAllIteratorTest.class,
    FlattenIterableTest.class, FlattenIteratorTest.class,
    GrepIterableTest.class, GrepIteratorTest.class,
    OrderedEntrySetIterableTest.class, OrderedEntrySetIteratorTest.class,
    PermutationIterableTest.class, PermutationIteratorTest.class,
    ProductIterableTest.class, ProductIteratorTest.class,
    RangeIterableTest.class, RangeIteratorTest.class, RejectIterableTest.class,
    RejectIteratorTest.class, RepeatedCombinationIterableTest.class,
    RepeatedCombinationIteratorTest.class,
    RepeatedPermutationIterableTest.class,
    RepeatedPermutationIteratorTest.class, ReverseEachIterableTest.class,
    ReverseEachIteratorTest.class, SliceBeforeIterableTest.class,
    SliceBeforeIteratorTest.class, StepIterableTest.class,
    StepIteratorTest.class, TakeIterableTest.class, TakeIteratorTest.class,
    TakeWhileIterableTest.class, TakeWhileIteratorTest.class,
    TransformIterableTest.class, TransformIteratorTest.class,
    ZipIterableTest.class, ZipIteratorTest.class, ByteUtilTest.class,
    DirectiveTest.class, PackerTest.class, PeekingIteratorTest.class,
    ComparableEntryTest.class })
public class RubyCollect4JTests {}
