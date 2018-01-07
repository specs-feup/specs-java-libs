/*
 * Copyright 2013 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */
package org.specs.generators.java.utils;

import java.util.ArrayList;
import java.util.Collection;

public class UniqueList<E> extends ArrayList<E> {
	/**
	 * Auto-Generated serial
	 */
	private static final long serialVersionUID = 8776711618197815102L;

	@Override
	public boolean add(E arg0) {
		if (!contains(arg0)) {
			return super.add(arg0);
		}
		return false;
	}

	@Override
	public void add(int index, E element) {
		if (!contains(element)) {
			super.add(index, element);
		}
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (final E element : c) {
			add(element);
		}
		return true;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		for (final E element : c) {
			if (!contains(element)) {
				add(index, element);
				index++;
			}
		}
		return true;
	}

}
