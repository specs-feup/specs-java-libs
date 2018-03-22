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

/**
 * 
 * @author Luis Reis
 *
 */
public class StringSlice implements CharSequence {
    public static final StringSlice EMPTY = new StringSlice("");

    protected final String internal;
    protected final int startIndex;
    protected final int endIndex;
    //
    // private final boolean trim;
    // private final boolean reverse;
    // private final Predicate<Character> separator;

    // public static class NextResult {
    // private final StringSlice modifiedSlice;
    // private final String word;
    //
    // public NextResult(StringSlice modifiedSlice, String word) {
    // this.modifiedSlice = modifiedSlice;
    // this.word = word;
    // }
    //
    // public StringSlice getModifiedSlice() {
    // return modifiedSlice;
    // }
    //
    // public String getWord() {
    // return word;
    // }
    // }

    /**
     * Builds a new StringSlice, with 'whitespace' as the default separator.
     * 
     * @param value
     */
    public StringSlice(String value) {
        // this(value, 0, value == null ? -1 : value.length(), false, false, aChar -> Character.isWhitespace(aChar));
        this(value, 0, value == null ? -1 : value.length());
        // if (value == null) {
        // throw new IllegalArgumentException("value must not be null");
        // }
        //
        // this.internal = value;
        // this.startIndex = 0;
        // this.endIndex = value.length();
        // this.trim = false;
        // this.reverse = false;
        // this.separator = aChar -> Character.isWhitespace(aChar);
    }

    /**
     * Helper constructor which accepts a StringSlice.
     * 
     * @param value
     */
    public StringSlice(StringSlice value) {
        this(value.toString());
    }

    // public StringSlice(String value, int start, int end) {
    //
    // }

    public StringSlice(String value, int start, int end) {
        // this(value, start, end, false, false, aChar -> Character.isWhitespace(aChar));
        // }
        //
        // public StringSlice(String value, int start, int end, boolean trim, boolean reverse,
        // Predicate<Character> separator) {

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

        // this.trim = trim;
        // this.reverse = reverse;
        // this.separator = separator;
    }
    //
    // public StringSlice setTrim(boolean trim) {
    // return new StringSlice(internal, startIndex, endIndex, trim, reverse, separator);
    // }
    //
    // public StringSlice setReverse(boolean reverse) {
    // return new StringSlice(internal, startIndex, endIndex, trim, reverse, separator);
    // }
    //
    // public StringSlice setSeparator(Predicate<Character> separator) {
    // return new StringSlice(internal, startIndex, endIndex, trim, reverse, separator);
    // }

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
        // return new StringSlice(this.internal, this.startIndex + start, this.startIndex + end, trim, reverse,
        // separator);
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

    /**
     * @deprecated
     * @return
     */
    // @Deprecated
    // public int indexOfFirstWhiteSpace() {
    // return SpecsStrings.indexOfFirstWhiteSpace(toString());
    // }

    @Deprecated
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
     * @deprecated use object StringParser
     * @return the substring until the first whitespace, or the complete string if no white space is found
     */
    // @Deprecated
    // public StringSlice getFirstWord() {
    // int spaceIndex = indexOf(' ');
    // if (spaceIndex == -1) {
    // return this;
    // }
    //
    // return substring(0, spaceIndex);
    // }

