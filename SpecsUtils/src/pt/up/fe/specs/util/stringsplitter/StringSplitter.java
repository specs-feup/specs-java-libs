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

import java.util.Optional;
import java.util.function.Predicate;

public class StringSplitter {

    private StringIterator currentString;

    public StringSplitter(String string) {
        this(new StringIterator(string));
    }

    public StringSplitter(StringIterator string) {
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
     * @param rule
     * @param predicate
     * @param updateString
     * @return
     */
    private <T> Optional<T> check(SplitRule<T> rule, Predicate<T> predicate, boolean updateString) {
        SplitResult<T> result = rule.apply(currentString);

        // Check if there was a match
        if (result == null) {
            return Optional.empty();
        }

        // Test predicate
        if (!predicate.test(result.getValue())) {
            return Optional.empty();
        }

        // Return if string should not be updated
        if (!updateString) {
            return Optional.of(result.getValue());
        }

        // Get resulting string
        // StringSIterator modifiedString = result.getModifiedSlice();

        // Trim string if needed
        // if (trimAfterApply) {
        // modifiedString = modifiedString.trim();
        // }

        // Update current string, preserving StringSlice state
        // currentString = currentString.setString(modifiedString);
        // currentString = currentString.set(modifiedString);
        // Update current string, preserving StringIterator state
        currentString = result.getModifiedSlice();

        return Optional.of(result.getValue());
    }

    /**
     * Similar to check, but throws exception if the rule does not match.
     * 
     * @param rule
     * @return
     */
    public <T> T parse(SplitRule<T> rule) {
        return check(rule)
                .orElseThrow(() -> new RuntimeException(
                        "Could not apply parsing rule over the string '" + currentString + "'"));
    }

    /**
     * Applies the rule over the current string. If the rule matches, returns the match and consumes the corresponding
     * string. Otherwise, returns an empty Optional and leaves the current string unchanged.
     * 
     * @param rule
     * @return
     */
    public <T> Optional<T> check(SplitRule<T> rule) {
        // Use check with a predicate that always returns true
        return check(rule, result -> true);
    }

    /**
     * Applies the given rule, and if it matches, checks if the results passes the predicate. The current string is only
     * consumed if both the rule and the predicate match.
     * 
     * @param rule
     * @param checker
     * @return
     */
    public <T> Optional<T> check(SplitRule<T> rule, Predicate<T> predicate) {
        return check(rule, predicate, true);
    }

    /**
     * Applies the rule over the current string, but does not consume the string even if the rule matches.
     * 
     * @param rule
     * @return
     */
    public <T> Optional<T> peek(SplitRule<T> rule) {
        return peek(rule, result -> true);
    }

    /**
     * Overload that accepts a Predicate.
     * 
     * @param rule
     * @param predicate
     * @return
     */
    public <T> Optional<T> peek(SplitRule<T> rule, Predicate<T> predicate) {
        return check(rule, predicate, false);
    }

    /**
     * Similar to 'check', but discards the result and returns if the value is present or not, consuming the
     * corresponding string.
     * 
     * @param rule
     * @param predicate
     * @return
     */
    public <T> boolean has(SplitRule<T> rule, Predicate<T> predicate) {
        return check(rule, predicate).isPresent();
    }

    public void setReverse(boolean reverse) {
        currentString = currentString.setReverse(reverse);
    }

    public void setSeparator(Predicate<Character> separator) {
        currentString = currentString.setSeparator(separator);
    }
}
