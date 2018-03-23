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

import java.util.function.Function;

import pt.up.fe.specs.util.stringsplitter.StringSliceWithSplit;
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

    private StringSlice currentString;
    private final boolean trimAfterApply;

    public StringParser(String string) {
        this(new StringSliceWithSplit(new StringSlice(string)));
    }

    public StringParser(StringSliceWithSplit string) {
        this(string, true);
    }

    public StringParser(StringSlice string) {
        this(new StringSliceWithSplit(string), true);
    }

    public StringParser(StringSliceWithSplit currentString, boolean trimAfterApply) {
        this.currentString = currentString.setTrim(trimAfterApply);
        this.trimAfterApply = trimAfterApply;
    }

    public StringSlice getCurrentString() {
        return currentString;
    }

    public <T> T applyPrivate(ParserResult<T> result) {
        int originalLength = currentString.length();

        // currentString = currentString.setString(result.getModifiedString());
        currentString = result.getModifiedString();

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

}
