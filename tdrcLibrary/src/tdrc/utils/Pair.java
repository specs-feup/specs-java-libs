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
 * Represents a pair of values.
 *
 * @param <K> the type of the first value
 * @param <V> the type of the second value
 */
public class Pair<K, V> {

	private K left;
	private V right;

	/**
	 * Creates a new instance of Pair with the specified values.
	 *
	 * @param <K> the type of the first value
	 * @param <V> the type of the second value
	 * @param left the first value
	 * @param right the second value
	 * @return a new Pair instance
	 */
	public static <K, V> Pair<K, V> newInstance(K left, V right) {
		return new Pair<>(left, right);
	}

	/**
	 * Default constructor. Initializes the pair with null values.
	 */
	public Pair() {
		this.left = null;
		this.right = null;
	}

	/**
	 * Constructor that initializes the pair with the specified values.
	 *
	 * @param left the first value
	 * @param right the second value
	 */
	public Pair(K left, V right) {
		setLeft(left);
		setRight(right);
	}

	/**
	 * Returns a string representation of the pair.
	 *
	 * @return a string in the format "(left, right)"
	 */
	@Override
	public String toString() {
		return "(" + this.left + ", " + this.right + ")";
	}

	/**
	 * Gets the first value of the pair.
	 *
	 * @return the first value
	 */
	public K getLeft() {
		return this.left;
	}

	/**
	 * Sets the first value of the pair.
	 *
	 * @param left the value to set
	 */
	public void setLeft(K left) {
		this.left = left;
	}

	/**
	 * Gets the second value of the pair.
	 *
	 * @return the second value
	 */
	public V getRight() {
		return this.right;
	}

	/**
	 * Sets the second value of the pair.
	 *
	 * @param right the value to set
	 */
	public void setRight(V right) {
		this.right = right;
	}

	/**
	 * Computes the hash code for the pair.
	 *
	 * @return the hash code
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.left == null) ? 0 : this.left.hashCode());
		result = prime * result + ((this.right == null) ? 0 : this.right.hashCode());
		return result;
	}

	/**
	 * Checks if this pair is equal to another object.
	 *
	 * @param obj the object to compare
	 * @return true if the object is a pair with equal values, false otherwise
	 */
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
}
