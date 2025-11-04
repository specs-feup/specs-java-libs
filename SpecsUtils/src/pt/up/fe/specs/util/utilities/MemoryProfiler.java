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
 * specific language governing permissions and limitations under the License.
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
    
    // Lifecycle management
    private volatile boolean running = false;
    private Thread workerThread;

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

    public synchronized void execute() {
        // Backwards-compatible alias for start()
        start();
    }

    /**
     * Starts the memory profiling in a dedicated daemon thread. If the profiler is already running this call is a
     * no-op.
     */
    public synchronized void start() {
        if (running) {
            return; // already running
        }

        if (outputFile != null) {
            try {
                var parent = outputFile.getParentFile();
                if (parent == null || parent.exists()) {
                    boolean created = outputFile.createNewFile();
                    if (!created && !outputFile.exists()) {
                        SpecsLogs.info(
                                "Could not create memory profile output file before starting: "
                                        + SpecsIo.getCanonicalPath(outputFile));
                        return;
                    }
                }
            } catch (Exception e) {
                SpecsLogs.info("Could not create memory profile output file before starting: " + e.getMessage());
                return;
            }
        }

        running = true;
        workerThread = new Thread(this::profile, "MemoryProfiler");
        workerThread.setDaemon(true); // Do not prevent JVM shutdown
        workerThread.start();
    }

    /**
     * Stops the profiling thread, if it is running. This method is idempotent.
     */
    public synchronized void stop() {
        running = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    /**
     * Returns true if the profiling worker thread is currently alive.
     */
    public boolean isRunning() {
        return running && workerThread != null && workerThread.isAlive();
    }

    /**
     * Exposes the underlying worker thread mainly for testing purposes.
     */
    public Thread getWorkerThread() {
        return workerThread;
    }

    private void profile() {
        if (outputFile == null) {
            SpecsLogs.info("MemoryProfiler started with a null output file, aborting.");
            running = false;
            return;
        }

        long totalMillis = TimeUnit.MILLISECONDS.convert(period, timeUnit);
        long totalNanos = TimeUnit.NANOSECONDS.convert(period, timeUnit);
        long totalNanosTruncated = totalMillis * 1_000_000L;
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

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    writer.close();
                } catch (IOException e) {
                    SpecsLogs.info("Memory profile failed, " + e.getMessage());
                }
            }));

            while (running && !Thread.currentThread().isInterrupted()) {
                // Get used memory, in Mb, calling the garbage collector before
                var usedMemory = SpecsSystem.getUsedMemoryMb(true);

                // Get timestamp
                var timestamp = ZonedDateTime.now(ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("uuuu.MM.dd.HH.mm.ss.nnnnnnnnn"));

                // Log line
                var line = timestamp + "," + usedMemory + "\n";

                // Write to file
                writer.write(line, 0, line.length());

                // Ensure data is flushed so other threads can read it
                writer.flush();

                // Sleep
                try {
                    Thread.sleep(totalMillis, (int) partialNanos);
                } catch (InterruptedException e) {
                    // Respect interruption
                    SpecsLogs.info("Interrupting memory profile");
                    Thread.currentThread().interrupt();
                    break;
                }
            }

        } catch (Exception e) {
            SpecsLogs.info("Interrupting memory profile, " + e.getMessage());
        } finally {
            running = false;
        }
    }
}