    /**
     * 
     * @param target
     * @param reverse
     * @return an index relative to the internal String
     */
    /*
    private int indexOfInternal(Predicate<Character> target, boolean reverse) {
        // Using internals
        // Test reverse order
        if (reverse) {
            for (int i = endIndex - 1; i >= startIndex; i--) {
                if (target.test(internal.charAt(i))) {
                    return i;
                }
            }
        }
        // Test original order
        else {
            for (int i = startIndex; i < endIndex; i++) {
                if (target.test(internal.charAt(i))) {
                    return i;
                }
            }
        }
    
        // Using class methods
        // // Test reverse order
        // if (reverse) {
        // for (int i = length() - 1; i >= 0; i--) {
        // if (target.test(charAtUnchecked(i))) {
        // return i;
        // }
        // }
        // }
        // // Test original order
        // else {
        // for (int i = 0; i < length(); i++) {
        // if (target.test(charAtUnchecked(i))) {
        // return i;
        // }
        // }
        // }
    
        return -1;
    }
    */
    /**
     * Parses a word according to the StringSlice defined rules (i.e., trim, reverse and separator).
     * <p>
     * If no separator is found, the result contains the remaining string.
     * 
     * @return
     */
    /*
    public NextResult next() {
        int internalSeparatorIndex = indexOfInternal(separator, reverse);
    
        NextResult result = reverse ? nextReverse(internalSeparatorIndex) : nextRegular(internalSeparatorIndex);
    
        if (trim) {
            return new NextResult(result.getModifiedSlice().trim(), result.getWord().trim());
        }
    
        return result;
    }
    */
    /*
    private NextResult nextRegular(int internalSeparatorIndex) {
    
        if (internalSeparatorIndex == -1) {
            internalSeparatorIndex = endIndex;
        }
    
        String word = internal.substring(startIndex, internalSeparatorIndex);
    
        // // Trim word
        // if (trim) {
        // word = word.trim();
        // }
    
        int internalSliceStartIndex = internalSeparatorIndex + 1;
    
        // If bigger than endIndex, return empty StringSlice
        if (internalSliceStartIndex > endIndex) {
            return new NextResult(substring(length()), word);
        }
    
        StringSlice modifiedSlice = new StringSlice(internal, internalSliceStartIndex, endIndex, trim, reverse,
                separator);
    
        // // Trim modified slice
        // if (trim) {
        // modifiedSlice = modifiedSlice.trim();
        // }
    
        return new NextResult(modifiedSlice, word);
        // StringSlice modifiedSlice;
        // if (reverse) {
        // modifiedSlice = substring(0, index + 1);
        // } else {
        // int modifiedStart = index + 1;
        // modifiedStart = modifiedStart >
        // modifiedSlice = substring(index + 1);
        // }
        //
        // String element = string.substring(0, endIndex).toString();
        //
        // // Update slice
        // string = string.substring(endIndex);
    
        // if (internalSeparatorIndex == -1) {
        // if (reverse) {
        // internalSeparatorIndex = startIndex - 1;
        // } else {
        // internalSeparatorIndex = endIndex;
        // }
        // }
    
        // int endIndex = string.indexOfFirstWhiteSpace();
        // if (endIndex == -1) {
        // endIndex = string.length();
        // }
    
        // String word;
        // if (reverse) {
        // word = internal.substring(internalSeparatorIndex + 1, endIndex);
        // } else {
        // word = internal.substring(startIndex, internalSeparatorIndex);
        // }
        //
        // if (trim) {
        // word = word.trim();
        // }
        //
        // StringSlice modifiedSlice;
        // if (reverse) {
        // modifiedSlice = substring(0, index + 1);
        // } else {
        // int modifiedStart = index + 1;
        // modifiedStart = modifiedStart >
        // modifiedSlice = substring(index + 1);
        // }
        //
        // String element = string.substring(0, endIndex).toString();
        //
        // // Update slice
        // string = string.substring(endIndex);
    
        // return null;
    }
    */
    /*
    private NextResult nextReverse(int internalSeparatorIndex) {
        if (internalSeparatorIndex == -1) {
            internalSeparatorIndex = startIndex - 1;
        }
    
        String word = internal.substring(internalSeparatorIndex + 1, endIndex);
    
        int internalSliceEndIndex = internalSeparatorIndex;
    
        if (internalSliceEndIndex < startIndex) {
            StringSlice modifiedSlice = new StringSlice("", 0, 0, trim, reverse, separator);
            return new NextResult(modifiedSlice, word);
        }
    
        StringSlice modifiedSlice = new StringSlice(internal, startIndex, internalSliceEndIndex, trim, reverse,
                separator);
    
        return new NextResult(modifiedSlice, word);
    }
    */

    /**
     * 
     * @return an empty StringSlice.
     */
    public StringSlice clear() {
        // return new StringSlice("", 0, 0, trim, reverse, separator);
        return new StringSlice("", 0, 0);
    }
    /*
    int endIndex = string.indexOfFirstWhiteSpace();if(endIndex==-1)
    {
        endIndex = string.length();
    }
    
    String element = string.substring(0, endIndex).toString();
    
    // Update slice
    string=string.substring(endIndex);
    */

    /**
     * Changes the current string to match the given StringSlice, while keeping the other settings (e.g., trim,
     * reverse).
     * 
     * @param modifiedString
     * @return
     */
    // public StringSlice setString(StringSlice stringSlice) {
    // return new StringSlice(stringSlice.internal, stringSlice.startIndex, stringSlice.endIndex);
    // // return new StringSlice(stringSlice.internal, stringSlice.startIndex, stringSlice.endIndex, trim, reverse,
    // // separator);
    // }

    // Does not make sense, it is an immutable class
    // public StringSlice copy() {
    // return new StringSlice(internal, startIndex, endIndex);
    // // return new StringSlice(stringSlice.internal, stringSlice.startIndex, stringSlice.endIndex, trim, reverse,
    // // separator);
    // }
}
