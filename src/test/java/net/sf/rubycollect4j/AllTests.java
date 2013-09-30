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
import net.sf.rubycollect4j.iter.CycleIterableTest;
import net.sf.rubycollect4j.iter.CycleIteratorTest;
import net.sf.rubycollect4j.iter.EachConsIterableTest;
import net.sf.rubycollect4j.iter.EachConsIteratorTest;
import net.sf.rubycollect4j.iter.OrderedEntrySetIterableTest;
import net.sf.rubycollect4j.iter.OrderedEntrySetIteratorTest;
import net.sf.rubycollect4j.packer.ByteUtilTest;
import net.sf.rubycollect4j.packer.DirectiveTest;
import net.sf.rubycollect4j.packer.PackerTest;
import net.sf.rubycollect4j.succ.DateSuccessorTest;
import net.sf.rubycollect4j.succ.DoubleSuccessorTest;
import net.sf.rubycollect4j.succ.IntegerSuccessorTest;
import net.sf.rubycollect4j.succ.LongSuccessorTest;
import net.sf.rubycollect4j.succ.StringSuccessorTest;
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
    RubyIOTest.class, RubyKernelTest.class, RubyRangeTest.class,
    DateSuccessorTest.class, DoubleSuccessorTest.class,
    IntegerSuccessorTest.class, LongSuccessorTest.class,
    StringSuccessorTest.class, LinkedIdentityMapTest.class, ListSetTest.class,
    ChunkIterableTest.class, ChunkIteratorTest.class,
    CombinationIterableTest.class, CombinationIteratorTest.class,
    CycleIterableTest.class, CycleIteratorTest.class,
    EachConsIterableTest.class, EachConsIteratorTest.class,
    OrderedEntrySetIterableTest.class, OrderedEntrySetIteratorTest.class,
    ByteUtilTest.class, DirectiveTest.class, PackerTest.class,
    PeekingIteratorTest.class })
public class AllTests {}
