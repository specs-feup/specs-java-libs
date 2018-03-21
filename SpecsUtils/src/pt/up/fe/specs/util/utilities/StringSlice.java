/**
 * Copyright 2015 SPeCS Research Group.
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

package pt.up.fe.specs.util.utilities;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * 
 * @author Luis Reis
 *
 */
public class StringSlice implements CharSequence {
    public static final StringSlice EMPTY = new StringSlice("");

    private final String internal;
    private final int startIndex;
    private final int endIndex;

    public StringSlice(String value) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }

        this.internal = value;
        this.startIndex = 0;
        this.endIndex = value.length();
    }

    /**
     * Helper constructor which accepts a StringSlice.
     * 
     * @param value
     */
    public StringSlice(StringSlice value) {
        this(value.toString());
    }

    public StringSlice(String value, int start, int end) {
        if (value == null) {
            throw new IllegalArgumentException("value must not be null");
        }
        if (start < 0 || start > value.length()) {
            throw new IndexOutOfBoundsException("start");
        }
        if (end < start || end > value.length()) {
            throw new IndexOutOfBoundsException("end");
        }

        this.internal = value;
        this.startIndex = start;
        this.endIndex = end;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index + this.startIndex >= this.endIndex) {
            throw new IndexOutOfBoundsException();
        }

        return charAtUnchecked(index);
        // return this.internal.charAt(index + this.startIndex);
    }

    private char charAtUnchecked(int index) {
        return this.internal.charAt(index + this.startIndex);
    }

    public char getFirstChar() {
        return this.internal.charAt(this.startIndex);
    }

    @Override
    public StringSlice subSequence(int start, int end) {
        if (start < 0 || start + this.startIndex > this.endIndex) {
            throw new IndexOutOfBoundsException("start");
        }
        if (end < start || end + this.startIndex > this.endIndex) {
            throw new IndexOutOfBoundsException("end");
        }

        return new StringSlice(this.internal, this.startIndex + start, this.startIndex + end);
    }

    public StringSlice substring(int start) {
        if (start < 0 || start > length()) {
            throw new IndexOutOfBoundsException();
        }

        return subSequence(start, length());
    }

    public StringSlice substring(int start, int end) {
        return subSequence(start, end);
    }

    @Override
    public int length() {
        return this.endIndex - this.startIndex;
    }

    public boolean isEmpty() {
        return length() == 0;
    }

    public boolean startsWith(String string) {
        if (string == null) {
            throw new IllegalArgumentException("string");
        }

        if (length() < string.length()) {
            return false;
        }

        for (int i = 0; i < string.length(); ++i) {
            if (charAt(i) != string.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    public boolean endsWith(String string) {
        if (string == null) {
            throw new IllegalArgumentException("string");
        }

        if (length() < string.length()) {
            return false;
        }

        for (int i = 0; i < string.length(); ++i) {
            int endIndex = string.length() - i - 1;
            int sliceEndIndex = length() - i - 1;
            if (charAt(sliceEndIndex) != string.charAt(endIndex)) {
                return false;
            }
        }

        return true;
    }

    public StringSlice trim() {
        int start, end;

        for (start = 0; start < length(); ++start) {
            if (!Character.isWhitespace(charAt(start))) {
                break;
            }
        }
        for (end = length(); end > start; --end) {
            if (!Character.isWhitespace(charAt(end - 1))) {
                break;
            }
        }

        return subSequence(start, end);
    }

    @Override
    public String toString() {
        return this.internal.substring(this.startIndex, this.endIndex);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }

        if (other instanceof String) {
            SpecsLogs.msgWarn("Using StringSlice.equals(String). Use equalsString instead.");
        }

        if (!(other instanceof StringSlice)) {
            return false;
        }

        StringSlice otherSlice = (StringSlice) other;
        return equals(otherSlice);
    }

    public boolean equalsString(String other) {
        return equals(toSlice(other));
    }

    public boolean equals(StringSlice other) {
        if (other == null) {
            return false;
        }

        if (length() != other.length()) {
            return false;
        }

        for (int i = 0; i < length(); ++i) {
            if (charAt(i) != other.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    public static StringSlice toSlice(String string) {
        if (string == null) {
            return null;
        }
        return new StringSlice(string);
    }

    public static String toString(StringSlice slice) {
        if (slice == null) {
            return null;
        }
        return slice.toString();
    }

    public int indexOf(char aChar) {
        return indexOf(aChar, 0);
    }

    public int indexOf(char aChar, int fromIndex) {
        // If index is outside string size, return -1
        if (fromIndex < 0 || fromIndex >= length()) {
            return -1;
        }

        // From this point on, we know that fromIndex is inside bounds
        for (int i = fromIndex; i < length(); i++) {
            if (charAtUnchecked(i) == aChar) {
                return i;
            }
        }

        // Could not find character
        return -1;
    }

    public int indexOfFirstWhiteSpace() {
        return SpecsStrings.indexOfFirstWhiteSpace(toString());
    }

    public int lastIndexOf(char aChar) {

        // Look for character, starting from the end
        for (int i = length() - 1; i >= 0; i--) {
            if (charAtUnchecked(i) == aChar) {
                return i;
            }
        }

        // Could not find character
        return -1;
    }

    /**
     * 
     * @return the substring until the first whitespace, or the complete string if no white space is found
     */
    public StringSlice getFirstWord() {
        int spaceIndex = indexOf(' ');
        if (spaceIndex == -1) {
            return this;
        }

        return substring(0, spaceIndex);
    }
}
