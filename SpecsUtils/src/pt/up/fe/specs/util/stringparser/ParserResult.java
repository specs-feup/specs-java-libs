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

import pt.up.fe.specs.util.utilities.StringSlice;

public record ParserResult<T>(StringSlice modifiedString, T result) {

    public static <T> ParserResult<Optional<T>> asOptional(ParserResult<T> parserResult) {
        return new ParserResult<>(parserResult.modifiedString(), Optional.ofNullable(parserResult.result()));
    }

}
