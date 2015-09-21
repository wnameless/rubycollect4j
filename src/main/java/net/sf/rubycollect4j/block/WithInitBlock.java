/*
 *
 * Copyright 2013-2015 Wei-Ming Wu
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
 * An interface for lambda expression to yield an element with an initial value
 * and return a value based on the initial one.
 * 
 * @param <E>
 *          the type of the elements
 */
public interface WithInitBlock<E, I> {

  public I yield(I init, E item);

}
