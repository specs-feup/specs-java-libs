/**
 * Copyright 2018 SPeCS.
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

package pt.up.fe.specs.util.stringsplitter;

import java.util.function.Predicate;

import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * StringSlice with splitting capabilities.
 * 
 * @author JoaoBispo
 *
 */
public class StringSliceWithSplit extends StringSlice {

    private static final Predicate<Character> DEFAULT_SEPARATOR = aChar -> Character.isWhitespace(aChar);

    private final boolean trim;
    private final boolean reverse;
    private final Predicate<Character> separator;

    public StringSliceWithSplit(String string) {
        this(new StringSlice(string));
    }

    public StringSliceWithSplit(StringSlice stringSlice) {
        this(stringSlice, true, false, DEFAULT_SEPARATOR);
    }

    public StringSliceWithSplit(StringSlice stringSlice, boolean trim, boolean reverse,
            Predicate<Character> separator) {

        super(stringSlice);

        this.trim = trim;
        this.reverse = reverse;
        this.separator = separator;
    }

    public StringSliceWithSplit setTrim(boolean trim) {
        return new StringSliceWithSplit(this, trim, reverse, separator);
    }

    public StringSliceWithSplit setReverse(boolean reverse) {
        return new StringSliceWithSplit(this, trim, reverse, separator);
    }

    public StringSliceWithSplit setSeparator(Predicate<Character> separator) {
        return new StringSliceWithSplit(this, trim, reverse, separator);
    }

    /**
     * Parses a word according to the current rules (i.e., trim, reverse and separator).
     * <p>
     * If no separator is found, the result contains the remaining string.
     * 
     * @return
     */
    public SplitResult<String> split() {
        int internalSeparatorIndex = indexOfInternal(separator, reverse);

        SplitResult<String> result = reverse ? nextReverse(internalSeparatorIndex)
                : nextRegular(internalSeparatorIndex);

        if (trim) {
            return new SplitResult<>(result.getModifiedSlice().trim(), result.getValue().trim());
        }

        return result;
    }

    private SplitResult<String> nextRegular(int internalSeparatorIndex) {

        if (internalSeparatorIndex == -1) {
            internalSeparatorIndex = endIndex;
        }

        String word = internal.substring(startIndex, internalSeparatorIndex);

        int internalSliceStartIndex = internalSeparatorIndex + 1;

        // If bigger than endIndex, return empty StringSlice
        if (internalSliceStartIndex > endIndex) {
            return new SplitResult<>(substring(length()), word);
        }

        StringSliceWithSplit modifiedSlice = new StringSliceWithSplit(
                new StringSlice(internal, internalSliceStartIndex, endIndex),
                trim, reverse,
                separator);

        return new SplitResult<>(modifiedSlice, word);

    }

    private SplitResult<String> nextReverse(int internalSeparatorIndex) {
        if (internalSeparatorIndex == -1) {
            internalSeparatorIndex = startIndex - 1;
        }

        String word = internal.substring(internalSeparatorIndex + 1, endIndex);

        int internalSliceEndIndex = internalSeparatorIndex;

        if (internalSliceEndIndex < startIndex) {
            StringSliceWithSplit modifiedSlice = new StringSliceWithSplit(new StringSlice("", 0, 0), trim, reverse,
                    separator);
            return new SplitResult<>(modifiedSlice, word);
        }

        StringSliceWithSplit modifiedSlice = new StringSliceWithSplit(
                new StringSlice(internal, startIndex, internalSliceEndIndex),
                trim, reverse, separator);

        return new SplitResult<>(modifiedSlice, word);
    }

    /**
     * 
     * @param target
     * @param reverse
     * @return an index relative to the internal String
     */
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

    public StringSliceWithSplit set(StringSlice modifiedString) {
        return new StringSliceWithSplit(modifiedString, trim, reverse, separator);
    }

    @Override
    public StringSliceWithSplit trim() {
        return set(super.trim());
    }

    @Override
    public StringSliceWithSplit substring(int start) {
        return set(super.substring(start));
    }

    @Override
    public StringSliceWithSplit clear() {
        return set(super.clear());
    }

}
