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
package net.sf.rubycollect4j.block;

/**
 * 
 * An interface for lambda expression to yield the key and values of 2 Map
 * entries and return one of the values.
 * 
 * @param <K>
 *          the type of key elements
 * @param <V>
 *          the type of value elements
 */
public interface EntryMergeBlock<K, V> {

  public V yield(K key, V oldval, V newval);

}