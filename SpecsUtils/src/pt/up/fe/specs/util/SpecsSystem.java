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
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Logger;

import pt.up.fe.specs.util.classmap.ClassSet;
import pt.up.fe.specs.util.lazy.Lazy;
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

    private static final ClassSet<Object> IMMUTABLE_JAVA_CLASSES = ClassSet.newInstance(Boolean.class, String.class,
            Number.class, Class.class);

    private static final Lazy<Boolean> IS_DEBUG = Lazy.newInstance(() -> new File("debug").isFile());

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

    public static ProcessOutputAsString runProcess(List<String> command, File workingDir,
            boolean storeOutput, boolean printOutput, Long timeoutNanos) {

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);
        Function<InputStream, String> stdout = new StreamToString(printOutput, storeOutput, OutputType.StdOut);
        Function<InputStream, String> stderr = new StreamToString(printOutput, storeOutput, OutputType.StdErr);

        ProcessOutput<String, String> output = runProcess(builder, stdout, stderr, timeoutNanos);

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

    /*
    public static ProcessOutputAsString runProcess(Process process, boolean storeOutput, boolean printOutput) {
        Function<InputStream, String> stdout = new StreamToString(printOutput, storeOutput, OutputType.StdOut);
        Function<InputStream, String> stderr = new StreamToString(printOutput, storeOutput, OutputType.StdErr);
    
        ProcessOutput<String, String> output = runProcess(process, stdout, stderr, null);
    
        return new ProcessOutputAsString(output.getReturnValue(), output.getStdOut(), output.getStdErr());
    }
    */

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
        return runProcess(builder, outputProcessor, errorProcessor, null);
    }

    /*
    public static <O, E> ProcessOutput<O, E> runProcess(ProcessBuilder builder,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor, Long timeoutNanos) {
    
        String commandString = getCommandString(builder.command());
        SpecsLogs.msgLib("Launching Process: " + commandString);
    
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            throw new RuntimeException("Could not start process", e);
        }
    
        return runProcess(process, outputProcessor, errorProcessor, timeoutNanos);
    }
    
    public static <O, E> ProcessOutput<O, E> runProcess(Process process,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor, Long timeoutNanos) {
    */
    public static <O, E> ProcessOutput<O, E> runProcess(ProcessBuilder builder,
            Function<InputStream, O> outputProcessor, Function<InputStream, E> errorProcessor, Long timeoutNanos) {

        String commandString = getCommandString(builder.command());
        SpecsLogs.msgLib("Launching Process: " + commandString);

        Process process = null;
        try {
            // Experiment: Calling Garbage Collector before starting process in order to reduce memory required to fork
            // VM
            // http://www.bryanmarty.com/2012/01/14/forking-jvm/
            long totalMemBefore = Runtime.getRuntime().totalMemory();
            System.gc();
            long totalMemAfter = Runtime.getRuntime().totalMemory();
            SpecsLogs.msgLib("Preparing to run process, memory before -> after GC: "
                    + SpecsStrings.parseSize(totalMemBefore) + " -> " + SpecsStrings.parseSize(totalMemAfter));
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

            int returnValue = executeProcess(process, timeoutNanos);

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

    private static int executeProcess(Process process, Long timeoutNanos) {
        try {
            // System.out.println("EXECUTE PROCESS TIMEOUT:" + timeoutNanos + "ns");

            if (timeoutNanos == null) {
                return process.waitFor();
            }

            boolean processExited = process.waitFor(timeoutNanos, TimeUnit.NANOSECONDS);
            // System.out.println("PROCESS EXITED: " + processExited);
            // System.out.println("PROCESS EXIT VALUE: " + process.exitValue());
            if (processExited) {
                return process.exitValue();
            }

            SpecsLogs.msgLib("SpecsSystem.executeProcess: Killing process...");
            process.destroyForcibly();
            SpecsLogs.msgLib("SpecsSystem.executeProcess: Waiting killing...");
            boolean processDestroyed = process.waitFor(1, TimeUnit.SECONDS);
            if (processDestroyed) {
                SpecsLogs.msgLib("SpecsSystem.executeProcess: Destroyed");
            } else {
                SpecsLogs.msgInfo("SpecsSystem.executeProcess: Could not destroy process!");
            }

            return -1;

            // SpecsLogs.msgInfo("Process timed out v2");
            // return -1;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            // SpecsLogs.msgInfo("Process timed out");
            return -1;
        }
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
        fixes();

        // Disable security manager for Web Start
        // System.setSecurityManager(null);
        // Redirect output to the logger
        SpecsLogs.setupConsoleOnly();
        // Read the general bootstrap configuration for the application
        SpecsProperty.applyProperties();
        // Set the look and feel according to the system
        SpecsSwing.setSystemLookAndFeel();
        // Platform specific initialization
        // PlatformUtils.programPlatformInit();

        // Debug information.
        SpecsLogs.debug("Current platform name: " + System.getProperty("os.name"));
        SpecsLogs.debug("Current platform version: " + System.getProperty("os.version"));

    }

    private static void fixes() {
        // To avoid illegal reflective accesses in Java 10 while library is not upgraded
        // https://stackoverflow.com/questions/33255578/old-jaxb-and-jdk8-metaspace-outofmemory-issue
        System.getProperties().setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
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
                        ? "64"
                        : "32";

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
     * Launches the callable in another thread and waits termination.
     * 
     * @param args
     * @return
     */
    public static <T> T executeOnThreadAndWait(Callable<T> callable) {
        // Launch weaver in another thread
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<T> future = executor.submit(callable);
        executor.shutdown();

        return get(future);
        /*
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SpecsLogs.msgInfo("Failed to complete execution on thread, returning null");
            return null;
        } catch (ExecutionException e) {
            // Rethrow cause
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
        
            throw new RuntimeException(e.getCause());
            // throw new RuntimeException("Error while executing thread", e);
        }
        */
    }

    public static <T> T get(Future<T> future) {
        try {
            return future.get();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            SpecsLogs.msgInfo("Failed to complete execution on thread, returning null");
            return null;
        } catch (ExecutionException e) {
            // Rethrow cause
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }

            throw new RuntimeException(e.getCause());
            // throw new RuntimeException("Error while executing thread", e);
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

        // File jarPath = SpecsIo.getJarPath(aClass).orElseThrow(
        // () -> new RuntimeException("Could not locate the JAR file for the class '" + aClass + "'"));
        // ((URLClassLoader() Thread.currentThread().getContextClassLoader()).getURL();
        // Process.exec("java", "-classpath", urls.join(":"), CLASS_TO_BE_EXECUTED)

        String classpath = System.getProperty("java.class.path");

        // System.out.println("CLASSPATH:" + classpath);

        String className = aClass.getCanonicalName();
        // String javaHome = "C:/Program Files/Java/jdk1.8.0_131/jre";
        List<String> command = new ArrayList<>();
        command.addAll(
                Arrays.asList("java", "-cp", classpath, className));
        // command.addAll(
        // Arrays.asList("java", "\"-Djava.home=" + javaHome + "\"", "-cp", classpath, className));
        // Arrays.asList("cmd", "/c", "java", "\"-Djava.home=" + javaHome + "\"", "-cp", classpath, className));
        // command.addAll(Arrays.asList("java", "-cp", "\"" + jarPath.getAbsolutePath() + "\"", className));
        command.addAll(args);

        ProcessBuilder process = new ProcessBuilder(command);
        process.directory(workingDir);

        // Set java home
        // System.setProperty("java.home", javaHome);
        // System.out.println("JAVA HOME:" + System.getProperty("java.home"));
        // System.out.println("JAVA HOME BEFORE:" + System.getenv().get("JAVA_HOME"));
        // System.getenv().put("JAVA_HOME", javaHome);
        // System.out.println("JAVA HOME AFTER:" + System.getenv().get("JAVA_HOME"));
        // process.environment().put("JAVA_HOME", javaHome);

        ProcessOutputAsString output = runProcess(process, false, true);
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

    /**
     * Based on this: https://stackoverflow.com/a/2591122/1189808
     * 
     * @return
     */
    public static double getJavaVersionNumber() {
        String version = System.getProperty("java.version");
        int pos = version.lastIndexOf('.');
        // pos = version.indexOf('.', pos + 1);
        return Double.parseDouble(version.substring(0, pos));
    }

    /***** Methods for dynamically extending the classpath *****/
    /***** Taken from https://stackoverflow.com/a/42052857/1189808 *****/
    private static class SpclClassLoader extends URLClassLoader {
        static {
            ClassLoader.registerAsParallelCapable();
        }

        private final Set<Path> userLibPaths = new CopyOnWriteArraySet<>();

        private SpclClassLoader() {
            super(new URL[0]);
        }

        @Override
        protected void addURL(URL url) {
            super.addURL(url);
        }

        protected void addLibPath(String newpath) {
            userLibPaths.add(Paths.get(newpath).toAbsolutePath());
        }

        @Override
        protected String findLibrary(String libname) {
            String nativeName = System.mapLibraryName(libname);
            return userLibPaths.stream().map(tpath -> tpath.resolve(nativeName)).filter(Files::exists)
                    .map(Path::toString).findFirst().orElse(super.findLibrary(libname));
        }
    }

    private final static SpclClassLoader ucl = new SpclClassLoader();

    /**
     * Adds a jar file or directory to the classpath. From Utils4J.
     *
     * @param newpaths
     *            JAR filename(s) or directory(s) to add
     * @return URLClassLoader after newpaths added if newpaths != null
     */
    public static ClassLoader addToClasspath(String... newpaths) {
        if (newpaths != null)
            try {
                for (String newpath : newpaths)
                    if (newpath != null && !newpath.trim().isEmpty())
                        ucl.addURL(Paths.get(newpath.trim()).toUri().toURL());
            } catch (IllegalArgumentException | MalformedURLException e) {
                RuntimeException re = new RuntimeException(e);
                re.setStackTrace(e.getStackTrace());
                throw re;
            }
        return ucl;
    }

    /**
     * Adds to library path in ClassLoader returned by addToClassPath
     *
     * @param newpaths
     *            Path(s) to directory(s) holding OS library files
     */
    public static void addToLibraryPath(String... newpaths) {
        for (String newpath : Objects.requireNonNull(newpaths))
            ucl.addLibPath(newpath);
    }

    /***** ENDS methods for dynamically extending the classpath *****/

    public static boolean isDebug() {
        return IS_DEBUG.get();
    }

    /*
    // public static <T> boolean hasCopyConstructor(T object) {
    public static boolean hasCopyConstructor(Object object) {
        // Class<T> aClass = (Class<T>) object.getClass();
    
        // Constructor<T> constructorMethod = null;
        for (Constructor<?> constructor : object.getClass().getConstructors()) {
            Class<?>[] constructorParams = constructor.getParameterTypes();
    
            if (constructorParams.length != 1) {
                continue;
            }
    
            if (object.getClass().isAssignableFrom(constructorParams[0])) {
                return true;
            }
        }
    
        return false;
        // Create copy constructor: new T(T data)
        // constructorMethod = aClass.getConstructor(aClass);
    }
    */

    /**
     * Uses the copy constructor to create a copy of the given object. Throws exception if the class does not have a
     * copy constructor.
     * 
     * @param object
     * @return
     */
    public static <T> T copy(T object) {

        if (object == null) {
            return null;
        }

        // Check if part of Immutable objects of java library
        if (IMMUTABLE_JAVA_CLASSES.contains(object)) {
            return object;
        }

        // Get class
        @SuppressWarnings("unchecked")
        Class<T> aClass = (Class<T>) object.getClass();

        Constructor<T> constructorMethod = null;
        try {
            // Create copy constructor: new T(T data)
            constructorMethod = aClass.getConstructor(aClass);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not create call to copy constructor for '" + aClass.getSimpleName()
                            + "'. Check if class contains a constructor of the form 'new T(T object)'.",
                    e);
        }

        // Invoke constructor
        try {
            return constructorMethod.newInstance(object);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not call constructor for object '" + object.getClass().getSimpleName() + "'", e);
        }

    }

    public static <T> T newInstance(String classname, Class<T> expectedClass, Object... arguments) {
        Object object = newInstance(classname, arguments);

        return expectedClass.cast(object);
    }

    public static Object newInstance(String classname, Object... arguments) {
        Class<?> aClass;
        try {
            aClass = Class.forName(classname);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Could not find class '" + classname + "'", e);
        }

        return newInstance(aClass, arguments);
    }

    public static <T> T newInstance(Class<T> aClass, Object... arguments) {
        Class<?>[] argClasses = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            argClasses[i] = arguments[i].getClass();
        }

        Constructor<T> constructorMethod = null;
        try {
            // Create copy constructor: new T(T data)
            constructorMethod = aClass.getConstructor(argClasses);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Could not create call to constructor for '" + aClass.getSimpleName()
                            + "' with arguments '" + Arrays.toString(argClasses) + "'",
                    e);
        }

        try {
            return constructorMethod.newInstance(arguments);
        } catch (Exception e) {
            throw new RuntimeException("Could not create new instance for '" + aClass.getSimpleName()
                    + "' with arguments " + Arrays.toString(arguments), e);
        }

    }

    /**
     * Taken from here:
     * https://stackoverflow.com/questions/9797212/finding-the-nearest-common-superclass-or-superinterface-of-a-collection-of-cla#9797689
     * 
     * @param clazz
     * @return
     */
    private static Set<Class<?>> getClassesBfs(Class<?> clazz) {
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        Set<Class<?>> nextLevel = new LinkedHashSet<Class<?>>();
        nextLevel.add(clazz);
        do {
            classes.addAll(nextLevel);
            Set<Class<?>> thisLevel = new LinkedHashSet<Class<?>>(nextLevel);
            nextLevel.clear();
            for (Class<?> each : thisLevel) {
                Class<?> superClass = each.getSuperclass();
                if (superClass != null && superClass != Object.class) {
                    nextLevel.add(superClass);
                }
                for (Class<?> eachInt : each.getInterfaces()) {
                    nextLevel.add(eachInt);
                }
            }
        } while (!nextLevel.isEmpty());
        return classes;
    }

    /**
     * Taken from here:
     * https://stackoverflow.com/questions/9797212/finding-the-nearest-common-superclass-or-superinterface-of-a-collection-of-cla#9797689
     * 
     * @param classes
     * @return
     */
    public static List<Class<?>> getCommonSuperClasses(Class<?>... classes) {
        return getCommonSuperClasses(Arrays.asList(classes));
    }

    /**
     * 
     * @param classes
     * @return
     */
    public static List<Class<?>> getCommonSuperClasses(List<Class<?>> classes) {
        // start off with set from first hierarchy
        Set<Class<?>> rollingIntersect = new LinkedHashSet<Class<?>>(
                getClassesBfs(classes.get(0)));
        // intersect with next
        for (int i = 1; i < classes.size(); i++) {
            rollingIntersect.retainAll(getClassesBfs(classes.get(i)));
        }
        return new LinkedList<Class<?>>(rollingIntersect);
    }

}
