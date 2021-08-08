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
 * specific language governing permissions and limitations under the License. under the License.
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
import pt.up.fe.specs.util.utilities.SecurityManagerNoExit;

/**
 * Launches an App object from the ProgramPanel.
 *
 * TODO: Extract Runnable ApplicationRunner from this class.
 *
 * @author Joao Bispo
 */
public class ApplicationWorker {

    public ApplicationWorker(ProgramPanel programPanel) {
	mainWindow = programPanel;
	workerExecutor = null;
    }

    /**
     * Executes the application in another thread.
     * 
     * @param options
     */
    public void execute(DataStore options) {

	// Run
	ExecutorService monitor = Executors.newSingleThreadExecutor();
	monitor.submit(() -> runner(options));

    }

    /**
     * To be run on Monitor thread, so the Gui is not waiting for the result of task.
     *
     * @param options
     */
    private void runner(DataStore setup) {
	// Disable buttons
	setButtons(false);

	// Save SecurityManager
	SecurityManager previousManager = System.getSecurityManager();
	// Set SecurityManager that catches System.exit() calls
	System.setSecurityManager(new SecurityManagerNoExit());

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

	// Restore SecurityManager
	System.setSecurityManager(previousManager);

	if (result == null) {
	    SpecsLogs.msgInfo("Application execution could not proceed.");
	    // LoggingUtils.getLogger().
	    // info("Cancelled application.");
	    // info("Application was cancelled.");
	} else if (result.compareTo(0) != 0) {
	    SpecsLogs.msgInfo("*Application Stopped*");
	    SpecsLogs.msgLib("Worker return value: " + result);
	    // LoggingUtils.getLogger().
	    // info("Application returned non-zero value:" + result);
	}

	// Enable buttons again
	setButtons(true);

    }

    private void setButtons(final boolean enable) {
	SpecsSwing.runOnSwing(new Runnable() {

	    @Override
	    public void run() {
		mainWindow.setButtonsEnable(enable);
	    }
	});

    }

    /**
     * Builds a task out of the application
     * 
     * @return
     */
    private Callable<Integer> getTask(DataStore setup) {
	return () -> mainWindow.getApplication().getKernel().execute(setup);
    }

    public void shutdown() {
	if (workerExecutor == null) {
	    SpecsLogs.getLogger().warning("Application is not running.");
	    return;
	}

	workerExecutor.shutdownNow();
    }

    private static void showExceptionMessage(ExecutionException ex) {
	String prefix = " happend while executing the application";

	Throwable ourCause = ex.getCause();
	// String prefix = ourCause.toString() +

	if (ourCause == null) {
	    // LoggingUtils.getLogger().
	    // info("\nAn Exception" + prefix + ", but could not get cause.");
	    SpecsLogs.warn("\nAn Exception" + prefix + ", but could not get cause.");
	} else {
	    // LoggingUtils.msgInfo("\n"+ourCause.toString());
	    SpecsLogs.msgInfo("");
	    // LoggingUtils.msgInfo(ourCause.getMessage());
	    SpecsLogs.warn(ourCause.toString(), ourCause);
	    /*
	    LoggingUtils.msgInfo("\nPrinting the stack trace:");
	         //info("\n"+ourCause.toString() + prefix + ". Printing the stack trace:\n");
	    StackTraceElement[] trace = ourCause.getStackTrace();
	    //LoggingUtils.getLogger().
	    //        info(ourCause.toString());
	    for (int i = 0; i < trace.length; i++) {
	    LoggingUtils.getLogger().
	            info("\tat " + trace[i]);
	    }
	    */
	}
    }

    private final ProgramPanel mainWindow;
    private ExecutorService workerExecutor;

}
