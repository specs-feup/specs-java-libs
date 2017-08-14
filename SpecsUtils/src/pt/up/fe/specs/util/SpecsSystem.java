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

package pt.up.fe.specs.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.function.Function;
import java.util.logging.Logger;

import pt.up.fe.specs.util.properties.SpecsProperty;
import pt.up.fe.specs.util.system.OutputType;
import pt.up.fe.specs.util.system.ProcessOutput;
import pt.up.fe.specs.util.system.ProcessOutputAsString;
import pt.up.fe.specs.util.system.StreamToString;

/**
 * Utility methods related to system tasks.
 *
 * @author Joao Bispo
 */
public class SpecsSystem {

    /**
     * Helper method which receives the command and the working directory instead of the builder.
     *
     * @param command
     * @param workingDir
     * @param storeOutput
     * @param printOutput
     * @return
     */
    public static ProcessOutputAsString runProcess(List<String> command, File workingDir,
            boolean storeOutput, boolean printOutput) {

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);

        return runProcess(builder, storeOutput, printOutput);
        // return runProcess(command, workingDir, storeOutput, printOutput, builder);

    }

    /**
     * Helper method which receives the command instead of the builder, and launches the process in the current
     * directory.
     *
     * @param command
     * @param storeOutput
     * @param printOutput
     * @return
     */
    public static ProcessOutputAsString runProcess(List<String> command,
            boolean storeOutput, boolean printOutput) {

        return runProcess(command, SpecsIo.getWorkingDir(), storeOutput, printOutput);
    }

    /**
     * Launches a process for the given command, that runs on 'workingDir'.
     *
     * @param command
     * @param workingDir
     * @param storeOutput
     * @param printOutput
     * @param builder
     * @return
     */
    /*
    public static ProcessOutput runProcess(List<String> command, String workingDir,
        boolean storeOutput, boolean printOutput, ProcessBuilder builder) {
    
    builder.command(command);
    builder.directory(new File(workingDir));
    
    // return runProcess(builder, storeOutput, printOutput).get();
    return runProcess(builder, storeOutput, printOutput);
    }
     */

    /**
     * Launches the process characterized by 'builder'.
     *
     * <p>
     * If there is any problem with the process, throws an exception.
     *
     * @param builder
     * @param storeOutput
     * @param printOutput
     * @return
     */
    public static ProcessOutputAsString runProcess(ProcessBuilder builder, boolean storeOutput, boolean printOutput) {
        Function<InputStream, String> stdout = new StreamToString(printOutput, storeOutput, OutputType.StdOut);
        Function<InputStream, String> stderr = new StreamToString(printOutput, storeOutput, OutputType.StdErr);

        ProcessOutput<String, String> output = runProcess(builder, stdout, stderr);

        return new ProcessOutputAsString(output.getReturnValue(), output.getStdOut(), output.getStdErr());
    }

    /**
     * Helper method which receives the command instead of the builder, and launches the process in the current
     * directory.
     *
     * @param command
     * @param storeOutput
     * @param printOutput
     * @return
     */
    public static <O, E> ProcessOutput<O, E> runProcess(List<String> command,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor) {

        return runProcess(command, SpecsIo.getWorkingDir(), outputProcessor, errorProcessor);
    }

    /**
     * Helper method which receives the command and the working directory instead of the builder.
     *
     * @param command
     * @param workingDir
     * @param storeOutput
     * @param printOutput
     * @return
     */
    public static <O, E> ProcessOutput<O, E> runProcess(List<String> command, File workingDir,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor) {

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);

        return runProcess(builder, outputProcessor, errorProcessor);
    }

    /**
     * Launches the process characterized by 'builder'.
     *
     * <p>
     * If there is any problem with the process, throws an exception.
     *
     * @param builder
     * @param storeOutput
     * @param printOutput
     * @return
     */
    public static <O, E> ProcessOutput<O, E> runProcess(ProcessBuilder builder,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor) {

        String commandString = getCommandString(builder.command());
        SpecsLogs.msgLib("Launching Process: " + commandString);

        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Could not start process", e);
        }

        InputStream errorStream = process.getErrorStream();
        InputStream inputStream = process.getInputStream();

        try {

            // FunctionContainer<InputStream, O> outputContainer = new FunctionContainer<>(outputProcessor);
            // FunctionContainer<InputStream, E> errorContainer = new FunctionContainer<>(errorProcessor);

            ExecutorService stdoutThread = Executors.newSingleThreadExecutor();
            Future<O> outputFuture = stdoutThread.submit(() -> outputProcessor.apply(inputStream));

            ExecutorService stderrThread = Executors.newSingleThreadExecutor();
            Future<E> errorFuture = stderrThread.submit(() -> errorProcessor.apply(errorStream));

            // The ExecutorService objects are shutdown, as they will not
            // receive more tasks.
            stdoutThread.shutdown();
            stderrThread.shutdown();

            int returnValue = process.waitFor();

            // Wait 2 seconds
            // stderrThread.awaitTermination(2, TimeUnit.SECONDS);
            // stdoutThread.awaitTermination(2, TimeUnit.SECONDS);

            try {
                O output = outputFuture.get();
                E error = errorFuture.get();

                // Save output
                ProcessOutput<O, E> processOutput = new ProcessOutput<>(returnValue, output, error);

                return processOutput;

            } catch (ExecutionException e) {
                throw new RuntimeException("Exception while processing outputs of process:", e);
            }
            // outputContainer.getResult();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            // Process is destroyed, even if we 'interrupt' the thread.
            // Thread.currentThread().interrupt() only sets a flag.
            process.destroy();
        }

        throw new RuntimeException("Could not execute the process");
    }

    public static ThreadFactory getDaemonThreadFactory() {
        return r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        };
    }

    /**
     * Transforms a String List representing a command into a single String separated by spaces.
     *
     * @param command
     * @return
     */
    private static String getCommandString(List<String> command) {
        StringBuilder builder = new StringBuilder();

        builder.append(command.get(0));
        for (int i = 1; i < command.size(); i++) {
            builder.append(" ");
            builder.append(command.get(i));
        }

        return builder.toString();
    }

    /**
     *
     * @return the StackTraceElement of the previous method of the method calling this method
     */
    public static StackTraceElement getCallerMethod() {
        return getCallerMethod(3);
    }

    public static StackTraceElement getCallerMethod(int callerMethodIndex) {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        if (stackTraceElements.length <= callerMethodIndex) {
            Logger.getLogger(SpecsSystem.class.getName()).warning(
                    "StackTrace size (" + stackTraceElements.length + ") is to "
                            + "small. Should have at least " + (callerMethodIndex + 1)
                            + " elements. Returning null.");
            return null;
        }

        return stackTraceElements[callerMethodIndex];
    }

    /**
     *
     * @param aClass
     * @param anInterface
     * @return true if the given class implements the given interface. False otherwise.
     */
    public static boolean implementsInterface(Class<?> aClass, Class<?> anInterface) {
        // Build set with interfaces of the given class
        Class<?>[] array = aClass.getInterfaces();
        Set<Class<?>> interfaces = new HashSet<>(Arrays.asList(array));
        return interfaces.contains(anInterface);
    }

    /**
     * Method with standard initialization procedures for a Java SE program.
     *
     * <p>
     * Turns off Security Manager, for Java WebStart, and setups the logger. Additionally, looks for the file
     * 'suika.properties' on the running folder and applies its options.
     */
    public static void programStandardInit() {
        // Disable security manager for Web Start
        System.setSecurityManager(null);
        // Redirect output to the logger
        SpecsLogs.setupConsoleOnly();
        // Read the general bootstrap configuration for the application
        SpecsProperty.applyProperties();
        // Set the look and feel according to the system
        SpecsSwing.setSystemLookAndFeel();
        // Platform specific initialization
        // PlatformUtils.programPlatformInit();
    }

    /**
     *
     * @return the name of the class of the main thread, or null if could not find the main thread.
     */
    public static StackTraceElement[] getMainStackTrace() {
        String mainThread = "main";
        Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
        for (Thread thread : map.keySet()) {
            if (!thread.getName().equals(mainThread)) {
                continue;
            }

            return map.get(thread);
        }

        SpecsLogs.getLogger().warning("Could not find thread '" + mainThread + "'.");
        return null;
    }

    public static String getProgramName() {
        StackTraceElement[] stack = getMainStackTrace();
        if (stack == null) {
            SpecsLogs.getLogger().warning(
                    "Could not get stack of main thread. Returning empty string.");
            return "";
        }

        StackTraceElement main = stack[stack.length - 1];

        String programName = main.getClassName();
        int dotIndex = programName.lastIndexOf(".");
        if (dotIndex != -1) {
            programName = programName.substring(dotIndex + 1, programName.length());
        }

        return programName;
    }

    /**
     * Returns true if the class with the given name is available.
     *
     * <p>
     * Code taken from: <br>
     * http://www.rgagnon.com/javadetails/java-0422.html
     *
     * @param className
     * @return
     */
    public static boolean isAvailable(String className) {
        boolean isFound = false;
        try {
            Class.forName(className, false, null);
            isFound = true;
        } catch (ClassNotFoundException e) {
            isFound = false;
        }

        return isFound;
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Similar to 'runProcess', but returns an int with the exit code, instead of the ProcessOutput.
     *
     * <p>
     * Prints the output, but does not store it to a String.
     *
     * @param command
     * @param workingDir
     * @return
     */
    public static int run(List<String> command, File workingDir) {
        ProcessOutputAsString output = runProcess(command, workingDir, false, true);
        return output.getReturnValue();
    }

    public static void getNanoTime(Runnable runnable) {
        long time = System.nanoTime();
        runnable.run();
        time = System.nanoTime() - time;
        SpecsLogs.msgInfo("time elapsed: " + SpecsStrings.parseTime(time));

    }

    public static <T> T getNanoTime(Returnable<T> runnable) {
        long time = System.nanoTime();
        T t = runnable.run();
        time = System.nanoTime() - time;
        SpecsLogs.msgInfo("time elapsed: " + SpecsStrings.parseTime(time));

        return t;
    }

    interface Returnable<T> {
        T run();
    }

    public static long getUsedMemory(boolean callGc) {
        if (callGc) {
            System.gc();
        }

        System.gc();

        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    /**
     * Taken from here: http://www.inoneo.com/en/blog/9/java/get-the-jvm-peak-memory-usage
     */
    public static void printPeakMemoryUsage() {
        // Place this code just before the end of the program
        try {
            String memoryUsage = new String();
            List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
            for (MemoryPoolMXBean pool : pools) {
                MemoryUsage peak = pool.getPeakUsage();

                memoryUsage += String.format("Peak %s memory used: %s\n", pool.getName(),
                        SpecsStrings.parseSize(peak.getUsed()));
                // memoryUsage += String.format("Peak %s memory reserved: %,d%n", pool.getName(), peak.getCommitted());
            }

            // we print the result in the console
            SpecsLogs.msgInfo(memoryUsage);

        } catch (Throwable t) {
            SpecsLogs.msgWarn("Exception in agent", t);
        }
    }

    /**
     * Do-nothing function, for cases that accept Runnable and we do not want to do anything.
     */
    public static void emptyRunnable() {

    }

    /**
     * 
     * @param command
     * @param workingdir
     * @return true if the program worked, false if it could not be started
     */
    public static boolean isCommandAvailable(List<String> command, File workingdir) {
        try {
            // Try to launch program
            runProcess(command, workingdir, false, false);
            return true;
        } catch (Exception e) {
            // Check if cause was CreateProcess error=2
            Throwable currentThrowable = e;
            while (currentThrowable.getCause() != null) {
                currentThrowable = currentThrowable.getCause();
            }

            System.out.println("MESSAGE:" + currentThrowable.getMessage());

            return false;
        }

    }

    /**
     * Adds a path to the java.library.path property, and flushes the path cache so that subsequent System.load calls
     * can find it.
     * 
     * @param path
     *            The path to add
     */
    public static void addJavaLibraryPath(String path) {
        System.setProperty("java.library.path",
                System.getProperty("java.library.path") + File.pathSeparatorChar + path);
        Field sysPathsField;
        try {
            sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
            sysPathsField.setAccessible(true);
            sysPathsField.set(null, null);
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            // Not supposed to happen
            throw new RuntimeException(e);
        }
    }

    /**
     * Taken from
     * http://stackoverflow.com/questions/4748673/how-can-i-check-the-bitness-of-my-os-using-java-j2se-not-os-
     * arch/5940770#5940770
     * 
     * @return true if the system is 64-bit, false otherwise.
     */
    public static boolean is64Bit() {
        String arch = System.getenv("PROCESSOR_ARCHITECTURE");
        String wow64Arch = System.getenv("PROCESSOR_ARCHITEW6432");

        if (arch == null) {
            String osArch = System.getProperty("os.arch");

            if (osArch.endsWith("amd64")) {
                return true;
            } else if (osArch.equals("i386") || osArch.equals("x86")) {
                return false;
            } else {
                throw new RuntimeException("Could not determine the bitness of the operating system");
            }
        }

        String realArch = arch.endsWith("64")
                || wow64Arch != null && wow64Arch.endsWith("64")
                        ? "64" : "32";

        if (realArch.equals("32")) {
            return false;
        }

        return true;
    }

    /**
     * Cannot reliably get the return value from a JAR.
     *
     * This method checks if the last line of output is the error message defined in this class.
     *
     * @param output
     * @return
     */
    /*
    public static boolean noErrorOccurred(String output) {
    
    // Get last line, check if error
    List<String> lines = LineReader.readLines(output);
    boolean exceptionOccurred = lines.get(lines.size() - 1).equals(ProcessUtils.ERROR);
    
    if (!exceptionOccurred) {
        return true;
    } else {
        return false;
    }
    }
     */

    /*
    public static String getErrorString() {
    return "\n" + ERROR;
    }
     */

    /**
     * Lauches the weaver in another thread and waits termination.
     * 
     * @param args
     * @return
     */
    public static <T> T executeOnThreadAndWait(Callable<T> callable) {
        // Launch weaver in another thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(callable);
        executor.shutdown();
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SpecsLogs.msgInfo("Failed to complete execution on thread, returning null");
            return null;
        } catch (ExecutionException e) {
            throw new RuntimeException("Error while executing thread", e);
        }
    }

    public static int executeOnProcessAndWait(Class<?> aClass, String... args) {
        return executeOnProcessAndWait(aClass, SpecsIo.getWorkingDir(), Arrays.asList(args));
    }

    // public static int executeOnProcessAndWaitWithExec(Class<?> aClass, String javaExecutable, String... args) {
    // return executeOnProcessAndWaitWithExec(aClass, javaExecutable, Arrays.asList(args));
    // }

    /**
     * Taken from here: https://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process
     * 
     * @param aClass
     * @return
     */
    // public static int executeOnProcessAndWaitWithExec(Class<?> aClass, String javaExecutable, List<String> args) {
    // return executeOnProcessAndWait(aClass, SpecsIo.getWorkingDir(), args);
    // }

    // public static int executeOnProcessAndWait(Class<?> aClass, File workingDir,
    // List<String> args) {
    //
    // return executeOnProcessAndWaitWith(aClass, workingDir, args);
    //
    // }

    /**
     * Taken from here: https://stackoverflow.com/questions/636367/executing-a-java-application-in-a-separate-process
     * 
     * @param aClass
     * @param javaExecutable
     * @param workingDir
     * @param args
     * @return
     */
    public static int executeOnProcessAndWait(Class<?> aClass, File workingDir,
            List<String> args) {

        // ((URLClassLoader() Thread.currentThread().getContextClassLoader()).getURL();
        // Process.exec("java", "-classpath", urls.join(":"), CLASS_TO_BE_EXECUTED)
        String classpath = System.getProperty("java.class.path");
        String className = aClass.getCanonicalName();

        List<String> command = new ArrayList<>();
        command.addAll(Arrays.asList("java", "-cp", classpath, className));
        command.addAll(args);

        ProcessOutputAsString output = runProcess(command, workingDir, false, true);
        return output.getReturnValue();
        // ProcessBuilder builder = new ProcessBuilder("java", "-cp", classpath, className);
        // Process process;
        // try {
        // process = builder.start();
        // process.waitFor();
        // return process.exitValue();
        // } catch (IOException e) {
        // SpecsLogs.msgWarn("Exception which executing process:\n", e);
        // } catch (InterruptedException e) {
        // Thread.currentThread().interrupt();
        // SpecsLogs.msgInfo("Failed to complete execution on process");
        // }
        //
        // return -1;
    }

    // public static ProcessBuilder buildJavaProcess(Class<?> aClass, String javaExecutable, List<String> args) {
    // public static ProcessBuilder buildJavaProcess(Class<?> aClass, List<String> args) {
    // List<String> command = new ArrayList<>();
    // command.add("java");
    //
    // String classpath = System.getProperty("java.class.path");
    // String className = aClass.getCanonicalName();
    //
    // command.add("-cp");
    // command.add(classpath);
    // command.add(className);
    //
    // command.addAll(args);
    //
    // ProcessBuilder process = new ProcessBuilder(command);
    //
    // return process;
    // }
}
