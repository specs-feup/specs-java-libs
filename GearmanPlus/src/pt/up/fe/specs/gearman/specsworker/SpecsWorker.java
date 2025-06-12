/**
 * Copyright 2013 SPeCS Research Group.
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

package pt.up.fe.specs.gearman.specsworker;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.gearman.GearmanFunction;
import org.gearman.GearmanFunctionCallback;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsStrings;

/**
 * Abstract base class for Gearman workers with timeout, setup/teardown, and error handling support.
 * <p>
 * Provides a framework for running Gearman jobs with configurable timeouts, setup/teardown hooks, and error reporting.
 * Subclasses should implement {@link #workInternal(String, byte[], GearmanFunctionCallback)} and
 * {@link #getErrorOutput(String)} for custom job logic and error output.
 *
 * @author Joao Bispo
 */
public abstract class SpecsWorker implements GearmanFunction {

    private final long timeout;
    private final TimeUnit timeUnit;

    /**
     * Constructs a SpecsWorker with the given timeout and time unit.
     *
     * @param timeout the timeout value for job execution
     * @param timeUnit the time unit for the timeout
     */
    public SpecsWorker(long timeout, TimeUnit timeUnit) {
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    /**
     * Returns the timeout value for job execution.
     *
     * @return the timeout value
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * Returns the time unit for the timeout value.
     *
     * @return the time unit
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * Returns the name of this worker. By default, returns the simple class name.
     *
     * @return the worker name
     */
    public String getWorkerName() {
        return getClass().getSimpleName();
    }

    /**
     * Executes the Gearman job, calling setup and teardown hooks.
     *
     * @param function the function name
     * @param data the input data
     * @param callback the Gearman callback
     * @return the result of the job
     * @throws Exception if job execution fails
     */
    @Override
    public byte[] work(String function, byte[] data, GearmanFunctionCallback callback)
            throws Exception {
        setUp();
        byte[] result = execute(function, data, callback);
        tearDown();
        return result;
    }

    /**
     * Executes the job with timeout and error handling, writing input/output to files if outputDir is set.
     *
     * @param function the function name
     * @param data the input data
     * @param callback the Gearman callback
     * @return the result of the job
     * @throws InterruptedException if execution is interrupted
     * @throws ExecutionException if execution fails
     */
    private byte[] execute(String function, byte[] data, GearmanFunctionCallback callback)
            throws InterruptedException, ExecutionException {
        File outputDir = getOutputDir();
        if (outputDir != null) {
            SpecsIo.write(new File(outputDir, "input_data.json"), new String(data));
        }
        byte[] result = executeInternal(function, data, callback);
        if (outputDir != null) {
            SpecsIo.write(new File(outputDir, "output_data.json"), new String(result));
        }
        return result;
    }

    /**
     * Executes the job in a separate thread, enforcing the timeout and handling exceptions.
     *
     * @param function the function name
     * @param data the input data
     * @param callback the Gearman callback
     * @return the result of the job
     */
    public byte[] executeInternal(String function, byte[] data, GearmanFunctionCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        TaskV2 task = new TaskV2(this, function, data, callback);
        Future<byte[]> future = executor.submit(task);
        byte[] result = null;
        try {
            String name = getWorkerName();
            String id = callback != null ? new String(callback.getJobHandle()) : "<No callback>";
            SpecsLogs.msgInfo("[SpecsWorker] Starting '" + name + "' (" + id + " -> "
                    + SpecsIo.getWorkingDir().getAbsolutePath() + ")");
            long workStart = System.nanoTime();
            result = future.get(timeout, timeUnit);
            long workEnd = System.nanoTime();
            SpecsLogs.msgInfo("[SpecsWorker] Finished '" + getWorkerName() + "', "
                    + SpecsStrings.parseTime(workEnd - workStart) + " (id " + id + ")");
        } catch (TimeoutException e) {
            SpecsLogs.warn("[SpecsWorker] Timeout during worker execution", e);
            future.cancel(true);
            SpecsLogs.msgInfo("Worker [" + Thread.currentThread().getName() + "]: putting thread/task to sleep... ");
            task.interrupt();
            result = getErrorOutput(getTimeoutMessage());
        } catch (Exception e) {
            SpecsLogs.warn("[SpecsWorker] Exception during worker execution", e);
            future.cancel(true);
            task.interrupt();
            result = getErrorOutput(e.getMessage());
        }
        executor.shutdownNow();
        return result;
    }

    /**
     * Returns a timeout message for error reporting.
     *
     * @return the timeout message
     */
    private String getTimeoutMessage() {
        return "Terminated task after exceeding the maximum alloted time of " + getTimeout()
                + SpecsStrings.getTimeUnitSymbol(getTimeUnit());
    }

    /**
     * Setup hook called before job execution. Default implementation does nothing.
     */
    public void setUp() {
        // No setup required by default
    }

    /**
     * Teardown hook called after job execution. Default implementation does nothing.
     */
    public void tearDown() {
        // No teardown required by default
    }

    /**
     * Performs the actual work for the Gearman job. Must be implemented by subclasses.
     *
     * @param function the function name
     * @param data the input data
     * @param callback the Gearman callback
     * @return the result of the job
     * @throws Exception if job execution fails
     */
    public abstract byte[] workInternal(String function, byte[] data,
            GearmanFunctionCallback callback) throws Exception;

    /**
     * Returns the error output for a given error message. Must be implemented by subclasses.
     *
     * @param message the error message
     * @return the error output as bytes
     */
    protected abstract byte[] getErrorOutput(String message);

    /**
     * Task for executing a Gearman job in a separate thread.
     */
    class Task implements Callable<byte[]> {
        private final String function;
        private final byte[] data;
        private final GearmanFunctionCallback callback;

        /**
         * Constructs a Task for the given function, data, and callback.
         *
         * @param function the function name
         * @param data the input data
         * @param callback the Gearman callback
         */
        public Task(String function, byte[] data, GearmanFunctionCallback callback) {
            this.function = function;
            this.data = data;
            this.callback = callback;
        }

        /**
         * Calls the workInternal method to perform the job.
         *
         * @return the result of the job
         * @throws Exception if job execution fails
         */
        @Override
        public byte[] call() throws Exception {
            return workInternal(function, data, callback);
        }
    }

    /**
     * TaskV2 for executing a Gearman job in a separate thread, with thread interruption support.
     */
    static class TaskV2 implements Callable<byte[]> {
        private final SpecsWorker worker;
        private final String function;
        private final byte[] data;
        private final GearmanFunctionCallback callback;
        private Thread taskThread = null;

        /**
         * Constructs a TaskV2 for the given worker, function, data, and callback.
         *
         * @param worker the SpecsWorker instance
         * @param function the function name
         * @param data the input data
         * @param callback the Gearman callback
         */
        public TaskV2(SpecsWorker worker, String function, byte[] data, GearmanFunctionCallback callback) {
            this.worker = worker;
            this.function = function;
            this.data = data;
            this.callback = callback;
        }

        /**
         * Calls the worker's workInternal method to perform the job, tracking the thread for interruption.
         *
         * @return the result of the job
         * @throws Exception if job execution fails
         */
        @Override
        public byte[] call() throws Exception {
            taskThread = Thread.currentThread();
            SpecsLogs.msgInfo("Running task in thread " + taskThread.getName());
            byte[] result = worker.workInternal(function, data, callback);
            SpecsLogs.msgInfo("Finished task in thread " + taskThread.getName());
            taskThread = null;
            return result;
        }

        /**
         * Returns the SpecsWorker associated with this task.
         *
         * @return the SpecsWorker instance
         */
        public SpecsWorker getWorker() {
            return worker;
        }

        /**
         * Interrupts the running thread for this task, waiting 2 seconds for cleanup.
         *
         * If the thread is still alive after interruption, logs a warning.
         */
        public void interrupt() {
            if (taskThread == null) {
                SpecsLogs.msgInfo("Task.sleep(): No thread set, returning");
                return;
            }
            SpecsLogs.msgInfo("Interrupting task in thread " + taskThread.getName() + ", waiting 2 seconds");
            taskThread.interrupt();
            try {
                Thread.sleep(2_000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // set interrupt flag
                SpecsLogs.warn("Thread was interrupted:\n", e);
            }

            // If thread is still alive, kill it forcefully
            if (taskThread.isAlive()) {
                SpecsLogs.msgInfo("Forcefully stopping the thread " + taskThread.getName());
                taskThread.stop();
                // Stopping two times due to experiment described here:
                // https://stackoverflow.com/questions/24855182/interrupt-java-thread-running-nashorn-script#
                taskThread.stop();
            } else {
                SpecsLogs.msgInfo("Thread " + taskThread.getName() + " died gracefully");
            }
        }
    }

    /**
     * Returns the output directory for input/output debug files. Default is null (no output).
     *
     * @return the output directory, or null if not set
     */
    public File getOutputDir() {
        return null;
    }
}
