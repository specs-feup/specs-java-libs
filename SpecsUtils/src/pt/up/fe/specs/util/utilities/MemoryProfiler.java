/**
 * Copyright 2021 SPeCS.
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;

/**
 * Launches a thread that periodically calls the garbage collector and reads the
 * memory used after collection.
 * 
 * @author JBispo
 *
 */
public class MemoryProfiler {

    private final long period;
    private final TimeUnit timeUnit;
    private final File outputFile;

    public MemoryProfiler(long period, TimeUnit timeUnit, File outputFile) {
        this.period = period;
        this.timeUnit = timeUnit;
        this.outputFile = outputFile;
    }

    /**
     * Helper constructor, which measure memory every 500 milliseconds, to a file
     * "memory_profile.csv" in the current working directory.
     */
    public MemoryProfiler() {
        this(500, TimeUnit.MILLISECONDS, new File("memory_profile.csv"));
    }

    public void execute() {
        // Launch thread
        var threadExecutor = Executors.newSingleThreadExecutor();
        threadExecutor.execute(this::profile);
        threadExecutor.shutdown();
    }

    private void profile() {
        long totalMillis = TimeUnit.MILLISECONDS.convert(period, timeUnit);
        long totalNanos = TimeUnit.NANOSECONDS.convert(period, timeUnit);
        long totalNanosTruncated = totalMillis * 1_000_000l;
        long partialNanos = totalNanos - totalNanosTruncated;

        long totalTime = totalNanosTruncated + partialNanos;

        SpecsLogs.info("Profiling memory with a period of " + SpecsStrings.parseTime(totalTime));

        // Make sure file exists
        try {
            outputFile.createNewFile();
        } catch (Exception e) {
            SpecsLogs.info("Could not start memory profile, " + e.getMessage());
            return;
        }

        SpecsLogs.info("Writing memory profile to file '" + SpecsIo.getCanonicalPath(outputFile) + "'");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile, true),
                SpecsIo.DEFAULT_CHAR_SET))) {

            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        SpecsLogs.info("Memory profile failed, " + e.getMessage());
                    }
                }
            });

            while (true) {
                // Sleep
                try {
                    Thread.sleep(totalMillis, (int) partialNanos);
                } catch (InterruptedException e) {
                    SpecsLogs.info("Interrupting memory profile");
                    break;
                }

                // Get used memory, in Mb, calling the garbage collector before
                var usedMemory = SpecsSystem.getUsedMemoryMb(true);

                // Get timestamp
                var timestamp = ZonedDateTime.now(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("uuuu.MM.dd.HH.mm.ss.nnnnnnnnn"));

                // Log line
                var line = timestamp + "," + usedMemory + "\n";

                // Write to file
                writer.write(line, 0, line.length());
            }

        } catch (Exception e) {
            SpecsLogs.info("Interrupting memory profile, " + e.getMessage());
        }
    }
}
