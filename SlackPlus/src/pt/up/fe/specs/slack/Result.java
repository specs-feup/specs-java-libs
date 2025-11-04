/**
 * Copyright 2023 SPeCS.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package pt.up.fe.specs.slack;

import java.util.NoSuchElementException;

public class Result<R, E> {

    private final boolean isOk;
    private final R result;
    private final E error;

    private Result(boolean isOk, R result, E error) {

        this.isOk = isOk;
        this.result = result;
        this.error = error;
    }

    public static <R, E> Result<R, E> ok(R result) {

        return new Result<R, E>(true, result, null);
    }

    public static <R, E> Result<R, E> err(E error) {

        return new Result<R, E>(false, null, error);
    }

    public R getResult() {
        return result;
    }

    public E getError() {
        return error;
    }

    public boolean isOk() {
        return isOk;
    }

    public boolean isErr() {
        return !isOk;
    }

    /**
     * Returns the result or, if it is an error, throws {@link NoSuchElementException}.
     * 
     * @return
     */
    public R orElseThrow() {

        if (isErr()) {
            throw new NoSuchElementException();
        }

        return result;
    }
}
