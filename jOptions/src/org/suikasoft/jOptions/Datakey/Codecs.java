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

package org.suikasoft.jOptions.Datakey;

import java.io.File;
import java.util.function.Function;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.parsing.StringCodec;

public class Codecs {

    public static StringCodec<File> file() {
        // Function<File, String> encoder = f -> SpecsIo.normalizePath(f);
        Function<String, File> decoder = s -> s == null ? new File("") : new File(s);

        return StringCodec.newInstance(Codecs::fileEncoder, decoder);
    }

    private static String fileEncoder(File file) {
        System.out.println("ENCODING FILE:" + SpecsIo.normalizePath(file));
        return SpecsIo.normalizePath(file);
    }
}
