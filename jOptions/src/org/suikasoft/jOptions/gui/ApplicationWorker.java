/*
 * Copyright 2010 SPeCS Research Group.
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

package org.suikasoft.jOptions.gui;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.suikasoft.jOptions.Interfaces.DataStore;
import org.suikasoft.jOptions.gui.panels.app.ProgramPanel;

import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSwing;

/**
 * Launches an App object from the ProgramPanel, managing execution in a
 * separate thread.
 *
 * <p>
 * This class provides methods to execute an application asynchronously and
 * handle its lifecycle in the GUI.
 *
 * TODO: Extract Runnable ApplicationRunner from this class.
 *
 * @author Joao Bispo
 */
public class ApplicationWorker {

    /**
     * Constructs an ApplicationWorker for the given ProgramPanel.
     *
     * @param programPanel the ProgramPanel to use
     */
    public ApplicationWorker(ProgramPanel programPanel) {
        mainWindow = programPanel;
        workerExecutor = null;
    }

    /**
     * Executes the application in another thread.
     *
     * @param options the DataStore with options for execution
     */
    public void execute(DataStore options) {

        // Run
        ExecutorService monitor = Executors.newSingleThreadExecutor();
        monitor.submit(() -> runner(options));

    }

    /**
     * To be run on Monitor thread, so the GUI is not waiting for the result of
     * task.
     *
     * @param setup the DataStore setup
     */
    private void runner(DataStore setup) {
        // Disable buttons
        setButtons(false);

        // Create task
        Callable<Integer> task = getTask(setup);

        // Submit task
        workerExecutor = Executors.newSingleThreadExecutor();
        Future<Integer> future = workerExecutor.submit(task);

        // Check if task finishes
        Integer result = null;
        try {
            result = future.get();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt(); // ignore/reset
        } catch (ExecutionException ex) {
            if (!workerExecutor.isShutdown()) {
                showExceptionMessage(ex);
            }
        }

        if (result == null) {
            SpecsLogs.msgInfo("Application execution could not proceed.");
        } else if (result.compareTo(0) != 0) {
            SpecsLogs.msgInfo("*Application Stopped*");
            SpecsLogs.msgLib("Worker return value: " + result);
        }

        // Enable buttons again
        setButtons(true);

    }

    /**
     * Enables or disables buttons in the GUI.
     *
     * @param enable true to enable buttons, false to disable
     */
    private void setButtons(final boolean enable) {
        SpecsSwing.runOnSwing(() -> mainWindow.setButtonsEnable(enable));

    }

    /**
     * Builds a task out of the application.
     *
     * @param setup the DataStore setup
     * @return a Callable task for execution
     */
    private Callable<Integer> getTask(DataStore setup) {
        return () -> mainWindow.getApplication().getKernel().execute(setup);
    }

    /**
     * Shuts down the worker executor, stopping any running tasks.
     */
    public void shutdown() {
        if (workerExecutor == null) {
            SpecsLogs.warn("Application is not running.");
            return;
        }

        workerExecutor.shutdownNow();
    }

    /**
     * Displays an exception message in the logs.
     *
     * @param ex the ExecutionException to log
     */
    private static void showExceptionMessage(ExecutionException ex) {
        String prefix = " happened while executing the application";

        Throwable ourCause = ex.getCause();

        if (ourCause == null) {
            SpecsLogs.warn("\nAn Exception" + prefix + ", but could not get cause.");
        } else {
            SpecsLogs.msgInfo("");
            SpecsLogs.warn(ourCause.toString(), ourCause);
        }
    }

    private final ProgramPanel mainWindow;
    private ExecutorService workerExecutor;

}
