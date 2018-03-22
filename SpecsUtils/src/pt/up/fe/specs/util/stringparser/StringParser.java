/**
 * Copyright 2016 SPeCS.
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

package pt.up.fe.specs.util.stringparser;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import pt.up.fe.specs.util.utilities.StringSlice;

/**
 * Utility class for performing parsing over a String.
 *
 * <p>
 * Uses a mutable StringSlice to apply parsing rules over the string which can update it.
 *
 * @author JoaoBispo
 *
 */
public class StringParser {

    // private StringSlice currentString;
    private StringSplitter currentString;
    private final boolean trimAfterApply;

    public StringParser(String string) {
        this(new StringSplitter(new StringSlice(string)));
    }

    public StringParser(StringSplitter string) {
        this(string, true);
    }

    public StringParser(StringSplitter currentString, boolean trimAfterApply) {
        this.currentString = currentString.setTrim(trimAfterApply);
        this.trimAfterApply = trimAfterApply;
    }

    public StringSplitter getCurrentString() {
        return currentString;
    }

    public <T> T applyPrivate(ParserResult<T> result) {
        int originalLength = currentString.length();

        // currentString = currentString.setString(result.getModifiedString());
        currentString = currentString.set(result.getModifiedString());

        // Apply trim if there where modifications
        if (trimAfterApply && currentString.length() != originalLength) {
            currentString = currentString.trim();
        }

        return result.getResult();
    }

    public <T> T applyFunction(Function<StringParser, T> worker) {
        T result = worker.apply(this);
        ParserResult<T> parserResult = new ParserResult<T>(currentString, result);
        return applyPrivate(parserResult);
    }

    public <T> T apply(ParserWorker<T> worker) {
        ParserResult<T> result = worker.apply(currentString);

        return applyPrivate(result);
        /*
        int originalLength = currentString.length();
        currentString = result.getModifiedString();
        
        // Apply trim if there where modifications
        if (currentString.length() != originalLength) {
            currentString = currentString.trim();
        }
        
        return result.getResult();
        */
    }

    public <T, U> T apply(ParserWorkerWithParam<T, U> worker, U parameter) {
        ParserResult<T> result = worker.apply(currentString, parameter);
        return applyPrivate(result);
        /*
        currentString = result.getModifiedString();
        
        return result.getResult();
        */
    }

    public <T, U, V> T apply(ParserWorkerWithParam2<T, U, V> worker, U parameter1, V parameter2) {
        ParserResult<T> result = worker.apply(currentString, parameter1, parameter2);
        return applyPrivate(result);
        /*
        currentString = result.getModifiedString();
        
        return result.getResult();
        */
    }

    public <T, U, V, W> T apply(ParserWorkerWithParam3<T, U, V, W> worker, U parameter1, V parameter2, W parameter3) {
        ParserResult<T> result = worker.apply(currentString, parameter1, parameter2, parameter3);
        return applyPrivate(result);
        /*
        currentString = result.getModifiedString();
        
        return result.getResult();
        */
    }

    public <T, U, V, W, Y> T apply(ParserWorkerWithParam4<T, U, V, W, Y> worker, U parameter1, V parameter2,
            W parameter3, Y parameter4) {
        ParserResult<T> result = worker.apply(currentString, parameter1, parameter2, parameter3, parameter4);
        return applyPrivate(result);
    }

    public String substring(int beginIndex) {
        String consumedString = currentString.substring(0, beginIndex).toString();

        currentString = currentString.substring(beginIndex);

        return consumedString;
    }

    public String clear() {
        String consumedString = currentString.toString();
        // currentString = new StringSlice("");
        currentString = currentString.clear();
        return consumedString;
    }

    public void trim() {
        currentString = currentString.trim();
    }

    /**
     * Checks if the internal string is empty, after trimming. If it is not, throws an Exception.
     */
    public void checkEmpty() {
        if (currentString.trim().isEmpty()) {
            return;
        }

        throw new RuntimeException("StringParser not empty, internal string is '" + currentString + "'");
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
    private <T> Optional<T> check(ParserRule<T> rule, Predicate<T> predicate, boolean updateString) {
        ParserResult<T> result = rule.apply(currentString);

        // Check if there was a match
        if (result == null) {
            return Optional.empty();
        }

        // Test predicate
        if (!predicate.test(result.getResult())) {
            return Optional.empty();
        }

        // Return if string should not be updated
        if (!updateString) {
            return Optional.of(result.getResult());
        }

        // Get resulting string
        StringSlice modifiedString = result.getModifiedString();

        // Trim string if needed
        if (trimAfterApply) {
            modifiedString = modifiedString.trim();
        }

        // Update current string, preserving StringSlice state
        // currentString = currentString.setString(modifiedString);
        currentString = currentString.set(modifiedString);

        return Optional.of(result.getResult());
    }

    /**
     * Similar to check, but throws exception if the rule does not match.
     * 
     * @param rule
     * @return
     */
    public <T> T parse(ParserRule<T> rule) {
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
    public <T> Optional<T> check(ParserRule<T> rule) {
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
    public <T> Optional<T> check(ParserRule<T> rule, Predicate<T> predicate) {
        return check(rule, predicate, true);
    }

    /**
     * Applies the rule over the current string, but does not consume the string even if the rule matches.
     * 
     * @param rule
     * @return
     */
    public <T> Optional<T> peek(ParserRule<T> rule) {
        return peek(rule, result -> true);
    }

    /**
     * Overload that accepts a Predicate.
     * 
     * @param rule
     * @param predicate
     * @return
     */
    public <T> Optional<T> peek(ParserRule<T> rule, Predicate<T> predicate) {
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
    public <T> boolean has(ParserRule<T> rule, Predicate<T> predicate) {
        return check(rule, predicate).isPresent();
    }

    public void setReverse(boolean reverse) {
        currentString = currentString.setReverse(reverse);
    }

    public void setSeparator(Predicate<Character> separator) {
        currentString = currentString.setSeparator(separator);
    }

}
