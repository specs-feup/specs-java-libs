/**
 * Copyright 2013 SPeCS Research Group.
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

package tdrc.utils;

/**
 * @author Tiago
 *
 */
public class Pair<K, V> {

	private K left;
	private V right;

	public static <K, V> Pair<K, V> newInstance(K left, V right) {

		return new Pair<>(left, right);
	}

	public Pair() {
		this.left = null;
		this.right = null;
	}

	public Pair(K left, V right) {
		setLeft(left);
		setRight(right);
	}

	@Override
	public String toString() {
		return "(" + this.left + ", " + this.right + ")";
	}

	/**
	 * @return the left
	 */
	public K getLeft() {
		return this.left;
	}

	/**
	 * @param left
	 *            the left to set
	 */
	public void setLeft(K left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public V getRight() {
		return this.right;
	}

	/**
	 * @param right
	 *            the right to set
	 */
	public void setRight(V right) {
		this.right = right;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.left == null) ? 0 : this.left.hashCode());
		result = prime * result + ((this.right == null) ? 0 : this.right.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Pair<?, ?> other = (Pair<?, ?>) obj;
		if (this.left == null) {
			if (other.left != null) {
				return false;
			}
		} else if (!this.left.equals(other.left)) {
			return false;
		}
		if (this.right == null) {
			if (other.right != null) {
				return false;
			}
		} else if (!this.right.equals(other.right)) {
			return false;
		}
		return true;
	}

	/*
	 * @Override public boolean equals(Object obj) { if (!(obj instanceof Pair))
	 * return false; Pair<?, ?> otherPair = (Pair<?, ?>) obj; return
	 * this.getLeft().equals(otherPair.getLeft()) &&
	 * this.getRight().equals(otherPair.getRight()); }
	 */

}
