/**
 * Copyright 2017 SPeCS.
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

package pt.up.fe.specs.util.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiOutputStream extends OutputStream {

    private final List<OutputStream> outputStreams;

    public MultiOutputStream(List<OutputStream> outputStreams) {
        this.outputStreams = outputStreams;
    }

    @Override
    public void write(int b) throws IOException {
        for (OutputStream stream : outputStreams) {
            stream.write(b);
        }
    }

    @Override
    public void close() throws IOException {
        List<IOException> exceptions = new ArrayList<>();

        for (OutputStream stream : outputStreams) {
            try {
                stream.close();
            } catch (IOException e) {
                exceptions.add(e);
            }
        }

        if (!exceptions.isEmpty()) {
            String exceptionsMessage = exceptions.stream()
                    .map(IOException::getMessage)
                    .collect(Collectors.joining("\n - ", " - ", ""));
            throw new IOException("An exception occurred in one or more streams:\n" + exceptionsMessage);
        }
    }

    @Override
    public void flush() throws IOException {
        List<IOException> exceptions = new ArrayList<>();

        for (OutputStream stream : outputStreams) {
            try {
                stream.flush();
            } catch (IOException e) {
                exceptions.add(e);
            }
        }

        if (!exceptions.isEmpty()) {
            String exceptionsMessage = exceptions.stream()
                    .map(IOException::getMessage)
                    .collect(Collectors.joining("\n - ", " - ", ""));
            throw new IOException("An exception occurred in one or more streams:\n" + exceptionsMessage);
        }
    }

}
