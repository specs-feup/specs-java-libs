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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class StringSplitter {

    private StringSliceWithSplit currentString;

    public StringSplitter(String string) {
        this(new StringSliceWithSplit(string));
    }

    public StringSplitter(StringSliceWithSplit string) {
        this.currentString = string;
    }

    @Override
    public String toString() {
        return currentString.toString();
    }

    public boolean isEmpty() {
        return currentString.isEmpty();
    }

    /**
     * Internal method that does the heavy work.
     *
     */
    private <T> Optional<T> check(SplitRule<T> rule, Predicate<T> predicate, boolean updateString) {
        SplitResult<T> result = rule.apply(currentString);

        // Check if there was a match
        if (result == null) {
            return Optional.empty();
        }

        // Test predicate
        if (!predicate.test(result.value())) {
            return Optional.empty();
        }

        // Return if string should not be updated
        if (!updateString) {
            return Optional.of(result.value());
        }

        // Update string
        currentString = result.modifiedSlice();

        return Optional.of(result.value());
    }

    /**
     * Similar to {@link StringSplitter#parseTry(SplitRule)}, but throws exception
     * if the rule does not match.
     *
     */
    public <T> T parse(SplitRule<T> rule) {
        return parseTry(rule)
                .orElseThrow(() -> new RuntimeException(
                        "Could not apply parsing rule over the string '" + currentString + "'"));
    }

    public <T> List<T> parse(SplitRule<T> rule, int numElements) {
        List<T> elements = new ArrayList<>();

        for (int i = 0; i < numElements; i++) {
            try {
                elements.add(parse(rule));
            } catch (Exception e) {
                throw new RuntimeException("Tried to parse " + numElements + " elements, found " + i, e);
            }

        }

        return elements;
    }

    /**
     * Applies the rule over the current string. If the rule matches, returns the
     * match and consumes the corresponding string. Otherwise, returns an empty
     * Optional and leaves the current string unchanged.
     *
     */
    public <T> Optional<T> parseTry(SplitRule<T> rule) {
        // Use check with a predicate that always returns true
        return parseIf(rule, result -> true);
    }

    /**
     * Applies the given rule, and if it matches, checks if the results passes the
     * predicate. The current string is only consumed if both the rule and the
     * predicate match.
     *
     */
    public <T> Optional<T> parseIf(SplitRule<T> rule, Predicate<T> predicate) {
        return check(rule, predicate, true);
    }

    /**
     * Applies the rule over the current string, but does not consume the string
     * even if the rule matches.
     *
     */
    public <T> Optional<T> peek(SplitRule<T> rule) {
        return peekIf(rule, result -> true);
    }

    /**
     * Overload that accepts a Predicate.
     *
     */
    public <T> Optional<T> peekIf(SplitRule<T> rule, Predicate<T> predicate) {
        return check(rule, predicate, false);
    }

    /**
     * Similar to {@link StringSplitter#parseIf(SplitRule, Predicate)}, but discards
     * the result and returns if the value is present or not, consuming the
     * corresponding string.
     *
     */
    public <T> boolean check(SplitRule<T> rule, Predicate<T> predicate) {
        return parseIf(rule, predicate).isPresent();
    }

    /**
     * Similar to {@link StringSplitter#parseIf(SplitRule, Predicate)}, but discards
     * the result and throws exception if the value is not present, consuming the
     * corresponding string.
     *
     */
    public void consume(String string) {
        var success = check(StringSplitterRules::string, s -> s.equals(string));

        if (!success) {
            throw new RuntimeException("Could not consume '" + string + "' from '" + this + "'");
        }
    }

    public void setReverse(boolean reverse) {
        currentString = currentString.setReverse(reverse);
    }

    public void setSeparator(Predicate<Character> separator) {
        currentString = currentString.setSeparator(separator);
    }

    public void setTrim(boolean trim) {
        currentString = currentString.setTrim(trim);
    }

    public char peekChar() {
        return currentString.charAt(0);
    }

    public char nextChar() {
        var result = currentString.charAt(0);
        currentString = currentString.substring(1);
        return result;
    }
}
