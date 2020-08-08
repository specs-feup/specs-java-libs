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
 * specific language governing permissions and limitations under the License. under the License.
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
 * @author Joao Bispo
 * 
 */
public abstract class SpecsWorker implements GearmanFunction {

    private final long timeout;
    private final TimeUnit timeUnit;

    public SpecsWorker(long timeout, TimeUnit timeUnit) {

        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    // public SpecsWorker() {
    // this(SPeCSGearman.getTimeout(), SPeCSGearman.getTimeunit());
    // }

    /**
     * @return the timeout
     */
    public long getTimeout() {
        return timeout;
    }

    /**
     * @return the timeUnit
     */
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    /**
     * As default, returns the simple name of the class.
     * 
     * @return
     */
    public String getWorkerName() {
        return getClass().getSimpleName();
    }

    /* (non-Javadoc)
     * @see org.gearman.GearmanFunction#work(java.lang.String, byte[], org.gearman.GearmanFunctionCallback)
     */
    @Override
    public byte[] work(String function, byte[] data, GearmanFunctionCallback callback)
            throws Exception {

        // if (name.endsWith("WithTimeout")) {
        // name = name.substring(0, name.length() - "WithTimeout".length());
        // }

        // long executeStart = System.nanoTime();
        // SetUp
        setUp();
        // LoggingUtils.msgInfo(ParseUtils.takeTime("[" + name + "] Setup: ", tic));

        byte[] result = execute(function, data, callback);

        // LoggingUtils.msgInfo(ParseUtils.takeTime("[" + name + "] Execution: ", executeStart));

        // TearDown
        // tic = System.nanoTime();
        tearDown();

        // LoggingUtils.msgInfo(ParseUtils.takeTime("[" + name + "] Teardown: ", tic));
        // LoggingUtils.msgInfo(ParseUtils.takeTime("[" + name + "] Total: ", workStart));

        return result;
    }

    /**
     * @param function
     * @param data
     * @param callback
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private byte[] execute(String function, byte[] data, GearmanFunctionCallback callback)
            throws InterruptedException, ExecutionException {

        File outputDir = getOutputDir();

        // Write input data, for debug
        if (outputDir != null) {
            SpecsIo.write(new File(outputDir, "input_data.json"), new String(data));
        }

        byte[] result = executeInternal(function, data, callback);

        // Write output data, for debug
        if (outputDir != null) {
            SpecsIo.write(new File(outputDir, "output_data.json"), new String(result));
        }

        // Create timeout output
        // if (result == null) {
        // result = getErrorOutput();
        // }
        return result;
    }

    public byte[] executeInternal(String function, byte[] data, GearmanFunctionCallback callback) {
        // Launch work in another thread
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Future<byte[]> future = executor.submit(new Task(gearmanFunction, data, callback));
        TaskV2 task = new TaskV2(this, function, data, callback);
        // Future<byte[]> future = executor.submit(new Task(function, data, callback));
        Future<byte[]> future = executor.submit(task);
        byte[] result = null;
        try {
            String name = getWorkerName();
            String id = callback != null ? new String(callback.getJobHandle()) : "<No callback>";
            SpecsLogs.msgInfo("[SpecsWorker] Starting '" + name + "' (" + id + " -> "
                    + SpecsIo.getWorkingDir().getAbsolutePath() + ")");

            // LoggingUtils.msgLib("Started task with a timeout of " + timeout + " " + timeUnit);
            long workStart = System.nanoTime();
            result = future.get(timeout, timeUnit);
            long workEnd = System.nanoTime();
            // LoggingUtils.msgLib("Finished task in the alloted time");
            SpecsLogs.msgInfo("[SpecsWorker] Finished '" + getWorkerName() + "', "
                    + SpecsStrings.parseTime(workEnd - workStart) + " (id " + id + ")");
        } catch (TimeoutException e) {
            SpecsLogs.msgWarn("[SpecsWorker] Timeout during worker execution", e);
            // future.cancel(true);
            future.cancel(true);
            SpecsLogs.msgInfo("Worker [" + Thread.currentThread().getName() + "]: putting thread/task to sleep... ");
            task.interrupt();
            // executor.shutdownNow();
            // return getErrorOutput(getTimeoutMessage());
            result = getErrorOutput(getTimeoutMessage());
        } catch (Exception e) {
            SpecsLogs.msgWarn("[SpecsWorker] Exception during worker execution", e);
            future.cancel(true);
            task.interrupt();
            // executor.shutdownNow();
            // return getErrorOutput(e.getMessage());
            result = getErrorOutput(e.getMessage());
            // return getErrorOutput();
        }

        executor.shutdownNow();
        return result;
    }

    private String getTimeoutMessage() {
        return "Terminated task after exceeding the maximum alloted time of " + getTimeout()
                + SpecsStrings.getTimeUnitSymbol(getTimeUnit());
    }

    /**
     * Executes before the main work. Default implementation does nothing.
     */
    public void setUp() {

    }

    /*
    public void sleep() {
        try {
            SpecsLogs.msgInfo("Task [" + Thread.currentThread().getName() + "] Have been put to sleep... ");
            Thread.sleep(2000);
            SpecsLogs.msgInfo("Task [" + Thread.currentThread().getName() + "] Awake!");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // set interrupt flag
            SpecsLogs.msgWarn("Interrupted:\n", e);
        }
    }
    */

    /**
     * Executes after the main work. Default implementation does nothing.
     */
    public void tearDown() {

    }

    public abstract byte[] workInternal(String function, byte[] data,
            GearmanFunctionCallback callback) throws Exception;

    protected abstract byte[] getErrorOutput(String message);

    class Task implements Callable<byte[]> {

        private final String function;
        private final byte[] data;
        private final GearmanFunctionCallback callback;

        /**
         * @param gearmanFunction
         * @param data
         * @param callback
         */
        /*
        public Task(String gearmanFunction, byte[] data, GearmanFunctionCallback callback) {
        this.function = gearmanFunction;
        this.data = data;
        this.callback = callback;
        }
        */

        public Task(String function, byte[] data, GearmanFunctionCallback callback) {

            this.function = function;
            this.data = data;
            this.callback = callback;

        }

        @Override
        public byte[] call() throws Exception {
            // return workInternal(gearmanFunction, data, callback);
            return workInternal(function, data, callback);
        }

        // public static void sleep() {
        // Thread.sleep(2000);
        // }
    }

    static class TaskV2 implements Callable<byte[]> {

        private final SpecsWorker worker;
        private final String function;
        private final byte[] data;
        private final GearmanFunctionCallback callback;
        private Thread taskThread = null;

        /**
         * @param gearmanFunction
         * @param data
         * @param callback
         */
        /*
        public Task(String gearmanFunction, byte[] data, GearmanFunctionCallback callback) {
        this.function = gearmanFunction;
        this.data = data;
        this.callback = callback;
        }
        */

        public TaskV2(SpecsWorker worker, String function, byte[] data, GearmanFunctionCallback callback) {
            this.worker = worker;
            this.function = function;
            this.data = data;
            this.callback = callback;

        }

        @Override
        public byte[] call() throws Exception {
            taskThread = Thread.currentThread();
            SpecsLogs.msgInfo("Running task in thread " + taskThread.getName());
            byte[] result = worker.workInternal(function, data, callback);
            SpecsLogs.msgInfo("Finished task in thread " + taskThread.getName());
            taskThread = null;
            return result;
            // return workInternal(gearmanFunction, data, callback);
            // return workInternal(function, data, callback);
        }

        public SpecsWorker getWorker() {
            return worker;
        }

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
                SpecsLogs.msgWarn("Thread was interrupted:\n", e);
            }

            // If thread is still alive, kill it forcefully
            if (taskThread.isAlive()) {
                SpecsLogs.msgInfo("Forcefully stopping the thread " + taskThread.getName());
                taskThread.stop();
                // Stopping two times due to experiment described here:
                // https://stackoverflow.com/questions/24855182/interrupt-java-thread-running-nashorn-script#
                taskThread.stop();
            } else {
                SpecsLogs.msgInfo("Thread " + taskThread.getName() + "died gracefully");
            }

            /*
            try {
                SpecsLogs.msgInfo("Task in thread " + taskThread.getName() + " awake!");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // set interrupt flag
                SpecsLogs.msgWarn("Interrupted:\n", e);
            }
            */
        }

    }

    public File getOutputDir() {
        return null;
    }
}
